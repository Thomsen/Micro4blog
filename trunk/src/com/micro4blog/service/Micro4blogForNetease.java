package com.micro4blog.service;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieSyncManager;

import com.micro4blog.data.Micro4blogInfo;
import com.micro4blog.dialog.DialogError;
import com.micro4blog.dialog.Micro4blogDialogListener;
import com.micro4blog.http.Micro4blogParameters;
import com.micro4blog.oauth.Micro4blog;
import com.micro4blog.utils.Micro4blogException;

public class Micro4blogForNetease extends Micro4blog {
	
	private static final String TAG = "Micro4blogForNetease";
	
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
		
	}

	@Override
	protected void startDialogAuth(Activity activity, String[] permissions) {
		
		// 针对permissions，进行对参数设置
		Micro4blogParameters params = new Micro4blogParameters();
		
		dialog(activity, params, new Micro4blogDialogListener() {

			@Override
			public void onComplete(Bundle values) {
				
				// ensure any cookies set by the dialog are saved
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
		
		parameters.add("client_type", "mobile");
		
		// oauth第二步，进行用户的授权认证
		getAuthorization(context, parameters, listener);
	
	}

	
	@Override
	protected void authorizeCallBack(int requestCode, int resultCode,
			Intent data) {
			
	}

	@Override
	public String getHomeTimeline(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Micro4blogInfo> parseHomeTimeline(String message) {
		// TODO Auto-generated method stub
		
		return null;
	}

}
