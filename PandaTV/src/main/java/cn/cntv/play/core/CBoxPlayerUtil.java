package cn.cntv.play.core;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.Toast;

public class CBoxPlayerUtil {  
	 
	/**
	 * ��ȡϵͳ�汾
	 */
	public static int systemVersions(){
		int currentapiVersion=android.os.Build.VERSION.SDK_INT;
		return currentapiVersion;
	}
    /** 
     * ����ֻ�ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * ����ֻ�ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }
    
    /**
     *��ȡ��Ļ���ܶ� 
     */
    public static float density(Context context){
    	final float scale = context.getResources().getDisplayMetrics().density; 
    	return scale;
    }
    /**
     *��ȡ��Ļ�Ŀ�
     */
    public static int screenWidth(Context context){
    	final int width = context.getResources().getDisplayMetrics().widthPixels; 
    	return width;
    }
    /**
     *��ȡ��Ļ�ĸ�
     */
    public static int screenHeight(Context context){
    	final int height = context.getResources().getDisplayMetrics().heightPixels; 
    	return height;
    }
    public static int statusBarHeight(Context context){
     
    int mStatusBarHeight = 38;
     try {  
            /**
             * ͨ������ƻ�ȡStatusBar�߶�
             */  
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");  
            Object object = clazz.newInstance();  
            Field field = clazz.getField("status_bar_height");  
            int height = Integer.parseInt(field.get(object).toString());         
            mStatusBarHeight = context.getResources().getDimensionPixelSize(height);  
        } catch (Exception e) {  
        } 
       return mStatusBarHeight;
     }
    /**
     * ��ʾ��ʾ
     */
    public static void DisplayToast(Context context,String string) 
    { 
  	 Toast.makeText(context,string,Toast.LENGTH_SHORT).show();      
     } 
	
	/**
	 * �������״̬
	 */
	public static boolean checkNetState(Context context){
    	boolean netstate = false;
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++)
				{
					if (info[i].getState() == NetworkInfo.State.CONNECTED) 
					{
						netstate = true;
						break;
					}
				}
			}
		}
		return netstate;
    }
	/**
	 * ��ʽ������ʱ�� 
	 */
	public static String getStringTime(long position) {
		SimpleDateFormat fmPlayTime;
		if (position <= 0) {
			return "00:00";
		}

		long lCurrentPosition = position / 1000;
		long lHours = lCurrentPosition / 3600;

		if (lHours > 0){
			fmPlayTime = new SimpleDateFormat("HH:mm:ss");
		}else{
            fmPlayTime = new SimpleDateFormat("mm:ss");
		}
		fmPlayTime.setTimeZone(TimeZone.getTimeZone("GMT"));
		return fmPlayTime.format(position);
	}
	/**
	 * ��ȡ�������� 
	 */
   public static String getStringDate(){
	   SimpleDateFormat sdf = new 
			      SimpleDateFormat("yyyy-MM-dd");// ʵ��ģ�����  
	   Date myDate = new Date();
	   return sdf.format(myDate);
   }
}