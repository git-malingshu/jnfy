package com.gxzwzx.activity;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gxzwzx.R;


/** 
 * @author MaRenBen
 * @version ����ʱ�䣺2015-6-2 ����10:47:50 
 * (��˵��) 
 */

public class CaptureActivity extends Activity {
	private final static int SCANNIN_GREQUEST_CODE = 1;

	/**
	 * ��ʾɨ����
	 */
	private TextView mTextView ;
	
	/*
	 * ��ʾ�����ʾ
	 */
	private TextView tips;
	
	/**
	 * ��ʾɨ���ĵ�ͼƬ
	 */
	private ImageView mImageView;
	
	private WebView web;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
				.detectNetwork().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
				.detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		
		setContentView(R.layout.activity_capture);
		String result = getIntent().getStringExtra("result");
//		mTextView = (TextView) findViewById(R.id.result); 
//		tips = (TextView) findViewById(R.id.tips);
////		mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);
		String[] temp = result.split("ʶ���룺");
		String code = "";
		if (temp.length >0 ){
			code = temp[1].toString();
		}
		
		
		// ���յ��ͼ�괫�ݵĶ�Ӧ��url
		//String url = getIntent().getStringExtra("url");
		String url = this.getString(R.string.serverURL);
		// ҳ�����ӵ�ָ����url
		web = (WebView) findViewById(R.id.web_scan);
		if (url == null) {
			url = "http://jn.gxcourt.gov.cn/info/1021/5393.htm";
		}
		url = url+"?id="+code;
		// ����webview�ؼ�
		web.getSettings().setJavaScriptEnabled(true);// ����JS
		web.setScrollBarStyle(0);// ���������Ϊ0���ǲ������������ռ䣬��������������ҳ��
		
		// ���ÿ���֧������ 
		web.getSettings().setSupportZoom(true); 
		// ���ó������Ź��� 
		web.getSettings().setBuiltInZoomControls(true);
		//�������������
		web.getSettings().setUseWideViewPort(true);
		//����Ӧ��Ļ
		web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		web.getSettings().setLoadWithOverviewMode(true);
		
		web.loadUrl(url);
		
		Button btnContinue = (Button)findViewById(R.id.btn_continue);
		btnContinue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(CaptureActivity.this, ScanActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
				CaptureActivity.this.finish();
			}
		});
		
//		String httpUrl = this.getString(R.string.serverURL);;
//		HttpPost httpRequest = new HttpPost(httpUrl);
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("id", code));
//		HttpEntity httpentity = null;
//		try {
//			httpentity = new UrlEncodedFormEntity(params, "utf8");
//			
//			httpRequest.setEntity(httpentity);
//			HttpClient httpclient = new DefaultHttpClient();
//			HttpResponse httpResponse = null;
//			
//			// ����ʱ
//			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
//            // ��ȡ��ʱ
//			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000    );
//			
//			httpResponse = httpclient.execute(httpRequest);
//			String strResult = null;
//			if (httpResponse.getStatusLine().getStatusCode() == 200) {
//				
//				try {
//					strResult = EntityUtils.toString(httpResponse.getEntity());
//					JSONObject msg = new JSONObject(strResult);
//					if ("\"done\"".equals(msg.getString("appmsg"))) {
//					 	//tips.setText(result+"  �ύ�ɹ�");
//						mTextView.setText(result+"  �ύ�ɹ�");
//						Toast.makeText(CaptureActivity.this, "��ϲ����ά���ύ�ɹ�", Toast.LENGTH_SHORT).show();
//					} else {
//						//tips.setText(result+"  �ύʧ��");
//						mTextView.setText(result+"  �ύʧ��");
//						Toast.makeText(CaptureActivity.this, "��Ǹ����ά���ύʧ��", Toast.LENGTH_SHORT).show();
//					}
//				} catch (ParseException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//				//Toast.makeText(CaptureActivity.this, strResult, Toast.LENGTH_SHORT).show();
////				Intent intent = new Intent(CaptureActivity.this, ScanActivity.class);
////				startActivity(intent);
////				CaptureActivity.this.finish();
//			} else {
//				//Toast.makeText(CaptureActivity.this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
//				//tips.setText(result+"  ����ʧ��");
//				mTextView.setText(result+"  ����ʧ��");
//				Toast.makeText(CaptureActivity.this, "��Ǹ����ά������ʧ��", Toast.LENGTH_SHORT).show();
//			}
//			
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//			Toast.makeText(CaptureActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//		} catch (ConnectTimeoutException e) {
//			Toast.makeText(CaptureActivity.this, "���ӷ�������ʱ!", Toast.LENGTH_SHORT).show();
//		} catch (IOException e) {
//			e.printStackTrace();
//			Toast.makeText(CaptureActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				// ��ʾɨ�赽������
				mTextView.setText(bundle.getString("result"));
				// ��ʾ
				//mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
			}
			break;
		}
	}	
}
