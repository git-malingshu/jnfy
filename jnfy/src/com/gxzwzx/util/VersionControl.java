package com.gxzwzx.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class VersionControl {
	//Ӧ�ð汾���
	public boolean versionControl(String versionInterfaceURL, String version) {
		boolean isNewVersion = false;
		try {
			//ͨ��httpclient����������汾��
			HttpClient client = new DefaultHttpClient();
			HttpPost postMethod = new HttpPost(versionInterfaceURL);
			HttpResponse response = client.execute(postMethod); // ִ��POST����
			String context = EntityUtils.toString(response.getEntity(), "utf-8");
			JSONArray contextJsonArray = new JSONArray(context);
			JSONObject jObject = contextJsonArray.getJSONObject(0);
			String serverVersion = jObject.getString("version");
			if (serverVersion.equals(version)) {
				isNewVersion = true;
			} else {
				isNewVersion = false;
			}

			System.out.println(serverVersion);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isNewVersion;
	}

}
