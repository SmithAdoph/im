package com.lejoying.mc.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lejoying.mc.MainActivity;
import com.lejoying.mc.R;
import com.lejoying.mc.api.API;
import com.lejoying.utils.SHA1;

public class LoginUsePassFragment extends BaseFragment implements
		OnClickListener {

	private View mContent;

	private EditText mView_phone;
	private EditText mView_pass;
	private Button mView_login;
	private Button mView_register;
	private TextView mView_clogin;
	private SHA1 mSha1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMCFragmentManager.hideCircleMenu();

		mContent = inflater.inflate(R.layout.f_plogin, null);

		mView_phone = (EditText) mContent.findViewById(R.id.et_plogin_phone);
		mView_pass = (EditText) mContent.findViewById(R.id.et_plogin_pass);
		mView_login = (Button) mContent.findViewById(R.id.btn_login);
		mView_register = (Button) mContent.findViewById(R.id.btn_register);
		mView_clogin = (TextView) mContent.findViewById(R.id.tv_clogin);

		mView_login.setOnClickListener(this);
		mView_register.setOnClickListener(this);
		mView_clogin.setOnClickListener(this);

		mSha1 = new SHA1();

		return mContent;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_clogin:
			mMCFragmentManager.replaceToContent(new LoginUseCodeFragment(),
					true);
			break;
		case R.id.btn_login:
			if (mView_phone.getText().toString().equals("")) {
				showMsg(getString(R.string.app_phonenotnull));
				showSoftInput(mView_phone);
				return;
			}
			if (mView_pass.getText().toString().equals("")) {
				showMsg(getString(R.string.app_passnotnull));
				showSoftInput(mView_pass);
				return;
			}
			Bundle params = new Bundle();
			params.putString("phone", mView_phone.getText().toString());
			String pass = mSha1.getDigestOfString(mView_pass.getText()
					.toString().getBytes());
			params.putString("password", pass);

			mMCFragmentManager.startNetworkForResult(API.ACCOUNT_AUTH, params,
					true, new NetworkStatusAdapter() {
						@Override
						public void success() {
							mMCFragmentManager.startToActivity(
									MainActivity.class, true);
						}
					});

			break;
		case R.id.btn_register:
			mMCFragmentManager.replaceToContent(new RegisterPhoneFragment(),
					true);
			break;
		default:
			break;
		}
	}

	@Override
	public EditText showSoftInputOnShow() {
		return mView_phone;
	}

}
