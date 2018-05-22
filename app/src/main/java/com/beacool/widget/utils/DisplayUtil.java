package com.beacool.widget.utils;

import android.content.Context;

/**
 * Package com.beacool.myviewtest.
 * Created by JoshuaYin on 16/6/1.
 * Company Beacool IT Ltd.
 * <p>
 * Description:
 */
public class DisplayUtil {
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	public static int getMinScreenSize(Context context) {
		return Math.min(getScreenWidth(context), getScreenHeight(context));
	}

	public static int getMaxScreenSize(Context context) {
		return Math.max(getScreenWidth(context), getScreenHeight(context));
	}
}
