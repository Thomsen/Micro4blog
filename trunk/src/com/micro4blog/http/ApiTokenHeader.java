package com.micro4blog.http;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

import com.micro4blog.oauth.Micro4blog;
import com.micro4blog.oauth.OauthToken;
import com.micro4blog.utils.Micro4blogException;

public class ApiTokenHeader extends HttpHeaderFactory {
	
	private static final String TAG = "ApiTokenHeader";

	@Override
	public Micro4blogParameters generateSignatureList(
			Micro4blogParameters bundle) {
	
		Micro4blogParameters mp = new Micro4blogParameters();
		String key = "";
		if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_TENCENT) {
			key = "format";
			mp.add(key, bundle.getValue(key));
		}
		
		key = "oauth_consumer_key";
		mp.add(key, bundle.getValue(key));
        key = "oauth_nonce";
        mp.add(key, bundle.getValue(key));
//        key = "oauth_signature";
//        mp.add(key, bundle.getValue(key));
        key = "oauth_signature_method";
        mp.add(key, bundle.getValue(key));
        key = "oauth_timestamp";
        mp.add(key, bundle.getValue(key));
        key = "oauth_token";
        mp.add(key, bundle.getValue(key));    
        
        // 获取用户信息时
//        if (Micro4blog.getCurrentServer() != Micro4blog.SERVER_NETEASE) {
//        	key = "oauth_verifier";
//        	mp.add(key, bundle.getValue(key));
//        }
        
        key = "oauth_version";
        mp.add(key, bundle.getValue(key));
        
//        key = "source";
//        mp.add(key, bundle.getValue(key));
		
		
		return mp;
	}


	@Override
	public String generateSignature(Micro4blog micro4blog, String data,
			OauthToken token) throws Micro4blogException {
		
	byte[] byteHMAC = null;
		
		// Message Authentication Code
		try {
			Mac mac = Mac.getInstance(CONST_HMAC_SHA1);
			SecretKeySpec spec = null;
			
			// 判断token中是否已有了密钥提供者
			if(null == token.getSecretKeySpec()) {
				String oauthSignature = encode(micro4blog.getAppSecret()) + "&"
						+ encode(token.getOauthTokenSecret());
				
				Log.d(TAG, "o s: " + oauthSignature);
				
				spec = new SecretKeySpec(oauthSignature.getBytes(), CONST_HMAC_SHA1);
				token.setSecretKeySpec(spec);
			}
			
			Log.d(TAG, "d t: " + data);
			
			spec = token.getSecretKeySpec();
			mac.init(spec);
			byteHMAC = mac.doFinal(data.getBytes());
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return String.valueOf(Utility.base64Encode(byteHMAC));
	}
	
	@Override
	public void addAdditionalParams(Micro4blogParameters des,
			Micro4blogParameters src) {

	}
}
