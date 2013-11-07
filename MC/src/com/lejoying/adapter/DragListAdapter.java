package com.lejoying.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lejoying.mc.R;

public class DragListAdapter extends ArrayAdapter<String> {

	private List<String> listTag = null;

	public DragListAdapter(Context context, List<String> objects,
			List<String> tags) {
		super(context, 0, objects);
		this.listTag = tags;
	}

	@Override
	public boolean isEnabled(int position) {
		if (listTag.contains(getItem(position))) {
			return false;
		}
		return super.isEnabled(position);
	}

	// �������ǵ����ģ���������Ϊ���췽���ڶ�����������ȡ��ÿһ��list��Ŀ������д�ͻ�ִ�У�
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (listTag.contains(getItem(position))) {
			view = LayoutInflater.from(getContext()).inflate(
					R.layout.group_list_item_tag, null);
		} else {
			view = LayoutInflater.from(getContext()).inflate(
					R.layout.group_list_item, null);
		}
		TextView textView = (TextView) view
				.findViewById(R.id.group_list_item_text);
		textView.setText(getItem(position));
		return view;
	}

	public Object getList() {
		// TODO Auto-generated method stub
		return listTag;
	}
	
}
