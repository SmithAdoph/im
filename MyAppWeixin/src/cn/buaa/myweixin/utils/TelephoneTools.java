package cn.buaa.myweixin.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

public final class TelephoneTools {
	
	/**
	 * ��ȡ�ֻ����룬�����Ӫ��δ��sim����д�������û��Լ��޸����ȡ�������
	 * @param context
	 * @return
	 */
	public static String getPhoneNumber(Context context){
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getLine1Number();
	}  
	
}  
