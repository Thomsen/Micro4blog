package com.micro4blog.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.micro4blog.GlobalFramework;
import com.micro4blog.R;

public class TimelineActivity extends GlobalFramework {
	
	Activity mActivity;
	
	ListView mListView;
	
	LinearLayout mLayoutFooter;
	
	LayoutParams mParams;
	
	boolean ib = true;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mActivity = this;
		
		setContentView(R.layout.main_content);
		
//		mParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//		
//		LayoutInflater footer = getLayoutInflater();
//		mLayoutFooter = (LinearLayout) footer.inflate(R.layout.footer, null);
//		
//		mLayoutFooter.setGravity(Gravity.BOTTOM);
//		
//		getParent().addContentView(mLayoutFooter, mParams);
		
//		setUp();
		
		mLayoutFooter = (LinearLayout) findViewById(R.id.footer);
		mLayoutFooter.setVisibility(View.GONE);
		
	}

	protected void setUp() {
		mListView = (ListView) findViewById(R.id.list_main);
		
		ListAdapter adapter = new SimpleAdapter(mActivity, getMapData(), 
						R.layout.list_item_timeline, 
						new String[] {"username", "content",  "forwarding_content"},
						new int[] {R.id.username_textview, R.id.timeline_content, R.id.forwarding_content});
		
		mListView.setAdapter(adapter);
		
	}
	
	protected List<Map<String, Object>> getMapData() {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("username", "user name");
		map.put("content", "content");
		map.put("forwarding_content", "forwarding_content");
		
		list.add(map);
		
		return list;
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

	