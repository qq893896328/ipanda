package cn.cntv.app.ipanda.ui.personal.entity;

/**
 * Created by Jack on 2016/5/16.
 */
public class NickName {

    /**
     * code : -100
     * error : 该用户未登录！
     */

    private int code;
    private String error;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
