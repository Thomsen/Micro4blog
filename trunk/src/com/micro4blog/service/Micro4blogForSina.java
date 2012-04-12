package com.micro4blog.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieSyncManager;

import com.micro4blog.dialog.DialogError;
import com.micro4blog.dialog.Micro4blogDialogListener;
import com.micro4blog.http.Micro4blogParameters;
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


}
