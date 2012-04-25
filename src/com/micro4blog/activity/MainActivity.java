package com.micro4blog.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.micro4blog.GlobalFramework;
import com.micro4blog.Micro4blog;
import com.micro4blog.R;
import com.micro4blog.dialog.DialogError;
import com.micro4blog.dialog.Micro4blogDialogListener;
import com.micro4blog.http.ApiTokenHeader;
import com.micro4blog.http.Oauth2AccessTokenHeader;
import com.micro4blog.http.Utility;
import com.micro4blog.oauth.AccessToken;
import com.micro4blog.oauth.OauthToken;
import com.micro4blog.oauth.RequestToken;
import com.micro4blog.plugin.PluginImpl;
import com.micro4blog.tests.ShareActivity;
import com.micro4blog.utils.Micro4blogException;

public class MainActivity extends GlobalFramework {

	private final String TAG = "MainActivity";

	private GridView mGridView;
	private Activity mActivity;

	private boolean isSinaOauthed = false;
	private boolean isSohuOauthed = false;
	private boolean isTencentOauthed = false;
	private boolean isNeteaseOauthed = false;

	Micro4blog micro4blog;

	// RequestToken mRequestToken = new RequestToken();
	// OauthToken mAccessToken;

	OauthToken mAccessToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = this;

		setContentView(R.layout.main_login);

		setContentUp();
		setListener();

		setHeaderUp();
		
		// test 
		// 无法找到与intellij idea中的相同配置
		// 暂时只有在intellij中进行插件开发
		// 通过idea中设置scope，只要不是compile即可运行
		// 这种方式不知到发布正常apk是否可以？
		// eclipse中以工程依赖，不以库依赖开发插件
		PluginImpl pi = new PluginImpl(mActivity);
		pi.useDexClassLoader2();

	}

	public void onResume() {
		super.onStart();

		isSinaOauthed = gShared.getBoolean("is_sina_oauthed", false);
		isSohuOauthed = gShared.getBoolean("is_sohu_oauthed", false);
		isTencentOauthed = gShared.getBoolean("is_tencent_oauthed", false);
		isNeteaseOauthed = gShared.getBoolean("is_netease_oauthed", false);

		// mAccessToken = new OauthToken();
		// mAccessToken.setOauthToken(gSharedPreferences.getString("sina_access_token",
		// ""));

	}

	private void readPreferences() {

		if (mAccessToken == null) {
			mAccessToken = new OauthToken();
		}

		if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_SINA) {

			mAccessToken = new AccessToken(gShared.getString(
					"sina_access_token", null), micro4blog.getAppSecret());

			// mAccessToken.setOauthToken(gSharedPreferences.getString("sina_access_token",
			// null));
			mAccessToken.setExpiresIn(gShared
					.getString("sina_expires_in", null));

			Utility.setAuthorization(new Oauth2AccessTokenHeader());

		} else if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_TENCENT) {

			mAccessToken.setOauthVerifier(gShared.getString(
					"tencent_oauth_verifier", null));

			mAccessToken.setOauthToken(gShared.getString(
					"tencent_access_token", null));
			mAccessToken.setOauthTokenSecret(gShared.getString(
					"tencent_oauth_token_sercet", null));

			Utility.setAuthorization(new ApiTokenHeader());

		} else if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_NETEASE) {

			// mAccessToken.setOauthVerifier(gSharedPreferences.getString("sohu_oauth_verifier",
			// null));

			mAccessToken.setOauthToken(gShared.getString(
					"netease_access_token", null));
			mAccessToken.setOauthTokenSecret(gShared.getString(
					"netease_oauth_token_sercet", null));

			Utility.setAuthorization(new ApiTokenHeader());

		} else if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_SOHU) {

			// try {
			// mRequestToken = mMicro4blog.getRequestToken(mThis,
			// Utility.HTTPMETHOD_GET, mMicro4blog.getAppKey(),
			// mMicro4blog.getAppSecret(), mMicro4blog.getRedirectUrl());
			//
			// mRequestToken.setOauthVerifier(gSharedPreferences.getString("sohu_oauth_verifier",
			// ""));
			//
			// mAccessToken = mMicro4blog.generateAccessToken(mThis,
			// Utility.HTTPMETHOD_GET, mRequestToken);
			//
			// } catch (Micro4blogException e) {
			// e.printStackTrace();
			// }

			// mRequestToken.setOauthToken(gSharedPreferences.getString("sohu_oauth_token",
			// ""));

			mAccessToken.setOauthVerifier(gShared.getString(
					"sohu_oauth_verifier", null));

			mAccessToken.setOauthToken(gShared.getString("sohu_access_token",
					null));
			mAccessToken.setOauthTokenSecret(gShared.getString(
					"sohu_oauth_token_sercet", null));

			Utility.setAuthorization(new ApiTokenHeader());

		}

		Log.d(TAG, "token: " + mAccessToken.getOauthToken() + "\n" + "secret: "
				+ mAccessToken.getOauthTokenSecret());

		// mMicro4blog.setRequestToken(mRequestToken);

		// mAccessToken.setOauthTokenSecret(mMicro4blog.getAppSecret());

		micro4blog.setAccessToken(mAccessToken);
	}

	// ==============================================
	/*
	 * 设置主页中不用服务的监听事件
	 */
	// ==============================================
	private void setListener() {
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// ImageView imageView = (ImageView) arg1;
				// imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

				// LinearLayout ll = (LinearLayout) arg1;
				// ImageView imageView = (ImageView) ll.getChildAt(0);
				//
				// imageView.setAdjustViewBounds(false);
				// imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				// imageView.setPadding(8, 8, 8, 8);

				// 登录服务器
				loginServer(arg2);

			}
		});

		mGridView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {

						// 添加服务器登录账户
						registerServer(arg2);

						return false;
					}
				});

		mGridView
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

					}

					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

	}

	/**
	 * 用户登录到主页，若没有添加账户，需要提示长按进行用户授权
	 * 
	 * @param serverType
	 */
	protected void loginServer(int serverType) {

		micro4blog = Micro4blog.getInstance(serverType);
		Intent intent = new Intent(mActivity, HomeTimelineActivity.class);
		// Intent intent = new Intent(mThis, ShareActivity.class);

		readPreferences();

		switch (serverType) {
		case Micro4blog.SERVER_SINA: {

			if (isSinaOauthed
					&& (System.currentTimeMillis() < mAccessToken
							.getExpiresIn())) {

				// try {
				// mMicro4blog.share2weibo(mThis, mAccessToken.getOauthToken(),
				// mMicro4blog.getAppSecret(), "adbds", null);
				// } catch (Micro4blogException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				// TODO 传递数据到timeline中显示，这时候就需要通信
				startActivity(intent);

			} else {
				isSinaOauthed = false;
				Toast.makeText(mActivity, "请长按进行新浪授权", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		}
		case Micro4blog.SERVER_TENCENT: {

			if (isTencentOauthed) {
				startActivity(intent);
			} else {
				Toast.makeText(mActivity, "请长按进行腾讯授权", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		}
		case Micro4blog.SERVER_NETEASE: {

			if (isNeteaseOauthed) {
				startActivity(intent);
			} else {
				Toast.makeText(mActivity, "请长按进行网易授权", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		}
		case Micro4blog.SERVER_SOHU: {

			if (isSohuOauthed) {
				startActivity(intent);
			} else {
				Toast.makeText(mActivity, "请长按进行搜狐授权", Toast.LENGTH_SHORT)
						.show();
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
	 * 
	 * @param serverType
	 */
	protected void registerServer(int serverType) {
		switch (serverType) {
		case Micro4blog.SERVER_SINA: {
			if (isSinaOauthed) {
				Toast.makeText(mActivity, "已经授权新浪服务了", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			break;
		}
		case Micro4blog.SERVER_TENCENT: {
			if (isTencentOauthed) {
				Toast.makeText(mActivity, "已经授权腾讯服务了", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			break;
		}
		case Micro4blog.SERVER_NETEASE: {
			if (isNeteaseOauthed) {
				Toast.makeText(mActivity, "已经授权网易服务了", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			break;
		}
		case Micro4blog.SERVER_SOHU: {
			if (isSohuOauthed) {
				Toast.makeText(mActivity, "已经授权搜狐服务了", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			break;
		}
		default: {
			break;
		}
		}

		micro4blog = Micro4blog.getInstance(serverType);
		micro4blog.authorize(mActivity, new MainAuthDialogListener());

	}

	/**
	 * 该内部类主要是针对授权后的回调处理
	 * 
	 * @author Thomsen
	 * 
	 */
	public class MainAuthDialogListener implements Micro4blogDialogListener {

		SharedPreferences.Editor editor = gShared.edit();

		// Syntax error on token ";", , expected TODO why
		// mAccessToken = new AccessToken();

		@Override
		public void onComplete(Bundle values) {

			mAccessToken = micro4blog.getAccessToken();

			if (Micro4blog.getCurrentServer() == Micro4blog.SERVER_SINA) {

				isSinaOauthed = true;
				editor.putBoolean("is_sina_oauthed", isSinaOauthed);

				editor.putString("sina_access_token",
						values.getString("access_token"));
				editor.putString("sina_expires_in",
						values.getString("expires_in"));

				// mAccessToken = new
				// AccessToken(values.getString("access_token"),
				// micro4blog.getAppSecret());
				//
				// mAccessToken.setExpiresIn(values.getString("expires_in"));
				// mAccessToken.setOauthToken(values.getString("access_token"));

				Log.d(TAG, values.toString());

			} else if (Micro4blog.SERVER_TENCENT == Micro4blog
					.getCurrentServer()) {

				isTencentOauthed = true;
				editor.putBoolean("is_tencent_oauthed", isTencentOauthed);

				editor.putString("tencent_access_token",
						mAccessToken.getOauthToken());
				editor.putString("tencent_oauth_token_sercet",
						mAccessToken.getOauthTokenSecret());
				editor.putString("tencent_oauth_verifier",
						values.getString("oauth_verifier"));

				// mRequestToken.setOauthToken(values.getString("oauth_token"));

			} else if (Micro4blog.SERVER_NETEASE == Micro4blog
					.getCurrentServer()) {

				isNeteaseOauthed = true;

				editor.putBoolean("is_netease_oauthed", isNeteaseOauthed);

				editor.putString("netease_access_token",
						mAccessToken.getOauthToken());
				editor.putString("netease_oauth_token_sercet",
						mAccessToken.getOauthTokenSecret());

				// oauth第三步，换取access token
				// RequestToken requestToken = micro4blog.getRequestToken();
				// //
				// requestToken.setOauthVerifier(values.getString("oauth_verifier"));
				// try {
				// mAccessToken = micro4blog.generateAccessToken(mThis,
				// Utility.HTTPMETHOD_GET,
				// requestToken);
				// } catch (Micro4blogException e) {
				// e.printStackTrace();
				// }

				// Toast.makeText(mThis, mAccessToken.getOauthToken() + "\n" +
				// mAccessToken.getOauthTokenSecret(),
				// Toast.LENGTH_SHORT).show();

			} else if (Micro4blog.SERVER_SOHU == Micro4blog.getCurrentServer()) {

				isSohuOauthed = true;
				mAccessToken = (AccessToken) micro4blog.getAccessToken();

				// Toast.makeText(mThis, values.toString(),
				// Toast.LENGTH_SHORT).show();

				// RequestToken requestToken = mMicro4blog.getRequestToken();
				// requestToken.setOauthVerifier(values.getString("oauth_verifier"));
				// try {
				// mAccessToken = mMicro4blog.generateAccessToken(mThis,
				// Utility.HTTPMETHOD_GET,
				// requestToken);
				// } catch (Micro4blogException e) {
				// e.printStackTrace();
				// }

				editor.putBoolean("is_sohu_oauthed", isSohuOauthed);
				// editor.putString("sohu_oauth_token",
				// values.getString("oauth_token"));
				// editor.putString("sohu_oauth_verifier",
				// values.getString("oauth_verifier"));
				//
				// mRequestToken.setOauthToken(values.getString("oauth_token"));
				// mAccessToken.setOauthVerifier(values.getString("oauth_verifier"));

				editor.putString("sohu_oauth_verifier", micro4blog
						.getRequestToken().getOauthVerifier());

				editor.putString("sohu_access_token",
						mAccessToken.getOauthToken());

				// 签名需要 使用request token sercet
				editor.putString("sohu_oauth_token_sercet",
						mAccessToken.getOauthTokenSecret());

			}

			Toast.makeText(
					mActivity,
					mAccessToken.getOauthToken() + "\n"
							+ mAccessToken.getOauthTokenSecret(),
					Toast.LENGTH_SHORT).show();

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

	private void setContentUp() {
		mGridView = (GridView) findViewById(R.id.main_grid);

//		ListAdapter adapter = new SimpleAdapter(mActivity, getMapData(),
//				R.layout.gird_server, new String[] { "image", "text" },
//				new int[] { R.id.grid_image, R.id.grid_text });
//
//		mGridView.setAdapter(adapter);
		
		mGridView.setAdapter(new ServerAdapter());
	}

	/**
	 * 登录显示界面
	 * 
	 * @return
	 */

	private List<Map<String, Object>> getMapData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// Map<String, Object> map = new HashMap<String, Object>();

		String[] text = new String[] { "Sina", "Tencent", "Netease", "Sohu",
				"More" };
		int[] imageId = new int[] { R.drawable.sina_logo,
				R.drawable.tencent_logo, R.drawable.netease_logo,
				R.drawable.sohu_logo, R.drawable.ic_launcher };

		for (int i = 0; i < text.length; i++) {
			// 要在这里初始化，不然结果都一样
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("text", text[i]);
			map.put("image", imageId[i]);
			list.add(map);
		}

		return list;
	}

	protected void setHeaderUp() {
		super.setHeaderUp();

		gHeaderLeftButton.setText("作者微博");
		gHeaderContent.setText("服务");
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_left: {
			Toast.makeText(mActivity, "作者微博", Toast.LENGTH_SHORT).show();
			break;
		}
		default:
			break;
		}
	}

	class ServerAdapter extends BaseAdapter {

		private List<ResolveInfo> mApps;
		
		String[] text = new String[] { "Sina", "Tencent", "Netease", "Sohu",
									"More" };
		int[] imageId = new int[] { R.drawable.sina_logo,
				R.drawable.tencent_logo, R.drawable.netease_logo,
				R.drawable.sohu_logo, R.drawable.ic_launcher };

		public ServerAdapter() {
			// Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			// mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			// mApps = getPackageManager().queryIntentActivities(mainIntent, 0);
		}

		@Override
		public int getCount() {
			return imageId.length;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			
			
			if (arg1 == null) {
//				i = new ImageView(mActivity);
//				i.setScaleType(ImageView.ScaleType.FIT_CENTER);
//				i.setLayoutParams(new GridView.LayoutParams(50, 50));
				
				arg1 = (LinearLayout) getLayoutInflater().inflate(R.layout.gird_server, null);
				arg1.setTag(R.id.grid_image, arg1.findViewById(R.id.grid_image));
				arg1.setTag(R.id.grid_text, arg1.findViewById(R.id.grid_text));
				
			} 
//			ResolveInfo info = mApps.get(arg0);
//			i.setImageDrawable(info.activityInfo.loadIcon(getPackageManager()));
			
			ImageView imageView = ((ImageView) arg1.getTag(R.id.grid_image));
//			ServerImageView imageView = (ServerImageView) arg1.getTag(R.id.grid_image);
			imageView.setAdjustViewBounds(false);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
//			imageView.setImageResource(imageId[arg0]);
			
			ServerImageView serverImageView = new ServerImageView(mActivity);
			
			imageView.setImageDrawable(serverImageView.setImageBg(imageId[arg0]));
			
			((TextView) arg1.getTag(R.id.grid_text)).setText(text[arg0]);
			
			return arg1;
		}

		

	}
	
	class ServerImageView extends ImageView {
		
		public ServerImageView(Context context) {
			super(context);
		}

		public StateListDrawable setImageBg(int id) {
			StateListDrawable bg = new StateListDrawable();
			
			Drawable drawable = getResources().getDrawable(id);
			
			Drawable pressed = getResources().getDrawable(R.drawable.ic_launcher);
//			pressed.setBounds(10, 10, 10, 10);
			
			
			// 单独使用View无法找到PRESSED，因其保护权限，顾可通过继承来访问
			bg.addState(View.PRESSED_ENABLED_STATE_SET, pressed);
			
			bg.addState(View.ENABLED_STATE_SET, drawable);
			

			return bg;
	}
	}

}
