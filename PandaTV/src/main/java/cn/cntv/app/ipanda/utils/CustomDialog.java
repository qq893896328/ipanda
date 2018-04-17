package cn.cntv.app.ipanda.utils;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.utils.xinterface.DialogListener;

/**
 * @ClassName: CustomDialog
 * @author Xiao JinLai
 * @Date Jan 26, 2016 11:51:47 AM
 * @Description：自定义Dialog
 */
public class CustomDialog {

	public static void showDialogView(View parentView,
			DialogListener dialogListener) {

		showDialogView(parentView, null, null, null, dialogListener);
	}

	public static void showDialogView(View parentView, String title,
			String confirmText, String cancelText,
			final DialogListener dialogListener) {

		View tDialogView = LayoutInflater.from(parentView.getContext())
				.inflate(R.layout.custom_dialog, null);

		// 提示信息view
		final TextView tTvTitle = (TextView) tDialogView
				.findViewById(R.id.tvCusDialogTitle);

		if (title != null && !title.equals("")) {

			tTvTitle.setText(title);
		}

		// 创建弹出对话框，设置弹出对话框的背景为圆角
		final PopupWindow tPopupWindow = new PopupWindow(tDialogView,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);

		// 响应返回键
		tPopupWindow.setFocusable(true);

		Button tBtnConfirm = (Button) tDialogView
				.findViewById(R.id.btnCusDialogConfirm);

		if (confirmText != null && !confirmText.equals("")) {

			tBtnConfirm.setText(confirmText);
		}

		tBtnConfirm.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (tPopupWindow != null && tPopupWindow.isShowing()) {

					tPopupWindow.dismiss();// 确认
					dialogListener.confirm();
				}
			}
		});

		Button tBtnCancel = (Button) tDialogView
				.findViewById(R.id.btnCusDialogCancle);

		if (cancelText != null && !cancelText.equals("")) {

			tBtnCancel.setText(cancelText);
		}

		tBtnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (tPopupWindow != null && tPopupWindow.isShowing()) {

					tPopupWindow.dismiss();// 关闭
				}
			}
		});

		// 显示 RoundCorner 对话框
		tPopupWindow.showAtLocation(parentView, Gravity.TOP | Gravity.CENTER,
				0, 0);
	}
}
