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

	@Override
	protected void initConfig() {
		
		setAppKey("xJjgBsXDO51ylviVj1zP");
		setAppSecret("lu2BhNYtIpcGdRXJSg=CoElLbQlL0PJihDp1d44o");
		
		setRedirectUrl("micro4blog://TimelineActivity");

		setUrlRequestToken("http://api.t.sohu.com/oauth/request_token");
		setUrlAccessToken("http://api.t.sohu.com/oauth/access_token");
		setUrlAccessAuthorize("http://api.t.sohu.com/oauth/authorize");
	}

	@Override
	protected void authorize(Activity activity, String[] permissions,
			int activityCode, Micro4blogDialogListener listener) {

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

				if (null == accessToken) {
					accessToken = new OauthToken();
				}

				accessToken.setOauthToken(values.getString("oauth_token"));

				if (isSessionValid()) {
					mAuthDialogListener.onComplete(values);
				} else {
					mAuthDialogListener
							.onMicro4blogException(new Micro4blogException(
									"Failed to receive access token."));
				}

				
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
		
		RequestToken requestToken = null;
		try {
			requestToken = getRequestToken(context, Utility.HTTPMETHOD_GET, getAppKey(), getAppSecret(), getRedirectUrl());
		} catch (Micro4blogException e) {			
			e.printStackTrace();
		}
		
		if (requestToken.getOauthToken() != null) {
			parameters.add("oauth_token", requestToken.getOauthToken());
		}
		
		parameters.add("clientType", "phone");
		parameters.add("oauth_callback", getRedirectUrl());
		
		Utility.setAuthorization(new AccessTokenHeader());
		
		String url = getUrlAccessAuthorize() + "?" + Utility.encodeUrl(parameters);
		Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
		
		new Micro4blogDialog(this, context, url, listener).show();
		
	}

	@Override
	protected void authorizeCallBack(int requestCode, int resultCode,
			Intent data) {
			
	}

}
