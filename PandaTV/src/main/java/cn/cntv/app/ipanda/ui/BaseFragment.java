package cn.cntv.app.ipanda.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager.BadTokenException;
import android.widget.Toast;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.di.HasComponent;

public class BaseFragment extends Fragment {
    
    private ProgressDialog progressDialog;
	private Dialog loadDialog;
	private int dialogNum;
    
    private OnFragmentInteractionListener mListener;

    public BaseFragment() {
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
	// progressDialog = new ProgressDialog(BaseActivity.this, R.style.dialog);
	// progressDialog.setCanceledOnTouchOutside(false);
	//
	// progressDialog.setView(LayoutInflater.from(BaseActivity.this).inflate(
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
    
    
    
    
    
    
    
    
    
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

	protected <C> C getComponent(Class<C> componentType) {
		return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
	}

}
