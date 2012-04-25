package com.micro4blog.dialog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.micro4blog.Micro4blog;
import com.micro4blog.R;
import com.micro4blog.android.SslError;
import com.micro4blog.http.Utility;
import com.micro4blog.utils.Micro4blogException;

/**
 * 用户授权的dialog
 */
public class Micro4blogDialog extends Dialog {

	static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
    static final int MARGIN = 4;
    static final int PADDING = 2;

    private Micro4blog mMicro4blog;
    private String mUrl;
    private Micro4blogDialogListener mListener;
    private ProgressDialog mSpinner;
    private ImageView mBtnClose;
    private WebView mWebView;
    private RelativeLayout webViewContainer;
    private RelativeLayout mContent;
    
    // 防止handleRedirectUrl执行两次
    // 因为sina存在隐式授权，所以加了这个，其他服务不行
	boolean isHandled = true;

    public Micro4blogDialog(Micro4blog micro4blog, Context context, String url, Micro4blogDialogListener listener) {
        super(context, R.style.ContentOverlay);
        mMicro4blog = micro4blog;
        mUrl = url;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSpinner = new ProgressDialog(getContext());
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContent = new RelativeLayout(getContext());

        setUpWebView();
        // setUpCloseBtn();

        addContentView(mContent, new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));       
    }
   

    private void setUpWebView() {
        webViewContainer = new RelativeLayout(getContext());
        // webViewContainer.setOrientation(LinearLayout.VERTICAL);

        // webViewContainer.addView(title, new
        // LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
        // getContext().getResources().getDimensionPixelSize(R.dimen.dialog_title_height)));

        mWebView = new WebView(getContext());
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new Micro4blogDialog.Micro4blogWebViewClient());
        mWebView.loadUrl(mUrl);
        mWebView.setLayoutParams(FILL);
        mWebView.setVisibility(View.INVISIBLE);
        
        webViewContainer.addView(mWebView);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        Resources resources = getContext().getResources();
//        lp.leftMargin = resources.getDimensionPixelSize(R.dimen.dialog_left_margin);
//        lp.topMargin = resources.getDimensionPixelSize(R.dimen.dialog_top_margin);
//        lp.rightMargin = resources.getDimensionPixelSize(R.dimen.dialog_right_margin);
//        lp.bottomMargin = resources.getDimensionPixelSize(R.dimen.dialog_bottom_margin);
        mContent.addView(webViewContainer, lp);
    }



	@SuppressWarnings("unused")
	private void setUpCloseBtn() {
        mBtnClose = new ImageView(getContext());
        mBtnClose.setClickable(true);
        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancel();
                Micro4blogDialog.this.dismiss();
            }
        });

        mBtnClose.setImageResource(R.drawable.close_selector);
        mBtnClose.setVisibility(View.INVISIBLE);

        RelativeLayout.LayoutParams closeBtnRL = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        closeBtnRL.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        closeBtnRL.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        closeBtnRL.topMargin = getContext().getResources().getDimensionPixelSize(
                R.dimen.dialog_btn_close_right_margin);
        closeBtnRL.rightMargin = getContext().getResources().getDimensionPixelSize(
                R.dimen.dialog_btn_close_top_margin);

        webViewContainer.addView(mBtnClose, closeBtnRL);
    }

    private class Micro4blogWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 待后台增加对默认重定向地址的支持后修改下面的逻辑
            if (url.startsWith(mMicro4blog.getRedirectUrl())  && isHandled) {           
        		handleRedirectUrl(view, url);               
//                Micro4blogDialog.this.dismiss();
//                return true;
            }
            
            isHandled = true;
            Micro4blogDialog.this.dismiss();
            return true;
                                             
//             launch non-dialog URLs in a full browser， TODO: 原来是在这里执行的
//            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mListener.onError(new DialogError(description, errorCode, failingUrl));
            Micro4blogDialog.this.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
              	
        	// google issue. shouldOverrideUrlLoading not executed
        	// 但是测试sohu授权时，shouldOverideUrlLoading能够执行
            if (url.startsWith(mMicro4blog.getRedirectUrl())) {
                handleRedirectUrl(view, url);
                view.stopLoading();
                Micro4blogDialog.this.dismiss();                
                isHandled = false;
                
                return;
            }
            super.onPageStarted(view, url, favicon);  // sina和tencent的区别，是tencent执行了两次， 然后执行到shouldOverrideUrlLoading
            mSpinner.show(); // 对话框开始
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mSpinner.dismiss();  // 对话框结束

            mContent.setBackgroundColor(Color.TRANSPARENT);
            webViewContainer.setBackgroundResource(R.drawable.dialog_bg);
            
        	mWebView.setVisibility(View.VISIBLE);     	            
        }

        @SuppressWarnings("unused")
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            
        }

    }

    private void handleRedirectUrl(WebView view, String url) {
        Bundle values = Utility.parseUrl(url);

        String error = values.getString("error");
        String error_code = values.getString("error_code");

        if (error == null && error_code == null) {
        	// 执行了Micor4blog下的service中的监听事件
            mListener.onComplete(values);
        } else if (error.equals("access_denied")) {
            // 用户或授权服务器拒绝授予数据访问权限
            mListener.onCancel();
        } else {
            mListener.onMicro4blogException(new Micro4blogException(error, Integer.parseInt(error_code)));
        }
    }

    @SuppressWarnings("unused")
	private static String getHtml(String urlString) {

        try {

            StringBuffer html = new StringBuffer();
            SocketAddress sa = new InetSocketAddress("10.75.0.103", 8093);
            Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP, sa);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String temp;

            while ((temp = br.readLine()) != null) {
                html.append(temp);
            }

            br.close();
            isr.close();
            return html.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }

}
