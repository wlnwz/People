package com.example.gongneng;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.people.PageActivity;
import com.example.people.R;

public class More extends Activity{
	TextView title, time, alltext,back,voice,pict,voic;
	ImageView pic;
	Button cancel;
	String title1,time1,id,infor,picname,voicename,sure;
	URL url = null;
	HttpURLConnection httpurlconnection = null;
	InputStream data,voicedata;
	Bitmap bitmap;
	String picturePath;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		
		Intent intent = getIntent();
		id=intent.getExtras().getString("ID1");
		sure=intent.getExtras().getString("sure");
		infor=intent.getExtras().getString("infor");
		picname=intent.getExtras().getString("picname");
		voicename=intent.getExtras().getString("voicename");
		
		time = (TextView)findViewById(R.id.time123);
		alltext = (TextView)findViewById(R.id.text);
		back = (TextView)findViewById(R.id.return001);
		voice = (TextView)findViewById(R.id.voice);
		pict = (TextView)findViewById(R.id.ima);
		voic = (TextView)findViewById(R.id.vic);
		pic = (ImageView)findViewById(R.id.pic);
		cancel = (Button)findViewById(R.id.cancel);
	
		if((sure.equals("未接收"))||(sure.equals("正在处理"))){
			cancel.setVisibility(View.VISIBLE);
		    cancel.setEnabled(true);
		}
		// download the pic
		if(picname.equals("null")){	
			pict.setText("pic:   无图片");
		}else{
			Initallpic();
			pict.setText("pic:   "+picname);
		}
		if(voicename.equals("null")){
			voic.setText("voice: 无语音");
			voice.setBackgroundResource(R.drawable.about);
		}else{
			voic.setText("voice: "+voicename);
			voice.setBackgroundResource(R.drawable.voice1);			
		}	
		
		
		time.setText(time1);
		alltext.setText(infor);
		
		Onclick();
	}

	void Onclick() {
		OnClickListener monclicklistener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.equals(back)) {
					More.this.finish();
				}else if(v.equals(voice)){
					if(!voicename.equals("null")){
						Initvoice();
					}
				}
				else if (v.equals(cancel)){
					AlertDialog.Builder builder = new AlertDialog.Builder(More.this);
					builder.setIcon(R.drawable.police)
					.setTitle("确认取消此次报警？")
					.setMessage("您将要取消此案的登记记录，一旦取消，公安部门将不再受理此案件，请谨慎选择？")
					.setPositiveButton("Sure", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Delete();
						}
						
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub							
						}
					})
					.show();				
				}else if(v.equals(pic)){
					if(!picname.equals("null")){
						showpicture();
					}
				}
			}
		};
		back.setOnClickListener(monclicklistener);
		voice.setOnClickListener(monclicklistener);
		cancel.setOnClickListener(monclicklistener);
		pic.setOnClickListener(monclicklistener);
	}
	
	void showpicture(){
//		Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//		startActivityForResult(intent, 333);
		System.out.println("picpath+="+picturePath);
		Intent intent = new Intent(Intent.ACTION_VIEW);    
        intent.setDataAndType(Uri.parse("file://"+picturePath), "image/*");    
        startActivity(intent);
	}
	
//	protected void onActivityResult(int requestCode, int resultCode, Intent data)
//	 {
//	  if ((requestCode==333)&&(resultCode == Activity.RESULT_OK))
//	  { 
//		  
//	//这个uri，就是返回的选中的图片
//	   Uri uri = data.getData();
//	   Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
//	   cursor.moveToFirst();
//	   for (int i = 0; i < cursor.getColumnCount(); i++)
//	   {// 取得图片uri的列名和此列的详细信息
//	    System.out.println(i + "-" + cursor.getColumnName(i) + "-" + cursor.getString(i));
//	   }
//	  }
//	 }
	
	void Delete(){
		Thread deleteThread = new Thread(new DeleteThread());
		deleteThread.start();
		
	}
	
	void Initallpic(){
		Thread downThread = new Thread(new PicThread());
		downThread.start();
	}
	
	void Initvoice(){
		Thread downvoiceThread = new Thread(new VoiceThread());
		downvoiceThread.start();
	}
	
	class DeleteThread implements Runnable {
		@Override
		public void run() {
 
			try {
				// URL合法，但是这一步并不验证密码是否正确
				boolean Msg = deletebj(id);
				Message msg = handler3.obtainMessage();
				if (Msg) {					
						msg.what = 0;
						handler3.sendMessage(msg);
					
				} else {
					msg.what = 2;
					handler3.sendMessage(msg);
				}
			} catch (Exception e) {
				// 发生超时,返回值区别于null与正常信息
				e.printStackTrace();
			}
		}
	}

	
	class PicThread implements Runnable {
		@Override
		public void run() {
 
			try {
				// URL合法，但是这一步并不验证密码是否正确
				data = downpic();
				Message msg = handler.obtainMessage();
				if (data!=null) {
					
						msg.what = 0;
						handler.sendMessage(msg);
					
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

	class VoiceThread implements Runnable {
		@Override
		public void run() {
 
			try {
				// URL合法，但是这一步并不验证密码是否正确
				voicedata = downvoice();
				Message msg = handler2.obtainMessage();
				if (voicedata!=null) {
					
						msg.what = 0;
						handler2.sendMessage(msg);
					
				} else {
					msg.what = 2;
					handler2.sendMessage(msg);
				}
			} catch (Exception e) {
				// 发生超时,返回值区别于null与正常信息
				e.printStackTrace();
			}
		}
	}
	
	Handler handler3 = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:    						
				Toast.makeText(More.this, "取消报警成功", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(More.this, Detail.class);
				setResult(888, intent);
				More.this.finish();
				break;
			case 2:

				Toast.makeText(More.this, "取消失败", Toast.LENGTH_SHORT)
						.show();
				break;
	
			}
		}
	};
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				
				bitmap = BitmapFactory.decodeStream(data);		
				
				
	    			File file = new File("/sdcard/myImagea/");
	    			file.mkdirs();
	    			 FileOutputStream fout;
	    			 picturePath = "/sdcard/myImagea/" + picname;
	    			 System.out.println("picname =====" + picname);

	    			try {
	    				fout = new FileOutputStream(picturePath);
	    				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
	    				fout.close();

	    			} catch (FileNotFoundException e) {
	    				e.printStackTrace();
	    			} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			
	    			connectHanlder.sendEmptyMessage(0);
	    			
				Toast.makeText(More.this, "读取图片成功", Toast.LENGTH_SHORT)
						.show();
//				bitmap.recycle();
				
				break;
			case 2:

				Toast.makeText(More.this, "读取图片失败", Toast.LENGTH_SHORT)
						.show();
				break;
		
			}

		}
	};
	
	Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
			//	MediaRecorder mMediaRecorder01 = new MediaRecorder();
//				MediaPlayer mmediaplayer = new MediaPlayer();
				try {
//					mmediaplayer.setDataSource("http://219.245.67.1:8080/service/Voice/"+voicename);
//					mmediaplayer.setDataSource("http://219.245.65.5:8080/service/Voice/"+voicename);
//					mmediaplayer.prepare();
//					mmediaplayer.start();
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalStateException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				  
				
				File file = new File("/sdcard/myVoice2/");
	    			file.mkdirs();
	    			// create the file
	    			String voicePath = "/sdcard/myVoice2/" + voicename;
	    			// 把数据写入文件
	    			try {
	    				new FileOutputStream(voicePath);

	    			} catch (FileNotFoundException e) {
	    				e.printStackTrace();
	    			}
				
				Toast.makeText(More.this, "读取声音成功", Toast.LENGTH_SHORT)
						.show();
				
				break;
			case 1:

				Toast.makeText(More.this, "读取失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:

				Toast.makeText(More.this, "服务器连接失败！", Toast.LENGTH_SHORT)
						.show();
				break;
			}

		}
	};
	
	
	private Handler connectHanlder = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
          
            // 更新UI，显示图片  
            if (bitmap != null) {  
                pic.setImageBitmap(bitmap);// display image  
                System.out.println("!!!!!!!!!1");
                            
                
            }  
        }  
    };  
	
	private InputStream downpic() {
		// 使用apache HTTP客户端实现
		String urlStr1 = "http://"+PageActivity.urltest+":8080/service/Image/";
		//	String urlStr1 = "http://219.245.65.5:8080/service/Image/";
		String urlStr=urlStr1+picname;
	
		try {
			data = getImageStream(urlStr);			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	
	}
	 
	 public InputStream getImageStream(String path) throws Exception{  
	        URL url = new URL(path);  
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	        conn.setConnectTimeout(5 * 1000);  
	        conn.setRequestMethod("GET");  
	        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){  
	            return conn.getInputStream();  
	        }  
	        return null;  
	    }  
	 
	 private InputStream downvoice() {
		
			// 使用apache HTTP客户端实现
			String urlStr1 = "http://"+PageActivity.urltest+":8080/service/Voice/";
  //	    String urlStr1 = "http://219.245.65.5:8080/service/Voice/";
			String urlStr=urlStr1+voicename;
		
			try {
				voicedata = getVoiceStream(urlStr);
				MediaPlayer mmediaplayer = new MediaPlayer();
				mmediaplayer.setDataSource(urlStr1+voicename);
				mmediaplayer.prepare();
				mmediaplayer.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return voicedata;
		
		}
		 
		 public InputStream getVoiceStream(String path) throws Exception{  
		        URL url = new URL(path);  
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
		        conn.setConnectTimeout(5 * 1000);  
		        conn.setRequestMethod("GET");  
		        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){  
		            return conn.getInputStream();  
		        }  
		        return null;  
		    }  
		
		 private boolean deletebj(String id) {
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
					dos.writeUTF("delete");
					dos.writeUTF(id);
					dos.flush();
					dos.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
				
					DataInputStream dis = new DataInputStream(
							httpurlconnection.getInputStream());
					String successornot = dis.readUTF();
					if(successornot.equals("success")){
					 loginValidate = true;
					 }
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return loginValidate;
			}
}
