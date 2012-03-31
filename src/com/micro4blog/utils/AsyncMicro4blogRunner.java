/*
 * Copyright 2011 Sina.
 *
 * Licensed under the Apache License and Micro4blog License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.open.Micro4blog.com
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.micro4blog.utils;

import java.io.IOException;

import com.micro4blog.http.Micro4blogParameters;
import com.micro4blog.oauth.Micro4blog;
import com.micro4blog.tests.ShareActivity;

import android.content.Context;


/**
 * Encapsulation main Micro4blog APIs, Include: 1. getRquestToken , 2. getAccessToken, 3. url request.
 * Implements a Micro4blog api as a asynchronized way. Every object used this runner should implement interface RequestListener.
 *
 * @author  ZhangJie (zhangjie2@staff.sina.com.cn)
 */
public class AsyncMicro4blogRunner {
	
	private Micro4blog mMicro4blog;
	
	public AsyncMicro4blogRunner(Micro4blog Micro4blog){
		this.mMicro4blog = Micro4blog;
	}
	
	public void request(final Context context, 
			final String url, 
			final Micro4blogParameters params, 
			final String httpMethod, 
			final RequestListener listener){
		new Thread(){
			@Override public void run() {
                try {
					String resp = mMicro4blog.request(context, url, params, httpMethod, mMicro4blog.getAccessToken());
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


	public void request(ShareActivity context, String url,
			Micro4blogParameters bundle, String httpmethodPost,
			ShareActivity shareActivity) {
		// TODO Auto-generated method stub
		
	}
	
}
