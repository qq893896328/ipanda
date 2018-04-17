package cn.cntv.app.ipanda.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.utils.xinterface.IAlertDialog;

/**
 * Toast统一管理类
 * 
 * @author way
 * 
 */
public class ToastUtil {
	// Toast
	private static Toast toast;

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, CharSequence message) {
		if (null == toast) {
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, int message) {
		if (null == toast) {
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, CharSequence message) {
		if (null == toast) {
			toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, int message) {
		if (null == toast) {
			toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, CharSequence message, int duration) {
		if (null == toast) {
			toast = Toast.makeText(context, message, duration);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}
		toast.show();
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, int message, int duration) {
		if (null == toast) {
			toast = Toast.makeText(context, message, duration);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}
		toast.show();
	}

	/** Hide the toast, if any. */
	public static void hideToast() {
		if (null != toast) {
			toast.cancel();
		}
	}

	public static <P> void showPpw(View parent, LayoutInflater inflater,
			String msg, final IAlertDialog<P> interfac, String positiverText,
			String negativeText) {
		final View dialogView = inflater.inflate(R.layout.common_ppw1, null,
				false);
		// 提示信息view
		final TextView tipTv2 = (TextView) dialogView
				.findViewById(R.id.tv_tip2);
		tipTv2.setText(msg);
		// 创建弹出对话框，设置弹出对话框的背景为圆角
		final PopupWindow ppw = new PopupWindow(dialogView,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		// 响应返回键
		ppw.setFocusable(true);
		final Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);
		final Button btnCancel = (Button) dialogView
				.findViewById(R.id.btn_cancel);
		btnOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (ppw != null && ppw.isShowing()) {
					ppw.dismiss();// 关闭
				}
				interfac.positive();
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (ppw != null && ppw.isShowing()) {
					ppw.dismiss();// 关闭
				}

			}
		});

		// 显示RoundCorner对话框
		ppw.showAtLocation(parent, Gravity.TOP | Gravity.CENTER, 0, 0);
	}

}
