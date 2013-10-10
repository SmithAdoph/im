package cn.buaa.myweixin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.buaa.myweixin.utils.HttpTools;
import cn.buaa.myweixin.utils.LocationTools;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	private EditText mUser; // �ʺű༭��
	private EditText mPassword; // ����༭��

	private byte[] data;

	private static final int LOGIN_NEXT = 0x81;
	private Handler handler;
	
	private double longitude;
	private double latitude;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		mUser = (EditText) findViewById(R.id.login_user_edit);
		mPassword = (EditText) findViewById(R.id.tv_password);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				int what = msg.what;
				switch (what) {
				case LOGIN_NEXT:
					try {
						JSONObject jo = new JSONObject(new String(data));
						String info = jo.getString("��ʾ��Ϣ");
						if (info.equals("�˺ŵ�¼�ɹ�")) {
							String status = jo.getString("status");
							if (status.equals("success")) {
								Intent intent = new Intent();
								intent.setClass(Login.this,
										LoadingActivity.class);
								startActivity(intent);
							}
							if (status.equals("unjoin")) {
								Intent intent = new Intent();
								intent.setClass(Login.this,
										CCommunityActivity.class);

							}
						} else {
							new AlertDialog.Builder(Login.this)
									.setIcon(
											getResources()
													.getDrawable(
															R.drawable.login_error_icon))
									.setTitle("��¼ʧ��")
									.setMessage("΢���ʺŻ������벻��ȷ��\n������������룡")
									.create().show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
		};

	}

	public void login_mainweixin(View v) {
		double[] location = LocationTools.getLocation(Login.this);
		longitude = location[0];
		latitude = location[1];
		boolean hasNetwork = HttpTools.hasNetwork(this);
		if (!hasNetwork)
			Toast.makeText(Login.this, "����������", Toast.LENGTH_SHORT).show();
		else {
			if ("".equals(mUser.getText().toString())
					|| "".equals(mPassword.getText().toString())) // �ж� �ʺź�����
			{
				new AlertDialog.Builder(Login.this)
						.setIcon(
								getResources().getDrawable(
										R.drawable.login_error_icon))
						.setTitle("��¼����").setMessage("΢���ʺŻ������벻��Ϊ�գ�\n��������ٵ�¼��")
						.create().show();
			} else {
				new Thread() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();

						Map<String, String> map = new HashMap<String, String>();
						map.put("phone", mUser.getText().toString());
						map.put("password",
								String.valueOf(mPassword.getText().toString()));
						map.put("longitude",String.valueOf(longitude));
						map.put("latitude",String.valueOf(latitude));
						
						try {
							data = HttpTools
									.sendPost(
											"http://192.168.0.198:8071/api2/account/auth",
											map);
							handler.sendEmptyMessage(LOGIN_NEXT);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}.start();
			}

		}
	}

	public void login_back(View v) { // ������ ���ذ�ť
		this.finish();
	}

	public void login_pw(View v) { // �������밴ť
		Uri uri = Uri.parse("http://3g.qq.com");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
		// Intent intent = new Intent();
		// intent.setClass(Login.this,Whatsnew.class);
		// startActivity(intent);
	}
}
