package com.gxzwzx.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gxzwzx.R;

public class ShowImgActivity extends Activity {
	private ImageButton img_return;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_wx);
		// 返回按钮，直接返回一级栏目
		img_return = (ImageButton) findViewById(R.id.img_return);
		img_return.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();

				// Intent intent = new Intent();
				// intent.setClass(WebActivity.this, MainActivity.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//
				// 设置不要刷新将要跳到的界面
				// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//
				// 关掉所要到的界面中间的activity
				// startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

	// 切换背景
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		ImageView welcomImageView = (ImageView) findViewById(R.id.img_welcome);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			welcomImageView.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.welcome_horizontal));
		} else {
			welcomImageView.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.welcome_vertical));
		}
	}

}
