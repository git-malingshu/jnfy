package com.gxzwzx.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.gxzwzx.activity.WelcomeActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseService {
	private final static int VERSION = 1;
	private final static String DBNAME = "channel";
	private DatabaseHelper dbHelper;
	private SQLiteDatabase sqliteDatabase;

	public DatabaseService(Context context) throws Exception {
		dbHelper = new DatabaseHelper(context, DBNAME, VERSION);
		dbHelper.getReadableDatabase();

		if (context.getClass().equals(WelcomeActivity.class)) {
			System.out.println("欢迎界面初始化数据库");
			if (isExist()) {// 若表中数据为空，则执行插入。
				try {
					initInsert();// 插入初始化数据
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			ApplicationProperties properties = new ApplicationProperties(context);
			String serverURL = properties.findBypropertyName("serverURL", context);
			isDBContextDiff(serverURL + "listChannel");
		}

	}

	// 判断数据库表中是否有数据
	public boolean isExist() {
		boolean result = true;
		String sql = "select count(*) count from channel";
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor cursor = sqliteDatabase.rawQuery(sql, null);
		if (cursor.moveToNext()) {
			int count = cursor.getInt(0);
			if (count > 0) {// 存在数据
				result = false;
			}
			cursor.close();
			sqliteDatabase.close();
		}
		return result;

	}

	// 初始化数据库
	public void initInsert() throws IOException {

		ContentValues values = new ContentValues();
		values.put("channelId", "1");
		values.put("channelName", "政府部门办事信息公开");
		values.put("url", "http://cs.gxi.gov.cn:8080/phone/deptList.jsp");
		values.put("flag", "1");
		values.put("channelParentId", "0");
		values.put("channelLevel", "1");

		List<ContentValues> data = new ArrayList<ContentValues>();
		data.add(values);
		for (int i = 0; i < data.size(); i++) {
			sqliteDatabase = dbHelper.getWritableDatabase();
			sqliteDatabase.insert("channel", null, data.get(i));
			sqliteDatabase.close();
		}

	}

	// 根据栏目名称查找URL
	public String findURLByName(String channelName) {
		sqliteDatabase = dbHelper.getWritableDatabase();
		String url = "";
		Cursor cursor = sqliteDatabase.query("channel", new String[] { "url" }, "channelName=?",
				new String[] { channelName }, null, null, null);
		while (cursor.moveToNext()) {
			url = cursor.getString(cursor.getColumnIndex("url"));
			cursor.close();
		}
		return url;

	}

	// 检查本地数据库是否和服务器栏目表一致，如果不一致则以服务器为准更新数据库
	public boolean isDBContextDiff(String DBInterfaceURL) throws Exception {
		boolean isDiff = false;
		HttpClient client = new DefaultHttpClient();
		HttpPost postMethod = new HttpPost(DBInterfaceURL);
		HttpResponse response = client.execute(postMethod); // 执行POST方法
		String context = EntityUtils.toString(response.getEntity(), "utf-8");
		Log.i(null, "resCode = " + response.getStatusLine().getStatusCode()); // 获取响应码
		JSONArray contextJsonArray = new JSONArray(context);
		for (int i = 0; i < contextJsonArray.length(); i++) {
			JSONObject jObject = contextJsonArray.getJSONObject(i);
			sqliteDatabase = dbHelper.getWritableDatabase();
			Cursor cursor = sqliteDatabase.query("channel", new String[] { "url" }, "channelName=? and url=?",
					new String[] { jObject.getString("channelName"), jObject.getString("url") }, null, null, null);
			if (cursor.moveToNext()) {
				//String url = cursor.getString(cursor.getColumnIndex("url"));
				isDiff = true;
				cursor.close();
			} else {
				isDiff = false;
				break;
			}
		}

		if (isDiff == true) {

		} else {
			sqliteDatabase = dbHelper.getWritableDatabase();
			sqliteDatabase.delete("channel", null, null);
			sqliteDatabase.execSQL("DELETE FROM sqlite_sequence WHERE name = 'channel'");
			for (int i = 0; i < contextJsonArray.length(); i++) {
				JSONObject jObject = contextJsonArray.getJSONObject(i);
				sqliteDatabase = dbHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put("channelId", jObject.getString("channelId"));
				values.put("channelName", jObject.getString("channelName"));
				values.put("url", jObject.getString("url"));
				values.put("flag", jObject.getString("flag"));
				values.put("channelParentId", jObject.getString("channelParentId"));
				values.put("channelLevel", jObject.getString("channelLevel"));
				sqliteDatabase.insert("channel", null, values);

			}
		}
		return isDiff;
	}

	// 根据栏目ID检查栏目是否存在子节点
	public boolean hasChild(String channelId) {
		boolean hasChild = false;
		try {
			sqliteDatabase = dbHelper.getWritableDatabase();
			Cursor cursor = sqliteDatabase.query("channel", new String[] { "url" }, "channelParentId=?",
					new String[] { channelId }, null, null, null);
			if (cursor.moveToNext()) {
				hasChild = true;
				cursor.close();
			} else {
				hasChild = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hasChild;
	}
}
