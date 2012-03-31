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
import com.micro4blog.utils.Micro4blogException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieSyncManager;

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

		Utility.setAuthorization(new RequestTokenHeader());
		
		mAuthDialogListener = listener;
		
		startDialogAuth(activity, permissions);
		
	}

	@Override
	protected void startDialogAuth(Activity activity, String[] permissions) {
		
		Micro4blogParameters params = new Micro4blogParameters();
		
		dialog(activity, params, new Micro4blogDialogListener() {

			@Override
			public void onComplete(Bundle values) {
				// FIXME 需要解决sohu tencent netease的授权后跳转问题
				
				CookieSyncManager.getInstance().sync();

				if (null == accessToken) {
					accessToken = new OauthToken();
				}

//				accessToken.setTokenOauthOrAccess(values.getString(TOKEN));
				accessToken.setTokenOauthOrAccess(values.getString("oauth_token"));

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

		HttpHeaderFactory hhp = new RequestTokenHeader();
		String result = "";
		
		try {
			
			hhp.getMicro4blogAuthHeader(this, "GET", getUrlRequestToken(), parameters, getAppKey(), getAppSecret(), accessToken);
		
			Micro4blogParameters params = hhp.getAuthParams();
			
			result = request(context, getUrlRequestToken(), params, "GET", accessToken);
		} catch (Micro4blogException e) {
			e.printStackTrace();
		}
		
		OauthToken requestToken = new OauthToken(result);
		
		if (requestToken.getTokenOauthOrAccess() != null) {
			parameters.add("oauth_token", requestToken.getTokenOauthOrAccess());
		}
		
		parameters.add("clientType", "phone");
		parameters.add("oauth_callback", getRedirectUrl());
		
		Utility.setAuthorization(new AccessTokenHeader());
		
		String url = getUrlAccessAuthorize() + "?" + Utility.encodeUrl(parameters);
		
		new Micro4blogDialog(this, context, url, listener).show();
		
	}

	@Override
	protected void authorizeCallBack(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		
	}

}
