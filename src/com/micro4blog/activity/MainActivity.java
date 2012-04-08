package com.micro4blog.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.micro4blog.GlobalFramework;
import com.micro4blog.R;
import com.micro4blog.dialog.DialogError;
import com.micro4blog.dialog.Micro4blogDialogListener;
import com.micro4blog.oauth.Micro4blog;
import com.micro4blog.oauth.OauthToken;
import com.micro4blog.tests.ShareActivity;
import com.micro4blog.utils.Micro4blogException;

public class MainActivity extends GlobalFramework {
	
	
	private final String TAG = "MainActivity";
	
	private GridView mGridView;
	private Activity mThis;
	
	
	private boolean isSinaOauthed = false;
	private boolean isSohuOauthed = false;
	private boolean isTencentOauthed = false;
	private boolean isNeteaseOauthed = false;
	
	Micro4blog mMicro4blog;
	
	OauthToken mAccessToken;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mThis = this;
		
		setContentView(R.layout.main_login);
		
		setUp();
		
		setListener();
		
		
		
		
	}
	
	public void onResume() {
		super.onStart();
		
		isSinaOauthed = gSharedPreferences.getBoolean("is_sina_oauthed", false);
		isSohuOauthed = gSharedPreferences.getBoolean("is_sohu_oauthed", false);
		isTencentOauthed = gSharedPreferences.getBoolean("is_tencent_oauthed", false);
		isNeteaseOauthed = gSharedPreferences.getBoolean("is_netease_oauthed", false);
		
		mAccessToken = new OauthToken();
		mAccessToken.setOauthToken(gSharedPreferences.getString("sina_access_token", ""));
	}

	//==============================================
	/*
	 * 设置主页中不用服务的监听事件
	 */
	//==============================================
	private void setListener() {
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				// 登录服务器
				loginServer(arg2);
				
			}
		});
		
		mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				// 添加服务器登录账户
				registerServer(arg2);
				
				return false;
			}
		});
		
		mGridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
	}

	/**
	 * 用户登录到主页，若没有添加账户，需要提示长按进行用户授权
	 * @param serverType
	 */
	protected void loginServer(int serverType) {
		
		mMicro4blog = Micro4blog.getInstance(serverType);
//		Intent intent = new Intent(mThis, HomeTimelineActivity.class);
		Intent intent = new Intent(mThis, ShareActivity.class);
		
				
		switch (serverType) {
		case Micro4blog.SERVER_SINA: {
			
			if (isSinaOauthed) {
				
				mMicro4blog.setAccessToken(mAccessToken);
				
				// TODO 传递数据到timeline中显示，这时候就需要通信
				startActivity(intent);
			} else {
				Toast.makeText(mThis,  "请长按进行新浪授权", Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
		case Micro4blog.SERVER_TENCENT: {
			
			if (isTencentOauthed ) {
				startActivity(intent);
			} else {
				Toast.makeText(mThis,  "请长按进行腾讯授权", Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
		case Micro4blog.SERVER_NETEASE: {
			
			if (isNeteaseOauthed ) {
				startActivity(intent);
			} else {
				Toast.makeText(mThis,  "请长按进行网易授权", Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
		case Micro4blog.SERVER_SOHU: {
			
			if (isSohuOauthed ) {
				startActivity(intent);
			} else {
				Toast.makeText(mThis,  "请长按进行搜狐授权", Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
		default: {
			break;
		}
		}
		
	}
	

	/**
	 * 进行授权，若服务已经授权，长按时进行提示
	 * @param serverType
	 */
	protected void registerServer(int serverType) {
		switch (serverType) {
		case Micro4blog.SERVER_SINA: {
			if (isSinaOauthed) {
				Toast.makeText(mThis, "已经授权新浪服务了", Toast.LENGTH_SHORT).show();
				return ;
			}
			break;
		}
		case Micro4blog.SERVER_TENCENT: {
			if (isTencentOauthed) {
				Toast.makeText(mThis, "已经授权腾讯服务了", Toast.LENGTH_SHORT).show();
				return ;
			}
			break;
		}
		case Micro4blog.SERVER_NETEASE: {
			if (isNeteaseOauthed) {
				Toast.makeText(mThis, "已经授权网易服务了", Toast.LENGTH_SHORT).show();
				return ;
			}
			break;
		}
		case Micro4blog.SERVER_SOHU: {
			if (isSohuOauthed) {
				Toast.makeText(mThis, "已经授权搜狐服务了", Toast.LENGTH_SHORT).show();
				return ;
			}
			break;
		}
		default: {
			break;
		}
		}
		
		
		mMicro4blog = Micro4blog.getInstance(serverType);
		mMicro4blog.authorize(mThis, new MainAuthDialogListener());
		
	}

	/**
	 * 该内部类主要是针对授权后的回调处理
	 * @author Thomsen
	 *
	 */
	public class MainAuthDialogListener implements Micro4blogDialogListener {
		
		SharedPreferences.Editor editor = gSharedPreferences.edit();

		@Override
		public void onComplete(Bundle values) {

			if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_SINA) {
				
				isSinaOauthed = true;
				editor.putBoolean("is_sina_oauthed", isSinaOauthed);
				
				editor.putString("sina_access_token", values.getString("access_token"));
								
				mAccessToken.setOauthToken(values.getString("access_token"));
											
				Log.d(TAG, values.toString());
				
			} else if (Micro4blog.SERVER_TENCENT == Micro4blog.getCurrentServer()) {

				isTencentOauthed = true;
				editor.putBoolean("is_tencent_oauthed", isTencentOauthed);
				
			} else if (Micro4blog.SERVER_NETEASE == Micro4blog.getCurrentServer()) {
				
				isNeteaseOauthed = true;
				editor.putBoolean("is_netease_oauthed", isNeteaseOauthed);
				
			} else if (Micro4blog.SERVER_SOHU == Micro4blog.getCurrentServer()) {
				
				isSohuOauthed = true;
				editor.putBoolean("is_sohu_oauthed", isSohuOauthed);
				
			}
			
			editor.commit();
		}

		@Override
		public void onError(DialogError error) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMicro4blogException(Micro4blogException e) {
			// TODO Auto-generated method stub
			
		}
		
	}

	private void setUp() {
		mGridView = (GridView) findViewById(R.id.main_grid);
		
		ListAdapter adapter = new SimpleAdapter(mThis, getMapData(), 
							R.layout.gird_cell, 
							new String[] {"image", "text"},
							new int[] {R.id.grid_image, R.id.grid_text});
		
		mGridView.setAdapter(adapter);
	}
	
	/**
	 * 登录显示界面
	 * @return
	 */

	private List<Map<String, Object>> getMapData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
//		Map<String, Object> map = new HashMap<String, Object>();
		
		String[] text = new String[] {
				"Sina", "Tencent", "Netease", "Sohu", "More"
		};
		int[] imageId = new int[] {
				R.drawable.sina_logo, R.drawable.tencent_logo,
				R.drawable.netease_logo, R.drawable.sohu_logo,
				R.drawable.ic_launcher
		};
		
		for (int i=0; i<text.length; i++) {
			// 要在这里初始化，不然结果都一样
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("text", text[i]);
			map.put("image", imageId[i]);
			list.add(map);
		}
		
		return list;
	}

}
