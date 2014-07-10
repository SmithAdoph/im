package com.lejoying.wxgs.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lejoying.wxgs.R;
import com.lejoying.wxgs.activity.mode.fragment.GroupShareFragment;
import com.lejoying.wxgs.activity.utils.CommonNetConnection;
import com.lejoying.wxgs.activity.utils.TimeUtils;
import com.lejoying.wxgs.activity.view.widget.Alert;
import com.lejoying.wxgs.app.MainApplication;
import com.lejoying.wxgs.app.data.API;
import com.lejoying.wxgs.app.data.Data;
import com.lejoying.wxgs.app.data.entity.Comment;
import com.lejoying.wxgs.app.data.entity.GroupShare;
import com.lejoying.wxgs.app.data.entity.GroupShare.VoiceContent;
import com.lejoying.wxgs.app.handler.DataHandler.Modification;
import com.lejoying.wxgs.app.handler.NetworkHandler.NetConnection;
import com.lejoying.wxgs.app.handler.NetworkHandler.Settings;
import com.lejoying.wxgs.app.handler.OSSFileHandler.FileResult;
import com.lejoying.wxgs.app.parser.JSONParser;

public class DetailsActivity extends Activity implements OnClickListener {
	MainApplication app = MainApplication.getMainApplication();
	InputMethodManager inputMethodManager;

	Intent intent;
	Handler handler;
	LayoutInflater inflater;
	GroupShare share;

	float height, width, dip;
	float density;

	int initialHeight, headWidth;
	
	String nickNameTo,phoneTo;

	boolean praiseStatus = false;

	LinearLayout ll_message_info, ll_detailContent, ll_praise, ll_praiseMember,
			ll_messageDetailComments;
	RelativeLayout rl_sendComment, backView,rl_comment;
	TextView tv_praiseNum, tv_checkComment, tv_sendComment,
			tv_squareMessageSendUserName, tv_messageTime;
	ImageView iv_addPraise, iv_checkComment, iv_comment,
			iv_squareMessageDetailBack, iv_messageUserHead;
	ScrollView sv_message_info;
	EditText et_comment;

	LayoutParams commentLayoutParams;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		initLayout();
		initEvent();
		initData();
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		initialHeight = et_comment.getHeight();
		headWidth = ll_praiseMember.getWidth()/5;
		rl_comment.setVisibility(View.GONE);
		super.onWindowFocusChanged(hasFocus);
	}
	private void initEvent() {
		iv_addPraise.setOnClickListener(this);
		ll_praise.setOnClickListener(this);
		iv_checkComment.setOnClickListener(this);
		iv_comment.setOnClickListener(this);
		tv_sendComment.setOnClickListener(this);

		backView.setOnTouchListener(new OnTouchListener() {
			GestureDetector backviewDetector = new GestureDetector(
					DetailsActivity.this,
					new GestureDetector.SimpleOnGestureListener() {

						@Override
						public boolean onDown(MotionEvent e) {
							return true;
						}

						@Override
						public boolean onSingleTapUp(MotionEvent e) {
							finish();
							return true;
						}

					});

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					backView.setBackgroundColor(Color.argb(143, 0, 0, 0));
					break;
				case MotionEvent.ACTION_UP:
					backView.setBackgroundColor(Color.argb(0, 0, 0, 0));
					break;
				}
				return backviewDetector.onTouchEvent(event);
			}
		});
		et_comment.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if ("".equals(et_comment.getText().toString())) {
					tv_sendComment
							.setBackgroundResource(R.drawable.squaredetail_comment_notselected);
					tv_sendComment.setTextColor(Color.WHITE);
				} else {
					tv_sendComment
							.setBackgroundResource(R.drawable.squaredetail_comment_selected);
					tv_sendComment.setTextColor(Color.BLACK);
				}
			}

		});
	}

	private void initLayout() {
		ll_message_info = (LinearLayout) findViewById(R.id.ll_message_info);
		ll_detailContent = (LinearLayout) findViewById(R.id.ll_detailContent);
		ll_praise = (LinearLayout) findViewById(R.id.ll_praise);
		ll_praiseMember = (LinearLayout) findViewById(R.id.ll_praiseMember);
		ll_messageDetailComments = (LinearLayout) findViewById(R.id.ll_messageDetailComments);
		rl_sendComment = (RelativeLayout) findViewById(R.id.rl_sendComment);
		rl_comment = (RelativeLayout) findViewById(R.id.rl_comment);
		backView = (RelativeLayout) findViewById(R.id.backview);
		sv_message_info = (ScrollView) findViewById(R.id.sv_message_info);
		tv_praiseNum = (TextView) findViewById(R.id.tv_praiseNum);
		tv_checkComment = (TextView) findViewById(R.id.tv_checkComment);
		tv_sendComment = (TextView) findViewById(R.id.tv_sendComment);
		tv_squareMessageSendUserName = (TextView) findViewById(R.id.tv_squareMessageSendUserName);
		tv_messageTime = (TextView) findViewById(R.id.tv_messageTime);
		iv_addPraise = (ImageView) findViewById(R.id.iv_addPraise);
		iv_checkComment = (ImageView) findViewById(R.id.iv_checkComment);
		iv_comment = (ImageView) findViewById(R.id.iv_comment);
		iv_squareMessageDetailBack = (ImageView) findViewById(R.id.iv_squareMessageDetailBack);
		iv_messageUserHead = (ImageView) findViewById(R.id.iv_messageUserHead);
		et_comment = (EditText) findViewById(R.id.et_comment);
	}

	private void initData() {
		inflater = getLayoutInflater();
		inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		density = dm.density;
		dip = (int) (40 * density + 0.5f);
		height = dm.heightPixels;
		width = dm.widthPixels;
		intent = getIntent();
		share = (GroupShare) intent.getSerializableExtra("content");
		nickNameTo = "";phoneTo="";
		handler = new Handler();
		final List<String> images = share.content.images;
		List<VoiceContent> voices = share.content.voices;
		String textContent = share.content.text;
		tv_messageTime.setText(TimeUtils.getTime(share.time));
		for (String str : share.praiseusers) {
			if (str.equals(app.data.user.phone)) {
				praiseStatus = true;
				break;
			}
		}
		for (int i = 0; i < images.size(); i++) {
			final int index = i;
			final ImageView imageView = new ImageView(this);
			ll_detailContent.addView(imageView);
			app.fileHandler.getSquareDetailImage(images.get(i), (int) width,
					new FileResult() {

						@Override
						public void onResult(String where, Bitmap bitmap) {
							int height = (int) (bitmap.getHeight() * (width / bitmap
									.getWidth()));
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
									(int) width, height);
							imageView.setLayoutParams(params);
							imageView.setImageBitmap(bitmap);
							imageView.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(
											DetailsActivity.this,
											PicAndVoiceDetailActivity.class);
									intent.putExtra("currentIndex", index);
									intent.putExtra("Activity", "Browse");
									intent.putStringArrayListExtra("content",
											(ArrayList<String>) images);
									startActivity(intent);
								}
							});
						}
					});
		}
		for (VoiceContent str : voices) {

		}
		if (!"".equals(textContent)) {
			TextView textview = new TextView(this);
			textview.setTextColor(Color.WHITE);
			textview.setBackgroundColor(Color.parseColor("#26ffffff"));
			textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			int padding = (int) (10 * density + 0.5f);
			textview.setPadding(padding, padding, padding, padding);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			textview.setLayoutParams(params);
			textview.setText(textContent);
			ll_detailContent.addView(textview);
		}

		app.fileHandler.getHeadImage(app.data.user.head, app.data.user.sex,
				new FileResult() {
					@Override
					public void onResult(String where, Bitmap bitmap) {
						iv_messageUserHead.setImageBitmap(bitmap);
					}
				});
		resetPraises();
		resetComments();
	}

	private void resetPraises() {
		System.out.println(headWidth);
		int headwidth=(int) (33 * density + 0.5f);
		int padding = (int) (5 * density + 0.5f);
		List<String> praiseusers = share.praiseusers;
		tv_praiseNum.setText("共获得" + praiseusers.size() + "个赞");
		ll_praiseMember.removeAllViews();
		for (int i = 0; i < praiseusers.size(); i++) {
			final ImageView view = new ImageView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					headwidth, headwidth);
			view.setPadding(padding, padding, padding, padding);
			view.setLayoutParams(params);
			app.fileHandler.getHeadImage(
					app.data.groupFriends.get(praiseusers.get(i)).head,
					app.data.groupFriends.get(praiseusers.get(i)).sex,
					new FileResult() {

						@Override
						public void onResult(String where, Bitmap bitmap) {
							view.setImageBitmap(bitmap);
						}
					});
			ll_praiseMember.addView(view);
			if (i == 5)
				break;
		}
	}

	private void resetComments() {
		List<Comment> comments = share.comments;
		tv_checkComment.setText("查看全部" + comments.size() + "条评论...");
		ll_messageDetailComments.removeAllViews();
		for (final Comment comment : comments) {
			View view = inflater
					.inflate(R.layout.groupshare_commentchild, null);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins((int) (10 * density + 0.5f), 0,
					(int) (10 * density + 0.5f), 0);
			view.setLayoutParams(params);
			TextView time = (TextView) view.findViewById(R.id.time);
			TextView content = (TextView) view.findViewById(R.id.content);
			TextView reply = (TextView) view.findViewById(R.id.reply);
			TextView receive = (TextView) view.findViewById(R.id.receive);
			TextView received = (TextView) view.findViewById(R.id.received);
			final ImageView head = (ImageView) view.findViewById(R.id.head);

			content.setText(comment.content);
			time.setText(TimeUtils.getTime(comment.time));
			receive.setText(comment.nickName);
			received.setText(comment.nickNameTo);

			if ("".equals(comment.nickNameTo)) {
				reply.setVisibility(View.GONE);
				received.setVisibility(View.GONE);
			}
			app.fileHandler.getHeadImage(comment.head, "男", new FileResult() {
				@Override
				public void onResult(String where, Bitmap bitmap) {
					head.setImageBitmap(bitmap);
				}
			});
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (rl_comment.getVisibility() == View.GONE) {
						rl_comment.setVisibility(View.VISIBLE);
						handler.post(new Runnable() {
							@Override
							public void run() {
								sv_message_info
										.fullScroll(ScrollView.FOCUS_DOWN);
							}
						});
					}
					if (!comment.phone.equals(app.data.user.phone)) {
						phoneTo = comment.phone;
						nickNameTo = comment.nickName;
						et_comment.setHint("回复" + nickNameTo);
					} else {
						phoneTo = "";
						nickNameTo = "";
						et_comment.setHint("添加评论 ... ...");
					}

				}
			});
			ll_messageDetailComments.addView(view);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_addPraise:
			addPraise(!praiseStatus);
			break;
		case R.id.ll_praise:
			// TODO show the praised members
			break;
		case R.id.iv_checkComment:
			if (!"".equals(phoneTo)) {
				et_comment.setText("");
				et_comment.setHint("添加评论 ... ...");
				phoneTo = "";
				nickNameTo = "";
			} else {
				if (rl_comment.getVisibility() == View.VISIBLE) {
					et_comment.setText("");
					et_comment.setHint("添加评论 ... ...");
					commentLayoutParams = rl_comment.getLayoutParams();
					commentLayoutParams.height = (int) (45 * density + 0.5f);
					rl_comment.setLayoutParams(commentLayoutParams);
					rl_comment.setVisibility(View.GONE);
				} else {
					phoneTo = "";
					nickNameTo = "";
					rl_comment.setVisibility(View.VISIBLE);
					handler.post(new Runnable() {

						@Override
						public void run() {
							sv_message_info.fullScroll(ScrollView.FOCUS_DOWN);
						}
					});
				}
			}			break;
		case R.id.iv_comment:
			// TODO show the faces
			break;
		case R.id.tv_sendComment:
			sendComment();
			break;
		default:
			break;
		}

	}

	private void addPraise(final boolean flag) {
		app.networkHandler.connection(new CommonNetConnection() {
			@Override
			public void success(JSONObject jData) {
				praiseStatus = !praiseStatus;
				modifyShare();
			}

			@Override
			protected void settings(Settings settings) {
				settings.url = API.DOMAIN + API.SHARE_ADDPRAISE;
				Map<String, String> params = new HashMap<String, String>();
				params.put("phone", app.data.user.phone);
				params.put("accessKey", app.data.user.accessKey);
				params.put("gid", app.data.currentGroup);
				params.put("gsid", share.gsid);
				params.put("option", flag + "");
				settings.params = params;

			}

		});

	}

	private void sendComment() {
		if (inputMethodManager.isActive()) {
			inputMethodManager.hideSoftInputFromWindow(DetailsActivity.this
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
		final String commentContent = et_comment.getText().toString().trim();
		if ("".equals(commentContent)) {
			Alert.showMessage("评论内容不能为空");
			return;
		}
		final long time = new Date().getTime();
		app.networkHandler.connection(new CommonNetConnection() {

			@Override
			protected void settings(Settings settings) {
				settings.url = API.DOMAIN + API.SHARE_ADDCOMMENT;
				Map<String, String> params = new HashMap<String, String>();
				params.put("phone", app.data.user.phone);
				params.put("phoneTo", share.phone);
				params.put("accessKey", app.data.user.accessKey);
				params.put("nickName", app.data.user.nickName);
				params.put("nickNameTo", nickNameTo);
				params.put("head", app.data.user.head);
				params.put("gid", app.data.currentGroup);
				params.put("gsid", share.gsid);
				params.put("time", String.valueOf(time));
				params.put("contentType", "text");
				params.put("content", commentContent);
				settings.params = params;

			}

			@Override
			public void success(JSONObject jData) {
				modifyShare();
			}
		});
	}

	private void modifyShare() {
		app.networkHandler.connection(new CommonNetConnection() {

			@Override
			public void success(JSONObject jData) {
				final GroupShare newShare;
				try {
					ArrayList<GroupShare> shares = JSONParser
							.generateSharesFromJSON(jData
									.getJSONArray("shares"));
					newShare = shares.get(0);
					share = newShare;
					app.dataHandler.exclude(new Modification() {

						@Override
						public void modifyData(Data data) {
							data.groupsMap
									.get(GroupShareFragment.mCurrentGroupShareID).groupSharesMap
									.put(newShare.gsid, newShare);
							phoneTo = "";
							nickNameTo = "";
						}

						@Override
						public void modifyUI() {
							et_comment.setText("");
							et_comment.setHint("添加评论 ... ...");
							resetPraises();
							resetComments();
							handler.post(new Runnable() {
								@Override
								public void run() {
									sv_message_info
											.fullScroll(ScrollView.FOCUS_DOWN);
								}
							});
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			protected void settings(Settings settings) {
				settings.url = API.DOMAIN + API.SHARE_GETSHARE;
				Map<String, String> params = new HashMap<String, String>();
				params.put("phone", app.data.user.phone);
				params.put("accessKey", app.data.user.accessKey);
				params.put("gid", app.data.currentGroup);
				params.put("gsid", share.gsid);
				settings.params = params;

			}
		});

	}
}
