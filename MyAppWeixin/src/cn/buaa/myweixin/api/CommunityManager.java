package cn.buaa.myweixin.api;

import java.util.Map;

import cn.buaa.myweixin.listener.ResponseListener;

public interface CommunityManager {
	public void find(Map<String, String> param,
			ResponseListener responseListener);
}