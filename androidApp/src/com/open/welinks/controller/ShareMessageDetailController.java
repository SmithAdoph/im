package com.open.welinks.controller;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.open.welinks.PictureBrowseActivity;
import com.open.welinks.R;
import com.open.welinks.SharePraiseusersActivity;
import com.open.welinks.controller.DownloadFile.DownloadListener;
import com.open.welinks.model.API;
import com.open.welinks.model.Data;
import com.open.welinks.model.Data.Shares.Share;
import com.open.welinks.model.Data.Shares.Share.Comment;
import com.open.welinks.model.Data.Shares.Share.ShareMessage;
import com.open.welinks.model.Data.UserInformation.User;
import com.open.welinks.model.ResponseHandlers;
import com.open.welinks.view.InnerScrollView.OnScrollChangedListener;
import com.open.welinks.view.PictureBrowseView;
import com.open.welinks.view.ShareMessageDetailView;

public class ShareMessageDetailController {

	public Data data = Data.getInstance();
	public String tag = "ShareMessageDetailController";

	public Context context;
	public ShareMessageDetailView thisView;
	public ShareMessageDetailController thisController;
	public Activity thisActivity;

	public String gsid = "";
	public ShareMessage shareMessage;
	public Share share;

	public OnClickListener mOnClickListener;
	public OnScrollChangedListener mOnScrollChangedListener;
	public OnTouchListener mOnTouchListener;
	public DownloadListener downloadListener;
	public TextWatcher textWatcher;

	public int IMAGEBROWSE_REQUESTCODE = 0x01;

	public DownloadFileList downloadFileList = DownloadFileList.getInstance();

	public ResponseHandlers responseHandlers = ResponseHandlers.getInstance();

	public User currentUser;

	public String phoneTo = "";
	public String nickNameTo = "";
	public String headTo;

	int initialHeight;

	public ShareMessageDetailController(Activity thisActivity) {
		this.thisActivity = thisActivity;
		this.context = thisActivity;
		thisController = this;

		currentUser = data.userInformation.currentUser;
	}

	public void initData() {
		share = data.shares.shareMap.get(data.localStatus.localData.currentSelectedGroup);
		String gsid = thisActivity.getIntent().getStringExtra("gsid");
		if (gsid != null) {
			this.gsid = gsid;
			shareMessage = data.shares.shareMap.get(data.localStatus.localData.currentSelectedGroup).shareMessagesMap.get(gsid);
		}
	}

	public void initializeListeners() {
		textWatcher = new TextWatcher() {

			String content = "";

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				content = s.toString();
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				LayoutParams commentLayoutParams = thisView.commentInputView.getLayoutParams();
				// Log.e(content, (45 * thisView.screenDensity + 0.5f) + "--------------height--" + thisView.commentEditTextView.getHeight() + "--------" + initialHeight);
				commentLayoutParams.height = (int) ((45 * thisView.screenDensity + 0.5f) + thisView.commentEditTextView.getHeight() - 40);
				// Log.e(content, commentLayoutParams.height + "--------------height");
				// thisView.commentInputView.setLayoutParams(commentLayoutParams);
			}

			@Override
			public void afterTextChanged(Editable s) {
				int selectionIndex = thisView.commentEditTextView.getSelectionStart();
				if (!(s.toString()).equals(content)) {
					thisView.commentEditTextView.setText(s.toString());
					thisView.commentEditTextView.setSelection(selectionIndex);
				}
				if ("".equals(thisView.commentEditTextView.getText().toString())) {
					thisView.sendCommentView.setBackgroundResource(R.drawable.comment_notselected);
					thisView.sendCommentView.setTextColor(Color.WHITE);
				} else {
					thisView.sendCommentView.setBackgroundResource(R.drawable.comment_selected);
					thisView.sendCommentView.setTextColor(Color.BLACK);
				}
			}
		};
		downloadListener = new DownloadListener() {

			@Override
			public void success(DownloadFile instance, int status) {
				final ImageView imageView = (ImageView) instance.view;
				thisView.imageLoader.displayImage("file://" + instance.path, imageView, thisView.displayImageOptions, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						int height = (int) (loadedImage.getHeight() * (thisView.screenWidth / loadedImage.getWidth()));
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) thisView.screenWidth, height);
						imageView.setLayoutParams(params);
					}
				});
			}

			@Override
			public void loading(DownloadFile instance, int precent, int status) {
				// TODO Auto-generated method stub

			}

			@Override
			public void failure(DownloadFile instance, int status) {
				// TODO Auto-generated method stub

			}
		};
		mOnTouchListener = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// if (thisView.commentInputView.getVisibility() == View.VISIBLE) {
				// thisView.commentInputView.setVisibility(View.GONE);
				// }
				return false;
			}
		};
		mOnScrollChangedListener = new OnScrollChangedListener() {

			@Override
			public void onScrollChangedListener(int l, int t, int oldl, int oldt) {
				if (thisView.commentInputView.getVisibility() == View.VISIBLE) {
					thisView.commentInputView.setVisibility(View.GONE);
				}
			}
		};
		mOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (view.equals(thisView.backView)) {
					thisActivity.finish();
				} else if (view.equals(thisView.praiseIconView)) {
					boolean option = false;
					if (!shareMessage.praiseusers.contains(currentUser.phone)) {
						option = true;
						shareMessage.praiseusers.add(currentUser.phone);
						thisView.praiseIconView.setImageResource(R.drawable.praised_icon);
					} else {
						shareMessage.praiseusers.remove(currentUser.phone);
						thisView.praiseIconView.setImageResource(R.drawable.praise_icon);
					}
					thisView.showPraiseUsersContent();
					modifyPraiseusersToMessage(option);
				} else if (view.equals(thisView.commentIconView)) {
					if (thisView.commentInputView.getVisibility() == View.GONE) {
						thisView.commentInputView.setVisibility(View.VISIBLE);
						int offset = thisView.mainScrollInnerView.getMeasuredHeight() - thisView.mainScrollView.getHeight();
						thisView.mainScrollView.scrollTo(0, offset);
					} else {
						thisView.commentInputView.setVisibility(View.GONE);
					}
				} else if (view.equals(thisView.praiseUserContentView)) {
					// Toast.makeText(thisActivity, "praiseUserContentView", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(thisActivity, SharePraiseusersActivity.class);
					data.tempData.praiseusersList = shareMessage.praiseusers;
					thisActivity.startActivity(intent);
				} else if (view.equals(thisView.shareMessageTimeView)) {
					if (thisView.menuOptionsView.getVisibility() == View.GONE) {
						thisView.menuOptionsView.setVisibility(View.VISIBLE);
						thisView.dialogSpring.addListener(thisView.dialogSpringListener);
						thisView.dialogSpring.setCurrentValue(0);
						thisView.dialogSpring.setEndValue(1);
					} else {
						thisView.menuOptionsView.setVisibility(View.GONE);
					}
				} else if (view.equals(thisView.confirmSendCommentView)) {
					String commentContent = thisView.commentEditTextView.getText().toString().trim();
					thisView.commentEditTextView.setText("");
					phoneTo = "";
					nickNameTo = "";
					headTo = "";
					thisView.commentEditTextView.setHint("添加评论 ... ...");
					if (thisView.commentInputView.getVisibility() == View.VISIBLE) {
						thisView.commentInputView.setVisibility(View.GONE);
					}
					if (thisView.inputMethodManager.isActive()) {
						thisView.inputMethodManager.hideSoftInputFromWindow(thisView.commentInputView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					}
					Comment comment = share.new Comment();
					comment.phone = currentUser.phone;
					comment.nickName = currentUser.nickName;
					comment.head = currentUser.head;
					comment.phoneTo = phoneTo;
					comment.nickNameTo = nickNameTo;
					comment.headTo = headTo;
					comment.contentType = "text";
					comment.content = commentContent;
					comment.time = new Date().getTime();
					shareMessage.comments.add(comment);

					thisView.notifyShareMessageComments();

					thisView.mainScrollView.fullScroll(ScrollView.FOCUS_DOWN);

					addCommentToMessage("text", commentContent);
				} else if (view.getTag() != null) {
					String tagContent = (String) view.getTag();
					int index = tagContent.lastIndexOf("#");
					String type = tagContent.substring(0, index);
					String content = tagContent.substring(index + 1);
					if ("ShareMessageDetailImage".equals(type)) {
						data.tempData.selectedImageList = thisView.showImages;
						Intent intent = new Intent(thisActivity, PictureBrowseActivity.class);
						intent.putExtra("position", content);
						intent.putExtra("type", PictureBrowseView.IMAGEBROWSE_COMMON);
						thisActivity.startActivityForResult(intent, IMAGEBROWSE_REQUESTCODE);
					} else if ("ShareComment".equals(type)) {
						Comment comment = (Comment) view.getTag(R.id.commentEditTextView);
						if (thisView.commentInputView.getVisibility() == View.GONE) {
							thisView.commentInputView.setVisibility(View.VISIBLE);
						}
						if (!content.equals(currentUser.phone)) {
							phoneTo = comment.phone;
							nickNameTo = comment.nickName;
							headTo = comment.head;
							thisView.commentEditTextView.setHint("回复" + nickNameTo);
						} else {
							phoneTo = "";
							nickNameTo = "";
							headTo = "";
							thisView.commentEditTextView.setHint("添加评论 ... ...");
						}
					}
				}
			}
		};
	}

	public void bindEvent() {
		thisView.shareMessageTimeView.setOnClickListener(mOnClickListener);
		thisView.backView.setOnClickListener(mOnClickListener);
		thisView.praiseUserContentView.setOnClickListener(mOnClickListener);
		thisView.praiseIconView.setOnClickListener(mOnClickListener);
		thisView.commentIconView.setOnClickListener(mOnClickListener);
		thisView.confirmSendCommentView.setOnClickListener(mOnClickListener);

		thisView.mainScrollView.setOnTouchListener(mOnTouchListener);
		thisView.detailScrollView.setOnScrollChangedListener(mOnScrollChangedListener);

		thisView.commentEditTextView.addTextChangedListener(textWatcher);
	}

	public void modifyPraiseusersToMessage(boolean option) {
		RequestParams params = new RequestParams();
		HttpUtils httpUtils = new HttpUtils();
		params.addBodyParameter("phone", data.userInformation.currentUser.phone);
		params.addBodyParameter("accessKey", data.userInformation.currentUser.accessKey);
		params.addBodyParameter("gid", data.localStatus.localData.currentSelectedGroup);
		params.addBodyParameter("gsid", shareMessage.gsid);
		params.addBodyParameter("option", option + "");

		httpUtils.send(HttpMethod.POST, API.SHARE_ADDPRAISE, params, responseHandlers.share_modifyPraiseusersCallBack);
	}

	public void addCommentToMessage(String contentType, String content) {
		RequestParams params = new RequestParams();
		HttpUtils httpUtils = new HttpUtils();
		params.addBodyParameter("phone", currentUser.phone);
		params.addBodyParameter("accessKey", currentUser.accessKey);
		params.addBodyParameter("gid", data.localStatus.localData.currentSelectedGroup);
		params.addBodyParameter("gsid", shareMessage.gsid);
		params.addBodyParameter("nickName", currentUser.nickName);
		params.addBodyParameter("head", currentUser.head);
		params.addBodyParameter("phoneTo", phoneTo);
		params.addBodyParameter("nickNameTo", nickNameTo);
		params.addBodyParameter("headTo", headTo);
		params.addBodyParameter("contentType", contentType);
		params.addBodyParameter("content", content);

		httpUtils.send(HttpMethod.POST, API.SHARE_ADDCOMMENT, params, responseHandlers.share_addCommentCallBack);
	}

	public void finish() {
		data.tempData.selectedImageList = null;
	}

	public void onWindwoFocusChanged() {
		initialHeight = thisView.commentEditTextView.getHeight();
	}
}
