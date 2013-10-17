package cn.buaa.myweixin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class HttpTools {

	public static final int SEND_GET = 0xff01;
	public static final int SEND_POST = 0xff02;

	/**
	 * �ж������Ƿ����,����trueʱ�������
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return true;
		}
		return false;
	}

	/**
	 * ��get��������params����ַΪpath�ķ������������ط�������Ӧ��byte[]
	 * 
	 * @param path
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static byte[] sendGet(String path, Map<String, String> params)
			throws IOException {

		// ƴ���������
		if (params != null) {
			Set<String> keys = params.keySet();
			if (keys != null) {
				path += "?";
				for (String key : keys) {
					path += key + "="
							+ URLEncoder.encode(params.get(key), "UTF-8") + "&";
				}
				path = path.substring(0, path.length() - 2);
			}
		}
		// ��������·��
		URL url = new URL(path);
		// ������������
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		// ��������ʽ
		httpURLConnection.setRequestMethod("GET");
		// ���ó�ʱ
		httpURLConnection.setConnectTimeout(5000);
		// �жϷ�������Ӧ
		byte data[] = null;
		if (httpURLConnection.getResponseCode() == 200) {
			InputStream is = httpURLConnection.getInputStream();
			data = StreamTools.isToData(is);
		}
		httpURLConnection.disconnect();
		return data;
	}

	/**
	 * ��post��������params����ַΪpath�ķ������������ط�������Ӧ��byte[]
	 * 
	 * @param path
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static byte[] sendPost(String path, Map<String, String> params)
			throws IOException {
		// ƴ���������
		String paramData = "";
		if (params != null) {
			Set<String> keys = params.keySet();
			if (keys != null) {
				for (String key : keys) {
					paramData += key + "=" + params.get(key) + "&";
				}
				paramData = paramData.substring(0, paramData.length() - 1);
			}
		}
		// �����������Ϊ��
		if (paramData.length() == 0) {
			throw new NullPointerException("�������Ϊ��");
		}
		// ��������·��
		URL url = new URL(path);
		// ������������
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		// �������󷽷�
		httpURLConnection.setRequestMethod("POST");
		// ��������ʱ
		httpURLConnection.setConnectTimeout(5000);
		httpURLConnection.setDoOutput(true);
		// ����Content-Type
		httpURLConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		// ����Content-Length
		httpURLConnection.setRequestProperty("Content-Length",
				paramData.length() + "");
		OutputStream os = httpURLConnection.getOutputStream();
		byte buffer[] = paramData.getBytes();
		os.write(buffer);
		os.flush();
		os.close();
		byte data[] = null;
		// �жϷ���״̬
		if (httpURLConnection.getResponseCode() == 200) {
			InputStream is = httpURLConnection.getInputStream();
			data = StreamTools.isToData(is);
		}
		httpURLConnection.disconnect();
		return data;
	}

	/**
	 * ʹ��method������params�������͵���ַΪpath�ķ�����������JSONObject����
	 * 
	 * @param path
	 * @param params
	 * @param method
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONObject sendForJSONObject(String path,
			Map<String, String> params, int method) throws IOException,
			JSONException {
		JSONObject jsonObject = null;
		if (method == SEND_GET) {
			byte[] data = sendGet(path, params);
			if (data != null)
				jsonObject = new JSONObject(new String(data));
		}
		if (method == SEND_POST) {
			byte[] data = sendPost(path, params);
			if (data != null)
				jsonObject = new JSONObject(new String(data));
		}
		return jsonObject;
	}

}
