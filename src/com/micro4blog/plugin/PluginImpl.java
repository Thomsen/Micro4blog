package com.micro4blog.plugin;

import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.util.Log;
import dalvik.system.DexClassLoader;

public class PluginImpl {
	
	private static final String TAG = "PluginImpl";
	
	private Context mContext;
	
	public PluginImpl(Context context) {
		mContext = context;
	}
	
	public void useDexClassLoader1() {
        Intent intent = new Intent("com.anhuioss.dexclassloader.plugin.client", null);
        PackageManager pm = mContext.getPackageManager();
        List<ResolveInfo> plugins = pm.queryIntentActivities(intent, 0);
        ResolveInfo resInfo = plugins.get(0);
        ActivityInfo actInfo = resInfo.activityInfo;
        
        String div = System.getProperty("path.separator");
        String packageName = actInfo.packageName;
        String dexPath = actInfo.applicationInfo.sourceDir;
        String dexOutputDir = mContext.getApplicationInfo().dataDir;
        String libPath = "";
        // android 2.3.3
//        String libPath = actInfo.applicationInfo.nativeLibraryDir;

        DexClassLoader cll = new DexClassLoader(dexPath, dexOutputDir, libPath, this.getClass().getClassLoader()) ;

        Class<?> clazz = null;
        try {
            clazz = cll.loadClass(packageName + ".PluginClass");
            Object obj = clazz.newInstance();
            Class[] params = new Class[2];
            params[0] = Integer.TYPE;
            params[1] = Integer.TYPE;
            Method method = clazz.getMethod("client", params);
            Integer tgr = (Integer) method.invoke(obj, 12, 34);
            Log.i(TAG, "Host return value is " + tgr);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
	
	 public void useDexClassLoader2() {
	        Intent intent = new Intent("com.micro4blog.plugin.test", null);
	        PackageManager pm = mContext.getPackageManager();
	        List<ResolveInfo> plugins = pm.queryIntentActivities(intent, 0);
	        
	        if (plugins==null || plugins.size()==0) {
	        	return ;
	        }
	        
	        ResolveInfo resInfo = plugins.get(0);
	        ActivityInfo actInfo = resInfo.activityInfo;

	        String div = System.getProperty("path.separator");
	        String packageName = actInfo.packageName;
	        String dexPath = actInfo.applicationInfo.sourceDir;
	        String dexOutputDir = mContext.getApplicationInfo().dataDir;
//	        String libPath = "";
	        // api level 9
	        String libPath = actInfo.applicationInfo.nativeLibraryDir;

	        DexClassLoader cll = new DexClassLoader(dexPath, dexOutputDir, libPath, this.getClass().getClassLoader()) ;

	        Class<?> clazz = null;

	        try {
	            clazz = cll.loadClass(packageName + ".Micro4blogPluginTest");
	            IPlugin comm = (IPlugin) clazz.newInstance();
//	            Integer ret = comm.client(32, 45);
//	            Log.i("thom", "Host 2 return value is " + ret);
	            comm.addPlugin();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        // 获取插件的版本信息
	        // 需要在插件中的strings中设置version值
	        Resources res = null;
	        try {
	            res = pm.getResourcesForApplication(packageName);
	        } catch (PackageManager.NameNotFoundException e) {
	            e.printStackTrace();
	        }
	        int id = 0;
	        String version = "0";
	        id = res.getIdentifier("version", "string", packageName);
	        if (id != 0) {
	        	 version = res.getString(id);
	        }
	        
	        Log.i("thom", "id " + id + " verison " + version);
	        
	        


	    }

}
