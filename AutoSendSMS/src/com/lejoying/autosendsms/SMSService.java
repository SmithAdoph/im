package com.lejoying.autosendsms;

import java.net.HttpURLConnection;
import java.util.LinkedList;
import java.util.Queue;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsManager;

import com.lejoying.utils.Ajax;
import com.lejoying.utils.Ajax.AjaxInterface;
import com.lejoying.utils.Ajax.Settings;
import com.lejoying.utils.HttpTools;

public class SMSService extends Service {

	public static final String ACTION = "SMSSERVICE";

	SmsManager smsManager;

	BroadcastReceiver sentReceiver;
	BroadcastReceiver deliveredReceiver;

	PendingIntent sentPI;
	PendingIntent deliverPI;

	public static boolean isStart;
	public static boolean isStartSend = true;
	boolean isSending;

	HttpURLConnection currentConnection;

	Queue<SMSEntity> mSMSQueue = new LinkedList<SMSEntity>();

	long sentCount;

	SMSEntity nowSendingEntity;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		smsManager = SmsManager.getDefault();
		String SENT_SMS_ACTION = "SENT_SMS_ACTION";
		Intent sentIntent;
		String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
		Intent deliverIntent;

		smsManager = SmsManager.getDefault();
		sentIntent = new Intent(SENT_SMS_ACTION);
		sentPI = PendingIntent.getBroadcast(this, 0, sentIntent, 0);
		sentReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					// System.out.println("RESULT_OK");
					sentCount++;
					handleOver();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					// System.out.println("RESULT_ERROR_GENERIC_FAILURE");
					handleOver();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					// System.out.println("RESULT_ERROR_RADIO_OFF");
					handleOver();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					// System.out.println("RESULT_ERROR_NULL_PDU");
					handleOver();
					break;
				}
			}
		};
		registerReceiver(sentReceiver, new IntentFilter(SENT_SMS_ACTION));

		deliverIntent = new Intent(DELIVERED_SMS_ACTION);
		deliverPI = PendingIntent.getBroadcast(this, 0, deliverIntent, 0);
		deliveredReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

			}
		};

		registerReceiver(deliveredReceiver, new IntentFilter(
				DELIVERED_SMS_ACTION));

		super.onCreate();
	}

	public void sendSMS(SMSEntity smsEntity) {
		mSMSQueue.offer(smsEntity);
		sendSMSBroadcast();
		handleSMSQueue();
	}

	public void handleSMSQueue() {
		if (!isStartSend) {
			return;
		}
		if (isSending || mSMSQueue.size() == 0) {
			return;
		}
		isSending = true;
		new Thread() {
			public void run() {
				SMSEntity smsEntity = mSMSQueue.poll();
				nowSendingEntity = smsEntity;
				sendSMSBroadcast();
				if (smsEntity.phone != null && !smsEntity.phone.equals("")
						&& smsEntity.text != null && !smsEntity.text.equals("")) {
					smsManager.sendTextMessage(smsEntity.phone, null,
							smsEntity.text, sentPI, deliverPI);
				} else {
					handleOver();
					// TODO SEND SMS IS FAILED
				}

			}
		}.start();
	}

	public void sendSMSBroadcast() {
		Intent intent = new Intent();
		intent.setAction(ACTION);
		if (nowSendingEntity != null) {
			intent.putExtra("nowSending", nowSendingEntity.phone);
		} else {
			intent.putExtra("nowSending", "(wait)");
		}
		intent.putExtra("queueCount", mSMSQueue.size());
		intent.putExtra("sentCount", sentCount);
		sendBroadcast(intent);

	}

	public void handleOver() {
		isSending = false;
		nowSendingEntity = null;
		sendSMSBroadcast();
		handleSMSQueue();
	}

	public SMSEntity generateSMSEntityFromJSON(JSONObject jSMSEntity) {
		SMSEntity smsEntity = new SMSEntity();
		try {
			smsEntity.phone = jSMSEntity.getString("phone");
			smsEntity.text = jSMSEntity.getString("text");
		} catch (JSONException e) {
		}
		return smsEntity;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			String operation = intent.getStringExtra("operation");
			if (operation != null) {
				if (operation.equals("start")) {
					if (!isStart) {
						isStart = true;
						startLongAjax();
					}
				} else if (operation.equals("stop")) {
					if (isStart) {
						isStart = false;
						stopLongAjax();
					}
				} else if (operation.equals("stopSend")) {
					isStartSend = false;
				} else if (operation.equals("startSend")) {
					if (!isStartSend) {
						isStartSend = true;
						handleSMSQueue();
					}
				}
			} else {
				sendSMSBroadcast();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	public void startLongAjax() {
		Ajax.ajax(this, new AjaxInterface() {
			@Override
			public void setParams(Settings settings) {
				settings.url = "http://115.28.51.197:8074/api2/session/event";
				settings.timeout = 30000;
				settings.method = HttpTools.SEND_POST;
			}

			@Override
			public void onSuccess(JSONObject jData) {
				if (isStart) {
					startLongAjax();
				}
				sendSMS(generateSMSEntityFromJSON(jData));
			}

			@Override
			public void failed() {
				if (isStart) {
					startLongAjax();
				}
			}

			@Override
			public void noInternet() {
			}

			@Override
			public void timeout() {
				if (isStart) {
					startLongAjax();
				}
			}

			@Override
			public void connectionCreated(HttpURLConnection httpURLConnection) {
				currentConnection = httpURLConnection;
			}

		});
	}

	public void stopLongAjax() {
		isStart = false;
		if (currentConnection != null) {
			currentConnection.disconnect();
		}
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(sentReceiver);
		unregisterReceiver(deliveredReceiver);
		super.onDestroy();
	}

}
