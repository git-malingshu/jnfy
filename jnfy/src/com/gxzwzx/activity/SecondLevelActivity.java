package com.gxzwzx.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gxzwzx.R;
import com.gxzwzx.util.DatabaseHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class SecondLevelActivity extends Activity {
	private final int REQUESTCODE = 1;
	private GridView mGridView;

	// ����ͼ������
	private int[] imageBSCX = { R.drawable.sj_gr, R.drawable.sj_qy, R.drawable.sj_bm };
	private int[] imageZTFW = { R.drawable.sj_zjbl, R.drawable.sj_zzrd, R.drawable.sj_qykb, R.drawable.sj_jyns };
	private int[] imageYHZC = { R.drawable.grzc, R.drawable.qyzc, R.drawable.sj005 };
	// �����������
	// private String[] itemName = { "������", "����ҵ", "������" };

	// ҹ��ģʽ����
	private WindowManager mWindowManager;
	public TextView textView = null;

	private ImageButton btnReturn;

	private DatabaseHelper dbHelper;
	private SQLiteDatabase sqliteDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_second_level);
		LinearLayout contentLayout = (LinearLayout) findViewById(R.id.second_content);
		if (getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE) {
			contentLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_horizontal));
		} else {
			contentLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_vertical));
		}

		// mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// String night_mode = new ApplicationProperties(this).getNight_mode();
		// if ("true".equals(night_mode)) {
		// night(true);
		// } else {
		// day();
		// }

		// ����һ����Ŀҳ���ݵ�channelId��������̬��ȡ������������Ŀ�������Ŀ
		mGridView = (GridView) findViewById(R.id.second_level_grid);
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		List<ContentValues> channel = new ArrayList<ContentValues>();
		dbHelper = new DatabaseHelper(SecondLevelActivity.this, "channel");
		sqliteDatabase = dbHelper.getWritableDatabase();
		Intent intent = getIntent();
		String channelParentId = intent.getStringExtra("channelId");
		Cursor cursor = sqliteDatabase.query("channel", new String[] { "channelName", "channelId", "url" },
				"channelLevel=? and channelParentId=?", new String[] { "2", channelParentId }, null, null, "channelId");
		while (cursor.moveToNext()) {
			ContentValues channelInfo = new ContentValues();
			String channelId = cursor.getString(cursor.getColumnIndex("channelId"));
			String channelName = cursor.getString(cursor.getColumnIndex("channelName"));
			String url = cursor.getString(cursor.getColumnIndex("url"));
			channelInfo.put("channelId", channelId);
			channelInfo.put("channelName", channelName);
			channelInfo.put("url", url);
			channel.add(channelInfo);
		}
		cursor.close();
		for (int i = 0; i < channel.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			// ��ͬ��һ����Ŀ�µ�����Ŀʹ�ò�ͬ��ͼ��
			if (channelParentId.equals("1002") || channelParentId.equals("1006")) {
				// ����ָ��
				map.put("ItemImageView", imageBSCX[i]);
			} else if (channelParentId.equals("1003")) {
				// ר�����
				map.put("ItemImageView", imageZTFW[i]);
			} else if (channelParentId.equals("1010")) {
				// �û�ע��
				map.put("ItemImageView", imageYHZC[i]);
			}
			map.put("ItemTextView", channel.get(i).getAsString("channelName"));
			map.put("channelId", channel.get(i).getAsString("channelId"));
			map.put("url", channel.get(i).getAsString("url"));
			data.add(map);
		}
		// Ϊitme.xml���������
		SimpleAdapter simpleAdapter = new SimpleAdapter(SecondLevelActivity.this, data, R.layout.grid_item,
				new String[] { "ItemImageView", "ItemTextView", "channelId", "url" }, new int[] { R.id.ItemImageView,
						R.id.ItemTextView, R.id.channelId, R.id.url });
		mGridView.setAdapter(simpleAdapter);
		// ΪmGridView��ӵ���¼�������
		mGridView.setOnItemClickListener(new GridViewItemOnClick());

		// ���÷��ذ�ť����¼�
		btnReturn = (ImageButton) findViewById(R.id.second_evel_return);
		btnReturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});
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

	// �������¼�������
	public class GridViewItemOnClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
			String url = ((TextView) view.findViewById(R.id.url)).getText().toString();
			Intent in = new Intent();
			in = new Intent(SecondLevelActivity.this, WebActivity.class);
			in.putExtra("url", url);
			startActivityForResult(in, REQUESTCODE);
		}
	}

	// �л�����
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		LinearLayout contentLayout = (LinearLayout) findViewById(R.id.second_content);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			contentLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_horizontal));
		} else {
			contentLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_vertical));
		}
	}
}
