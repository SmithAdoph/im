package cn.buaa.myweixin;

import cn.buaa.myweixin.apiutils.MCTools;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainTopCenterDialog extends Activity {
	private LinearLayout layout;

	private TextView tv_communityinfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_top_center_dialog);

		/*layout = (LinearLayout) findViewById(R.id.main_top_center_dialog_layout);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "��ʾ����������ⲿ�رմ��ڣ�",
						Toast.LENGTH_SHORT).show();
			}
		});*/
		initView();
	}
	
	public void initView(){
		tv_communityinfo = (TextView) findViewById(R.id.tv_communityinfo);
		if(MCTools.INNEWCOMMUNITY){
			tv_communityinfo.setText("���뱾��վ");
		}else{
			tv_communityinfo.setText("��վ��Ϣ");
		}
	}

	// �������λ������˵�
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	public void showCCommunity(View v) {
		Intent intent = new Intent(MainTopCenterDialog.this,
				CCommunityActivity.class);
		startActivity(intent);
		finish();
	}

}
