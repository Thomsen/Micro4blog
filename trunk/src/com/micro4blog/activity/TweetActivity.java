package com.micro4blog.activity;

import com.micro4blog.GlobalFramework;
import com.micro4blog.R;
import com.micro4blog.oauth.Micro4blog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class TweetActivity extends GlobalFramework {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_send);
		
		setHeaderUp();
	}
	
	public void setHeaderUp() {
		super.setHeaderUp();
		
		if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_SINA) {
			
			gHeaderContent.setText("新浪微博");
			
		} else if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_TENCENT) {
			
			gHeaderContent.setText("腾讯微博");
			
		} else if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_NETEASE) {
			
			gHeaderContent.setText("网易微博");
			
		} else if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_SOHU) {
			
			gHeaderContent.setText("搜狐微博");
			
		}
		
		gHeaderRightButton.setText("发送");
		gHeaderLeftButton.setText("返回");
			
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_left: {
			finish();
			break;
		}
		case R.id.header_right: {
			break;
		}
		default :
			break;
		}
	}

}
