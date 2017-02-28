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
		if (file.exists()) { // �ж��ļ��Ƿ����
			if (file.isFile()) { // �ж��Ƿ����ļ�
				file.delete(); // delete()���� ��Ӧ��֪�� ��ɾ������˼;
			} else if (file.isDirectory()) { // �����������һ��Ŀ¼
				File files[] = file.listFiles(); // ����Ŀ¼�����е��ļ� files[];
				for (int i = 0; i < files.length; i++) { // ����Ŀ¼�����е��ļ�
					this.deleteFile(files[i]); // ��ÿ���ļ� ������������е���
				}
			}
			file.delete();
		} else {
		}

	}

	// ��ȡ�����ļ�����
	public void readFile(String fileName, Context context) {
		try {
			InputStream is = context.openFileInput(fileName);
			Properties properties = new Properties();
			properties.load(is);
			System.out.println("Ӧ�������ļ���ȡ�ɹ�");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �������ļ����������
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

	// ����properties���ƻ�ȡֵ
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