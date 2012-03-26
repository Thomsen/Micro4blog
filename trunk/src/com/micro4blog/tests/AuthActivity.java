package com.micro4blog.tests;

import com.micro4blog.R;
import com.micro4blog.oauth.AccessToken;
import com.micro4blog.oauth.DialogError;
import com.micro4blog.oauth.Micro4blog;
import com.micro4blog.oauth.Micro4blogDialogListener;
import com.micro4blog.oauth.Micro4blogException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AuthActivity extends Activity {

	Micro4blog m4b;
	
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_test);
		
		Button b = (Button) findViewById(R.id.auth);
		
		b.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				m4b = Micro4blog.getInstance(Micro4blog.SERVER_SINA);
//				m4b = Micro4blog.getInstance(Micro4blog.SERVER_TENCENT);

				m4b.authorize(AuthActivity.this, new AuthDialogListener());
				
//				Toast.makeText(AuthActivity.this, m4b.getUrlOauth2AccessToken() + "", Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	public class AuthDialogListener implements Micro4blogDialogListener {

		public void onComplete(Bundle values) {

			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			AccessToken accessToken = new AccessToken(token, m4b.getAppSecret());
			accessToken.setExpiresIn(expires_in);
//			Micro4blog.getInstance(Micro4blog.SERVER_SINA).setAccessToken(accessToken);
			m4b.setAccessToken(accessToken);
			Intent intent = new Intent();
			intent.setClass(AuthActivity.this, TimelineActivity.class);
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
