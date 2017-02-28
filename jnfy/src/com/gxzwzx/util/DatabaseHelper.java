package com.gxzwzx.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final int version = 1;

	public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public DatabaseHelper(Context context, String name) {
		this(context, name, version);
	}

	public DatabaseHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建数据库表
		System.out.println("Create a database");
		String sql = "CREATE TABLE IF NOT EXISTS channel (id integer primary key autoincrement,channelId varchar(100), channelName varchar(128), url varchar(128),flag varchar(32),channelParentId varchar(100),channelLevel varchar(32))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 更新数据 库
		System.out.println("Upgrade Table done!");
	}

}
