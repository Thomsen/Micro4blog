package com.micro4blog.tests;

import com.micro4blog.GlobalFramework;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SwipeTest extends Activity implements OnGestureListener {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View view = new View(this);
		view.setLayoutParams(GlobalFramework.gParams);
	
		setContentView(view);
		
		view.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				
				return new GestureDetector(SwipeTest.this).onTouchEvent(event);
			}
		});
		
		// 触发不同的手势
		view.setLongClickable(true);
	}
	
	public boolean onDown(MotionEvent event) {
		Toast.makeText(this, "a", Toast.LENGTH_SHORT).show();
		
		return false;
	}
	
	public void onShowPress(MotionEvent event) {
		Toast.makeText(this, "b", Toast.LENGTH_SHORT).show();
	}
	
	public boolean onSingleTapUp(MotionEvent event) {
		Toast.makeText(this, "c", Toast.LENGTH_SHORT).show();
		
		return false;
	}
	
	public boolean onScroll(MotionEvent event1, MotionEvent event2, float dx, float dy) {
		Toast.makeText(this, "d", Toast.LENGTH_SHORT).show();
		
		return false;
	}
	
	public void onLongPress(MotionEvent event) {
		Toast.makeText(this, "e", Toast.LENGTH_SHORT).show();
	}
	
	public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
		Toast.makeText(this, "f", Toast.LENGTH_SHORT).show();
		
		return false;
	}


	
	
}
