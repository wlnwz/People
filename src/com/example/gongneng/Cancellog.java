package com.example.gongneng;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.people.MaindemoActivity;
import com.example.people.R;

public class Cancellog extends Activity {
	TextView name, telp,back;
	Button cancel;
	String username, telphone, ID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cancellog);

		Intent intent = getIntent();
		username = intent.getExtras().getString("username");
		telphone = intent.getExtras().getString("telphone");
		ID = intent.getExtras().getString("ID");

		Init();
		Initclick();
	}

	void Init() {
		name = (TextView) findViewById(R.id.userinfor);
		telp = (TextView) findViewById(R.id.telinfor);
		cancel = (Button) findViewById(R.id.cancelbutton);
		back = (TextView) findViewById(R.id.back);
		name.setText(username);
		telp.setText("µç»°£º"+telphone);
	}

	void Initclick() {
		OnClickListener mOnclickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.equals(cancel)) {
					username = "ÇëµÇÂ¼";
					telphone = "";
					ID = "0";
					LoginActivity.sp.edit().putBoolean("AUTO_ISCHECK", false).commit();		
					Intent intent = new Intent(Cancellog.this,
							MaindemoActivity.class);

					intent.putExtra("NAME", username);
					intent.putExtra("phone", telphone);
					intent.putExtra("ID", ID);
					// return intent
					setResult(100, intent);
					Cancellog.this.finish();
				}else if(v.equals(back)){
					Cancellog.this.finish();
				}
			}

		};
		cancel.setOnClickListener(mOnclickListener);
		back.setOnClickListener(mOnclickListener);
	}
}
