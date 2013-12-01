package com.lejoying.mc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.lejoying.mc.fragment.BaseInterface;
import com.lejoying.mc.fragment.CircleMenuFragment;
import com.lejoying.mc.view.BackgroundView;

public abstract class BaseFragmentActivity extends FragmentActivity implements
		BaseInterface {

	private FragmentManager mFragmentManager;

	private CircleMenuFragment mCircle;

	private int mContentId;

	public abstract Fragment setFirstPreview();

	protected abstract int setBackground();

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mFragmentManager = getSupportFragmentManager();

		mContentId = R.id.fl_content;

		if (setFirstPreview() != null) {
			if (mFragmentManager.getFragments() == null
					|| mFragmentManager.getFragments().size() == 0) {
				mFragmentManager.beginTransaction()
						.replace(mContentId, setFirstPreview()).commit();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		View backgroundView = findViewById(R.id.fl_background);

		if (backgroundView != null) {
			BackgroundView background = (BackgroundView) backgroundView
					.findViewById(R.id.background);
			background.setBackground(setBackground());
		}

		View circleMenuView = findViewById(R.id.fl_circleMenu);

		if (circleMenuView != null) {
			circleMenuView.getParent().bringChildToFront(circleMenuView);
			mCircle = new CircleMenuFragment();
			mFragmentManager.beginTransaction()
					.replace(R.id.fl_circleMenu, mCircle).commit();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mCircle != null) {
			if (keyCode == KeyEvent.KEYCODE_BACK && mCircle.cancelMenu()) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void hideCircleMenu() {
		if (mCircle != null) {
			mCircle.hideCircleMenu(null);
		}
	}

	@Override
	public void showCircleMenuToTop(boolean lock, boolean showBack) {
		mCircle.showToTop(lock, showBack);
	}

	@Override
	public void showCircleMenuToBottom() {
		mCircle.showToBottom();
	}

	@Override
	public void setCircleMenuPageName(String pageName) {
		mCircle.setPageName(pageName);
	}

	@Override
	public int relpaceToContent(Fragment fragment, boolean toBackStack) {
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		transaction.setCustomAnimations(R.anim.activity_in,
				R.anim.activity_out, R.anim.activity_in2, R.anim.activity_out2);
		transaction.replace(mContentId, fragment);
		if (toBackStack) {
			transaction.addToBackStack(null);
		}
		return transaction.commit();
	}

}
