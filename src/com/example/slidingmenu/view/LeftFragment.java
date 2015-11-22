/*
 * Copyright (C) 2014 yeran(Ge bing)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.slidingmenu.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gongneng.Cancellog;
import com.example.gongneng.Knowledge;
import com.example.gongneng.LoginActivity;
import com.example.gongneng.Myrecord;
import com.example.gongneng.SetActivity;
import com.example.gongneng.Tongzhi;
import com.example.gongneng.about;
import com.example.people.R;

public class LeftFragment extends Fragment {

	private TextView myrecord;
	private TextView tongz;
	private TextView set;
	private TextView about;
	private static TextView name;
	private TextView knowledge;
	int REQUEST_CODE = 100;
	String name1 = "请登录", phone, ID;
	private final String BROADCAST_ACTION = "com.example.broadcast.name";
	private MyBroadcastRecever myBroadcastRecever;
	private namechuanzhi mcallback;
	private ImageView red;
	private String[] policename, ptelphone, myweizhi, sure, grade, time, ID1,
			titl, tim1, maintex, id2;
	private int length = 100, tongzhilength = 0, badrecord;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mcallback = (namechuanzhi) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		System.out.println(name);

		ID1 = new String[length];
		myweizhi = new String[length];
		ptelphone = new String[length];
		policename = new String[length];
		time = new String[length];
		sure = new String[length];
		grade = new String[length];

		id2 = new String[length];
		tim1 = new String[length];
		titl = new String[length];
		maintex = new String[length];

		IntentFilter filter = new IntentFilter();
		myBroadcastRecever = new MyBroadcastRecever();

		// 设置接收广播的类型，这里要和Service里设置的类型匹配，还可以在AndroidManifest.xml文件中注册
		filter.addAction("com.example.broadcast.sure");
		filter.addAction("com.example.tongzhi");
		getActivity().registerReceiver(myBroadcastRecever, filter);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (resultCode == REQUEST_CODE) {

			Bundle extras = data.getExtras();

			name1 = extras.getString("NAME");
			phone = extras.getString("phone");
			badrecord = extras.getInt("badrecord");
			ID = extras.getString("ID");

			System.out.println(name1 + "22222222");

			name.setText(name1);

			mcallback.onNamechoose(name1);

			Intent a = new Intent();
			a.putExtra("name", name1);
			a.putExtra("phone", phone);
			a.putExtra("ID", ID);
			a.putExtra("badrecord", badrecord);
			a.setAction(BROADCAST_ACTION);
			this.getActivity().sendBroadcast(a);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public interface namechuanzhi {
		public void onNamechoose(String name);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left, null);

		final ImageView imageview = (ImageView) view
				.findViewById(R.id.head_view);
		red = (ImageView) view.findViewById(R.id.red);
		myrecord = (TextView) view.findViewById(R.id.record);
		tongz = (TextView) view.findViewById(R.id.tzhi);
		knowledge = (TextView) view.findViewById(R.id.knowledge);
		set = (TextView) view.findViewById(R.id.set);
		about = (TextView) view.findViewById(R.id.about);
		name = (TextView) view.findViewById(R.id.name1);

		imageview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// fragment是基于activity的，我用了后的理解是，它就像是相当于activity布局中的一个View，
				// 一个activity可以有多个View（fragment）。
				// 你要实现用intent的跳转，必须是要通过activity跳activity的，new Intent的时候，
				// 里面两个参数很明确的，一个是第一个参数是Context，fragemnt显然不行。
				// 调用的是getActivity(),这样写就可以intent.setClass(getActivity()，跳转的Activity.class)

				if (name1.equals("请登录")) {
					Intent mainIntent = new Intent();
					mainIntent.setClass(getActivity(), LoginActivity.class);
					startActivityForResult(mainIntent, 11);
				} else {
					Intent intent = new Intent();
					intent.setClass(getActivity(), Cancellog.class);
					intent.putExtra("username", name1);
					intent.putExtra("telphone", phone);
					intent.putExtra("ID", ID);
					startActivityForResult(intent, 22);
				}
			}

		});

		about.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mainIntent1 = new Intent();
				mainIntent1.setClass(getActivity(), about.class);
				startActivity(mainIntent1);
			}

		});

		knowledge.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mainIntent = new Intent();
				mainIntent.setClass(getActivity(), Knowledge.class);
				startActivity(mainIntent);
			}

		});

		tongz.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mainintent = new Intent(getActivity(), Tongzhi.class);
				mainintent.putExtra("tongzhilength", tongzhilength);
				mainintent.putExtra("id2", id2);
				mainintent.putExtra("tim1", tim1);
				mainintent.putExtra("titl", titl);
				mainintent.putExtra("maintex", maintex);
				System.out.println("titl" + titl[0]);
				startActivity(mainintent);
			}

		});

		set.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (name.getText().toString().equals("请登录")) {
					Toast.makeText(getActivity(), "请先登录您的账号！",
							Toast.LENGTH_LONG).show();

				} else {
					Intent intent = new Intent(getActivity(), SetActivity.class);
					intent.putExtra("username", name1);
					intent.putExtra("telphone", phone);
					startActivityForResult(intent, 123);
				}
			}

		});

		myrecord.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (name.getText().toString().equals("请登录")) {
					Toast.makeText(getActivity(), "请先登录您的账号！",
							Toast.LENGTH_LONG).show();

				} else {
					Intent intent = new Intent(getActivity(), Myrecord.class);
					intent.putExtra("name", name1);
					intent.putExtra("policername", policename);
					intent.putExtra("ptelphone", ptelphone);
					intent.putExtra("ID1", ID1);
					intent.putExtra("length", length);
					intent.putExtra("time", time);
					intent.putExtra("myweizhi", myweizhi);
					intent.putExtra("sure", sure);
					intent.putExtra("grade", grade);
					startActivity(intent);
					red.setVisibility(View.GONE);
				}
			}

		});

		return view;
	}

	protected Context getApplicationContext() {
		// TODO Auto-generated method stub

		return null;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	class MyBroadcastRecever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			if (intent.getAction().equals("com.example.broadcast.sure")) {
				length = intent.getIntExtra("length", 0);
				ID1 = intent.getStringArrayExtra("ID1");
				policename = intent.getStringArrayExtra("policename");
				ptelphone = intent.getStringArrayExtra("policephone");
				myweizhi = intent.getStringArrayExtra("weizhi");
				time = intent.getStringArrayExtra("time");
				sure = intent.getStringArrayExtra("sure");
				grade = intent.getStringArrayExtra("grade");

				for (int i = 0; i < length; i++) {
					if (sure[i].equals("0")) {
						sure[i] = "未接收";

					} else if (sure[i].equals("1")) {
						sure[i] = "正在处理";
						red.setVisibility(View.VISIBLE);
					} else {
						sure[i] = "完结  " + grade[i];
						red.setVisibility(View.VISIBLE);
					}
					if (policename[i].equals("null")) {
						System.out.println("！！！！！！");
						policename[i] = "暂无";
					}
					if (myweizhi[i].equals(" ")) {
						myweizhi[i] = "未知";
					}
				}

			} else if (intent.getAction().equals("com.example.tongzhi")) {
				tongzhilength = intent.getIntExtra("tongzhilength", 0);
				id2 = intent.getStringArrayExtra("id2");
				tim1 = intent.getStringArrayExtra("tim1");
				titl = intent.getStringArrayExtra("titl");
				maintex = intent.getStringArrayExtra("maintex");
				System.out.println("tongzhilength==" + tongzhilength + tim1[0]);
			}
		}
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
