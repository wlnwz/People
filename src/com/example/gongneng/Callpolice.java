package com.example.gongneng;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.people.PageActivity;
import com.example.people.R;
import com.example.people.R.color;

public class Callpolice extends Activity {
	private TextView center;
	private TextView lb, choose, time, photo, view1;
	private ImageView img, img1;
	private EditText edittext;
	private Button edit;
	private boolean a = true, b = true;
	private LinearLayout linear;
	private RelativeLayout linear2;
	private String date, date1, weizhi, weizhi1, picturePath = "null", bjinfor,
			name, phone, ID, name1;
	private double location1, location2, location01, location02;
	private int Photocode = 1, Cameracode = 2, REQUEST_TIMEOUT = 5000,
			SO_TIMEOUT = 10000;
	private int voiceornot = 1;
	private String responseMsg = "", voicePath = "null";
	private Vibrator vibrator = null;
	private MediaRecorder mMediaRecorder01;
	private File myRecAudioFile;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.callpolice);
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		weizhi = intent.getStringExtra("weizhi");
		phone = intent.getStringExtra("phone");
		ID = intent.getStringExtra("ID");
		location1 = intent.getDoubleExtra("location1", 0);
		location2 = intent.getDoubleExtra("location2", 0);

		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
		date = sDateFormat.format(new java.util.Date());

		System.out.println("Id====@@@@@@" + ID);
		if (ID == null) {
			ID = "0";
		}
		Init();
		Onclick();

	}

	void Init() {
		center = (TextView) findViewById(R.id.center);
		lb = (TextView) findViewById(R.id.abc);
		edit = (Button) findViewById(R.id.edit);
		linear = (LinearLayout) findViewById(R.id.linear);
		linear2 = (RelativeLayout) findViewById(R.id.linear2);
		choose = (TextView) findViewById(R.id.choose);
		time = (TextView) findViewById(R.id.time);
		photo = (TextView) findViewById(R.id.photo);
		view1 = (TextView) findViewById(R.id.view);
		img = (ImageView) findViewById(R.id.showphoto);
		edittext = (EditText) findViewById(R.id.edittext);
		img1 = (ImageView) findViewById(R.id.voice);

		weizhi1 = " ";
		location01 = 0;
		location02 = 0;
		choose.setText("@");
		time.setText("T");

	}

	/*
	 * If we want to send the text or image to polices click the button(lb) and
	 * then we can edit what you want.If we want send our voice, we should click
	 * the button(edit). First the image or voice will be saved in our SD-card
	 * and then if we want send them away, just click the button.
	 */
	void Onclick() {
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (view.equals(lb)) {
					if (a == true) {
						linear.setVisibility(View.VISIBLE);
						edit.setText("发送");

						a = false;
					} else {
						linear.setVisibility(View.GONE);
						edit.setText("语音");
						a = true;
					}
				} else if (view.equals(choose)) {
					String zx = "@预警中心";
					edittext.setText(edittext.getText() + "" + zx);

				} else if (view.equals(time)) {
					Editable te1 = edittext.getText();
					edittext.setText(date + " " + te1);

				} else if (view.equals(photo)) {
					// use the camera

					File file = new File("/sdcard/myImage/");
					if (!file.exists()) {
						file.mkdirs();
					}
					new DateFormat();
					SimpleDateFormat sDateFormat = new SimpleDateFormat(
							"yyyy-MM-dd-HHmmss");
					String date = sDateFormat.format(new java.util.Date());
					name1 = ID + "_" + date + ".jpg";
					picturePath = "/sdcard/myImage/" + name1;
					File file2 = new File(picturePath);

					Uri u = Uri.fromFile(file2);

					Intent mintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					mintent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
					mintent.putExtra(MediaStore.EXTRA_OUTPUT, u);
					startActivityForResult(mintent, Cameracode);

				} else if (view.equals(view1)) {
					if (b == true) {
						weizhi1 = weizhi;
						view1.setBackgroundResource(color.white);
						view1.setTextColor(Color.BLACK);
						view1.setText("< " + weizhi);
						b = false;
					} else {
						view1.setBackgroundResource(color.bg_color_deep);
						view1.setTextColor(color.gray);
						view1.setText(">显示所在位置 ");
						weizhi1 = "";
						b = true;
					}

				} else if (view.equals(edit)) {

					SimpleDateFormat sDateFormat = new SimpleDateFormat(
							"yyyy-MM-dd-HHmmss");
					date1 = sDateFormat.format(new java.util.Date());

					if (edit.getText().toString().equals("语音")) {
						if (voiceornot == 1) {
							lb.setEnabled(false);
							Toast.makeText(Callpolice.this, "按住语音报警",
									Toast.LENGTH_SHORT).show();

							if (!Environment.getExternalStorageState().equals(
									android.os.Environment.MEDIA_MOUNTED)) {
								Toast.makeText(Callpolice.this,
										"SD卡不存在，请插入SD卡！", 5000).show();
								return;
							}

							Toast.makeText(Callpolice.this, "按住语音报警",
									Toast.LENGTH_SHORT).show();
							vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
							vibrator.vibrate(200);

							linear2.setVisibility(View.VISIBLE);
							img1.setBackgroundResource(R.drawable.voice1);
							img1.setVisibility(View.VISIBLE);

							myRecAudioFile = new File("/sdcard/myVoice");
							if (!myRecAudioFile.exists()) {
								myRecAudioFile.mkdirs();
							}
							if (name != null) {
								voicePath = "/sdcard/myVoice/" + ID + "_"
										+ date1 + ".amr";
							} else {
								voicePath = "/sdcard/myVoice/" + "null_"
										+ date1 + ".amr";
							}

							mMediaRecorder01 = new MediaRecorder();

							mMediaRecorder01
									.setAudioSource(MediaRecorder.AudioSource.MIC);
							mMediaRecorder01
									.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

							mMediaRecorder01
									.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
							mMediaRecorder01.setOutputFile(voicePath);

							try {
								mMediaRecorder01.prepare();
							} catch (IllegalStateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							mMediaRecorder01.start();
							voiceornot = 0;
						} else {
							vibrator.vibrate(150);
							linear2.setVisibility(View.GONE);
							img1.setVisibility(View.GONE);
							if (myRecAudioFile != null) {
								/* stop */
								mMediaRecorder01.stop();
								mMediaRecorder01.release();
								mMediaRecorder01 = null;
								img1.setVisibility(View.GONE);
								AlertDialog.Builder builder = new AlertDialog.Builder(
										Callpolice.this);
								builder.setIcon(R.drawable.voice1);
								builder.setTitle("voice callalarm");
								builder.setMessage("save and send ？");
								builder.setPositiveButton("send ",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {

												weizhi1 = weizhi;
												location01 = location1;
												location02 = location2;

												uploadinfor();
												upvoice();

											}
										});
								builder.setNegativeButton("cancel",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												File file = new File(voicePath);
												try {
													if (file.isFile()
															&& file.exists()) {
														file.delete();
													}
												} catch (Exception e) {
													Toast.makeText(
															Callpolice.this,
															"unnormal",
															Toast.LENGTH_LONG)
															.show();
												}
											}
										});
								builder.show();

							}
							voiceornot = 1;
							lb.setEnabled(true);
						}
					} else {
						send();
					}

				} else if (view.equals(center)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							Callpolice.this);
					builder.setIcon(R.drawable.hx);
					builder.setTitle("-陕西省预警中心-");
					builder.setMessage("这里是陕西省预警中心，提供24小时的情报汇聚，预警通报等服务。"
							+ "\r\n" + "        联系电话：18710849414");
					// PositiveButton
					builder.setPositiveButton("call",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent();
									intent.setAction(Intent.ACTION_CALL);
									intent.setData(Uri.parse("tel:"
											+ "18710849414"));
									startActivity(intent);
								}
							});
					// NegativeButton
					builder.setNegativeButton("cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					builder.show();
				}
			}
		};
		lb.setOnClickListener(onClickListener);
		edit.setOnClickListener(onClickListener);
		center.setOnClickListener(onClickListener);
		choose.setOnClickListener(onClickListener);
		time.setOnClickListener(onClickListener);
		view1.setOnClickListener(onClickListener);
		photo.setOnClickListener(onClickListener);

	}

	// we can choose whether show our location or not.
	void send() {
		bjinfor = edittext.getText().toString();
		if (bjinfor == null) {
			bjinfor = "无";
		}
		if (view1.getText().toString().equals(">显示所在位置 ")) {
			location01 = 0;
			location02 = 0;
		} else {
			location01 = location1;
			location02 = location2;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(Callpolice.this);
		builder.setIcon(R.drawable.hx);
		builder.setTitle("-Auto Callalarm-");
		builder.setMessage("it will be sent to the alarm-center!");
		builder.setPositiveButton("sure",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(Callpolice.this, "sending...",
								Toast.LENGTH_LONG).show();
						uploadinfor();
						if (picturePath != null) {
							upload2pic();
						}
					}
				});
		builder.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		builder.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Cameracode) {
			switch (resultCode) {
			case Activity.RESULT_OK:
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
					Log.v("TestFile",
							"SD card is not avaiable/writeable right now.");
					return;
				}

//				Bundle bundle = data.getExtras();
				// get the date form camera and transform to Bitmap
//				Bitmap bitmap = (Bitmap) bundle.get("data");
//				FileOutputStream fout = null;

				// create the file
				// picturePath = "/sdcard/myImage/" + name1;
				// 把数据写入文件
//				try {
//					fout = new FileOutputStream(picturePath);
//					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
//
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				} finally {
//					try {
//						fout.flush();
//						fout.close();
//						Bitmap bm = BitmapFactory.decodeFile(picturePath);
//						// show the picture
//						img.setImageBitmap(bm);
//
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
				 File f=new File(picturePath);
				 try { 
					   Uri u = 
					   Uri.parse(android.provider.MediaStore.Images.Media.insertImage(getContentResolver(), 
					   f.getAbsolutePath(), null, null)); 
					int degree =  getExifOrientation(picturePath);
					
					Bitmap bitmap = BitmapFactory.decodeFile(picturePath);;//压缩图片  
					if(Math.abs(degree) > 0){  
					    bitmap = rotaingImageView(degree, bitmap);//旋转图片  
					}
					
					FileOutputStream fout = null;
					fout = new FileOutputStream(picturePath);
					int level = compressImage(bitmap);
					bitmap.compress(Bitmap.CompressFormat.JPEG, level, fout);
									
					fout.flush();
					fout.close();
					
					
					
					img.setImageBitmap(bitmap);
				//	bitmap.recycle();
				 }catch (FileNotFoundException e) { 
					   // TODO Auto-generated catch block 
					   e.printStackTrace(); 
					} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				 
				break;

			case Activity.RESULT_CANCELED:
				Intent picture = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(picture, Photocode);
				break;
			}
		}
		if (requestCode == Photocode) {
			switch (resultCode) {
			case Activity.RESULT_OK: {
				Uri uri = data.getData();
				String[] filePathColumns = { MediaStore.Images.Media.DATA };
				Cursor cursor = Callpolice.this.getContentResolver().query(uri,
						null, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
				picturePath = cursor.getString(columnIndex);
				cursor.close();
				if (picturePath != null) {
					File file = new File(picturePath);
					if (file.exists()) {
						Bitmap bm = BitmapFactory.decodeFile(picturePath);

						// show the picture
						img.setImageBitmap(bm);
					}
					break;
				} else {
					Toast.makeText(Callpolice.this, "choose again!",
							Toast.LENGTH_SHORT).show();
				}
			}
				break;
			case Activity.RESULT_CANCELED:
				break;
			}
		}
	}
	
	
	public static int compressImage(Bitmap image) {  
		  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
	    int options = 100;  
	    while ( (baos.toByteArray().length / 1024)>200) {  //循环判断如果压缩后图片是否大于200kb,大于继续压缩  
	        baos.reset();//重置baos即清空baos 
	        options -= 20;//每次都减少10 
	        image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  	         
	    System.out.println("@@@@@@@@baos.bytearray=="+(baos.toByteArray().length / 1024));
	    }  	    
//	    ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
//	    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
	    return options;  
	}
	
	public static int getExifOrientation(String filepath) {  
	    int degree = 0;  
	    ExifInterface exif = null;  
	    try {  
	        exif = new ExifInterface(filepath);  
	    } catch (IOException ex) {  
	        Log.d("Tag", "cannot read exif" + ex);  
	    }  
	    if (exif != null) {  
	    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);  
	        if (orientation != -1) {  
	            switch(orientation) {  
	                case ExifInterface.ORIENTATION_ROTATE_90:  
	                    degree = 90;  
	                    break;  
	                case ExifInterface.ORIENTATION_ROTATE_180:  
	                    degree = 180;  
	                    break;  
	                case ExifInterface.ORIENTATION_ROTATE_270:  
	                    degree = 270;  
	                    break;  
	            }  
	        }  
	    }  
	    return degree;  
	}  
	
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {  
	    //旋转图片 动作  
	    Matrix matrix = new Matrix();;  
	    matrix.postRotate(angle);  
	    System.out.println("angle2=" + angle);  
	    // 创建新的图片  
	    Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
	            bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
	    return resizedBitmap;  
	}

	void uploadinfor() {
		Thread upload = new Thread(new UpThreadinfor());
		upload.start();
		try {
			upload.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void upload2pic() {
		Thread upload = new Thread(new UpThread2pic());
		upload.start();
	}

	void upvoice() {
		Thread upload = new Thread(new UpThread3voice());
		upload.start();

	}

	class UpThreadinfor implements Runnable {

		@Override
		public void run() {
			boolean upValidate = upServerinfor();
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
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(Callpolice.this, "send success",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				// registerDialog.cancel();
				Toast.makeText(getApplicationContext(), "send failed",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				// registerDialog.cancel();
				Toast.makeText(getApplicationContext(), "connect failed！",
						Toast.LENGTH_SHORT).show();
				break;
			}

		}
	};

	private boolean upServerinfor() {
		boolean loginValidate = false;
		// use HTTP
		 String urlStr = "http://"+PageActivity.urltest+":8080/service/servlet/upServlet";
	//	String urlStr = "http://219.245.65.5:8080/service/servlet/upServlet";
		HttpPost request = new HttpPost(urlStr);
		// transmit parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// add parameters
		params.add(new BasicNameValuePair("ID", String.valueOf(ID)));
		params.add(new BasicNameValuePair("NAME", String.valueOf(name)));
		params.add(new BasicNameValuePair("DATE", String.valueOf(date1)));
		params.add(new BasicNameValuePair("BJ", (bjinfor)));
		params.add(new BasicNameValuePair("WDU", String.valueOf(location01)));
		params.add(new BasicNameValuePair("JDU", String.valueOf(location02)));
		params.add(new BasicNameValuePair("WZ", String.valueOf(weizhi1)));
		params.add(new BasicNameValuePair("phone", phone));
		params.add(new BasicNameValuePair("picturename",
				String.valueOf(picturePath.substring(picturePath
						.lastIndexOf("/") + 1))));
		params.add(new BasicNameValuePair("voicename", String.valueOf(voicePath
				.substring(voicePath.lastIndexOf("/") + 1))));

		System.out.println("params" + params);
		try {
			System.out.println("response??");
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient client = getHttpClient();

			HttpResponse response = client.execute(request);
			System.out.println("response" + response);

			if (response.getStatusLine().getStatusCode() == 200) {
				loginValidate = true;
				// get the response
				responseMsg = EntityUtils.toString(response.getEntity());
				System.out.println("responseMsg" + responseMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginValidate;
	}

	class UpThread2pic implements Runnable {

		@Override
		public void run() {
			boolean upValidate = upServer2pic();
			Message msg = handler2.obtainMessage();
			if (upValidate) {
				if (true) {
					msg.what = 0;
					handler2.sendMessage(msg);
				} else {
					msg.what = 1;
					handler2.sendMessage(msg);
				}
			} else {
				msg.what = 2;
				handler2.sendMessage(msg);
			}
		}
	}

	Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			System.out.println("handler");
			switch (msg.what) {
			case 0:
				Toast.makeText(Callpolice.this, "picture success", 1).show();
				break;
			case 1:

				break;
			case 2:

				break;
			}

		}
	};

	private boolean upServer2pic() {
		boolean loginValidate = false;
		System.out.println("b!!!!!!!!r");
		 String uploadUrl ="http://"+PageActivity.urltest+":8080/service/servlet/UploadServlet";
		 //	String uploadUrl = "http://219.245.65.5:8080/service/servlet/UploadServlet";
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream(
					httpURLConnection.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
					+ picturePath.substring(picturePath.lastIndexOf("/") + 1)
					+ "\"" + end);
			dos.writeBytes(end);

			FileInputStream fis = new FileInputStream(picturePath);
			
			byte[] buffer = new byte[819200]; // 800k
			int count = 0;
			while ((count = fis.read(buffer)) != -1) {
				System.out.println("buffer" + count);
				dos.write(buffer, 0, count);
			}
			fis.close();

			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			InputStream is = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String result = br.readLine();

			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			loginValidate = true;
			/* close DataOutputStream */
			System.out.println(b.toString().trim());
			dos.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginValidate;
	}

	class UpThread3voice implements Runnable {
		@Override
		public void run() {
			// URL合法，但是这一步并不验证密码是否正确
			boolean upValidate = upServervoice();
			Message msg = handler3.obtainMessage();
			if (upValidate) {
				if (true) {
					msg.what = 0;
					handler3.sendMessage(msg);
				} else {
					msg.what = 1;
					handler3.sendMessage(msg);
				}
			} else {
				msg.what = 2;
				handler3.sendMessage(msg);
			}
		}
	}

	Handler handler3 = new Handler() {
		public void handleMessage(Message msg) {
			System.out.println("handler");
			switch (msg.what) {
			case 0:
				// registerDialog.cancel();
				Toast.makeText(Callpolice.this, "send success", 1).show();
				break;
			case 1:

				break;
			case 2:

				break;
			}

		}
	};

	private boolean upServervoice() {
		boolean loginValidate = false;
		System.out.println("b!!!!!!!!r");
		 String uploadUrl = "http://"+PageActivity.urltest+":8080/service/servlet/upvoiceServlet";
	//	String uploadUrl = "http://219.245.65.5:8080/service/servlet/upvoiceServlet";
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream(
					httpURLConnection.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
					+ voicePath.substring(voicePath.lastIndexOf("/") + 1)
					+ "\"" + end);
			dos.writeBytes(end);

			FileInputStream fis = new FileInputStream(voicePath);
			byte[] buffer = new byte[819200]; // 800k
			int count = 0;
			while ((count = fis.read(buffer)) != -1) {
				System.out.println("buffer" + count);
				dos.write(buffer, 0, count);
			}
			fis.close();

			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			InputStream is = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String result = br.readLine();

			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			loginValidate = true;
			/* 关闭DataOutputStream */
			System.out.println(b.toString().trim());
			dos.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginValidate;
	}

	// 初始化HttpClient，并设置超时
	public HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Callpolice.this.finish();

	}
	
	@Override 
    public void onConfigurationChanged(Configuration config) { 
    super.onConfigurationChanged(config); 
    } 

}
