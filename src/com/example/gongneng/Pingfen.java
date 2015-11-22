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
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.people.PageActivity;
import com.example.people.R;

public class Pingfen extends Activity implements OnRatingBarChangeListener {

	private TextView mRatingText, save, name, phone, back, starttime, overtime,
			alltime;
	private EditText pingjia;
	String level = "4.5星/90.0分", ID1, policephone, policename, time, overtime1;
	private String responseMsg = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pingfen);

		Intent intent = getIntent();
		ID1 = intent.getExtras().getString("ID1");
		policename = intent.getExtras().getString("policename");
		policephone = intent.getExtras().getString("ptelphone");
		time = intent.getExtras().getString("time");
		overtime1 = intent.getExtras().getString("overtime");

		String[] starttime2 = { time.substring(0, 4), time.substring(5, 7),
				time.substring(8, 10), time.substring(11, 13),
				time.substring(13, 15) };
		String[] overtime2 = { overtime1.substring(0, 4),
				overtime1.substring(5, 7), overtime1.substring(8, 10),
				overtime1.substring(11, 13), overtime1.substring(13, 15) };


		int overfen = Integer.valueOf(overtime2[0]) * 365 * 24 * 60
				+ Integer.valueOf(overtime2[1]) * 30 * 24 * 60
				+ Integer.valueOf(overtime2[2]) * 24 * 60
				+ Integer.valueOf(overtime2[3]) * 60
				+ Integer.valueOf(overtime2[4]);

		int startfen = Integer.valueOf(starttime2[0]) * 365 * 24 * 60
				+ Integer.valueOf(starttime2[1]) * 30 * 24 * 60
				+ Integer.valueOf(starttime2[2]) * 24 * 60
				+ Integer.valueOf(starttime2[3]) * 60
				+ Integer.valueOf(starttime2[4]);

	    int allfen =overfen-startfen;
	    int nian=allfen/(60*24*365);
	    int yue=(allfen-nian*(60*24*365))/(60*24*30);
	    int ri=(allfen-nian*(60*24*365)-yue*(60*24*30))/(60*24);
	    int shi=(allfen-nian*(60*24*365)-yue*(60*24*30)-ri*(60*24))/(60);
	    int fen = (allfen-nian*(60*24*365)-yue*(60*24*30)-ri*(60*24)-shi*60);
	    
	    System.out.println("相距时间为："+nian+"年"+yue+"月"+ri+"日"+shi+"时"+fen+"分");
	    
	    StringBuilder alltime1=new StringBuilder();	
	    alltime1.append("时长：");
	    if(nian!=0){    	
	    	alltime1.append(nian+"年");
	    }
	    if(yue!=0){
	    	alltime1.append(yue+"月");
	    }
	    if(ri!=0){
	    	alltime1.append(ri+"日");
	    }
	    if(shi!=0){
	    	alltime1.append(shi+"时");
	    }
	    alltime1.append(fen+"分");

		back = (TextView) findViewById(R.id.return001);
		mRatingText = (TextView) findViewById(R.id.rating);
		name = (TextView) findViewById(R.id.policename);
		phone = (TextView) findViewById(R.id.policephone);
		pingjia = (EditText) findViewById(R.id.edittext);
		save = (TextView) findViewById(R.id.sure);
		starttime = (TextView) findViewById(R.id.starttime);
		overtime = (TextView) findViewById(R.id.overtime);
		alltime = (TextView) findViewById(R.id.alltime);

		// We copy the most recently changed rating on to these indicator-only
		// rating bars
		// mSmallRatingBar = (RatingBar) findViewById(R.id.small_ratingbar);
		// The different rating bars in the layout. Assign the listener to us.

		((RatingBar) findViewById(R.id.ratingbar2))
				.setOnRatingBarChangeListener(this);

		starttime.setText(time);
		overtime.setText(overtime1);
		alltime.setText(alltime1);
		name.setText("警员：" + policename);
		phone.setText("电话：" + policephone);

		InitClick();
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		final int numStars = ratingBar.getNumStars();
		mRatingText.setText(" 星级评价：" + rating
		// + "/" + numStars
				+ "星/" + (rating * 20) + "分");
		level = rating + "星/" + (rating * 20) + "分";

	}

	void InitClick() {
		OnClickListener onclicklistener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.equals(save)) {

					Thread upThread = new Thread(new uppingjiaThread());
					upThread.start();

				} else if (v.equals(back)) {
					Pingfen.this.finish();
				}
			}
		};
		save.setOnClickListener(onclicklistener);
		back.setOnClickListener(onclicklistener);
	}

	class uppingjiaThread implements Runnable {
		@Override
		public void run() {

			try {
				// URL合法，但是这一步并不验证密码是否正确
				boolean upValidate = uppingfen();
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
				// 发生超时,返回值区别于null与正常信息
				e.printStackTrace();
			}
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(Pingfen.this, "评价成功", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(Pingfen.this, Detail.class);
				intent.putExtra("level", level);
				setResult(456, intent);
				Pingfen.this.finish();

				break;
			case 1:

				Toast.makeText(Pingfen.this, "读取失败", Toast.LENGTH_SHORT).show();
				break;
			case 2:

				Toast.makeText(Pingfen.this, "服务器连接失败！", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	private boolean uppingfen() {
		boolean loginValidate = false;
		// 使用apache HTTP客户端实现
		// String urlStr =
		// "http://219.245.65.102:8080/service/servlet/upServlet";
		String urlStr = "http://"+PageActivity.urltest+":8080/service/servlet/pingfenServlet";
		HttpPost request = new HttpPost(urlStr);
		// 如果传递参数多的话，可以丢传递的参数进行封装
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 添加用户名和密码
		params.add(new BasicNameValuePair("level", level));
		params.add(new BasicNameValuePair("ID1", ID1));
		System.out.println("params" + params);
		try {
			System.out.println("response??");
			// 设置请求参数项
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient client = getHttpClient();
			// 执行请求返回相应
			HttpResponse response = client.execute(request);
			System.out.println("response" + response);
			// 判断是否请求成功
			if (response.getStatusLine().getStatusCode() == 200) {
				loginValidate = true;
				// 获得响应信息
				responseMsg = EntityUtils.toString(response.getEntity());
				System.out.println("responseMsg" + responseMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginValidate;
	}

	// 初始化HttpClient，并设置超时
	public HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}
}
