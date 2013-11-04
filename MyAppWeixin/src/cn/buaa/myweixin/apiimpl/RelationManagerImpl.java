package cn.buaa.myweixin.apiimpl;

import java.util.Map;

import android.app.Activity;

import cn.buaa.myweixin.api.RelationManager;
import cn.buaa.myweixin.apiutils.MCTools;
import cn.buaa.myweixin.listener.ResponseListener;
import cn.buaa.myweixin.utils.HttpTools;

public class RelationManagerImpl implements RelationManager {

	private Activity activity;

	public RelationManagerImpl(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void addfriend(Map<String, String> param,
			ResponseListener responseListener) {
		MCTools.ajax(activity, "/api2/relation/addfriend", param, true,
				HttpTools.SEND_POST, 5000, responseListener);
	}

	@Override
	public void getfriends(Map<String, String> param,
			ResponseListener responseListener) {
		MCTools.ajax(activity, "/api2/relation/getfriends", param, true,
				HttpTools.SEND_POST, 5000, responseListener);
	}

	@Override
	public void getcommunities(Map<String, String> param,
			ResponseListener responseListener) {
		MCTools.ajax(activity, "/api2/relation/getcommnities", param, true,
				HttpTools.SEND_POST, 5000, responseListener);
	}

	@Override
	public void addcircle(Map<String, String> param,
			ResponseListener responseListener) {
		MCTools.ajax(activity, "/api2/relation/addcircle", param, true,
				HttpTools.SEND_POST, 5000, responseListener);
	}

	@Override
	public void getcirclesandfriends(Map<String, String> param,
			ResponseListener responseListener) {
		MCTools.ajax(activity, "/api2/relation/getcirclesandfriends", param, true,
				HttpTools.SEND_POST, 5000, responseListener);
	}

	@Override
	public void getaskfriends(Map<String, String> param,
			ResponseListener responseListener) {
		MCTools.ajax(activity, "/api2/relation/getaskfriends", param, true,
				HttpTools.SEND_POST, 5000, responseListener);
	}

	@Override
	public void addfriendagree(Map<String, String> param,
			ResponseListener responseListener) {
		MCTools.ajax(activity, "/api2/relation/addfriendagree", param, true,
				HttpTools.SEND_POST, 5000, responseListener);
	}

	

}
