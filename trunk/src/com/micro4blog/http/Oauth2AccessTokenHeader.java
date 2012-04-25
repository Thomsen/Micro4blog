package com.micro4blog.http;

import com.micro4blog.Micro4blog;
import com.micro4blog.oauth.OauthToken;
import com.micro4blog.utils.Micro4blogException;

public class Oauth2AccessTokenHeader extends HttpHeaderFactory {

	@Override
	public String getMicro4blogAuthHeader(Micro4blog micro4blog, String method, String url,
			Micro4blogParameters params, String app_key, String app_secret,
			OauthToken token) throws Micro4blogException {
		if (token == null) {
			return null;
		}
		
		// 如果这里的OAuth2写成了Oauth2，则一直出现auth faild 21301
		return "OAuth2 " + token.getOauthToken();
	}

	@Override
	public Micro4blogParameters generateSignatureList(
			Micro4blogParameters bundle) {
		return null;
	}

	@Override
	public void addAdditionalParams(Micro4blogParameters des,
			Micro4blogParameters src) {
				
	}

	@Override
	public String generateSignature(Micro4blog micro4blog, String data, OauthToken token)
			throws Micro4blogException {
		return "";
	}
	
	

}
