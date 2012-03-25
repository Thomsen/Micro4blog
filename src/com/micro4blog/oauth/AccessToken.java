package com.micro4blog.oauth;

public class AccessToken extends Token {
	
	public AccessToken(String resultStr) {
		super(resultStr);
	}

	public AccessToken(String token, String secret) {
		super(token, secret);
	}
}
