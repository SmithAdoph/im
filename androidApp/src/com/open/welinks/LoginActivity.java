package com.open.welinks;

import com.open.welinks.controller.LoginController;
import com.open.welinks.model.Data;
import com.open.welinks.view.LoginView;
import com.open.welinks.view.LoginView.Status;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class LoginActivity extends Activity {
	public Data data = Data.getInstance();
	public String tag = "LoginActivity";

	public Context context;
	public LoginView thisView;
	public LoginController thisController;
	public Activity thisActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		linkViewController();

		if (thisView.status == Status.welcome) {
			thisView.status = Status.welcome;
		} else {
			thisView.status = Status.start;
		}
	}

	void linkViewController() {
		this.thisActivity = this;
		this.context = this;
		this.thisView = new LoginView(thisActivity);
		this.thisController = new LoginController(thisActivity);
		this.thisView.thisController = this.thisController;
		this.thisController.thisView = this.thisView;
		
		thisView.initView();
		thisController.onCreate();
		thisController.initializeListeners();
		thisController.bindEvent();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_debug_1, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.debug1_1) {
			Log.d(tag, "debug1.1");
		}
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return thisController.onKeyDown(keyCode, event);
	}

}