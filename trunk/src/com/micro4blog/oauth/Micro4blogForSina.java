package com.micro4blog.oauth;

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

        boolean singleSignOnStarted = false;
        
        mAuthDialogListener = listener;
        
        Log.i("thom", "Micro4blog:authorize activityCode " + activityCode);

        // Prefer single sign-on, where available.
        if (activityCode >= 0) {
            singleSignOnStarted = startSingleSignOn(activity, getAppKey(), permissions, activityCode);
            Log.i("thom", "Micro4blog:authorize appKey " + getAppKey());
            
        }
        // Otherwise fall back to traditional dialog.
        if (!singleSignOnStarted) {
            startDialogAuth(activity, permissions);
            
            Log.i("thom", "singleSignOnStarted");
            for (String s : permissions) {
            	Log.i("thom", "Micro4blog:authorize " + s);
            }
        }

    }
    
    protected void startDialogAuth(Activity activity, String[] permissions) {
        Micro4blogParameters params = new Micro4blogParameters();
        if (permissions.length > 0) {
            params.add("scope", TextUtils.join(",", permissions));
        }
        
        Log.i("thom", "start Dialog Auth");
        
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
            Log.i("thom", "isSessionValid is success");
        }
        String url = getUrlAccessAuthorize() + "?" + Utility.encodeUrl(parameters);
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            Utility.showAlert(context, "Error",
                    "Application requires permission to access the Internet");
        } else {
        	Log.i("thom", "dialog show");
            new Micro4blogDialog(this, context, url, listener).show();
        }
    }


	@Override
	protected void authorizeCallBack(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		
	}

//    public boolean isSessionValid() {
//        if (accessToken != null) {
//        	Log.i("thom", "accessToken " + accessToken);
//            return (!TextUtils.isEmpty(accessToken.getTokenOauthOrAccess()) && (accessToken.getExpiresIn() == 0 || (System
//                    .currentTimeMillis() < accessToken.getExpiresIn())));
//        }
//        return false;
//    }

}
