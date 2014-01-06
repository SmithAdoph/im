package com.lejoying.mc.service;

import java.net.HttpURLConnection;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.lejoying.mc.LoginActivity;
import com.lejoying.mc.MainActivity;
import com.lejoying.mc.R;
import com.lejoying.mc.api.API;
import com.lejoying.mc.data.App;
import com.lejoying.mc.utils.MCHttpTools;
import com.lejoying.mc.utils.MCNetTools;
import com.lejoying.mc.utils.MCNetTools.ResponseListener;

public class PushService extends Service {
	App app = App.getInstance();
	HttpURLConnection longAjaxConnection;
	public boolean isStart;

	long failTime;
	int failCount;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public void startLongAjax(final Bundle params) {
		isStart = true;
		MCNetTools.ajax(this, API.SESSION_EVENT, params, MCHttpTools.SEND_POST,
				30000, new ResponseListener() {

					@Override
					public void success(JSONObject data) {
						if (isStart) {
							startLongAjax(params);
						}
						try {
							data.getString(getString(R.string.app_reason));
							if (MainActivity.instance != null) {
								Intent intent = new Intent(
										MainActivity.instance,
										LoginActivity.class);
								MainActivity.instance.startActivity(intent);
								MainActivity.instance = null;
							}
							isStart = false;
							return;
						} catch (JSONException e) {
						}
						System.out.println(data);

						try {
							String event = data.getString("event");
							JSONObject eventContent = data
									.getJSONObject("event_content");
							if (event.equals("message")) {
								app.dataHandler.sendMessage(
										app.dataHandler.HANDLER_MESSAGE,
										eventContent.getJSONArray("message"));
								app.isDataChanged = true;
								if (app.data.user.flag.equals("none")) {
									app.data.user.flag = String.valueOf(1);
								} else {
									app.data.user.flag = String.valueOf(Integer
											.valueOf(app.data.user.flag)
											.intValue() + 1);
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void noInternet() {
						System.out.println("noInternet");
					}

					@Override
					public void failed() {
						if (isStart) {
							startLongAjax(params);
						} else {
							if (MainActivity.instance != null) {
								Intent intent = new Intent(
										MainActivity.instance,
										LoginActivity.class);
								MainActivity.instance.startActivity(intent);
								MainActivity.instance.finish();
								MainActivity.instance = null;
							}
						}
						long nowTime = new Date().getTime();
						if (nowTime - failTime < 5000) {
							failCount++;
						}
						failTime = nowTime;
						if (failCount > 5) {
							isStart = false;
							failCount = 0;
						}
						System.out.println("failed");
					}

					@Override
					public void connectionCreated(
							HttpURLConnection httpURLConnection) {
						System.out.println("create long ajax");
						longAjaxConnection = httpURLConnection;
					}
				});
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String objective = intent.getStringExtra("objective");
		if (objective.equals("start")) {
			Bundle params = new Bundle();
			params.putString("phone", app.data.user.phone);
			params.putString("accessKey", app.data.user.accessKey);
			if (!isStart) {
				startLongAjax(params);
			}
		} else if (objective.equals("stop")) {
			isStart = false;
			longAjaxConnection.disconnect();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}