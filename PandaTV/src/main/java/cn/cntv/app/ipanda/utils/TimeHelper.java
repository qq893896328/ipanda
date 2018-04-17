package cn.cntv.app.ipanda.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;

public class TimeHelper {
	/**
	 * 返回格式化的当前时间
	 * 
	 * @return 返回的时间格式为 2015-03-22
	 */
	public static String getCurrentData() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(getCurrentMillisTime());
		return format.format(date);
	}
	
	/**
	 * 返回当前毫秒数
	 * 
	 * @return
	 */
	public static long getCurrentMillisTime() {
		Calendar calendar = Calendar.getInstance();
		long timeInMillis = calendar.getTimeInMillis();
		return timeInMillis;
	}
}
