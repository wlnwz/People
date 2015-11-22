package com.example.gongneng;

import java.text.SimpleDateFormat;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.people.PageActivity;
import com.example.people.R;
import com.util.Encrypt;
import com.util.SysApplication;

public class Register extends Activity implements OnClickListener {
	private ImageView  user_name_clear, password_clear,
			confirm_password_clear,sfz_clear,phone_clear;
	private TextView register_back,user_name_error, password_error, confirm_password_error,sfz_error,phone_error;
	private Button register_button;
	private EditText user_name_edit, password_edit, confirm_password_edit,sfzhm_edit,phone_edit;
	private String responseMsg = "",date;
	String newusername,newpassword;
	private static final int REQUEST_TIMEOUT = 5 * 1000;// ��������ʱ10����
	private static final int SO_TIMEOUT = 10 * 1000; // ���õȴ����ݳ�ʱʱ��10����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		SysApplication.getInstance().addActivity(this);
		
		Intent intent = getIntent();
		newusername = intent.getExtras().getString("username");
		newpassword = intent.getExtras().getString("password");
		
		
		InitView();
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		date = sDateFormat.format(new java.util.Date());
	}

	private void InitView() {
		// TODO Auto-generated method stub
		register_back = (TextView) findViewById(R.id.register_back);
		user_name_clear = (ImageView) findViewById(R.id.user_name_clear);
		password_clear = (ImageView) findViewById(R.id.password_clear);
		phone_clear = (ImageView) findViewById(R.id.phone_clear);
		sfz_clear = (ImageView) findViewById(R.id.sfz_clear);
		confirm_password_clear = (ImageView) findViewById(R.id.confirm_password_clear);
		register_button = (Button) findViewById(R.id.register_button);
		user_name_edit = (EditText) findViewById(R.id.user_name_edit);
		password_edit = (EditText) findViewById(R.id.password_edit);
		phone_edit = (EditText) findViewById(R.id.phone_edit);
		confirm_password_edit = (EditText) findViewById(R.id.confirm_password_edit);
		sfzhm_edit = (EditText) findViewById(R.id.sfzhm);
		user_name_error = (TextView) findViewById(R.id.user_name_error);
		password_error = (TextView) findViewById(R.id.password_error);
		phone_error = (TextView) findViewById(R.id.phone_error);
		confirm_password_error = (TextView) findViewById(R.id.confirm_password_error);
		sfz_error = (TextView) findViewById(R.id.sfz_error);
		
		register_back.setOnClickListener(this);
		register_button.setOnClickListener(this);
		user_name_edit.setOnClickListener(this);
		password_edit.setOnClickListener(this);
		confirm_password_edit.setOnClickListener(this);
		sfzhm_edit.setOnClickListener(this);
		user_name_clear.setOnClickListener(this);
		password_clear.setOnClickListener(this);
		sfz_clear.setOnClickListener(this);
		confirm_password_clear.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_back:
			// Intent intent = new
			// Intent(Register.this,registrationActivity.class);
			// startActivity(intent);
			Bundle bundle = new Bundle();
			bundle.putString("username", newusername);
			bundle.putString("password", newpassword);
			Intent intent = new Intent();
			intent.putExtras(bundle);
			// ����intent
			setResult(RESULT_OK, intent);
			Register.this.finish();

			break;
		case R.id.register_button:

			RegisterUser();
			break;
		case R.id.user_name_edit:
			user_name_error.setVisibility(View.GONE);
			user_name_clear.setVisibility(View.VISIBLE);
			password_clear.setVisibility(View.GONE);
			sfz_clear.setVisibility(View.GONE);
			confirm_password_clear.setVisibility(View.GONE);
			phone_clear.setVisibility(View.GONE);
			break;
		case R.id.password_edit:
			password_error.setVisibility(View.GONE);
			user_name_clear.setVisibility(View.GONE);
			password_clear.setVisibility(View.VISIBLE);
			sfz_clear.setVisibility(View.GONE);
			confirm_password_clear.setVisibility(View.GONE);
			phone_clear.setVisibility(View.GONE);
			break;
		case R.id.confirm_password_edit:
			confirm_password_error.setVisibility(View.GONE);
			user_name_clear.setVisibility(View.GONE);
			password_clear.setVisibility(View.GONE);
			sfz_clear.setVisibility(View.GONE);
			confirm_password_clear.setVisibility(View.VISIBLE);
			phone_clear.setVisibility(View.GONE);
			break;
		case R.id.sfzhm:
			sfz_error.setVisibility(View.GONE);
			user_name_clear.setVisibility(View.GONE);
			password_clear.setVisibility(View.GONE);
			confirm_password_clear.setVisibility(View.GONE);
			sfz_clear.setVisibility(View.VISIBLE);
			phone_clear.setVisibility(View.GONE);
			break;
		case R.id.phone_edit:
			password_error.setVisibility(View.GONE);
			user_name_clear.setVisibility(View.GONE);
			phone_clear.setVisibility(View.VISIBLE);
			sfz_clear.setVisibility(View.GONE);
			confirm_password_clear.setVisibility(View.GONE);
			password_clear.setVisibility(View.GONE);
			break;
		case R.id.user_name_clear:
			user_name_edit.setText("");
			break;

		case R.id.password_clear:
			password_edit.setText("");
			break;
		case R.id.confirm_password_clear:
			confirm_password_edit.setText("");
			break;
		case R.id.sfz_clear:
			sfzhm_edit.setText("");
			break;
		case R.id.phone_clear:
			phone_edit.setText("");
			break;
		default:
			break;
		}
	}

	public void RegisterUser() {

		if (user_name_edit.getText().toString().trim().equals("")
				|| user_name_edit.getText().toString().trim().length() > 20
				|| user_name_edit.getText().toString().trim().length() < 1) {
			user_name_error.setVisibility(View.VISIBLE);
		} else if (password_edit.getText().toString().trim().equals("")
				|| password_edit.getText().toString().trim().length() > 16
				|| password_edit.getText().toString().trim().length() < 6) {
			password_error.setVisibility(View.VISIBLE);
		} else if (!confirm_password_edit.getText().toString().trim()
				.equals(password_edit.getText().toString().trim())) {
			confirm_password_error.setVisibility(View.VISIBLE);
		} else if (
			 sfzhm_edit.getText().toString().trim().length()>20
			 || sfzhm_edit.getText().toString().trim().length() < 18)
			{
			sfz_error.setVisibility(View.VISIBLE);
		}else if (
				 phone_edit.getText().toString().trim().length()<11
				 || sfzhm_edit.getText().toString().trim().length()> 18)
				{
				phone_error.setVisibility(View.VISIBLE);
			}else {

			String newusername = user_name_edit.getText().toString();
			String newpassword = Encrypt
					.md5(password_edit.getText().toString());
			String confirmpwd = Encrypt.md5(confirm_password_edit.getText()
					.toString());

			Thread loginThread = new Thread(new RegisterThread());
			loginThread.start();
		}

	}

	// ��ʼ��HttpClient�������ó�ʱ
	public HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}

	private boolean registerServer(String username, String password,String sfzhm,String phone) {
		boolean loginValidate = false;
		// ʹ��apache HTTP�ͻ���ʵ��
		   String urlStr = "http://"+PageActivity.urltest+":8080/service/servlet/RegisterServlet";
		//	String urlStr = "http://219.245.67.1:8080/service/servlet/RegisterServlet";
		HttpPost request = new HttpPost(urlStr);
		// ������ݲ�����Ļ������Զ����ݵĲ������з�װ
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// ����û���������
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("sfzhm", sfzhm));
		params.add(new BasicNameValuePair("phone", phone));
		params.add(new BasicNameValuePair("time", date));
		try {
			// �������������
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient client = getHttpClient();
			// ִ�����󷵻���Ӧ
			HttpResponse response = client.execute(request);

			// �ж��Ƿ�����ɹ�
			if (response.getStatusLine().getStatusCode() == 200) {
				loginValidate = true;
				// �����Ӧ��Ϣ
				responseMsg = EntityUtils.toString(response.getEntity());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return loginValidate;

	}

	// Handler
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// registerDialog.cancel();
				Bundle bundle = new Bundle();
				bundle.putString("username", user_name_edit.getText()
						.toString());
				bundle.putString("password", password_edit.getText().toString());
				
				Intent intent = new Intent();
				intent.putExtras(bundle);
				// ����intent
				setResult(RESULT_OK, intent);
				Register.this.finish();
				Toast.makeText(Register.this, "ע��ɹ�", 1).show();
				break;
			case 1:
				// registerDialog.cancel();
				Toast.makeText(getApplicationContext(), "ע��ʧ��",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				// registerDialog.cancel();
				Toast.makeText(getApplicationContext(), "����������ʧ�ܣ�",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				// registerDialog.cancel();
				Toast.makeText(getApplicationContext(), "���û����Ѿ���ע�ᣡ",
						Toast.LENGTH_SHORT).show();
				break;

			}

		}
	};

	// RegisterThread�߳���
	class RegisterThread implements Runnable {

		@Override
		public void run() {
			String username = user_name_edit.getText().toString();
			String password = Encrypt.md5(password_edit.getText().toString());
			String sfzhm = sfzhm_edit.getText().toString();
			String phone = phone_edit.getText().toString();
			System.out.println(username + "11111111111");
			// URL�Ϸ���������һ��������֤�����Ƿ���ȷ
			boolean registerValidate = registerServer(username, password,sfzhm,phone);
			// System.out.println("----------------------------bool is :"+registerValidate+"----------response:"+responseMsg);
			Message msg = handler.obtainMessage();
			if (registerValidate) {
				if (responseMsg.equals("success")) {
					msg.what = 0;
					handler.sendMessage(msg);
				} else if (responseMsg.equals("have")){
					msg.what=3;
					handler.sendMessage(msg);
				}else{
					msg.what = 1;
					handler.sendMessage(msg);
				}

			} else {
				msg.what = 2;
				handler.sendMessage(msg);
			}
		}

	}

	// ��ֹʹ�÷��ؼ�
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			return true;// ���ѵ����˼�
		}
		return super.onKeyDown(keyCode, event);
	}

}
