package cn.buaa.myweixin;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.buaa.myweixin.utils.MCTools;
import cn.buaa.myweixin.utils.MCTools.HttpStatusListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterCheckingActivity extends Activity {

	public static RegisterCheckingActivity instance = null;
	private String registerNumber;
	private TextView tv_registernumber;
	private TextView tv_checkingcode;

	private String checkingcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_checking);
		// ����activityʱ�Զ����������
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		initView();
	}

	public void initView() {
		instance = this;
		Bundle bundle = getIntent().getExtras();
		registerNumber = bundle.getString("number");

		tv_registernumber = (TextView) findViewById(R.id.tv_registernumber);
		tv_registernumber.setText("+86" + registerNumber);

		tv_checkingcode = (TextView) findViewById(R.id.tv_checkingcode);
	}

	public void registerCheckingNext(View v) {

		checkingcode = tv_checkingcode.getText().toString();
		if (checkingcode == null || checkingcode.equals("")) {
			Toast.makeText(this, "��������֤��", Toast.LENGTH_SHORT).show();
			return;
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("code", String.valueOf(checkingcode));
		map.put("phone", String.valueOf(registerNumber));

		MCTools.postForJSON(this,
				"http://192.168.0.19:8071/api2/account/verifycode", map, true,
				new HttpStatusListener() {

					@Override
					public void shortIntervalTime() {
						// TODO Auto-generated method stub

					}

					@Override
					public void noInternet() {
						new AlertDialog.Builder(RegisterCheckingActivity.this)
						.setIcon(
								getResources()
										.getDrawable(
												R.drawable.login_error_icon))
						.setTitle("�������")
						.setMessage("����������,�����������\n���ԣ�").create()
						.show();
					}

					@Override
					public void getJSONSuccess(JSONObject data) {
						Intent intent = new Intent(
								RegisterCheckingActivity.this,
								RegisterSetPassActivity.class);
						Bundle bundle = new Bundle();
						try {
							String info = data.getString("��ʾ��Ϣ");
							if (info.equals("��֤�ɹ�")) {
								String number = data.getString("phone");
								if (number.equals(registerNumber)) {
									bundle.putString("number", registerNumber);
									intent.putExtras(bundle);
									RegisterCheckingActivity.this
											.startActivity(intent);

								} else {
									Toast.makeText(
											RegisterCheckingActivity.this,
											"�����쳣", Toast.LENGTH_SHORT).show();
								}
							} else {
								String err = data.getString("ʧ��ԭ��");
								new AlertDialog.Builder(RegisterCheckingActivity.this)
								.setIcon(
										getResources()
												.getDrawable(
														R.drawable.login_error_icon))
								.setTitle("��֤ʧ��")
								.setMessage(err).create()
								.show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

	}

	// ���ذ�ť
	public void register_back(View v) {
		finish();
	}

}
