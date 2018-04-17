package cn.cntv.app.ipanda.ui.home.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.net.ConnectivityManager;
import android.widget.BaseAdapter;
import android.widget.Toast;

/**
 * 数据适配器基础类
 * 
 * @Description
 * @author Cheng Yong
 * @version 1.0 2012-7-15
 * @class MyBaseAdapter
 */
public abstract class MyBaseAdapter extends BaseAdapter {

	protected Activity mActivity;
	protected Context mContext;

	protected MyBaseAdapter(Context context) {


	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @Description
	 * @return
	 * @author Cheng Yong
	 * @version 1.0 2012-7-25
	 */
	protected boolean checkNetWork() {
		boolean newWorkOK = false;
		ConnectivityManager connectManager = (ConnectivityManager) mActivity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectManager.getActiveNetworkInfo() != null) {
			newWorkOK = true;
		}
		return newWorkOK;
	}

	/**
	 * 长提示
	 * 
	 * @Description
	 * @param resId
	 *            文本资源ID
	 * @author Cheng Yong
	 * @version 1.0 2012-7-19
	 */
	protected void toastLong(int resId) {
		Toast.makeText(mActivity, mActivity.getResources().getString(resId),
				Toast.LENGTH_LONG).show();
	}

	/**
	 * 创建进度条
	 * 
	 * @Description
	 * @param resId
	 * @return
	 * @author Cheng Yong
	 * @version 1.0 2012-7-19
	 */
	public ProgressDialog createProgress(int resId) {
		ProgressDialog dialog = new ProgressDialog(mActivity);
		dialog.setMessage(mActivity.getResources().getString(resId));
		dialog.setIndeterminate(false);
		dialog.setCancelable(true);
		return dialog;
	}

	/**
	 * 长提示
	 * 
	 * @Description
	 * @param msg
	 * @author Cheng Yong
	 * @version 1.0 2012-7-18
	 */
	protected void toastLong(String msg) {
		Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
	}

}
