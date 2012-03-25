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

public abstract class Micro4blog {
	
	// TODO 需要设计，兼容4大微博
	
	public static final int SERVER_SINA = 0;
	public static final int SERVER_TENCENT = 1;
	public static final int SERVER_NETEASE = 2;
	public static final int SERVER_SOHU = 3;

	public static String SERVER = null;
	
	public static String TOKEN = "access_token";
	public static String EXPIRES = "expires_in";
	
	public static int DEFAULT_AUTH_ACTIVITY_CODE = 0;
	
	private String urlRequestToken = null;
	private String urlAccessToken = null;
	private String urlAccessAuthorize = null;
	
	private static String appKey;
	private static String appSecret;
	
	private static String redirectUrl;
	
	private static Micro4blog micro4blogInstance;
	
//	private String urlOauth2AccessToken = null;
//	public static String urlOauth2AccessAuthorize = null;
	
	private RequestToken requestToken;
	protected static Token accessToken;
	
	protected Micro4blogDialogListener mAuthDialogListener;
	
	protected abstract void initConfig();
	
	public Micro4blog() {
		Utility.setRequestHeader("Accept-Encoding", "gzip");
		Utility.setTokenObject(this.requestToken);
	}
	
	public static Micro4blog getInstance(int serverType) {
		if (serverType == SERVER_SINA) {
			micro4blogInstance = new Micro4blogForSina();
		} else if (serverType == SERVER_TENCENT) {
			micro4blogInstance = new Micro4blogForTencent();
		} else if (serverType == SERVER_NETEASE) {
			micro4blogInstance = new Micro4blogForNetease();
		} else if (serverType == SERVER_SOHU) {
			micro4blogInstance = new Micro4blogForSohu();
		}
		
		micro4blogInstance.initConfig();
		
		return micro4blogInstance;
	}

	public String request(Context context, String url, Micro4blogParameters params, String httpMethod,
            Token token) throws Micro4blogException {
        String rlt = Utility.openUrl(micro4blogInstance, context, url, httpMethod, params, this.accessToken);
        return rlt;
    }

    public RequestToken getRequestToken(Context context, String key, String secret,
            String callback_url) throws Micro4blogException {
        Utility.setAuthorization(new RequestTokenHeader());
        Micro4blogParameters postParams = new Micro4blogParameters();
        postParams.add("oauth_callback", callback_url);
        String rlt;
        rlt = Utility.openUrl(micro4blogInstance, context, micro4blogInstance.urlAccessToken, "POST", postParams, null);
        RequestToken request = new RequestToken(rlt);
        this.requestToken = request;
        return request;
    }

    public AccessToken generateAccessToken(Context context, RequestToken requestToken)
            throws Micro4blogException {
        Utility.setAuthorization(new AccessTokenHeader());
        Micro4blogParameters authParam = new Micro4blogParameters();
        authParam.add("oauth_verifier", this.requestToken.getOauthVerifier()/* "605835" */);
        authParam.add("source", appKey);
        String rlt = Utility.openUrl(micro4blogInstance, context, micro4blogInstance.urlAccessAuthorize, "POST", authParam,
                this.requestToken);
        AccessToken accessToken = new AccessToken(rlt);
        this.accessToken = accessToken;
        return accessToken;
    }  

    public Oauth2AccessToken getOauth2AccessToken(Context context, String app_key,
            String app_secret, String usrname, String password) throws Micro4blogException {
        Utility.setAuthorization(new Oauth2AccessTokenHeader());
        Micro4blogParameters postParams = new Micro4blogParameters();
        postParams.add("username", usrname);
        postParams.add("password", password);
        postParams.add("client_id", app_key);
        postParams.add("client_secret", app_secret);
        postParams.add("grant_type", "password");
        String rlt = Utility.openUrl(micro4blogInstance, context, micro4blogInstance.urlAccessToken, "POST", postParams,
                null);
        Oauth2AccessToken accessToken = new Oauth2AccessToken(rlt);
        this.accessToken = accessToken;
        return accessToken;
    }

    public boolean share2weibo(Activity activity, String accessToken, String tokenSecret,
            String content, String picPath) throws Micro4blogException {
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

    protected boolean startSingleSignOn(Activity activity, String applicationId,
            String[] permissions, int activityCode) {
        return false;
    }

    public void authorize(Activity activity, final Micro4blogDialogListener listener) {
        authorize(activity, new String[] {}, DEFAULT_AUTH_ACTIVITY_CODE, listener);
    }

    public void authorize(Activity activity, String[] permissions,
            final Micro4blogDialogListener listener) {
        authorize(activity, permissions, DEFAULT_AUTH_ACTIVITY_CODE, listener);
    }

    protected abstract void authorize(Activity activity, String[] permissions, int activityCode,
            final Micro4blogDialogListener listener);
    
    protected abstract void startDialogAuth(Activity activity, String[] permissions);

    protected abstract void dialog(Context context, Micro4blogParameters parameters,
            final Micro4blogDialogListener listener);
    
    protected abstract void authorizeCallBack(int requestCode, int resultCode, Intent data);

    
    protected boolean isSessionValid() {
        if (accessToken != null) {
        	Log.i("thom", "accessToken " + accessToken);
            return (!TextUtils.isEmpty(accessToken.getTokenOauthOrAccess()) && (accessToken.getExpiresIn() == 0 || (System
                    .currentTimeMillis() < accessToken.getExpiresIn())));
        }
        return false;
    }
    
	//==========================================
	/*
	 * 
	 */
	//==========================================

	public String getUrlAccessToken() {
		return urlAccessAuthorize;
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

	public static Token getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(Token accessToken) {
		Micro4blog.accessToken = accessToken;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		Micro4blog.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		Micro4blog.appSecret = appSecret;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public  void setRedirectUrl(String redirectUrl) {
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

	
}
