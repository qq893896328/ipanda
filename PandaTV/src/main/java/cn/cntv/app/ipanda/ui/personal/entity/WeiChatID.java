package cn.cntv.app.ipanda.ui.personal.entity;

/**
 * Created by maqingwei on 2016/4/19.
 */
public class WeiChatID {
    private String errorCode;
    private String errorMsg;
    private String unionid;
    private String openid;
    private String user_seq_id;
    private String timestamp;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUser_seq_id() {
        return user_seq_id;
    }

    public void setUser_seq_id(String user_seq_id) {
        this.user_seq_id = user_seq_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "WeixinId{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", unionid='" + unionid + '\'' +
                ", openid='" + openid + '\'' +
                ", user_seq_id='" + user_seq_id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
