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
import com.micro4blog.http.ApiTokenHeader;
import com.micro4blog.http.Oauth2AccessTokenHeader;
import com.micro4blog.http.Utility;
import com.micro4blog.oauth.AccessToken;
import com.micro4blog.oauth.Micro4blog;
import com.micro4blog.oauth.OauthToken;
import com.micro4blog.oauth.RequestToken;
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
	
	Micro4blog micro4blog;
	
//	RequestToken mRequestToken = new RequestToken();
//	OauthToken mAccessToken;
	
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
		
//		mAccessToken = new OauthToken();
//		mAccessToken.setOauthToken(gSharedPreferences.getString("sina_access_token", ""));
				
	}
	
	private void readPreferences() {
		
		if (mAccessToken == null) {
			mAccessToken = new OauthToken();
		}
						
		if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_SINA) {
			
			mAccessToken = new AccessToken(gSharedPreferences.getString("sina_access_token", null), micro4blog.getAppSecret());
					
//			mAccessToken.setOauthToken(gSharedPreferences.getString("sina_access_token", null));
			mAccessToken.setExpiresIn(gSharedPreferences.getString("sina_expires_in", null));
			
			Utility.setAuthorization(new Oauth2AccessTokenHeader());
						
		} else if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_TENCENT) {
			
			mAccessToken.setOauthVerifier(gSharedPreferences.getString("tencent_oauth_verifier", null));
			
			mAccessToken.setOauthToken(gSharedPreferences.getString("tencent_access_token", null));
			mAccessToken.setOauthTokenSecret(gSharedPreferences.getString("tencent_oauth_token_sercet", null));
			
			Utility.setAuthorization(new ApiTokenHeader());
			
		} else if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_NETEASE) {
			
//			mAccessToken.setOauthVerifier(gSharedPreferences.getString("sohu_oauth_verifier", null));
			
			mAccessToken.setOauthToken(gSharedPreferences.getString("netease_access_token", null));
			mAccessToken.setOauthTokenSecret(gSharedPreferences.getString("netease_oauth_token_sercet", null));
			
			Utility.setAuthorization(new ApiTokenHeader());
			
		} else if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_SOHU) {
			
//			try {
//				mRequestToken = mMicro4blog.getRequestToken(mThis, Utility.HTTPMETHOD_GET, mMicro4blog.getAppKey(),
//						mMicro4blog.getAppSecret(), mMicro4blog.getRedirectUrl());
//				
//				mRequestToken.setOauthVerifier(gSharedPreferences.getString("sohu_oauth_verifier", ""));
//				
//				mAccessToken = mMicro4blog.generateAccessToken(mThis, Utility.HTTPMETHOD_GET, mRequestToken);
//			
//			} catch (Micro4blogException e) {
//				e.printStackTrace();
//			}
						
//			mRequestToken.setOauthToken(gSharedPreferences.getString("sohu_oauth_token", ""));
			
			mAccessToken.setOauthVerifier(gSharedPreferences.getString("sohu_oauth_verifier", null));
			
			mAccessToken.setOauthToken(gSharedPreferences.getString("sohu_access_token", null));
			mAccessToken.setOauthTokenSecret(gSharedPreferences.getString("sohu_oauth_token_sercet", null));
			
			Utility.setAuthorization(new ApiTokenHeader());
			
			
		}	
		
		Log.d(TAG, "token: " + mAccessToken.getOauthToken() + "\n" + "secret: " + mAccessToken.getOauthTokenSecret());
				
//		mMicro4blog.setRequestToken(mRequestToken);
				
//		mAccessToken.setOauthTokenSecret(mMicro4blog.getAppSecret());
		
		micro4blog.setAccessToken(mAccessToken);
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
		
		micro4blog = Micro4blog.getInstance(serverType);
//		Intent intent = new Intent(mThis, HomeTimelineActivity.class);
		Intent intent = new Intent(mThis, ShareActivity.class);
		
		readPreferences();
				
		switch (serverType) {
		case Micro4blog.SERVER_SINA: {
			
			if (isSinaOauthed) {
					
//				try {
//					mMicro4blog.share2weibo(mThis, mAccessToken.getOauthToken(), mMicro4blog.getAppSecret(), "adbds", null);
//				} catch (Micro4blogException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
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
		
		micro4blog = Micro4blog.getInstance(serverType);
		micro4blog.authorize(mThis, new MainAuthDialogListener());
		
	}

	/**
	 * 该内部类主要是针对授权后的回调处理
	 * @author Thomsen
	 *
	 */
	public class MainAuthDialogListener implements Micro4blogDialogListener {
				
		SharedPreferences.Editor editor = gSharedPreferences.edit();
		
		// Syntax error on token ";", , expected TODO why
//		mAccessToken = new AccessToken();

		@Override
		public void onComplete(Bundle values) {
			
			mAccessToken =  micro4blog.getAccessToken();

			if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_SINA) {
				
				isSinaOauthed = true;
				editor.putBoolean("is_sina_oauthed", isSinaOauthed);
				
				editor.putString("sina_access_token", values.getString("access_token"));
				editor.putString("sina_expires_in", values.getString("expires_in"));
				
//				mAccessToken = new AccessToken(values.getString("access_token"), micro4blog.getAppSecret());
//				
//				mAccessToken.setExpiresIn(values.getString("expires_in"));
//				mAccessToken.setOauthToken(values.getString("access_token"));
											
				Log.d(TAG, values.toString());
				
			} else if (Micro4blog.SERVER_TENCENT == Micro4blog.getCurrentServer()) {

				isTencentOauthed = true;
				editor.putBoolean("is_tencent_oauthed", isTencentOauthed);
				
				editor.putString("tencent_access_token", mAccessToken.getOauthToken());
				editor.putString("tencent_oauth_token_sercet", mAccessToken.getOauthTokenSecret());
				editor.putString("tencent_oauth_verifier", values.getString("oauth_verifier"));
				
//				mRequestToken.setOauthToken(values.getString("oauth_token"));
				
			} else if (Micro4blog.SERVER_NETEASE == Micro4blog.getCurrentServer()) {
				
				isNeteaseOauthed = true;
								
				editor.putBoolean("is_netease_oauthed", isNeteaseOauthed);
				
				editor.putString("netease_access_token", mAccessToken.getOauthToken());
				editor.putString("netease_oauth_token_sercet", mAccessToken.getOauthTokenSecret());
				
				
				// oauth第三步，换取access token
//				RequestToken requestToken = micro4blog.getRequestToken();
////				requestToken.setOauthVerifier(values.getString("oauth_verifier"));
//				try {
//					mAccessToken = micro4blog.generateAccessToken(mThis, Utility.HTTPMETHOD_GET,
//							requestToken);
//				} catch (Micro4blogException e) {
//					e.printStackTrace();
//				}
				
//				Toast.makeText(mThis, mAccessToken.getOauthToken() + "\n" + mAccessToken.getOauthTokenSecret(), Toast.LENGTH_SHORT).show();
				
			} else if (Micro4blog.SERVER_SOHU == Micro4blog.getCurrentServer()) {
				
				isSohuOauthed = true;
				mAccessToken = (AccessToken) micro4blog.getAccessToken();
				
//				Toast.makeText(mThis, values.toString(), Toast.LENGTH_SHORT).show();
				
//				RequestToken requestToken = mMicro4blog.getRequestToken();
//				requestToken.setOauthVerifier(values.getString("oauth_verifier"));
//				try {
//					mAccessToken = mMicro4blog.generateAccessToken(mThis, Utility.HTTPMETHOD_GET,
//							requestToken);
//				} catch (Micro4blogException e) {
//					e.printStackTrace();
//				}
				
				editor.putBoolean("is_sohu_oauthed", isSohuOauthed);
//				editor.putString("sohu_oauth_token", values.getString("oauth_token"));
//				editor.putString("sohu_oauth_verifier", values.getString("oauth_verifier"));
//				
//				mRequestToken.setOauthToken(values.getString("oauth_token"));
//				mAccessToken.setOauthVerifier(values.getString("oauth_verifier"));
				
				editor.putString("sohu_oauth_verifier", micro4blog.getRequestToken().getOauthVerifier());
				
				editor.putString("sohu_access_token", mAccessToken.getOauthToken());
				
				// 签名需要 使用request token sercet
				editor.putString("sohu_oauth_token_sercet", mAccessToken.getOauthTokenSecret());
				
			}
			
			Toast.makeText(mThis, mAccessToken.getOauthToken() + "\n" + mAccessToken.getOauthTokenSecret(), Toast.LENGTH_SHORT).show();
			
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

		protected void getUserAccessToken(Bundle values) {
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
