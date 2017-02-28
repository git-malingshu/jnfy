package com.gxzwzx.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gxzwzx.R;

public class WebActivity extends Activity {
	// 夜间模式参数
	private WindowManager mWindowManager;
	public TextView textView = null;
	private WebView web;

	private ImageButton web_return;

	private ValueCallback<Uri> mUploadMessage;
	private final int FILECHOOSER_RESULTCODE = 1;

	private ProgressBar progressbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_web);

		// 遮罩层实现夜间模式
		// mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// String night_mode = new ApplicationProperties(this).getNight_mode();
		// if ("true".equals(night_mode)) {
		// night(true);
		// } else {
		// day();
		// }

		// 接收点击图标传递的对应的url
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");

		// 页面连接到指定的url
		web = (WebView) findViewById(R.id.show);
		if (url == null) {
			url = "http://www.ifeng.com";
		}
		// 设置webview控件
		web.getSettings().setJavaScriptEnabled(true);// 可用JS
		web.setScrollBarStyle(0);// 滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
		
		// 设置可以支持缩放 
		web.getSettings().setSupportZoom(true); 
		// 设置出现缩放工具 
		web.getSettings().setBuiltInZoomControls(true);
		//扩大比例的缩放
		web.getSettings().setUseWideViewPort(true);
		//自适应屏幕
		web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		web.getSettings().setLoadWithOverviewMode(true);
		
		web.loadUrl(url);
		// webview下载设置
		web.setDownloadListener(new DownloadListener() {

			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
					long contentLength) {
				Log.i("tag", "url=" + url);
				Log.i("tag", "userAgent=" + userAgent);
				Log.i("tag", "contentDisposition=" + contentDisposition);
				Log.i("tag", "mimetype=" + mimetype);
				Log.i("tag", "contentLength=" + contentLength);
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
		// webview 页面错误时提示
		web.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				// TODO Auto-generated method stub
				// super.onReceivedError(view, errorCode, description, failingUrl);
				System.out.println("err:" + errorCode + "  des:" + description + "   url:" + failingUrl);
				setConnection(description);

			}
		});
		// 初始化进度条
		progressbar = new ProgressBar(WebActivity.this, null, android.R.attr.progressBarStyleHorizontal);
		// 设置进度条风格
		progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5));

		web.addView(progressbar);
		web.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				if (newProgress == 100) {
					// 加载完成隐藏进度条
					progressbar.setVisibility(view.GONE);
				} else {
					if (progressbar.getVisibility() == view.GONE)
						progressbar.setVisibility(view.VISIBLE);
					progressbar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}

			// 上传文件，不同版本兼容
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
				mUploadMessage = uploadMsg;
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");

				startActivityForResult(Intent.createChooser(intent, "完成操作需要使用"), FILECHOOSER_RESULTCODE);

			}

			// 3.0 + 调用这个方法
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
				mUploadMessage = uploadMsg;
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(Intent.createChooser(intent, "完成操作需要使用"), FILECHOOSER_RESULTCODE);
			}

			// Android < 3.0 调用这个方法
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				mUploadMessage = uploadMsg;
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(Intent.createChooser(intent, "完成操作需要使用"), FILECHOOSER_RESULTCODE);
			}
		});

		// 返回按钮，直接返回一级栏目
		web_return = (ImageButton) findViewById(R.id.web_return);
		web_return.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				WebView web = (WebView) findViewById(R.id.show);
				if (web.canGoBack()) {
					web.goBack();
				} else {
					setResult(RESULT_OK);
					finish();
				}

				// Intent intent = new Intent();
				// intent.setClass(WebActivity.this, MainActivity.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);// 设置不要刷新将要跳到的界面
				// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 关掉所要到的界面中间的activity
				// startActivity(intent);
			}
		});
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
		} else {
		}
	}

	// 选择上传的文件后获取文件信息
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == FILECHOOSER_RESULTCODE) {
			// mUploadMessage = wcci.getmUploadMessage();
			if (null == mUploadMessage)
				return;
			Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
	}

	public void night(boolean flag) {
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		lp.gravity = Gravity.BOTTOM;// 可以自定义显示的位置
		lp.y = 10;// 距离底部的距离是10像素 如果是 top 就是距离top是10像素

		textView = new TextView(this);
		textView.setBackgroundColor(0x99000000);
		mWindowManager.addView(textView, lp);

	}

	public void day() {
		if (textView != null) {
			mWindowManager.removeView(textView);
		}
	}

	// 自定义页面出错，网络连接异常时弹出对话框
	public void setConnection(String description) {
		AlertDialog.Builder builder = new Builder(WebActivity.this);
		builder.setTitle("设置网络");
		builder.setMessage(description + "请检查是否网络设置有问题，或联系服务器管理员咨询。");

		builder.setPositiveButton("设置网络", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				int currentapiVersion = android.os.Build.VERSION.SDK_INT;
				//System.out.println("currentapiVersion = " + currentapiVersion);
				Intent intent;
				if (currentapiVersion < 11) {
					// 3.0版本以前
					intent = new Intent();
					intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
				} else {
					// 3.0以后
					// intent = new Intent( android.provider.Settings.ACTION_WIRELESS_SETTINGS);
					intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
				}
				WebActivity.this.startActivity(intent);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.create().show();
	}

	// 点击返回键r
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (web.canGoBack()) {
				web.goBack();
			} else {
				setResult(RESULT_OK);
				finish();
			}
		}
		return true;
	}

}
