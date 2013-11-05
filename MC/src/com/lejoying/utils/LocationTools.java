package com.lejoying.utils;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

public class LocationTools {

	public static double[] getLocation(Activity activity) {
		LocationManager lm = (LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE);
		// ����������֪��λ���ṩ�ߵ������б�����δ��׼���ʻ���ûĿǰ��ͣ�õġ�
		//List<String> lp = lm.getAllProviders();
		// for (String item : lp) {
		// System.out.println("����λ�÷���" + item);
		// }
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(false);
		// ����λ�÷������
		criteria.setAccuracy(Criteria.ACCURACY_COARSE); // ����ˮƽλ�þ���
		// getBestProvider ֻ��������ʵ��û��λ�ù�Ӧ�̽�������
		String providerName = lm.getBestProvider(criteria, true);
		//System.out.println("------λ�÷���" + providerName);

		if (providerName != null) {
			Location location = lm.getLastKnownLocation(providerName);
			// ��ȡά����Ϣ
			double latitude = location.getLatitude();
			// ��ȡ������Ϣ
			double longitude = location.getLongitude();
			return new double[] { longitude, latitude };
		} else {
			Toast.makeText(activity, "1.������������ \n2.����ҵ�λ��",
					Toast.LENGTH_SHORT).show();
		}

		return null;
	}
}
