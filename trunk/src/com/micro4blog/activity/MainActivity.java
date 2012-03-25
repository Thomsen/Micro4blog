package com.micro4blog.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.micro4blog.GlobalFramework;
import com.micro4blog.R;

public class MainActivity extends GlobalFramework {
	
	private GridView mGridView;
	private Activity mThis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mThis = this;
		
		setContentView(R.layout.main);
		
		setUp();
		
		setListener();
		
		
		
		
	}

	//==============================================
	/*
	 * 设置主页中不用服务的监听事件
	 */
	//==============================================
	private void setListener() {
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				// 登录服务器
				loginServer(arg2);
				
			}
		});
		
		mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				// 添加服务器登录账户
				registerServer(arg2);
				
				return false;
			}
		});
		
		mGridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
	}

	protected void loginServer(int arg2) {
		// TODO Auto-generated method stub
		
	}

	protected void registerServer(int arg2) {
		// TODO Auto-generated method stub
		
	}

	private void setUp() {
		mGridView = (GridView) findViewById(R.id.main_grid);
		
		ListAdapter adapter = new SimpleAdapter(mThis, getMapData(), 
							R.layout.gird_cell, 
							new String[] {"image", "text"},
							new int[] {R.id.grid_image, R.id.grid_text});
		
		mGridView.setAdapter(adapter);
	}

	private List<Map<String, Object>> getMapData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
//		Map<String, Object> map = new HashMap<String, Object>();
		
		String[] text = new String[] {
				"Sina", "Tencent", "Netease", "Sohu", "More"
		};
		int[] imageId = new int[] {
				R.drawable.sina_logo, R.drawable.tencent_logo,
				R.drawable.netease_logo, R.drawable.sohu_logo,
				R.drawable.ic_launcher
		};
		
		for (int i=0; i<text.length; i++) {
			// 要在这里初始化，不然结果都一样
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("text", text[i]);
			map.put("image", imageId[i]);
			list.add(map);
		}
		
		return list;
	}

}
