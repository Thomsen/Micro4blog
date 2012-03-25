package com.micro4blog.oauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class Micro4blogForNetease extends Micro4blog {

	@Override
	protected void initConfig() {
		
		setAppKey("5V20v8ORzD8ie78k");
		setAppSecret("O3iJyOQM5WQZD7tJjew7bpbHpQYt8VKy");

		setUrlRequestToken("http://api.t.163.com/oauth/request_token");
		setUrlAccessToken("http://api.t.163.com/oauth/access_token");
		setUrlAccessAuthorize("http://api.t.163.com/oauth/authenticate");
		
		
		
	}

	@Override
	protected void authorize(Activity activity, String[] permissions,
			int activityCode, Micro4blogDialogListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void startDialogAuth(Activity activity, String[] permissions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void dialog(Context context, Micro4blogParameters parameters,
			Micro4blogDialogListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void authorizeCallBack(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		
	}

}
