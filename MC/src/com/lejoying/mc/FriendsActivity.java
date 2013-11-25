package com.lejoying.mc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lejoying.adapter.CircleAdapter;
import com.lejoying.listener.CircleMenuItemClickListener;
import com.lejoying.mcutils.CircleMenu;
import com.lejoying.mcutils.Friend;
import com.lejoying.mcutils.MenuEntity;

public class FriendsActivity extends Activity {
	// DEFINITION view
	private LinearLayout ll_content;
	private RelativeLayout rl_control;

	// DEFINITION object
	private CircleAdapter circleAdapter;
	private CircleMenu cm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends);
		initView();
	}

	@SuppressLint("NewApi")
	public void initView() {
		// INIT view
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		rl_control = (RelativeLayout) findViewById(R.id.rl_control);

		// INIT object
		List<Friend> friends1 = new ArrayList<Friend>();
		List<Friend> friends2 = new ArrayList<Friend>();

		// TODO
		Map<String, List<Friend>> circlefriends = new HashMap<String, List<Friend>>();
		for (int i = 0; i < 100; i++) {
			Friend friend = new Friend();
			friend.setNickName("����" + i);
			if (i / 50 < 1) {
				friends1.add(friend);
			} else {
				friends2.add(friend);
			}
		}
		circlefriends.put("����Ȧ1", friends1);
		circlefriends.put("����Ȧ2", friends2);
		circlefriends.put("����Ȧ3", friends1);
		circlefriends.put("����Ȧ4", friends2);
		circlefriends.put("����Ȧ5", friends1);

		circleAdapter = new CircleAdapter(circlefriends, ll_content,
				rl_control, this);

		circleAdapter.createView();

		cm = new CircleMenu(this);
		List<MenuEntity> list = new ArrayList<MenuEntity>();
		list.add(new MenuEntity(R.drawable.test_menu_item1, "ɨһɨ"));
		list.add(new MenuEntity(R.drawable.test_menu_item2, "��Ϣ"));
		list.add(new MenuEntity(R.drawable.test_menu_item3, "����"));
		list.add(new MenuEntity(R.drawable.test_menu_item4,
				CircleMenu.CIRCLE_MORE));
		cm.showMenu(CircleMenu.SHOW_TOP, list, false);
		List<MenuEntity> list2 = new ArrayList<MenuEntity>();
		list2.add(new MenuEntity(R.drawable.test_menu_item5, "ɨһɨ"));
		list2.add(new MenuEntity(R.drawable.test_menu_item6, "��Ϣ"));
		list2.add(new MenuEntity(R.drawable.test_menu_item1, "�ҵ���Ƭ"));
		list2.add(new MenuEntity(R.drawable.test_menu_item2,
				CircleMenu.CIRCLE_BACK));
		list2.add(new MenuEntity(R.drawable.test_menu_item3, "����"));
		list2.add(new MenuEntity(R.drawable.test_menu_item4, "�ʽ��˻�"));
		cm.addMore(list2);

		cm.setCircleMenuItemClickListener(new CircleMenuItemClickListener() {
			@Override
			public void onItemClick(int item, ImageView icon, TextView text) {
				if (item == 1) {
					Intent intent = new Intent(FriendsActivity.this,
							ScanQRCodeActivity.class);
					startActivity(intent);
				} else if (item == 2) {
					Intent intent = new Intent(FriendsActivity.this,
							MessagesActivity.class);
					startActivity(intent);
				} else if (item == 13) {
					Intent intent = new Intent(FriendsActivity.this,
							BusinessCardActivity.class);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public void finish() {
		if (cm.isShow()) {
			cm.cancelMenu();
			return;
		}
		if (circleAdapter.getEditMode()) {
			circleAdapter.exitEdit();
		} else {
			super.finish();
		}
	}
}
