package com.micro4blog.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.graphics.drawable.Drawable;

public class AsyncMicro4blogImage {
	
	// 针对GC，强、软、弱、虚引用
	private Map<String, SoftReference<Drawable>> mDrawableCache;
	
	public AsyncMicro4blogImage() {
		mDrawableCache = new HashMap<String, SoftReference<Drawable>>();
	}
	
	
	

}
