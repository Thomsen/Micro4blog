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

public class TimelineActivity extends GlobalFramework {
	
	Activity mActivity;
	
	RelativeLayout mLayoutContent;
	ListView mListView;
	
	boolean ib = true;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mActivity = this;

		mLayoutContent = (RelativeLayout) findViewById(R.id.content_main);
						
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
			
			View view = getLayoutInflater().inflate(R.layout.gird_server, null);
			
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
			
			View view = getLayoutInflater().inflate(R.layout.timeline_list, null);
			
			mLayoutContent.addView(view);
			break;
		}
		
		default :
			break;
		}
		
	}
		
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
			gFooterLayout.setVisibility(View.VISIBLE);
			ib = false;
		} else {
			gFooterLayout.setVisibility(View.GONE);
			ib = true;
		}
		
		return false; // true open system menu
	}





		
}

	