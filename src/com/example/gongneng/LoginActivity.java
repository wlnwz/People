package com.example.gongneng;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.people.PageActivity;
import com.example.people.MaindemoActivity;
import com.example.people.R;
import com.util.Encrypt;
import com.util.SysApplication;

public class LoginActivity extends Activity implements OnClickListener {
	private TextView registertextview, login_cancle, recode_textview;
	private TextView usernameerrorid, passworderrorid;
	private EditText etLoginName, etPassword;
	private ImageView login_name_clear_btn, clear_btn2;
	private CheckBox autologin,keepsecret;
	private Button BtnMenulogin;
	private String responseMsg = "", phone = "0";
	private static final int REQUEST_TIMEOUT = 5 * 1000;// 设置请求超时10秒钟
	private static final int SO_TIMEOUT = 10 * 1000; // 设置等待数据超时时间10秒钟
	public static SharedPreferences sp;

	int REQUEST_CODE = 0;
	public String name22, ID, username,password;
	URL url = null;
	HttpURLConnection httpurlconnection = null;
	private String check = "1";
	int progressbarMax,i=0,badrecord;

	@SuppressWarnings("deprecation")
	private final int[] MODES = new int[] { Activity.MODE_PRIVATE,// 默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容，如果想把新写入的内容追加到原文件中，可以使用Activity.MODE_APPEND
			Activity.MODE_WORLD_READABLE,// 表示当前文件可以被其他应用读取，
			Activity.MODE_WORLD_WRITEABLE,// 表示当前文件可以被其他应用写入；
											// 如果希望文件被其他应用读和写，可以传入:Activity.MODE_WORLD_READABLE+Activity.MODE_WORLD_WRITEABLE
			Activity.MODE_APPEND // 该模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		SysApplication.getInstance().addActivity(this);

		sp = getSharedPreferences("userdata", MODES[1]);

		InitView();

		// 判断记住密码多选框的状态
		if (sp.getBoolean("ISCHECK", false)) {
			// 设置默认是记录密码状态
			keepsecret.setChecked(true);
			etLoginName.setText(sp.getString("username", ""));
			etPassword.setText(sp.getString("password", ""));		
			 
			if(sp.getBoolean("AUTO_ISCHECK", false))
	       	  { 
	       		     //设置默认是自动登录状态
	       		     autologin.setChecked(true);
	       		     
	       		  username = etLoginName.getText().toString();
	  			  password = etPassword.getText().toString();
	       		    //跳转界面
	       		  Thread loginThread = new Thread(new LoginThread());
	  			  loginThread.start();
					
	       	  }
		}

		// 监听记住密码选项
		keepsecret.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
						if (keepsecret.isChecked()) {

							sp.edit().putBoolean("ISCHECK", true).commit();

							
						} else {
							
								sp.edit().putBoolean("ISCHECK", false).commit();

						}
					}
				});
		
		//监听自动登录多选框事件
		autologin.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (autologin.isChecked()) {
					System.out.println("自动登录已选中");
					sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

				} else {
					System.out.println("自动登录没有选中");
					sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == REQUEST_CODE || resultCode == RESULT_OK) {

			Bundle extras = data.getExtras();
			etLoginName.setText(extras.getString("username"));
			etPassword.setText(extras.getString("password"));

		}
	}

	private void InitView() {
		// TODO Auto-generated method stub
		registertextview = (TextView) findViewById(R.id.register_text);
		login_cancle = (TextView) findViewById(R.id.login_cancle);

		etLoginName = (EditText) findViewById(R.id.LoginName);
		etPassword = (EditText) findViewById(R.id.password);

		recode_textview = (TextView) findViewById(R.id.recode_textview);
		usernameerrorid = (TextView) findViewById(R.id.usernameerrorid);
		passworderrorid = (TextView) findViewById(R.id.passworderrorid);

		login_name_clear_btn = (ImageView) findViewById(R.id.login_name_clear_btn);
		clear_btn2 = (ImageView) findViewById(R.id.clear_btn2);
	
		autologin = (CheckBox) findViewById(R.id.autologin);
		keepsecret = (CheckBox) findViewById(R.id.keepsecret);

		BtnMenulogin = (Button) findViewById(R.id.BtnMenulogin);
		
		registertextview.setOnClickListener(this);
		login_cancle.setOnClickListener(this);
		etLoginName.setOnClickListener(this);
		etPassword.setOnClickListener(this);
		login_name_clear_btn.setOnClickListener(this);
		clear_btn2.setOnClickListener(this);
		recode_textview.setOnClickListener(this);
		BtnMenulogin.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.register_text:

			Intent intent = new Intent(LoginActivity.this, Register.class);
			intent.putExtra("username", etLoginName.getText().toString());
			intent.putExtra("password", etPassword.getText().toString());
			startActivityForResult(intent, REQUEST_CODE);

			break;
		case R.id.login_cancle:	
			
			this.finish();
			break;

		case R.id.LoginName:
			usernameerrorid.setVisibility(View.GONE);
			login_name_clear_btn.setVisibility(View.VISIBLE);
			clear_btn2.setVisibility(View.GONE);
			break;
		case R.id.password:
			passworderrorid.setVisibility(View.GONE);
			clear_btn2.setVisibility(View.VISIBLE);
			login_name_clear_btn.setVisibility(View.GONE);
			break;
		case R.id.login_name_clear_btn:
			etLoginName.setText("");
			passworderrorid.setVisibility(View.GONE);
			passworderrorid.setVisibility(View.GONE);
			break;
		case R.id.clear_btn2:
			passworderrorid.setVisibility(View.GONE);
			passworderrorid.setVisibility(View.GONE);
			etPassword.setText("");
			break;

		case R.id.recode_textview:

			Toast.makeText(this, "未处理", 1).show();
			break;
		case R.id.BtnMenulogin:

			username = etLoginName.getText().toString();
			password = etPassword.getText().toString();
			if (keepsecret.isChecked()) {
				// 记住用户名、密码、
				Editor editor = sp.edit();
				editor.putString("username", username);
				editor.putString("password", password);
				editor.commit();
			}
			Loginbtn();
			break;

		default:
			break;
		}
	}

	public void Loginbtn() {

		if (etLoginName.getText().toString().trim().equals("")
				|| etLoginName.getText().toString().trim().length() > 20
				|| etLoginName.getText().toString().trim().length() < 1) {
			usernameerrorid.setVisibility(View.VISIBLE);
			usernameerrorid.setText("用户名错误");
		} else if (etPassword.getText().toString().trim().equals("")
				|| etPassword.getText().toString().trim().length() > 16
				|| etPassword.getText().toString().trim().length() < 6) {
			passworderrorid.setVisibility(View.VISIBLE);
			passworderrorid.setText("密码错误");
		} else {

			Thread loginThread = new Thread(new LoginThread());
			loginThread.start();
			try {
				loginThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// LoginThread线程类
	class LoginThread implements Runnable {

		@Override
		public void run() {
			
			boolean checkstatus = sp.getBoolean("ISCHECK1", false);
			if (checkstatus) {
				// 获取已经存在的用户名和密码
				String realUsername = sp.getString("username", "");
				String realPassword = sp.getString("password", "");
				if ((!realUsername.equals("")) && !(realUsername == null)
						|| (!realPassword.equals(""))
						|| !(realPassword == null)) {
					if (username.equals(realUsername)
							&& password.equals(realPassword)) {
						username = etLoginName.getText().toString();
						password = etPassword.getText().toString();
					}
				}
			} else {
				password = Encrypt.md5(password);
			}

			// URL合法，但是这一步并不验证密码是否正确
			boolean loginValidate = loginServer(username, password);
			Message msg = handler.obtainMessage();
			if (loginValidate) {	  
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
		}

	}

	// Handler
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				Toast.makeText(getApplicationContext(), "登录成功！",
						Toast.LENGTH_SHORT).show();
				String bundle = new String();
				bundle = etLoginName.getText().toString();
				System.out.println(bundle + "11111111");

				Intent intent = new Intent(LoginActivity.this, MaindemoActivity.class);

				intent.putExtra("NAME", bundle);
				intent.putExtra("phone", phone);
				intent.putExtra("ID", ID);
				intent.putExtra("badrecord", badrecord);
				// 返回intent
				setResult(100, intent);

				LoginActivity.this.finish();

				break;
			case 1:
				Toast.makeText(getApplicationContext(), "输入的密码错误",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				// loadingDialog.cancel();
				Toast.makeText(getApplicationContext(), "服务器连接失败",
						Toast.LENGTH_SHORT).show();
				
				break;
			}

		}
	};

	private boolean loginServer(String username, String password) {
		boolean loginValidate = false;
		// 使用apache HTTP客户端实现
		  String urlStr ="http://"+PageActivity.urltest+":8080/service/servlet/LoginServlet";
		  //   	String urlStr = "http://192.168.253.1:8080/service/servlet/LoginServlet";
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
	
		try {
			httpurlconnection.setRequestMethod("POST");
			httpurlconnection.setConnectTimeout(5000);
			httpurlconnection.setReadTimeout(10000);
//			System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
//			System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		httpurlconnection.setDoOutput(true);
		DataOutputStream dos;
		// 添加用户名和密码
		try {			
			dos = new DataOutputStream(httpurlconnection.getOutputStream());
			dos.writeUTF(String.valueOf(username));
			dos.writeUTF(String.valueOf(password));
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
			phone = dis.readUTF();
			ID = dis.readUTF();
			badrecord = dis.readInt();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginValidate;
	}

	// // 初始化HttpClient，并设置超时
	public HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}
}
