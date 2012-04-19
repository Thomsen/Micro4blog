package com.micro4blog.utils;

import java.util.List;
import java.util.Map;

import com.micro4blog.R;
import com.micro4blog.data.Micro4blogInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class Micro4blogBaseAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<Micro4blogInfo> mListData;
	
	public Micro4blogBaseAdapter(Context mContext,
			List<Micro4blogInfo> mListData) {
		super();
		this.mContext = mContext;
		this.mListData = mListData;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// 优化
		Micro4blogHodler hodler;
		
		if (convertView == null) {
			hodler = new Micro4blogHodler();
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.timeline_list, null);
			
			hodler.userTextView = (TextView) convertView.findViewById(R.id.username_textview);
			hodler.userImageView = (ImageView) convertView.findViewById(R.id.userimage_imageview);
			
			hodler.retweetLinearLayout = (LinearLayout) convertView.findViewById(R.id.retweet_layout);
		
			hodler.contentView = (TextView) convertView.findViewById(R.id.timeline_content);
			hodler.retweetContentView = (TextView) convertView.findViewById(R.id.timeline_retweet);
		
			hodler.retweetCount = (TextView) convertView.findViewById(R.id.retweet_count);
			hodler.commentCount = (TextView) convertView.findViewById(R.id.comment_count);
			
			hodler.originalRetweetCount = (TextView) convertView.findViewById(R.id.origin_retweet_count);
			hodler.originalCommentCount = (TextView) convertView.findViewById(R.id.origin_comment_count);
			
			convertView.setTag(hodler);
		} else {
			hodler = (Micro4blogHodler) convertView.getTag();
		}
		
		Micro4blogInfo m4bInfo = (Micro4blogInfo) mListData.get(position);
		
		if (m4bInfo.isHasRetweet()) {
			hodler.retweetContentView.setText(m4bInfo.getM4bRetweetInfo().getM4bText());
			hodler.originalRetweetCount.setText(String.valueOf(m4bInfo.getM4bRetweetInfo().getM4bRetweetCount()));
			hodler.originalCommentCount.setText(String.valueOf(m4bInfo.getM4bRetweetInfo().getM4bCommentCount()));
		} else {
			hodler.retweetLinearLayout.setVisibility(View.GONE);
		}
		
		hodler.userTextView.setText(m4bInfo.getUserInfo().getUserName());
		hodler.userImageView.setBackgroundResource(R.drawable.ic_launcher);
		
		hodler.contentView.setText(m4bInfo.getM4bText());
		hodler.retweetCount.setText(String.valueOf(m4bInfo.getM4bRetweetCount()));
		hodler.commentCount.setText(String.valueOf(m4bInfo.getM4bCommentCount()));
				
		// TODO 图片的异步加载
		
		return convertView;

		
	}

	class Micro4blogHodler {
		TextView userTextView;
		ImageView userImageView;
		
		TextView contentView;
		TextView retweetContentView;
		
		LinearLayout retweetLinearLayout;
		
		TextView retweetCount;
		TextView commentCount;
		
		TextView originalRetweetCount;
		TextView originalCommentCount;
	}

	@Override
	public int getCount() {
		// 首先执行getCount，当返回0时就不执行getView
		return mListData.size();
	}




	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
