package com.micro4blog.oauth;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.micro4blog.dialog.Micro4blogDialog;
import com.micro4blog.dialog.Micro4blogDialogListener;
import com.micro4blog.http.AccessTokenHeader;
import com.micro4blog.http.HttpHeaderFactory;
import com.micro4blog.http.Micro4blogParameters;
import com.micro4blog.http.Oauth2AccessTokenHeader;
import com.micro4blog.http.RequestTokenHeader;
import com.micro4blog.http.Utility;
import com.micro4blog.service.Micro4blogForNetease;
import com.micro4blog.service.Micro4blogForSina;
import com.micro4blog.service.Micro4blogForSohu;
import com.micro4blog.service.Micro4blogForTencent;
import com.micro4blog.tests.ShareActivity;
import com.micro4blog.utils.Micro4blogException;

public abstract class Micro4blog {

	private static final String TAG = "Micro4blog";

	// TODO 需要设计，兼容4大微博
	// 解决 oauth版本的问题，sina 2.0 sohu 1.0 tencent 1.0a netease 1.0a
	// 1.0a也是1.0版
	// 首先要确定oauth的标准参数，不同平台的参数提供的不一样
	// netease在access token的返回中使用的是access_token_secreate
	// 这只能怪编写文档的人，文档编写的太差

	public static final int SERVER_SINA = 0;
	public static final int SERVER_TENCENT = 1;
	public static final int SERVER_NETEASE = 2;
	public static final int SERVER_SOHU = 3;

	private static int currentServer = -1;

	private static String serverUrl = "";

	public static int DEFAULT_AUTH_ACTIVITY_CODE = 0;

	private String urlRequestToken = null;
	private String urlAccessToken = null;
	private String urlAccessAuthorize = null;

	private String appKey = "";
	private String appSecret = "";

	private static String redirectUrl = "null";

	private static Micro4blog micro4blogInstance;

	private RequestToken requestToken;
	protected OauthToken accessToken;

	protected Micro4blogDialogListener mAuthDialogListener;

	protected Context mContext;

	public Micro4blog() {
		Utility.setRequestHeader("Accept-Encoding", "gzip");
		Utility.setTokenObject(this.requestToken);
	}

	public static Micro4blog getInstance(int serverType) {

		currentServer = serverType;
		if (serverType == SERVER_SINA) {
			micro4blogInstance = Micro4blogForSina.getInstance();
		} else if (serverType == SERVER_TENCENT) {
			micro4blogInstance = Micro4blogForTencent.getInstance();
		} else if (serverType == SERVER_NETEASE) {
			micro4blogInstance = Micro4blogForNetease.getInstance();
		} else if (serverType == SERVER_SOHU) {
			micro4blogInstance = Micro4blogForSohu.getInstance();
		}
		micro4blogInstance.initConfig();

		return micro4blogInstance;
	}

	/**
	 * OAuth第一步，得到未经授权的request token 
	 * @param context
	 * @param parameters
	 */
	protected void getAppRequestToken(Context context,
			Micro4blogParameters parameters) {
		
		RequestTokenHeader header = new RequestTokenHeader();		
		Utility.setAuthorization(header);

		if (Micro4blog.getCurrentServer() != Micro4blog.SERVER_TENCENT) {
			// netease sohu
			if(null == requestToken) {
				requestToken = new RequestToken();
			}		
			try {
				requestToken = getRequestToken(context, Utility.HTTPMETHOD_GET,
						getAppKey(), getAppSecret(), getRedirectUrl());
			} catch (Micro4blogException e) {
				e.printStackTrace();
			}
		} else {
			// tencent
			String result = requestWithGet(header, getUrlRequestToken(),
					parameters, requestToken);
			requestToken = new RequestToken(result);
		}
		
		if (requestToken.getOauthToken() != null) {
			parameters.add("oauth_token", requestToken.getOauthToken());
		}

	}

	/**
	 * OAuth第二步， 得到经过授权qeust token 与未经授权的request token相同 OAuth2.0，使用参数向服务器端进行授权
	 * 
	 * @param context
	 * @param parameters
	 * @param listener
	 */
	protected void getAuthorization(Context context,
			Micro4blogParameters parameters, Micro4blogDialogListener listener) {

		if (Micro4blog.SERVER_SINA == Micro4blog.getCurrentServer()) {
			// sina
			Utility.setAuthorization(new Oauth2AccessTokenHeader());
			
			if (isSessionValid()) {
				parameters.add("access_token", accessToken.getOauthToken());
			}			

		} else {
			// tencent netease sohu
			Utility.setAuthorization(new AccessTokenHeader());
		}
		
		String url = getUrlAccessAuthorize() + "?"
		+ Utility.encodeUrl(parameters);
		
		if (context
				.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
			Utility.showAlert(context, "Error",
					"Application requires permission to access the Internet");
		} else {

			Toast.makeText(context, url, Toast.LENGTH_SHORT).show();

			new Micro4blogDialog(this, context, url, listener).show();
		}

	}

	/**
	 * OAuth第三步，用授权的request token换取access token OAuth2.0 获取access token
	 * 
	 * @param values
	 *            服务器端返回的值，在Utility.parseUrl(url)方法中获得
	 */
	protected void getUserAccessToken(Bundle values) {
		if (null == requestToken) {
			requestToken = new RequestToken();
		}

		requestToken.setOauthToken(values.getString("oauth_token"));
		requestToken.setOauthVerifier(values.getString("oauth_verifier"));

		setRequestToken(requestToken);

		if (Micro4blog.getCurrentServer() != Micro4blog.SERVER_TENCENT) {

			if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_SINA) {
				// sina
				if (null == accessToken) {
					accessToken = new OauthToken();
				}
				accessToken.setOauthToken(values.getString("access_token"));
				accessToken.setExpiresIn(values.getString("expires_in"));
				setAccessToken(accessToken);
				if (isSessionValid()) {
					Log.d(TAG, "Login Success! access_token="
							+ accessToken.getOauthToken() + " expires="
							+ accessToken.getExpiresIn());
					mAuthDialogListener.onComplete(values);
				} else {
					Log.d(TAG, "Failed to receive access token");
					mAuthDialogListener
							.onMicro4blogException(new Micro4blogException(
									"Failed to receive access token."));
				}
			} else {
				// sohu netease
				try {
					accessToken = generateAccessToken(mContext,
							Utility.HTTPMETHOD_GET, requestToken);
				} catch (Micro4blogException e) {
					e.printStackTrace();
				}
				setAccessToken(accessToken);
				mAuthDialogListener.onComplete(values);
			}

		} else {
			// tencent
			AccessTokenHeader header = new AccessTokenHeader();
			Micro4blogParameters parameters = new Micro4blogParameters();
			parameters.add("oauth_verifier", requestToken.getOauthVerifier());
			String result = requestWithGet(header, getUrlAccessToken(),
					parameters, requestToken);
			accessToken = new OauthToken(result);
			setAccessToken(accessToken);
			mAuthDialogListener.onComplete(values);
		}

	}

	public String requestWithGet(HttpHeaderFactory header, String url,
			Micro4blogParameters parameters, OauthToken token) {

		String result = "";

		try {

			Micro4blogParameters params = new Micro4blogParameters();
			// params.add("oauth_verifier", requestToken.getOauthVerifier());

			header.getMicro4blogAuthHeader(this, Utility.HTTPMETHOD_GET, url,
					parameters, getAppKey(), getAppSecret(), token);

			params = header.getAuthParams();

			params.addAll(parameters);

			Log.d(TAG,
					"request with get params: "
							+ Utility.encodeParameters(params));

			// params.add("oauth_callback", getRedirectUrl());

			// result = request(mContext, getUrlAccessToken(), params,
			// Utility.HTTPMETHOD_GET, requestToken);

			result = Utility.openUrl(micro4blogInstance, mContext, url,
					Utility.HTTPMETHOD_GET, params, token);

		} catch (Micro4blogException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String request(Context context, String url,
			Micro4blogParameters params, String httpMethod, OauthToken token)
			throws Micro4blogException {

		String result = Utility.openUrl(micro4blogInstance, context, url,
				httpMethod, params, this.accessToken);
		return result;
	}

	public RequestToken getRequestToken(Context context, String httpMethod,
			String key, String secret, String callback_url)
			throws Micro4blogException {
		Utility.setAuthorization(new RequestTokenHeader());
		Micro4blogParameters params = new Micro4blogParameters();
		params.add("oauth_callback", callback_url);
		String rlt;
		rlt = Utility.openUrl(micro4blogInstance, context,
				micro4blogInstance.getUrlRequestToken(), httpMethod, params,
				null);
		RequestToken request = new RequestToken(rlt);
		this.requestToken = request;
		return request;
	}

	public AccessToken generateAccessToken(Context context, String httpMethod,
			RequestToken requestToken) throws Micro4blogException {
		Utility.setAuthorization(new AccessTokenHeader());
		Micro4blogParameters authParam = new Micro4blogParameters();

		if (requestToken.getOauthVerifier() != null) {
			authParam.add("oauth_verifier",
					this.requestToken.getOauthVerifier());
		} else {
			authParam.add("oauth_token", requestToken.getOauthToken());
		}

		String rlt = Utility.openUrl(micro4blogInstance, context,
				micro4blogInstance.getUrlAccessToken(), httpMethod, authParam,
				this.requestToken);
		AccessToken accessToken = new AccessToken(rlt);
		this.accessToken = accessToken;
		return accessToken;
	}

	public Oauth2AccessToken getOauth2AccessToken(Context context,
			String httpMethod, String app_key, String app_secret,
			String usrname, String password) throws Micro4blogException {
		Utility.setAuthorization(new Oauth2AccessTokenHeader());
		Micro4blogParameters postParams = new Micro4blogParameters();
		postParams.add("username", usrname);
		postParams.add("password", password);
		postParams.add("client_id", app_key);
		postParams.add("client_secret", app_secret);
		postParams.add("grant_type", "password");
		String rlt = Utility.openUrl(micro4blogInstance, context,
				micro4blogInstance.getUrlAccessToken(), httpMethod, postParams,
				null);
		Oauth2AccessToken accessToken = new Oauth2AccessToken(rlt);
		this.accessToken = accessToken;
		return accessToken;
	}

	public boolean share2weibo(Activity activity, String accessToken,
			String tokenSecret, String content, String picPath)
			throws Micro4blogException {
		if (TextUtils.isEmpty(accessToken)) {
			throw new Micro4blogException("token can not be null!");
		}

		if (TextUtils.isEmpty(content) && TextUtils.isEmpty(picPath)) {
			throw new Micro4blogException("weibo content can not be null!");
		}
		Intent i = new Intent(activity, ShareActivity.class);
		i.putExtra(ShareActivity.EXTRA_ACCESS_TOKEN, accessToken);
		i.putExtra(ShareActivity.EXTRA_TOKEN_SECRET, tokenSecret);
		i.putExtra(ShareActivity.EXTRA_MICRO4BLOG_CONTENT, content);
		i.putExtra(ShareActivity.EXTRA_PIC_URI, picPath);
		activity.startActivity(i);

		return true;
	}

	protected boolean startSingleSignOn(Activity activity,
			String applicationId, String[] permissions, int activityCode) {
		return false;
	}

	public void authorize(Activity activity,
			final Micro4blogDialogListener listener) {
		authorize(activity, new String[] {}, DEFAULT_AUTH_ACTIVITY_CODE,
				listener);
	}

	public void authorize(Activity activity, String[] permissions,
			final Micro4blogDialogListener listener) {
		authorize(activity, permissions, DEFAULT_AUTH_ACTIVITY_CODE, listener);
	}

	protected boolean isSessionValid() {
		if (accessToken != null) {
			return (!TextUtils.isEmpty(accessToken.getOauthToken()) && (accessToken
					.getExpiresIn() == 0 || (System.currentTimeMillis() < accessToken
					.getExpiresIn())));
		}
		return false;
	}

	public String getUrlAccessToken() {
		return urlAccessToken;
	}

	public void setUrlAccessToken(String urlAccessToken) {
		this.urlAccessToken = urlAccessToken;
	}

	public RequestToken getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(RequestToken requestToken) {
		this.requestToken = requestToken;
	}

	public OauthToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(OauthToken accessToken) {
		this.accessToken = accessToken;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		Micro4blog.redirectUrl = redirectUrl;
	}

	public String getUrlRequestToken() {
		return urlRequestToken;
	}

	public void setUrlRequestToken(String urlRequestToken) {
		this.urlRequestToken = urlRequestToken;
	}

	public String getUrlAccessAuthorize() {
		return urlAccessAuthorize;
	}

	public void setUrlAccessAuthorize(String urlAccessAuthorize) {
		this.urlAccessAuthorize = urlAccessAuthorize;
	}

	public static int getCurrentServer() {
		return currentServer;
	}

	public static void setCurrentServer(int currentServer) {
		Micro4blog.currentServer = currentServer;
	}

	public static void setServerUrl(String serverUrl) {
		Micro4blog.serverUrl = serverUrl;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * 初始化每个服务的不同参数
	 */
	protected abstract void initConfig();

	/**
	 * 进行服务的授权
	 * @param activity
	 * @param permissions
	 * @param activityCode
	 * @param listener
	 */
	protected abstract void authorize(Activity activity, String[] permissions,
			int activityCode, final Micro4blogDialogListener listener);

	/**
	 * 启动对话框进行授权，主要是对permission进行参数配置
	 * @param activity
	 * @param permissions
	 */
	protected abstract void startDialogAuth(Activity activity,
			String[] permissions);

	/**
	 * 服务的授权对话框，用户授权后，处理授权结果
	 * @param context
	 * @param parameters
	 * @param listener
	 */
	protected abstract void dialog(Context context,
			Micro4blogParameters parameters,
			final Micro4blogDialogListener listener);

	/**
	 * 授权的回调，暂时没用用到
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	protected abstract void authorizeCallBack(int requestCode, int resultCode,
			Intent data);

}
