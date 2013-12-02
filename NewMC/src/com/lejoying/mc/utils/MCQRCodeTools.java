package com.lejoying.mc.utils;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class MCQRCodeTools {

	private final static int QRCODE_SIZE = 300;
	private static BitMatrix mBitMatrix;

	public static Bitmap createBitmap(String content) {
		// ��������QR��ά�����
		Hashtable<EncodeHintType, Object> qrParam = new Hashtable<EncodeHintType, Object>();
		// ����QR��ά��ľ����𡪡�����ѡ�����H����
		qrParam.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// ���ñ��뷽ʽ
		qrParam.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		// ����QR��ά�����ݡ�������ֻ�ǵõ�һ����true��false��ɵ�����
		// ����˳��ֱ�Ϊ���������ݣ��������ͣ�����ͼƬ��ȣ�����ͼƬ�߶ȣ����ò���
		try {
			if (mBitMatrix == null) {
				mBitMatrix = new MultiFormatWriter().encode(content,
						BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE,
						qrParam);
			}
			// ��ʼ���ö�ά�����ݴ���BitmapͼƬ���ֱ���Ϊ�ڰ���ɫ
			int w = mBitMatrix.getWidth();
			int h = mBitMatrix.getHeight();
			int[] data = new int[w * h];

			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					if (mBitMatrix.get(x, y))
						data[y * w + x] = 0xff000000;// ��ɫ
					else
						data[y * w + x] = -1;// -1 �൱��0xffffffff ��ɫ
				}
			}
			// ����һ��bitmapͼƬ��������ߵ�Ч����ʾ
			Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			// ������Ķ�ά����ɫ���鴫�룬����ͼƬ��ɫ
			bitmap.setPixels(data, 0, w, 0, 0, w, h);
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void createQRCodeBitmapWithPortrait(Bitmap qr, Bitmap portrait) {
		// ͷ��ͼƬ�Ĵ�С
		int portrait_W = portrait.getWidth();
		int portrait_H = portrait.getHeight();

		// ����ͷ��Ҫ��ʾ��λ�ã���������ʾ
		int left = (QRCODE_SIZE - portrait_W) / 2;
		int top = (QRCODE_SIZE - portrait_H) / 2;
		int right = left + portrait_W;
		int bottom = top + portrait_H;
		Rect rect1 = new Rect(left, top, right, bottom);

		// ȡ��qr��ά��ͼƬ�ϵĻ��ʣ���Ҫ�ڶ�ά��ͼƬ�ϻ������ǵ�ͷ��
		Canvas canvas = new Canvas(qr);

		// ��������Ҫ���Ƶķ�Χ��С��Ҳ����ͷ��Ĵ�С��Χ
		Rect rect2 = new Rect(0, 0, portrait_W, portrait_H);
		// ��ʼ����
		canvas.drawBitmap(portrait, rect2, rect1, null);
	}

}
