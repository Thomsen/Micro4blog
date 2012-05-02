package com.micro4blog;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class Micro4blogApplication extends Application {
	
	private static Context m4bContext;
	
//	PluginImpl mPluginImpl;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate();
		
		m4bContext = this;
		
//		mPluginImpl = new PluginImpl(m4bContext);
//		mPluginImpl.useDexClassLoader2();
	}
	
	
	public static void debug(String message) {
		Toast.makeText(m4bContext, message, Toast.LENGTH_SHORT).show();
	}
}
