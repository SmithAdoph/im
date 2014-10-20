package com.open.welinks.view;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.open.lib.MyLog;
import com.open.welinks.R;
import com.open.welinks.controller.GroupInfoController;
import com.open.welinks.model.Data;
import com.open.welinks.model.Data.Relationship.Friend;
import com.open.welinks.model.FileHandlers;

public class GroupInfoView {
	public Data data = Data.getInstance();
	public String tag = "GroupInfoView";
	public MyLog log = new MyLog(tag, true);

	public Context context;
	public GroupInfoView thisView;
	public GroupInfoController thisController;
	public Activity thisActivity;

	public View backView;
	public TextView backTitleView;

	public View headOptionView;
	public ImageView headIvView;
	public View nickNameOptionView;
	public TextView nickNameView;
	public View businessOptionView;
	public TextView businessView;
	public View converOptionView;
	public View addressOptionView;
	public TextView addressView;
	public View newMessageSettingOptionView;
	public SeekBar newMessageSettingBar;
	public View permissionOptionView;
	public View inCardOptionView;
	public SeekBar inCardBar;
	public View exit2DeleteGroup;
	public View memberListTopView;
	public LinearLayout memberListView;

	public TextView groupMemberCountView;

	public FileHandlers fileHandlers = FileHandlers.getInstance();

	public DisplayImageOptions options;

	public GroupInfoView(Activity thisActivity) {
		this.context = thisActivity;
		this.thisView = this;
		this.thisActivity = thisActivity;
	}

	DisplayMetrics displayMetrics;

	public void initView() {
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.face_man).showImageForEmptyUri(R.drawable.face_man).showImageOnFail(R.drawable.face_man).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).displayer(new RoundedBitmapDisplayer(70)).build();
		displayMetrics = new DisplayMetrics();
		thisActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		this.thisActivity.setContentView(R.layout.activity_group_info);
		this.backView = thisActivity.findViewById(R.id.backView);
		this.backTitleView = (TextView) thisActivity.findViewById(R.id.backTitleView);
		this.backTitleView.setText("群组信息");
		this.headOptionView = thisActivity.findViewById(R.id.headOption);
		this.headIvView = (ImageView) thisActivity.findViewById(R.id.headIv);
		this.nickNameOptionView = thisActivity.findViewById(R.id.nickNameOption);
		this.nickNameView = (TextView) thisActivity.findViewById(R.id.nickNameTx);
		this.businessOptionView = thisActivity.findViewById(R.id.businessOption);
		this.businessView = (TextView) thisActivity.findViewById(R.id.businessTx);
		this.converOptionView = thisActivity.findViewById(R.id.converOption);
		this.addressOptionView = thisActivity.findViewById(R.id.addressOption);
		this.addressView = (TextView) thisActivity.findViewById(R.id.addressTx);
		this.newMessageSettingOptionView = thisActivity.findViewById(R.id.newMessageSettingOption);
		this.newMessageSettingBar = (SeekBar) thisActivity.findViewById(R.id.newMessageSettingBar);
		this.newMessageSettingBar.setTag("newMessageSettingBar");
		this.permissionOptionView = thisActivity.findViewById(R.id.permissionOption);
		this.inCardOptionView = thisActivity.findViewById(R.id.inCardOption);
		this.inCardBar = (SeekBar) thisActivity.findViewById(R.id.inCardBar);
		this.inCardBar.setTag("inCardBar");
		this.exit2DeleteGroup = thisActivity.findViewById(R.id.exit2DeleteGroup);
		this.groupMemberCountView = (TextView) thisActivity.findViewById(R.id.groupMemberCount);
		this.memberListTopView = thisActivity.findViewById(R.id.memberListTop);
		this.memberListView = (LinearLayout) thisActivity.findViewById(R.id.memberList);
	}

	public DisplayImageOptions options0 = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.face_man).showImageForEmptyUri(R.drawable.face_man).showImageOnFail(R.drawable.face_man).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).displayer(new RoundedBitmapDisplayer(70)).build();

	public void setMembersList() {

		int size = thisController.currentGroup.members.size();
		if (size >= 5) {
			size = 5;
		}
		int height = (int) (50 * displayMetrics.density + 0.5f);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height, height);
		params.leftMargin = (int) (14 * displayMetrics.density + 0.5f);
		this.memberListView.removeAllViews();
		for (int i = 0; i < size; i++) {
			ImageView imageView = new ImageView(thisActivity);

			Friend friend = data.relationship.friendsMap.get(thisController.currentGroup.members.get(i));
			fileHandlers.getHeadImage(friend.head, imageView, options0);
			this.memberListView.addView(imageView, params);
		}
	}

	public void setData() {
		this.nickNameView.setText(thisController.currentGroup.name);
		this.fileHandlers.getHeadImage(thisController.currentGroup.icon, this.headIvView, options);
		this.businessView.setText(thisController.currentGroup.description);
		boolean isNotice = false;
		if (data.localStatus.localData != null) {
			if (data.localStatus.localData.newMessagePowerMap != null) {
				if (data.localStatus.localData.newMessagePowerMap.get(thisController.currentGroup.gid + "") != null) {
					isNotice = data.localStatus.localData.newMessagePowerMap.get(thisController.currentGroup.gid + "");
				} else {
					isNotice = true;
				}
			} else {
				isNotice = true;
				data.localStatus.localData.newMessagePowerMap = new HashMap<String, Boolean>();
			}
		} else {
			isNotice = true;
		}
		if (!isNotice) {
			this.newMessageSettingBar.setProgress(0);
		} else {
			this.newMessageSettingBar.setProgress(100);
		}
		groupMemberCountView.setText(thisController.currentGroup.members.size() + "人");
		setMembersList();
	}
}
