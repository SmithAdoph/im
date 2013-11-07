package com.lejoying.mc;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.lejoying.adapter.DragListAdapter;

public class BusinessCardActivity_test extends Activity {

	private List<String> list = null; // �����ϵ�����ݵ�list
	private List<String> listtag = null; // �����ĸ�����ݵ�list
	private DragListAdapter adapter = null; // �Զ����Adapter����
	private ListView listView = null; // ��layout���õ���listview

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.businesscard);
		setData(); // ��ʼ����ϵ�˺�����ĸ������
		adapter = new DragListAdapter(this, list, listtag); // [��Ҫ],��ÿһ��item��д�Ű�ͱ༭�õ���Ϣview֮��ŵ�adapter����
		// �������Զ����adapter�ŵ�listview����
		listView = (ListView) findViewById(R.id.lv_test);
		listView.setAdapter(adapter);
	}

	public void setData() {
		list = new ArrayList<String>();
		listtag = new ArrayList<String>();
		list.add("A");
		listtag.add("A");
		for (int i = 0; i < 4; i++) {
			list.add("�����ε�" + i);
		}
		list.add("B");
		listtag.add("B");
		for (int i = 0; i < 4; i++) {
			list.add("��ʿ��" + i);
		}
		list.add("C");
		listtag.add("C");
		for (int i = 0; i < 4; i++) {
			list.add("����" + i);
		}
	}
}
