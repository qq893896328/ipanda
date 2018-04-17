package cn.cntv.app.ipanda.ui.personal.entity;

/**
 * Created by maqingwei on 2016/4/19.
 */
public class SinaOpenID {

    /**
     * timestamp : 1461049077
     * ticket : eebedee3-fa5a-49f2-99d1-bce4bf28fb1a
     * unionid :
     * errType : 0
     * errMsg :
     * errorCode : 0
     * user_seq_id : 47033201
     * openid : 5760516905
     * errorMsg :
     */

    private String timestamp;
    private String ticket;
    private String unionid;
    private String errType;
    private String errMsg;
    private String errorCode;
    private String user_seq_id;
    private String openid;
    private String errorMsg;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getErrType() {
        return errType;
    }

    public void setErrType(String errType) {
        this.errType = errType;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getUser_seq_id() {
        return user_seq_id;
    }

    public void setUser_seq_id(String user_seq_id) {
        this.user_seq_id = user_seq_id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
