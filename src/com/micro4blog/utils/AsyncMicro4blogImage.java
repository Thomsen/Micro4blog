package com.micro4blog.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class AsyncMicro4blogImage {
	
	// 针对GC，强、软、弱、虚引用
	private Map<String, SoftReference<Drawable>> mDrawableCache;
	
	public AsyncMicro4blogImage() {
		mDrawableCache = new HashMap<String, SoftReference<Drawable>>();
	}
	
	
	public Drawable loadDrawable(final String imageUrl, final ImageCallback imageCallback) {
		if (mDrawableCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softDrawable = mDrawableCache.get(imageUrl);
			Drawable drawable = softDrawable.get();
			
			if( drawable != null) {
				return drawable;
			}
		}
		
		final Handler handler = new Handler() {
			
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
			
		};
		
		new Thread() {
			
			public void run() {
				Drawable drawable = loadDrawableFormUrl(imageUrl);
				mDrawableCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
			
		}.start();
		
		return null;
	}
	
	private Drawable loadDrawableFormUrl(String imageUrl) {

		URL fileUrl = null;
		Drawable drawable = null;
		
		try {
			fileUrl = new URL(imageUrl);
			
			HttpURLConnection httpConn = (HttpURLConnection) fileUrl.openConnection();
			httpConn.setDoInput(true);
			httpConn.connect();
			
			InputStream inStream = httpConn.getInputStream();
			
			Bitmap bitmap = BitmapFactory.decodeStream(inStream);
			
			// bitmap to drawable
			drawable = new BitmapDrawable(bitmap);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return drawable;
	}
	
	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}

}
