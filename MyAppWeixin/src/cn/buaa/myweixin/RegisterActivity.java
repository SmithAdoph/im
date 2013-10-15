package cn.buaa.myweixin;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.buaa.myweixin.utils.MCTools;
import cn.buaa.myweixin.utils.MCTools.HttpStatusListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	public static RegisterActivity instance = null;
	private boolean isAgreeProvision;

	private ImageView iv_agreeprovision;

	private String registerNumber;
	private EditText register_number_edit;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_mobile);
		// ����activityʱ�Զ����������
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		initView();
	
	}

	public void initView() {
		instance = this;
		isAgreeProvision = true;
		iv_agreeprovision = (ImageView) findViewById(R.id.iv_agreeprovision);
		register_number_edit = (EditText) findViewById(R.id.register_number_edit);
	}

	// �����һ��
	public void registerMobileNext(View v) {
		registerNumber = register_number_edit.getText().toString();
		if (isAgreeProvision) {

			String number = register_number_edit.getText().toString();
			if (number == null || number.equals("")) {
				Toast.makeText(this, "��������ȷ���ֻ���", Toast.LENGTH_SHORT).show();
				return;
			}

			Map<String, String> map = new HashMap<String, String>();
			map.put("phone", String.valueOf(registerNumber));

			MCTools.postForJSON(this,
					"http://192.168.0.19:8071/api2/account/verifyphone", map,
					true, new HttpStatusListener() {
						@Override
						public void shortIntervalTime() {
							// TODO Auto-generated method stub

						}

						@Override
						public void noInternet() {
							new AlertDialog.Builder(RegisterActivity.this)
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
							Intent intent = new Intent(RegisterActivity.this,
									RegisterCheckingActivity.class);
							Bundle bundle = new Bundle();
							try {
								String info = data.getString("��ʾ��Ϣ");
								if (info.equals("�ֻ�����֤�ɹ�")) {
									String number = data.getString("phone");
									if (number.equals(registerNumber)) {
										bundle.putString("number", registerNumber);
										intent.putExtras(bundle);
										RegisterActivity.this.startActivity(intent);

									} else {
										Toast.makeText(RegisterActivity.this, "�����쳣",
												Toast.LENGTH_SHORT).show();

									}
								} else {
									String err = data.getString("ʧ��ԭ��");

									System.out.println(err);
									new AlertDialog.Builder(RegisterActivity.this)
									.setIcon(
											getResources()
													.getDrawable(
															R.drawable.login_error_icon))
									.setTitle("��֤ʧ��")
									.setMessage(err).create()
									.show();

								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
		} else {
			new AlertDialog.Builder(RegisterActivity.this)
			.setIcon(
					getResources()
							.getDrawable(
									R.drawable.login_error_icon))
			.setTitle("ͬ������")
			.setMessage("��ͬ��ʹ���������˽���ߣ�").create()
			.show();
		}

	}

	// ͬ������
	public void agreeProvision(View v) {
		if (isAgreeProvision) {
			iv_agreeprovision.setImageResource(R.drawable.reg_checkbox_normal);
			isAgreeProvision = false;
		} else {
			iv_agreeprovision.setImageResource(R.drawable.reg_checkbox_checked);
			isAgreeProvision = true;
		}
	}

	// ���ذ�ť
	public void register_back(View v) {
		finish();
	}
}
