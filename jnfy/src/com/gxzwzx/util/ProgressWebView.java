package com.gxzwzx.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * 自定义带进度条WebView
 * 
 */
public class ProgressWebView extends WebView {
	/** 进度条 */
	private ProgressBar progressbar;

	private ValueCallback<Uri> mUploadMessage;
	private final int FILECHOOSER_RESULTCODE = 1;

	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
//		// 初始化进度条
//		progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
//		// 设置进度条风格
//		progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5, 0, 0));
//		progressbar.getWidth();
//
//		addView(progressbar);
//		setWebChromeClient(new WebChromeClient() {
//
//			@Override
//			public void onProgressChanged(WebView view, int newProgress) {
//				// TODO Auto-generated method stub
//				super.onProgressChanged(view, newProgress);
//				if (newProgress == 100) {
//					// 加载完成隐藏进度条
//					progressbar.setVisibility(GONE);
//				} else {
//					if (progressbar.getVisibility() == GONE)
//						progressbar.setVisibility(VISIBLE);
//					progressbar.setProgress(newProgress);
//				}
//				super.onProgressChanged(view, newProgress);
//			}
//
//		});
	}

	// public class WebChromeClient extends android.webkit.WebChromeClient {
	// @Override
	// public void onProgressChanged(WebView view, int newProgress) {
	// if (newProgress == 100) {
	// // 加载完成隐藏进度条
	// progressbar.setVisibility(GONE);
	// } else {
	// if (progressbar.getVisibility() == GONE)
	// progressbar.setVisibility(VISIBLE);
	// progressbar.setProgress(newProgress);
	// }
	// super.onProgressChanged(view, newProgress);
	// }
	// }

//	@Override
//	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//		LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
//		lp.x = l;
//		lp.y = t;
//		progressbar.setLayoutParams(lp);
//		super.onScrollChanged(l, t, oldl, oldt);
//	}

}
