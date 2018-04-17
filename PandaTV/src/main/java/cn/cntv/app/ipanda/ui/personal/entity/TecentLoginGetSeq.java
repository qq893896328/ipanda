package cn.cntv.app.ipanda.ui.personal.entity;

/********************
 * 作者：malus
 * 日期：16/3/9
 * 时间：上午11:40
 * 注释：
 ********************/
public class TecentLoginGetSeq {
    private String errType;
    private String errMsg;
    private String user_seq_id;
    private String ticket;
    private String timestamp;

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

    public String getUser_seq_id() {
        return user_seq_id;
    }

    public void setUser_seq_id(String user_seq_id) {
        this.user_seq_id = user_seq_id;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "TecentLoginGetSeq{" +
                "errType='" + errType + '\'' +
                ", errMsg='" + errMsg + '\'' +
                ", user_seq_id='" + user_seq_id + '\'' +
                ", ticket='" + ticket + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
