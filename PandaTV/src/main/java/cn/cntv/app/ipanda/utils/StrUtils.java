package cn.cntv.app.ipanda.utils;

import com.gridsum.mobiledissector.util.StringUtil;

/**
 * @author： pj
 * @Date： 2016年1月26日 下午2:44:34
 * @Description:字符串工具类
 */
public class StrUtils {
	/**
	 * 提取字符串
	 * 
	 * @param str
	 * @param index
	 * @return
	 */
	public static final String subStrLength16(String str, int index) {
		if (!StringUtil.isNullOrEmpty(str)) {
			return str.length() > index ? str.substring(0, index) + "..." : str;
		}
		return "";
	}
}
