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

public class Micro4blogForTencent extends Micro4blog {
	
	private static final String TAG = "Micro4blogForTencent";
	
	private static Micro4blogForTencent m4bTencent;
	
	public Micro4blogForTencent() {
		super();
	}
	
	public synchronized static Micro4blogForTencent getInstance() {
		if (m4bTencent == null) {
			m4bTencent = new Micro4blogForTencent();
		}
		
		return m4bTencent;
	}

	@Override
	protected void initConfig() {

		setAppKey("801111016");
		setAppSecret("77f11f15151a8b85b15044bca6c2d2ed");
		
		// 要设置callback url，并在manifest中配置
		setRedirectUrl("micro4blog://TimelineActivity");

		// 为了在dialog显示， 原来是https 换成了http 记得要改post为get，反之亦是
		// 针对dialog的callback
		setUrlRequestToken("http://open.t.qq.com/cgi-bin/request_token");
		setUrlAccessToken("http://open.t.qq.com/cgi-bin/access_token");
		setUrlAccessAuthorize("http://open.t.qq.com/cgi-bin/authorize");
		
		setServerUrl("http://open.t.qq.com/api/");

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
		
		CookieSyncManager.createInstance(activity);

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
