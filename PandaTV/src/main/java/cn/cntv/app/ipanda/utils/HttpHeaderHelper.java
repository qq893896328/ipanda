package cn.cntv.app.ipanda.utils;

import java.util.List;

import okhttp3.Headers;

/**
 * Created by yuerq on 2016/5/25.
 */
public class HttpHeaderHelper {


    public static String parseVerifyCode(Headers headers) {
      return parseAttribute(headers, "verifycode");
    }

    public static String parseAttribute(Headers headers, String name) {

        List<String> cookieList = headers.values("Set-Cookie");
        for (int j = cookieList.size()-1; j >0; j--) {
            String cookie = cookieList.get(j);
            if (cookie != null) {
                if (cookie.endsWith(";")) {
                    cookie.substring(0, cookie.length() - 1);
                }
                String[] params = cookie.split(";");
                for (int i = 0; i < params.length; i++) {
                    String[] pair = params[params.length - 1 - i].trim().split("=");
                    if (pair.length == 2) {
                        if (pair[0].equals(name)) {
                            return pair[1];
                        }
                    }
                }
            }
        }
        return null;
    }

}
