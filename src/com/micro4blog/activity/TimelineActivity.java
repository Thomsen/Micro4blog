package com.micro4blog.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.micro4blog.GlobalFramework;
import com.micro4blog.R;

public class TimelineActivity extends GlobalFramework implements OnClickListener {
	
	Activity mActivity;
	LayoutParams mParams;
	
	RelativeLayout mLayoutContent;
	ListView mListView;
	
	LinearLayout mLayoutFooter;
	Button mFooterHome;
	Button mFooterMessage;
	Button mFooterProfile;
	Button mFooterSquare;
	Button mFooterMore;

	boolean ib = true;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mActivity = this;
		
		mParams = new LayoutParams(LayoutParams.FILL_PARENT, 
				LayoutParams.WRAP_CONTENT);
		
		setContentView(R.layout.main_content);
		
		mLayoutContent = (RelativeLayout) findViewById(R.id.content_main);
				
		mLayoutFooter = (LinearLayout) findViewById(R.id.footer);
		mLayoutFooter.setVisibility(View.GONE);
		
		// 设置导航栏
		setFooterUp();
		
	}
			
	protected void setFooterUp() {
		mFooterHome = (Button) findViewById(R.id.footer_home);
		mFooterMessage = (Button) findViewById(R.id.footer_message);
		mFooterProfile = (Button) findViewById(R.id.footer_profile);
		mFooterSquare = (Button) findViewById(R.id.footer_square);
		mFooterMore = (Button) findViewById(R.id.footer_more);
		
		mFooterHome.setOnClickListener(this);
		mFooterMessage.setOnClickListener(this);
		mFooterProfile.setOnClickListener(this);
		mFooterSquare.setOnClickListener(this);
		mFooterMore.setOnClickListener(this);
	
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.footer_home: {
			mLayoutContent.removeAllViews();
			
			mLayoutContent.addView(mListView);
			break;
		}
		case R.id.footer_message: {
			mLayoutContent.removeAllViews();
			
			View view = getLayoutInflater().inflate(R.layout.gird_cell, null);
			
			mLayoutContent.addView(view);
			
			break;
		}
		case R.id.footer_profile: {
			mLayoutContent.removeAllViews();
			
			View view = getLayoutInflater().inflate(R.layout.footer, null);
			
			mLayoutContent.addView(view);
			break;
		}
		case R.id.footer_square: {
			mLayoutContent.removeAllViews();
			
			View view = getLayoutInflater().inflate(R.layout.header, null);
			
			mLayoutContent.addView(view);
			break;
		}
		case R.id.footer_more: {
			mLayoutContent.removeAllViews();
			
			View view = getLayoutInflater().inflate(R.layout.list_item_timeline, null);
			
			mLayoutContent.addView(view);
			break;
		}
		
		default :
			break;
		}
		
	}
	
	//================================================================
	/*
	 * 添加菜单
	 */
	//================================================================
	
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add("menu1");
		menu.add("menu2");
		menu.add("menu3");
		menu.add("menu4");
		menu.add("menu5");
		
		return super.onCreateOptionsMenu(menu);
	
	}
	
	public boolean onMenuOpened(int featureId, Menu menu) {
		
		
		
		if (ib) {
			mLayoutFooter.setVisibility(View.VISIBLE);
			ib = false;
		} else {
			mLayoutFooter.setVisibility(View.GONE);
			ib = true;
		}
		
		return false; // true open system menu
	}





		
}

	