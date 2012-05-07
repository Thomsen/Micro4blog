package com.micro4blog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 全局界面结构，控制全局的界面基调
 * @author Thomsen
 *
 */
public class GlobalFramework extends Activity  implements OnClickListener{
	
	public static SharedPreferences gShared;
	public static LayoutParams gParams;
	
	protected RelativeLayout gHeaderLayout;
	protected Button gHeaderLeftButton;
	protected TextView gHeaderContent;
	protected Button gHeaderRightButton;
	
	protected LinearLayout gFooterLayout;
	protected Button gFooterHome;
	protected Button gFooterMessage;
	protected Button gFooterProfile;
	protected Button gFooterSquare;
	protected Button gFooterMore;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		gParams = new LayoutParams(LayoutParams.FILL_PARENT, 
				LayoutParams.WRAP_CONTENT);
		
		gShared = getSharedPreferences("micro4blog", 0);
		
		setContentView(R.layout.main_content);
		
		// 设置标题
		setHeaderUp();
		
		// 设置导航栏
		setFooterUp();
		

		
	}

	protected void setHeaderUp() {
		gHeaderLayout = (RelativeLayout) findViewById(R.id.header);
		
		gHeaderLeftButton = (Button) findViewById(R.id.header_left);
		gHeaderContent = (TextView) findViewById(R.id.header_content);
		gHeaderRightButton = (Button) findViewById(R.id.header_right);
		
		gHeaderLeftButton.setOnClickListener(this);
		gHeaderRightButton.setOnClickListener(this);
	}
	
	protected void setFooterUp() {
		
		gFooterLayout = (LinearLayout) findViewById(R.id.footer);
		gFooterLayout.setVisibility(View.GONE);
		
		gFooterHome = (Button) findViewById(R.id.footer_home);
		gFooterMessage = (Button) findViewById(R.id.footer_message);
		gFooterProfile = (Button) findViewById(R.id.footer_profile);
		gFooterSquare = (Button) findViewById(R.id.footer_square);
		gFooterMore = (Button) findViewById(R.id.footer_more);
		
		gFooterHome.setOnClickListener(this);
		gFooterMessage.setOnClickListener(this);
		gFooterProfile.setOnClickListener(this);
		gFooterSquare.setOnClickListener(this);
		gFooterMore.setOnClickListener(this);
	
	}



	@Override
	public void onClick(View paramView) {
		
	}
	
	
	

}
