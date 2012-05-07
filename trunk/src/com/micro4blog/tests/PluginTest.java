package com.micro4blog.tests;

import com.micro4blog.GlobalFramework;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class PluginTest extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(GlobalFramework.gParams);
		
		Button button = new Button(this);
		button.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 
				LayoutParams.WRAP_CONTENT));
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		ll.addView(button);
		
		setContentView(ll);
	}

}
