package com.example.gongneng;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.people.MaindemoActivity;
import com.example.people.R;

public class gnActivity extends Activity implements OnGetShareUrlResultListener,
                                   OnGetPoiSearchResultListener, OnGetSuggestionResultListener {
	private ShareUrlSearch mShareUrlSearch = null;
	private ImageView share, search;
	private String weizhi,city;
	private double location1, location2;
	private LatLng ll;
	private AutoCompleteTextView keyWorldsView = null;
	private ArrayAdapter<String> sugAdapter = null;
	private SuggestionSearch mSuggestionSearch = null;
	private PoiSearch mPoiSearch = null;
	EditText search2;
	int a = 1;
	private int load_Index = 0;
	private static StringBuilder sb1;
	int posi;
	MyListAdapter myAdapter = null;
	private ListView listview;
	private String[]  sb,sb3;
	private LatLng[] wz;
	private final String BROADCAST_ACTION = "com.example.broadcast.gnactivity";
	private RelativeLayout relativelayout;
	@SuppressLint("CutPasteId")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gn);

		Intent intent = getIntent();
		city = intent.getStringExtra("city");
		weizhi = intent.getStringExtra("weizhi");
		location1 = intent.getDoubleExtra("location1", 0);
		location2 = intent.getDoubleExtra("location2", 0);
		ll = new LatLng(location1, location2);

		mShareUrlSearch = ShareUrlSearch.newInstance();
		mShareUrlSearch.setOnGetShareUrlResultListener(this);

		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		
		relativelayout=(RelativeLayout)findViewById(R.id.liear2);
		share = (ImageView) findViewById(R.id.share1);
		search = (ImageView) findViewById(R.id.search);
		search2 = (EditText) findViewById(R.id.edittext);
		search2.setTextSize(26);
		keyWorldsView = (AutoCompleteTextView) findViewById(R.id.edittext); 
		sugAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line);
		keyWorldsView.setAdapter(sugAdapter);
		listview = (ListView) findViewById(R.id.list1);
		
		/**
		 * 当输入关键字变化时，动态更新建议列表
		 */
		keyWorldsView.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.length() <= 0) {
					return;
				}

				String city1 = city;
				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city(city1));
			}
		});

		Initclick();
	}

	void Initclick() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.equals(share)) {
					mShareUrlSearch
							.requestLocationShareUrl(new LocationShareURLOption()
									.location(ll).snippet("您好:测试定位")
									.name("市民："));

				} else if (v.equals(search)) {
					if (a == 1) {
						relativelayout.setVisibility(View.VISIBLE);
						keyWorldsView.setVisibility(View.VISIBLE);
						search2.setVisibility(View.VISIBLE);
						listview.setVisibility(View.VISIBLE);
						
						search2.setOnKeyListener(new View.OnKeyListener() {

							public boolean onKey(View v, int keyCode,
									KeyEvent event) {
								// TODO Auto-generated method stub

								if (keyCode == KeyEvent.KEYCODE_ENTER) {
									InputMethodManager imm = (InputMethodManager) v
											.getContext()
											.getSystemService(
													Context.INPUT_METHOD_SERVICE);
									if (imm.isActive()) {

										imm.hideSoftInputFromWindow(
												v.getApplicationWindowToken(),
												0);
									}

									String Searchname = search2.getText()
											.toString();
									Toast.makeText(gnActivity.this, Searchname,
											Toast.LENGTH_SHORT);
									if (Searchname.equals(""))
										Toast.makeText(gnActivity.this,
												"输入不正确，请换个姿势试试",
												Toast.LENGTH_LONG).show();
									else {
										mPoiSearch
												.searchInCity((new PoiCitySearchOption())
														.city(city)
														.keyword(
																search2.getText()
																		.toString())
														.pageNum(load_Index)
														.pageCapacity(8));
										sb1 = new StringBuilder();
										new StringBuilder();
									}
									return true;
								}
								return false;
							}
						
						});
						a = 0;
					}else {	
						System.out.println("!!!!!!!!!!");
					    listview.setVisibility(View.GONE);
					    relativelayout.setVisibility(View.GONE);
					    keyWorldsView.setVisibility(View.GONE);
					    search2.setVisibility(View.GONE);						
						a = 1;
					}
				}
			}
		};
		share.setOnClickListener(onClickListener);
		search.setOnClickListener(onClickListener);
	}

	@Override
	public void onGetLocationShareUrlResult(ShareUrlResult result) {

		// 分享短串结果
		Intent it = new Intent(Intent.ACTION_SEND);
		it.putExtra(Intent.EXTRA_TEXT, "您的朋友与您分享一个位置: " + weizhi + " -- "
				+ result.getUrl());
		it.setType("text/plain");
		startActivity(Intent.createChooser(it, "将位置分享到"));
	}

	@Override
	public void onGetPoiDetailShareUrlResult(ShareUrlResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		// TODO Auto-generated method stub
		if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		sugAdapter.clear();
		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			if (info.key != null)
				sugAdapter.add(info.key);
		}
		sugAdapter.notifyDataSetChanged();
	}
	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		// TODO Auto-generated method stub

		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(gnActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(gnActivity.this,
					result.getName() + ": " + result.getAddress(),
					Toast.LENGTH_SHORT).show();

			Uri uri = Uri.parse(result.getDetailUrl());
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	}
	@Override
	public void onGetPoiResult(PoiResult result) {
		// TODO Auto-generated method stub
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {

			int i = 0;
			sb = new String[result.getCurrentPageCapacity()];
			sb3 = new String[result.getCurrentPageCapacity()];
			wz = new LatLng[result.getCurrentPageCapacity()];

			for (PoiInfo poi : result.getAllPoi()) {

				sb[i] = "名称：" + poi.name;
				sb3[i] = "名称：" + poi.name + "\r\n" + "位置：" + poi.address
						+ "\r\n" + "电话：" + poi.phoneNum;
				wz[i] = poi.location;
				i++;
				sb1.append("名称：").append(poi.name).append("\n");
			}

			myAdapter = new MyListAdapter(gnActivity.this, R.layout.sousuo);
			listview.setAdapter(myAdapter);

			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapterview, View view,
						int position, long id) {
					// TODO Auto-generated method stub

					// 通过AlertDialog显示当前页搜索到的POI
					AlertDialog.Builder builder = new AlertDialog.Builder(
							gnActivity.this);
					// 设置Title的内容
					builder.setTitle("详细信息");
					// 设置Content来显示一个信息
					builder.setMessage(sb3[position]);
					posi = position;
					// 设置一个PositiveButton
					builder.setPositiveButton("定位",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent();
									intent.putExtra("wz1", wz[posi].latitude);
									intent.putExtra("wz2", wz[posi].longitude);
                                    intent.putExtra("poi", search2.getText().toString());
                                    intent.setAction(BROADCAST_ACTION);
                                	gnActivity.this.sendBroadcast(intent);
                                	gnActivity.this.finish();
                                	
								}
							});
					builder.show();
				}
			});


			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

			// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
			String strInfo = "在";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "找到结果";
			Toast.makeText(gnActivity.this, strInfo, Toast.LENGTH_LONG).show();
		}
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
			return sb.length;
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
			TextView text = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						mTextViewResourceID, null);

				text = (TextView) convertView.findViewById(R.id.array_text);
				text.setText(sb[position]);
				text.setTextSize(14);
			}
			return convertView;
		}
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mPoiSearch.destroy();
		mSuggestionSearch.destroy();

	}

}
