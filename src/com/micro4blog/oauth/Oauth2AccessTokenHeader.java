package com.micro4blog.oauth;

public class Oauth2AccessTokenHeader extends HttpHeaderFactory {

	@Override
	public String getMicro4blogAuthHeader(Micro4blog micro4blog, String method, String url,
			Micro4blogParameters params, String app_key, String app_secret,
			Token token) throws Micro4blogException {
		if (token == null) {
			return null;
		}
		
		return "Oauth2 " + token.getOauthVerifier();
	}

	@Override
	public Micro4blogParameters generateSignatureList(
			Micro4blogParameters bundle) {
		return null;
	}

	@Override
	public void addAdditionalParams(Micro4blogParameters des,
			Micro4blogParameters src) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String generateSignature(Micro4blog micro4blog, String data, Token token)
			throws Micro4blogException {
		return "";
	}
	
	

}
