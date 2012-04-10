package com.micro4blog.http;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.micro4blog.oauth.Micro4blog;
import com.micro4blog.oauth.OauthToken;
import com.micro4blog.utils.Micro4blogException;

public class AccessTokenHeader extends HttpHeaderFactory {

	@Override
	public Micro4blogParameters generateSignatureList(
			Micro4blogParameters bundle) {
		 if (bundle == null || (bundle.size() == 0)) {
	            return null;
	        }
        Micro4blogParameters pp = new Micro4blogParameters();
        String key = "oauth_consumer_key";
        pp.add(key, bundle.getValue(key));
        key = "oauth_nonce";
        pp.add(key, bundle.getValue(key));
        key = "oauth_signature_method";
        pp.add(key, bundle.getValue(key));
        key = "oauth_timestamp";
        pp.add(key, bundle.getValue(key));
        key = "oauth_token";
        pp.add(key, bundle.getValue(key));
        
        if (Micro4blog.getCurrentServer() != Micro4blog.SERVER_NETEASE) {
            key = "oauth_verifier";
            pp.add(key, bundle.getValue(key));            
           
        }
        
        key = "oauth_version";
        pp.add(key, bundle.getValue(key));
               
 
        
        if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_SINA) {
	        key = "source";
	        pp.add(key, bundle.getValue(key));
        }
        
        return pp;
	}

	@Override
	public String generateSignature(Micro4blog micro4blog, String data, OauthToken token)
			throws Micro4blogException {
		byte[] byteHMAC = null;
		
        try {
            Mac mac = Mac.getInstance(HttpHeaderFactory.CONST_HMAC_SHA1);
            SecretKeySpec spec = null;
            if (null == token.getSecretKeySpec()) {
                String oauthSignature = encode(micro4blog.getAppSecret()) + "&"
                        + encode(token.getOauthTokenSecret());
                spec = new SecretKeySpec(oauthSignature.getBytes(),
                        HttpHeaderFactory.CONST_HMAC_SHA1);
                token.setSecretKeySpec(spec);
            }
            spec = token.getSecretKeySpec();
            mac.init(spec);
            byteHMAC = mac.doFinal(data.getBytes());
        } catch (InvalidKeyException e) {
            throw new Micro4blogException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new Micro4blogException(e);
        }
        return String.valueOf(Utility.base64Encode(byteHMAC));
	}
	
	@Override
	public void addAdditionalParams(Micro4blogParameters des,
			Micro4blogParameters src) {
			
	}

}
