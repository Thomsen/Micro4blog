package com.micro4blog.server;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieSyncManager;

import com.micro4blog.data.Micro4blogInfo;
import com.micro4blog.data.UserInfo;
import com.micro4blog.dialog.DialogError;
import com.micro4blog.dialog.Micro4blogDialogListener;
import com.micro4blog.http.Micro4blogParameters;
import com.micro4blog.http.Utility;
import com.micro4blog.oauth.Micro4blog;
import com.micro4blog.utils.Micro4blogException;

public class Micro4blogForSohu extends Micro4blog {
	
	private static final String TAG = "Micro4blogForSohu";
	
	private static Micro4blogForSohu m4bSohu;
	
	public Micro4blogForSohu() {
		super();
	}
	
	public synchronized static Micro4blogForSohu getInstance() {
		if (m4bSohu == null) {
			m4bSohu = new Micro4blogForSohu();
		}
		
		return m4bSohu;
	}

	@Override
	protected void initConfig() {
		
		setAppKey("xJjgBsXDO51ylviVj1zP");
		setAppSecret("lu2BhNYtIpcGdRXJSg=CoElLbQlL0PJihDp1d44o");
		
		setRedirectUrl("micro4blog://TimelineActivity");

		setUrlRequestToken("http://api.t.sohu.com/oauth/request_token");
		setUrlAccessToken("http://api.t.sohu.com/oauth/access_token");
		setUrlAccessAuthorize("http://api.t.sohu.com/oauth/authorize");
	
		setServerUrl("http://api.t.sohu.com/");
	}

	@Override
	protected void authorize(Activity activity, String[] permissions,
			int activityCode, Micro4blogDialogListener listener) {

		mContext = activity;
		
		mAuthDialogListener = listener;
		
		startDialogAuth(activity, permissions);
		
	}

	@Override
	protected void startDialogAuth(Activity activity, String[] permissions) {
		
		// 针对permissions，进行对参数设置
		Micro4blogParameters params = new Micro4blogParameters();
		
		dialog(activity, params, new Micro4blogDialogListener() {

			@Override
			public void onComplete(Bundle values) {
				
				CookieSyncManager.getInstance().sync();
                
                // oauth第三步，换取access token				
				getUserAccessToken(values);
														
			}

			@Override
			public void onError(DialogError error) {
								
			}

			@Override
			public void onCancel() {
							
			}

			@Override
			public void onMicro4blogException(Micro4blogException e) {
							
			}
			
		});
		
	}

	@Override
	protected void dialog(Context context, Micro4blogParameters parameters,
			Micro4blogDialogListener listener) {
	
		// oauth第一步，获取request token
		getAppRequestToken(context, parameters);
		
		parameters.add("clientType", "phone");
		parameters.add("oauth_callback", getRedirectUrl());
		
		// oauth第二步，进行用户的授权认证
		getAuthorization(context, parameters, listener);
		
	}

	@Override
	protected void authorizeCallBack(int requestCode, int resultCode,
			Intent data) {
			
	}
	
	
	
	private void setMicro4blogList(String message,
			ArrayList<Micro4blogInfo> m4bInfoList) {
		try {
//			JSONObject jsonObject = new JSONObject(message);
//			JSONArray m4bArray = jsonObject.getJSONArray("statuses");
			JSONArray m4bArray = new JSONArray(message);
			JSONObject m4bObject;
			Micro4blogInfo m4bInfo;
			UserInfo userInfo;
			for (int i=0; i<m4bArray.length(); i++) {
				m4bObject = (JSONObject) m4bArray.get(i);

				userInfo = new UserInfo();
				m4bInfo = new Micro4blogInfo();
				
				setMicro4blogInfo(m4bObject, m4bInfo);	
				
				setRetweetMicro4blogInfo(m4bObject, m4bInfo);
				
				setUserInfo(m4bObject, m4bInfo, userInfo);
								
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
			m4bInfo.setM4bStrId(m4bObject.getString("id"));
			m4bInfo.setM4bText(m4bObject.getString("text"));
			m4bInfo.setM44Source(m4bObject.getString("source"));
			m4bInfo.setM4bFovorited(m4bObject.getBoolean("favorited"));
			m4bInfo.setM4bTruncated(m4bObject.getBoolean("truncated"));
//			m4bInfo.setM4bInReplyToStatusId(m4bObject.getInt("in_replay_to_status_id"));
//			m4bInfo.setM4bInReplyToUserId(m4bObject.getInt("in_replay_to_user_id"));
//			m4bInfo.setM4bInReplyToScreenName(m4bObject.getString("in_reply_to_screen_name"));
//			m4bInfo.setM4bMid(m4bObject.getInt("mid"));
//			m4bInfo.setM4bMiddlePicture(m4bObject.getString("bmiddle_pic"));
//			m4bInfo.setM4bOriginPicture(m4bObject.getString("original_pic"));
//			m4bInfo.setM4bThumbnailPic(m4bObject.getString("thumbnail_pic"));
//	
//			m4bInfo.setM4bForwardingCount(m4bObject.getInt("reposts_count"));
//			m4bInfo.setM4bForwardingCount(m4bObject.getInt("retweet_count"));
//	
//			m4bInfo.setM4bCommentCount(m4bObject.getInt("comments_count"));
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
//		userInfo.setProvince(userObject.getInt("province"));
//		userInfo.setCity(userObject.getInt("city"));
		userInfo.setLocation(userObject.getString("location"));
		userInfo.setDescription(userObject.getString("description"));
		userInfo.setBlogUrl(userObject.getString("url"));
		userInfo.setProfileImageUrl(userObject.getString("profile_image_url"));
//		userInfo.setDomain(userObject.getString("domain"));
		userInfo.setGender(userObject.getString("gender"));
		userInfo.setFollowersCount(userObject.getInt("followers_count"));
		userInfo.setFriendsCount(userObject.getInt("friends_count"));
		userInfo.setM4bCount(userObject.getInt("statuses_count"));
		userInfo.setFavouritesCount(userObject.getInt("favourites_count"));
		userInfo.setCreateAt(userObject.getString("created_at"));
//		userInfo.setFollowing(userObject.getBoolean("following"));
//		userInfo.setAllowAllActMsg(userObject.getBoolean("allow_all_act_msg"));
		userInfo.setGeoEnabled(userObject.getBoolean("geo_enabled"));
		userInfo.setVerified(userObject.getBoolean("verified"));
//		userInfo.setAllowAllComment(userObject.getBoolean("allow_all_comment"));
//		userInfo.setAvatarLarge(userObject.getString("avatar_large"));
//		userInfo.setVerifiedReason(userObject.getString("verified_reason"));
//		userInfo.setFollowMe(userObject.getBoolean("follow_me"));
//		userInfo.setOnlineStatus(userObject.getInt("online_status"));
//		userInfo.setBiFollowersCount(userObject.getInt("bi_followers_count"));
						
		m4bInfo.setUserInfo(userInfo);
		return userObject;
	}

	@Override
	public String getHomeTimeline(Context context) {
		apiUrl = getServerUrl() + "statuses/friends_timeline.json";
		
		// TODO 解决带参数的未授权问题
//		apiParameters.add("count", "20");
		
//		serverResult = request(context, serverUrl, apiParameters, Utility.HTTPMETHOD_GET, accessToken);
		
		apiResult = request(apiHeader, Utility.HTTPMETHOD_GET, apiUrl, apiParameters, accessToken);
		
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
	
	
	

}
