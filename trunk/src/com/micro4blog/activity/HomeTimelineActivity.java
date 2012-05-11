package com.micro4blog.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Toast;

import com.micro4blog.Micro4blog;
import com.micro4blog.R;
import com.micro4blog.data.Micro4blogInfo;
import com.micro4blog.utils.AsyncMicro4blogRunner;
import com.micro4blog.utils.Micro4blogBaseAdapter;
import com.micro4blog.utils.Micro4blogException;

public class HomeTimelineActivity extends TimelineActivity 
		implements AsyncMicro4blogRunner.RequestListener {
	
	private Activity mActivity;
	
	ArrayList<Micro4blogInfo> m4bList;
	
	Micro4blogBaseAdapter micro4blogAdapter;
	
	Micro4blog micro4blog; 
	
	Micro4blogInfo micro4blogInfo;
		
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		mActivity = this;
		
		micro4blog = Micro4blog.getInstance(mActivity, Micro4blog.getCurrentServer());
		
		m4bList = micro4blog.parseHomeTimeline(micro4blog.getHomeTimeline(mActivity));
	
		setListUp();
		
		setHeaderUp();
	}
	
	private void refresh() {
		
		m4bList = micro4blog.parseHomeTimeline(micro4blog.getHomeTimeline(mActivity));
		
		setListUp();
		
		
	}
	
	protected void setListUp() {
		mListView = (ListView) findViewById(R.id.list_main);		
		
		// 设置item之间的分割线
		mListView.setDivider(null);
		
		// 防止滚动时，显示内容跟背景进行混合运算
		mListView.setCacheColorHint(Color.TRANSPARENT);
				
		micro4blogAdapter = new Micro4blogBaseAdapter(mActivity, m4bList);
		
		mListView.setAdapter(micro4blogAdapter);
		
		// 由于activity也有onCreateContextMenu，若给ListView的Context Menu
		// 需要的对象是mListView，这样就不会出现很多不知名的错误
		mListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				
				menu.setHeaderIcon(R.drawable.ic_launcher);
				menu.setHeaderTitle("微博");  // TODO 可以做一个实体类，动态改变标题
				
				menu.add(0, 0, 0, "删除");
				menu.add(0, 1, 0, "取消");
				
			}
			
		});
		
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				micro4blogInfo = (Micro4blogInfo) arg0.getAdapter().getItem(arg2);
				
				registerForContextMenu(arg1);
				
				// 返回true，不能相应context menu
				return false;
			}
			
		});
		
		mListView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				micro4blogInfo = (Micro4blogInfo) arg0.getAdapter().getItem(arg2);
				
				Toast.makeText(mActivity, "item : " + micro4blogInfo.getM4bStrId(), Toast.LENGTH_SHORT).show();
				
			}
			
		});
		
		// 防止getCount返回0时，没有执行getView
		// 重新加载适配器，执行getView
		// 例，在删除时刷新View
		micro4blogAdapter.notifyDataSetChanged();
		

	}
	
	protected List<Map<String, Object>> getMapData() {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		for (Micro4blogInfo m4bInfo : m4bList) {
		
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("userimage", String.valueOf(R.drawable.ic_launcher));			
			map.put("username", m4bInfo.getUserInfo().getUserName());
			map.put("content", m4bInfo.getM4bText());
			map.put("forwarding_content", "forwarding_content");
			
			list.add(map);
		
		}
		
		return list;
	}
	
	protected void setHeaderUp() {
		super.setHeaderUp();
		
		gHeaderLeftButton.setText("服务");
		gHeaderRightButton.setText("发布");
		gHeaderContent.setText("主页");
		
	}
	
	public void onClick(View v) {
		super.onClick(v);
		
		switch (v.getId()) {
		case R.id.header_left: {
			Intent intent = new Intent(mActivity, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		case R.id.header_right: {
			Toast.makeText(mActivity, "发布", Toast.LENGTH_SHORT).show();
			
			Intent intent = new Intent(mActivity, TweetActivity.class);
			
			startActivity(intent);
			
			break;
		}
		default :
			break;
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
			
		switch (item.getItemId()) {
		case 0: {
			Toast.makeText(mActivity, "delete", Toast.LENGTH_SHORT).show();
			
			micro4blog.destroy(micro4blogInfo.getM4bStrId());
			
			refresh();
			
			break;
		}
		}
				
		return super.onContextItemSelected(item);
	}
	
	@Override
	public void onContextMenuClosed(Menu menu) {
		super.onContextMenuClosed(menu);

	}

	@Override
	public void onComplete(String response) {
		runOnUiThread(new Runnable() {

            public void run() {
                Toast.makeText(mActivity, R.string.send_sucess, Toast.LENGTH_LONG).show();
            }
        });

        this.finish();
		
	}

	@Override
	public void onIOException(IOException e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Micro4blogException e) {
		// TODO Auto-generated method stub
		
	}





}
