package com.example.people;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;

import com.example.gongneng.Detail;
import com.example.gongneng.Myrecord;
import com.example.slidingmenu.view.LeftFragment;
import com.example.slidingmenu.view.LeftFragment.namechuanzhi;
import com.example.slidingmenu.view.SlidingMenu;
import com.example.slidingmenu.view.ViewPageFragment;
import com.example.slidingmenu.view.ViewPageFragment.MyPageChangeListener;

public class MaindemoActivity extends FragmentActivity implements namechuanzhi {
	SlidingMenu mSlidingMenu;
	LeftFragment leftFragment;
	ViewPageFragment viewPageFragment;
	private String username;
	public static Activity MaindemoActivity;
	int REQUEST_CODE = 100;
	boolean internetavaliable;
	private boolean czjc = true;
	
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	protected void onCreate(Bundle v) {
		super.onCreate(v);
		setContentView(R.layout.main);
		MaindemoActivity = this;


		init();
		initListener();

		internetavaliable = isNetworkAvailable(this);
		if (internetavaliable == true) {
		} else {
			Toast.makeText(MaindemoActivity.this, "您的网络连接不正常，请重新连接！",
					Toast.LENGTH_SHORT).show();
		}

		
		
	}
	
	private void init() {
		mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(
				R.layout.left_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(
				R.layout.center_frame, null));

		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		leftFragment = new LeftFragment();
		t.replace(R.id.left_frame, leftFragment);

		viewPageFragment = new ViewPageFragment();
		t.replace(R.id.center_frame, viewPageFragment);

		System.out.println("username!!!!!" + username);

		// Bundle bundle1 = new Bundle();
		// bundle1.putString("NA", name);
		// leftFragment.setArguments(bundle1);

		t.commit();
	}

	private void initListener() {
		viewPageFragment.setMyPageChangeListener(new MyPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (viewPageFragment.isFirst()) {
					mSlidingMenu.setCanSliding(false, false);
				} else if (viewPageFragment.isEnd()) {
					mSlidingMenu.setCanSliding(false, true);
				} else {
					mSlidingMenu.setCanSliding(false, false);
				}
			}
		});
	}

	public void showLeft() {
		mSlidingMenu.showLeftView();
	}

	public void showRight() {
		mSlidingMenu.showRightView();
	}

	// 回调接口，传值
	public void onNamechoose(String name) {
		username = name;
		System.out.println("username11==" + username);
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		} else {
			NetworkInfo networkinfo = cm.getActiveNetworkInfo();
			if (networkinfo == null || !networkinfo.isAvailable()) {
				return false;
			}
			return true;
		}
	}
	
	private long exitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-exitTime) > 2000){  
	            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
	            exitTime = System.currentTimeMillis();   
	        } else {
	        	czjc = false;
	            finish();
	            System.exit(0);
	        }
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
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
		
	}
	
}
