package cn.cntv.app.ipanda.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuerq on 16/8/12.
 */
public class PermissionUtil {

    /**
     * 请求权限
     * @param activity
     * @param permissions
     * @param requestCode
     * @return 是否要请求权限
     */
    public static boolean requestPermissions(Activity activity, String[] permissions, int requestCode) {

        List<String> list = new ArrayList<>();

        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(activity, p) != PackageManager.PERMISSION_GRANTED) {
                list.add(p);
            }
        }

        if (list.size() > 0) {
            ActivityCompat.requestPermissions(
                    activity,
                    list.toArray(new String[list.size()]),
                    requestCode);

            return true;
        }
        return false;
    }

}
