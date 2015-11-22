package com.example.gongneng;

import com.example.people.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class knowledgetext extends Activity{
	TextView title,alltext,back;
	String title1,alltext1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.knowledgetext);
		
		Intent intent = getIntent();
		title1=intent.getExtras().getString("title");
		alltext1=intent.getExtras().getString("text");
		
		title = (TextView)findViewById(R.id.title);
		alltext = (TextView)findViewById(R.id.alltext);
		back = (TextView)findViewById(R.id.return001);
		
		title1 =title1.substring(2);
		// show the title and text		
		title.setText(title1);
		alltext.setText(alltext1);
		Onclick();
	}

	void Onclick() {
		OnClickListener monclicklistener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.equals(back)) {
					knowledgetext.this.finish();
				}
			}
		};
		back.setOnClickListener(monclicklistener);
	}
}
