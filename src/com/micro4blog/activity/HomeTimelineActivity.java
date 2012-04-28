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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.micro4blog.Micro4blog;
import com.micro4blog.R;
import com.micro4blog.data.Micro4blogInfo;
import com.micro4blog.utils.AsyncMicro4blogRunner;
import com.micro4blog.utils.Micro4blogBaseAdapter;
import com.micro4blog.utils.Micro4blogException;

public class HomeTimelineActivity extends TimelineActivity 
		implements AsyncMicro4blogRunner.RequestListener, OnItemClickListener {
	
	private Activity mActivity;
	
	ArrayList<Micro4blogInfo> m4bList;
	
	Micro4blogBaseAdapter mMicro4blogAdapter;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		mActivity = this;
		
		Micro4blog m4b = Micro4blog.getInstance(mActivity, Micro4blog.getCurrentServer());
		
//		Micro4blogParameters m4bParams = new Micro4blogParameters();
	
//		String result = "";
//		try {
//			result = m4b.request(this, "https://api.weibo.com/2/statuses/home_timeline.json", m4bParams, Utility.HTTPMETHOD_GET, null);
//		} catch (Micro4blogException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
//		String url = m4b.getServerUrl() + "statuses/home_timeline.json";	
//		AsyncMicro4blogRunner Micro4blogRunner = new AsyncMicro4blogRunner(m4b);
//		Micro4blogRunner.request(mThis, url, m4bParams, Utility.HTTPMETHOD_POST, this);		
//		Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
		
//		Log.i("thom", m4b.getHomeTimeline(mThis));
		
		m4bList = m4b.parseHomeTimeline(m4b.getHomeTimeline(mActivity));
	
		for (Micro4blogInfo m4bInfo : m4bList) {
			Log.i("thom", m4bInfo.getM4bCreateAt());
		}
		
		setListUp();
		
		setHeaderUp();
	}
	
	
	
	protected void setListUp() {
		mListView = (ListView) findViewById(R.id.list_main);
		
//		ListAdapter adapter = new SimpleAdapter(mActivity, getMapData(), 
//						R.layout.list_item_content, 
//						new String[] {"username", "content",  "forwarding_content"},
//						new int[] {R.id.username_textview, R.id.timeline_content, R.id.forwarding_content});
//		
//		mListView.setAdapter(adapter);
		
		// 设置item之间的分割线
		mListView.setDivider(null);
		
		// 防止滚动时，显示内容跟背景进行混合运算
		mListView.setCacheColorHint(Color.TRANSPARENT);
		
		// 触发状态
//		mListView.setClickable(true);
		
		// WebView对click的影响, 暂时改成ImgeView
//		mMicro4blogAdapter = new Micro4blogSimpleAdapter(mActivity, getMapData(), 
//				R.layout.timeline_list, 
//				new String[] {"userimage", "username", "content", "forwarding_content"},
//				new int[] {R.id.userimage_imageview, R.id.username_textview, R.id.timeline_content, R.id.forwarding_content});
//
//		mMicro4blogAdapter.setViewBinder(mMicro4blogAdapter.getViewBinder());
		
		mMicro4blogAdapter = new Micro4blogBaseAdapter(mActivity, m4bList);
		
		mListView.setAdapter(mMicro4blogAdapter);
		
		mListView.setOnItemClickListener(this);
		
		// 防止getCount返回0时，没有执行getView
		// 重新加载适配器，执行getView
//		mMicro4blogAdapter.notifyDataSetChanged();
		

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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		Micro4blogInfo m4bInfo = (Micro4blogInfo) arg0.getAdapter().getItem(arg2);
		
		Toast.makeText(mActivity, "item : " + m4bInfo.getM4bStrId(), Toast.LENGTH_SHORT).show();
		
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
