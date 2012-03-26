package com.micro4blog.oauth;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class RequestTokenHeader extends HttpHeaderFactory {

	@Override
	public Micro4blogParameters generateSignatureList(
			Micro4blogParameters bundle) {
		if (bundle == null || (bundle.size() == 0)) {
            return null;
        }
        Micro4blogParameters pp = new Micro4blogParameters();
        String key = "oauth_callback";
        pp.add(key, bundle.getValue(key));
        key = "oauth_consumer_key";
        pp.add(key, bundle.getValue(key));
        key = "oauth_nonce";
        pp.add(key, bundle.getValue(key));
        key = "oauth_signature_method";
        pp.add(key, bundle.getValue(key));
        key = "oauth_timestamp";
        pp.add(key, bundle.getValue(key));
        key = "oauth_version";
        pp.add(key, bundle.getValue(key));
//        key = "source";
//        pp.add(key, bundle.getValue(key));
        return pp;
	}

	@Override
	public void addAdditionalParams(Micro4blogParameters des,
			Micro4blogParameters src) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String generateSignature(Micro4blog micro4blog, String data, OauthToken token)
			throws Micro4blogException {
		byte[] byteHMAC = null;
        try {
            Mac mac = Mac.getInstance(HttpHeaderFactory.CONST_HMAC_SHA1);
            SecretKeySpec spec = null;
            String oauthSignature = encode(micro4blog.getAppSecret()) + "&";
            spec = new SecretKeySpec(oauthSignature.getBytes(), HttpHeaderFactory.CONST_HMAC_SHA1);
            mac.init(spec);
            byteHMAC = mac.doFinal(data.getBytes());
        } catch (InvalidKeyException e) {
            throw new Micro4blogException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new Micro4blogException(e);
        }
        return String.valueOf(Utility.base64Encode(byteHMAC));
	}

}
