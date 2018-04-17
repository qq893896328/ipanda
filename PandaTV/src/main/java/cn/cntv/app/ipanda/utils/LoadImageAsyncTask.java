package cn.cntv.app.ipanda.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.cntv.app.ipanda.constant.PandaEyeConstants;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;


public class LoadImageAsyncTask extends AsyncTask<String, Integer, Bitmap> {
	private LoadImageAsynTaskCallBack callback;
	private Bitmap bitmap;
	public LoadImageAsyncTask(LoadImageAsynTaskCallBack callback) {
		this.callback = callback;
	}

	public interface LoadImageAsynTaskCallBack {
		public void beforeImageLoad();
		
		public void onImageLoaded(Bitmap bitmap);
	}

	/**
	 * 下载图片之前调用
	 */
	@Override
	protected void onPreExecute() {
		callback.beforeImageLoad();
		super.onPreExecute();
	}

	
	
	
	
	/**
	 * 异步下载图片
	 */
	@Override
	protected Bitmap doInBackground(String... params) {

		try {
			String ivPath = params[0].substring(params[0].lastIndexOf("/") + 1,
					params[0].length());
			boolean b = isBitmap(PandaEyeConstants.cache, ivPath);
			if (b) {
				// 获取缓存图片
				 Log.e("gao", "获取缓存图片:"+PandaEyeConstants.cache+ File.separator + ivPath);
				bitmap = BitmapFactory.decodeFile(PandaEyeConstants.cache
						+ File.separator + ivPath);
			} else {
				// 下载图片
				String path = params[0];
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				InputStream is = conn.getInputStream();

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					bos.write(buffer, 0, len);
				}
				byte[] result = bos.toByteArray();

				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
				}

				bitmap = BitmapFactory
						.decodeByteArray(result, 0, result.length);

			}

			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 下载图片完成之后
	 */
	@Override
	protected void onPostExecute(Bitmap result) {
		callback.onImageLoaded(result);
		super.onPostExecute(result);

	}

	/**
	 * 
	 * @param iv
	 *            将要显示的图片引用
	 * @param dir
	 *            表示要遍历的文件夹为当前文件夹
	 * @param path
	 *            查看是否有图片
	 * @return
	 */
	private boolean isBitmap(String dir, String path) {
		File f = new File(dir);
		String sf[] = f.list();// 将文件夹中的文件名放入数组中
		for (String s : sf) {// 开始遍历文件夹
			if (s.equals(path)) {
				return true;
			}
		}
		return false;
	}

}
