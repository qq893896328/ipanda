package cn.cntv.app.ipanda.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import u.aly.da;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

/**
 * 时间工具类
 *
 * @author way
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
        return format.format(new Date(time));
    }

    public static String getTime2(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return format.format(new Date(time));
    }

    public static String getTime3(String time) {
        Date date = null;
//		long tTime = Long.parseLong(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            if (!TextUtils.isEmpty(time)) {
                date = format.parse(time);
            }
        } catch (ParseException e) {
            return time;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        return dateFormat.format(date);
    }

    public static String getTimecctv(String time) {
        Date date = null;
        String str="";
        SimpleDateFormat format=null;
        SimpleDateFormat dateFormat=null;
        if (time!=null&&!time.equals("")&&time.contains("-")){
            str=time.replace("-","");
             format = new SimpleDateFormat("yyyyMMdd");
            try {
                if (!TextUtils.isEmpty(time)) {
                    date = format.parse(str);
                }
            } catch (ParseException e) {
                return time;
            }
             dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        }else if (time!=null&&!time.equals("")){
            format = new SimpleDateFormat("yyyyMMdd");
            try {
                if (!TextUtils.isEmpty(time)) {
                    date = format.parse(time);
                }
            } catch (ParseException e) {
                return time;
            }
            dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        }


        return dateFormat.format(date);
    }

    public static String getTime4(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(new Date(time));
    }

    public static String getTime5(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));
    }

    public static String getTime6(String timeText) {

        SimpleDateFormat tDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date tDate = null;
        Date tDate2 = new Date();
        try {

            long date = Long.parseLong(timeText) * 1000;
            tDate = new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tDateFormat.format(tDate);
    }

    public static String getDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        return format.format(new Date(time));
    }

    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    public static String getChatTime(long timesamp) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(timesamp);
        int temp = Integer.parseInt(sdf.format(today))
                - Integer.parseInt(sdf.format(otherDay));

        switch (temp) {
            case 0:
                result = "今天 " + getHourAndMin(timesamp);
                break;
            case 1:
                result = "昨天 " + getHourAndMin(timesamp);
                break;
            case 2:
                result = "前天 " + getHourAndMin(timesamp);
                break;

            default:
                // result = temp + "天前 ";
                result = getTime(timesamp);
                break;
        }

        return result;
    }

    public static String getFollowUpDate(long timesamp) {

        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        c.setTimeInMillis(timesamp);
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mMinute = String.valueOf(c.get(Calendar.MINUTE));
        if (mMonth.length() == 1) {
            mMonth = "0" + mMonth;
        }
        if (mDay.length() == 1) {
            mDay = "0" + mDay;
        }
        if (mHour.length() == 1) {
            mHour = "0" + mHour;
        }
        if (mMinute.length() == 1) {
            mMinute = "0" + mMinute;
        }

        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mYear + "-" + mMonth + "-" + mDay + "" + "(星期" + mWay + ")  "
                + mHour + ":" + mMinute;
    }

    // String(HH:mm) to int
    public static int convertHHmm2Int(String time) {
        String[] splitStrings = time.split(":");
        if (splitStrings.length == 2) {
            int nHour = Integer.valueOf(splitStrings[0]);
            int nMinute = Integer.valueOf(splitStrings[1]);
            return nHour * 60 + nMinute;
        }
        return 0;
    }

    public static int CompareTwoTime(String time1, String time2) {
        int index1 = time1.indexOf(" ");
        int index2 = time2.indexOf(" ");
        if (index1 > 0) {
            time1 = time1.substring(0, index1);
        }
        if (index2 > 0) {
            time2 = time2.substring(0, index2);
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = df.parse(time1);
            Date d2 = df.parse(time2);
            long diff = d1.getTime() - d2.getTime();
            if (diff > 0) {
                return 1;
            } else if (diff < 0) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return 0;

    }

    // 获取以秒为最小单位的毫秒值
    public static long getMinSecondTime(long time) {
        long minSecondTime = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(format.format(new Date(time)));
            minSecondTime = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return minSecondTime;
    }

    // 时间转成毫秒 simpleDateFormat:例如"yyyy-MM-dd HH:mm:ss"
    public static long convertFormatTimeToTimeMillis(String strTime,
                                                     String simpleDateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(simpleDateFormat);

        long millionSeconds = 0;
        try {
            millionSeconds = sdf.parse(strTime).getTime();// 毫秒
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return millionSeconds;
    }

    public static String convertString2DateString(String date) {

        if (date == null || date.equals("")) {

            return "";
        }

        try {

            Calendar tCalendar = Calendar.getInstance();
            tCalendar.setTimeInMillis(Long.parseLong(date) * 1000);
            String tDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm")
                    .format(tCalendar.getTime());

            return tDateString;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
