package com.micro4blog.http;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.micro4blog.oauth.Micro4blog;
import com.micro4blog.oauth.OauthToken;
import com.micro4blog.utils.Micro4blogException;

public class RequestTokenHeader extends HttpHeaderFactory {

	@Override
	public Micro4blogParameters generateSignatureList(
			Micro4blogParameters bundle) {
		if (bundle == null || (bundle.size() == 0)) {
            return null;
        }
		
		// 下面的头部在流程中都要逐步的生成，不然会encode错误，或者无法通过认证
		
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
        
        // 对于Tencent的Oauth参数授权，不需要source       
        if (Micro4blog.getCurrentServer() != Micro4blog.SERVER_TENCENT) {
	        key = "source";
	        pp.add(key, bundle.getValue(key));
        }
        
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
