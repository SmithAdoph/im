package com.open.welinks;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.open.welinks.controller.ShareMessageDetailController;
import com.open.welinks.model.Data;
import com.open.welinks.view.ShareMessageDetailView;

public class ShareMessageDetailActivity extends Activity {

	public Data data = Data.getInstance();
	public String tag = "ShareMessageDetailActivity";

	public Context context;
	public ShareMessageDetailView thisView;
	public ShareMessageDetailController thisController;
	public Activity thisActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		linkViewController();
	}

	public void linkViewController() {
		this.thisActivity = this;
		this.context = this;
		this.thisView = new ShareMessageDetailView(thisActivity);
		this.thisController = new ShareMessageDetailController(thisActivity);
		this.thisView.thisController = this.thisController;
		this.thisController.thisView = this.thisView;

		thisController.initData();
		thisController.initializeListeners();
		thisView.initView();
		thisController.bindEvent();
	}
}
