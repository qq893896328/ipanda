package cn.cntv.app.ipanda.manager;

import android.content.SharedPreferences;
import android.text.TextUtils;

import cn.cntv.app.ipanda.AppContext;

/**
 * Created by yuerq on 2016/5/25.
 */
public class UserManager {

    private static UserManager sInstance;

    private SharedPreferences prefs;

    private boolean isUserInfoRetrieved;

    private UserManager(){
        prefs = AppContext.getInstance().getSharedPreferences("user_info", 0);
    }

    public static UserManager getInstance() {
        if (sInstance == null) {
            sInstance = new UserManager();
        }
        return sInstance;
    }

    public boolean isUserLogged() {
       String userId = prefs.getString("userId", null);
        return  !TextUtils.isEmpty(userId);
    }

    public String getUserId() {
        return prefs.getString("userId", null);
    }

    public void saveUserId(String userId) {
        prefs.edit().putString("userId", userId).commit();
    }

    public String getNickName() {
        return prefs.getString("nickName", null);
    }

    public void saveNickName(String nickName) {
        prefs.edit().putString("nickName", nickName).commit();
    }

    public String getUserFace() {
        return prefs.getString("userface", null);
    }

    public void saveUserFace(String userface) {
        prefs.edit().putString("userface", userface).commit();
    }

    public String getVerifycode (){return prefs.getString("verifycode", null);}

    public void saveVerifycode(String verifycode) {
        prefs.edit().putString("verifycode", verifycode).commit();
    }

    /**
     * 是否成功请求过用户信息
     * @return
     */
    public boolean isUserInfoRetrieved() {
        return isUserInfoRetrieved;
    }

    public void setUserInfoRetrieved(boolean b) {
        isUserInfoRetrieved = b;
    }

    /**
     * 删除用户信息
     */
    public void clearUser() {
        prefs.edit().remove("userId").remove("nickName").remove("userface").remove("verifycode").commit();
    }
}
