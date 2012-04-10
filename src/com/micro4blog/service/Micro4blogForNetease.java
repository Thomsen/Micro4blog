package com.micro4blog.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieSyncManager;

import com.micro4blog.dialog.DialogError;
import com.micro4blog.dialog.Micro4blogDialogListener;
import com.micro4blog.http.Micro4blogParameters;
import com.micro4blog.http.RequestTokenHeader;
import com.micro4blog.http.Utility;
import com.micro4blog.oauth.Micro4blog;
import com.micro4blog.oauth.OauthToken;
import com.micro4blog.oauth.RequestToken;
import com.micro4blog.utils.Micro4blogException;

public class Micro4blogForNetease extends Micro4blog {
	
	private static Micro4blog m4bNetease;
	
	public Micro4blogForNetease() {
		super();
	}
	
	public synchronized static Micro4blog getInstance() {
		if (m4bNetease == null) {
			m4bNetease = new Micro4blogForNetease();
		}
		
		return m4bNetease;
	}

	@Override
	protected void initConfig() {
		
		setAppKey("5V20v8ORzD8ie78k");
		setAppSecret("O3iJyOQM5WQZD7tJjew7bpbHpQYt8VKy");
		
//		setRedirectUrl("micro4blog://TimelineActivity");
		setRedirectUrl("http://github.com/thomsen/Micro4blog");

		setUrlRequestToken("http://api.t.163.com/oauth/request_token");
		setUrlAccessToken("http://api.t.163.com/oauth/access_token");
		setUrlAccessAuthorize("http://api.t.163.com/oauth/authenticate");

//		setUrlAccessAuthorize("https://api.t.163.com/oauth2/authorize");
//		setUrlAccessToken("https://api.t.163.com/oauth2/access_token");
		
		setServerUrl("http://api.t.163.com/");
		
	}

	@Override
	protected void authorize(Activity activity, String[] permissions,
			int activityCode, Micro4blogDialogListener listener) {
		
		mAuthDialogListener = listener;
		
		startDialogAuth(activity, permissions);
		
//		Utility.setAuthorization(new Oauth2AccessTokenHeader());
//		
//		mAuthDialogListener = listener;
//		
//		boolean singleSignOnStarted = false;
//		
//		if (activityCode >= 0) {
//			singleSignOnStarted = startSingleSignOn(activity, getAppKey(), permissions, activityCode);
//		}
//		
//		
//		if (!singleSignOnStarted) {
//			startDialogAuth(activity, permissions);
//		}
	}

	@Override
	protected void startDialogAuth(Activity activity, String[] permissions) {
		
		Micro4blogParameters params = new Micro4blogParameters();
		
		dialog(activity, params, new Micro4blogDialogListener() {

			@Override
			public void onComplete(Bundle values) {
				// ensure any cookies set by the dialog are saved
                CookieSyncManager.getInstance().sync();
                
                // oauth第三步，换取access token				
				getUserAccessToken(values);
				
				mAuthDialogListener.onComplete(values);
                
//                // 代码的健壮性
//                if (null == requestToken) {
//                	requestToken = new RequestToken();
//                }
//                
//                // 获取第二步，得到的结果
//                // 用户授权过后的token，网易没有返回oauth_verifier
//                requestToken.setOauthToken(values.getString("oauth_token"));
//                setRequestToken(requestToken);
//                        
//                if (isSessionValid()) {
//                    Log.d("Weibo-authorize",
//                            "Login Success! access_token=" + accessToken.getOauthToken() + " expires="
//                                    + accessToken.getExpiresIn());
//                    mAuthDialogListener.onComplete(values);
//                } else {
//                    Log.d("Weibo-authorize", "Failed to receive access token");
//                    mAuthDialogListener.onMicro4blogException(new Micro4blogException(
//                            "Failed to receive access token."));
//                }
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
		
		parameters.add("client_type", "mobile");
		
		// oauth第二步，进行用户的授权认证
		getUserRequestToken(context, parameters, listener);
	
//		parameters.add("client_id", getAppKey());
//        parameters.add("response_type", "token");
//        parameters.add("redirect_uri", getRedirectUrl());
//        parameters.add("display", "mobile");
//
//        if (isSessionValid()) {
//            parameters.add(TOKEN, accessToken.getTokenOauthOrAccess());
//        }
//        String url = getUrlAccessAuthorize() + "?" + Utility.encodeUrl(parameters);
//        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
//            Utility.showAlert(context, "Error",
//                    "Application requires permission to access the Internet");
//        } else {
//            new Micro4blogDialog(this, context, url, listener).show();
//        }
	}

	
	@Override
	protected void authorizeCallBack(int requestCode, int resultCode,
			Intent data) {
			
	}

}
