package com.gxzwzx.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gxzwzx.R;
import com.gxzwzx.util.DatabaseService;

public class MainActivity extends Activity {
	private final int REQUESTCODE = 1;
	private GridView mGridView;

	// 栏目图标数组，对应栏目顺序
	private int[] imageRes = { R.drawable.sj001, R.drawable.sj002, R.drawable.sj003, R.drawable.sj004,
			R.drawable.sj005, R.drawable.sj006, R.drawable.sj007, R.drawable.sj_bmsxcx, R.drawable.ysqgk,
			R.drawable.yhzc,R.drawable.sj005 };
//	private String[] channelNameArr = { "信用动态","政策法规","联合奖惩","双公示","信用知识","信用研究","信用搜索" };
//	private String[] urlArr = {"http://kelsoft.cn:18646/mobile/xydt/index.jhtml","http://kelsoft.cn:18646/mobile/zcfg/index.jhtml","http://kelsoft.cn:18646/mobile/lhcj/index.jhtml","http://kelsoft.cn:18646/mobile/sgs/index.jhtml","http://kelsoft.cn:18646/mobile/xyzs/index.jhtml","http://kelsoft.cn:18646/mobile/xyyj/index.jhtml","http://kelsoft.cn:18646/msearch.jspx"};
	
	private String[] channelNameArr = { "法院简介","新闻中心","审务公开","执行信息查询","诉讼指南","立案资料自助录入","开庭公告","庭审直播","判决文书公开","官方微信","防伪识别" };
	private String[] urlArr = {"http://jn.gxcourt.gov.cn/info/1021/5393.htm","http://jn.gxcourt.gov.cn/xwzx.htm","http://jn.gxcourt.gov.cn/swgk.htm","http://171.106.48.55:18897/wsfy-ww/pub/ajxx/cxm.htm?fy=2750","http://ygjn.gxcourt.gov.cn/index/sszn.htm","","http://171.106.48.55:8899/legalsystem/web/ktgg.jsp?webid=1032256544","","http://nnjnfy.chinacourt.org/public/index.php?LocationID=0800000000","","http://www.baidu.com"};

	// 夜间模式参数
	private WindowManager mWindowManager;
	public TextView textView = null;

//	private DatabaseHelper dbHelper;
//	private SQLiteDatabase sqliteDatabase;

	// 栏目列表下拉参数
	// private ImageButton btn_scroll;
	// private int scrollRange = 50;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_content);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			mainLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_horizontal));
		} else {
			mainLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_vertical));
		}

		// mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// String night_mode = new ApplicationProperties(this).getNight_mode();
		// if ("true".equals(night_mode)) {
		// night(true);
		// } else {
		// day();
		// }

		// 从数据库获取数据，动态创建一级栏目界面
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		List<ContentValues> channel = new ArrayList<ContentValues>();
//		dbHelper = new DatabaseHelper(MainActivity.this, "channel");
//		sqliteDatabase = dbHelper.getWritableDatabase();
//		Cursor cursor = sqliteDatabase.query("channel", new String[] { "channelName", "channelId", "url" },
//				"channelLevel=?", new String[] { "1" }, null, null, "channelId");
//		while (cursor.moveToNext()) {
//			ContentValues channelInfo = new ContentValues();
//			String channelId = cursor.getString(cursor.getColumnIndex("channelId"));
//			String channelName = cursor.getString(cursor.getColumnIndex("channelName"));
//			String url = cursor.getString(cursor.getColumnIndex("url"));
//			channelInfo.put("channelId", channelId);
//			channelInfo.put("channelName", channelName);
//			channelInfo.put("url", url);
//			channel.add(channelInfo);
//		}
		for (int i = 0; i < 11; i++) {
			ContentValues channelInfo = new ContentValues();
			String channelId = "channelId"+i;
			String channelName = channelNameArr[i];
			String url = urlArr[i];
			channelInfo.put("channelId", channelId);
			channelInfo.put("channelName", channelName);
			channelInfo.put("url", url);
			channel.add(channelInfo);
		}
		//cursor.close();
		for (int i = 0; i < channel.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImageView", imageRes[i]);
			map.put("ItemTextView", channel.get(i).getAsString("channelName"));
			map.put("channelId", channel.get(i).getAsString("channelId"));
			map.put("url", channel.get(i).getAsString("url"));
			data.add(map);
		}
		mGridView = (GridView) findViewById(R.id.main_grid);
		// 为itme.xml添加适配器
		SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this, data, R.layout.grid_item, new String[] {
				"ItemImageView", "ItemTextView", "channelId", "url" }, new int[] { R.id.ItemImageView,
				R.id.ItemTextView, R.id.channelId, R.id.url });
		mGridView.setAdapter(simpleAdapter);
		// 为mGridView添加点击事件监听器
		mGridView.setOnItemClickListener(new GridViewItemOnClick());

		// 栏目列表下拉设置，暂时不用
		// btn_scroll = (ImageButton) findViewById(R.id.scroll);
		// btn_scroll.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// mGridView.scrollTo(0, scrollRange);
		// }
		// });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// new MenuInflater(this).inflate(R.menu.new_menu, menu);
		// return super.onCreateOptionsMenu(menu);

		menu.add(0, 0, 0, "abc");
		menu.add(0, 1, 1, "def");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.r000_delete_restaurant).setVisible(false);
		menu.findItem(R.id.r000_update_restaurant).setVisible(false);
		return super.onPrepareOptionsMenu(menu);
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

	// 定义点击事件监听器
	public class GridViewItemOnClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
			String channelId = ((TextView) view.findViewById(R.id.channelId)).getText().toString();
			String url = ((TextView) view.findViewById(R.id.url)).getText().toString();
			// Toast.makeText(getApplicationContext(), position + "", Toast.LENGTH_SHORT).show();
			Intent in = new Intent();
			DatabaseService service = null;
			try {
				service = new DatabaseService(MainActivity.this);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), position + "手机网络或服务器出现异常", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
//			if (service.hasChild(channelId)) {
//				in = new Intent(MainActivity.this, SecondLevelActivity.class);
//				in.putExtra("channelId", channelId);
//				startActivityForResult(in, REQUESTCODE);
			if (channelId.equals("channelId10")){
				Intent intent = new Intent(MainActivity.this, ScanActivity.class);
				//MainActivity.this.finish();
				startActivity(intent);
			} else if (channelId.equals("channelId9")) {
				Intent intent = new Intent(MainActivity.this, ShowImgActivity.class);
				//MainActivity.this.finish();
				startActivity(intent);
			} else {
				in = new Intent(MainActivity.this, WebActivity.class);
				// in.putExtra("url", new DatabaseService(MainActivity.this).findURLByName(channelName));
				in.putExtra("url", url);
				startActivityForResult(in, REQUESTCODE);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Builder builder = new Builder(this);
			builder.setMessage("确定退出吗？");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					System.exit(0);
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			builder.show();
		}

		return true;
	}

	// 切换背景
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_content);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

			mainLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_horizontal));
		} else {
			mainLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_vertical));
		}
	}

}
