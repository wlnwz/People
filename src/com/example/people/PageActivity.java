package com.example.people;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

public class PageActivity extends Activity {
	private AnimationDrawable ad;
	private ImageView mimageview;
	private final int SPLASH_DISPLAY_LENGHT = 2000;
	Button mbutton;
//	public static String urltest ="192.168.253.1";219.245.64.1;172.27.35.2
	public static String urltest ="219.245.64.1";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mimageview = (ImageView) findViewById(R.id.jc);
		mimageview.setImageResource(R.anim.donghua);
		ad = (AnimationDrawable) mimageview.getDrawable();
		ad.start();

		new Handler().postDelayed(new Runnable() {
			public void run() {

				Intent mainIntent = new Intent(PageActivity.this,
						MaindemoActivity.class);
				PageActivity.this.startActivity(mainIntent);
				PageActivity.this.finish();
			}

		}, SPLASH_DISPLAY_LENGHT);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		// ÍË³ö
		if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
			finish();
		}
	}

}
