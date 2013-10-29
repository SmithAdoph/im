package cn.buaa.myweixin;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import cn.buaa.myweixin.apiutils.MCTools;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ExitFromSettings extends Activity {
	// private MyDialog dialog;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exit_dialog_from_settings);
		// dialog=new MyDialog(this);
		layout = (LinearLayout) findViewById(R.id.exit_layout2);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "��ʾ����������ⲿ�رմ��ڣ�",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	public void exitbutton1(View v) {
		this.finish();
	}

	public void exitbutton0(View v) {
		MCTools.saveAccount(this, null);
		this.finish();
		MainWeixin.instance.finish();// �ر�Main ���Activity
	}

}
