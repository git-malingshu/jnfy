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
			System.out.println("��ӭ�����ʼ�����ݿ�");
			if (isExist()) {// ����������Ϊ�գ���ִ�в��롣
				try {
					initInsert();// �����ʼ������
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			ApplicationProperties properties = new ApplicationProperties(context);
			String serverURL = properties.findBypropertyName("serverURL", context);
			isDBContextDiff(serverURL + "listChannel");
		}

	}

	// �ж����ݿ�����Ƿ�������
	public boolean isExist() {
		boolean result = true;
		String sql = "select count(*) count from channel";
		sqliteDatabase = dbHelper.getReadableDatabase();
		Cursor cursor = sqliteDatabase.rawQuery(sql, null);
		if (cursor.moveToNext()) {
			int count = cursor.getInt(0);
			if (count > 0) {// ��������
				result = false;
			}
			cursor.close();
			sqliteDatabase.close();
		}
		return result;

	}

	// ��ʼ�����ݿ�
	public void initInsert() throws IOException {

		ContentValues values = new ContentValues();
		values.put("channelId", "1");
		values.put("channelName", "�������Ű�����Ϣ����");
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

	// ������Ŀ���Ʋ���URL
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

	// ��鱾�����ݿ��Ƿ�ͷ�������Ŀ��һ�£������һ�����Է�����Ϊ׼�������ݿ�
	public boolean isDBContextDiff(String DBInterfaceURL) throws Exception {
		boolean isDiff = false;
		HttpClient client = new DefaultHttpClient();
		HttpPost postMethod = new HttpPost(DBInterfaceURL);
		HttpResponse response = client.execute(postMethod); // ִ��POST����
		String context = EntityUtils.toString(response.getEntity(), "utf-8");
		Log.i(null, "resCode = " + response.getStatusLine().getStatusCode()); // ��ȡ��Ӧ��
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

	// ������ĿID�����Ŀ�Ƿ�����ӽڵ�
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
