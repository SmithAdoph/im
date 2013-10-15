package cn.buaa.myweixin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import cn.buaa.myweixin.utils.Account;
import cn.buaa.myweixin.utils.MCTools;
import cn.buaa.myweixin.utils.MCNowUser;
import cn.buaa.myweixin.utils.MCTools.HttpStatusListener;

public class Login extends Activity {
	private EditText mUser; // �ʺű༭��
	private EditText mPassword; // ����༭��

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mUser = (EditText) findViewById(R.id.login_user_edit);
		mPassword = (EditText) findViewById(R.id.tv_password);
	}

	public void login_mainweixin(View v) {

		if ("".equals(mUser.getText().toString())
				|| "".equals(mPassword.getText().toString())) {
			new AlertDialog.Builder(Login.this)
					.setIcon(
							getResources().getDrawable(
									R.drawable.login_error_icon))
					.setTitle("��¼����").setMessage("΢���ʺŻ������벻��Ϊ�գ�\n��������ٵ�¼��")
					.create().show();
			return;
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", mUser.getText().toString());
		map.put("password", String.valueOf(mPassword.getText().toString()));

		MCTools.postForJSON(Login.this,
				"http://192.168.0.19:8071/api2/account/auth", map, true, true,
				new HttpStatusListener() {
					public void noInternet() {
						new AlertDialog.Builder(Login.this)
								.setIcon(
										getResources().getDrawable(
												R.drawable.login_error_icon))
								.setTitle("�������")
								.setMessage("����������,�����������\n���ԣ�").create()
								.show();
					}

					public void getJSONSuccess(JSONObject data) {
						try {
							String info = data.getString("��ʾ��Ϣ");
							if (info.equals("�˺ŵ�¼�ɹ�")) {
								JSONObject jaccount = data
										.getJSONObject("account");
								String status = jaccount.getString("status");
								Account account = new Account();
								String phone = jaccount.getString("phone");
								String nickName=  jaccount.getString("nickName");
								String mainBusiness = jaccount.getString("mainBusiness");
								String head = jaccount.getString("head");
								account.setPhone(phone);
								account.setNickName(nickName);
								account.setHead(head);
								account.setMainBusiness(mainBusiness);
								account.setStatus(status);
								MCNowUser.setNowUser(account);
								MCTools.saveAccount(Login.this, account);
								if (status.equals("success")) {
									Intent intent = new Intent();
									intent.setClass(Login.this,
											LoadingActivity.class);
									startActivity(intent);
								}
								if (status.equals("unjoin")) {
									Intent intent = new Intent();
									Bundle bundle = new Bundle();
									JSONObject community = data
											.getJSONObject("nowcommunity");
									bundle.putString("nowcommunity",
											community.toString());
									intent.putExtras(bundle);
									intent.setClass(Login.this,
											CCommunityActivity.class);
									startActivity(intent);
								}
								finish();
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
							e.printStackTrace();
						}
					}

					@Override
					public void shortIntervalTime() {
						// TODO Auto-generated method stub

					}
				});
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
