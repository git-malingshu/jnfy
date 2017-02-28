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
	// ҹ��ģʽ����
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

		// ���ֲ�ʵ��ҹ��ģʽ
		// mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// String night_mode = new ApplicationProperties(this).getNight_mode();
		// if ("true".equals(night_mode)) {
		// night(true);
		// } else {
		// day();
		// }

		// ���յ��ͼ�괫�ݵĶ�Ӧ��url
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");

		// ҳ�����ӵ�ָ����url
		web = (WebView) findViewById(R.id.show);
		if (url == null) {
			url = "http://www.ifeng.com";
		}
		// ����webview�ؼ�
		web.getSettings().setJavaScriptEnabled(true);// ����JS
		web.setScrollBarStyle(0);// ���������Ϊ0���ǲ������������ռ䣬��������������ҳ��
		
		// ���ÿ���֧������ 
		web.getSettings().setSupportZoom(true); 
		// ���ó������Ź��� 
		web.getSettings().setBuiltInZoomControls(true);
		//�������������
		web.getSettings().setUseWideViewPort(true);
		//����Ӧ��Ļ
		web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		web.getSettings().setLoadWithOverviewMode(true);
		
		web.loadUrl(url);
		// webview��������
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
		// webview ҳ�����ʱ��ʾ
		web.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				// TODO Auto-generated method stub
				// super.onReceivedError(view, errorCode, description, failingUrl);
				System.out.println("err:" + errorCode + "  des:" + description + "   url:" + failingUrl);
				setConnection(description);

			}
		});
		// ��ʼ��������
		progressbar = new ProgressBar(WebActivity.this, null, android.R.attr.progressBarStyleHorizontal);
		// ���ý��������
		progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5));

		web.addView(progressbar);
		web.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				if (newProgress == 100) {
					// ����������ؽ�����
					progressbar.setVisibility(view.GONE);
				} else {
					if (progressbar.getVisibility() == view.GONE)
						progressbar.setVisibility(view.VISIBLE);
					progressbar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}

			// �ϴ��ļ�����ͬ�汾����
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
				mUploadMessage = uploadMsg;
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");

				startActivityForResult(Intent.createChooser(intent, "��ɲ�����Ҫʹ��"), FILECHOOSER_RESULTCODE);

			}

			// 3.0 + �����������
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
				mUploadMessage = uploadMsg;
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(Intent.createChooser(intent, "��ɲ�����Ҫʹ��"), FILECHOOSER_RESULTCODE);
			}

			// Android < 3.0 �����������
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				mUploadMessage = uploadMsg;
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(Intent.createChooser(intent, "��ɲ�����Ҫʹ��"), FILECHOOSER_RESULTCODE);
			}
		});

		// ���ذ�ť��ֱ�ӷ���һ����Ŀ
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
				// intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);// ���ò�Ҫˢ�½�Ҫ�����Ľ���
				// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// �ص���Ҫ���Ľ����м��activity
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

	// ѡ���ϴ����ļ����ȡ�ļ���Ϣ
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

		lp.gravity = Gravity.BOTTOM;// �����Զ�����ʾ��λ��
		lp.y = 10;// ����ײ��ľ�����10���� ����� top ���Ǿ���top��10����

		textView = new TextView(this);
		textView.setBackgroundColor(0x99000000);
		mWindowManager.addView(textView, lp);

	}

	public void day() {
		if (textView != null) {
			mWindowManager.removeView(textView);
		}
	}

	// �Զ���ҳ��������������쳣ʱ�����Ի���
	public void setConnection(String description) {
		AlertDialog.Builder builder = new Builder(WebActivity.this);
		builder.setTitle("��������");
		builder.setMessage(description + "�����Ƿ��������������⣬����ϵ����������Ա��ѯ��");

		builder.setPositiveButton("��������", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				int currentapiVersion = android.os.Build.VERSION.SDK_INT;
				//System.out.println("currentapiVersion = " + currentapiVersion);
				Intent intent;
				if (currentapiVersion < 11) {
					// 3.0�汾��ǰ
					intent = new Intent();
					intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
				} else {
					// 3.0�Ժ�
					// intent = new Intent( android.provider.Settings.ACTION_WIRELESS_SETTINGS);
					intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
				}
				WebActivity.this.startActivity(intent);
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.create().show();
	}

	// ������ؼ�r
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
