package com.micro4blog.activity;

import com.micro4blog.R;
import com.micro4blog.oauth.Micro4blog;

import android.os.Bundle;
import android.widget.Toast;

public class HomeTimelineActivity extends TimelineActivity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		Toast.makeText(this, Micro4blog.getCurrentServer() + "", Toast.LENGTH_SHORT).show();
	}

}
