package com.example.gongneng;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.people.MaindemoActivity;
import com.example.people.R;
import com.util.Encrypt;
import com.util.SysApplication;

public class SetActivity extends Activity implements OnClickListener{
	private TextView sureview, login_cancle;
	private TextView username_error, lastpassword_error, newpassword_error,newphone_error;
	private EditText LoginnewName, newPassword,lastPassword,newtelphone;
	private ImageView login_name_clear_btn, clear_btn2,clear_btn3,phone_clean;
	private String username,telphone,newusername,newpassword,lastpassword,newphone,responseMsg = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setactivity);
		SysApplication.getInstance().addActivity(this);
    	Intent intent = getIntent();
	      username=intent.getExtras().getString("username");
	      telphone=intent.getExtras().getString("telphone");
		InitView();
	}
	
	private void InitView() {
		// TODO Auto-generated method stub
		sureview = (TextView) findViewById(R.id.sure_text);
		login_cancle = (TextView) findViewById(R.id.login_cancle);

		LoginnewName = (EditText) findViewById(R.id.LoginnewName);
		LoginnewName.setText(username);
		newPassword = (EditText) findViewById(R.id.newpassword);
		lastPassword = (EditText) findViewById(R.id.lastpassword);
		newtelphone = (EditText) findViewById(R.id.newtelphone);
		newtelphone.setText(telphone);
		
		newpassword_error = (TextView) findViewById(R.id.passworderrorid);
		username_error = (TextView) findViewById(R.id.usernameerrorid);
		lastpassword_error = (TextView) findViewById(R.id.lastpassworderrorid);
		newphone_error = (TextView) findViewById(R.id.phoneerrorid);

		login_name_clear_btn = (ImageView) findViewById(R.id.login_name_clear_btn);
		clear_btn2 = (ImageView) findViewById(R.id.newpassword_clear_btn);
		clear_btn3 = (ImageView) findViewById(R.id.lastpassword_clear_btn);
		phone_clean = (ImageView) findViewById(R.id.phone_clear_btn);

		
		sureview.setOnClickListener(this);
		login_cancle.setOnClickListener(this);
		LoginnewName.setOnClickListener(this);
		newPassword.setOnClickListener(this);
		lastPassword.setOnClickListener(this);
		newtelphone.setOnClickListener(this);
		login_name_clear_btn.setOnClickListener(this);
		clear_btn2.setOnClickListener(this);
		clear_btn3.setOnClickListener(this);
		phone_clean.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sure_text:

			newusername = LoginnewName.getText().toString();			
			lastpassword= lastPassword.getText().toString();
			newphone= newtelphone.getText().toString();			
			newpassword = newPassword.getText().toString();
				
			if( newpassword.length()<6){
					newpassword =lastpassword;
				}
			System.out.println("newpassword"+newpassword);
			newLoginbtn();

			break;
		case R.id.login_cancle:	
			
			this.finish();
			break;

		case R.id.LoginnewName:
			username_error.setVisibility(View.GONE);
			login_name_clear_btn.setVisibility(View.VISIBLE);
			clear_btn2.setVisibility(View.GONE);
			newphone_error.setVisibility(View.GONE);
			clear_btn3.setVisibility(View.GONE);
			break;
		case R.id.newpassword:
			username_error.setVisibility(View.GONE);
			clear_btn2.setVisibility(View.VISIBLE);
			login_name_clear_btn.setVisibility(View.GONE);
			newphone_error.setVisibility(View.GONE);
			clear_btn3.setVisibility(View.GONE);
			break;
		case R.id.lastpassword:
			username_error.setVisibility(View.GONE);
			clear_btn3.setVisibility(View.VISIBLE);
			login_name_clear_btn.setVisibility(View.GONE);
			newphone_error.setVisibility(View.GONE);
			clear_btn2.setVisibility(View.GONE);
			break;
		case R.id.login_name_clear_btn:
			LoginnewName.setText("");
			lastpassword_error.setVisibility(View.GONE);
			newpassword_error.setVisibility(View.GONE);
			break;
		case R.id.newpassword_clear_btn:
			lastpassword_error.setVisibility(View.GONE);
			newpassword_error.setVisibility(View.GONE);
			newPassword.setText("");
			break;

		case R.id.lastpassword_clear_btn:
			lastpassword_error.setVisibility(View.GONE);
			newpassword_error.setVisibility(View.GONE);
			lastPassword.setText("");
			break;


		default:
			break;
		}
	}
	
	void newLoginbtn(){
		if (LoginnewName.getText().toString().trim().equals("")
				|| LoginnewName.getText().toString().trim().length() > 20
				|| LoginnewName.getText().toString().trim().length() < 1) {
			username_error.setVisibility(View.VISIBLE);
			username_error.setText("用户名错误");
		} else if (lastPassword.getText().toString().trim().equals("")
				|| lastPassword.getText().toString().trim().length() > 16
				|| lastPassword.getText().toString().trim().length() < 6) {
			lastpassword_error.setVisibility(View.VISIBLE);
			lastpassword_error.setText("密码错误");
		} else {
			Thread loginThread = new Thread(new ReLoginThread());
			loginThread.start();
			try {
				loginThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// ReLoginThread线程类
	class ReLoginThread implements Runnable {

		@Override
		public void run() {
			
			newpassword = Encrypt.md5(newpassword);
			lastpassword = Encrypt.md5(lastpassword);


			// URL合法，但是这一步并不验证密码是否正确
			boolean loginValidate = loginServer(username,newusername,newphone, newpassword,lastpassword );
			Message msg = handler.obtainMessage();
			if (loginValidate) {
	//			showbar.setVisibility(View.GONE);		  
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
				Toast.makeText(getApplicationContext(), "修改成功！",
						Toast.LENGTH_SHORT).show();
				String bundle,newtephone = new String();
				bundle = LoginnewName.getText().toString();
				
				newtephone = newtelphone.getText().toString();
				
				System.out.println(bundle + "11111111");

				Intent intent = new Intent(SetActivity.this, MaindemoActivity.class);

				intent.putExtra("NAME", bundle);
				intent.putExtra("phone", newtephone);
		
				// 返回intent
				setResult(100, intent);

				SetActivity.this.finish();

				break;
			case 1:
				// loadingDialog.cancel();
				Toast.makeText(getApplicationContext(), "原密码错误",
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

	private boolean loginServer(String username,String newusername,String phone, String newpassword,String lastpassword) {
		boolean loginValidate = false;
		// 使用apache HTTP客户端实现
		String urlStr = "http://219.245.67.1:8080/service/servlet/ReLoginServlet";
	//	String urlStr = "http://219.245.65.224:8080/service/servlet/LoginServlet";
		HttpPost request = new HttpPost(urlStr);
		// 如果传递参数多的话，可以丢传递的参数进行封装
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 添加用户名和密码
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("newusername", newusername));
		params.add(new BasicNameValuePair("newphone", phone));
		params.add(new BasicNameValuePair("newpassword", newpassword));
		params.add(new BasicNameValuePair("lastpassword", lastpassword));
		try {
			// 设置请求参数项
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient client = getHttpClient();
			// 执行请求返回相应
			HttpResponse response = client.execute(request);

			// 判断是否请求成功
			if (response.getStatusLine().getStatusCode() == 200) {
				loginValidate = true;
				// 获得响应信息
				responseMsg = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return loginValidate;
	}

	// // 初始化HttpClient，并设置超时
	public HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}
}
