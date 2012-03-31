package com.micro4blog.service;

import com.micro4blog.dialog.DialogError;
import com.micro4blog.dialog.Micro4blogDialog;
import com.micro4blog.dialog.Micro4blogDialogListener;
import com.micro4blog.http.Micro4blogParameters;
import com.micro4blog.http.Oauth2AccessTokenHeader;
import com.micro4blog.http.Utility;
import com.micro4blog.oauth.Micro4blog;
import com.micro4blog.oauth.OauthToken;
import com.micro4blog.utils.Micro4blogException;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieSyncManager;

public class Micro4blogForSina extends Micro4blog {

	@Override
	protected void initConfig() {
		
		setAppKey("3198633271");
		setAppSecret("fa2ced3df9f410d8bef7796f93fa81c0");
		
		setRedirectUrl("http://github.com/thomsen/Micro4blog");
		
//		setUrlOauth2AccessToken("https://api.weibo.com/oauth2/access_token");
//		setUrlOauth2AccessAuthorize("https://api.weibo.com/oauth2/authorize");
		
		setUrlAccessToken("https://api.weibo.com/oauth2/access_token");
		setUrlAccessAuthorize("https://api.weibo.com/oauth2/authorize");
	
	}
	
	
	protected void authorize(Activity activity, String[] permissions, int activityCode,
            final Micro4blogDialogListener listener) {
        Utility.setAuthorization(new Oauth2AccessTokenHeader());

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
        
        // FIXME sina multiple add problem
        // 这个问题是由于Micro4blogDialog中使用了onStop，将mMicro4blog置为null
        // 不过却连续生成了两个TimelineActivity

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
                if (null == accessToken) {
                	accessToken = new OauthToken();
                }
                accessToken.setTokenOauthOrAccess(values.getString(TOKEN));
                accessToken.setExpiresIn(values.getString(EXPIRES));
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

            public void onError(DialogError error) {
                Log.d("Weibo-authorize", "Login failed: " + error);
                mAuthDialogListener.onError(error);
            }

            public void onMicro4blogException(Micro4blogException error) {
                Log.d("Weibo-authorize", "Login failed: " + error);
                mAuthDialogListener.onMicro4blogException(error);
            }

            public void onCancel() {
                Log.d("Weibo-authorize", "Login canceled");
                mAuthDialogListener.onCancel();
            }

        });
    }


    public void dialog(Context context, Micro4blogParameters parameters,
            final Micro4blogDialogListener listener) {
        parameters.add("client_id", getAppKey());
        parameters.add("response_type", "token");
        parameters.add("redirect_uri", getRedirectUrl());
        parameters.add("display", "mobile");

        if (isSessionValid()) {
            parameters.add(TOKEN, accessToken.getTokenOauthOrAccess());
        }
        String url = getUrlAccessAuthorize() + "?" + Utility.encodeUrl(parameters);
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            Utility.showAlert(context, "Error",
                    "Application requires permission to access the Internet");
        } else {
            new Micro4blogDialog(this, context, url, listener).show();
        }
    }


	@Override
	protected void authorizeCallBack(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
	}


}
