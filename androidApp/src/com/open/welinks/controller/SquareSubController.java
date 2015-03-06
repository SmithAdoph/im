package com.open.welinks.controller;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.google.gson.Gson;
import com.open.lib.MyLog;
import com.open.welinks.R;
import com.open.welinks.customListener.MyOnClickListener;
import com.open.welinks.model.Data;
import com.open.welinks.model.Parser;
import com.open.welinks.view.SquareSubView;

public class SquareSubController {

	public Data data = Data.getInstance();
	public Parser parser = Parser.getInstance();

	public String tag = "SquareSubController";
	public MyLog log = new MyLog(tag, true);

	public SquareSubView thisView;
	public Context context;
	public Activity thisActivity;

	public MainController mainController;

	public MyOnClickListener mOnClickListener;
	public OnTouchListener mOnTouchListener;

	public Gson gson = new Gson();

	public SquareSubController(MainController mainController) {
		thisActivity = mainController.thisActivity;
		this.mainController = mainController;
	}

	public LocationManagerProxy mLocationManagerProxy;
	public AMapLocationListener mAMapLocationListener;
	public AMapLocation mAmapLocation;

	public void getLocation() {
		mLocationManagerProxy = LocationManagerProxy.getInstance(thisActivity);
		mLocationManagerProxy.setGpsEnable(true);
		mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 1000, mAMapLocationListener);
	}

	public View onTouchDownView;

	public boolean isTouchDown = false;

	public void initializeListeners() {
		mAMapLocationListener = new AMapLocationListener() {

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			}

			@Override
			public void onProviderEnabled(String arg0) {
			}

			@Override
			public void onProviderDisabled(String arg0) {
			}

			@Override
			public void onLocationChanged(Location location) {
			}

			@Override
			public void onLocationChanged(AMapLocation amapLocation) {
				mLocationManagerProxy.removeUpdates(mAMapLocationListener);
				mLocationManagerProxy.destroy();
				if (amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0) {
					mAmapLocation = amapLocation;
					thisView.titleNameView.setText(mAmapLocation.getProvince() + mAmapLocation.getCity());
					// searchNearby(amapLocation);
					// searchNearByPolygon(0);
				}
			}
		};
		getLocation();
		this.mOnTouchListener = new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					if (isTouchDown) {
						return false;
					}
					String view_class = (String) view.getTag(R.id.tag_class);
					if (view_class.equals("SelectBody")) {
						onTouchDownView = view;
						isTouchDown = true;
						log.e("mOnTouchListener onTouch");
						if (thisView.dialogSpring.getCurrentValue() == 1d) {
							thisView.targetView = view;
							thisView.dialogSpring.addListener(thisView.dialogSpringListener);
							thisView.dialogSpringListener.id = 0;
							thisView.dialogSpring.setCurrentValue(1);
							thisView.dialogSpring.setEndValue(0.9);
						}
					}
				}
				return false;
			}
		};

		mOnClickListener = new MyOnClickListener() {
			@Override
			public void onClickEffective(View view) {
				if (view.getTag(R.id.tag_class) != null) {
					String view_class = (String) view.getTag(R.id.tag_class);
					if (view_class.equals("SelectBody")) {

						int id = (Integer) view.getTag(R.id.tag_first);
						if (onTouchDownView != null) {
							thisView.dialogSpring.setCurrentValue(0.9);
							thisView.dialogSpringListener.id = id;
							thisView.dialogSpring.setEndValue(1);
						}
					}
				}
			}
		};
	}

	public void bindEvent() {
	}

	public void onSingleTapUp(MotionEvent event) {
		log.e("onSingleTapUp");
		if (this.onTouchDownView != null) {
			String view_class = (String) this.onTouchDownView.getTag(R.id.tag_class);
			if (view_class.equals("SelectBody")) {
				int id = (Integer) this.onTouchDownView.getTag(R.id.tag_first);
				if (id == R.drawable.sidebar_icon_discover_normal) {
					onTouchDownView.performClick();
				} else if (id == R.drawable.sidebar_icon_days_normal) {
					onTouchDownView.performClick();
				} else if (id == R.drawable.sidebar_icon_group_normal) {
					onTouchDownView.performClick();
				} else if (id == R.drawable.sidebar_icon_category_normal) {
					onTouchDownView.performClick();
				} else if (id == R.drawable.sidebar_icon_test_normal) {
					onTouchDownView.performClick();
				} else if (id == R.drawable.sidebar_icon_beauty_normal) {
					onTouchDownView.performClick();
				}
			}
			onTouchDownView = null;
		}
		isTouchDown = false;
	}

	public void onScroll() {
		if (onTouchDownView != null) {
			thisView.dialogSpringListener.id = 0;
			thisView.dialogSpring.setCurrentValue(0.9);
			thisView.dialogSpring.setEndValue(1);
		}
		// isTouchDown = false;
		onTouchDownView = null;
		isTouchDown = false;
	}

	public void onDestroy() {

	}
}
