package com.micro4blog.utils;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micro4blog.R;
import com.micro4blog.data.Micro4blogInfo;
import com.micro4blog.utils.AsyncMicro4blogImage.ImageCallback;

public class Micro4blogBaseAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<Micro4blogInfo> mListData;
	
	private AsyncMicro4blogImage mAsyncImage;
	
	public Micro4blogBaseAdapter(Context mContext,
			List<Micro4blogInfo> mListData) {
		super();
		this.mContext = mContext;
		this.mListData = mListData;
		
		mAsyncImage = new AsyncMicro4blogImage();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// 优化
		final Micro4blogHodler hodler;
		
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
//		hodler.userImageView.setBackgroundResource(R.drawable.ic_launcher);
		
		hodler.contentView.setText(m4bInfo.getM4bText());
		hodler.retweetCount.setText(String.valueOf(m4bInfo.getM4bRetweetCount()));
		hodler.commentCount.setText(String.valueOf(m4bInfo.getM4bCommentCount()));
				
		// TODO 图片的异步加载
		Drawable cachedImage = mAsyncImage.loadDrawable(m4bInfo.getUserInfo().getProfileImageUrl(), new ImageCallback() {

			@Override
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				
				// 这里起作用了
				hodler.userImageView.setImageDrawable(imageDrawable);
								
			}
			
		});
		
		// 这里没看出效果
		hodler.userImageView.setImageDrawable(cachedImage);
		
		
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
		
		// 在长按的时候获取该微博对象
		return mListData.get(position);
	}




	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
