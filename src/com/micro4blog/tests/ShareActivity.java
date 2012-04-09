package com.micro4blog.tests;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import com.micro4blog.R;
import com.micro4blog.http.AccessTokenHeader;
import com.micro4blog.http.ApiTokenHeader;
import com.micro4blog.http.HttpHeaderFactory;
import com.micro4blog.http.Micro4blogParameters;
import com.micro4blog.http.Oauth2AccessTokenHeader;
import com.micro4blog.http.RequestTokenHeader;
import com.micro4blog.http.Utility;
import com.micro4blog.oauth.AccessToken;
import com.micro4blog.oauth.Micro4blog;
import com.micro4blog.utils.AsyncMicro4blogRunner;
import com.micro4blog.utils.Micro4blogException;
import com.micro4blog.utils.AsyncMicro4blogRunner.RequestListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShareActivity extends Activity implements OnClickListener, RequestListener {
	
	private static final String TAG = "ShareActivity";

	public static final String EXTRA_ACCESS_TOKEN = "micro4blog.access.token";
	public static final String EXTRA_TOKEN_SECRET = "micro4blog.token.secret";
	public static final String EXTRA_MICRO4BLOG_CONTENT = "micro4blog.content";
	public static final String EXTRA_PIC_URI = "micro4blog.pic.uri";

	 private TextView mTextNum;
	    private Button mSend;
	    private EditText mEdit;
	    private FrameLayout mPiclayout;

	    private String mPicPath = "";
	    private String mContent = "";
	    private String mAccessToken = "";
	    private String mTokenSecret = "";

	    public static final int Micro4blog_MAX_LENGTH = 140;

	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
//	        this.setContentView(R.layout.share_mblog_view);

//	        Intent in = this.getIntent();
//	        mPicPath = in.getStringExtra(EXTRA_PIC_URI);
//	        mContent = in.getStringExtra(EXTRA_MICRO4BLOG_CONTENT);
//	        mAccessToken = in.getStringExtra(EXTRA_ACCESS_TOKEN);
//	        mTokenSecret = in.getStringExtra(EXTRA_TOKEN_SECRET);
//
//	        AccessToken accessToken = new AccessToken(mAccessToken, mTokenSecret);
//	        Micro4blog micro4blog = Micro4blog.getInstance(Micro4blog.SERVER_SINA);  // TODO four instance
//	        micro4blog.setAccessToken(accessToken);
//
//	        Button close = (Button) this.findViewById(R.id.btnClose);
//	        close.setOnClickListener(this);
//	        mSend = (Button) this.findViewById(R.id.btnSend);
//	        mSend.setOnClickListener(this);
//	        LinearLayout total = (LinearLayout) this.findViewById(R.id.ll_text_limit_unit);
//	        total.setOnClickListener(this);
//	        mTextNum = (TextView) this.findViewById(R.id.tv_text_limit);
//	        ImageView picture = (ImageView) this.findViewById(R.id.ivDelPic);
//	        picture.setOnClickListener(this);
//
//	        mEdit = (EditText) this.findViewById(R.id.etEdit);
//	        mEdit.addTextChangedListener(new TextWatcher() {
//	            public void afterTextChanged(Editable s) {
//	            }
//
//	            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//	            }
//
//	            public void onTextChanged(CharSequence s, int start, int before, int count) {
//	                String mText = mEdit.getText().toString();
//	                String mStr;
//	                int len = mText.length();
//	                if (len <= Micro4blog_MAX_LENGTH) {
//	                    len = Micro4blog_MAX_LENGTH - len;
//	                    mTextNum.setTextColor(R.color.text_num_gray);
//	                    if (!mSend.isEnabled())
//	                        mSend.setEnabled(true);
//	                } else {
//	                    len = len - Micro4blog_MAX_LENGTH;
//
//	                    mTextNum.setTextColor(Color.RED);
//	                    if (mSend.isEnabled())
//	                        mSend.setEnabled(false);
//	                }
//	                mTextNum.setText(String.valueOf(len));
//	            }
//	        });
//	        mEdit.setText(mContent);
//	        mPiclayout = (FrameLayout) ShareActivity.this.findViewById(R.id.flPic);
//	        if (TextUtils.isEmpty(this.mPicPath)) {
//	            mPiclayout.setVisibility(View.GONE);
//	        } else {
//	            mPiclayout.setVisibility(View.VISIBLE);
//	            File file = new File(mPicPath);
//	            if (file.exists()) {
//	                Bitmap pic = BitmapFactory.decodeFile(this.mPicPath);
//	                ImageView image = (ImageView) this.findViewById(R.id.ivImage);
//	                image.setImageBitmap(pic);
//	            } else {
//	                mPiclayout.setVisibility(View.GONE);
//	            }
//	        }
	        
	        Micro4blog micro4blog = Micro4blog.getInstance(Micro4blog.getCurrentServer());
	        
	        try {
				Log.i(TAG, getPublicTimeline(micro4blog));
				
				Toast.makeText(this, getPublicTimeline(micro4blog), Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}  
	        
	    }

	    public void onClick(View v) {
	        int viewId = v.getId();

	        if (viewId == R.id.btnClose) {
	            finish();
	        } else if (viewId == R.id.btnSend) {
	            Micro4blog micro4blog = Micro4blog.getInstance(Micro4blog.SERVER_SINA); // TODO four instance
	            try {
	            	// XXX 判断是否登录
	                if (!TextUtils.isEmpty((String) (micro4blog.getAccessToken().getOauthToken()))) {
	                    this.mContent = mEdit.getText().toString();
	                    if (!TextUtils.isEmpty(mPicPath)) {
	                        upload(micro4blog, micro4blog.getAppKey(), this.mPicPath, this.mContent, "", "");

	                    } else {
	                        // Just update a text Micro4blog!
	                        update(micro4blog, micro4blog.getAppKey(), mContent, "", "");
	                    }
	                } else {
	                    Toast.makeText(this, this.getString(R.string.please_login), Toast.LENGTH_LONG).show();
	                }
	            } catch (MalformedURLException e) {
	                e.printStackTrace();
	            } catch (IOException e) {
	                e.printStackTrace();
	            } catch (Micro4blogException e) {
	                e.printStackTrace();
	            }
	        } else if (viewId == R.id.ll_text_limit_unit) {
	            Dialog dialog = new AlertDialog.Builder(this).setTitle(R.string.attention)
	                    .setMessage(R.string.delete_all)
	                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int which) {
	                            mEdit.setText("");
	                        }
	                    }).setNegativeButton(R.string.cancel, null).create();
	            dialog.show();
	        } else if (viewId == R.id.ivDelPic) {
	            Dialog dialog = new AlertDialog.Builder(this).setTitle(R.string.attention)
	                    .setMessage(R.string.del_pic)
	                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int which) {
	                            mPiclayout.setVisibility(View.GONE);
	                        }
	                    }).setNegativeButton(R.string.cancel, null).create();
	            dialog.show();
	        }
	    }

	    private String upload(Micro4blog micro4blog, String source, String file, String status, String lon,
	            String lat) throws Micro4blogException {
	        Micro4blogParameters bundle = new Micro4blogParameters();
	        bundle.add("source", source);
	        bundle.add("pic", file);
	        bundle.add("status", status);
	        if (!TextUtils.isEmpty(lon)) {
	            bundle.add("lon", lon);
	        }
	        if (!TextUtils.isEmpty(lat)) {
	            bundle.add("lat", lat);
	        }
	        String rlt = "";
	        String url = micro4blog.getServerUrl() + "statuses/upload.json";
	        AsyncMicro4blogRunner micro4blogRunner = new AsyncMicro4blogRunner(micro4blog);
	        micro4blogRunner.request(this, url, bundle, Utility.HTTPMETHOD_POST, this);

	        return rlt;
	    }

	    private String update(Micro4blog micro4blog, String source, String status, String lon, String lat)
	            throws MalformedURLException, IOException, Micro4blogException {
	        Micro4blogParameters bundle = new Micro4blogParameters();
	        bundle.add("source", source);
	        bundle.add("status", status);
	        if (!TextUtils.isEmpty(lon)) {
	            bundle.add("lon", lon);
	        }
	        if (!TextUtils.isEmpty(lat)) {
	            bundle.add("lat", lat);
	        }
	        String rlt = "";
	        String url = micro4blog.getServerUrl() + "statuses/update.json";
	        AsyncMicro4blogRunner micro4blogRunner = new AsyncMicro4blogRunner(micro4blog);
	        micro4blogRunner.request(this, url, bundle, Utility.HTTPMETHOD_POST, this);
	     
	        return rlt;
	    }

	    public void onComplete(String response) {
	        runOnUiThread(new Runnable() {

	            public void run() {
	                Toast.makeText(ShareActivity.this, R.string.send_sucess, Toast.LENGTH_LONG).show();
	            }
	        });

	        this.finish();
	    }

	    public void onIOException(IOException e) {
	        // TODO Auto-generated method stub

	    }

	    public void onError(final Micro4blogException e) {
	        runOnUiThread(new Runnable() {

	            public void run() {
	                Toast.makeText(
	                        ShareActivity.this,
	                        String.format(ShareActivity.this.getString(R.string.send_failed) + ":%s",
	                                e.getMessage()), Toast.LENGTH_LONG).show();
	            }
	        });

	    }
	    
	    private String getPublicTimeline(Micro4blog micro4blog) throws MalformedURLException, IOException,
        Micro4blogException {
	    	
	    	
	    	
	    	String url = micro4blog.getServerUrl() + "statuses/friends_timeline.json";
		    Micro4blogParameters bundle = new Micro4blogParameters();

	    	
//		    String url = micro4blog.getServerUrl() + "statuses/public_timeline.json";
	    	
//		    bundle.add("source", micro4blog.getAppKey());
//		    bundle.add("oauth_verifier", micro4blog.getAccessToken().getOauthVerifier());
//		    bundle.add("oauth_token", micro4blog.getAccessToken().getOauthToken());
		    
		    bundle.add("count", "20");
		    
		    String rlt = micro4blog.request(this, url, bundle, Utility.HTTPMETHOD_GET, micro4blog.getAccessToken());
		    return rlt;
	    } 
}
