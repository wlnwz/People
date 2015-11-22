package com.example.gongneng;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.people.PageActivity;
import com.example.people.R;
import com.example.people.R.color;
import com.util.BuileGestureExt;

public class Myrecord extends Activity {
	MyListAdapter myAdapter = null;
	private ListView listview;
	private TextView back, qd, choose1, choose2, choose3, choose4;
	URL url = null;
	HttpURLConnection httpurlconnection = null;
	private String responseMsg = "", name;
	private String[] policename, ptelphone, myweizhi, sure, grade, time, ID1,
	infor,picname,voicename,infor1,picname1,voicename1,
	infor2,picname2,voicename2,infor3,picname3,voicename3,infor4,picname4,voicename4,
			overtime, policename1, ptelphone1, myweizhi1, sure1, grade1, time1,
			ID11, policename2, ptelphone2, myweizhi2, sure2, grade2, time2,
			ID12, policename3, ptelphone3, myweizhi3, sure3, grade3, time3,
			ID13, overtime3, policename4, ptelphone4, myweizhi4, sure4, grade4,
			time4, ID14, overtime4;
	String level;
	private int length, posi, ch = 0;

	private GestureDetector gestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myrecord);

		Intent intent = getIntent();
		name = intent.getExtras().getString("name");

		// length = intent.getExtras().getInt("length");
		// ID1 = intent.getExtras().getStringArray("ID1");
		// myweizhi = intent.getExtras().getStringArray("myweizhi");
		// ptelphone = intent.getExtras().getStringArray("ptelphone");
		// policename = intent.getExtras().getStringArray("policername");
		// time = intent.getExtras().getStringArray("time");
		// sure = intent.getExtras().getStringArray("sure");
		// grade = intent.getExtras().getStringArray("grade");

		load();

		Ingroup();

		Init();
		Onclick();

		gestureDetector = new BuileGestureExt(this,
				new BuileGestureExt.OnGestureResult() {
					@Override
					public void onGestureResult(int direction) {

						// show(Integer.toString(direction));
						dochange(direction);
					}
				}).Buile();

	}

	void Ingroup() {
		int aa = 0, bb = 0, cc = 0, dd = 0;
		for (int i = 0; i < length; i++) {
			if (sure[i].equals("未接收")) {
				aa++;
				System.out.println("aa=" + aa);
			} else if (sure[i].equals("正在处理")) {

				bb++;
				System.out.println("bb=" + bb);
			} else if (sure[i].equals("完结  0")) {

				cc++;
				System.out.println("cc=" + cc);
			} else {

				dd++;
				System.out.println("dd=" + dd);
			}
		}

		ID11 = new String[aa];
		myweizhi1 = new String[aa];
		ptelphone1 = new String[aa];
		policename1 = new String[aa];
		time1 = new String[aa];
		sure1 = new String[aa];
		grade1 = new String[aa];
		infor1 = new String[aa];
		picname1 = new String[aa];
		voicename1 = new String[aa];

		ID12 = new String[bb];
		myweizhi2 = new String[bb];
		ptelphone2 = new String[bb];
		policename2 = new String[bb];
		time2 = new String[bb];
		sure2 = new String[bb];
		grade2 = new String[bb];
		infor2 = new String[bb];
		picname2 = new String[bb];
		voicename2 = new String[bb];

		ID13 = new String[cc];
		myweizhi3 = new String[cc];
		ptelphone3 = new String[cc];
		policename3 = new String[cc];
		time3 = new String[cc];
		overtime3 = new String[cc];
		sure3 = new String[cc];
		grade3 = new String[cc];
		infor3 = new String[cc];
		picname3 = new String[cc];
		voicename3 = new String[cc];

		ID14 = new String[dd];
		myweizhi4 = new String[dd];
		ptelphone4 = new String[dd];
		policename4 = new String[dd];
		time4 = new String[dd];
		overtime4 = new String[dd];
		sure4 = new String[dd];
		grade4 = new String[dd];
		infor4 = new String[dd];
		picname4 = new String[dd];
		voicename4 = new String[dd];

		int a1 = 0, b1 = 0, c1 = 0, d1 = 0;
		for (int i = 0; i < length; i++) {
			if (sure[i].equals("未接收")) {
				ID11[a1] = ID1[i];
				sure1[a1] = sure[i];
				myweizhi1[a1] = myweizhi[i];
				ptelphone1[a1] = ptelphone[i];
				policename1[a1] = policename[i];
				time1[a1] = time[i];
				grade1[a1] = grade[i];
				infor1[a1] = infor[i];
				picname1[a1] = picname[i];
				voicename1[a1] = voicename[i];

				a1++;
			} else if (sure[i].equals("正在处理")) {
				ID12[b1] = ID1[i];
				sure2[b1] = sure[i];
				myweizhi2[b1] = myweizhi[i];
				ptelphone2[b1] = ptelphone[i];
				policename2[b1] = policename[i];
				time2[b1] = time[i];
				grade2[b1] = grade[i];
				infor2[b1] = infor[i];
				picname2[b1] = picname[i];
				voicename2[b1] = voicename[i];
				b1++;
			} else if (sure[i].equals("完结  0")) {
				ID13[c1] = ID1[i];
				sure3[c1] = sure[i];
				myweizhi3[c1] = myweizhi[i];
				ptelphone3[c1] = ptelphone[i];
				policename3[c1] = policename[i];
				time3[c1] = time[i];
				overtime3[c1] = overtime[i];
				grade3[c1] = grade[i];
				infor3[c1] = infor[i];
				picname3[c1] = picname[i];
				voicename3[c1] = voicename[i];
				c1++;
			} else {
				ID14[d1] = ID1[i];
				sure4[d1] = sure[i];
				myweizhi4[d1] = myweizhi[i];
				ptelphone4[d1] = ptelphone[i];
				policename4[d1] = policename[i];
				time4[d1] = time[i];
				overtime4[d1] = overtime[i];
				grade4[d1] = grade[i];
				infor4[d1] = infor[i];
				picname4[d1] = picname[i];
				voicename4[d1] = voicename[i];
				d1++;
			}
		}
	}

	void Init() {

		back = (TextView) findViewById(R.id.return001);
		choose1 = (TextView) findViewById(R.id.choose01);
		choose2 = (TextView) findViewById(R.id.choose02);
		choose3 = (TextView) findViewById(R.id.choose03);
		choose4 = (TextView) findViewById(R.id.choose04);
		listview = (ListView) findViewById(R.id.Listview);
		myAdapter = new MyListAdapter(Myrecord.this, R.layout.recordlist);
		listview.setAdapter(myAdapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (ch == 0) {
					sure = sure1;
					ID1 = ID11;
					policename = policename1;
					ptelphone = ptelphone1;
					time = time1;
					infor = infor1;
					picname = picname1;
					voicename = voicename1;
				} else if (ch == 1) {
					sure = sure2;
					ID1 = ID12;
					policename = policename2;
					ptelphone = ptelphone2;
					time = time2;
					infor = infor2;
					picname = picname2;
					voicename = voicename2;
				} else if (ch == 2) {
					sure = sure4;
					ID1 = ID14;
					policename = policename4;
					ptelphone = ptelphone4;
					time = time4;
					overtime = overtime4;
					infor = infor4;
					picname = picname4;
					voicename = voicename4;
				} else if (ch == 3) {
					sure = sure3;
					ID1 = ID13;
					policename = policename3;
					ptelphone = ptelphone3;
					time = time3;
					overtime = overtime3;
					infor = infor3;
					picname = picname3;
					voicename = voicename3;
				}
				{

					Intent intent = new Intent(Myrecord.this, Detail.class);
					intent.putExtra("sure", sure[position]);
					intent.putExtra("ID1", ID1[position]);
					intent.putExtra("policename", policename[position]);
					intent.putExtra("ptelphone", ptelphone[position]);
					intent.putExtra("time", time[position]);
					intent.putExtra("overtime", overtime[position]);
					intent.putExtra("infor", infor[position]);
					intent.putExtra("picname", picname[position]);
					intent.putExtra("voicename", voicename[position]);
					startActivityForResult(intent,789);
				}
			}
		});

	}

	void Onclick() {
		OnClickListener monclicklistener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.equals(back)) {
					Myrecord.this.finish();
				} else if (v.equals(choose1)) {
					ch = 0;
				} else if (v.equals(choose2)) {
					ch = 1;
				} else if (v.equals(choose3)) {
					ch = 2;
				} else if (v.equals(choose4)) {
					ch = 3;
				}
				setcolor(ch);
				myAdapter.notifyDataSetChanged();
				listview.setAdapter(myAdapter);

			}

		};
		back.setOnClickListener(monclicklistener);
		choose1.setOnClickListener(monclicklistener);
		choose2.setOnClickListener(monclicklistener);
		choose3.setOnClickListener(monclicklistener);
		choose4.setOnClickListener(monclicklistener);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (resultCode == 999) {

			Bundle extras = data.getExtras();

			level = extras.getString("level");
			sure[posi] = level;
			System.out.println("level!!=" + level);

			load();
			Ingroup();

			myAdapter = new MyListAdapter(Myrecord.this, R.layout.recordlist);
			listview.setAdapter(myAdapter);

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	// private void show(String value) {
	// Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
	// }

	private void dochange(int direction) {
		if (direction == 2) {
			if (ch < 3) {
				ch++;
			} else if (ch == 3) {
				ch = 0;
			}
		} else if (direction == 3) {
			if (ch > 0) {
				ch--;
			} else if (ch == 0) {
				ch = 3;
			}

		}
		setcolor(ch);

		myAdapter.notifyDataSetChanged();
		listview.setAdapter(myAdapter);

	}

	private void setcolor(int ch) {
		if (ch == 0) {
			choose4.setBackgroundColor(getResources().getColor(color.white));
			choose3.setBackgroundColor(getResources().getColor(color.white));
			choose2.setBackgroundColor(getResources().getColor(color.white));

			choose1.setBackgroundResource(R.drawable.back);
			choose1.setTextColor(getResources().getColor(color.blue));
			choose2.setTextColor(getResources().getColor(color.black123));
			choose3.setTextColor(getResources().getColor(color.black123));
			choose4.setTextColor(getResources().getColor(color.black123));
		} else if (ch == 1) {
			choose1.setBackgroundColor(getResources().getColor(color.white));
			choose3.setBackgroundColor(getResources().getColor(color.white));
			choose4.setBackgroundColor(getResources().getColor(color.white));

			choose2.setBackgroundResource(R.drawable.back);
			choose2.setTextColor(getResources().getColor(color.blue));
			choose1.setTextColor(getResources().getColor(color.black123));
			choose3.setTextColor(getResources().getColor(color.black123));
			choose4.setTextColor(getResources().getColor(color.black123));
		} else if (ch == 2) {
			choose2.setBackgroundColor(getResources().getColor(color.white));
			choose1.setBackgroundColor(getResources().getColor(color.white));
			choose4.setBackgroundColor(getResources().getColor(color.white));

			choose3.setBackgroundResource(R.drawable.back);
			choose3.setTextColor(getResources().getColor(color.blue));
			choose2.setTextColor(getResources().getColor(color.black123));
			choose1.setTextColor(getResources().getColor(color.black123));
			choose4.setTextColor(getResources().getColor(color.black123));
		} else if (ch == 3) {
			choose3.setBackgroundColor(getResources().getColor(color.white));
			choose2.setBackgroundColor(getResources().getColor(color.white));
			choose1.setBackgroundColor(getResources().getColor(color.white));

			choose4.setBackgroundResource(R.drawable.back);
			choose4.setTextColor(getResources().getColor(color.blue));
			choose2.setTextColor(getResources().getColor(color.black123));
			choose3.setTextColor(getResources().getColor(color.black123));
			choose1.setTextColor(getResources().getColor(color.black123));
		}
	}

	public class MyListAdapter extends ArrayAdapter<Object> {
		int mTextViewResourceID = 0;
		private Context mContext;

		public MyListAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			mTextViewResourceID = textViewResourceId;
			mContext = context;
		}

		public int getCount() {
			if (ch == 0) {
				length = sure1.length;
			} else if (ch == 1) {
				length = sure2.length;
			} else if (ch == 2) {
				length = sure4.length;
			} else if (ch == 3) {
				length = sure3.length;
			}
			System.out.println("length==" + length);
			return length;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			TextView text = null, shijian = null, didian = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						mTextViewResourceID, null);

			}
			text = (TextView) convertView.findViewById(R.id.xunjing2);
			shijian = (TextView) convertView.findViewById(R.id.time);
			didian = (TextView) convertView.findViewById(R.id.weizhi);
			qd = (TextView) convertView.findViewById(R.id.zhuangtai);

			if (ch == 0) {

				text.setText("巡警-" + policename1[position]);
				didian.setText(myweizhi1[position]);
				shijian.setText(time1[position]);
				qd.setText(sure1[position]);
			} else if (ch == 1) {
				text.setText("巡警-" + policename2[position]);
				didian.setText(myweizhi2[position]);
				shijian.setText(time2[position]);
				qd.setText(sure2[position]);
			} else if (ch == 2) {
				text.setText("巡警-" + policename4[position]);
				didian.setText(myweizhi4[position]);
				shijian.setText(time4[position]);
				qd.setText(sure4[position]);
			} else if (ch == 3) {
				text.setText("巡警-" + policename3[position]);
				didian.setText(myweizhi3[position]);
				shijian.setText(time3[position]);
				qd.setText(sure3[position]);
			}
			return convertView;
		}
	}

	/**
	 * load(),用于下载最新的记录信息
	 * **/
	void load() {
		Thread downThread = new Thread(new SureThread());
		downThread.start();
		try {
			downThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class SureThread implements Runnable {
		@Override
		public void run() {
			try {
				// URL合法，但是这一步并不验证密码是否正确
				boolean upValidate = upServerplocation(name);
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

	Handler handler = new Handler();

	private boolean upServerplocation(String username) {
		boolean loginValidate = false;
		// 使用apache HTTP客户端实现
		String urlStr = "http://"+PageActivity.urltest+":8080/service/servlet/SureServlet";
		//		String urlStr = "http://219.245.65.5:8080/service/servlet/SureServlet";
		
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
		// 上传用户名
		try {
			dos = new DataOutputStream(httpurlconnection.getOutputStream());
			dos.writeUTF("record");
			dos.writeUTF(String.valueOf(username));
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

			// 读取连接状态以及数据总数
			responseMsg = dis.readUTF();
//			int abc = dis.readInt();
//			System.out.println("abc@@@@@@2==="+abc);
			
			length = dis.readInt();

			ID1 = new String[length];
			myweizhi = new String[length];
			ptelphone = new String[length];
			policename = new String[length];
			time = new String[length];
			overtime = new String[length];
			sure = new String[length];
			grade = new String[length];
			infor = new String[length];
			picname = new String[length];
			voicename = new String[length];

			// 循环读取警察位置表中的所有信息，包括警察编号、电话、位置信息等
			for (int i = 0; i < length; i++) {
				ID1[i] = dis.readUTF();
				policename[i] = dis.readUTF();
				ptelphone[i] = dis.readUTF();
				myweizhi[i] = dis.readUTF();
				sure[i] = dis.readUTF();
				grade[i] = dis.readUTF();
				time[i] = dis.readUTF();
				overtime[i] = dis.readUTF();
				
				infor[i] = dis.readUTF();
				picname[i] = dis.readUTF();
				voicename[i] = dis.readUTF();
				
				System.out.println("length*******"+grade[i] + length + " " + sure[i]);
			}

			for (int i = 0; i < length; i++) {
				if (sure[i].equals("0")) {
					sure[i] = "未接收";

				} else if (sure[i].equals("1")) {
					sure[i] = "正在处理";

				} else {
					sure[i] = "完结  " + grade[i];

				}
				if (policename[i].equals("null")) {
					System.out.println("！！！！！！");
					policename[i] = "暂无";
				}
				if (myweizhi[i].equals(" ")) {
					myweizhi[i] = "未知";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginValidate;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		myAdapter.notifyDataSetChanged();
		listview.setAdapter(myAdapter);
	}
}
