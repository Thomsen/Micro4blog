package com.micro4blog.utils;

import java.util.List;
import java.util.Map;

import com.micro4blog.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class Micro4blogAdapter extends SimpleAdapter {

	public Micro4blogAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
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
		
		return super.getView(position, convertView, parent);
	}
	
	
	
	
	@Override
	public ViewBinder getViewBinder() {
//		return super.getViewBinder();
		
		return new Micro4blogBinder();
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
