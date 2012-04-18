package com.micro4blog.oauth;

import java.util.ArrayList;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.micro4blog.data.Micro4blogInfo;
import com.micro4blog.dialog.Micro4blogDialog;
import com.micro4blog.dialog.Micro4blogDialogListener;
import com.micro4blog.http.AccessTokenHeader;
import com.micro4blog.http.ApiTokenHeader;
import com.micro4blog.http.HttpHeaderFactory;
import com.micro4blog.http.Micro4blogParameters;
import com.micro4blog.http.Oauth2AccessTokenHeader;
import com.micro4blog.http.RequestTokenHeader;
import com.micro4blog.http.Utility;
import com.micro4blog.server.Micro4blogForNetease;
import com.micro4blog.server.Micro4blogForSina;
import com.micro4blog.server.Micro4blogForSohu;
import com.micro4blog.server.Micro4blogForTencent;
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
	
	private String serverUrl = "";

	// 应用于Api的调用参数
	protected String apiUrl = "";
	protected String apiResult = "";
	protected Micro4blogParameters apiParameters;
	protected ApiTokenHeader apiHeader;

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
		
		micro4blogInstance.apiParameters = new Micro4blogParameters();
		micro4blogInstance.apiHeader = new ApiTokenHeader();
		
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
			String result = request(header, Utility.HTTPMETHOD_GET, getUrlRequestToken(),
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
	 * OAuth第三步，用授权的request token换取access token 
	 * OAuth2.0 获取access token
	 * @param values
	 *       服务器端返回的值，在Utility.parseUrl(url)方法中获得
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
			String result = request(header, Utility.HTTPMETHOD_GET, getUrlAccessToken(),
					parameters, requestToken);
			accessToken = new OauthToken(result);
			setAccessToken(accessToken);
			mAuthDialogListener.onComplete(values);
		}

	}

	/**
	 * 提供tencent和netease sohu api调用时的请求方式
	 * @param header 设置参数的类型，继承之HttpHeaderFactory
	 * @param url	请求的URL
	 * @param parameters	请求的参数
	 * @param token	请求的oauth token
	 * @return
	 */
	public String request(HttpHeaderFactory header, String httpMethod, String url,
			Micro4blogParameters parameters, OauthToken token) {

		String result = "";
		try {
			Micro4blogParameters params = new Micro4blogParameters();
			header.getMicro4blogAuthHeader(this, httpMethod, url,
					parameters, getAppKey(), getAppSecret(), token);
			params = header.getAuthParams();
			params.addAll(parameters);
			
			Log.d(TAG, "request with get params: " + Utility.encodeParameters(params));
			
			result = Utility.openUrl(micro4blogInstance, mContext, url,
					Utility.HTTPMETHOD_GET, params, token);

		} catch (Micro4blogException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 没有对参数进行处理的请求方式
	 * @param context
	 * @param url
	 * @param params
	 * @param httpMethod
	 * @param token
	 * @return
	 */
	public String request(Context context, String url,
			Micro4blogParameters params, String httpMethod, OauthToken token) {
		String result = "";
		try {
			result = Utility.openUrl(micro4blogInstance, context, url,
					httpMethod, params, token);
		} catch (Micro4blogException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * oauth1.0中获取request token
	 * @param context
	 * @param httpMethod
	 * @param key
	 * @param secret
	 * @param callback_url
	 * @return
	 * @throws Micro4blogException
	 */
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

	/**
	 * oauth1.0中使用request token换取access token
	 * @param context
	 * @param httpMethod
	 * @param requestToken
	 * @return
	 * @throws Micro4blogException
	 */
	public AccessToken generateAccessToken(Context context, String httpMethod,
			RequestToken requestToken) throws Micro4blogException {
		Utility.setAuthorization(new AccessTokenHeader());
		Micro4blogParameters authParam = new Micro4blogParameters();

		// 针对netease中没有获取oauth_verifier的处理
		if (requestToken.getOauthVerifier() != null) {
			authParam.add("oauth_verifier", requestToken.getOauthVerifier());
		} else {
			authParam.add("oauth_token", requestToken.getOauthToken());
		}

		String result = Utility.openUrl(micro4blogInstance, context,
				micro4blogInstance.getUrlAccessToken(), httpMethod, authParam,
				this.requestToken);
		AccessToken accessToken = new AccessToken(result);
		this.accessToken = accessToken;
		return accessToken;
	}

	/**
	 * oauth2.0获取服务器返回的access token，暂时在应用中还没用到
	 * @param context
	 * @param httpMethod
	 * @param app_key
	 * @param app_secret
	 * @param usrname
	 * @param password
	 * @return
	 * @throws Micro4blogException
	 */
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
		String result = Utility.openUrl(micro4blogInstance, context,
				micro4blogInstance.getUrlAccessToken(), httpMethod, postParams,
				null);
		Oauth2AccessToken accessToken = new Oauth2AccessToken(result);
		this.accessToken = accessToken;
		return accessToken;
	}

	/**
	 * 判断是进行单用户登录，暂时没有处理
	 * @param activity
	 * @param applicationId
	 * @param permissions
	 * @param activityCode
	 * @return
	 */
	protected boolean startSingleSignOn(Activity activity,
			String applicationId, String[] permissions, int activityCode) {
		return false;
	}

	/**
	 * 进行服务的授权
	 * @param activity
	 * @param listener
	 */
	public void authorize(Activity activity,
			final Micro4blogDialogListener listener) {
		authorize(activity, new String[] {}, DEFAULT_AUTH_ACTIVITY_CODE,
				listener);
	}

	/**
	 * 进行服务授权，添加允许的权限
	 * @param activity
	 * @param permissions
	 * @param listener
	 */
	public void authorize(Activity activity, String[] permissions,
			final Micro4blogDialogListener listener) {
		authorize(activity, permissions, DEFAULT_AUTH_ACTIVITY_CODE, listener);
	}

	/**
	 * 判断当前会话是否过期
	 * @return
	 */
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

	public void setRedirectUrl(String url) {
		redirectUrl = url;
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

	public static void setCurrentServer(int server) {
		currentServer = server;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getServerUrl() {
		return serverUrl;
	}
	
	/**
	 * sina oauth2.0 发布微博的例子
	 * @param activity
	 * @param accessToken
	 * @param tokenSecret
	 * @param content
	 * @param picPath
	 * @return
	 * @throws Micro4blogException
	 */
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

	/**
	 * 初始化每个服务的不同常量
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

	//---------------------------------------------------------------------
	/*
	 * API调用部分
	 */
	//---------------------------------------------------------------------
	
	public abstract String getHomeTimeline(Context context);
	
	public abstract ArrayList<Micro4blogInfo> parseHomeTimeline(String message);
}
