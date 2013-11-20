package com.lejoying.adapter;

import org.json.JSONObject;

import android.app.Activity;

import com.lejoying.listener.ResponseListener;
import com.lejoying.mcutils.MCTools;

public class MCResponseAdapter implements ResponseListener {

	private Activity activity;

	public MCResponseAdapter(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void noInternet() {
		MCTools.showMsg(activity, "没有网络，请检查网络后重试");
	}

	@Override
	public void success(JSONObject data) {

	}

	@Override
	public void unsuccess(JSONObject data) {
		MCTools.showMsg(activity, "数据异常,操作失败");
	}

	@Override
	public void failed() {
		MCTools.showMsg(activity, "连接服务器异常");
	}

}
