package cn.buaa.myweixin.utils;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class ImageTools {

	/**
	 * ����Բ��ͼƬ,source��Ϊ������ͼƬ
	 * 
	 * @param source
	 * @return
	 */
	public static Bitmap getCircleBitmap(Bitmap source) {
		return getCircleBitmap(source, null);
	}

	/**
	 * ����ֱ��Ϊdiameter�����ص�Բ��ͼƬ,source��Ϊ������ͼƬ
	 * 
	 * @param source
	 * @param diameter
	 * @return
	 */
	public static Bitmap getCircleBitmap(Bitmap source, Integer diameter) {
		// �����µ�bitmap
		Bitmap bitmap = Bitmap.createBitmap(source.getWidth(),
				source.getHeight(), Config.ARGB_8888);
		// ��ȡ�뾶
		int radius = 0;
		if (diameter == null || diameter == 0) {
			radius = source.getWidth() / 2;
		} else {
			radius = diameter / 2;
		}

		for (int i = 0; i < source.getWidth(); i++) {
			for (int j = 0; j < source.getHeight(); j++) {

				int color = source.getPixel(i, j);
				int newColor;
				if ((radius - i) * (radius - i) + (radius - j) * (radius - j) > radius
						* radius)
					newColor = Color.argb(0, Color.red(color),
							Color.green(color), Color.blue(color));
				else
					newColor = Color.argb(255, Color.red(color),
							Color.green(color), Color.blue(color));
				synchronized (bitmap) {
					bitmap.setPixel(i, j, newColor);
				}
			}
		}

		return bitmap;
	}

	/**
	 * ����ͼƬ������ͼƬ���С�ڵ���maxWidth,�߶�С�ڵ���maxHeight.���maxWidth����ԭͼƬ��Ȳ���maxHeight����ԭͼƬ�߶��򷵻�ԭͼƬ
	 * @param is
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static Bitmap getZoomBitmapFromStream(InputStream is,
			Integer maxWidth, Integer maxHeight) {
		byte[] data = null;
		try {
			data = StreamTools.isToData(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// �õ�options����
		BitmapFactory.Options boptions = new BitmapFactory.Options();
		// ֻ����ͼƬ�߿�
		boptions.inJustDecodeBounds = true;
		// ������
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, boptions);
		// �õ�ԭͼƬ�Ŀ��
		int width = boptions.outWidth;
		int height = boptions.outHeight;
		System.out.println(maxWidth+".."+maxHeight+".."+width+".."+height);
		if (maxWidth == null || maxWidth == 0) {
			maxWidth = width;
		}
		if (maxHeight == null || maxWidth == 0) {
			maxHeight = height;
		}
		// ��ʼ�����ű���Ϊ1
		int scale = 1;
		// ���Ҫ���ŵ�����ߴ���ԭͼƬ����ֱ�ӷ���ԭͼƬ
		if (maxWidth > width && maxHeight > height) {
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		}
		// ���Ҫ���ŵ������С��ԭͼƬ�����������
		if (maxWidth <= width && maxHeight <= height) {
			while (boptions.outHeight / scale / 2 > maxHeight
					|| boptions.outWidth / scale / 2 > maxWidth) {
				scale *= 2;
			}

			boptions.inJustDecodeBounds = false;
			boptions.inSampleSize = scale;
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,boptions );
		}
		
		return bitmap;
	}
}
