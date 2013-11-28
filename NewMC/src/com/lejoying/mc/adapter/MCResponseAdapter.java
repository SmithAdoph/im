package com.lejoying.mc.adapter;

import org.json.JSONObject;

import android.app.Activity;

import com.lejoying.mc.LoadingActivity;
import com.lejoying.mc.listener.ResponseListener;
import com.lejoying.mc.utils.MCTools;

public class MCResponseAdapter implements ResponseListener {

	private Activity activity;

	public MCResponseAdapter(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void noInternet() {
		if (LoadingActivity.instance != null) {
			LoadingActivity.instance.finish();
		}
		MCTools.showMsg(activity, "û�����磬�������������");
	}

	@Override
	public void success(JSONObject data) {
		if (LoadingActivity.instance != null) {
			LoadingActivity.instance.finish();
		}
	}

	@Override
	public void unsuccess(JSONObject data) {
		if (LoadingActivity.instance != null) {
			LoadingActivity.instance.finish();
		}
		MCTools.showMsg(activity, "�����쳣,����ʧ��");
	}

	@Override
	public void failed() {
		if (LoadingActivity.instance != null) {
			LoadingActivity.instance.finish();
		}
		MCTools.showMsg(activity, "���ӷ������쳣");
	}

}
