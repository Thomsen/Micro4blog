package com.micro4blog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * 全局界面结构，控制全局的界面基调
 * @author Thomsen
 *
 */
public class GlobalFramework extends Activity {
	
	protected SharedPreferences gSharedPreferences; 
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		gSharedPreferences = getSharedPreferences("micro4blog", 0);
		
	}
	
	//===================================================
	/*
	 * 添加菜单
	 */
	//===================================================

}
