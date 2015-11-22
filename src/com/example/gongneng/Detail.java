package com.example.gongneng;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.people.R;

public class Detail extends Activity {

	private String sure, ID, policename, ptelphone, time, overtime,infor,picname,voicename;
	private TextView call, receive, waiting, finish, record, state, back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);

		Intent intent = getIntent();
		sure = intent.getExtras().getString("sure");
		ID = intent.getExtras().getString("ID1");
		policename = intent.getExtras().getString("policename");
		ptelphone = intent.getExtras().getString("ptelphone");
		time = intent.getExtras().getString("time");
		overtime = intent.getExtras().getString("overtime");
		infor = intent.getExtras().getString("infor");
		picname = intent.getExtras().getString("picname");
		voicename = intent.getExtras().getString("voicename");

		Init();

		detail();

		Onclick();
	}

	private void Init() {
		back = (TextView) findViewById(R.id.return001);
		state = (TextView) findViewById(R.id.state);
		call = (TextView) findViewById(R.id.call123);
		receive = (TextView) findViewById(R.id.receive123);
		waiting = (TextView) findViewById(R.id.waiting123);
		finish = (TextView) findViewById(R.id.finish123);
		record = (TextView) findViewById(R.id.record123);

	}

	private void detail() {
		if (sure.equals("δ����")) {
			state.setText("δ����");
			call.setTextColor(getResources().getColor(R.color.red));

		} else if (sure.equals("���ڴ���")) {
			state.setText("�ڴ���");
			waiting.setVisibility(View.VISIBLE);
			receive.setText("���ڴ���...");
			waiting.setTextColor(getResources().getColor(R.color.red));

		} else if (sure.equals("���  0")) {
			state.setText("δ����");
			receive.setText("���ڴ���...");
			finish.setText("�ȴ�����~");
			waiting.setVisibility(View.VISIBLE);
			finish.setVisibility(View.VISIBLE);
			record.setVisibility(View.VISIBLE);
			record.setTextColor(getResources().getColor(R.color.red));

		} else {
			state.setText("������");
			receive.setText("���ڴ���...");
			record.setText("�����ۣ�");
			waiting.setVisibility(View.VISIBLE);
			finish.setVisibility(View.VISIBLE);
			record.setVisibility(View.VISIBLE);

		}

	}

	private void Onclick() {

		OnClickListener monclicklistener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.equals(back)) {
					
					Intent intent = new Intent(Detail.this, Myrecord.class);
					intent.putExtra("level", sure);
					setResult(999, intent);
					Detail.this.finish();
					
					Detail.this.finish();
				} else if (v.equals(receive)) {
					if (sure.equals("δ����")) {
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								Detail.this);
						builder.setTitle("������Ϣ")
								.setMessage(
										"��Ա��" + policename + "\n" + "�绰��"
												+ ptelphone + "\n" + "ʱ�䣺"
												+ time).show();
					}

				} else if (v.equals(call)) {					
						Intent intent = new Intent(Detail.this,More.class);
						intent.putExtra("ID1", ID);
						intent.putExtra("sure", sure);
						intent.putExtra("infor", infor);
						intent.putExtra("picname", picname);
						intent.putExtra("voicename", voicename);
						startActivityForResult(intent,888);						

				} else if (v.equals(finish)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							Detail.this);
					builder.setTitle("�᰸��Ϣ")
							.setMessage(
									"��Ա��" + policename + "\n" + "�᰸ʱ�䣺"
											+ overtime).show();

				} else if (v.equals(record)) {
					if (sure.equals("���  0")) {
						{

							Intent intent = new Intent(Detail.this,
									Pingfen.class);
							intent.putExtra("ID1", ID);
							intent.putExtra("policename", policename);
							intent.putExtra("ptelphone", ptelphone);
							intent.putExtra("time", time);
							intent.putExtra("overtime", overtime);
							startActivityForResult(intent, 456);
						}

					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								Detail.this);
						builder.setTitle("������Ϣ")
								.setMessage(
										"��Ա��" + policename + "\n" + "���֣�"
												+ sure).show();
					}
				}
			}
		};
		back.setOnClickListener(monclicklistener);
		receive.setOnClickListener(monclicklistener);
		call.setOnClickListener(monclicklistener);
		finish.setOnClickListener(monclicklistener);
		record.setOnClickListener(monclicklistener);

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (resultCode == 456) {

			Bundle extras = data.getExtras();
			sure = extras.getString("level");
			detail();

		}if(resultCode==888){
			
			Intent intent = new Intent(Detail.this, Myrecord.class);
			intent.putExtra("level", sure);
			setResult(999, intent);
			Detail.this.finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK )  
        {           
        	Intent intent = new Intent(Detail.this, Myrecord.class);
			intent.putExtra("level", sure);
			setResult(999, intent);
			Detail.this.finish();

        }  
          
        return false;  
          
    } 

}
