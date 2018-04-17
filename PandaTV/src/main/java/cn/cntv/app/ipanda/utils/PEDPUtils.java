package cn.cntv.app.ipanda.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 本类为此工程工具类
 * 
 * @author gaoming
 * 
 */
public class PEDPUtils {

	private final static boolean flag = true;

	/**
	 * px转换为dp
	 * 
	 * @param context
	 *            上下文
	 * @param pxValue
	 *            传入的px
	 * @return px对应的dp
	 */
	public static int px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().densityDpi;
		return (int) ((pxValue * 160) / scale + 0.5f);
	}

	/**
	 * dp转换为px
	 * 
	 * @param context
	 *            上下文
	 * @param dipValue
	 *            传入的dp
	 * @return dp对应的px
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().densityDpi;
		return (int) (dipValue * (scale / 160) + 0.5f);
	}

	private static Toast toast2;
	private static TextView view;

	/**
	 * 显示土司
	 * 
	 * @param context
	 *            上下文环境
	 * @param id
	 *            资源ID
	 */
	public static void showToast(Context context, int id) {
//		Toast.makeText(context, id, 0).show();
		if(toast2==null){
			toast2 = new Toast(context);
			view = new TextView(context);
			view.setText(id);
			view.setBackgroundDrawable(context.getResources().getDrawable(android.R.drawable.toast_frame));
			toast2.setView(view);
		}else {
			view.setText(id);
			view.setBackgroundDrawable(context.getResources().getDrawable(android.R.drawable.toast_frame));
			toast2.setView(view);
		}
		toast2.show();
		
	}
	
	/**
	 * 输入流对象转换为String
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static String getBytes(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
		}
		is.close();
		return new String(baos.toByteArray());
	}

	/**
	 * 统一管理LogCat
	 * 
	 * @param tag
	 *            标记
	 * @param msg
	 *            输出信息
	 */
	public static void printLogE(String tag, String msg) {
		if (flag) {
			Log.e(tag, msg);
		}
	}

	public static void printLogD(String tag, String msg) {
		if (flag) {
			Log.d(tag, msg);
		}
	}

	public static void printLogI(String tag, String msg) {
		if (flag) {
			Log.i(tag, msg);
		}
	}

	public static void printLogV(String tag, String msg) {
		if (flag) {
			Log.v(tag, msg);
		}
	}

	public static void printLogW(String tag, String msg) {
		if (flag) {
			Log.w(tag, msg);
		}
	}

	/**
	 * @param dir
	 *            缓存文件存放目录getCacheDir().getPath();
	 * @param address
	 *            图片地址
	 * @param i
	 *            图片引用地址
	 */
	public static void getLoadImage(final String dir, final String address,
			final ImageView i) {
		LoadImageAsyncTask asyncTask = new LoadImageAsyncTask(
				new LoadImageAsyncTask.LoadImageAsynTaskCallBack() {

					@Override
					public void onImageLoaded(Bitmap bitmap) {
						if (bitmap != null) {
							i.setImageBitmap(bitmap);
//							dao = new NewsOperationDao(context);
							
//							Log.e("size", ""+bitmap.getWidth());
//							Log.e("size", ""+bitmap.getHeight());
							//添加整个工程的bitmap到集合中，退出应用时候释放掉
							//App.addBitmap(bitmap);
							String ivPath = address.substring(
									address.lastIndexOf("/") + 1,
									address.length());
//							dao.addImageWH(ivPath, bitmap.getHeight()+"", bitmap.getWidth()+"");
							// 缓存获取到的图片
							File file = null;
							try {
								if (!isBitmap(dir, ivPath)) {
									// System.out.println("对图片进行缓存");
									file = new File(dir, ivPath);
									FileOutputStream fos = new FileOutputStream(
											file);
									bitmap.compress(CompressFormat.PNG, 100,
											fos);
									fos.flush();
									fos.close();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
//							return bitmap.getWidth()+"-"+bitmap.getHeight();
						} else {
							i.setImageBitmap(BitmapFactory.decodeFile(dir
									+ File.separator
									+ address.substring(
											address.lastIndexOf("/") + 1,
											address.length())));
						}
					}

		 			@Override
					public void beforeImageLoad() {
						// showToast(Context context, "下载图片失败");
					}
				});
		asyncTask.execute(address);
	}
	public static void makeRootDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {

		}
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
	public static boolean isBitmap(String dir, String path) {
		File f = new File(dir);
		String sf[] = f.list();// 将文件夹中的文件名放入数组中
		for (String s : sf) {// 开始遍历文件夹
			if (s.equals(path)) {
				return true;
			}
		}
		return false;
	}

	public static int getStatusHeight(Activity activity) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = activity.getResources()
						.getDimensionPixelSize(i5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}
	
	
	
	
//	/**
//	 * 获取推送开关状态
//	 * @param context
//	 * @return
//	 */
//	public static String getPushSwitch(Context context,String key){
//		SharedPreferences sp = context.getSharedPreferences(Interfaces.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//		return sp.getString(key, Interfaces.SWITCH_ON);
//	}
//
//	/**
//	 * 设置推送开关状态
//	 * @param context
//	 * @param pushSwitch  Constant.PUSH_SWITCH_ON or Constant.PUSH_SWITCH_OFF
//	 */
//	public static void setPushSwitch(Context context,String key,String value){
//		SharedPreferences sp = context.getSharedPreferences(Interfaces.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//		Editor editor =  sp.edit();
//		editor.putString(key, value);
//		editor.commit();
//	}
}
