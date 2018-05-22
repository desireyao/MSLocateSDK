package com.beacool.widget.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.marslocate.log.SDKLogTool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Joshua Yin on 2016/2/2.
 */
public class BitmapTool {
	public static Bitmap getBmpFromAsset(Context context, String path) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;

		try {
			InputStream is = context.getAssets().open(path);
			Bitmap bmp = BitmapFactory.decodeStream(is, null, opt);
			is.close();

			return bmp;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Bitmap getBmpFromFile(final File file) {
		if (file == null) {
			return null;
		}
		SDKLogTool.LogD("BitmapTool", "getBmpFromFile--->" + file.getAbsolutePath());

		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		InputStream is = null;
		InputStream is2 = null;

		try {
			is = new FileInputStream(file);
			is2 = new FileInputStream(file);
			opt.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is, null, opt);
			int imageHeight = opt.outHeight;
			int imageWidth = opt.outWidth;
			SDKLogTool.LogE("", "imageHeight = " + imageHeight + ", " + " = imageWidth " + imageWidth);

			if (imageHeight * imageWidth >= 2000 * 2000) {
				opt.inSampleSize = 2; // 缩小为原来的1/4
				SDKLogTool.LogE("", "inSampleSize = " + opt.inSampleSize);
			}
			opt.inJustDecodeBounds = false;
			return BitmapFactory.decodeStream(is2, null, opt);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is2 != null) {
				try {
					is2.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
