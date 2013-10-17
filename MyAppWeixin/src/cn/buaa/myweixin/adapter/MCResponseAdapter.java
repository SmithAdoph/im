package cn.buaa.myweixin.adapter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import cn.buaa.myweixin.R;
import cn.buaa.myweixin.listener.ResponseListener;

public class MCResponseAdapter implements ResponseListener {

	private Activity activity;
	
	public MCResponseAdapter(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void noInternet() {
		Context context = activity;
		new AlertDialog.Builder(activity)
				.setIcon(
						context.getResources().getDrawable(
								R.drawable.login_error_icon))
				.setTitle("�������").setMessage("���������ӣ����������������").create()
				.show();
	}

	@Override
	public void success(JSONObject data) {
		
	}

	@Override
	public void failed(JSONObject data) {
		Context context = activity;
		try {
			String err = data.getString("ʧ��ԭ��");
			new AlertDialog.Builder(activity)
			.setIcon(
					context.getResources().getDrawable(
							R.drawable.login_error_icon))
			.setTitle("����ʧ��").setMessage(err).create()
			.show();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
