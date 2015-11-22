package com.example.gongneng;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.example.people.PageActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

public class showpollocService extends Service 
{
	private String username,userid;

	private String[] policename, plocation, ptelphone;
	URL url = null;
	HttpURLConnection httpurlconnection = null;
	private String responseMsg = "";
	private int aa;
	private final String BROADCAST_ACTION = "com.example.broadcast.police";
	private final String BROADCAST_ACTION2 = "com.example.broadcast.showornot";
	private boolean czjc;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * ÿ�ε���startService����ִ��onStart
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(final Intent intent, int startId) 
	{

		username = intent.getStringExtra("username");
		userid = intent.getStringExtra("userid");
		czjc = intent.getBooleanExtra("startornot", false);
		System.out.println("username==" + username+userid);
 
		plocation = new String[50];
		ptelphone = new String[25];
		policename = new String[25];

		if (czjc == true)
		{
			mHandler.post(runnable);
		} 
		else
		{
			Intent intent3 = new Intent();
			intent3.putExtra("showornot", false);
			intent3.setAction(BROADCAST_ACTION2);
			sendBroadcast(intent3);
			
			mHandler.removeCallbacks(runnable);
		}
		super.onStart(intent, startId);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) 
		{
			System.out.println("handler");
			switch (msg.what) {
			case 0:
				Intent intent2 = new Intent();
				intent2.putExtra("aa", aa);
				intent2.putExtra("policename", policename);
				intent2.putExtra("policephone", ptelphone);
				intent2.putExtra("plocation", plocation);
				intent2.setAction(BROADCAST_ACTION);
				sendBroadcast(intent2);

		//		Toast.makeText(showpollocService.this, "���ͳɹ�", 1).show();
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "����ʧ��",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				// registerDialog.cancel();
				Toast.makeText(getApplicationContext(), "����������ʧ�ܣ�",
						Toast.LENGTH_SHORT).show();
				break;
			}

		}
	};

	private boolean upServerplocation(String username,String userid) {
		boolean loginValidate = false;
		// ʹ��apache HTTP�ͻ���ʵ��
	//	String urlStr = "http://219.245.67.1:8080/service/servlet/downpollocServlet";
		String urlStr = "http://"+PageActivity.urltest+":8080/service/servlet/downpollocServlet";
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
		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DataOutputStream dos;
		// �ϴ��û���
		try {
			dos = new DataOutputStream(httpurlconnection.getOutputStream());
			dos.writeUTF(String.valueOf(username));
			dos.writeUTF(String.valueOf(userid));
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

			// ��ȡ����״̬�Լ���������
			responseMsg = dis.readUTF();
			aa = dis.readInt();
			System.out.println("aa" + aa);

			// ѭ����ȡ����λ�ñ��е�������Ϣ�����������š��绰��λ����Ϣ��
			for (int i = 0; i < (aa) / 2; i++) {

				policename[i] = dis.readUTF();
				ptelphone[i] = dis.readUTF();
				plocation[2 * i] = dis.readUTF();
				plocation[2 * i + 1] = dis.readUTF();

				System.out.println("policename" + policename + " "
						+ plocation[2 * i]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginValidate;
	}

	Runnable runnable = new Runnable() 
	{
		@Override
		public void run() {
            load();
       //     Toast.makeText(showpollocService.this, "ˢ���߳�", 1).show();
			mHandler.postDelayed(runnable, 10000);

		}
	};

	private Handler mHandler = new Handler();

	void load()
	{
		Thread downThread = new Thread(new DownpoliceThread());
		downThread.start();
	}

	class DownpoliceThread implements Runnable {
		@Override
		public void run() {

			try {
				// URL�Ϸ���������һ��������֤�����Ƿ���ȷ
				boolean upValidate = upServerplocation(username,userid);
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
				// ������ʱ,����ֵ������null��������Ϣ
				e.printStackTrace();
			}
		}
	}

	public void finish() {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(runnable);
	}
}
