package com.micro4blog.utils;

import java.util.List;
import java.util.Map;

import com.micro4blog.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class Micro4blogAdapter extends SimpleAdapter {
	
	private Context mContext;
	private List<? extends Map<String, ?>> mListData;

	public Micro4blogAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		
		mContext = context;
		mListData = data;
	}

	@Override
	public int getCount() {

		return super.getCount();
	}

	@Override
	public Object getItem(int position) {

		return super.getItem(position);
	}

	@Override
	public long getItemId(int position) {

		return super.getItemId(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// 优化
		ViewHodler hodler;
		
		if (convertView == null) {
			hodler = new ViewHodler();
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item_content, null);
			
			hodler.userTextView = (TextView) convertView.findViewById(R.id.username_textview);
			hodler.userImageView = (ImageView) convertView.findViewById(R.id.userimage_imageview);
			
		
			convertView.setTag(hodler);
		} else {
			hodler = (ViewHodler) convertView.getTag();
		}
		
		hodler.userTextView.setText(mListData.get(position).get("username").toString());
		
		// TODO 图片的异步加载
		
		return convertView;
		
//		return super.getView(position, convertView, parent);
		
	}
	
	
	
	
	@Override
	public ViewBinder getViewBinder() {
//		return super.getViewBinder();
		
		return new Micro4blogBinder();
	}


	class ViewHodler {
		TextView userTextView;
		ImageView userImageView;
	}


	class Micro4blogBinder implements ViewBinder {

		@Override
		public boolean setViewValue(View view, Object data,
				String textRepresentation) {
			
			if (view.getId() == R.id.userimage_imageview) {
				view.setFocusable(false);
				
				view.setBackgroundResource(R.drawable.ic_launcher);
			}
			
//			if (view instanceof WebView) {
//				view.setFocusable(false);
//			}
			
			return false;
		}
		
	}
	
	

}
