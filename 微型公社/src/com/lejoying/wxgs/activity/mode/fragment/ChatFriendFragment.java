package com.lejoying.wxgs.activity.mode.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lejoying.wxgs.R;
import com.lejoying.wxgs.activity.MapStorageDirectoryActivity;
import com.lejoying.wxgs.activity.PicAndVoiceDetailActivity;
import com.lejoying.wxgs.activity.mode.MainModeManager;
import com.lejoying.wxgs.activity.utils.CommonNetConnection;
import com.lejoying.wxgs.activity.utils.ExpressionUtil;
import com.lejoying.wxgs.activity.utils.TimeUtils;
import com.lejoying.wxgs.activity.view.SampleView;
import com.lejoying.wxgs.activity.view.widget.Alert;
import com.lejoying.wxgs.activity.view.widget.Alert.AlertInputDialog;
import com.lejoying.wxgs.activity.view.widget.Alert.AlertInputDialog.OnDialogClickListener;
import com.lejoying.wxgs.app.MainApplication;
import com.lejoying.wxgs.app.adapter.AnimationAdapter;
import com.lejoying.wxgs.app.data.API;
import com.lejoying.wxgs.app.data.Data;
import com.lejoying.wxgs.app.data.entity.Friend;
import com.lejoying.wxgs.app.data.entity.Group;
import com.lejoying.wxgs.app.data.entity.Message;
import com.lejoying.wxgs.app.handler.DataHandler.Modification;
import com.lejoying.wxgs.app.handler.NetworkHandler.Settings;
import com.lejoying.wxgs.app.handler.OSSFileHandler;
import com.lejoying.wxgs.app.handler.OSSFileHandler.FileInterface;
import com.lejoying.wxgs.app.handler.OSSFileHandler.FileMessageInfoInterface;
import com.lejoying.wxgs.app.handler.OSSFileHandler.FileMessageInfoSettings;
import com.lejoying.wxgs.app.handler.OSSFileHandler.FileResult;
import com.lejoying.wxgs.app.handler.OSSFileHandler.FileSettings;
import com.lejoying.wxgs.app.handler.OSSFileHandler.ImageMessageInfo;
import com.lejoying.wxgs.app.handler.OSSFileHandler.UploadFileInterface;
import com.lejoying.wxgs.app.handler.OSSFileHandler.UploadFileSettings;

public class ChatFriendFragment extends BaseFragment {

	MainApplication app = MainApplication.getMainApplication();
	MainModeManager mMainModeManager;

	public static final int CHAT_FRIEND = 1;
	public static final int CHAT_GROUP = 2;

	public int mStatus;

	public Friend mNowChatFriend;
	public Group mNowChatGroup;

	private View mContent;
	public BaseAdapter mAdapter;

	public MediaRecorder recorder;
	public MediaPlayer mPlayer;
	public List<String> voice_list;
	public int play_order = 0;
	public double voice_length = 0;
	public long startTime = 0;

	int RESULT_SELECTPICTURE = 0x124;
	int RESULT_TAKEPICTURE = 0xa3;
	int RESULT_CATPICTURE = 0x3d;
	int messageNum = 1;
	int height, width, dip;
	float density;

	boolean VOICE_PLAYSTATUS = false;
	boolean VOICE_SAVESTATUS = false;
	boolean isSELECTPICTURE = false;
	LayoutInflater mInflater;

	Map<String, Bitmap> tempImages = new Hashtable<String, Bitmap>();

	View iv_send;
	View iv_more;
	View iv_more_select;
	EditText editText_message;
	RelativeLayout rl_chatbottom;
	RelativeLayout rl_message;
	RelativeLayout rl_select;
	RelativeLayout rl_audiopanel;
	View rl_selectpicture;
	View rl_makeaudio;
	TextView tv_voice;
	TextView tv_voice_start;
	ImageView iv_voice_send;
	ImageView iv_voice_play;
	TextView tv_voice_timelength;
	OnTouchListener mOnTouchListener;

	RelativeLayout rl_face;
	LinearLayout ll_facepanel;
	LinearLayout ll_facemenu;
	RelativeLayout rl_selectedface;
	ViewPager chat_vPager;
	int chat_vPager_now = 0;
	ImageView iv_face_left;
	ImageView iv_face_right;
	ImageView iv_face_delete;
	List<ImageView> faceMenuShowList;
	List<List<String>> faceNameList;
	static Map<String, String> expressionFaceMap = new HashMap<String, String>();
	List<String[]> faceNamesList;
	public static String faceRegx = "[\\[,<]{1}[\u4E00-\u9FFF]{1,5}[\\],>]{1}|[\\[,<]{1}[a-zA-Z0-9]{1,5}[\\],>]{1}";

	View groupTopBar;
	View groupTopBar_back;
	TextView textView_groupName;
	TextView textView_memberCount;
	LinearLayout linearlayout_members;
	View groupSetting;

	View groupCenterBar;
	TextView textView_groupNameAndMemberCount;
	LinearLayout linearlayout;

	int beforeHeight;
	int beforeLineHeight;

	final int MAXTYPE_COUNT = 3;

	public int showFirstPosition;

	public ListView chatContent;

	public void setMode(MainModeManager mainMode) {
		mMainModeManager = mainMode;
	}

	public void initShowFirstPosition() {
		int initShowCount = 10;
		if (mNowChatFriend.notReadMessagesCount > 10) {
			initShowCount = mNowChatFriend.notReadMessagesCount;
		}
		int messagesTotalCount = mNowChatFriend.messages.size();
		if (messagesTotalCount < 10) {
			initShowCount = messagesTotalCount;
		}
		showFirstPosition = messagesTotalCount - initShowCount;
	}

	@Override
	public void onResume() {
		mMainModeManager.handleMenu(false);
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	View headView;
	View footerView;

	public float dp2px(float px) {
		float dp = getActivity().getResources().getDisplayMetrics().density
				* px + 0.5f;
		return dp;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		// voice
		voice_list = new ArrayList<String>();
		faceMenuShowList = new ArrayList<ImageView>();
		faceNamesList = new ArrayList<String[]>();
		mContent = inflater.inflate(R.layout.f_chat, null);
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		density = dm.density;
		dip = (int) (40 * density + 0.5f);
		height = dm.heightPixels;
		width = dm.widthPixels;
		chatContent = (ListView) mContent.findViewById(R.id.chatContent);

		if (headView == null) {
			AbsListView.LayoutParams params = new AbsListView.LayoutParams(
					android.widget.AbsListView.LayoutParams.WRAP_CONTENT,
					(int) dp2px(35));
			headView = new View(getActivity());
			headView.setLayoutParams(params);
		}
		if (footerView == null) {
			footerView = new View(getActivity());
		}
		chatContent.addHeaderView(headView);
		chatContent.addFooterView(footerView);

		iv_send = mContent.findViewById(R.id.iv_send);
//		iv_more = mContent.findViewById(R.id.iv_more);
		iv_more_select = mContent.findViewById(R.id.iv_more_select);
		editText_message = (EditText) mContent.findViewById(R.id.et_message);
		rl_chatbottom = (RelativeLayout) mContent
				.findViewById(R.id.chat_bottom_bar);
		rl_message = (RelativeLayout) mContent.findViewById(R.id.rl_message);
		rl_select = (RelativeLayout) mContent.findViewById(R.id.rl_select);
		rl_audiopanel = (RelativeLayout) mContent
				.findViewById(R.id.rl_audiopanel);
		rl_selectpicture = mContent.findViewById(R.id.rl_selectpicture);
		rl_makeaudio = mContent.findViewById(R.id.rl_makeaudio);
		tv_voice = (TextView) mContent.findViewById(R.id.tv_voice);
		tv_voice_start = (TextView) mContent.findViewById(R.id.tv_voice_start);
		iv_voice_send = (ImageView) mContent.findViewById(R.id.iv_voice_send);
		iv_voice_play = (ImageView) mContent.findViewById(R.id.iv_voice_play);
		tv_voice_timelength = (TextView) mContent
				.findViewById(R.id.tv_voice_timelength);
		rl_face = (RelativeLayout) mContent.findViewById(R.id.rl_face);
		ll_facepanel = (LinearLayout) mContent.findViewById(R.id.ll_facepanel);
		ll_facemenu = (LinearLayout) mContent.findViewById(R.id.ll_facemenu);
		rl_selectedface = (RelativeLayout) mContent
				.findViewById(R.id.rl_selectedface);
		chat_vPager = (ViewPager) mContent.findViewById(R.id.chat_vPager);
		iv_face_left = (ImageView) mContent.findViewById(R.id.iv_face_left);
		iv_face_right = (ImageView) mContent.findViewById(R.id.iv_face_right);
		iv_face_delete = (ImageView) mContent.findViewById(R.id.iv_face_delete);

		groupTopBar = mContent.findViewById(R.id.relativeLayout_topbar);
		groupTopBar_back = mContent.findViewById(R.id.backview);
		textView_groupName = (TextView) mContent
				.findViewById(R.id.textView_groupName);
		textView_memberCount = (TextView) mContent
				.findViewById(R.id.textView_memberCount);

		groupCenterBar = mContent.findViewById(R.id.relativeLayout_group);
		textView_groupNameAndMemberCount = (TextView) mContent
				.findViewById(R.id.textView_groupNameAndMemberCount);
		linearlayout = (LinearLayout) groupCenterBar
				.findViewById(R.id.linearlayout_user);
		groupSetting = groupCenterBar.findViewById(R.id.groupSetting);

		linearlayout_members = (LinearLayout) mContent
				.findViewById(R.id.linearlayout_members);

		if (mStatus == CHAT_FRIEND) {
			groupTopBar.setVisibility(View.VISIBLE);
			if (!mNowChatFriend.alias.equals("")
					&& mNowChatFriend.alias != null) {
				textView_groupName.setText(mNowChatFriend.alias);
			} else {
				textView_groupName.setText(mNowChatFriend.nickName);
			}
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			// layoutParams.setMargins(45, 0, 0, 0);
			layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
			textView_groupName.setLayoutParams(layoutParams);
			textView_memberCount.setText("");
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					60, 60);
			params.rightMargin = 20;
			final ImageView iv_head = new ImageView(getActivity());
			final String headFileName = mNowChatFriend.head;
			app.fileHandler.getHeadImage(headFileName, mNowChatFriend.sex,
					new FileResult() {
						@Override
						public void onResult(String where, Bitmap bitmap) {
							iv_head.setImageBitmap(app.fileHandler.bitmaps
									.get(headFileName));
						}
					});
			iv_head.setLayoutParams(params);
			linearlayout_members.addView(iv_head);
			initShowFirstPosition();
			if (mNowChatFriend.notReadMessagesCount != 0) {
				app.dataHandler.exclude(new Modification() {

					@Override
					public void modifyData(Data data) {
						data.friends.get(mNowChatFriend.phone).notReadMessagesCount = 0;
					}

					@Override
					public void modifyUI() {
						// mMainModeManager.mCirclesFragment.notifyViews();
						mMainModeManager.mChatMessagesFragment.notifyViews();
					}
				});
			}
		} else if (mStatus == CHAT_GROUP) {
			groupTopBar.setVisibility(View.VISIBLE);
			textView_groupName.setText(mNowChatGroup.name);
			textView_memberCount.setText("(" + mNowChatGroup.members.size()
					+ "人)");
			int topShowCount = mNowChatGroup.members.size() < 4 ? mNowChatGroup.members
					.size() : 4;
			for (int i = 0; i < topShowCount; i++) {
				final ImageView iv_head = new ImageView(getActivity());
				final String headFileName = app.data.groupFriends
						.get(mNowChatGroup.members.get(i)).head;
				app.fileHandler
						.getHeadImage(headFileName, app.data.groupFriends
								.get(mNowChatGroup.members.get(i)).sex,
								new FileResult() {
									@Override
									public void onResult(String where,
											Bitmap bitmap) {
										iv_head.setImageBitmap(app.fileHandler.bitmaps
												.get(headFileName));
									}
								});
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						40, 40);
				if (i != 3)
					params.setMargins(0, 0, 10, 0);
				iv_head.setLayoutParams(params);
				linearlayout_members.addView(iv_head);
			}

			textView_groupNameAndMemberCount.setText(mNowChatGroup.name + "("
					+ mNowChatGroup.members.size() + "人)");
			for (int i = 0; i < mNowChatGroup.members.size(); i++) {
				final Friend friend = app.data.groupFriends
						.get(mNowChatGroup.members.get(i));
				View userView = inflater.inflate(
						R.layout.fragment_circles_gridpage_item, null);
				final ImageView iv_head = (ImageView) userView
						.findViewById(R.id.iv_head);
				TextView tv_nickname = (TextView) userView
						.findViewById(R.id.tv_nickname);
				tv_nickname.setText(friend.nickName);
				final String headFileName = friend.head;
				app.fileHandler.getHeadImage(headFileName, friend.sex,
						new FileResult() {
							@Override
							public void onResult(String where, Bitmap bitmap) {
								iv_head.setImageBitmap(app.fileHandler.bitmaps
										.get(headFileName));
							}
						});
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);

				params.setMargins(40, 0, 0, 0);

				if (i == mNowChatGroup.members.size() - 1) {
					params.setMargins(40, 0, 40, 0);
				}
				userView.setLayoutParams(params);

				userView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (app.data.friends.get(friend.phone) != null) {
							mMainModeManager.mBusinessCardFragment.mStatus = BusinessCardFragment.SHOW_FRIEND;
							mMainModeManager.mBusinessCardFragment.mShowFriend = friend;
							mMainModeManager
									.showNext(mMainModeManager.mBusinessCardFragment);
						} else if (friend.phone.equals(app.data.user.phone)) {
							mMainModeManager.mBusinessCardFragment.mStatus = BusinessCardFragment.SHOW_SELF;
							mMainModeManager.mBusinessCardFragment.mShowFriend = friend;
							mMainModeManager
									.showNext(mMainModeManager.mBusinessCardFragment);
						} else {
							mMainModeManager.mBusinessCardFragment.mStatus = BusinessCardFragment.SHOW_TEMPFRIEND;
							mMainModeManager.mBusinessCardFragment.mShowFriend = friend;
							mMainModeManager
									.showNext(mMainModeManager.mBusinessCardFragment);
						}
					}
				});

				linearlayout.addView(userView);
			}

		}
		mMainModeManager.handleMenu(false);
		initEvent();
		initBaseFaces();
		return mContent;
	}

	void initEvent() {
		chat_vPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				chat_vPager_now = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		ll_facepanel.setOnClickListener(null);
		iv_face_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int start = editText_message.getSelectionStart();
				String content = editText_message.getText().toString();
				if (start - 1 < 0)
					return;
				String faceEnd = content.substring(start - 1, start);
				if ("]".equals(faceEnd) || ">".equals(faceEnd)) {
					String str = content.substring(0, start);
					int index = "]".equals(faceEnd) ? str.lastIndexOf("[")
							: str.lastIndexOf("<");
					if (index != -1) {
						String faceStr = content.substring(index, start);
						Pattern patten = Pattern.compile(faceRegx,
								Pattern.CASE_INSENSITIVE);
						Matcher matcher = patten.matcher(faceStr);
						if (matcher.find()) {
							editText_message.setSelection(start
									- faceStr.length());
						} else {
							if (start - 1 >= 0) {
								editText_message.setSelection(start - 1);
							}
						}
					}
				} else {
					if (start - 1 >= 0) {
						editText_message.setSelection(start - 1);
					}
				}
			}
		});
		iv_face_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int start = editText_message.getSelectionStart();
				String content = editText_message.getText().toString();
				if (start + 1 > content.length())
					return;
				String faceEnd = content.substring(start, start + 1);
				if ("[".equals(faceEnd) || "<".equals(faceEnd)) {
					String str = content.substring(start);
					int index = "[".equals(faceEnd) ? str.indexOf("]") : str
							.indexOf(">");
					if (index != -1) {
						String faceStr = content.substring(start, index + start
								+ 1);
						Pattern patten = Pattern.compile(faceRegx,
								Pattern.CASE_INSENSITIVE);
						Matcher matcher = patten.matcher(faceStr);
						if (matcher.find()) {
							editText_message.setSelection(start
									+ faceStr.length());
						} else {
							if (start + 1 <= content.length()) {
								editText_message.setSelection(start + 1);
							}
						}
					}
				} else {
					if (start + 1 <= content.length()) {
						editText_message.setSelection(start + 1);
					}
				}
			}
		});
		iv_face_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int start = editText_message.getSelectionStart();
				String content = editText_message.getText().toString();
				if (start - 1 < 0)
					return;
				String faceEnd = content.substring(start - 1, start);
				if ("]".equals(faceEnd) || ">".equals(faceEnd)) {
					String str = content.substring(0, start);
					int index = "]".equals(faceEnd) ? str.lastIndexOf("[")
							: str.lastIndexOf("<");
					if (index != -1) {
						String faceStr = content.substring(index, start);
						Pattern patten = Pattern.compile(faceRegx,
								Pattern.CASE_INSENSITIVE);
						Matcher matcher = patten.matcher(faceStr);
						if (matcher.find()) {
							editText_message.setText(content.substring(0, start
									- faceStr.length())
									+ content.substring(start));
							editText_message.setSelection(start
									- faceStr.length());
						} else {
							if (start - 1 >= 0) {
								editText_message.setText(content.substring(0,
										start - 1) + content.substring(start));
								editText_message.setSelection(start - 1);
							}
						}
					}
				} else {
					if (start - 1 >= 0) {
						editText_message.setText(content
								.substring(0, start - 1)
								+ content.substring(start));
						editText_message.setSelection(start - 1);
					}
				}
			}
		});
		rl_selectedface.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int show_status = rl_face.getVisibility();
				if (show_status == View.VISIBLE) {
					rl_face.setVisibility(View.GONE);
				} else {
					rl_face.setVisibility(View.VISIBLE);
					hideSelectTab();
				}
			}
		});
		groupTopBar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mMainModeManager.mBusinessCardFragment.mStatus = BusinessCardFragment.SHOW_FRIEND;
				mMainModeManager.mBusinessCardFragment.mShowFriend = mNowChatFriend;
				mMainModeManager
						.showNext(mMainModeManager.mBusinessCardFragment);
			}
		});
		final GestureDetector backViewDetector = new GestureDetector(
				getActivity(), new GestureDetector.SimpleOnGestureListener() {
					@Override
					public boolean onDown(MotionEvent e) {
						// TODO Auto-generated method stub
						return true;
					}

					@Override
					public boolean onSingleTapUp(MotionEvent e) {
						mMainModeManager.back();
						return true;
					}
				});
		groupTopBar_back.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					groupTopBar_back.setBackgroundColor(Color
							.argb(143, 0, 0, 0));
					break;
				case MotionEvent.ACTION_UP:
					groupTopBar_back.setBackgroundColor(Color.argb(0, 0, 0, 0));
					break;
				}
				backViewDetector.onTouchEvent(event);
				return true;
			}
		});

		groupCenterBar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				groupCenterBar.setVisibility(View.GONE);
			}
		});

		groupSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mMainModeManager.mGroupManagerFragment.status = GroupManagerFragment.MODE_MANAGER;
				mMainModeManager.mGroupManagerFragment.mCurrentManagerGroup = mNowChatGroup;
				mMainModeManager
						.showNext(mMainModeManager.mGroupManagerFragment);
			}
		});

		rl_selectpicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectPicture();
			}
		});

		rl_makeaudio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int show_status = rl_audiopanel.getVisibility();
				if (show_status == View.VISIBLE) {
					if (voice_list.size() != 0) {
						Alert.createDialog(getActivity())
								.setTitle("语音尚未发送，是否取消？")
								.setOnConfirmClickListener(
										new OnDialogClickListener() {
											@Override
											public void onClick(
													AlertInputDialog dialog) {
												tv_voice.setText("语音");
												rl_audiopanel
														.setVisibility(View.GONE);
												for (int i = 0; i < voice_list
														.size(); i++) {
													File file = new File(
															voice_list.get(i));
													file.delete();
												}
												voice_list.clear();
												voice_length = 0;
											}
										}).show();
					} else {
						tv_voice.setText("语音");
						rl_audiopanel.setVisibility(View.GONE);
					}
				} else {
					initVoice();
				}
			}
		});
		rl_audiopanel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// not to do
			}
		});
		iv_voice_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!VOICE_PLAYSTATUS) {
					iv_voice_play.setImageBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.voice_stop));
					VOICE_PLAYSTATUS = true;
					if (voice_list.size() != 0) {
						play_order = 0;
						play(play_order);
					} else {
						Toast.makeText(getActivity(), "voice not exist",
								Toast.LENGTH_SHORT).show();
						iv_voice_play.setImageBitmap(BitmapFactory
								.decodeResource(getResources(),
										R.drawable.voice_start));
						VOICE_PLAYSTATUS = false;
					}
				} else {
					iv_voice_play.setImageBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.voice_start));
					VOICE_PLAYSTATUS = false;
					mPlayer.stop();
				}
			}
		});
		mOnTouchListener = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();

				switch (action) {
				case MotionEvent.ACTION_DOWN:
					startTime = System.currentTimeMillis();
					start();
					tv_voice_start.setText("正在录音");
					break;
				case MotionEvent.ACTION_UP:
					long currentTime = System.currentTimeMillis();
					if (currentTime - startTime > 1000) {
						finish();
						File file = new File(app.sdcardVoiceFolder,
								voice_list.get(0));
						if (file.exists()) {
							mPlayer = MediaPlayer
									.create(getActivity(),
											Uri.parse((new File(
													app.sdcardVoiceFolder,
													voice_list.get(voice_list
															.size() - 1)))
													.getAbsolutePath()));
							if (mPlayer != null) {
								voice_length += mPlayer.getDuration();
							}
							tv_voice_timelength.setText((int) Math
									.ceil(voice_length / 1000) + "\"");
						}
					} else {
						File file = new File(app.sdcardVoiceFolder,
								voice_list.remove(voice_list.size() - 1));
						if (file.exists()) {
							file.delete();
						}
						Toast.makeText(getActivity(), "录音时间太短",
								Toast.LENGTH_SHORT).show();
						recorder.reset();
						recorder = null;
					}
					tv_voice_start.setText("继续录音");
					break;
				case MotionEvent.ACTION_CANCEL:
					Toast.makeText(getActivity(), "ACTION_CANCEL",
							Toast.LENGTH_SHORT).show();
					break;
				}
				return true;
			}
		};
		tv_voice_start.setOnTouchListener(mOnTouchListener);
		iv_voice_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (voice_length == 0) {
					Toast.makeText(getActivity(), "尚未录制语音", Toast.LENGTH_SHORT)
							.show();
				} else {
					// mergeAACAudioFiles();
					getRecordVoice();
					app.UIHandler.post(new Runnable() {
						public void run() {
							initVoice();
						}
					});
				}

			}
		});
		iv_more_select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSelectTab();
			}
		});

		final GestureDetector gestureDetector = new GestureDetector(
				getActivity(), new OnGestureListener() {

					@Override
					public boolean onSingleTapUp(MotionEvent e) {
						return false;
					}

					@Override
					public void onShowPress(MotionEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean onScroll(MotionEvent e1, MotionEvent e2,
							float distanceX, float distanceY) {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public void onLongPress(MotionEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						boolean flag = false;
						if (e2.getX() - e1.getX() > 0 && velocityX > 2000) {
							showSelectTab();
							flag = true;
						}
						return flag;
					}

					@Override
					public boolean onDown(MotionEvent e) {
						return false;
					}
				});

		editText_message.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});

		editText_message.setVisibility(View.GONE);
		editText_message.setVisibility(View.VISIBLE);
		// editText_message.requestFocus();

		iv_more.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});

		iv_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showSelectTab();
			}
		});
		editText_message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO
				rl_face.setVisibility(View.GONE);
				new Thread() {
					public void run() {
						try {
							sleep(50);
							app.UIHandler.post(new Runnable() {
								@Override
								public void run() {
									chatContent.setSelection(chatContent
											.getBottom());
								}
							});
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					};
				}.start();

			}
		});
		editText_message.addTextChangedListener(new TextWatcher() {
			String content = "";

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (beforeHeight == 0) {
					beforeHeight = editText_message.getHeight();
				}
				if (beforeLineHeight == 0) {
					beforeLineHeight = editText_message.getLineHeight();
				}

				if (beforeHeight == 0 || beforeLineHeight == 0) {
					return;
				}

				LayoutParams etparams = editText_message.getLayoutParams();
				LayoutParams rlparams = rl_chatbottom.getLayoutParams();

				int lineCount = editText_message.getLineCount();

				switch (lineCount) {
				case 4:
					etparams.height = beforeHeight + beforeLineHeight;
					rlparams.height = beforeHeight + beforeLineHeight;
					break;
				case 5:
					etparams.height = beforeHeight + beforeLineHeight * 2;
					rlparams.height = beforeHeight + beforeLineHeight * 2;
					break;

				default:
					if (lineCount <= 3) {
						etparams.height = beforeHeight;
						rlparams.height = beforeHeight;
					}
					break;
				}
				if (lineCount > 5) {
					etparams.height = beforeHeight + beforeLineHeight * 2;
					rlparams.height = beforeHeight + beforeLineHeight * 2;
				}
				editText_message.setLayoutParams(etparams);
				rl_chatbottom.setLayoutParams(rlparams);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				content = s.toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				int selectionIndex = editText_message.getSelectionStart();
				if (!(s.toString()).equals(content)) {
					SpannableString spannableString = ExpressionUtil
							.getExpressionString(getActivity(), s.toString(),
									faceRegx, expressionFaceMap);
					editText_message.setText(spannableString);
					editText_message.setSelection(selectionIndex);
				}
			}
		});

		iv_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String message = editText_message.getText().toString();
				ArrayList<String> messages = new ArrayList<String>();
				messages.add(message);
				editText_message.setText("");
				if (message != null && !message.equals("")) {
					sendMessage("text", messages);
					rl_face.setVisibility(View.GONE);
				}
			}
		});
	}

	void mergeAACAudioFiles() {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/wxgs/";
		File mergeACC = new File(path + new Date().getTime() + ".aac");
		FileOutputStream mergerAACFos = null;
		if (!mergeACC.exists()) {
			try {
				mergeACC.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			mergerAACFos = new FileOutputStream(mergeACC);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < voice_list.size(); i++) {
			File file = new File(path + voice_list.get(i));
			if (file.exists()) {
				try {
					FileInputStream fis = new FileInputStream(file);
					byte[] myByte = new byte[fis.available()];
					int length = myByte.length;

					if (i == 0) {
						while (fis.read(myByte) != -1) {
							mergerAACFos.write(myByte, 0, length);
						}
					} else {
						while (fis.read(myByte) != -1) {
							mergerAACFos.write(myByte, 6, length - 6);
						}
					}
					mergerAACFos.flush();
					fis.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				continue;
			}
		}
		try {
			mergerAACFos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		deleteRecordFile(path);
	}

	void deleteRecordFile(String path) {
		for (int i = 0; i < voice_list.size(); i++) {
			File file = new File(path + voice_list.get(i));
			if (file.exists()) {
				file.delete();
			}
		}
		voice_list.clear();
		voice_length = 0;
		tv_voice_timelength.setText("0\"");
		tv_voice_start.setText("按住录音");
	}

	void initVoice() {
		tv_voice_timelength.setText("0\"");
		tv_voice.setText("取消");
		tv_voice_start.setText("按住录音");
		rl_audiopanel.setVisibility(View.VISIBLE);
		int show_status = rl_face.getVisibility();
		if (show_status == View.VISIBLE) {
			rl_face.setVisibility(View.GONE);
		}
	}

	void play(int i) {
		play_order = i;
		playAudio(play_order).setOnCompletionListener(
				new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						mp.reset();
						play_order++;
						if (play_order < voice_list.size()) {
							playAudio(play_order);
						} else {
							iv_voice_play.setImageBitmap(BitmapFactory
									.decodeResource(getResources(),
											R.drawable.voice_start));
							VOICE_PLAYSTATUS = false;
							mp.stop();
							mp.release();
							mp = null;
						}
					}
				});
	}

	MediaPlayer playAudio(int i) {
		mPlayer = MediaPlayer.create(getActivity(), Uri.parse((new File(
				app.sdcardVoiceFolder, voice_list.get(i))).getAbsolutePath()));
		mPlayer.start();
		return mPlayer;
	}

	@SuppressLint("InlinedApi")
	void start() {
		String fileName = new Date().getTime() + ".aac";
		// AudioRecord audioRecord = new AudioRecord(audioSource,
		// sampleRateInHz, channelConfig, audioFormat,
		// bufferSizeInBytes)
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);//
		// recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		// recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		// recorder.setAudioSamplingRate(3000);
		// recorder.setAudioEncodingBitRate(10000);
		recorder.setOutputFile((new File(app.sdcardVoiceFolder, fileName))
				.getAbsolutePath());
		try {
			recorder.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}

		recorder.start();
		voice_list.add(fileName);
		// };
		// }.start();
	}

	void finish() {
		if (recorder != null) {
			recorder.stop();
			recorder.reset();
			recorder.release();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (mStatus == CHAT_FRIEND) {
			mAdapter = new ChatAdapter();
		} else if (mStatus == CHAT_GROUP) {
			mAdapter = new GroupChatAdapter();
		}
		chatContent.setAdapter(mAdapter);

		chatContent.setSelection(mAdapter.getCount() - 1);

		chatContent.setOnScrollListener(new OnScrollListener() {
			boolean isFirst = true;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem == 0 && showFirstPosition != 0 && !isFirst) {
					int old = showFirstPosition;
					showFirstPosition = showFirstPosition > 10 ? showFirstPosition - 10
							: 0;
					mAdapter.notifyDataSetChanged();
					chatContent.setSelection(old - showFirstPosition);
				}
				isFirst = false;
			}
		});
	}

	public void showSelectTab() {
		hideSoftInput();
		Animation outAnimation = new TranslateAnimation(0,
				rl_chatbottom.getWidth(), 0, 0);
		outAnimation.setDuration(150);
		outAnimation.setAnimationListener(new AnimationAdapter() {
			@Override
			public void onAnimationEnd(Animation animation) {
				rl_message.setVisibility(View.GONE);
				rl_message.clearAnimation();
			}
		});
		rl_message.startAnimation(outAnimation);

		Animation inAnimation = new TranslateAnimation(
				-rl_chatbottom.getWidth(), 0, 0, 0);
		inAnimation.setDuration(150);
		rl_select.setVisibility(View.VISIBLE);
		rl_select.startAnimation(inAnimation);
	}

	public void hideSelectTab() {
		Animation outAnimation = new TranslateAnimation(0,
				-rl_chatbottom.getWidth(), 0, 0);
		outAnimation.setDuration(150);
		outAnimation.setAnimationListener(new AnimationAdapter() {
			@Override
			public void onAnimationEnd(Animation animation) {
				rl_select.setVisibility(View.GONE);
				rl_message.clearAnimation();
			}
		});
		rl_select.startAnimation(outAnimation);

		Animation inAnimation = new TranslateAnimation(
				rl_chatbottom.getWidth(), 0, 0, 0);
		inAnimation.setDuration(150);
		rl_message.setVisibility(View.VISIBLE);
		editText_message.requestFocus();
		rl_message.startAnimation(inAnimation);
		if (rl_audiopanel.getVisibility() == View.VISIBLE) {
			rl_audiopanel.setVisibility(View.GONE);
			tv_voice.setText("语音");
		}
	}

	public class GroupChatAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mNowChatGroup.messages.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			return convertView;
		}

	}

	public class ChatAdapter extends BaseAdapter {

		@Override
		public void notifyDataSetChanged() {
			if (mNowChatFriend.notReadMessagesCount != 0) {
				app.dataHandler.exclude(new Modification() {

					@Override
					public void modifyData(Data data) {
						data.friends.get(mNowChatFriend.phone).notReadMessagesCount = 0;
					}

					@Override
					public void modifyUI() {
						mMainModeManager.mCirclesFragment.notifyViews();
						// mMainModeManager.handleMenu(false);
					}
				});
			}
			super.notifyDataSetChanged();
			chatContent.setSelection(mAdapter.getCount() - 1);
		}

		@Override
		public int getCount() {
			return mNowChatFriend.messages.size() - showFirstPosition;
		}

		@Override
		public Object getItem(int position) {
			return mNowChatFriend.messages.get(showFirstPosition + position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			return ((Message) getItem(position)).type;
		}

		@Override
		public int getViewTypeCount() {
			return MAXTYPE_COUNT;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final MessageHolder messageHolder;
			final int type = getItemViewType(position);
			if (convertView == null) {
				messageHolder = new MessageHolder();
				switch (type) {
				case Message.MESSAGE_TYPE_SEND:
					convertView = mInflater.inflate(R.layout.f_chat_item_right,
							null);
					messageHolder.text = convertView
							.findViewById(R.id.rl_chatright);
					break;
				case Message.MESSAGE_TYPE_RECEIVE:
					convertView = mInflater.inflate(R.layout.f_chat_item_left,
							null);
					messageHolder.text = convertView
							.findViewById(R.id.rl_chatleft);
					break;
				default:
					break;
				}
				messageHolder.tv_time = (TextView) convertView
						.findViewById(R.id.tv_time);
				messageHolder.image = (RelativeLayout) convertView
						.findViewById(R.id.rl_chatleft_image);
				messageHolder.ll_image = (LinearLayout) convertView
						.findViewById(R.id.ll_image);
				messageHolder.rl_image = (RelativeLayout) convertView
						.findViewById(R.id.rl_image);
				messageHolder.iv_image_gif = (RelativeLayout) convertView
						.findViewById(R.id.iv_image_gif);
				messageHolder.tv_nickname = (TextView) convertView
						.findViewById(R.id.tv_nickname);
				messageHolder.iv_head = (ImageView) convertView
						.findViewById(R.id.iv_head);
				messageHolder.iv_sendhead = (ImageView) convertView
						.findViewById(R.id.iv_sendhead);
				messageHolder.tv_chat = (TextView) convertView
						.findViewById(R.id.tv_chat);
				messageHolder.voice = convertView
						.findViewById(R.id.rl_chatleft_voice);
				messageHolder.iv_voicehead_status = (ImageView) convertView
						.findViewById(R.id.iv_voicehead_status);
				messageHolder.iv_voicehead = (ImageView) convertView
						.findViewById(R.id.iv_voicehead);
				messageHolder.tv_voicetime = (TextView) convertView
						.findViewById(R.id.tv_voicetime);
				messageHolder.sk_voice = (SeekBar) convertView
						.findViewById(R.id.sk_voice);

				convertView.setTag(messageHolder);
			} else {
				messageHolder = (MessageHolder) convertView.getTag();
			}
			// final Message message = (Message) getItem(position);
			final Message message = mNowChatFriend.messages
					.get(showFirstPosition + position);
			messageHolder.tv_time.setText(TimeUtils.getTime(Long
					.valueOf(message.time)));
			if (message.contentType.equals("text")) {
				messageHolder.text.setVisibility(View.VISIBLE);
				messageHolder.image.setVisibility(View.GONE);
				messageHolder.voice.setVisibility(View.GONE);
				messageHolder.rl_image.setVisibility(View.GONE);
				// messageHolder.tv_chat.setText(message.content);
				String content;
				try {
					content = message.content.get(0);
				} catch (Exception e) {
					content = message.content.toString();
				}
				SpannableString spannableString = ExpressionUtil
						.getExpressionString(getActivity(), content, faceRegx,
								expressionFaceMap);
				messageHolder.tv_chat.setText(spannableString);

				String fileName = app.data.user.head;
				switch (type) {
				case Message.MESSAGE_TYPE_SEND:
					fileName = app.data.user.head;
					break;
				case Message.MESSAGE_TYPE_RECEIVE:
					fileName = mNowChatFriend.head;
					break;
				default:
					break;
				}
				final String headFileName = fileName;
				final ImageView iv_head = messageHolder.iv_head;
				app.fileHandler.getHeadImage(headFileName, app.data.user.sex,
						new FileResult() {
							@Override
							public void onResult(String where, Bitmap bitmap) {
								iv_head.setImageBitmap(app.fileHandler.bitmaps
										.get(headFileName));
							}
						});
				iv_head.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						switch (type) {
						case Message.MESSAGE_TYPE_SEND:
							mMainModeManager.mBusinessCardFragment.mStatus = BusinessCardFragment.SHOW_SELF;
							mMainModeManager
									.showNext(mMainModeManager.mBusinessCardFragment);
							break;
						case Message.MESSAGE_TYPE_RECEIVE:
							mMainModeManager.mBusinessCardFragment.mStatus = BusinessCardFragment.SHOW_FRIEND;
							mMainModeManager.mBusinessCardFragment.mShowFriend = mNowChatFriend;
							mMainModeManager
									.showNext(mMainModeManager.mBusinessCardFragment);
							break;

						default:
							break;
						}
					}
				});
				messageHolder.text
						.setOnLongClickListener(new OnLongClickListener() {

							@SuppressWarnings("deprecation")
							@TargetApi(Build.VERSION_CODES.HONEYCOMB)
							@Override
							public boolean onLongClick(View v) {
								@SuppressWarnings("static-access")
								ClipboardManager clipboard = (ClipboardManager) getActivity()
										.getSystemService(
												getActivity().CLIPBOARD_SERVICE);
								clipboard.setText(message.content.get(0));
								// ClipboardManager clip = (ClipboardManager)
								// getActivity()
								// .getSystemService(
								// Context.CLIPBOARD_SERVICE);
								// // clip.setPrimaryClip()
								// clip.setText(message.content);
								Toast.makeText(getActivity(), "复制成功!",
										Toast.LENGTH_SHORT).show();
								return true;
							}
						});
			} else if (message.contentType.equals("image")) {
				messageHolder.text.setVisibility(View.GONE);
				messageHolder.voice.setVisibility(View.GONE);
				final String imageFileName = message.content.get(0);
				String content = message.content.get(0);
				final String imgLastName = content.substring(content
						.lastIndexOf(".") + 1);
				if ("gif".equals(imgLastName)) {
					messageHolder.image.setVisibility(View.VISIBLE);
					messageHolder.rl_image.setVisibility(View.GONE);
					messageHolder.iv_image_gif.setVisibility(View.VISIBLE);
					messageHolder.iv_image_gif.removeAllViews();
					app.fileHandler.getFile(new FileInterface() {

						@Override
						public void setParams(FileSettings settings) {
							settings.fileName = imageFileName;
							settings.folder = app.sdcardImageFolder;
						}

						@Override
						public void onSuccess(Boolean flag, String fileName) {

							app.UIHandler.post(new Runnable() {

								@Override
								public void run() {
									SampleView sampleView = new SampleView(
											getActivity(),
											app.fileHandler.getFileBytes(
													app.sdcardImageFolder,
													imageFileName));
									messageHolder.iv_image_gif
											.addView(sampleView);
									// if (where == app.fileHandler.FROM_WEB) {
									// mAdapter.notifyDataSetChanged();
									// }
								}
							});

						}
					});

				} else {
					messageHolder.image.setVisibility(View.GONE);
					messageHolder.rl_image.setVisibility(View.VISIBLE);
					messageHolder.ll_image.removeAllViews();
					final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							(int) (width * 0.493055556f),
							(int) (height * 0.1640625f));
					params.topMargin = (int) ((height * 0.01171875f) / 2);
					params.leftMargin = (int) (width * 0.01388889f);
					params.rightMargin = (int) (width * 0.01388889f);
					params.bottomMargin = (int) ((height * 0.01171875f) / 2);
					for (int i = 0; i < message.content.size(); i++) {
						String mImageFileName;
						final int index = i;
						try {
							mImageFileName = message.content.get(i);
						} catch (Exception e) {
							mImageFileName = message.content.toString();
						}
						final String imageFilename = mImageFileName;
						View addView = mInflater.inflate(
								R.layout.view_child, null);
						final ImageView iv_image = (ImageView) addView
								.findViewById(R.id.iv_child);
						addView.setLayoutParams(params);
						messageHolder.ll_image.addView(addView);
						iv_image.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								Intent intent = new Intent(getActivity(),
										PicAndVoiceDetailActivity.class);
								intent.putExtra("Activity", "Browse");
								intent.putExtra("currentIndex", index);
								intent.putStringArrayListExtra("content",
										message.content);
								startActivity(intent);
							}
						});
						app.fileHandler.getImage(mImageFileName,
								new FileResult() {
									@Override
									public void onResult(String where,
											Bitmap bitmap) {
										SoftReference<Bitmap> bm = new SoftReference<Bitmap>(
												bitmap);
										iv_image.setImageBitmap(bm.get());
									}
								});
					}

				}
				final ImageView iv_sendhead = messageHolder.iv_sendhead;
				switch (type) {
				case Message.MESSAGE_TYPE_SEND:
					iv_sendhead.setVisibility(View.GONE);
					break;
				case Message.MESSAGE_TYPE_RECEIVE:
					messageHolder.tv_nickname.setText(mNowChatFriend.nickName);
					final String headFileName = mNowChatFriend.head;
					iv_sendhead.setVisibility(View.VISIBLE);
					app.fileHandler.getHeadImage(headFileName,
							mNowChatFriend.sex, new FileResult() {
								@Override
								public void onResult(String where, Bitmap bitmap) {
									iv_sendhead
											.setImageBitmap(app.fileHandler.bitmaps
													.get(headFileName));
									iv_sendhead
											.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View v) {
													mMainModeManager.mBusinessCardFragment.mStatus = BusinessCardFragment.SHOW_FRIEND;
													mMainModeManager.mBusinessCardFragment.mShowFriend = mNowChatFriend;
													mMainModeManager
															.showNext(mMainModeManager.mBusinessCardFragment);
												}
											});
								}
							});

					break;
				default:
					break;
				}
			} else if (message.contentType.equals("voice")) {
				messageHolder.text.setVisibility(View.GONE);
				messageHolder.image.setVisibility(View.GONE);
				messageHolder.rl_image.setVisibility(View.GONE);
				messageHolder.voice.setVisibility(View.VISIBLE);
				String fileName = app.data.user.head;
				String sex = "男";
				switch (type) {
				case Message.MESSAGE_TYPE_SEND:
					fileName = app.data.user.head;
					sex = app.data.user.sex;
					break;
				case Message.MESSAGE_TYPE_RECEIVE:
					fileName = mNowChatFriend.head;
					sex = mNowChatFriend.sex;
					break;
				default:
					break;
				}
				String mVoiceContent;
				try {
					mVoiceContent = message.content.get(0);
				} catch (Exception e) {
					mVoiceContent = message.content.toString();
				}
				final String voiceContent = mVoiceContent;
				final String headFileName = fileName;
				final ImageView iv_head = messageHolder.iv_voicehead;
				final ImageView iv_voicehead_status = messageHolder.iv_voicehead_status;
				app.fileHandler.getHeadImage(headFileName, sex,
						new FileResult() {
							@Override
							public void onResult(String where, Bitmap bitmap) {
								iv_head.setImageBitmap(app.fileHandler.bitmaps
										.get(headFileName));
								// iv_head.setBackgroundDrawable(new
								// BitmapDrawable(
								// app.fileHandler.bitmaps.get(headFileName)));
								Bitmap newBitmap = BitmapFactory
										.decodeResource(getResources(),
												R.drawable.head_voice_start);

								iv_voicehead_status.setImageBitmap(newBitmap);
								iv_voicehead_status.setTag("start");
							}
						});
				app.fileHandler.getFile(new FileInterface() {

					@Override
					public void setParams(FileSettings settings) {
						settings.fileName = voiceContent;
						settings.folder = app.sdcardVoiceFolder;
					}

					@Override
					public void onSuccess(Boolean flag, String fileName) {
						VOICE_SAVESTATUS = flag;
						if (flag) {
							try {
								final MediaPlayer mpPlayer = MediaPlayer
										.create(getActivity(), Uri
												.parse((new File(
														app.sdcardVoiceFolder,
														voiceContent))
														.getAbsolutePath()));
								messageHolder.mpPlayer = mpPlayer;
								app.UIHandler.post(new Runnable() {

									@Override
									public void run() {
										messageHolder.tv_voicetime.setText((int) Math
												.ceil((double) (mpPlayer
														.getDuration()) / 1000)
												+ "\"");
									}
								});
								messageHolder.sk_voice.setMax(mpPlayer
										.getDuration());
								// messageHolder.sk_voice.setMax((int) Math
								// .ceil((double) (mpPlayer.getDuration()) /
								// 1000));
							} catch (SecurityException e) {
								e.printStackTrace();
							} catch (IllegalStateException e) {
								e.printStackTrace();
							}
						} else {
							// to do loading voice failed
						}

					}
				});

				messageHolder.sk_voice.setProgress(0);
				messageHolder.sk_voice
						.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

							@Override
							public void onStopTrackingTouch(SeekBar seekBar) {
								messageHolder.sk_voice.setProgress(seekBar
										.getProgress());
								messageHolder.mpPlayer.seekTo(seekBar
										.getProgress());
								if (!messageHolder.mpPlayer.isPlaying()) {
									messageHolder.mpPlayer.start();
									iv_head.performClick();
								}
							}

							@Override
							public void onStartTrackingTouch(SeekBar seekBar) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onProgressChanged(SeekBar seekBar,
									int arg1, boolean arg2) {
								// TODO Auto-generated method stub

							}
						});
				iv_head.setOnClickListener(new OnClickListener() {
					Thread thread = null;

					@Override
					public void onClick(View v) {
						Bitmap bitmap = null;

						final MediaPlayer mpPlayer = messageHolder.mpPlayer;
						if (mpPlayer == null)
							return;
						if (iv_voicehead_status.getTag() == "start") {
							bitmap = BitmapFactory.decodeResource(
									getResources(), R.drawable.head_voice_stop);
							iv_voicehead_status.setTag("stop");
							int playTime = messageHolder.sk_voice.getProgress();
							if (mpPlayer.getDuration() - playTime > 10) {
								mpPlayer.seekTo(playTime);
							} else {
								mpPlayer.seekTo(0);
							}

							mpPlayer.start();
							thread = new Thread() {
								public void run() {
									while (true) {
										try {
											if (getActivity() == null) {
												mpPlayer.stop();
												mpPlayer.release();
												thread.interrupt();
												break;
											}
											Thread.sleep(50);
											messageHolder.sk_voice.setProgress(mpPlayer
													.getCurrentPosition());
											if (mpPlayer.getDuration()
													- mpPlayer
															.getCurrentPosition() < 50) {
												messageHolder.sk_voice
														.setProgress(messageHolder.sk_voice
																.getMax());
												Thread.currentThread()
														.interrupt();
												break;
											}
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								};
							};
							thread.start();

							mpPlayer.setOnCompletionListener(new OnCompletionListener() {

								@Override
								public void onCompletion(MediaPlayer arg0) {
									if (getActivity() == null) {
										thread.interrupt();
										return;
									}
									Bitmap bitmap = BitmapFactory
											.decodeResource(getResources(),
													R.drawable.head_voice_start);
									iv_voicehead_status.setImageBitmap(bitmap);
									iv_voicehead_status.setTag("start");
								}
							});
						} else {
							bitmap = BitmapFactory
									.decodeResource(getResources(),
											R.drawable.head_voice_start);
							iv_voicehead_status.setTag("start");
							mpPlayer.pause();
							thread.interrupt();
						}
						iv_voicehead_status.setImageBitmap(bitmap);
					}
				});
			}
			return convertView;
		}
	}

	class MessageHolder {
		View text;
		ImageView iv_head;
		TextView tv_chat;
		TextView tv_time;
		ImageView iv_sendhead;

		RelativeLayout rl_image;
		RelativeLayout image;
		LinearLayout ll_image;
		TextView tv_nickname;
		RelativeLayout iv_image_gif;

		View voice;
		ImageView iv_voicehead;
		ImageView iv_voicehead_status;
		TextView tv_voicetime;
		SeekBar sk_voice;
		MediaPlayer mpPlayer;
	}

	public void sendMessage(final String type, final ArrayList<String> content) {
		final Message message = new Message();
		message.type = Message.MESSAGE_TYPE_SEND;
		if (mStatus == CHAT_FRIEND) {
			message.sendType = "point";
			message.phone = mNowChatFriend.phone;
		} else if (mStatus == CHAT_GROUP) {
			message.sendType = "group";
		}
		message.content = content;
		message.contentType = type;
		message.status = "sending";
		message.time = String.valueOf(new Date().getTime());

		app.dataHandler.exclude(new Modification() {
			@Override
			public void modifyData(Data data) {
				if (mStatus == CHAT_FRIEND) {
					data.friends.get(mNowChatFriend.phone).messages
							.add(message);
					data.lastChatFriends.remove("f" + mNowChatFriend.phone);
					data.lastChatFriends.add(0, "f" + mNowChatFriend.phone);
					// Log.e("Coolspan", data.lastChatFriends.size()
					// + "---------------chat length");
				} else {
					mNowChatGroup.messages.add(message);
				}
			}

			@Override
			public void modifyUI() {
				mMainModeManager.mChatMessagesFragment.notifyViews();
				mAdapter.notifyDataSetChanged();
				chatContent.setSelection(mAdapter.getCount() - 1);
				if (mMainModeManager.mCirclesFragment.isAdded()) {
					mMainModeManager.mCirclesFragment.notifyViews();
				}
				mMainModeManager.handleMenu(false);
			}
		});

		app.networkHandler.connection(new CommonNetConnection() {

			@Override
			protected void settings(Settings settings) {
				settings.url = API.DOMAIN + API.MESSAGE_SEND;
				settings.params = generateMessageParams(type, content);
			}

			@Override
			public void success(JSONObject jData) {
				try {
					String time = jData.getString("time");
					message.time = time;
					message.status = "sent";
				} catch (JSONException e) {
					message.status = "failed";
				}
				if (mStatus == CHAT_FRIEND) {
					if (app.data.lastChatFriends.indexOf(mNowChatFriend.phone) != 0) {
						app.data.lastChatFriends.remove("f"
								+ mNowChatFriend.phone);
						app.data.lastChatFriends.add(0, "f"
								+ mNowChatFriend.phone);
					}
				}
			}

			@Override
			protected void unSuccess(JSONObject jData) {
				message.status = "failed";
				super.unSuccess(jData);
			}
		});

	}

	public Map<String, String> generateMessageParams(String type,
			List<String> content) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("phone", app.data.user.phone);
		params.put("accessKey", app.data.user.accessKey);
		JSONArray jFriends = new JSONArray();
		if (mStatus == CHAT_FRIEND) {
			params.put("sendType", "point");
			jFriends.put(mNowChatFriend.phone);
			params.put("phoneto", jFriends.toString());
		} else if (mStatus == CHAT_GROUP) {
			params.put("sendType", "group");
			params.put("gid", String.valueOf(mNowChatGroup.gid));
			for (String phone : mNowChatGroup.members) {
				jFriends.put(phone);
			}
			params.put("phoneto", jFriends.toString());
		}
		JSONObject jMessage = new JSONObject();
		JSONArray jContent = new JSONArray();
		try {
			jMessage.put("contentType", type);
			for (String fileName : content) {
				jContent.put(fileName);
			}
			jMessage.put("content", jContent);
			params.put("message", jMessage.toString());
		} catch (JSONException e) {
		}

		return params;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_SELECTPICTURE
				&& resultCode == Activity.RESULT_OK) {
			final ArrayList<String> messages = new ArrayList<String>();
			isSELECTPICTURE = true;
			messageNum = 1;
			for (int i = 0; i < MapStorageDirectoryActivity.selectedImages
					.size(); i++) {
				final String filePath = MapStorageDirectoryActivity.selectedImages
						.get(i);
				app.fileHandler
						.getFileMessageInfo(new FileMessageInfoInterface() {

							@Override
							public void setParams(
									FileMessageInfoSettings settings) {
								settings.FILE_TYPE = OSSFileHandler.FILE_TYPE_SDSELECTIMAGE;
								settings.path = filePath;
								settings.fileName = filePath.substring(filePath
										.lastIndexOf("/"));
							}

							@Override
							public void onSuccess(
									ImageMessageInfo imageMessageInfo) {
								String contentType = (String) MapStorageDirectoryActivity.selectedImagesMap
										.get(filePath).get("contentType");
								// checkImage(fileName, base64, messages);
								checkImage(imageMessageInfo, contentType,
										filePath, "image", messages);
							}
						});
			}

		} else if (requestCode == RESULT_TAKEPICTURE
				&& resultCode == Activity.RESULT_OK) {

		} else if (requestCode == RESULT_CATPICTURE
				&& resultCode == Activity.RESULT_OK && data != null) {

		}
	}

	void selectPicture() {
		MapStorageDirectoryActivity.selectedImages.clear();
		Intent selectFromGallery = new Intent(getActivity(),
				MapStorageDirectoryActivity.class);
		startActivityForResult(selectFromGallery, RESULT_SELECTPICTURE);
	}

	File tempFile;

	void takePicture() {
		tempFile = new File(app.sdcardImageFolder, "tempimage");
		int i = 1;
		while (tempFile.exists()) {
			tempFile = new File(app.sdcardImageFolder, "tempimage" + (i++));
		}
		Uri uri = Uri.fromFile(tempFile);
		Intent tackPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		tackPicture.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
		tackPicture.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(tackPicture, RESULT_TAKEPICTURE);
	}

	public void checkImage(final ImageMessageInfo imageMessageInfo,
			final String contentType, final String path, final String fileType,
			final ArrayList<String> messages) {
		app.networkHandler.connection(new CommonNetConnection() {

			@Override
			protected void settings(Settings settings) {
				settings.url = API.DOMAIN + API.IMAGE_CHECK;
				Map<String, String> params = new HashMap<String, String>();
				params.put("phone", app.data.user.phone);
				params.put("accessKey", app.data.user.accessKey);
				params.put("filename", imageMessageInfo.fileName);
				settings.params = params;
			}

			@Override
			public void success(JSONObject jData) {
				try {
					if (jData.getBoolean("exists")) {
						if ("image".equals(fileType)) {
							messages.add(imageMessageInfo.fileName);
							if (isSELECTPICTURE) {
								if (messageNum == MapStorageDirectoryActivity.selectedImages
										.size()) {
									sendMessage(fileType, messages);
									isSELECTPICTURE = false;
								} else {
									messageNum++;
								}
							} else {
								sendMessage(fileType, messages);
							}
						} else {
							sendMessage(fileType, messages);
						}

					} else {
						app.fileHandler.uploadFile(new UploadFileInterface() {

							@Override
							public void setParams(UploadFileSettings settings) {
								settings.imageMessageInfo = imageMessageInfo;
								settings.contentType = contentType;
								settings.fileName = imageMessageInfo.fileName;
								settings.path = path;
								if ("image".equals(fileType)) {
									settings.uploadFileType = OSSFileHandler.UPLOAD_FILE_TYPE_IMAGES;
								} else if ("voice".equals(fileType)) {
									settings.uploadFileType = OSSFileHandler.UPLOAD_FILE_TYPE_VOICES;
								}
							}

							@Override
							public void onSuccess(Boolean flag, String fileName) {
								if ("image".equals(fileType)) {
									messages.add(fileName);
									if (isSELECTPICTURE) {
										if (messageNum == MapStorageDirectoryActivity.selectedImages
												.size()) {
											sendMessage(fileType, messages);
											isSELECTPICTURE = false;
										} else {
											messageNum++;
										}
									} else {
										sendMessage(fileType, messages);
									}
								} else {
									sendMessage(fileType, messages);
								}
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});
	}

	public void uploadImageOrVoice(final String type, final String fileName,
			final String base64, final ArrayList<String> messages) {
		app.networkHandler.connection(new CommonNetConnection() {

			@Override
			protected void settings(Settings settings) {
				settings.url = API.DOMAIN + API.IMAGE_UPLOAD;
				Map<String, String> params = new HashMap<String, String>();
				params.put("phone", app.data.user.phone);
				params.put("accessKey", app.data.user.accessKey);
				params.put("filename", fileName);
				params.put("imagedata", base64);
				settings.params = params;
			}

			@Override
			public void success(JSONObject jData) {
				messages.add(fileName);
				if (isSELECTPICTURE) {
					if (messageNum == MapStorageDirectoryActivity.selectedImages
							.size()) {
						sendMessage(type, messages);
						isSELECTPICTURE = false;
					} else {
						messageNum++;
					}
				} else {
					sendMessage(type, messages);

				}
			}

		});
	}

	public void getRecordVoice() {
		app.fileHandler.getFileMessageInfo(new FileMessageInfoInterface() {

			@Override
			public void setParams(FileMessageInfoSettings settings) {
				settings.fileName = voice_list.get(0);
				settings.folder = app.sdcardVoiceFolder;
				settings.FILE_TYPE = OSSFileHandler.FILE_TYPE_SDVOICE;
			}

			@Override
			public void onSuccess(final ImageMessageInfo imageMessageInfo) {

				app.fileHandler.uploadFile(new UploadFileInterface() {

					@Override
					public void setParams(UploadFileSettings settings) {
						settings.fileName = imageMessageInfo.fileName;
						settings.imageMessageInfo = imageMessageInfo;
						settings.contentType = "audio/x-mei-aac";
						settings.uploadFileType = OSSFileHandler.UPLOAD_FILE_TYPE_VOICES;
					}

					@Override
					public void onSuccess(Boolean flag, String fileName) {
						File fromFile = new File(app.sdcardVoiceFolder,
								voice_list.get(0));
						File toFile = new File(app.sdcardVoiceFolder,
								imageMessageInfo.fileName);
						fromFile.renameTo(toFile);
						voice_list.clear();
						voice_length = 0;
						ArrayList<String> messages = new ArrayList<String>();
						messages.add(fileName);
						sendMessage("voice", messages);
					}
				});

			}
		});
	}

	void initBaseFaces() {
		faceNamesList = mMainModeManager.faceNamesList;
		List<View> mListViews = new ArrayList<View>();
		List<String> images1 = new ArrayList<String>();
		for (int i = 0; i < 105; i++) {
			expressionFaceMap.put(faceNamesList.get(0)[i], "smiley_" + i
					+ ".png");
			images1.add("smiley_" + i + ".png");
		}
		List<String> images2 = new ArrayList<String>();
		for (int i = 0; i < 77; i++) {
			expressionFaceMap.put(faceNamesList.get(1)[i], "emoji_" + i
					+ ".png");
			images2.add("emoji_" + i + ".png");
		}
		List<String> images3 = new ArrayList<String>();
		for (int i = 1; i < 17; i++) {
			images3.add("tusiji_" + i + ".gif");
		}
		faceNameList = new ArrayList<List<String>>();
		faceNameList.add(images1);
		faceNameList.add(images2);
		faceNameList.add(images3);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100,
				LayoutParams.MATCH_PARENT);
		lp.gravity = Gravity.CENTER;
		for (int i = 0; i < 3; i++) {
			try {
				ImageView iv = new ImageView(getActivity());
				iv.setImageBitmap(BitmapFactory.decodeStream(getActivity()
						.getAssets().open(
								"images/" + faceNameList.get(i).get(0))));
				iv.setLayoutParams(lp);
				if (i == 0) {
					// iv.setBackgroundColor(Color.RED);
				}
				iv.setTag(i);
				ll_facemenu.addView(iv);
				faceMenuShowList.add(iv);
				ImageView iv_1 = new ImageView(getActivity());
				iv_1.setBackgroundColor(Color.WHITE);
				iv_1.setMinimumWidth(1);
				iv_1.setMinimumHeight(80);
				iv_1.setMaxWidth(1);
				ll_facemenu.addView(iv_1);
				iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int position = (Integer) v.getTag();
						// faceMenuShowList.get(chat_vPager_now)
						// .setBackgroundColor(Color.WHITE);
						chat_vPager_now = position;
						// faceMenuShowList.get(chat_vPager_now)
						// .setBackgroundColor(Color.RED);
						chat_vPager.setCurrentItem(chat_vPager_now);
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (i != 2) {
				View v = mInflater.inflate(R.layout.f_chat_face_base_gridview,
						null);
				GridView chat_base_gv = (GridView) v
						.findViewById(R.id.chat_base_gv);
				chat_base_gv.setAdapter(new myGridAdapter(faceNameList.get(i)));
				mListViews.add(chat_base_gv);
			} else {
				View v = mInflater.inflate(R.layout.f_chat_bigimg_gridview,
						null);
				GridView chat_base_gv = (GridView) v
						.findViewById(R.id.chat_base_gv);
				chat_base_gv.setAdapter(new myGridAdapter(faceNameList.get(i)));
				mListViews.add(chat_base_gv);
				chat_vPager.setAdapter(new myPageAdapter(mListViews));
			}
		}
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(100,
				LayoutParams.WRAP_CONTENT);
		lp1.gravity = Gravity.CENTER;
		ImageView iv = new ImageView(getActivity());
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.face_add);
		iv.setImageBitmap(bitmap);
		iv.setLayoutParams(lp1);
		ll_facemenu.addView(iv);
		ImageView iv_1 = new ImageView(getActivity());
		iv_1.setBackgroundColor(Color.WHITE);
		iv_1.setMinimumWidth(1);
		iv_1.setMinimumHeight(80);
		iv_1.setMaxWidth(1);
		ll_facemenu.addView(iv_1);
		faceMenuShowList.add(iv);
	}

	class myGridAdapter extends BaseAdapter {
		List<String> list;

		public myGridAdapter(List<String> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (getCount() < 30) {
				convertView = mInflater.inflate(
						R.layout.f_chat_bigimg_gridview_item, null);
			} else {
				convertView = mInflater.inflate(
						R.layout.f_chat_base_gridview_item, null);
			}
			ImageView iv = (ImageView) convertView
					.findViewById(R.id.chat_base_iv);
			try {
				iv.setImageBitmap(BitmapFactory.decodeStream(getActivity()
						.getAssets().open("images/" + list.get(position))));
			} catch (IOException e) {
				e.printStackTrace();
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (chat_vPager_now < 2) {
						editText_message.getText().insert(
								editText_message.getSelectionStart(),
								faceNamesList.get(chat_vPager_now)[position]);
					} else {
						rl_face.setVisibility(View.GONE);
						app.fileHandler
								.getFileMessageInfo(new FileMessageInfoInterface() {

									@Override
									public void setParams(
											FileMessageInfoSettings settings) {
										settings.fileName = "tusiji_"
												+ (position + 1) + ".gif";
										settings.FILE_TYPE = OSSFileHandler.FILE_TYPE_ASSETS;
										settings.assetsPath = "images/";
									}

									@Override
									public void onSuccess(
											final ImageMessageInfo imageMessageInfo) {
										app.networkHandler
												.connection(new CommonNetConnection() {

													@Override
													protected void settings(
															Settings settings) {
														settings.url = API.DOMAIN
																+ API.IMAGE_CHECK;
														Map<String, String> params = new HashMap<String, String>();
														params.put(
																"phone",
																app.data.user.phone);
														params.put(
																"accessKey",
																app.data.user.accessKey);
														params.put(
																"filename",
																imageMessageInfo.fileName);
														settings.params = params;
													}

													@Override
													public void success(
															JSONObject jData) {
														try {
															if (jData
																	.getBoolean("exists")) {
																ArrayList<String> messages = new ArrayList<String>();
																messages.add(imageMessageInfo.fileName);
																sendMessage(
																		"image",
																		messages);
															} else {
																app.fileHandler
																		.uploadFile(new UploadFileInterface() {

																			@Override
																			public void setParams(
																					UploadFileSettings settings) {
																				settings.fileName = imageMessageInfo.fileName;
																				settings.imageMessageInfo = imageMessageInfo;
																				settings.contentType = "image/gif";
																				settings.uploadFileType = OSSFileHandler.UPLOAD_FILE_TYPE_IMAGES;
																			}

																			@Override
																			public void onSuccess(
																					Boolean flag,
																					String fileName) {
																				ArrayList<String> messages = new ArrayList<String>();
																				messages.add(fileName);
																				sendMessage(
																						"image",
																						messages);
																			}
																		});
															}
														} catch (JSONException e) {
															e.printStackTrace();
														}

													}
												});

									}
								});
					}
				}
			});
			return convertView;
		}
	}

	class myPageAdapter extends PagerAdapter {
		List<View> mListViews;

		public myPageAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0 == arg1);
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			try {
				if (mListViews.get(arg1).getParent() == null)
					((ViewPager) arg0).addView(mListViews.get(arg1), 0);
				else {
					// 很难理解新添加进来的view会自动绑定一个父类，由于一个儿子view不能与两个父类相关，所以得解绑
					// 不这样做否则会产生 viewpager java.lang.IllegalStateException: The
					// specified child already has a parent. You must call
					// removeView() on the child's parent first.
					// 还有一种方法是viewPager.setOffscreenPageLimit(3); 这种方法不用判断parent
					// 是不是已经存在，但多余的listview不能被destroy
					((ViewGroup) mListViews.get(arg1).getParent())
							.removeView(mListViews.get(arg1));
					((ViewPager) arg0).addView(mListViews.get(arg1), 0);
				}
			} catch (Exception e) {
				// Log.d("parent=", "" + mListViews.get(arg1).getParent());
				e.printStackTrace();
			}
			return mListViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}
	}

}
