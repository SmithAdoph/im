package com.lejoying.wxgs.activity.mode.fragment;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lejoying.wxgs.R;
import com.lejoying.wxgs.activity.mode.MainModeManager;
import com.lejoying.wxgs.activity.utils.CommonNetConnection;
import com.lejoying.wxgs.activity.utils.MCImageUtils;
import com.lejoying.wxgs.activity.view.widget.CircleMenu;
import com.lejoying.wxgs.app.MainApplication;
import com.lejoying.wxgs.app.adapter.AnimationAdapter;
import com.lejoying.wxgs.app.data.API;
import com.lejoying.wxgs.app.data.Data;
import com.lejoying.wxgs.app.data.entity.Friend;
import com.lejoying.wxgs.app.data.entity.Group;
import com.lejoying.wxgs.app.data.entity.Message;
import com.lejoying.wxgs.app.handler.DataHandler.Modification;
import com.lejoying.wxgs.app.handler.FileHandler.FileResult;
import com.lejoying.wxgs.app.handler.FileHandler.SaveBitmapInterface;
import com.lejoying.wxgs.app.handler.FileHandler.SaveSettings;
import com.lejoying.wxgs.app.handler.NetworkHandler.Settings;

public class ChatFragment extends BaseFragment {

	MainApplication app = MainApplication.getMainApplication();
	MainModeManager mMainModeManager;

	public static final int CHAT_FRIEND = 1;
	public static final int CHAT_GROUP = 2;

	public int mStatus;

	public Friend mNowChatFriend;
	public Group mNowChatGroup;

	private View mContent;
	public ChatAdapter mAdapter;

	public MediaRecorder recorder;

	int RESULT_SELECTPICTURE = 0x124;
	int RESULT_TAKEPICTURE = 0xa3;
	int RESULT_CATPICTURE = 0x3d;

	boolean VOICE_PLAYSTATUS = false;

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

	View groupTopBar;
	TextView textView_groupName;
	TextView textView_memberCount;
	LinearLayout linearlayout_members;

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
		CircleMenu.showBack();
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		initShowFirstPosition();
		mInflater = inflater;
		mContent = inflater.inflate(R.layout.f_chat, null);

		chatContent = (ListView) mContent.findViewById(R.id.chatContent);

		if (mNowChatFriend.notReadMessagesCount != 0) {
			app.dataHandler.exclude(new Modification() {

				@Override
				public void modifyData(Data data) {
					data.friends.get(mNowChatFriend.phone).notReadMessagesCount = 0;
				}

				@Override
				public void modifyUI() {
					// TODO refresh
					// mMainModeManager.mCirclesFragment.mAdapter
					// .notifyDataSetChanged();
				}
			});
		}

		iv_send = mContent.findViewById(R.id.iv_send);
		iv_more = mContent.findViewById(R.id.iv_more);
		iv_more_select = mContent.findViewById(R.id.iv_more_select);
		editText_message = (EditText) mContent.findViewById(R.id.et_message);
		rl_chatbottom = (RelativeLayout) mContent.findViewById(R.id.chat_bottom_bar);
		rl_message = (RelativeLayout) mContent.findViewById(R.id.rl_message);
		rl_select = (RelativeLayout) mContent.findViewById(R.id.rl_select);
		rl_audiopanel = (RelativeLayout) mContent.findViewById(R.id.rl_audiopanel);
		rl_selectpicture = mContent.findViewById(R.id.rl_selectpicture);
		rl_makeaudio = mContent.findViewById(R.id.rl_makeaudio);
		tv_voice = (TextView) mContent.findViewById(R.id.tv_voice);
		tv_voice_start = (TextView) mContent.findViewById(R.id.tv_voice_start);
		iv_voice_send = (ImageView) mContent.findViewById(R.id.iv_voice_send);
		iv_voice_play = (ImageView) mContent.findViewById(R.id.iv_voice_play);
		tv_voice_timelength = (TextView) mContent
				.findViewById(R.id.tv_voice_timelength);

		groupTopBar = mContent.findViewById(R.id.relativeLayout_topbar);
		textView_groupName = (TextView) mContent.findViewById(R.id.textview_groupname);
		textView_memberCount = (TextView) mContent.findViewById(R.id.textview_membercount);

		linearlayout_members = (LinearLayout) mContent.findViewById(R.id.linearlayout_members);

		// if (mStatus == CHAT_FRIEND) {
		// groupTopBar.setVisibility(View.GONE);
		// } else if (mStatus == CHAT_GROUP) {
		// groupTopBar.setVisibility(View.VISIBLE);
		// }

		for (int i = 0; i < 4; i++) {
			ImageView iv_head = new ImageView(getActivity());
			iv_head.setImageBitmap(app.fileHandler.defaultHead);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40, 40);
			if (i != 3)
				params.setMargins(0, 0, 10, 0);
			iv_head.setLayoutParams(params);
			linearlayout_members.addView(iv_head);
		}

		groupCenterBar = mContent.findViewById(R.id.relativeLayout_group);
		textView_groupNameAndMemberCount = (TextView) mContent.findViewById(R.id.textView_groupNameAndMemberCount);
		linearlayout = (LinearLayout) groupCenterBar.findViewById(R.id.linearlayout_user);

		groupTopBar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				groupCenterBar.setVisibility(View.VISIBLE);
			}
		});

		groupCenterBar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				groupCenterBar.setVisibility(View.GONE);
			}
		});

		for (int i = 0; i < 14; i++) {
			View userView = inflater.inflate(R.layout.fragment_circles_gridpage_item, null);
			ImageView iv_head = (ImageView) userView.findViewById(R.id.iv_head);
			TextView tv_nickname = (TextView) userView.findViewById(R.id.tv_nickname);
			iv_head.setImageBitmap(app.fileHandler.defaultHead);
			tv_nickname.setText("测试" + i);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			params.setMargins(40, 0, 0, 0);

			if (i == 13) {
				params.setMargins(40, 0, 40, 0);
			}

			userView.setLayoutParams(params);
			linearlayout.addView(userView);
		}

		rl_selectpicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectPicture();
			}
		});
		rl_makeaudio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int show_status = rl_audiopanel.getVisibility();
				if (show_status == View.VISIBLE) {
					tv_voice.setText("语音");
					rl_audiopanel.setVisibility(View.GONE);
				} else {
					tv_voice.setText("取消");
					rl_audiopanel.setVisibility(View.VISIBLE);
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
				// TODO Auto-generated method stub
				if (!VOICE_PLAYSTATUS) {
					iv_voice_play.setImageBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.voice_stop));
					VOICE_PLAYSTATUS = true;
				} else {
					iv_voice_play.setImageBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.voice_start));
					VOICE_PLAYSTATUS = false;
				}
			}
		});
		try {
			tv_voice_start.setOnTouchListener(new OnTouchListener() {

				@SuppressLint("InlinedApi")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int action = event.getAction();

					switch (action) {
					case MotionEvent.ACTION_DOWN:
						start();
						tv_voice_start.setText("正在录音");
						Toast.makeText(getActivity(), "ACTION_DOWN",
								Toast.LENGTH_SHORT).show();
						break;
					case MotionEvent.ACTION_UP:
						finish();
						tv_voice_start.setText("继续录音");
						Toast.makeText(getActivity(), "ACTION_UP",
								Toast.LENGTH_SHORT).show();
						break;
					case MotionEvent.ACTION_CANCEL:// 当手指移动到view外面，会cancel
						Toast.makeText(getActivity(), "ACTION_CANCEL",
								Toast.LENGTH_SHORT).show();
						break;
					}

					return true;
				}
			});
		} catch (Exception e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		// tv_voice_start.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// Toast.makeText(getActivity(), "tv_voice_start",
		// Toast.LENGTH_SHORT).show();
		// }
		// });
		iv_voice_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "iv_voice_send",
						Toast.LENGTH_SHORT).show();
			}
		});
		iv_more_select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSelectTab();
			}
		});

		final GestureDetector gestureDetector = new GestureDetector(getActivity(), new OnGestureListener() {

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
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

		editText_message.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
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
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		iv_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String message = editText_message.getText().toString();
				editText_message.setText("");
				if (message != null && !message.equals("")) {
					sendMessage("text", message);
				}
			}
		});

		return mContent;
	}

	@SuppressLint("InlinedApi")
	public void start() {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		path += "/Coolspan.aac";
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC_ELD);
		recorder.setOutputFile(path);
		try {
			recorder.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}

		recorder.start();
	}

	public void finish() {
		recorder.stop();
		recorder.release();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (mAdapter == null) {
			mAdapter = new ChatAdapter();
		}
		chatContent.setAdapter(mAdapter);
		chatContent.setSelection(mAdapter.getCount() - 1);
		chatContent.setOnScrollListener(new OnScrollListener() {
			boolean isFirst = true;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem == 0 && showFirstPosition != 0 && !isFirst) {
					int old = showFirstPosition;
					showFirstPosition = showFirstPosition > 10 ? showFirstPosition - 10 : 0;
					mAdapter.notifyDataSetChanged();
					chatContent.setSelection(old - showFirstPosition);
				}
				isFirst = false;
			}
		});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	public void showSelectTab() {
		hideSoftInput();
		Animation outAnimation = new TranslateAnimation(0, rl_chatbottom.getWidth(), 0, 0);
		outAnimation.setDuration(150);
		outAnimation.setAnimationListener(new AnimationAdapter() {
			@Override
			public void onAnimationEnd(Animation animation) {
				rl_message.setVisibility(View.GONE);
				rl_message.clearAnimation();
			}
		});
		rl_message.startAnimation(outAnimation);

		Animation inAnimation = new TranslateAnimation(-rl_chatbottom.getWidth(), 0, 0, 0);
		inAnimation.setDuration(150);
		rl_select.setVisibility(View.VISIBLE);
		rl_select.startAnimation(inAnimation);
	}

	public void hideSelectTab() {
		Animation outAnimation = new TranslateAnimation(0, -rl_chatbottom.getWidth(), 0, 0);
		outAnimation.setDuration(150);
		outAnimation.setAnimationListener(new AnimationAdapter() {
			@Override
			public void onAnimationEnd(Animation animation) {
				rl_select.setVisibility(View.GONE);
				rl_message.clearAnimation();
			}
		});
		rl_select.startAnimation(outAnimation);

		Animation inAnimation = new TranslateAnimation(rl_chatbottom.getWidth(), 0, 0, 0);
		inAnimation.setDuration(150);
		rl_message.setVisibility(View.VISIBLE);
		editText_message.requestFocus();
		rl_message.startAnimation(inAnimation);
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
						// TODO refresh
						// mMainModeManager.mCirclesFragment.mAdapter.notifyDataSetChanged();
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
			MessageHolder messageHolder;
			int type = getItemViewType(position);
			if (convertView == null) {
				messageHolder = new MessageHolder();
				switch (type) {
				case Message.MESSAGE_TYPE_SEND:
					convertView = mInflater.inflate(R.layout.f_chat_item_right, null);
					messageHolder.text = convertView.findViewById(R.id.rl_chatright);
					break;
				case Message.MESSAGE_TYPE_RECEIVE:
					convertView = mInflater.inflate(R.layout.f_chat_item_left, null);
					messageHolder.text = convertView.findViewById(R.id.rl_chatleft);
					break;
				default:
					break;
				}
				messageHolder.image = convertView.findViewById(R.id.rl_chatleft_image);
				messageHolder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
				messageHolder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
				messageHolder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
				messageHolder.tv_chat = (TextView) convertView.findViewById(R.id.tv_chat);
				convertView.setTag(messageHolder);
			} else {
				messageHolder = (MessageHolder) convertView.getTag();
			}
			Message message = (Message) getItem(position);
			if (message.contentType.equals("text")) {
				messageHolder.text.setVisibility(View.VISIBLE);
				messageHolder.image.setVisibility(View.GONE);
				messageHolder.tv_chat.setText(message.content);
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
				app.fileHandler.getHeadImage(headFileName, new FileResult() {
					@Override
					public void onResult(String where) {
						iv_head.setImageBitmap(app.fileHandler.bitmaps.get(headFileName));
					}
				});
			} else if (message.contentType.equals("image")) {
				messageHolder.text.setVisibility(View.GONE);
				messageHolder.image.setVisibility(View.VISIBLE);
				final String imageFileName = message.content;
				final ImageView iv_image = messageHolder.iv_image;
				app.fileHandler.getImage(imageFileName, new FileResult() {
					@Override
					public void onResult(String where) {
						iv_image.setImageBitmap(app.fileHandler.bitmaps.get(imageFileName));
						if (where == app.fileHandler.FROM_WEB) {
							mAdapter.notifyDataSetChanged();
						}
					}
				});
				switch (type) {
				case Message.MESSAGE_TYPE_SEND:
					break;
				case Message.MESSAGE_TYPE_RECEIVE:
					messageHolder.tv_nickname.setText(mNowChatFriend.nickName);
					break;
				default:
					break;
				}
			} else if (message.contentType.equals("voice")) {

			}
			return convertView;
		}
	}

	class MessageHolder {
		View text;
		ImageView iv_head;
		TextView tv_chat;

		View image;
		ImageView iv_image;
		TextView tv_nickname;
	}

	public void sendMessage(final String type, final String content) {
		final Message message = new Message();
		message.type = Message.MESSAGE_TYPE_SEND;
		message.sendType = "point";
		message.content = content;
		message.contentType = type;
		message.status = "sending";
		message.phone = mNowChatFriend.phone;
		message.time = String.valueOf(new Date().getTime());

		app.dataHandler.exclude(new Modification() {
			@Override
			public void modifyData(Data data) {
				data.friends.get(mNowChatFriend.phone).messages.add(message);
				data.lastChatFriends.remove(mNowChatFriend.phone);
				data.lastChatFriends.add(0, mNowChatFriend.phone);
			}

			@Override
			public void modifyUI() {
				mAdapter.notifyDataSetChanged();
				chatContent.setSelection(mAdapter.getCount() - 1);
				if (mMainModeManager.mCirclesFragment.isAdded()) {
					// TODO refresh
					// mMainModeManager.mCirclesFragment.mAdapter.notifyDataSetChanged();
				}
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
				if (app.data.lastChatFriends.indexOf(mNowChatFriend.phone) != 0) {
					app.data.lastChatFriends.remove(mNowChatFriend.phone);
					app.data.lastChatFriends.add(0, mNowChatFriend.phone);
				}
			}

			@Override
			protected void unSuccess(JSONObject jData) {
				message.status = "failed";
				super.unSuccess(jData);
			}
		});

	}

	public Map<String, String> generateMessageParams(String type, String content) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("phone", app.data.user.phone);
		params.put("accessKey", app.data.user.accessKey);
		params.put("sendType", "point");
		JSONArray jFriends = new JSONArray();
		jFriends.put(mNowChatFriend.phone);
		params.put("phoneto", jFriends.toString());
		JSONObject jMessage = new JSONObject();
		try {
			jMessage.put("contentType", type);
			jMessage.put("content", content);
			params.put("message", jMessage.toString());
		} catch (JSONException e) {
		}

		return params;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_SELECTPICTURE && resultCode == Activity.RESULT_OK && data != null) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			final String picturePath = cursor.getString(columnIndex).toLowerCase(Locale.getDefault());
			final String format = picturePath.substring(picturePath.lastIndexOf("."));
			cursor.close();

			final Bitmap bitmap = MCImageUtils.getZoomBitmapFromFile(new File(picturePath), 960, 540);
			if (bitmap != null) {
				app.fileHandler.saveBitmap(new SaveBitmapInterface() {

					@Override
					public void setParams(SaveSettings settings) {
						settings.compressFormat = format.equals(".jpg") ? settings.JPG : settings.PNG;
						settings.source = bitmap;
					}

					@Override
					public void onSuccess(String fileName, String base64) {
						checkImage(fileName, base64);
					}
				});
			}

		} else if (requestCode == RESULT_TAKEPICTURE && resultCode == Activity.RESULT_OK) {

		} else if (requestCode == RESULT_CATPICTURE && resultCode == Activity.RESULT_OK && data != null) {

		}
	}

	void selectPicture() {
		Intent selectFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

	public void checkImage(final String fileName, final String base64) {
		app.networkHandler.connection(new CommonNetConnection() {

			@Override
			protected void settings(Settings settings) {
				settings.url = API.DOMAIN + API.IMAGE_CHECK;
				Map<String, String> params = new HashMap<String, String>();
				params.put("phone", app.data.user.phone);
				params.put("accessKey", app.data.user.accessKey);
				params.put("filename", fileName);
				settings.params = params;
			}

			@Override
			public void success(JSONObject jData) {
				try {
					if (jData.getBoolean("exists")) {
						sendMessage("image", fileName);
					} else {
						uploadImage(fileName, base64);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	public void uploadImage(final String fileName, final String base64) {
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
				sendMessage("image", fileName);
			}

		});
	}

}
