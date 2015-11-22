/**
 * Copyright (C) 2014 yeran
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package com.example.slidingmenu.view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.example.gongneng.Callpolice;
import com.example.gongneng.gnActivity;
import com.example.people.PageActivity;
import com.example.people.MaindemoActivity;
import com.example.people.R;
import com.example.people.R.color;

public class ViewPageFragment extends Fragment {

	private TextView showLeft;
	private MyAdapter mAdapter;
	private ViewPager mPager;
	private ArrayList<Fragment> pagerItemList = new ArrayList<Fragment>();
	private TextView weizhi1, share, callpolice;
	private ImageView read;
	private MyBroadcastRecever myBroadcastRecever;
	private String weizhi, name, phone = "0", ID, city,
			BROADCAST_ACTION = "com.example.broadcast.sure";
	double location1, location2;
	private LatLng ll;
	URL url = null;
	HttpURLConnection httpurlconnection = null;
	private String responseMsg = "";
	private int length = 1, abc = 0, tongzhilength = 1, badrecord;
	private String[] policename, ptelphone, myweizhi, sure, grade, time,overtime, ID1,
			titl, tim1, maintex, id2,infor,picname,voicename;

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		SDKInitializer.initialize(this.getActivity().getApplicationContext());

		IntentFilter filter = new IntentFilter();
		myBroadcastRecever = new MyBroadcastRecever();
		// 设置接收广播的类型，这里要和Service里设置的类型匹配，还可以在AndroidManifest.xml文件中注册

		filter.addAction("com.example.set.broadcast");
		filter.addAction("com.example.broadcast.name");
		getActivity().registerReceiver(myBroadcastRecever, filter);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.view_pager, null);

		showLeft = (TextView) mView.findViewById(R.id.showLeft);
		showLeft.setBackgroundResource(R.drawable.menu1);

		weizhi1 = (TextView) mView.findViewById(R.id.location1);
		share = (TextView) mView.findViewById(R.id.share);
		callpolice = (TextView) mView.findViewById(R.id.baojing);

		read = (ImageView) mView.findViewById(R.id.read);

		mPager = (ViewPager) mView.findViewById(R.id.pager);
		PageFragment1 page1 = new PageFragment1();
		pagerItemList.add(page1);
		mAdapter = new MyAdapter(getFragmentManager());
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				if (myPageChangeListener != null)
					myPageChangeListener.onPageSelected(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int position) {
			}
		});

		Initclick();

		return mView;
	}

	void Initclick() {

		OnClickListener mOnclickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (v.equals(share)) {
					if ((weizhi1.getText()).equals("正在定位...")) {
						Toast.makeText(getActivity(), "正在定位中，请稍等...",
								Toast.LENGTH_SHORT).show();
					} else {

						Intent intent = new Intent();
						intent.putExtra("city", city);
						intent.putExtra("weizhi", weizhi);
						intent.putExtra("location1", ll.latitude);
						intent.putExtra("location2", ll.longitude);
						intent.setClass(getActivity(), gnActivity.class);
						startActivity(intent);
					}
				} else if (v.equals(callpolice)) {
					if ((weizhi1.getText()).equals("正在定位...")) {
						Toast.makeText(getActivity(), "正在定位中，请稍等...",
								Toast.LENGTH_SHORT).show();
					}
					/*
					 * 信用度以15为分界线，可定期整理
					 */
					else if (badrecord <= 15) {
						if (name == null || name.equals("请登录")) {
							name = "匿名";
						}
						if (ll == null) {
							ll = new LatLng(0, 0);
						}
						Intent intent = new Intent(getActivity(),
								Callpolice.class);
						intent.putExtra("name", name);
						intent.putExtra("weizhi", weizhi);
						intent.putExtra("phone", phone);
						intent.putExtra("ID", ID);
						intent.putExtra("location1", ll.latitude);
						intent.putExtra("location2", ll.longitude);
						startActivity(intent);
					} else {
						Toast.makeText(getActivity(), "您的信用记录不良！请联系警方进行相关处理！",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		share.setOnClickListener(mOnclickListener);
		callpolice.setOnClickListener(mOnclickListener);
	}

	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		showLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MaindemoActivity) getActivity()).showLeft();
			}
		});

	}

	public boolean isFirst() {
		if (mPager.getCurrentItem() == 0)
			return true;
		else
			return false;
	}

	public boolean isEnd() {
		if (mPager.getCurrentItem() == pagerItemList.size() - 1)
			return true;
		else
			return false;
	}

	public class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return pagerItemList.size();
		}

		@Override
		public Fragment getItem(int position) {

			Fragment fragment = null;
			if (position < pagerItemList.size())
				fragment = pagerItemList.get(position);
			else
				fragment = pagerItemList.get(0);

			return fragment;

		}
	}

	private MyPageChangeListener myPageChangeListener;

	public void setMyPageChangeListener(MyPageChangeListener l) {

		myPageChangeListener = l;

	}

	public interface MyPageChangeListener {
		public void onPageSelected(int position);
	}

	class MyBroadcastRecever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("com.example.set.broadcast")) {
				weizhi1.setTextColor(color.black);
				city = intent.getStringExtra("city");
				weizhi = intent.getStringExtra("weizhi1");
				location1 = intent.getDoubleExtra("location1", 0);
				location2 = intent.getDoubleExtra("location2", 0);
				ll = new LatLng(location1, location2);
				weizhi1.setText(intent.getStringExtra("weizhi1"));
			} else if (intent.getAction().equals("com.example.broadcast.name")) {
				name = intent.getStringExtra("name");
				phone = intent.getStringExtra("phone");
				ID = intent.getStringExtra("ID");
				badrecord = intent.getIntExtra("badrecord", 0);
				Initsearch();
			}

		}
	}

	// 进入搜索报警相应线程，查询数据库，如果已经被响应，更新
	private void Initsearch() {
		mHandler.post(runnable);
	}

	private Handler mHandler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			load();
			mHandler.postDelayed(runnable, 60000);

		}
	};

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

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				for (int i = 0; i < length; i++) {
					if (sure[i].equals("1")) {
						abc = 1;
					}
				}
				if (abc == 1) {
					read.setVisibility(View.VISIBLE);

				}

				sendbaoan();
				sendtongzhi();

				break;
			case 1:

				Toast.makeText(getActivity(), "读取失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:

				Toast.makeText(getActivity(), "服务器连接失败！", Toast.LENGTH_SHORT)
						.show();
				break;
			}

		}
	};

	private boolean upServerplocation(String username) {
		boolean loginValidate = false;
		// 使用apache HTTP客户端实现
		String urlStr = "http://"+PageActivity.urltest+":8080/service/servlet/SureServlet";
		//	String urlStr = "http://219.245.65.5:8080/service/servlet/SureServlet";

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
			dos.writeUTF("all");
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
				System.out.println("length==*****"+grade[i] + length + " " + sure[i]);
			}

		
			tongzhilength = dis.readInt();
			id2 = new String[tongzhilength];
			titl = new String[tongzhilength];
			tim1 = new String[tongzhilength];
			maintex = new String[tongzhilength];
			
			for (int i = 0; i < tongzhilength; i++) {
				id2[i] = dis.readUTF();
				tim1[i] = dis.readUTF();
				titl[i] = dis.readUTF();
				maintex[i] = dis.readUTF();				
			}
			System.out.println("length1@@@@@@@@2" + tongzhilength );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginValidate;
	}

	void sendbaoan() {
		Intent intent2 = new Intent();
		intent2.putExtra("length", length);
		intent2.putExtra("ID1", ID1);
		intent2.putExtra("policename", policename);
		intent2.putExtra("policephone", ptelphone);
		intent2.putExtra("weizhi", myweizhi);
		intent2.putExtra("time", time);
		intent2.putExtra("sure", sure);
		intent2.putExtra("grade", grade);
		intent2.setAction(BROADCAST_ACTION);
		getActivity().sendBroadcast(intent2);

		// Toast.makeText(getActivity(), "读取报案信息成功", Toast.LENGTH_SHORT).show();
	}

	void sendtongzhi() {
		Intent intent3 = new Intent();
		intent3.putExtra("tongzhilength", tongzhilength);
		intent3.putExtra("id2", id2);
		intent3.putExtra("tim1", tim1);
		intent3.putExtra("titl", titl);
		intent3.putExtra("maintex", maintex);
		intent3.setAction("com.example.tongzhi");
		getActivity().sendBroadcast(intent3);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(myBroadcastRecever);
	}
}
