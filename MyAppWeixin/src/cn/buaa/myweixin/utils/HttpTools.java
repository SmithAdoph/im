package cn.buaa.myweixin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

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
	public static byte[] sendGet(String path, int timeout,
			Map<String, String> params) {
		byte data[] = null;
		// ��������·��
		
		HttpURLConnection httpURLConnection = null;
		try {
			// ƴ���������
			if (params != null) {
				Set<String> keys = params.keySet();
				if (keys != null) {
					path += "?";
					for (String key : keys) {
						path += key + "="
								+ params.get(key)
								+ "&";
					}
					path = path.substring(0, path.length() - 2);
				}
			}
			System.out.println(path);
			URL url = new URL(path);
			// ������������
			httpURLConnection = (HttpURLConnection) url.openConnection();
			// ��������ʽ
			httpURLConnection.setRequestMethod("GET");
			// ���ó�ʱ
			httpURLConnection.setReadTimeout(timeout);
			// �жϷ�������Ӧ

			if (httpURLConnection.getResponseCode() == 200) {
				InputStream is = httpURLConnection.getInputStream();
				data = StreamTools.isToData(is);
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpURLConnection.disconnect();
		}
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
	public static byte[] sendPost(String path, int timeout,
			Map<String, String> params) {
		byte data[] = null;
		HttpURLConnection httpURLConnection = null;
		try {
			// ƴ���������
			String paramData = "";
			if (params != null) {
				Set<String> keys = params.keySet();
				if (keys != null) {
					for (String key : keys) {
						paramData += key + "=" + URLEncoder.encode(params.get(key), "UTF-8") + "&";
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
			httpURLConnection = (HttpURLConnection) url
					.openConnection();
			// �������󷽷�
			httpURLConnection.setRequestMethod("POST");
			// ��������ʱ
			httpURLConnection.setReadTimeout(timeout);
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

			// �жϷ���״̬
			if (httpURLConnection.getResponseCode() == 200) {
				InputStream is = httpURLConnection.getInputStream();
				data = StreamTools.isToData(is);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			httpURLConnection.disconnect();
		}
		httpURLConnection.disconnect();
		return data;
	}
}
