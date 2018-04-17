package cn.cntv.app.ipanda.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Xiao JinLai
 * @Date: 2016-04-28 12:24
 * @Description:正则工具类
 */
public class MatcherUtils {

    /**
     * 判断颜色值
     * @param color
     * @return
     */
    public static boolean isColor(String color) {

        if (color == null || color.equals(""))
            return false;

        Pattern pattern = Pattern.compile("\\#[0-9a-fA-F]{6}");
        Matcher matcher = pattern.matcher(color);

        return matcher.matches();
    }
}
