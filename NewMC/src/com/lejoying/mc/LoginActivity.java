package com.lejoying.mc;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.lejoying.mc.data.App;
import com.lejoying.mc.fragment.LoginUsePassFragment;
import com.lejoying.utils.HttpTools;

public class LoginActivity extends BaseFragmentActivity {

	App app = App.getInstance();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout._main);
		app.dataHandler.sendEmptyMessage(app.dataHandler.DATA_HANDLER_CLEANDATA);
	}

	@Override
	public Fragment setFirstPreview() {
		return new LoginUsePassFragment();
	}

	@Override
	protected int setBackground() {
		return R.drawable.snow_d_blur;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		app.context = this;
	}

}
