package com.lejoying.wxgs.activity.mode.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lejoying.wxgs.R;
import com.lejoying.wxgs.activity.mode.MainModeManager;
import com.lejoying.wxgs.activity.utils.CommonNetConnection;
import com.lejoying.wxgs.activity.view.widget.Alert;
import com.lejoying.wxgs.activity.view.widget.CircleMenu;
import com.lejoying.wxgs.activity.view.widget.Alert.AlertInputDialog;
import com.lejoying.wxgs.app.MainApplication;
import com.lejoying.wxgs.app.adapter.FriendGroupsGridViewAdapter;
import com.lejoying.wxgs.app.data.API;
import com.lejoying.wxgs.app.data.Data;
import com.lejoying.wxgs.app.data.entity.Friend;
import com.lejoying.wxgs.app.data.entity.Group;
import com.lejoying.wxgs.app.handler.DataHandler.Modification;
import com.lejoying.wxgs.app.handler.FileHandler.FileResult;
import com.lejoying.wxgs.app.handler.NetworkHandler.NetConnection;
import com.lejoying.wxgs.app.handler.NetworkHandler.Settings;
import com.lejoying.wxgs.app.service.PushService;

public class BusinessCardFragment extends BaseFragment {

	public int mStatus;
	public int groupNum;
	public Friend mShowFriend;
	public static final int SHOW_SELF = 1;
	public static final int SHOW_FRIEND = 2;
	public static final int SHOW_TEMPFRIEND = 3;

	MainApplication app = MainApplication.getMainApplication();
	MainModeManager mMainModeManager;
	private static final int SCROLL = 0x51;

	View mContent;
	View tv_group;
	View tv_square;
	View tv_msg;
	LayoutInflater mInflater;

	private TextView tv_spacing;
	private TextView tv_spacing2;
	private TextView tv_mainbusiness;
	private RelativeLayout rl_show;
	private ScrollView sv_content;

	// DEFINITION object
	private Handler handler;
	private boolean stopSend;

	FriendGroupsGridViewAdapter adapter = null;

	public void setMode(MainModeManager mainMode) {
		mMainModeManager = mainMode;
	}

	@SuppressLint("HandlerLeak")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		mContent = inflater.inflate(R.layout.f_businesscard, null);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				int what = msg.what;
				switch (what) {
				case SCROLL:
					if (sv_content.getScrollY() > 10) {
						tv_mainbusiness.setMaxLines(100);
					}
					if (sv_content.getScrollY() < 10) {
						tv_mainbusiness.setMaxLines(3);
					}
					break;
				}
				super.handleMessage(msg);
			}
		};

		tv_spacing = (TextView) mContent.findViewById(R.id.tv_spacing);
		tv_spacing2 = (TextView) mContent.findViewById(R.id.tv_spacing2);
		tv_mainbusiness = (TextView) mContent
				.findViewById(R.id.tv_mainbusiness);
		rl_show = (RelativeLayout) mContent.findViewById(R.id.rl_show);

		initData();

		AsyncTask<Integer, Integer, Boolean> asyncTask = new AsyncTask<Integer, Integer, Boolean>() {

			@Override
			protected Boolean doInBackground(Integer... params) {
				while (rl_show.getHeight() == 0)
					;
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				DisplayMetrics dm = new DisplayMetrics();
				getActivity().getWindowManager().getDefaultDisplay()
						.getMetrics(dm);

				Rect frame = new Rect();
				getActivity().getWindow().getDecorView()
						.getWindowVisibleDisplayFrame(frame);
				int statusBarHeight = frame.top;
				sv_content = (ScrollView) mContent
						.findViewById(R.id.sv_content);

				tv_spacing.setHeight((int) (dm.heightPixels
						- rl_show.getHeight() - statusBarHeight - tv_spacing2
						.getHeight()));

				sv_content.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						stopSend = true;
						new Thread() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								while (stopSend) {
									handler.sendEmptyMessage(SCROLL);
									int start = sv_content.getScrollY();
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									int stop = sv_content.getScrollY();
									if (start == stop) {
										stopSend = false;
									}
								}

								super.run();
							}

						}.start();
						return false;
					}
				});
			}
		};
		asyncTask.execute();
		return mContent;
	}

	@Override
	public void onResume() {
		CircleMenu.showBack();
		super.onResume();
	}

	public void initData() {
		ViewGroup group = (ViewGroup) mContent.findViewById(R.id.ll_content);
		tv_group = generateFriendGroup();
		tv_square = mContent.findViewById(R.id.tv_square_layout);
		tv_msg = mContent.findViewById(R.id.tv_msg_layout);
		final ImageView iv_head = (ImageView) mContent
				.findViewById(R.id.iv_head);

		TextView tv_squarepanel_name = (TextView) mContent
				.findViewById(R.id.tv_squarepanel_name);
		TextView tv_grouppanel_name = (TextView) mContent
				.findViewById(R.id.tv_grouppanel_name);
		TextView tv_msgpanel_name = (TextView) mContent
				.findViewById(R.id.tv_msgpanel_name);
		TextView tv_nickname = (TextView) mContent
				.findViewById(R.id.tv_nickname);
		TextView tv_phone = (TextView) mContent.findViewById(R.id.tv_phone);
		TextView tv_mainbusiness = (TextView) mContent
				.findViewById(R.id.tv_mainbusiness);
		TextView tv_id = (TextView) mContent.findViewById(R.id.tv_id);
		TextView tv_sex = (TextView) mContent.findViewById(R.id.tv_sex);
		Button button1 = (Button) mContent.findViewById(R.id.button1);
		Button button2 = (Button) mContent.findViewById(R.id.button2);
		Button button3 = (Button) mContent.findViewById(R.id.button3);
		String fileName = "", phone = "";

		if (mStatus == SHOW_TEMPFRIEND) {
			if (mShowFriend.sex.equals("男")) {
				tv_grouppanel_name.setText("他的群组");
				tv_msgpanel_name.setText("他的广播");

			} else {
				tv_grouppanel_name.setText("她的群组");
				tv_msgpanel_name.setText("她的广播");
			}
			tv_squarepanel_name.setText("常驻广场");
			tv_id.setText(String.valueOf(mShowFriend.id));
			tv_sex.setText(mShowFriend.sex);
			tv_nickname.setText(mShowFriend.nickName);
			if (mShowFriend.phone.length() == 11) {
				phone = mShowFriend.phone.substring(0, 3) + "****"
						+ mShowFriend.phone.substring(7);
			} else {
				phone = mShowFriend.phone;
			}

			tv_phone.setText(phone);
			fileName = mShowFriend.head;
			tv_mainbusiness.setText(mShowFriend.mainBusiness);
			group.removeView(button3);
			button1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mMainModeManager.mAddFriendFragment.mAddFriend = mShowFriend;
					mMainModeManager
							.showNext(mMainModeManager.mAddFriendFragment);
				}
			});
			button2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

				}
			});
		} else if (mStatus == SHOW_SELF) {
			button1.setText("修改个人信息");
			button2.setText("退出登录");
			tv_id.setText(String.valueOf(app.data.user.id));
			tv_sex.setText(app.data.user.sex);
			tv_nickname.setText(app.data.user.nickName);
			fileName = app.data.user.head;
			tv_phone.setText(app.data.user.phone);
			tv_mainbusiness.setText(app.data.user.mainBusiness);
			group.removeView(button3);
			group.removeView(tv_group);
			group.removeView(tv_msg);
			group.removeView(tv_square);
			button1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mMainModeManager.showNext(mMainModeManager.mModifyFragment);
				}
			});

			button2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Alert.createDialog(getActivity())
							.setTitle("退出登录后您将接收不到任何消息，确定要退出登录吗？")
							.setOnConfirmClickListener(
									new AlertInputDialog.OnDialogClickListener() {

										@Override
										public void onClick(
												AlertInputDialog dialog) {
											Intent service = new Intent(
													getActivity(),
													PushService.class);
											service.putExtra("operation",
													"stop");
											getActivity().startService(service);
										}
									}).show();
				}
			});

		} else if (mStatus == SHOW_FRIEND) {
			if (mShowFriend.sex.equals("男")) {
				tv_grouppanel_name.setText("他的群组");
				tv_msgpanel_name.setText("他的广播");
			} else {
				tv_grouppanel_name.setText("她的群组");
				tv_msgpanel_name.setText("她的广播");
			}
			tv_squarepanel_name.setText("常驻广场");
			button1.setText("发起聊天");
			button2.setText("修改备注");
			button3.setText("解除好友关系");

			tv_id.setText(String.valueOf(mShowFriend.id));
			tv_sex.setText(mShowFriend.sex);
			tv_nickname.setText(mShowFriend.nickName);
			if (mShowFriend.phone.length() == 11) {
				phone = mShowFriend.phone.substring(0, 3) + "****"
						+ mShowFriend.phone.substring(7);
			} else {
				phone = mShowFriend.phone;
			}

			tv_phone.setText(phone);
			fileName = mShowFriend.head;
			tv_mainbusiness.setText(mShowFriend.mainBusiness);
			button1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					mMainModeManager.mChatFragment.mStatus = ChatFriendFragment.CHAT_FRIEND;
					mMainModeManager.mChatFragment.mNowChatFriend = app.data.friends
							.get(mShowFriend.phone);
					mMainModeManager.showNext(mMainModeManager.mChatFragment);
				}
			});
			button2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

				}
			});
			button3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Alert.createDialog(getActivity())
							.setTitle(
									"确定解除和" + mShowFriend.nickName + "的好友关系吗？")
							.setOnConfirmClickListener(
									new AlertInputDialog.OnDialogClickListener() {

										@Override
										public void onClick(
												AlertInputDialog dialog) {
											app.networkHandler
													.connection(new CommonNetConnection() {

														@Override
														public void success(
																JSONObject jData) {
															app.dataHandler
																	.exclude(new Modification() {

																		@Override
																		public void modifyData(
																				Data data) {
																			data.lastChatFriends
																					.remove(mShowFriend.phone);
																			data.newFriends
																					.remove(mShowFriend);
																			data.friends
																					.remove(mShowFriend.phone);
																			for (String rid : data.circles) {
																				data.circlesMap
																						.get(rid).phones
																						.remove(mShowFriend);
																			}
																		}

																		@Override
																		public void modifyUI() {
																			// TODO
																			// refresh
																			if (mMainModeManager.mCirclesFragment
																					.isAdded()) {
																				mMainModeManager.mCirclesFragment
																						.notifyViews();
																			}

																		}
																	});
														}

														@Override
														protected void settings(
																Settings settings) {
															settings.url = API.DOMAIN
																	+ API.RELATION_DELETEFRIEND;
															Map<String, String> params = new HashMap<String, String>();
															params.put(
																	"phone",
																	app.data.user.phone);
															params.put(
																	"accessKey",
																	app.data.user.accessKey);
															params.put(
																	"phoneto",
																	"[\""
																			+ mShowFriend.phone
																			+ "\"]");
															settings.params = params;
														}
													});
											mMainModeManager.back();
										}
									}).show();

				}
			});
		}
		final String headFileName = fileName;
		app.fileHandler.getHeadImage(headFileName, new FileResult() {
			@Override
			public void onResult(String where) {
				iv_head.setImageBitmap(app.fileHandler.bitmaps
						.get(headFileName));
			}
		});

	}

	View generateFriendGroup() {
		float density;
		int listSize;
		GridView gridView;
		View groupView = mContent.findViewById(R.id.tv_group_layout);
		gridView = (GridView) mContent.findViewById(R.id.tv_gridview);

		// inflater = (LayoutInflater) this
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		List<Group> groups = getFriendGroups();
		if (groupNum % 6 == 0) {
			listSize = groupNum / 2;
		} else {
			if (groupNum % 6 > 3) {
				listSize = groupNum / 6 * 3 + 3;
			} else {
				listSize = groupNum / 6 * 3 + groupNum % 6;
			}
		}
		System.out.println(groupNum);
		System.out.println(listSize);
		adapter = new FriendGroupsGridViewAdapter(mInflater, groups);
		if (mStatus != SHOW_SELF) {
			gridView.setAdapter(adapter);

			DisplayMetrics outMetrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay()
					.getMetrics(outMetrics);
			density = outMetrics.density; // 像素密度

			ViewGroup.LayoutParams params = gridView.getLayoutParams();
			int itemWidth = (int) (90 * density);
			int spacingWidth = (int) (4 * density);

			params.width = itemWidth * listSize + (listSize - 1) * spacingWidth;
			gridView.setStretchMode(GridView.NO_STRETCH); // 设置为禁止拉伸模式
			gridView.setNumColumns(listSize);
			gridView.setHorizontalSpacing(spacingWidth);
			gridView.setColumnWidth(itemWidth);
			gridView.setLayoutParams(params);

			// int size = groups.size();
			// DisplayMetrics dm = new DisplayMetrics();
			// getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
			//
			// float density = dm.density;
			// int allWidth = (int) (110 * size * density);
			// int itemWidth = (int) (100 * density);
			// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			// allWidth, LinearLayout.LayoutParams.FILL_PARENT);
			// gridView.setLayoutParams(params);
			// gridView.setColumnWidth(itemWidth);
			// gridView.setHorizontalSpacing(10);
			// gridView.setStretchMode(GridView.NO_STRETCH);
			// gridView.setNumColumns(size);
		}

		return groupView;
	}

	public List<Group> getFriendGroups() {
		final List<Group> groups = new ArrayList<Group>();
		NetConnection netConnection = new CommonNetConnection() {

			@Override
			protected void settings(Settings settings) {
				settings.url = API.DOMAIN + API.GROUP_GETUSERGROUPS;
				Map<String, String> params = new HashMap<String, String>();
				params.put("phone", app.data.user.phone);
				params.put("accessKey", app.data.user.accessKey);
				params.put("target", mShowFriend.phone);
				settings.params = params;
			}

			@Override
			public void success(final JSONObject jData) {

				app.dataHandler.exclude(new Modification() {

					@Override
					public void modifyData(Data data) {
						try {
							JSONArray jGroups = jData.getJSONArray("groups");
							for (int i = 0; i < jGroups.length(); i++) {
								try {
									JSONObject jGroup = jGroups
											.getJSONObject(i);
									Group group = new Group();
									group.gid = jGroup.getInt("gid");
									group.name = jGroup.getString("name");
									// Object[] groupAndFriends =
									// generateGroupFromJSON(jGroup);
									if (group != null) {
										groups.add(group);
									}
								} catch (JSONException e) {
								}
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void modifyUI() {
						groupNum = groups.size();
						adapter.notifyDataSetChanged();
					}
				});

			}
		};
		app.networkHandler.connection(netConnection);

		return groups;
	}

}
