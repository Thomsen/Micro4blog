package com.micro4blog.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.micro4blog.dialog.DialogError;
import com.micro4blog.dialog.Micro4blogDialog;
import com.micro4blog.dialog.Micro4blogDialogListener;
import com.micro4blog.http.AccessTokenHeader;
import com.micro4blog.http.Micro4blogParameters;
import com.micro4blog.http.RequestTokenHeader;
import com.micro4blog.http.Utility;
import com.micro4blog.oauth.Micro4blog;
import com.micro4blog.oauth.OauthToken;
import com.micro4blog.oauth.RequestToken;
import com.micro4blog.utils.Micro4blogException;

public class Micro4blogForSohu extends Micro4blog {
	
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
		
		Micro4blogParameters params = new Micro4blogParameters();
		
		dialog(activity, params, new Micro4blogDialogListener() {

			@Override
			public void onComplete(Bundle values) {
				
				CookieSyncManager.getInstance().sync();
                
                // oauth第三步，换取access token				
				getUserAccessToken(values);
				
				mAuthDialogListener.onComplete(values);
						
//				CookieSyncManager.getInstance().sync();
//				
//				
//				// oauth第三步，换取access token
//				
//				if (null == requestToken) {
//					requestToken = new RequestToken();
//				}
//
//				requestToken.setOauthToken(values.getString("oauth_token"));
//				requestToken.setOauthVerifier(values.getString("oauth_verifier"));
//				
//				setRequestToken(requestToken);
//				
//				try {
//					accessToken = generateAccessToken(mContext, Utility.HTTPMETHOD_GET,
//							requestToken);
//				} catch (Micro4blogException e) {
//					e.printStackTrace();
//				}
//				
//				setAccessToken(accessToken);
//				
//				mAuthDialogListener.onComplete(values);

				// sina oauth2.0
//				if (isSessionValid()) {
//					// 执行了MainActivity中的监听事件，一次
//					mAuthDialogListener.onComplete(values);
//				} else {
//					mAuthDialogListener
//							.onMicro4blogException(new Micro4blogException(
//									"Failed to receive access token."));
//				}

				
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
		
////		RequestToken requestToken = new RequestToken();
//		try {
//			requestToken = getRequestToken(context, Utility.HTTPMETHOD_GET, getAppKey(), getAppSecret(), getRedirectUrl());
//		
//			// 放在这里错了，因为是未经授权的token
////			setRequestToken(requestToken);			
//			
//		} catch (Micro4blogException e) {			
//			e.printStackTrace();
//		}
//		
//		if (requestToken.getOauthToken() != null) {
//			parameters.add("oauth_token", requestToken.getOauthToken());
//		}
//		
//		parameters.add("clientType", "phone");
//		parameters.add("oauth_callback", getRedirectUrl());
//		
//		Utility.setAuthorization(new AccessTokenHeader());
//		
//		String url = getUrlAccessAuthorize() + "?" + Utility.encodeUrl(parameters);
//		Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
//		
//		new Micro4blogDialog(this, context, url, listener).show();
		
		// oauth第一步，获取request token
		getAppRequestToken(context, parameters);
		
		parameters.add("clientType", "phone");
		parameters.add("oauth_callback", getRedirectUrl());
		
		// oauth第二步，进行用户的授权认证
		getUserRequestToken(context, parameters, listener);
		
	}

	@Override
	protected void authorizeCallBack(int requestCode, int resultCode,
			Intent data) {
			
	}


}
