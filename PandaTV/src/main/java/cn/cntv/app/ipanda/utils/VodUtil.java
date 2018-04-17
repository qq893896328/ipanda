package cn.cntv.app.ipanda.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cn.cntv.app.ipanda.vod.entity.PlayModeBean;

public class VodUtil {

    public static ArrayList<PlayModeBean> setBitRate(String mplayUrl, InputStream is) {
        ArrayList<PlayModeBean> modeLists = new ArrayList<PlayModeBean>();
        try {
            String lUrl = null;
            String hUrl = null;
            String sUrl = null;
            String cUrl = null;
            String cgUrl = null;
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is));

            for (String line = reader.readLine(); line != null; line = reader
                    .readLine()) {
                // Logs.e(TAG, "jsx===line==" + line);
                if (line.contains("BANDWIDTH")) {
                    line = line.replace(" ", "");
                    String[] tmpA = line.split(",");
                    String str = "";
                    for (int i = 0; i < tmpA.length; ++i) {
                        str = tmpA[i];
                        if (str.indexOf("BANDWIDTH") >= 0) {
                            tmpA = str.split("=");
                            str = tmpA[1];
                            break;
                        }
                    }

                    Logs.e("url", "jsx==mPlayUrl==" + mplayUrl);
                    // Logs.e(TAG,
                    // "jsx===Integer.valueOf(str) / 1024=="
                    // + Integer.valueOf(str) / 1024 + "");

                    if (Integer.valueOf(str) / 1024 > 1600
                            && Integer.valueOf(str) / 1024 <= 2500) {// 超高清
                        String subUrl = reader.readLine();
                        cgUrl = getVarientPlaylist(mplayUrl, subUrl);
                        Logs.e("url", "jsx==cgUrl==" + cgUrl);

                        PlayModeBean bean = new PlayModeBean();
                        bean.setTitle("超高清");
                        bean.setChecked(false);
                        bean.setPlayUrl(cgUrl);
                        modeLists.add(bean);
                    }
                    if (Integer.valueOf(str) / 1024 > 900
                            && Integer.valueOf(str) / 1024 <= 1600) {// 超清
                        String subUrl = reader.readLine();
                        cUrl = getVarientPlaylist(mplayUrl, subUrl);
                        Logs.e("url", "jsx==cUrl==" + cUrl + "");

                        PlayModeBean bean = new PlayModeBean();
                        bean.setTitle("超清");
                        bean.setChecked(false);
                        bean.setPlayUrl(cUrl);
                        modeLists.add(bean);
                    }
                    if (Integer.valueOf(str) / 1024 > 600
                            && Integer.valueOf(str) / 1024 <= 900) {// 高清
                        String subUrl = reader.readLine();
                        hUrl = getVarientPlaylist(mplayUrl, subUrl);
                        Logs.e("url", "jsx==hurl==" + hUrl + "");

                        PlayModeBean bean = new PlayModeBean();
                        bean.setTitle("高清");
                        bean.setChecked(false);
                        bean.setPlayUrl(hUrl);
                        modeLists.add(bean);
                    }
                    if (Integer.valueOf(str) / 1024 <= 600
                            && Integer.valueOf(str) / 1024 > 300) {
                        String subUrl = reader.readLine();
                        lUrl = getVarientPlaylist(mplayUrl, subUrl);
                        Logs.e("url", "jsx==lurl==" + lUrl + "");

                        PlayModeBean bean = new PlayModeBean();
                        bean.setTitle("标清");
                        bean.setChecked(false);
                        bean.setPlayUrl(lUrl);
                        modeLists.add(bean);
                    }
                    if (Integer.valueOf(str) / 1024 <= 300) {
                        String subUrl = reader.readLine();
                        sUrl = getVarientPlaylist(mplayUrl, subUrl);
                        Logs.e("url", "jsx==surl==" + sUrl + "");

                        PlayModeBean bean = new PlayModeBean();
                        bean.setTitle("流畅");
                        bean.setChecked(false);
                        bean.setPlayUrl(sUrl);
                        modeLists.add(bean);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Log.e("url", modeLists.toString());

        return modeLists;
    }


    public static String getVarientPlaylist(String mainUrl, String subUrl) {

        if (mainUrl == null || "".equals((mainUrl = mainUrl.trim()))) {
            return null;
        }

        if (!mainUrl.startsWith("http://") && !mainUrl.startsWith("https://")) {
            return null;
        }

        String protocol = mainUrl.substring(0, mainUrl.indexOf("/") - 1);
        String strtoken = mainUrl.substring(mainUrl.indexOf("/") + 2);
        String domainName = "";

        int domainIdx = strtoken.indexOf("/");
        if (domainIdx != -1) {

            domainName = strtoken.substring(0, domainIdx);
        } else
            return null;


        String realUrl = "";
        if (subUrl.startsWith("http://") || subUrl.startsWith("https://")) {
            //contain whole real url
            realUrl = subUrl;
        } else if (subUrl.startsWith("/")) {
            //domain + subUrl;
            String domainUrl = protocol + "://" + domainName;
            realUrl = domainUrl + subUrl;
        } else {
            //mainUrl + subUrl;
            int index = mainUrl.lastIndexOf("/");
            realUrl = mainUrl.substring(0, index) + "/" + subUrl;
        }
        return realUrl;
    }


}
