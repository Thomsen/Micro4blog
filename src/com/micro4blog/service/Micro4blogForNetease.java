package com.micro4blog.service;

import com.micro4blog.dialog.DialogError;
import com.micro4blog.dialog.Micro4blogDialog;
import com.micro4blog.dialog.Micro4blogDialogListener;
import com.micro4blog.http.AccessTokenHeader;
import com.micro4blog.http.HttpHeaderFactory;
import com.micro4blog.http.Micro4blogParameters;
import com.micro4blog.http.RequestTokenHeader;
import com.micro4blog.http.Utility;
import com.micro4blog.oauth.Micro4blog;
import com.micro4blog.oauth.OauthToken;
import com.micro4blog.oauth.RequestToken;
import com.micro4blog.utils.Micro4blogException;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieSyncManager;

public class Micro4blogForNetease extends Micro4blog {

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
		
	}

	@Override
	protected void authorize(Activity activity, String[] permissions,
			int activityCode, Micro4blogDialogListener listener) {

//		Utility.setAuthorization(new RequestTokenHeader());
		
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
                if (null == accessToken) {
                	accessToken = new OauthToken();
                }
//                accessToken.setTokenOauthOrAccess(values.getString(TOKEN));
                
                accessToken.setTokenOauthOrAccess(values.getString("oauth_token"));
                        
                if (isSessionValid()) {
                    Log.d("Weibo-authorize",
                            "Login Success! access_token=" + accessToken.getTokenOauthOrAccess() + " expires="
                                    + accessToken.getExpiresIn());
                    mAuthDialogListener.onComplete(values);
                } else {
                    Log.d("Weibo-authorize", "Failed to receive access token");
                    mAuthDialogListener.onMicro4blogException(new Micro4blogException(
                            "Failed to receive access token."));
                }
			}

			@Override
			public void onError(DialogError error) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onMicro4blogException(Micro4blogException e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}

	@Override
	protected void dialog(Context context, Micro4blogParameters parameters,
			Micro4blogDialogListener listener) {

//		HttpHeaderFactory hhp = new RequestTokenHeader();
//		String result = "";
//		
//		try {
//			
//			hhp.getMicro4blogAuthHeader(this, "GET", getUrlRequestToken(), parameters, getAppKey(), getAppSecret(), accessToken);
//		
//			Micro4blogParameters params = hhp.getAuthParams();
//			
//			result = request(context, getUrlRequestToken(), params, "GET", accessToken);
//		} catch (Micro4blogException e) {
//			e.printStackTrace();
//		}
//		
//		OauthToken requestToken = new OauthToken(result);
		
		RequestToken requestToken = null;
		try {
			requestToken = getRequestToken(context, Utility.HTTPMETHOD_GET, getAppKey(), getAppSecret(), getRedirectUrl());
		} catch (Micro4blogException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (requestToken != null) {
			parameters.add("oauth_token", requestToken.getTokenOauthOrAccess());
		}
		
		parameters.add("client_type", "mobile");
		
		Utility.setAuthorization(new AccessTokenHeader());
		
		String url = getUrlAccessAuthorize() + "?" + Utility.encodeUrl(parameters);
		
		new Micro4blogDialog(this, context, url, listener).show();
	
	
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
		// TODO Auto-generated method stub
		
	}

}
