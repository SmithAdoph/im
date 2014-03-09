package com.lejoying.wxgs.handler;

import java.net.HttpURLConnection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.lejoying.wxgs.app.MainApplication;
import com.lejoying.wxgs.utils.HttpUtils;
import com.lejoying.wxgs.utils.HttpUtils.Callback;

public class NetworkHandler {

	MainApplication app;

	public static final int WORKTHREADCOUNT_MIN = 1;
	public static final int WORKTHREADCOUNT_MAX = 10;

	public int mWorkThreadCount;

	Queue<NetConnection> mNetConnections;

	Map<String, HttpURLConnection> mConnections;

	public NetworkHandler() {

	}

	public void initialize(MainApplication app, int connectionCount) {
		this.app = app;
		mNetConnections = new LinkedList<NetConnection>();
		mConnections = new Hashtable<String, HttpURLConnection>();
		if (connectionCount > WORKTHREADCOUNT_MAX) {
			mWorkThreadCount = WORKTHREADCOUNT_MAX;
		} else if (connectionCount < WORKTHREADCOUNT_MIN) {
			mWorkThreadCount = WORKTHREADCOUNT_MIN;
		} else {
			mWorkThreadCount = connectionCount;
		}
		for (int i = 0; i < mWorkThreadCount; i++) {
			new NetworkHandlerWorkThread(i).start();
		}
	}

	public void connection(NetConnection netConnection) {
		mNetConnections.offer(netConnection);
		synchronized (this) {
			notify();
		}
	}

	public void disConnection(String url) {
		if (url != null && !url.equals("")) {
			HttpURLConnection connection = mConnections.get(url);
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public void disConnection(NetConnection netConnection) {
		if (netConnection != null) {
			disConnection(netConnection.settings.url);
		}
	}

	public NetConnection getExclude() throws InterruptedException {
		if (mNetConnections.size() == 0) {
			synchronized (this) {
				wait();
			}
		}
		return mNetConnections.poll();
	}

	public static class Settings {
		public String url;
		public Map<String, String> params;
		public int timeout = 5000;
		public int method = HttpUtils.SEND_POST;
		public boolean disConnectionSameUrl = true;
	}

	public static abstract class NetConnection implements Callback, Runnable {

		Settings settings = new Settings();

		public abstract void settings(Settings settings);

		@Override
		public void connectionCreated(HttpURLConnection httpURLConnection) {
			// TODO Auto-generated method stub

		}

		@Override
		public void timeout() {
			// TODO Auto-generated method stub

		}

		@Override
		public void error() {
			// TODO Auto-generated method stub

		}

		@Override
		public final void run() {
		}
	}

	class NetworkHandlerWorkThread extends Thread {

		public int id;

		boolean interrupt;

		public NetworkHandlerWorkThread(int id) {
			this.id = id;
		}

		@Override
		public void run() {
			while (!interrupt) {
				NetConnection netConnection = null;
				try {
					while ((netConnection = getExclude()) == null)
						;
				} catch (InterruptedException e) {
					return;
				}
				netConnection.settings(netConnection.settings);
				if (netConnection.settings.url != null
						&& !netConnection.settings.url.equals("")) {
					if (netConnection.settings.disConnectionSameUrl) {
						HttpURLConnection connection = mConnections
								.get(netConnection.settings.url);
						if (connection != null) {
							connection.disconnect();
						}
					}
					HttpUtils.connection(netConnection.settings.url,
							netConnection.settings.method,
							netConnection.settings.timeout,
							netConnection.settings.params, netConnection);
				}
			}
		}
	}

}
