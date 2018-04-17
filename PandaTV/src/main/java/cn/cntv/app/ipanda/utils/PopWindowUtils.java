package cn.cntv.app.ipanda.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.cntv.app.ipanda.R;

public class PopWindowUtils {
	/**
	 * PopWindow实例
	 */
	private static PopWindowUtils mPopWindowUtils;

	private static PopupWindow mPopWindow;

	private Handler mHandler = new Handler();

	/**
	 * 获取PopWindowUtils单例
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static PopWindowUtils getInstance() {
		if (mPopWindowUtils == null) {
			mPopWindowUtils = new PopWindowUtils();
		}
		return mPopWindowUtils;
	}

	/**
	 * 销毁PopWindowUtils
	 */
	public static void destroyPopWindowUtils() {
		if (mPopWindowUtils != null && mPopWindow != null) {
			mPopWindow = null;
			mPopWindowUtils = null;
		}
	}

	/**
	 * 关闭提示对话框
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public static void hidePopWindow() {
		if (mPopWindow != null && mPopWindow.isShowing()) {
			mPopWindow.dismiss();
		}
	}

	public static void showPopWindow() {

	}

	/**
	 * 居中显示弹出框
	 */
	public void showPopWindowCenter(Context context, String cueText) {

		showPopWindowCenter(context, ((Activity) context).getWindow()
				.getDecorView(), R.layout.ppw_define_cue_center, cueText, true,
				2000);
	}

	/**
	 * 居中显示弹出框
	 * 
	 * @param context
	 *            上下文
	 * @param parent
	 *            依附的view对象
	 * @param layoutId
	 *            自定义的布局Id
	 * @param cueText
	 *            提示文字
	 * @param isAutoHide
	 *            是否自动隐藏
	 * @param showTime
	 *            显示时间,单位：毫秒
	 */
	public void showPopWindowCenter(Context context, View parent, int layoutId,
			String cueText, boolean isAutoHide, final int showTime) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final View popView = inflater.inflate(layoutId, null, false);

		mPopWindow = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);

		mPopWindow.setFocusable(true);
		mPopWindow.setBackgroundDrawable(new BitmapDrawable());

		TextView cueTv = (TextView) popView.findViewById(R.id.cue);
		cueTv.setText(cueText);

		// 设置显示和消失动画
		mPopWindow.setAnimationStyle(R.style.popupAnimationJustAlpha);
		// 显示对话框
		mPopWindow.showAtLocation(parent, Gravity.TOP | Gravity.CENTER, 0, 0);

		if (isAutoHide) {
			// 如果是自动隐藏
			mHandler.postDelayed(runnable, showTime);
		}

	}



	private static final Runnable runnable = new Runnable() {

		@Override
		public void run() {
			hidePopWindow();
		}
	};
}
