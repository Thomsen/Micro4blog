package com.micro4blog.oauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

public class Micro4blogForTencent extends Micro4blog {

	@Override
	protected void initConfig() {
		
		setAppKey("801111016");
		setAppSecret("77f11f15151a8b85b15044bca6c2d2ed");
		
		setUrlRequestToken("https://open.t.qq.com/cgi-bin/request_token");
		setUrlAccessToken("https://open.t.qq.com/cgi-bin/access_token");
		setUrlAccessAuthorize("https://open.t.qq.com/cgi-bin/authorize");
		
	}

	@Override
	protected void authorize(Activity activity, String[] permissions,
			int activityCode, Micro4blogDialogListener listener) {
		
//		中间出现的一切关于token错误，都是因为这里的头设置错误
//		Utility.setAuthorization(new AccessTokenHeader());
		
		Utility.setAuthorization(new RequestTokenHeader());
		
		mAuthDialogListener = listener;
		
		startDialogAuth(activity, permissions);
	}

	@Override
	protected void startDialogAuth(Activity activity, String[] permissions) {
		
		Micro4blogParameters params = new Micro4blogParameters();
		
		CookieSyncManager.createInstance(activity);
		
		dialog(activity, params, new Micro4blogDialogListener() {

			@Override
			public void onComplete(Bundle values) {

				CookieSyncManager.getInstance().sync();
				
				if (null == accessToken) {
					accessToken = new Token();
				}
				
				accessToken.setTokenOauthOrAccess(values.getString(TOKEN));
			
				if (isSessionValid()) {
					 mAuthDialogListener.onComplete(values);
				} else {
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
		
//		parameters.add("aouth_callback", null);
//		parameters.add("oauth_consumer_key", getAppKey());
		
		

//		String url = getUrlRequestToken() + "?" + Utility.encodeUrl(parameters);
		
		// 这里不该初始化
//		HttpHeaderFactory hhp = new AccessTokenHeader();
		HttpHeaderFactory hhp = new RequestTokenHeader();
		
		String header = "";
		
		Log.i("thom", "hader " + "1");
		
	
		try {
			 header = hhp.getMicro4blogAuthHeader(Micro4blog.getInstance(SERVER_TENCENT), "GET", getUrlRequestToken(), parameters, getAppKey(), getAppSecret(), accessToken);
		} catch (Micro4blogException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.i("thom", "header " + header);
		
		String url = getUrlRequestToken() + "?" + header;
		
		
		Log.i("thom", "url " + url);
		
//		new Micro4blogDialog(this, context, url, listener).show();
		WebView wv = new WebView(context);
		wv.loadUrl(url);
		
		((Activity) context).addContentView(wv, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT) ); 
		
	}

	@Override
	protected void authorizeCallBack(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		
	}

}
