package com.micro4blog.tests;

import com.micro4blog.R;
import com.micro4blog.dialog.DialogError;
import com.micro4blog.dialog.Micro4blogDialogListener;
import com.micro4blog.http.Utility;
import com.micro4blog.oauth.AccessToken;
import com.micro4blog.oauth.Micro4blog;
import com.micro4blog.utils.Micro4blogException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AuthActivity extends Activity {

	Micro4blog m4b;
	
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_test);
		
		Button sina = (Button) findViewById(R.id.sina);
		Button tencent = (Button) findViewById(R.id.tencent);
		Button netease = (Button) findViewById(R.id.netease);
		Button sohu = (Button) findViewById(R.id.sohu);
		
		
		
		sina.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				m4b = Micro4blog.getInstance(Micro4blog.SERVER_SINA);

				m4b.authorize(AuthActivity.this, new AuthDialogListener());
				
			}
		});
		
		tencent.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				m4b = Micro4blog.getInstance(Micro4blog.SERVER_TENCENT);
				
				m4b.authorize(AuthActivity.this, new AuthDialogListener());
				
			}
		});
		
		netease.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				m4b = Micro4blog.getInstance(Micro4blog.SERVER_NETEASE);
				
				m4b.authorize(AuthActivity.this, new AuthDialogListener());
				
			}
		});
		
		sohu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				m4b = Micro4blog.getInstance(Micro4blog.SERVER_SOHU);
				
				m4b.authorize(AuthActivity.this, new AuthDialogListener());
				
			}
		});
		
		
		
	}
	
	protected void onDestory() {
		super.onDestroy();
		
		Utility.clearCookies(this);
	}
	
	public class AuthDialogListener implements Micro4blogDialogListener {

		public void onComplete(Bundle values) {

			String token = values.getString("access_token");
			
			if (Micro4blog.getCurrentServer() != Micro4blog.SERVER_SINA) {
				token = values.getString("oauth_token");
			}
			
			String expires_in = values.getString("expires_in");
			AccessToken accessToken = new AccessToken(token, m4b.getAppSecret());
			accessToken.setExpiresIn(expires_in);
//			Micro4blog.getInstance(Micro4blog.SERVER_SINA).setAccessToken(accessToken);
			m4b.setAccessToken(accessToken);
			Intent intent = new Intent();
			intent.setClass(AuthActivity.this, TimelineActivity.class);
			
			intent.putExtra("acctoken", token);
			
			startActivity(intent);
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
	
}
