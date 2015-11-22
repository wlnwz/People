/*
 * Copyright (C) 2014 yeran
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.slidingmenu.view;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.example.people.R;
import com.util.MyOrientationListener;
import com.util.MyOrientationListener.OnOrientationListener;
import com.util.jwdtransfertolength;

public class PageFragment1 extends Fragment implements OnItemClickListener,
		OnGetGeoCoderResultListener, OnGetPoiSearchResultListener {

	SupportMapFragment map;
	LocationClient mLocClient;
	MapStatusUpdate u,u1;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private double weidu1, jingdu1, wz1, wz2;
	boolean isFirstLoc = true;// 是否首次定位
	private LatLng ll, ll2;
	private LatLng[] llA2;
	private OverlayOptions[] oo;
	private BitmapDescriptor bdA;
	private Marker[] mMarker;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	Button requestLocButton;
	BitmapDescriptor mCurrentMarker;
	private GeoCoder mGeoCoder = null;
	private String weizhi, searchpoi, city, username,userid;
	private String[] policename, plocation, ptelphone, length;
	private PoiSearch mPoiSearch = null;
	private int load_Index = 0, aa, h, mXDirection;
	private String[] sb;
	private final String BROADCAST_ACTION = "com.example.set.broadcast";
	private MyBroadcastRecever myBroadcastRecever;
	jwdtransfertolength transfer;
	private boolean showornot;
	Button button, see;
	private InfoWindow mInfoWindow;
	MyOrientationListener myOrientationListener;
	private float mCurrentAccracy;
	private boolean czjc = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(this.getActivity().getApplicationContext());

		initOritationListener();

		IntentFilter filter = new IntentFilter();
		myBroadcastRecever = new MyBroadcastRecever();
		// 设置接收广播的类型，这里要和Service里设置的类型匹配，还可以在AndroidManifest.xml文件中注册
		filter.addAction("com.example.broadcast.police");
		filter.addAction("com.example.broadcast.gnactivity");
		filter.addAction("com.example.broadcast.showornot");
		filter.addAction("com.example.broadcast.name");
		getActivity().registerReceiver(myBroadcastRecever, filter);

		plocation = new String[aa];
		ptelphone = new String[aa / 2];
		policename = new String[aa / 2];

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.page1, null);
		see = (Button) view.findViewById(R.id.see);

		requestLocButton = (Button) view.findViewById(R.id.button1);
		requestLocButton.getBackground().setAlpha(150);

		mGeoCoder = GeoCoder.newInstance();
		mGeoCoder.setOnGetGeoCodeResultListener(PageFragment1.this);

		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);

		mCurrentMode = LocationMode.FOLLOWING;
		// 地图初始化
		mMapView = (MapView) view.findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationEnabled(true);
		mLocClient = new LocationClient(this.getActivity());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		// 设置定位模式
		option.setAddrType("all");
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(2000);
		option.setPriority(LocationClientOption.GpsFirst);
		mLocClient.setLocOption(option);
		mLocClient.start();
        
//		testmap();
		
		Initclick();

		return view;
	}

	private void initOritationListener() {
		myOrientationListener = new MyOrientationListener(getActivity()
				.getApplicationContext());
		myOrientationListener
				.setOnOrientationListener(new OnOrientationListener() {
					@Override
					public void onOrientationChanged(float x) {
						mXDirection = (int) x;

						// 构造定位数据
						MyLocationData locData = new MyLocationData.Builder()
								.accuracy(mCurrentAccracy)
								// 此处设置开发者获取到的方向信息，顺时针0-360
								.direction(mXDirection).latitude(weidu1)
								.longitude(jingdu1).build();
						// 设置定位数据
						mBaiduMap.setMyLocationData(locData);
						// 设置自定义图标
						// BitmapDescriptor mCurrentMarker =
						// BitmapDescriptorFactory
						// .fromResource(R.drawable.navi_map_gps_locked);

					}
				});
	}

	void Initclick() {

		OnClickListener btnClickListener = new OnClickListener() {
			@SuppressLint("NewApi")
			public void onClick(View v) {
				if (v.equals(requestLocButton)) {

					switch (mCurrentMode) {
					case NORMAL:

						mCurrentMode = LocationMode.FOLLOWING;
						mBaiduMap
								.setMyLocationConfigeration(new MyLocationConfiguration(
										mCurrentMode, true, mCurrentMarker));
//						u = MapStatusUpdateFactory.zoomTo(15);
//						mBaiduMap.animateMapStatus(u);
						mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));
						requestLocButton.setText("跟");
//						requestLocButton
//								.setBackground(null);
						
						mBaiduMap.clear();
						break;
					case COMPASS:
						mCurrentMode = LocationMode.NORMAL;
						mBaiduMap
								.setMyLocationConfigeration(new MyLocationConfiguration(
										mCurrentMode, true, mCurrentMarker));
						requestLocButton.setText("普");
//						requestLocButton
//								.setBackgroundResource(R.drawable.fuyuan);
						requestLocButton.getBackground().setAlpha(200);
						break;
					case FOLLOWING:
						mCurrentMode = LocationMode.COMPASS;
						mBaiduMap
								.setMyLocationConfigeration(new MyLocationConfiguration(
										mCurrentMode, true, mCurrentMarker));
						requestLocButton.setText("罗");
//						requestLocButton.setBackground(null);

						break;
					}
				} else if (v.equals(see)) {
					if (username == null || username.equals("请登录")) {
						Toast.makeText(PageFragment1.this.getActivity(),
								"请先登录您的账号~", Toast.LENGTH_SHORT).show();
					} else {
						// 开启Service服务，当再次启动时，不会执行onCreate，但仍旧执行onStart方法
						Intent intent = new Intent("showpollocService");
						intent.putExtra("username", username);
						intent.putExtra("userid", userid);
						intent.putExtra("startornot", czjc);

						PageFragment1.this.getActivity().startService(intent);						

						if (czjc == true) {
							see.setTextColor(getResources().getColor(
									R.color.red));
							see.setText("显示");
							see.setTextSize(14);							
							czjc = false;
							Toast.makeText(PageFragment1.this.getActivity(),
									"进入警察数据查询", Toast.LENGTH_SHORT).show();

						} else {
							see.setTextColor(getResources().getColor(
									R.color.black));
							see.setText("未显示");
							see.setTextSize(10);
							czjc = true;
						}
					}
				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);
		see.setOnClickListener(btnClickListener);
	}

	void Initpoi() {
		mPoiSearch.searchInCity((new PoiCitySearchOption()).city("西安")
				.keyword(searchpoi).pageNum(load_Index).pageCapacity(8));
		new StringBuilder();
	}

//	void testmap(){
//		weidu1 = 34.235595;
//		jingdu1 = 108.924103;
//		ll = new LatLng(weidu1, jingdu1);
//		weizhi = "陕西省西安市雁塔区高新商圈光华路45号";
//		city ="西安";
//		u = MapStatusUpdateFactory.newLatLng(ll);
//		mBaiduMap.setMapStatus(u);
//		
//		u = MapStatusUpdateFactory.zoomTo(15);		
//		mBaiduMap.animateMapStatus(u);
//	}
	
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
//			MyLocationData locData = new MyLocationData.Builder()
//					.accuracy(location.getRadius())
//					// 此处设置开发者获取到的方向信息，顺时针0-360
//					.direction(mXDirection).latitude(location.getLatitude())
//					.longitude(location.getLongitude()).build();
			
			MyLocationData locData = new MyLocationData.Builder()
		//	.accuracy(location.getRadius())
			// 此处设置开发者获取到的方向信息，顺时针0-360
			.direction(mXDirection).latitude(34.235595)
			.longitude(108.924103).build();

			mBaiduMap.setMyLocationData(locData);

			if (isFirstLoc) {
				isFirstLoc = false;
				mCurrentAccracy = location.getRadius();
//				weidu1 = location.getLatitude();
//				jingdu1 = location.getLongitude();
				
				weidu1 = 34.235595;
				jingdu1 = 108.924103;
				
				ll = new LatLng(weidu1, jingdu1);
//				weizhi = location.getAddrStr();
				weizhi = "陕西省西安市雁塔区高新商圈光华路45号";
//				weizhi = "首尔";
//				city = location.getCity();
				city ="西安";
			

				u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.setMapStatus(u);
//				u1 = MapStatusUpdateFactory.zoomTo(10);		
//				mBaiduMap.animateMapStatus(u1);	
				mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));
				System.out.println("location==" + ll);
			}
			broadcast();
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}

	}

	void broadcast() {
		if (weizhi != null) {
			Intent a = new Intent();
			a.putExtra("city", city);
			a.putExtra("weizhi1", weizhi);
			a.putExtra("location1", ll.latitude);
			a.putExtra("location2", ll.longitude);
			a.setAction(BROADCAST_ACTION);
			System.out.println("a==" + a);
			this.getActivity().sendBroadcast(a);
		}
	}

	public void Addr() {
		// 发起反地理编码请求
		mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
		Toast.makeText(this.getActivity(),
				String.format("搜索位置： %f，%f", ll.latitude, ll.longitude),
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {

	}

	@Override 
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		weizhi = result.getAddress();
		System.out.println("weizhi" + weizhi);
		if (weizhi != null) {
			Intent a = new Intent();
			a.putExtra("weizhi1", weizhi);
			a.putExtra("location1", ll.latitude);
			a.putExtra("location2", ll.longitude);
			a.setAction(BROADCAST_ACTION);
			System.out.println("a==" + a);
			this.getActivity().sendBroadcast(a);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationEnabled(true);
		mLocClient = new LocationClient(this.getActivity());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		// 设置定位模式
		option.setAddrType("all");
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(2000);
		option.setPriority(LocationClientOption.GpsFirst);
		mLocClient.setLocOption(option);
		mLocClient.start();
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));
		myOrientationListener.start();
		if (!mLocClient.isStarted()) {
			mLocClient.start();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
		
		
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onStop() {
		mBaiduMap.setMyLocationEnabled(false);
		mLocClient.stop();
		myOrientationListener.stop();

		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(myBroadcastRecever);
		mLocClient.stop();
		mPoiSearch.destroy();
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		// TODO Auto-generated method stub
		System.out.println("result===" + result);
		 if (result.error != SearchResult.ERRORNO.NO_ERROR) {
		 Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT)
		 .show();
		 } else
		{
			Toast.makeText(getActivity(),
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
			mBaiduMap.clear();

			PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);

			sb = new String[result.getCurrentPageCapacity()];
			int i = 0;
			for (PoiInfo poi : result.getAllPoi()) {

				sb[i] = "名称：" + poi.name;
				i++;
			}
			System.out.println("sb@@@@@@==" + Arrays.toString(sb));

			overlay.setData(result);
			overlay.addToMap();
			overlay.zoomToSpan();

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
			Toast.makeText(getActivity(), strInfo, Toast.LENGTH_LONG).show();
		}
	}

	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			System.out.println("poi@@=" + poi.name + poi.uid);
			mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
					.poiUid(poi.uid));
			return true;
		}
	}

	class MyBroadcastRecever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			if (intent.getAction().equals("com.example.broadcast.gnactivity")) {
				weizhi = intent.getStringExtra("weizhi1");
				wz1 = intent.getDoubleExtra("wz1", 0);
				wz2 = intent.getDoubleExtra("wz2", 0);
				searchpoi = intent.getStringExtra("poi");
				ll2 = new LatLng(wz1, wz2);
				Initpoi();

				requestLocButton.setText("普");
//				requestLocButton.setBackgroundResource(R.drawable.fuyuan);
//				requestLocButton.getBackground().setAlpha(200);
				mCurrentMode = LocationMode.NORMAL;
				mBaiduMap
						.setMyLocationConfigeration(new MyLocationConfiguration(
								mCurrentMode, true, mCurrentMarker));
				u = MapStatusUpdateFactory.newLatLng(ll2);
				mBaiduMap.setMapStatus(u);

			} else if (intent.getAction()
					.equals("com.example.broadcast.police")) {
				aa = intent.getIntExtra("aa", 0);
				policename = intent.getStringArrayExtra("policename");
				ptelphone = intent.getStringArrayExtra("policephone");
				plocation = intent.getStringArrayExtra("plocation");

//				Toast.makeText(getActivity(),
//						"进入警察数据导入" + plocation[0] + plocation[1],
//						Toast.LENGTH_SHORT).show();
				initOverlay();
			} else if (intent.getAction().equals(
					"com.example.broadcast.showornot")) {
				showornot = intent.getBooleanExtra("showornot", false);
				if (showornot == false) {
					mBaiduMap.clear();
				}
				Toast.makeText(getActivity(), "关闭警察位置", Toast.LENGTH_SHORT)
						.show();
			} else if (intent.getAction().equals("com.example.broadcast.name")) {
				username = intent.getStringExtra("name");
				userid = intent.getStringExtra("ID");
				System.out.println("@@@@@@@name" + username);

			}

		}
	}

	// 显示警员位置贴图
	void initOverlay() {

		length = new String[aa / 2];
		transfer = new jwdtransfertolength();
		llA2 = new LatLng[(aa) / 2];
		mMarker = new Marker[(aa) / 2];
		oo = new OverlayOptions[(aa) / 2];
		bdA = BitmapDescriptorFactory.fromResource(R.drawable.ddx);

		for (int i = 0; i < (aa) / 2; i++) {
			length[i] = String.valueOf(transfer.getDistance(
					Double.parseDouble(plocation[(2 * i) + 1]),
					Double.parseDouble(plocation[2 * i]), jingdu1, weidu1));

			llA2[i] = new LatLng(Double.parseDouble(plocation[2 * i]),
					Double.parseDouble(plocation[2 * i + 1]));
			oo[i] = new MarkerOptions().position(llA2[i]).icon(bdA).zIndex(9)
					.draggable(true);
			mMarker[i] = (Marker) (mBaiduMap.addOverlay(oo[i]));
			System.out.println("llA2" + llA2[i]);
		}

		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
		mBaiduMap.setMapStatus(u);
		Initmarketclick();
	}

	// 点击贴图出现警员信息效果
	void Initmarketclick() {
		OnMarkerClickListener onClickListener = new OnMarkerClickListener() {
			// mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				button = new Button(getActivity().getApplicationContext());
				button.setBackgroundResource(R.drawable.popup);
				final LatLng ll = marker.getPosition();
				Point p = mBaiduMap.getProjection().toScreenLocation(ll);
				p.y -= 47;
				LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
				OnInfoWindowClickListener listener = null;
				for (int i = 0; i < ((aa) / 2); i++) {
					if (marker == mMarker[i])
						h = i;
					button.setText("巡警:" + policename[h] + "-详细");
				}
				{
					System.out.println("h@@@==" + h);
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									getActivity());
							builder.setIcon(R.drawable.hx);
							builder.setTitle("您好！" + "我是巡警：" + policename[h]
									+ "。" + "\n" + " 目前距离您：" + length[h] + "m");
							builder.setMessage("我的电话:" + ptelphone[h]);
							builder.setPositiveButton("拨打电话",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent intent = new Intent();
											intent.setAction(Intent.ACTION_CALL);
											intent.setData(Uri.parse("tel:"
													+ ptelphone[h]));
											startActivity(intent);
										}
									});
							builder.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									});
							builder.show();
							mBaiduMap.hideInfoWindow();
						}
					};
				}
				mInfoWindow = new InfoWindow(button, llInfo, listener);
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		};
		mBaiduMap.setOnMarkerClickListener(onClickListener);

	}

}