package com.micro4blog.server;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieSyncManager;

import com.micro4blog.data.Micro4blogInfo;
import com.micro4blog.data.UserInfo;
import com.micro4blog.dialog.DialogError;
import com.micro4blog.dialog.Micro4blogDialogListener;
import com.micro4blog.http.ApiTokenHeader;
import com.micro4blog.http.Micro4blogParameters;
import com.micro4blog.http.Utility;
import com.micro4blog.oauth.Micro4blog;
import com.micro4blog.utils.Micro4blogException;

public class Micro4blogForSina extends Micro4blog {
	
	private static final String TAG = "Micro4blogForSina";

	private static Micro4blogForSina m4bSina;
	
	public Micro4blogForSina() {
		super();
	}
	
	public static Micro4blogForSina getInstance() {
		if (m4bSina == null) {
			m4bSina = new Micro4blogForSina();
		}
		apiParameters = new Micro4blogParameters();
		apiHeader = new ApiTokenHeader();
		return m4bSina;
	}
	
	@Override
	protected void initConfig() {
		
		setAppKey("3198633271");
		setAppSecret("fa2ced3df9f410d8bef7796f93fa81c0");
		
		setRedirectUrl("http://github.com/thomsen/Micro4blog");
				
		setUrlAccessToken("https://api.weibo.com/oauth2/access_token");
		setUrlAccessAuthorize("https://api.weibo.com/oauth2/authorize");
		
		setServerUrl("https://api.weibo.com/2/");
	
	}
	
	protected void authorize(Activity activity, String[] permissions, int activityCode,
            final Micro4blogDialogListener listener) {
        
        
        // XXX 官方微博客户端式的直接登录

//        boolean singleSignOnStarted = false;
        
        mAuthDialogListener = listener;
        
        // Prefer single sign-on, where available.
//        if (activityCode >= 0) {
//            singleSignOnStarted = startSingleSignOn(activity, getAppKey(), permissions, activityCode);
//            
//        }
        // Otherwise fall back to traditional dialog.
//        if (!singleSignOnStarted) {
//            startDialogAuth(activity, permissions);
//        }
        
        startDialogAuth(activity, permissions);
        

    }
    
    protected void startDialogAuth(Activity activity, String[] permissions) {
        Micro4blogParameters params = new Micro4blogParameters();
        if (permissions.length > 0) {
            params.add("scope", TextUtils.join(",", permissions));
        }
        
        CookieSyncManager.createInstance(activity);
        dialog(activity, params, new Micro4blogDialogListener() {

            public void onComplete(Bundle values) {
                // ensure any cookies set by the dialog are saved
                CookieSyncManager.getInstance().sync();
                
                // oauth2.0 第三步
               getUserAccessToken(values);
                              
            }

            public void onError(DialogError error) {
                Log.d(TAG, "Login failed: " + error);
                mAuthDialogListener.onError(error);
            }

            public void onMicro4blogException(Micro4blogException error) {
                Log.d(TAG, "Login failed: " + error);
                mAuthDialogListener.onMicro4blogException(error);
            }

            public void onCancel() {
                Log.d(TAG, "Login canceled");
                mAuthDialogListener.onCancel();
            }

        });
    }


    public void dialog(Context context, Micro4blogParameters parameters,
            final Micro4blogDialogListener listener) {
	
    	// OAuth2.0 1，2步，授权
    	parameters.add("client_id", getAppKey());
		parameters.add("response_type", "token");
		parameters.add("redirect_uri", getRedirectUrl());
		parameters.add("display", "mobile");
		
    	getAuthorization(context, parameters, listener);
    }


	@Override
	protected void authorizeCallBack(int requestCode, int resultCode,
			Intent data) {
		
	}

	@Override
	public String getHomeTimeline(Context context) {
		
		serverUrl = getServerUrl() + "statuses/home_timeline.json";
		apiParameters.add("count", "20");
		serverResult = request(context, serverUrl, apiParameters, Utility.HTTPMETHOD_GET, accessToken);
		
		return serverResult;
	}

	@Override
	public ArrayList<Micro4blogInfo> parseHomeTimeline(String message) {
		ArrayList<Micro4blogInfo> m4bInfoList = new ArrayList<Micro4blogInfo>();
		if(message == null) {
			return m4bInfoList;
		}
		
		try {
			JSONObject jsonObject = new JSONObject(message);
			JSONArray m4bArray = jsonObject.getJSONArray("statuses");
			JSONObject m4bObject;
			JSONObject userObject;
			Micro4blogInfo m4bInfo;
			UserInfo userInfo;
			for (int i=0; i<m4bArray.length(); i++) {
				m4bObject = (JSONObject) m4bArray.get(i);

				userInfo = new UserInfo();
				m4bInfo = new Micro4blogInfo();
				
				m4bInfo.setM4bCreateAt(m4bObject.getString("created_at"));
				m4bInfo.setM4bId(m4bObject.getInt("id"));
				m4bInfo.setM4bText(m4bObject.getString("text"));
				m4bInfo.setM44Source(m4bObject.getString("source"));
				m4bInfo.setM4bFovorited(m4bObject.getBoolean("favorited"));
				m4bInfo.setM4bTruncated(m4bObject.getBoolean("truncated"));
//				m4bInfo.setM4bInReplyToStatusId(m4bObject.getInt("in_replay_to_status_id"));
//				m4bInfo.setM4bInReplyToUserId(m4bObject.getInt("in_replay_to_user_id"));
//				m4bInfo.setM4bInReplyToScreenName(m4bObject.getString("in_reply_to_screen_name"));
				m4bInfo.setM4bMid(m4bObject.getInt("mid"));
//				m4bInfo.setM4bMiddlePicture(m4bObject.getString("bmiddle_pic"));
//				m4bInfo.setM4bOriginPicture(m4bObject.getString("original_pic"));
//				m4bInfo.setM4bThumbnailPic(m4bObject.getString("thumbnail_pic"));
				m4bInfo.setM4bForwardingCount(m4bObject.getInt("reposts_count"));
				m4bInfo.setM4bCommentCount(m4bObject.getInt("comments_count"));				
				
				userObject = m4bObject.getJSONObject("user");
			
				userInfo.setUserId(userObject.getLong("id"));
				userInfo.setUserName(userObject.getString("name"));
				userInfo.setUserName(userObject.getString("screen_name"));
				userInfo.setProvince(userObject.getInt("province"));
				userInfo.setCity(userObject.getInt("city"));
				userInfo.setLocation(userObject.getString("location"));
				userInfo.setDescription(userObject.getString("description"));
				userInfo.setBlogUrl(userObject.getString("url"));
				userInfo.setProfileImageUrl(userObject.getString("profile_image_url"));
				userInfo.setDomain(userObject.getString("domain"));
				userInfo.setGender(userObject.getString("gender"));
				userInfo.setFollowersCount(userObject.getInt("followers_count"));
				userInfo.setFriendsCount(userObject.getInt("friends_count"));
				userInfo.setM4bCount(userObject.getInt("statuses_count"));
				userInfo.setFavouritesCount(userObject.getInt("statuses_count"));
				userInfo.setCreateAt(userObject.getString("created_at"));
				userInfo.setFollowing(userObject.getBoolean("following"));
				userInfo.setAllowAllActMsg(userObject.getBoolean("allow_all_act_msg"));
				userInfo.setGeoEnabled(userObject.getBoolean("geo_enabled"));
				userInfo.setVerified(userObject.getBoolean("verified"));
				userInfo.setAllowAllComment(userObject.getBoolean("allow_all_comment"));
				userInfo.setAvatarLarge(userObject.getString("avatar_large"));
				userInfo.setVerifiedReason(userObject.getString("verified_reason"));
				userInfo.setFollowMe(userObject.getBoolean("follow_me"));
				userInfo.setOnlineStatus(userObject.getInt("online_status"));
				userInfo.setBiFollowersCount(userObject.getInt("bi_followers_count"));
								
				m4bInfo.setUserInfo(userInfo);
				
				m4bInfoList.add(m4bInfo);	
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		return m4bInfoList;
		
		
//		JSONArray jArray = new JSONArray(message);
//		
//		JSONObject jObject ;
//		JSONObject userObject;
//		UserInfo userInfo = null ;
//		Micro4blogInfo timeLineInfo = null ;
//		for (int i = 0; i < jArray.length(); i++) {
//			
//			jObject = (JSONObject) jArray.get(i);
//			userInfo = new UserInfo();
//			timeLineInfo = new Micro4blogInfo();
//			
//			timeLineInfo.setTime(jObject.getString("created_at"));
//			timeLineInfo.setMessageId(jObject.getString("id"));
//			
//			// Status And Image URL
//			String imageUrl = "";
//			if(!jObject.isNull("thumbnail_pic")){
//				imageUrl = "\n" + jObject.getString("thumbnail_pic");
//			}
//			timeLineInfo.setStatus(jObject.getString("text") + imageUrl);
//			timeLineInfo.setFavorite(jObject.getString("favorited"));
//			
//		    userObject = jObject.getJSONObject("user");
//			
//			userInfo.setUid(userObject.getString("id"));
//		    userInfo.setScreenName(userObject.getString("screen_name"));
//		    userInfo.setDescription(userObject.getString("description"));
//		    userInfo.setUserImageURL(userObject.getString("profile_image_url"));
//		    userInfo.setFollowerCount(userObject.getString("followers_count"));
//		    userInfo.setFollowCount(userObject.getString("friends_count"));
//			userInfo.setVerified(userObject.getString("verified"));
//		    try {
//		    	
//		    	if (jObject.has("retweeted_status")) {
//		        	if (jObject.getString("retweeted_status") != null) {
//				    	JSONObject retweetObject = jObject.getJSONObject("retweeted_status");
//				    	timeLineInfo.setRetweeted(true);
//				    	
//						// Status And Image URL
//						String retweetedImageUrl = "";
//						if(!retweetObject.isNull("thumbnail_pic")){
//							retweetedImageUrl = "\n" + retweetObject.getString("thumbnail_pic");
//						}
//				    	timeLineInfo.setRetweetedStatus(retweetObject.getString("text") + retweetedImageUrl);
//				    	
//				    	JSONObject originalUserObject = retweetObject.getJSONObject("user");
//				    	userInfo.setRetweetedScreenName(originalUserObject.getString("screen_name"));
//				    	userInfo.setRetweetUserId(originalUserObject.getString("id"));
//				    	
//				    	//userInfo.setScreenName(originalUserObject.getString("screen_name") + " RT by " + userObject.getString("screen_name"));
//				    }
//		        }
//		    	
//		    } catch (JSONException e) {
//		    	
//		    	e.printStackTrace();
//		    	
//		    }
//		    
//			timeLineInfo.setUserInfo(userInfo);
//		    
//		    jsonInfoList.add(timeLineInfo);
//		    
//			
//		}
		
	}


}
