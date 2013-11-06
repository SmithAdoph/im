package com.lejoying.mcutils;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.lejoying.utils.StreamTools;

public final class ImageTools {

	/**
	 * ����Բ��ͼƬ,source����Ϊ������ͼƬ
	 * 
	 * @param source
	 * @return
	 */
	public static Bitmap getCircleBitmap(Bitmap source) {
		return getCircleBitmap(source, false, null, null);
	}

	/**
	 * ���ش�1px��ɫΪborderColor�ı߿��Բ��ͼƬ��source����Ϊ������ͼƬ
	 * 
	 * @param source
	 * @param showBorder
	 * @param borderColor
	 * @return
	 */
	public static Bitmap getCircleBitmap(Bitmap source, boolean showBorder,
			Integer borderWidth, Integer borderColor) {
		int sourceWidth = source.getWidth();
		int sourceHeight = source.getHeight();

		if (!showBorder || borderWidth == null) {
			borderWidth = 0;
		}
		// �����µ�bitmap
		Bitmap bitmap = Bitmap.createBitmap(sourceWidth + borderWidth * 2,
				sourceHeight + borderWidth * 2, Config.ARGB_8888);

		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();

		// ��ȡԭͼƬ�뾶
		int radius = 0;
		radius = source.getWidth() / 2;

		// ��ȡ����ͼƬ�뾶
		int newRandius = 0;
		newRandius = bitmap.getWidth() / 2;

		for (int i = 0; i < bitmapWidth; i++) {
			for (int j = 0; j < bitmapHeight; j++) {
				int newColor = 0;
				if (i < sourceWidth && j < sourceHeight) {
					int color = source.getPixel(i, j);
					if (Math.hypot(radius - i, radius - j) > radius) {
						newColor = Color.argb(0, 0, 0, 0);

					} else {
						newColor = Color.argb(255, Color.red(color),
								Color.green(color), Color.blue(color));
					}
					bitmap.setPixel(i + borderWidth, j + borderWidth, newColor);
				}
				if (showBorder) {
					double sqrt = Math.hypot(newRandius - i, newRandius - j);
					if (sqrt <= newRandius && sqrt > radius) {
						if (borderColor == null) {
							newColor = Color.rgb(0, 0, 0);
						} else {
							newColor = borderColor;
						}
						bitmap.setPixel(i, j, newColor);
					}
				}
			}
		}
		return bitmap;
	}

	/**
	 * ����ͼƬ������ͼƬ���С�ڵ���maxWidth,�߶�С�ڵ���maxHeight.
	 * ���maxWidth����ԭͼƬ��Ȳ���maxHeight����ԭͼƬ�߶��򷵻�ԭͼƬ
	 * 
	 * @param is
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static Bitmap getZoomBitmapFromStream(InputStream is,
			Integer maxWidth, Integer maxHeight) {
		byte[] data = null;
	    data = StreamTools.isToData(is);
		// �õ�options����
		BitmapFactory.Options boptions = new BitmapFactory.Options();
		// ֻ����ͼƬ�߿�
		boptions.inJustDecodeBounds = true;
		// ������
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
				boptions);
		// �õ�ԭͼƬ�Ŀ��
		int width = boptions.outWidth;
		int height = boptions.outHeight;

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
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
					boptions);
		}

		return bitmap;
	}
}
