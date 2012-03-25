package com.micro4blog.oauth;

import org.json.JSONException;
import org.json.JSONObject;

public class Oauth2AccessToken extends Token {

	public Oauth2AccessToken(String resultStr) {
		if (resultStr != null) {
			if (resultStr.indexOf("{") > 0) {
				try {
					JSONObject json = new JSONObject(resultStr);
					setTokenOauthOrAccess(json.getString("access_token"));
					setExpiresIn(json.getString("expires_in"));
					setTokenRefresh(json.getString("refresh_token"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public Oauth2AccessToken(String token, String secret) {
		super(token, secret);
	}
}
