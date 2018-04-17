package cn.cntv.app.ipanda.ui.personal.entity;

/**
 * Created by maqingwei on 2016/4/19.
 */
public class SinaUrl {

    /**
     * timestamp :
     * errType :
     * errMsg :
     * errorCode : 0
     * errorMsg : 成功
     * url : https://reg.cntv.cn/oauthlogin/OAuthLogin.action?clientName=sina&from=http%3A%2F%2Fipanda_mobile.regclientuser.cntv.cn&openid=5760516905&requestTime=2016-04-19+14%3A39%3A24%2C511&signature=D68A15D53B4AE4312F43DF4A588296EE
     */

    private String timestamp;
    private String errType;
    private String errMsg;
    private String errorCode;
    private String errorMsg;
    private String url;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
