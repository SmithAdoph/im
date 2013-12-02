package com.lejoying.mc.adapter;

import org.json.JSONObject;

import android.app.Activity;

import com.lejoying.mc.listener.ResponseListener;
import com.lejoying.mc.utils.MCNetTools;

public class MCResponseAdapter implements ResponseListener {

	private Activity activity;

	public MCResponseAdapter(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void noInternet() {
		MCNetTools.showMsg(activity, "û�����磬�������������");
	}

	@Override
	public void success(JSONObject data) {

	}

	@Override
	public void unsuccess(JSONObject data) {
		MCNetTools.showMsg(activity, "�����쳣,����ʧ��");
	}

	@Override
	public void failed() {
		MCNetTools.showMsg(activity, "���ӷ������쳣");
	}

}
