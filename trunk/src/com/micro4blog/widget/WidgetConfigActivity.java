package com.micro4blog.widget;

import com.micro4blog.R;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RemoteViews;

public class WidgetConfigActivity extends Activity implements OnClickListener {

	private Context mActivity;
	int mAppWidgetId;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mActivity = this;
		
		setResult(RESULT_CANCELED);
		
		setContentView(R.layout.main_widget);
		
		 // First, get the App Widget ID from the intent that launched the Activity
        Intent intent = getIntent();  
        Bundle extras = intent.getExtras();  
        if (extras != null) {  
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,  
                    AppWidgetManager.INVALID_APPWIDGET_ID);  
        }  
  
        // If they gave us an intent without the widget id, just bail.  
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {  
            finish();  
        }  
        
//        // Get an instance of the AppWidgetManager by calling getInstance(context)
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mActivity);
//        
//        // Update the App Widget
//        RemoteViews remoteViews = new RemoteViews(mActivity.getPackageName(), R.layout.main_widget);
//        appWidgetManager.updateAppWidget(mAppWidgetId, remoteViews);
//          
//        // return OK  
//        Intent resultValue = new Intent();  
//        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,  
//                mAppWidgetId);  
//          
//        setResult(RESULT_OK, resultValue); 
//		
//		finish();
        
        findViewById(R.id.button).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		int srcId = R.drawable.ic_launcher;
		
		switch (v.getId()) {
		case R.id.button: {
			srcId = R.drawable.sina_logo;
			break;
		}
		default :
			break;
		}
		
      // Get an instance of the AppWidgetManager by calling getInstance(context)
      AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mActivity);
      
      // Update the App Widget
      RemoteViews remoteViews = new RemoteViews(mActivity.getPackageName(), R.layout.main_widget);
      remoteViews.setImageViewResource(R.id.image, srcId);
      appWidgetManager.updateAppWidget(mAppWidgetId, remoteViews);
        
      // return OK  
      Intent resultValue = new Intent();  
      resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,  
              mAppWidgetId);  
        
      setResult(RESULT_OK, resultValue); 
		
		finish();
	}
}
