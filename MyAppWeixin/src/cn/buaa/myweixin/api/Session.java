package cn.buaa.myweixin.api;

import java.util.Map;

import cn.buaa.myweixin.listener.ResponseListener;

public interface Session {
	public void eventweb(Map<String, String> param,
			ResponseListener responseListener);
	public void event(Map<String, String> param,
			ResponseListener responseListener);
	
}
