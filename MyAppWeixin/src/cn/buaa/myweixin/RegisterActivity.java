package cn.buaa.myweixin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private boolean isAgreeProvision;
	
	private ImageView iv_agreeprovision;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_mobile);
		initView();
	}
	
	public void initView(){
		isAgreeProvision = false;
		iv_agreeprovision = (ImageView) findViewById(R.id.iv_agreeprovision);
	}
	
	//�����һ��
	public void registerMobileNext(View v){
		Toast.makeText(RegisterActivity.this, "Next", Toast.LENGTH_SHORT).show();
	}
	
	//ͬ������
	public void agreeProvision(View v){
		if(isAgreeProvision){
			System.out.println("disagree");
			iv_agreeprovision.setImageResource(R.drawable.reg_checkbox_normal);
			isAgreeProvision = false;
		}else{
			System.out.println("agree");
			iv_agreeprovision.setImageResource(R.drawable.reg_checkbox_checked);
			isAgreeProvision = true;
		}
	}
	
	//���ذ�ť
	public void register_back(View v){
		finish();
	}
}
