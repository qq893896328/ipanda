package cn.cntv.app.ipanda.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.widget.Toast;

import cn.cntv.app.ipanda.R;

public class PEBaseFragment extends Fragment {
	private ProgressDialog progressDialog;
	private Dialog loadDialog;
	private int dialogNum;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 得到屏幕宽度
	 * 
	 * @return 宽度
	 */
	public int getScreenWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		return screenWidth;
	}

	/*
	 * 获取应用版本号
	 * 
	 * @author GaoMing
	 * 
	 * @return 返回版本号
	 */
	public String getVersion() {
		try {
			PackageInfo info = getActivity().getPackageManager()
					.getPackageInfo(getActivity().getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 得到屏幕高度
	 * 
	 * @return 高度
	 */
	public int getScreenHeight() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenHeight = dm.heightPixels;
		return screenHeight;
	}

	/**
	 * 是否全屏和显示标题，true为全屏和无标题，false为无标题，请在setContentView()方法前调用
	 * 
	 * @param fullScreen
	 */
	public void setFullScreen(boolean fullScreen) {
		if (fullScreen) {
			getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
			getActivity().getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
		}

	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param info
	 *            显示的内容
	 */
	public void showToast(String info) {
		Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param info
	 *            显示的内容
	 */
	public void showToastLong(String info) {
		Toast.makeText(getActivity(), info, Toast.LENGTH_LONG).show();
	}

	/**
	 * 短时间显示Toast
	 * 
	 *            显示的内容
	 */
	public void showToast(int resId) {
		Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 长时间显示Toast
	 * 
	 *            显示的内容
	 */
	public void showToastLong(int resId) {
		Toast.makeText(getActivity(), resId, Toast.LENGTH_LONG).show();
	}

	/**
	 * 判断手机是否有网络
	 * 
	 * @return true 有网络
	 */
	public boolean isConnected() {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) getActivity()
					.getSystemService(getActivity().CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();

				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 显示正在加载的进度条
	 * 
	 */
	public void showLoadingDialog() {
		dialogNum++;
		if (loadDialog != null && loadDialog.isShowing()) {
			loadDialog.dismiss();
			loadDialog = null;
		}
		loadDialog = new Dialog(getActivity(), R.style.dialog);
		loadDialog.setCanceledOnTouchOutside(false);

		loadDialog.setContentView(R.layout.layout_dialog);
		try {
			loadDialog.show();
		} catch (BadTokenException exception) {
			exception.printStackTrace();
		}
	}

	// public void showProgressDialog() {
	// if (progressDialog != null && progressDialog.isShowing()) {
	// progressDialog.dismiss();
	// progressDialog = null;
	// }
	// progressDialog = new ProgressDialog(getActivity(), R.style.dialog);
	// progressDialog.setCanceledOnTouchOutside(false);
	//
	// progressDialog.setView(LayoutInflater.from(getActivity()).inflate(
	// R.layout.layout_dialog, null));
	// try {
	// progressDialog.show();
	// } catch (BadTokenException exception) {
	// exception.printStackTrace();
	// }
	// }

	public void showProgressDialog(String msg) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage(msg);
		try {
			progressDialog.show();
		} catch (BadTokenException exception) {
			exception.printStackTrace();
		}
	}


	public ProgressDialog createProgressDialog(String msg) {
		ProgressDialog progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage(msg);
		progressDialog.setCanceledOnTouchOutside(false);
		return progressDialog;
	}

	/**
	 * 隐藏正在加载的进度条
	 * 
	 */
	public void dismissLoadDialog() {
		dialogNum--;
		if (dialogNum > 0) {
			return;
		}
		if (null != loadDialog && loadDialog.isShowing() == true) {
			loadDialog.dismiss();
			loadDialog = null;
		}
	}

	// public void dismissProgressDialog() {
	// dialogNum--;
	// if (dialogNum > 0) {
	// return;
	// }
	// if (null != progressDialog && progressDialog.isShowing() == true) {
	// progressDialog.dismiss();
	// progressDialog = null;
	// }
	// }

	/**
	 * 获取控件的宽高
	 * 
	 * @param view
	 * @return
	 */
	public int[] getWigetWidthHeight(View view) {
		int[] array = new int[2];
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		int width = view.getMeasuredWidth();
		int height = view.getMeasuredHeight();
		array[0] = width;
		array[1] = height;
		return array;
	}

//	public void showShareContent(String title, String shareurl, String content,
//			String imgUrl) {
//		ShareSDK.initSDK(getActivity());
//		OnekeyShare oks = new OnekeyShare();
//		oks.addHiddenPlatform(QZone.NAME);
//		// oks.setNotification(R.drawable.ic_launcher,
//		// getString(R.string.app_name));
//		// 关闭sso授权
//		oks.disableSSOWhenAuthorize();
//
//		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
//		// oks.setNotification(R.drawable.ic_launcher,
//		// getString(R.string.app_name));
//		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//		oks.setTitle(title);
//		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//		oks.setTitleUrl(shareurl);
//		// text是分享文本，所有平台都需要这个字段
//		oks.setText(content);
//
//		Log.e("grow", "imgUrl...=" + imgUrl);
//
//		// if(imgUrl != null){
//		// oks.setImageUrl(imgUrl);
//		// }
//
//		oks.setVenueDescription("This is a beautiful place!");
//		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//		// url仅在微信（包括好友和朋友圈）中使用
//		oks.setUrl(shareurl);
//		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
//		// oks.setComment("我是测试评论文本");
//		// site是分享此内容的网站名称，仅在QQ空间使用
//		oks.setSite("成长之路");
//		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
//		oks.setSiteUrl(shareurl);
//
//		// 启动分享GUI
//		oks.show(getActivity());
//	}
//
//	


	/**
	 * 跳转到登录界面：
	 */
	// public void turnLogin() {
	// Intent it = new Intent(softApplication, LoginActivity.class);
	// startActivity(it);
	// }
}

