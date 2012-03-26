package com.micro4blog.oauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.LinearLayout;

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

		// 中间出现的一切关于token错误，都是因为这里的头设置错误
		// Utility.setAuthorization(new AccessTokenHeader());

		// TODO: 通过Utility中的setHeader可以设置头部，即得到parameters
		// generateSignatureList的使用
		// setHeader中调用getMicro4blogAuthHeader，
		// generateMicro4blogAuthHeader中会使用到generateSignatureList
		// 那么setHeader在哪里调用了呢？
		// openUrl通信中
		
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
					accessToken = new OauthToken();
				}

				accessToken.setTokenOauthOrAccess(values.getString(TOKEN));

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
		
		// TODO： listener该如何使用
		// listener.onComplete，可以使用在调用该方法时必须实现的接口方法

		// parameters.add("aouth_callback", null);
		// parameters.add("oauth_consumer_key", getAppKey());

		// String url = getUrlRequestToken() + "?" +
		// Utility.encodeUrl(parameters);

		// 这里不该初始化
		// HttpHeaderFactory hhp = new AccessTokenHeader();
		HttpHeaderFactory hhp = new RequestTokenHeader();
		
		String result = "";
		
		try {
			
			hhp.getMicro4blogAuthHeader(this, "POST", getUrlRequestToken(), parameters, getAppKey(), getAppSecret(), accessToken);
		
			Micro4blogParameters params = hhp.getAuthParams();
			
			result = request(context, getUrlRequestToken(), params, "POST", accessToken);
		} catch (Micro4blogException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.i("thom", "result " + result);
//		
//		parameters = hhp.getAuthParams();
		
		
		OauthToken requestToken = new OauthToken(result);
		
		
//		try {
//			hhp.getMicro4blogAuthHeader(this, "POST", getUrlRequestToken(), parameters, getAppKey(), getAppSecret(), accessToken);
//		} catch (Micro4blogException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		String header = "";
//
//		Log.i("thom", "hader " + "1");

//		try {
//			header = hhp.getMicro4blogAuthHeader(
//					Micro4blog.getInstance(SERVER_TENCENT), "GET",
//					getUrlRequestToken(), parameters, getAppKey(),
//					getAppSecret(), accessToken);
//		} catch (Micro4blogException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		Log.i("thom", "header " + header);

		// String url = getUrlRequestToken() + "?" + header;

//		RequestToken rt;
//		String url = "";
//		try {
//			rt = this.getRequestToken(context, getAppKey(), getAppSecret(), "");
//			url = getUrlAccessAuthorize() + "?oauth_token="
//					+ this.generateAccessToken(context, rt);
//		} catch (Micro4blogException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// TODO: 1、通过request url和请求参数得到request token
		// 2、通过request token和oauthorize url得到回调url，从中得到verifier code
		// 第二步，是通过url中提供的用户登录授权后得到verifier code
		// 3、通过request token、verifier code加上之前参数通过access url得到access token
		// 中间传递的token都是以oauth_token传递
		
		// 通信的过程都在Utility的openUrl中
		
		// 是通过这个方法来得到正确的url请求
		// 通过generateAccessToken只是设置parameters， 返回的结果用于测试
		// 到了这里全局主要的问题就是得到该有的parameters
		
		// 需要研究一下sina的parameters的获取
		// 较之1.0，2.0的请求参数比较少，sina是在请求前传入的
		
		
		// TODO： 解决多次注册的encode问题
		parameters.add("oauth_token", requestToken.getTokenOauthOrAccess());
		
		String url = getUrlAccessAuthorize() + "?" + Utility.encodeUrl(parameters);
		
		// TODO： 如何理解在何时进行的url通信
		// 以我看应该是在Micro4blogDialog中
		
		// 别忘了，1.0中callback url的使用

		Log.i("thom", "url " + url);

		new Micro4blogDialog(this, context, url, listener).show();
		// show方法执行了整个dialog的该实现的部分
		
//		WebView wv = new WebView(context);
//		wv.loadUrl(url);
//		((Activity) context).addContentView(wv, new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
//								LinearLayout.LayoutParams.FILL_PARENT));

	}

	@Override
	protected void authorizeCallBack(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub

	}

}
