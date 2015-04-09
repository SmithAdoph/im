package com.open.welinks.model;

import android.app.Application;
import android.content.Intent;

import com.open.welinks.service.ExceptionService;

public class MainApplication extends Application {

	public Data data = Data.getInstance();

	@Override
	public void onCreate() {
		super.onCreate();
		ExceptionService service = new ExceptionService();
		Intent intent = new Intent(getApplicationContext(), ExceptionService.class);
		startService(intent);
		ExceptionHandler handler = ExceptionHandler.getInstance();
		handler.init(getApplicationContext(), service);
		Thread.setDefaultUncaughtExceptionHandler(handler);
	}
}
