package com.example.gongneng;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.people.PageActivity;
import com.example.people.R;

public class Alltext extends Activity{
	TextView title, time, alltext,back;
	String title1,time1,id2,allmessage;
	URL url = null;
	HttpURLConnection httpurlconnection = null;
	private String responseMsg = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alltext);
		
		Intent intent = getIntent();
		id2=intent.getExtras().getString("id2");
		title1=intent.getExtras().getString("title");
		time1=intent.getExtras().getString("time");
		
		
		title = (TextView)findViewById(R.id.title);
		time = (TextView)findViewById(R.id.time123);
		alltext = (TextView)findViewById(R.id.alltext);
		back = (TextView)findViewById(R.id.return001);
		
		// download the content
		Initalltext();
		
		title.setText(title1);
		time.setText(time1);
		alltext.setText(allmessage);
		
		Onclick();
	}

	void Onclick() {
		OnClickListener monclicklistener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.equals(back)) {
					Alltext.this.finish();
				}
			}
		};
		back.setOnClickListener(monclicklistener);
	}
	
	void Initalltext(){
		Thread downThread = new Thread(new downThread());
		downThread.start();
		try {
			downThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class downThread implements Runnable {
		@Override
		public void run() {
			try {
				// URLºÏ·¨
				boolean upValidate = upServerplocation(id2);
				Message msg = handler.obtainMessage();
				if (upValidate) {
					if (responseMsg.equals("success")) {
						msg.what = 0;
						handler.sendMessage(msg);
					} else {
						msg.what = 1;
						handler.sendMessage(msg);
					}
				} else {
					msg.what = 2;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				// timeout
				e.printStackTrace();
			}
		}
	}
	
	Handler handler = new Handler(); 
	
	private boolean upServerplocation(String id) {
		boolean loginValidate = false;
		// use  HTTP
		String urlStr = "http://"+PageActivity.urltest+":8080/service/servlet/alltextServlet";
		//	String urlStr = "http://219.245.65.5:8080/service/servlet/alltextServlet";
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			httpurlconnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		httpurlconnection.setDoOutput(true);
		try {
			httpurlconnection.setRequestMethod("POST");
			httpurlconnection.setConnectTimeout(5000);
			httpurlconnection.setReadTimeout(10000);
		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DataOutputStream dos;
		try {
			dos = new DataOutputStream(httpurlconnection.getOutputStream());
			dos.writeUTF(String.valueOf(id));
			dos.flush();
			dos.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			loginValidate = true;
			DataInputStream dis = new DataInputStream(
					httpurlconnection.getInputStream());

			responseMsg = dis.readUTF();
			allmessage = dis.readUTF();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginValidate;
	}
}
