package cn.buaa.myweixin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.buaa.myweixin.utils.HttpTools;
import cn.buaa.myweixin.utils.LocationTools;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterSetPassActivity extends Activity {

	private final int REGISTER_NEXT = 0x22;
	private String registerNumber;
	private TextView tv_password;
	private Handler handler;

	private String password;

	private byte[] data;

	private double longitude;
	private double latitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_setpass);
		// ����activityʱ�Զ����������
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		initView();
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				int what = msg.what;
				switch (what) {
				case REGISTER_NEXT:
					Intent intent = new Intent(RegisterSetPassActivity.this,
							CCommunityActivity.class);
					Bundle bundle = new Bundle();
					try {
						JSONObject jo = new JSONObject(new String(data));
						String info = jo.getString("��ʾ��Ϣ");
						if (info.equals("ע��ɹ�")) {
							JSONObject community = jo
									.getJSONObject("nowcommunity");
							bundle.putString("nowcommunity", community.toString());
							intent.putExtras(bundle);
							RegisterSetPassActivity.this.startActivity(intent);

						} else {
							String err = jo.getString("ʧ��ԭ��");
							Toast.makeText(RegisterSetPassActivity.this, err,
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						Toast.makeText(RegisterSetPassActivity.this, "�����쳣",
								Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
		};
	}

	public void initView() {
		Bundle bundle = getIntent().getExtras();
		registerNumber = bundle.getString("number");

		tv_password = (TextView) findViewById(R.id.tv_setpass);
	}

	private boolean flag = true;
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

		flag = true;
	}

	public void registerCheckingNext(View v) {
		if (flag) {
			double[] location = LocationTools
					.getLocation(RegisterSetPassActivity.this);
			longitude = location[0];
			latitude = location[1];
			boolean hasNetwork = HttpTools.hasNetwork(this);

			if (!hasNetwork)
				Toast.makeText(RegisterSetPassActivity.this, "����������",
						Toast.LENGTH_SHORT).show();
			else {

				password = tv_password.getText().toString();
				if (password == null || password.equals("")) {
					Toast.makeText(this, "����������", Toast.LENGTH_SHORT).show();
					return;
				}
				new Thread() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();

						Map<String, String> map = new HashMap<String, String>();
						map.put("password", String.valueOf(password));
						map.put("phone", String.valueOf(registerNumber));
						map.put("longitude", String.valueOf(longitude));
						map.put("latitude", String.valueOf(latitude));

						try {
							flag = false;
							data = HttpTools
									.sendPost(
											"http://192.168.0.19:8071/api2/account/verifypass",
											map);
							handler.sendEmptyMessage(REGISTER_NEXT);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							flag = true;
							e.printStackTrace();
						}
					}
				}.start();
			}
		} else {

		}
	}

	// ���ذ�ť
	public void register_back(View v) {
		finish();
	}

}
