package com.micro4blog.widget;

import com.micro4blog.R;
import com.micro4blog.activity.TweetActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class Micro4blogWidgetProvider extends AppWidgetProvider {

	private static final String TAG = "Micro4blogWidgetProvider";

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		Log.i(TAG, "widget service start");
		
		Intent intent = new Intent(context, WidgetUpdateService.class);
		context.startService(intent);
		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
//		 Intent intent = new Intent(context, ShareActivity.class);
//         PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//         
//         RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_widget);
//         views.setOnClickPendingIntent(R.id.button, pendingIntent);
//
//         appWidgetManager.updateAppWidget(appWidgetIds, views);
	}
	
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {

		Log.i(TAG, "widget service stop");
		
		Intent intent = new Intent(context, WidgetUpdateService.class);
		context.stopService(intent);
	
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
	}
	

}
