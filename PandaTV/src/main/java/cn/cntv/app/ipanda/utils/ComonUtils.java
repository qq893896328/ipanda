package cn.cntv.app.ipanda.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

public class ComonUtils {
	/**
	 * dip转为 px
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px 转为 dip
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int[] getDisplay(Activity activity) {
		// 手机屏幕分辨率
		DisplayMetrics outMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		int[] displayMetric = new int[2];
		displayMetric[0] = outMetrics.widthPixels;
		displayMetric[1] = outMetrics.heightPixels;
		return displayMetric;
	}

	// 设置缩放比例16:9
	public static void setScale16_9(Activity activity, final View view) {
		int[] display = getDisplay(activity);
		LayoutParams layoutParams = view.getLayoutParams();
		// 小窗口的比例
		float ratio = (float) 16 / 9;
		int initHeight = (int) Math.ceil((float) display[0] / ratio);
		layoutParams.height = initHeight;
		view.setLayoutParams(layoutParams);

	}

	public static int getDisplayWidth(Activity activity) {

		DisplayMetrics outMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);

		return outMetrics.widthPixels;
	}
}
