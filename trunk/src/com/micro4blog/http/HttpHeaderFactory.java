/*
 * Copyright 2011 Sina.
 *
 * Licensed under the Apache License and Weibo License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.open.weibo.com
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.micro4blog.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Random;

import com.micro4blog.oauth.Micro4blog;
import com.micro4blog.oauth.OauthToken;
import com.micro4blog.utils.Micro4blogException;

import android.os.Bundle;
import android.util.Log;

/**
 * Encapsulation a abstract weibo http headers base class.
 * 
 */

public abstract class HttpHeaderFactory {
	
	private static final String TAG = "HttpHeaderFactory";
	
    public static final String CONST_HMAC_SHA1 = "HmacSHA1";
    public static final String CONST_SIGNATURE_METHOD = "HMAC-SHA1";
    public static final String CONST_OAUTH_VERSION = "1.0";
    public static final String CONST_OAUTH_VERSION_A = "1.0a";
    
    private Micro4blogParameters authParams;
    private Micro4blogParameters signatureParams;

    public HttpHeaderFactory() {
    }

    public String getMicro4blogAuthHeader(Micro4blog micro4blog, String method, String url, Micro4blogParameters params,
            String app_key, String app_secret, OauthToken token) throws Micro4blogException {
        
    	// step 1: generate timestamp and nonce
        final long timestamp = System.currentTimeMillis() / 1000;
        final long nonce = timestamp + (new Random()).nextInt();
//        Random random = new Random();
//        long nonce = (random.nextInt(9876599) + 123400);
        
        // step 2: authParams有两个用处：1.加密串一部分 2.生成最后Authorization头域
        authParams = this.generateAuthParameters(micro4blog, nonce, timestamp, token);
        Log.d(TAG, "authParams before : " + Utility.encodeParameters(authParams));

        signatureParams = this.generateSignatureParameters(micro4blog, authParams, params, url);
        Log.d(TAG, "signatureParams: " + Utility.encodeParameters(signatureParams));
        
        // step 3: 生成用于签名的base String
        String oauthBaseString = this.generateAuthSignature(method, signatureParams, url, token);
        
        // step 4: 生成oauth_signature
        String signature = generateSignature(micro4blog, oauthBaseString, token);
        authParams.add("oauth_signature", signature);
        Log.d(TAG, "authParams after : " + Utility.encodeParameters(authParams));       
        
        // step 5: for additional parameters
        this.addAdditionalParams(authParams, params);
        
        return "OAuth " + encodeParameters(authParams, ",", true);
    }

    private String generateAuthSignature(final String method, Micro4blogParameters signatureParams,
            final String url, OauthToken token) {
    	
    	if (token != null && token.getOauthVerifier() != null) {
    		signatureParams.add("oauth_verifier", token.getOauthVerifier());
    	}
    	
        StringBuffer base = new StringBuffer(method).append("&")
                .append(encode(constructRequestURL(url))).append("&");
        base.append(encode(encodeParameters(signatureParams, "&", false)));
        String oauthBaseString = base.toString();
        return oauthBaseString;
    }

    protected Micro4blogParameters generateSignatureParameters(Micro4blog micro4blog, Micro4blogParameters authParams,
    		Micro4blogParameters params, String url) throws Micro4blogException {
    	Micro4blogParameters signatureParams = new Micro4blogParameters();
        signatureParams.addAll(authParams);
        signatureParams.add("source", micro4blog.getAppKey());
        
        // 不行，generateSignatureList将其过滤了
//        if (micro4blog.getAccessToken().getOauthVerifier() != null) {
//        	signatureParams.add("oauth_verifier", micro4blog.getAccessToken().getOauthVerifier());
//        }
        
        signatureParams.addAll(params);
        this.parseUrlParameters(url, signatureParams);
        Micro4blogParameters lsp = generateSignatureList(signatureParams);
        return lsp;
    }

    private Micro4blogParameters generateAuthParameters(Micro4blog micro4blog, long nonce, long timestamp, OauthToken token) {
    	Micro4blogParameters authParams = new Micro4blogParameters();
     	// 腾讯微博使用URL参数方式
    	if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_TENCENT) {
    	
    		if (token == null) {
    			authParams.add("oauth_callback", micro4blog.getRedirectUrl()); 
    		} else {
    			authParams.add("oauth_verifier", token.getOauthVerifier());
    		}
          		
    	}
    	authParams.add("oauth_consumer_key", micro4blog.getAppKey());
        authParams.add("oauth_nonce", String.valueOf(nonce));
        authParams.add("oauth_signature_method", HttpHeaderFactory.CONST_SIGNATURE_METHOD);
        authParams.add("oauth_timestamp", String.valueOf(timestamp));
        authParams.add("oauth_version", HttpHeaderFactory.CONST_OAUTH_VERSION);
        if (token != null) {
            authParams.add("oauth_token", token.getOauthToken());
            
            // 为了使用access token，生成signature
            // 不能在这里加，不能产生access token
//            authParams.add("oauth_verifier", token.getOauthVerifier());
            
            // 放入generateSignatureParameters
            
        } else {
            authParams.add("source", micro4blog.getAppKey());
        }
        
       
        return authParams;
    }

   
    // 解析url中参数对,存储到signatureBaseParams
    public void parseUrlParameters(String url, Micro4blogParameters signatureBaseParams)
            throws Micro4blogException {
        int queryStart = url.indexOf("?");
        if (-1 != queryStart) {
            String[] queryStrs = url.substring(queryStart + 1).split("&");
            try {
                for (String query : queryStrs) {
                    String[] split = query.split("=");
                    if (split.length == 2) {
                        signatureBaseParams.add(URLDecoder.decode(split[0], "UTF-8"),
                                URLDecoder.decode(split[1], "UTF-8"));
                    } else {
                        signatureBaseParams.add(URLDecoder.decode(split[0], "UTF-8"), "");
                    }
                }
            } catch (UnsupportedEncodingException e) {
                throw new Micro4blogException(e);
            }
        }
    }
     
    public static String encodeParameters(Micro4blogParameters postParams, String splitter, boolean quot) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < postParams.size(); i++) {
            if (buf.length() != 0) {
                if (quot) {
                    buf.append("\"");
                }
                buf.append(splitter);
            }
            buf.append(encode(postParams.getKey(i))).append("=");
            if (quot) {
                buf.append("\"");
            }
            buf.append(encode(postParams.getValue(i)));
        }
        if (buf.length() != 0) {
            if (quot) {
                buf.append("\"");
            }
        }
        return buf.toString();
    }

    public static String encodeParameters(Bundle postParams, String split, boolean quot) {
        final String splitter = split;
        StringBuffer buf = new StringBuffer();
        for (String key : postParams.keySet()) {
            if (buf.length() != 0) {
                if (quot) {
                    buf.append("\"");
                }
                buf.append(splitter);
            }
            buf.append(encode(key)).append("=");
            if (quot) {
                buf.append("\"");
            }
            buf.append(encode(postParams.getString(key)));
        }
        if (buf.length() != 0) {
            if (quot) {
                buf.append("\"");
            }
        }
        return buf.toString();
    }

    //
    public static String constructRequestURL(String url) {
        int index = url.indexOf("?");
        if (-1 != index) {
            url = url.substring(0, index);
        }
        int slashIndex = url.indexOf("/", 8);
        String baseURL = url.substring(0, slashIndex).toLowerCase();
        int colonIndex = baseURL.indexOf(":", 8);
        if (-1 != colonIndex) {
            // url contains port number
            if (baseURL.startsWith("http://") && baseURL.endsWith(":80")) {
                // http default port 80 MUST be excluded
                baseURL = baseURL.substring(0, colonIndex);
            } else if (baseURL.startsWith("https://") && baseURL.endsWith(":443")) {
                // http default port 443 MUST be excluded
                baseURL = baseURL.substring(0, colonIndex);
            }
        }
        url = baseURL + url.substring(slashIndex);

        return url;
    }

    /**
     * @param value
     *            string to be encoded
     * @return encoded parameters string
     */
    public static String encode(String value) {
        String encoded = null;
                      
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
        }
        StringBuffer buf = new StringBuffer(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%' && (i + 1) < encoded.length() && encoded.charAt(i + 1) == '7'
                    && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        return buf.toString();
    }
    
    // 生成用于哈希的base string串，注意要按顺序，按需文档需求参数生成，否则40107错误
    public abstract Micro4blogParameters generateSignatureList(Micro4blogParameters bundle);

    public abstract String generateSignature(Micro4blog micro4blog, String data, OauthToken token) throws Micro4blogException;

 // add additional parameters to des key-value pairs,support to expanding
    // params
    public abstract void addAdditionalParams(Micro4blogParameters des, Micro4blogParameters src);
    
	public Micro4blogParameters getAuthParams() {
		return authParams;
	}

	public void setAuthParams(Micro4blogParameters authParams) {
		this.authParams = authParams;
	}

	public Micro4blogParameters getSignatureParams() {
		return signatureParams;
	}

	public void setSignatureParams(Micro4blogParameters signatureParams) {
		this.signatureParams = signatureParams;
	}
    
    


}
