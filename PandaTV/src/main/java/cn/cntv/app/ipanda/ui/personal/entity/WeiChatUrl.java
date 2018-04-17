package cn.cntv.app.ipanda.ui.personal.entity;

/**
 * Created by maqingwei on 2016/4/19.
 */
public class WeiChatUrl {

    /**
     * timestamp :
     * errType :
     * errMsg :
     * errorCode : 0
     * errorMsg : 成功
     * url : https://reg.cntv.cn/oauthlogin/OAuthMobileWeixinLogin.action?openid=oEB-4wD4t1L-RBGmsvyTbBEG1qMY&signature=5689DBC15C03B1FE34E603A54C1462F2&unionid=o_e6Ys6P1z4pYX_-EW2I1e-_SdVQ&from=ipanda_moblile
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
