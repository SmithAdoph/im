package cn.buaa.myweixin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class RegisterSetPassActivity extends Activity {

	private int checkingCode;
	private String registerNumber;
	private TextView tv_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_setpass);
		// ����activityʱ�Զ����������
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		
		initView();
	}

	public void initView() {
		tv_password = (TextView) findViewById(R.id.tv_registernumber);
		
	}
	
	public void registerCheckingNext(View v){
		Intent intent = new Intent(this,CCommunityActivity.class);
		startActivity(intent);
	}
	// ���ذ�ť
		public void register_back(View v) {
			finish();
		}

}
