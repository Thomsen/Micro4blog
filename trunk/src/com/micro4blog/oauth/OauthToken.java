package com.micro4blog.oauth;

import javax.crypto.spec.SecretKeySpec;

public class OauthToken {
	
	private String tokenOauthOrAccess;
	private String tokenRefresh;
	private long expiresIn;
	private String oauthVerifier;
	private String oauthTokenSecret;
	private String[] responseStr;
	
	protected SecretKeySpec secretKeySpec;
	
	public OauthToken() {
		
	}
	
	public OauthToken(String resultStr) {
		responseStr = resultStr.split("&");
		oauthTokenSecret = getParameter("oauth_token_secret");
		tokenOauthOrAccess = getParameter("oauth_token");
	}
	
	public OauthToken(String token, String secret) {
		oauthTokenSecret = secret;
		tokenOauthOrAccess = token;
	}
	
	private String getParameter(String params) {
		String value = null;
		
		for (String str : responseStr) {
			if (str.startsWith(params + "=")) {
				value = str.split("=")[1].trim();
			}
		}
		
		return value;
	}

	public String getTokenOauthOrAccess() {
		return tokenOauthOrAccess;
	}

	public void setTokenOauthOrAccess(String tokenOauthOrAccess) {
		this.tokenOauthOrAccess = tokenOauthOrAccess;
	}

	public String getTokenRefresh() {
		return tokenRefresh;
	}

	public void setTokenRefresh(String tokenRefresh) {
		this.tokenRefresh = tokenRefresh;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}
	
	public void setExpiresIn(String expiresStr) {
		if (expiresStr != null && ! expiresStr.equals("0")) {
			setExpiresIn(System.currentTimeMillis() + Integer.valueOf(expiresStr) * 1000);
		}
	}

	public String getOauthVerifier() {
		return oauthVerifier;
	}

	public void setOauthVerifier(String oauthVerifier) {
		this.oauthVerifier = oauthVerifier;
	}

	public String getOauthTokenSecret() {
		return oauthTokenSecret;
	}

	public void setOauthTokenSecret(String oauthTokenSecret) {
		this.oauthTokenSecret = oauthTokenSecret;
	}

	public String[] getResponseStr() {
		return responseStr;
	}

	public void setResponseStr(String[] responseStr) {
		this.responseStr = responseStr;
	}

	public SecretKeySpec getSecretKeySpec() {
		return secretKeySpec;
	}

	public void setSecretKeySpec(SecretKeySpec secretKeySpec) {
		this.secretKeySpec = secretKeySpec;
	}
	
	
	

}
