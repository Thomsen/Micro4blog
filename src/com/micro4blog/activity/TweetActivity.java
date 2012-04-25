package com.micro4blog.activity;

import com.micro4blog.GlobalFramework;
import com.micro4blog.Micro4blog;
import com.micro4blog.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class TweetActivity extends GlobalFramework {
	
	private EditText mTweetContent;
	
	private Micro4blog micro4blog;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_send);
		
		setHeaderUp();
		
		setTweetContent();
		
		micro4blog = Micro4blog.getInstance(Micro4blog.getCurrentServer());
	}

	private void setTweetContent() {
		mTweetContent = (EditText) findViewById(R.id.tweet_content);
		
		mTweetContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
			
		});
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
			tweet();
			break;
		}
		default :
			break;
		}
	}

	private void tweet() {
		String status = mTweetContent.getText().toString();
		
		micro4blog.update(status, null, null);
		
	}

}
