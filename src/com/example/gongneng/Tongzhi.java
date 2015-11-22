package com.example.gongneng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.people.R;

public class Tongzhi extends Activity {
	MyListAdapter myAdapter = null;
	private ListView listview;
	boolean a = true;
	private String[] tim1, titl, maintex,id2;
	private int tongzhilength = 0;
	private TextView back,time1, time2, title, maintext, showmain;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tongzhi);

		tim1 = new String[tongzhilength];
		titl = new String[tongzhilength];
		maintex = new String[tongzhilength];

		
		// get the note(time,title,precontent)
		Intent intent = getIntent();
		tongzhilength = intent.getExtras().getInt("tongzhilength");
		id2 = intent.getExtras().getStringArray("id2");
		tim1 = intent.getExtras().getStringArray("tim1");
		titl = intent.getExtras().getStringArray("titl");
		maintex = intent.getExtras().getStringArray("maintex");
		System.out.println("maintex" +tim1[0]+titl[0]+ maintex[0]);

		back = (TextView) findViewById(R.id.return001);
		listview = (ListView) findViewById(R.id.list1);
		myAdapter = new MyListAdapter(Tongzhi.this, R.layout.list_item);
		listview.setAdapter(myAdapter);

		Onclick();
	}

	void Onclick() {
		OnClickListener monclicklistener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.equals(back)) {
					Tongzhi.this.finish();
				}
			}
		};
		back.setOnClickListener(monclicklistener);
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
			return tongzhilength;
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
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						mTextViewResourceID, null);			
			}
			
			time1 = (TextView) convertView.findViewById(R.id.show123);
			time2 = (TextView) convertView.findViewById(R.id.array_text2);
			title = (TextView) convertView.findViewById(R.id.title_text);
			maintext = (TextView) convertView.findViewById(R.id.main_text); 
		
		     String time123=tim1[position].substring(5,11);

			time1.setText(time123);
			time2.setText(tim1[position]);
			title.setText(titl[position]);
			maintext.setText(maintex[position]);

			//goto the Alltext to show the content
			showmain = (TextView) convertView.findViewById(R.id.showmain);
			showmain.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Tongzhi.this,Alltext.class);
					intent.putExtra("id2", id2[position]);
					intent.putExtra("title", titl[position]);
					intent.putExtra("time", tim1[position]);
					intent.putExtra("alltext", maintex[position]);
					startActivity(intent);

				}
			});
			
			return convertView;
		}
	}

}
