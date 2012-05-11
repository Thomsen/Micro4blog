package com.micro4blog.server;

import java.io.IOException;
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
import android.widget.Toast;

import com.micro4blog.Micro4blog;
import com.micro4blog.activity.HomeTimelineActivity;
import com.micro4blog.data.Micro4blogInfo;
import com.micro4blog.data.UserInfo;
import com.micro4blog.dialog.DialogError;
import com.micro4blog.dialog.Micro4blogDialogListener;
import com.micro4blog.http.Micro4blogParameters;
import com.micro4blog.http.HttpUtility;
import com.micro4blog.utils.AsyncMicro4blogRunner.RequestListener;
import com.micro4blog.utils.Micro4blogException;

public class Micro4blogForSina extends Micro4blog {
	
	private static final String TAG = "Micro4blogForSina";

	private static Micro4blog m4bSina = new Micro4blogForSina();
	
	public Micro4blogForSina() {
		super();
	}
	
	public static Micro4blog getInstance() {
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

	
	

	private void setMicro4blogList(String message,
			ArrayList<Micro4blogInfo> m4bInfoList) {
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
				
				setMicro4blogInfo(m4bObject, m4bInfo);				
				
				userObject = setUserInfo(m4bObject, m4bInfo, userInfo);
			
				setRetweetMicro4blogInfo(m4bObject, m4bInfo);
				
//				getRepostTimeline(m4bInfo.getM4bId());
//				getRepostTimeline(m4bInfo.getM4bStrId());
								
				m4bInfoList.add(m4bInfo);				
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void setMicro4blogInfo(JSONObject m4bObject, Micro4blogInfo m4bInfo) {
		
		try {
			m4bInfo.setM4bCreateAt(m4bObject.getString("created_at"));
//			m4bInfo.setM4bId(m4bObject.getInt("id"));
			m4bInfo.setM4bStrId(m4bObject.getString("idstr"));
			m4bInfo.setM4bText(m4bObject.getString("text"));
			m4bInfo.setM4bSource(m4bObject.getString("source"));
			m4bInfo.setM4bFovorited(m4bObject.getBoolean("favorited"));
			m4bInfo.setM4bTruncated(m4bObject.getBoolean("truncated"));
//			m4bInfo.setM4bInReplyToStatusId(m4bObject.getInt("in_replay_to_status_id"));
//			m4bInfo.setM4bInReplyToUserId(m4bObject.getInt("in_replay_to_user_id"));
//			m4bInfo.setM4bInReplyToScreenName(m4bObject.getString("in_reply_to_screen_name"));
			m4bInfo.setM4bMid(m4bObject.getInt("mid"));
//			m4bInfo.setM4bMiddlePicture(m4bObject.getString("bmiddle_pic"));
//			m4bInfo.setM4bOriginPicture(m4bObject.getString("original_pic"));
//		m	4bInfo.setM4bThumbnailPic(m4bObject.getString("thumbnail_pic"));
			m4bInfo.setM4bRetweetCount(m4bObject.getInt("reposts_count"));
			m4bInfo.setM4bCommentCount(m4bObject.getInt("comments_count"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	private JSONObject setUserInfo(JSONObject m4bObject,
			Micro4blogInfo m4bInfo, UserInfo userInfo) throws JSONException {
		JSONObject userObject;
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
		userInfo.setFavouritesCount(userObject.getInt("favourites_count"));
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
		return userObject;
	}
	
	@Override
	public String getHomeTimeline(Context context) {
		
		mContext = context;
		
		apiUrl = getServerUrl() + "statuses/home_timeline.json";
		apiParameters.add("count", "20");
		apiResult = request(context, apiUrl, apiParameters, HttpUtility.HTTPMETHOD_GET, accessToken);
		
		return apiResult;
	}

	@Override
	public ArrayList<Micro4blogInfo> parseHomeTimeline(String message) {
		ArrayList<Micro4blogInfo> m4bInfoList = new ArrayList<Micro4blogInfo>();
		if(message == null) {
			return m4bInfoList;
		}
		
		setMicro4blogList(message, m4bInfoList);
		
		
		return m4bInfoList;

		
	}
	
	public String getRepostTimeline(String strId) {
		
		apiUrl = getServerUrl() + "statuses/repost_timeline.json";
		
		apiParameters.clear();
		apiParameters.add("id", strId);
		
//		apiResult = request(mContext, apiUrl, apiParameters, Utility.HTTPMETHOD_GET, accessToken);
		
		apiRunner.request(mContext, apiUrl, apiParameters, HttpUtility.HTTPMETHOD_GET, new RequestListener() {

			@Override
			public void onComplete(String response) {
				
				apiResult = response;
				
			}

			@Override
			public void onIOException(IOException e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(Micro4blogException e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		return apiResult;
		
	}

	@Override
	public String update(String status, String lon, String lat) {
        
        apiUrl = getServerUrl() + "statuses/update.json";
        
        apiParameters.add("status", status);
        if (!TextUtils.isEmpty(lon)) {
            apiParameters.add("lon", lon);
        }
        if (!TextUtils.isEmpty(lat)) {
            apiParameters.add("lat", lat);
        }
        
        // 由于线程的问题，多服务使用apiRunner会出现
        // Launch timeout has expired giving up wake lock
        // 但是，这样没法解决发布后的跳转问题
        // 错了， 问题的核心不是这个问题，而是在timeline时出现的
        // 这样，是因为线程耗时超过了10秒，为了更好的操作新建一个线程
        apiRunner.request(mContext, apiUrl, apiParameters, HttpUtility.HTTPMETHOD_POST, new RequestListener() {

			@Override
			public void onComplete(String response) {
				Toast.makeText(mContext, "send success", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(mContext, HomeTimelineActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mContext.startActivity(intent);
				
			}

			@Override
			public void onIOException(IOException e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(Micro4blogException e) {
				// TODO Auto-generated method stub
				
			}
        	
        });
     
        return apiResult;
	}

	@Override
	public boolean destroy(String strId) {

		apiUrl = getServerUrl() + "statuses/destroy.json";
		
		apiParameters.add("id", strId);
		
		apiRunner.request(mContext, apiUrl, apiParameters, HttpUtility.HTTPMETHOD_POST, null);
		
		return false;
	}



}
