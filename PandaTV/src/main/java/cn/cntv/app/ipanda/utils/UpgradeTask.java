package cn.cntv.app.ipanda.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.personal.entity.PIUpdateModel;

public class UpgradeTask extends AsyncTask<Void, Integer, Exception> {

	ProgressDialog pd;
	String apk_path;
	File file = null;
	PIUpdateModel pum;
	Context mContext;

	public UpgradeTask(Context ct, PIUpdateModel spum) {
		// TODO Auto-generated constructor stub
		pum = spum;
		mContext = ct;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pd = new ProgressDialog(mContext);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setTitle("版本升级");
		str = buildStr();
		pd.setMessage("正在升级中，请稍后" + str);
		pd.setIcon(R.drawable.share_logo_ipnda);
		pd.setMax(100);
		pd.setCancelable(false);
		pd.setIndeterminate(false);
		pd.show();

		// 非强制升级，允许手机菜单的返回键生效,1.2.0版本改为全部强制升级
		pd.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg1 == KeyEvent.KEYCODE_BACK)
					//UpgradeTask.this.cancel(true);
				System.exit(0);
				pd.dismiss();
				if (null != file)
					file.delete();
				if (pum != null) {
					if ("0".equals(pum.getVersionsUpdate())) {
						Toast.makeText(mContext, R.string.cancel_download, Toast.LENGTH_SHORT)
								.show();
					} else {
						System.exit(0);
					}
				}
				return false;
			}
		});
	}

	String str = "";

	public String buildStr() {
		final View v = ((Activity) mContext).getWindow().getDecorView();
		final Runnable r = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (pd.isShowing()) {
					if (str.length() == 3)
						str = ".";
					else
						str += ".";
					v.postDelayed(this, 1000);
					pd.setMessage("正在升级中，请稍后" + str);
					System.out.println(str);
				} else {
					v.removeCallbacks(this);
				}
			}
		};
		v.post(r);
		return str;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		Integer progress = values[0];
		pd.setProgress(progress);
	}

	@Override
	protected Exception doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		Exception me = null;
		String apk_url = pum.getVersionsUrl()/* "https://dl.wandoujia.com/files/jupiter/latest/wandoujia-wandoujia_web.apk" */;
		// String apk_url =
		// "http://www.pgyer.com/apiv1/app/install?aId=9c14dacfc89d4e056c81e3c397e4f969&_api_key=63e68489be2e8ddd77487afcff0ca3d5";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)
				&& !TextUtils.isEmpty(apk_url)) {
			try {
				apk_path = Environment.getExternalStorageDirectory()
						+ File.separator + "Android" + File.separator
						+ "Panda.apk";
				String apk_path_dir = Environment.getExternalStorageDirectory()
						+ File.separator + "Android" + File.separator;
				File fdir = new File(apk_path_dir);
				if (!fdir.exists())
					fdir.mkdirs();
				file = new File(fdir, "Panda.apk");
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				URL url = new URL(apk_url);
				System.out.println("connect    !!");
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setDoInput(true);
				conn.connect();
				long fsize = conn.getContentLength();
				int progress = 0;

				BufferedInputStream bis = new BufferedInputStream(
						conn.getInputStream());
				int count = 0;
				byte[] buffer = new byte[1024 * 4];
				int readNum = 0;
				while (!isCancelled()) {
					int cur = 0;
					while ((readNum = bis.read(buffer)) != -1) {
						count += readNum;
						fos.write(buffer, 0, readNum);
						progress = (int) ((count * 100 / fsize));
						if (/* progress <= 95 && */progress - cur >= 1) {
							cur = progress;
							publishProgress(progress);
						}
					}
					fos.close();
					;
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				me = e;
				cancel(true);
				file.delete();
				pd.dismiss();
				Toast.makeText(mContext, R.string.download_failed, Toast.LENGTH_SHORT).show();
			}
		}
		return me;
	}

	@Override
	protected void onPostExecute(Exception result) {
		// TODO Auto-generated method stub
		pd.dismiss();
		if (isCancelled() || null != result) {
			if (null != file)
				file.delete();
			return;
		}
		if (!apk_path.endsWith(".apk")) {
			throw new IllegalArgumentException("Invalidate File ...");
		}
		try {
			File file = new File(apk_path);
			if (!file.exists()) {
				throw new Exception("The file not found");
			}
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
			mContext.startActivity(intent);
		} catch (Exception e) {
		}
	}
}