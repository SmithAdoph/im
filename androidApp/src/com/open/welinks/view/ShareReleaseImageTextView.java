package com.open.welinks.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.open.lib.TouchImageView;
import com.open.lib.TouchView;
import com.open.welinks.R;
import com.open.welinks.controller.ShareReleaseImageTextController;
import com.open.welinks.model.Data;

public class ShareReleaseImageTextView {
	public Data data = Data.getInstance();
	public String tag = "ShareReleaseImageTextView";

	public Context context;
	public ShareReleaseImageTextView thisView;
	public ShareReleaseImageTextController thisController;
	public Activity thisActivity;

	public EditText mEditTextView;
	public TouchView mImagesContentView;
	public RelativeLayout mReleaseButtomBarView;

	public TextView mCancleButtonView;
	public TextView mConfirmButtonView;
	public ImageView mSelectImageButtonView;

	public DisplayMetrics displayMetrics;
	public ImageLoader imageLoader = ImageLoader.getInstance();
	public DisplayImageOptions options;

	public MyScrollImageBody myScrollImageBody;

	public ShareReleaseImageTextView(Activity thisActivity) {
		this.context = thisActivity;
		this.thisActivity = thisActivity;
	}

	public void initView() {
		displayMetrics = new DisplayMetrics();

		thisActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		thisActivity.setContentView(R.layout.share_release_imagetext);
		mEditTextView = (EditText) thisActivity.findViewById(R.id.releaseTextContentView);
		mImagesContentView = (TouchView) thisActivity.findViewById(R.id.releaseImagesContent);
		mReleaseButtomBarView = (RelativeLayout) thisActivity.findViewById(R.id.releaseButtomBar);
		mCancleButtonView = (TextView) thisActivity.findViewById(R.id.releaseCancel);
		mConfirmButtonView = (TextView) thisActivity.findViewById(R.id.releaseConfirm);
		mSelectImageButtonView = (ImageView) thisActivity.findViewById(R.id.selectImageButton);

		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_stub).showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_error).considerExifParams(true).displayer(new RoundedBitmapDisplayer(0)).build();
		myScrollImageBody = new MyScrollImageBody();
		myScrollImageBody.initialize(mImagesContentView);
	}

	int width;

	public void showSelectedImages() {
		this.mImagesContentView.removeAllViews();
		ArrayList<String> selectedImageList = data.tempData.selectedImageList;
		if (selectedImageList.size() > 0)
			this.mImagesContentView.setVisibility(View.VISIBLE);
		for (int i = 0; i < selectedImageList.size(); i++) {
			String key = selectedImageList.get(i);
			ImageBody imageBody = new ImageBody();
			imageBody.initialize();

			width = (int) (displayMetrics.density * 50);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, width);
			myScrollImageBody.contentView.addView(imageBody.imageView, layoutParams);
			float x = i * (width + 2 * displayMetrics.density) + 2 * displayMetrics.density;
			if (i == 0) {
				x = 2 * displayMetrics.density;
			}
			imageBody.imageView.setX(x);// Translation
			imageLoader.displayImage("file://" + key, imageBody.imageView, options);
			myScrollImageBody.selectedImagesSequence.add(key);
			myScrollImageBody.selectedImagesSequenceMap.put(key, imageBody);
			imageBody.imageView.setTag(i);
			imageBody.imageView.setOnClickListener(thisController.monClickListener);
		}
		myScrollImageBody.contentView.setOnTouchListener(thisController.onTouchListener);
	}

	public class MyScrollImageBody {
		public ArrayList<String> selectedImagesSequence = new ArrayList<String>();
		public HashMap<String, ImageBody> selectedImagesSequenceMap = new HashMap<String, ImageBody>();

		public TouchView contentView;

		public TouchView initialize(TouchView view) {
			this.contentView = view;
			return view;
		}

		public void recordChildrenPosition() {
			for (int i = 0; i < selectedImagesSequence.size(); i++) {
				String key = selectedImagesSequence.get(i);
				ImageBody imageBody = selectedImagesSequenceMap.get(key);
				imageBody.x = imageBody.imageView.getX();
				imageBody.y = imageBody.imageView.getY();
			}
		}

		public void setChildrenPosition(float deltaX, float deltaY) {
			float screenWidth = displayMetrics.widthPixels;
			float totalLength = selectedImagesSequence.size() * (width + 2 * displayMetrics.density) + 2 * displayMetrics.density;
			if (totalLength < screenWidth) {
				return;
			}
			for (int i = 0; i < selectedImagesSequence.size(); i++) {
				String key = selectedImagesSequence.get(i);
				ImageBody imageBody = selectedImagesSequenceMap.get(key);
				if ((imageBody.x + deltaX) < (screenWidth - totalLength))
					break;
				if (i == 0 && (imageBody.x + deltaX) > (5 * displayMetrics.density))
					break;
				imageBody.imageView.setX(imageBody.x + deltaX);
				imageBody.imageView.setY(imageBody.y + deltaY);
			}
		}
	}

	public class ImageBody {
		public int i;

		public float x;
		public float y;
		public TouchImageView imageView;

		public TouchImageView initialize() {
			this.imageView = new TouchImageView(context);
			return this.imageView;
		}
	}
}
