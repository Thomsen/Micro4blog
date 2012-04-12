package com.micro4blog.server;

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
