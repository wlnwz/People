package com.example.gongneng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.people.R;

public class Knowledge extends Activity{
	MyListAdapter myAdapter = null;
	private ListView listview;
	private TextView back,title;
	private String[] text,title1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.knowledge);
		back = (TextView) findViewById(R.id.return001);

		title1=this.getResources().getStringArray(R.array.title);
		text=this.getResources().getStringArray(R.array.text);

		listview = (ListView) findViewById(R.id.listview);
		myAdapter = new MyListAdapter(Knowledge.this, R.layout.knowledge_list);
		listview.setAdapter(myAdapter);
	
		//click the item and then go into knewledgetext to show the text.		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view,
					int position, long id) {
				// TODO Auto-generated method stub				
					Intent intent = new Intent(Knowledge.this,knowledgetext.class);
					intent.putExtra("title", title1[position]);
					intent.putExtra("text", text[position]);			
					startActivity(intent);			
			}
		});
		
		Onclick();
	}

	// back (finish this activity)
	void Onclick() {
		OnClickListener monclicklistener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.equals(back)) {
					Knowledge.this.finish();
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
			return 5;
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

				title = (TextView) convertView.findViewById(R.id.title_text);
				title.setText(title1[position]);
				
			}
			return convertView;
		}
	}

}
