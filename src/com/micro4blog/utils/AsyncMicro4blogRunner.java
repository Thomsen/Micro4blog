package com.micro4blog.utils;

import java.io.IOException;

import android.content.Context;

import com.micro4blog.http.Micro4blogParameters;
import com.micro4blog.oauth.Micro4blog;

/**
 * 
 * 封装Api请求时需要的授权参数
 *
 */
public class AsyncMicro4blogRunner {
	
	private Micro4blog micro4blog;
	
	public AsyncMicro4blogRunner(Micro4blog micro4blog){
		this.micro4blog = micro4blog;
	}
	
	public void request(final Context context, 
			final String url, 
			final Micro4blogParameters params, 
			final String httpMethod, 
			final RequestListener listener){
		new Thread(){
			@Override public void run() {
                try {
					String resp = micro4blog.request(context, url, params, httpMethod, micro4blog.getAccessToken());
                    listener.onComplete(resp);
                } catch (Micro4blogException e) {
                    listener.onError(e);
                }
            }
		}.run();
		
	}
	
	
    public static interface RequestListener {

        public void onComplete(String response);

        public void onIOException(IOException e);

        public void onError(Micro4blogException e);

    }

	
}
