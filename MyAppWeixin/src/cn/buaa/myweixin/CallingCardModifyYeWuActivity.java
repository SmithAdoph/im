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
		yewu.add("����ְһ������ҵ��  Сѧ���пα���70/������ѧ���пα�100/��  ���������пα���150/������ע�����������������ѣ����й��˷緶��  ");
		yewu.add("����ְ�������۸�����Сͬѧ��               ������շѣ���1��3��1��4��Сͬѧ�� 50Ԫ/�ˣ�  1��4��1��5/60Ԫ��1��5-1��6/70Ԫ��1��6��1��7/90Ԫ��1��7��1��8/100����1��8���ϵĻ�� ����ע���и����ͬһѧУ�ļӶ�ʮԪ���ְ�����ʦ�ļ���ʮԪÿ�ˣ���������������ֶ�ʮ���ˣ��������Ϳ��࣬�зḻɳ����ս����");
		yewu.add("����ְ��������˼Ҳ�����         һ¥10/�顣��¥20/�飬��¥30/�顣��¥���ϣ�����¥��40/�顣��ע�������д󹷻�ְ��ڼҵ�ÿ�����Ԫ����Ҳ�ɰ������շѣ�ÿ������Ԫ�����˳����Ƶ������Ա����飬���Զ������ǿ�������б��� ��");
		yewu.add("����ְ�ġ����������г�̥�����г�̥���ٱ��������ͨ���г�1/�Ρ��ߵ����г�3/�Ρ���ͨĦ�г�5/��,�ߵ�Ħ��7/�Ρ����нγ�10/�Ρ��������Ļ�ӡ���ע���������Ƹߵ����룬�־��㣬�����󣬱��ưٷְ١�");
		yewu.add("����ְ�塷Ϳ�࣬        �����˼�ǽ��Ϳ�ࡣÿ������ʮԪ��������Լ��ṩ����ע�������������۵�ʽëৣ���ˢ��¥���£�����¥���������д󹷡��ְָ���ڼ���ÿ��Ӷ�ʮԪ���з��ʮԪ�������������������Ҳ������ʦ�����÷�㡣Ů��ʦ��ʮԪÿͰ������ʦ��ʮԪÿͰ��������ʦ������ȭ������������ֵ���ʦ�������ӡ���         ��   �����пͻ��ۼ�������Ԫ��������Żݡ�");
		yewu.add("���˳�����Сѧ��д���٣������ҵ����Сѧ���۸�����С���ѣ���4-8�꣩");
		yewu.add("�н�����ҵ�񣺿������ˣ�ǯ����ˮ�磬�߹�����ǽ��ض���ͱ������ˮ�����ĳ�������׽�飬�����ţ���ǹ���ڳ������֣��������䣬��ɱ��¡�ء���������");
		yewu.add("����ճ��칫�������������Ÿ��౨�����ϣ���ɲ����ཻܼ��������������");
		yewu.add("����ָ����༰����Ա����ճ���Ӫ������ȷ��ǰ������˳����չ�����в������Ա��");
		yewu.add("Ѳ�ӹ�Ͻ��Χ�ڵĻ���������������ע�����豸��������ת��");
		yewu.add("Э�������������ֹ������ԭ�����ֲ����ճ������� ���Լ��ֹܵĹ�������Ҫ���Ρ�");
		yewu.add("����ִ���ܹ�˾�ɹ�����涨��ʵʩϸ���ϸ񰴲ɹ��ƻ��ɹ���������ʱ�����ã����������ʻ�ѹ�Ͳɹ��ɱ����Թ�����Ʒ����Ʊ֤��ȫ��Ʊ����������ʼ�ʱ��");
		yewu.add("����쵼����������������");
		yewu.add("����᳹���Ҽ����������롢���ߺ͹�˾�йع涨����֯�ƶ����ռ����������ںͳ�Զ��չ�滮�����ƶ�������֯��ʩ������");
		yewu.add("����ȫ���˴�ľ�����ȫ���˴�ί��ľ������������ɣ��������Ժ��������������ίԱ��������������ίԱ�����Ρ���Ƴ������鳤��������ҵ�ѫ�º������ƺţ���������������������״̬������ս��״̬��������Ա�");
		yewu.add("����ȫ���˴����ȫ���˴�ί��ľ������£�ֻ�д���Ȩ��û�ж����ľ���Ȩ��");
		yewu.add("�����ټ��������ξֻ�����������ξֳ���ίԱ����飬������������Ǵ��Ĺ�����");
		yewu.add("ȫ���쵼����Ժ��������ȫ���˴��䳣ί�Ḻ��");
		yewu.add("�䲼�ͷ�ֹ�������桢�����ر��������������١��������");
		yewu.add("ͳһָ��ȫ����װ��������������ս�Ժ���װ��������ս����");
		yewu.add("�쵼�͹����й������ž��Ľ��裬�ƶ��滮���ƻ�����֯ʵʩ");
		yewu.add("�����й������ž������ƺͱ��ƣ��涨�ܲ��Լ������������ֺ�������������λ�������ְ��");
		yewu.add("��׼��װ����������װ�����ƺ�����װ����չ�滮���ƻ���Эͬ����Ժ�쵼���������������");
		yewu.add("��������ɨ����һ�����Ҳ������");
		yewu.add("�����ƶ���ִ�л������ߡ�");
		yewu.add("��������ң������������ͨ��");
		yewu.add("�а����Ժ������������");
		yewu.add("׼ʱ���°࣬���������������ĸ�����๤�����������ǣ���ֹ淶�����񣬼��ع�����λ");
		yewu.add("���������ʱ�����������������");
		yewu.add("����˾Ҫ��߱�׼�����������ڵ���ɨ���๤����");
		yewu.add("ÿ����������ڵ�¥���͵�·��������ʩ������ɨ����һ��");
		yewu.add("�����������������������͹�����̨��");
		yewu.add("Э����������С���������������ֿ����˻��£���������������˱��档");
		yewu.add("������ÿ����ɨ�����ڶ��Σ�����ʱ���࣬Ҫ�����ɾ�������ζ������Ӭ��");
		yewu.add("����˾������йز��źͻ������粢�������ú�����ϵ");
		yewu.add("���𽫹�˾�����ߡ�ԭ�򡢲��Ե���Ϣ�����١�������׼ȷ�ش����ֱ���¼�");
		yewu.add("�������ֱ������ճ��������ٿ������ڲ��������顣");
		yewu.add("��չ�����о�����ʱ�˽�����ѧ���д��ڵ���������ѣ���ѧУ��ز��ŷ�ӳ��");
		yewu.add("�����Ȱ�������ҵ��Ը��Ϊ���ڷ������ʿΪ��Ա��");
		yewu.add("ȫ��Ļ�����֯�����һ����ǿ��������֯������ʵ��Ч��������̨�ס�");
		yewu.add("����Ա�ɲ�����˼�����ν����ͷ������ɽ�������֯�������鹤��");
	}

	public void top_back(View v) {
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
		int size = yewu.size();
		int temp=random.nextInt(size);
		while(position==temp){
			temp = random.nextInt(size);
		}
		position = temp;
		modifyyw.setText(yewu.get(position));
	}
}
