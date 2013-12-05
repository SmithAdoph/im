package com.lejoying.mc;

import com.lejoying.mc.service.MainService;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class WelcomeActivity extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout._welcome);
		Intent service = new Intent(this, MainService.class);
		startService(service);
		// User user = MCDataTools.getLoginedUser(this);
		// if (user != null && user.getPhone() != null
		// && user.getAccessKey() != null) {
		// Bundle params = new Bundle();
		// params.putString("phone", user.getPhone());
		// params.putString("accessKey", user.getAccessKey());
		// params.putString("target", user.getPhone());
		// startNetwork(API.ACCOUNT_GET, params, false,
		// new NetworkStatusListener() {
		// @Override
		// public void onReceive(int STATUS, String log) {
		// switch (STATUS) {
		// case NetworkService.STATUS_SUCCESS:
		// startToMain();
		// break;
		// case NetworkService.STATUS_UNSUCCESS:
		//
		// break;
		// case NetworkService.STATUS_NOINTERNET:
		//
		// break;
		// case NetworkService.STATUS_FAILED:
		//
		// break;
		//
		// default:
		// break;
		// }
		// }
		// });
		// } else {
		// startToLogin();
		// }
		//
		startToLogin();
	}

	private void startToLogin() {
		Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
		WelcomeActivity.this.startActivity(intent);
		WelcomeActivity.this.finish();
	}

	private void startToMain() {
		Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
		WelcomeActivity.this.startActivity(intent);
		WelcomeActivity.this.finish();
	}

	@Override
	public Fragment setFirstPreview() {
		return null;
	}

	@Override
	protected int setBackground() {
		return R.drawable.app_start;
	}

}
