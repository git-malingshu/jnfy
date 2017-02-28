package com.gxzwzx.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.provider.SyncStateContract.Constants;

public class ApplicationProperties {

	public ApplicationProperties(Context context) {
		// if (context.getClass().equals(WelcomeActivity.class)) {
		try {
			InputStream is = context.openFileInput("application.properties");
			Properties properties = new Properties();
			properties.load(is);
			deleteFile(new File("application.properties"));
			writeFile("application.properties", context);
		} catch (Exception e) {
			writeFile("application.properties", context);
			e.printStackTrace();
		}
		// } else {
		//
		// }
	}

	public void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		} else {
		}

	}

	// 读取配置文件内容
	public void readFile(String fileName, Context context) {
		try {
			InputStream is = context.openFileInput(fileName);
			Properties properties = new Properties();
			properties.load(is);
			System.out.println("应用配置文件读取成功");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 往配置文件里填充内容
	public void writeFile(String fileName, Context context) {
		try {
			Properties properties = new Properties();
			properties.setProperty("version", "1.0");
			properties.setProperty("serverURL", "http://zwzx.gxzf.gov.cn/app/channel.do?method=");

			FileOutputStream fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
			properties.store(fos, "");
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 根据properties名称获取值
	public String findBypropertyName(String propertyName, Context context) {
		String value = "";
		try {
			InputStream is = context.openFileInput("application.properties");
			Properties properties = new Properties();
			properties.load(is);
			value = properties.getProperty(propertyName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}

}