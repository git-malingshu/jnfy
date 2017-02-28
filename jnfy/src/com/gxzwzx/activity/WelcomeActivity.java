package com.gxzwzx.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.gxzwzx.R;

public class WelcomeActivity extends Activity {
	public WindowManager mWindowManager;
	public TextView textView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		ImageView welcomImageView = (ImageView) findViewById(R.id.img_welcome);
		welcomImageView.setScaleType(ScaleType.CENTER);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			welcomImageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.welcome_horizontal));
		} else {
			welcomImageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.welcome_vertical));
		}
		// ����ҹ��ģʽ����ʱ����
		// mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// String channel = new ApplicationProperties(WelcomeActivity.this).getChannel();
		// if ("true".equals(night_mode)) {
		// night(true);
		// } else {
		// day();
		// }

//		// ��ʼ�����ݿ�
//		try {
//			DatabaseService service = new DatabaseService(WelcomeActivity.this);
//		} catch (Exception e) {
//			Toast.makeText(getApplicationContext(), "��ȡ��Ŀ��Ϣʧ��", Toast.LENGTH_SHORT).show();
//			e.printStackTrace();
//		}
//
//		// ��ȡ�����ļ������Ӧ�ð汾
//		ApplicationProperties properties = new ApplicationProperties(WelcomeActivity.this);
//		String serverURL = properties.findBypropertyName("serverURL", WelcomeActivity.this);
//		String version = properties.findBypropertyName("version", WelcomeActivity.this);
//		boolean isNewVersion = new VersionControl().versionControl(serverURL + "getVersion", version);
//		if (isNewVersion == true) {
//
//		} else {
//			Toast.makeText(getApplicationContext(), "Ӧ�ð汾�и��£������ز����°�װ", Toast.LENGTH_SHORT).show();
//		}

		// �ȴ�ҳ�棬��ʱ����������
		ImageView welcomeView = (ImageView) findViewById(R.id.img_welcome);
		// AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
		// anim.setDuration(3000);
		// anim.setAnimationListener(new AnimationListener() {
		//
		// @Override
		// public void onAnimationStart(Animation arg0) {
		//
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animation arg0) {
		//
		// }
		//
		// @Override
		// public void onAnimationEnd(Animation arg0) {
		// Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
		// startActivity(intent);
		// WelcomeActivity.this.finish();
		// }
		// });
		// welcomeView.setAnimation(anim);
		// anim.start();
		welcomeView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
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

	// �л�����
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		ImageView welcomImageView = (ImageView) findViewById(R.id.img_welcome);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			welcomImageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.welcome_horizontal));
		} else {
			welcomImageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.welcome_vertical));
		}
	}

}
