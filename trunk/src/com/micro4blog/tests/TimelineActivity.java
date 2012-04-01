package com.micro4blog.tests;

import com.micro4blog.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class TimelineActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_content);
		
		Toast.makeText(this, getIntent().getExtras().getString("acctoken")
		, Toast.LENGTH_SHORT).show();
	}
}
