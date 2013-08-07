package cn.buaa.myweixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CallingCardModifyYeWuActivity extends Activity {

	private EditText modifyyw;
	private TextView yewulength;
	private List<String> yewu;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.callingcard_modify_yewu);
		initView();
		initData();
		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}

	public void initView() {
		modifyyw = (EditText) findViewById(R.id.modifyyewu);
		yewulength = (TextView) findViewById(R.id.yewulength);
		modifyyw.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				yewulength.setText(s.length() + "/240");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void initData() {
		yewu = new ArrayList<String>();
		yewu.add("ѧУ�ΰ����ã����������������Ա���룬һ������ϰ�¿κ�һ��������ѧУ��С�����Ļ�ʱ����������������ѧ���ҳ���ѧУ���ۣ�ָ��У��û��װ·��ʲô�ġ� У���ظ�������·�Ƶĵط�����Ҳ��ȥ����");
		yewu.add("�ż�ʱ��ѧУ�쵼�����ǿ���˵��ͬѧ�ǣ�����ǧ��Ҫע��������ȫ��������ȫ�޷����������һ��������һ����������һ�ֶ��ǳ����á���");
		yewu.add("���ǰ��и��ܲ����˴�����ͬѧ����һ����ͻȻˤ����Ҫ�оȻ�������ҿ���ȷʵͦ��ֻ������д�Ÿ��������ġ�");
		yewu.add("Ϊ�˽�ʡ�����ˮ�ѣ���ÿ�쵽��λ֮����ϴ����ˢ�����κ��ӡ���ޡ�");
		yewu.add("�ҴӼ������һ�����裬����û�˵�ʱ�򣬾�����ˮ�������ˮ�ݽš�");
	}

	public void chat_back(View v) {
		finish();
	}

	public void save(View v) {
		if (modifyyw.getText().toString().equals("")) {
			return;
		}
		Intent intent = new Intent(this, CallingCardModifyActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("item", CallingCardModifyActivity.MODIFY_YEWU);
		bundle.putString("value", modifyyw.getText().toString());
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void random(View v) {
		Random random = new Random();
		int temp=random.nextInt(5);
		while(position==temp){
			temp = random.nextInt(5);
		}
		position = temp;
		modifyyw.setText(yewu.get(position));
	}
}
