package cn.buaa.myweixin.utils;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.Color;

public class ImageTools {
	
	/**
	 * ����Բ��ͼƬ,source��Ϊ������ͼƬ
	 * @param source
	 * @return
	 */
	public static Bitmap getCircleBitmap(Bitmap source){
		return getCircleBitmap(source, null);
	}
	/**
	 * ����ֱ��Ϊdiameter�����ص�Բ��ͼƬ,source��Ϊ������ͼƬ
	 * @param source
	 * @param diameter
	 * @return
	 */
	public static Bitmap getCircleBitmap(Bitmap source,Integer diameter ) {
		// �����µ�bitmap
		Bitmap bitmap = Bitmap.createBitmap(source.getWidth(),
				source.getHeight(), Config.ARGB_8888);
		// ��ȡ�뾶
		int radius = 0;
		if(diameter==null||diameter==0){
			radius = source.getWidth() / 2;
		}else{
			radius = diameter/2;
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
}
