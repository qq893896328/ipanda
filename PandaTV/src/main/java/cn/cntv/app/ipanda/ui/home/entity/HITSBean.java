package cn.cntv.app.ipanda.ui.home.entity;

import java.io.Serializable;

/**
 * @author ma qingwei
 * @ClassName: HITSBean
 * @Date on 2016/6/6 14:48
 * @Description：
 */
public  class HITSBean implements Serializable{
    private String id;
    private String PLAYTIME;
    private String TITLE;
    private String CONTENT;
    private String CHANNEL;
    private int DURATION;
    private String PICPATH;
    private String URL;
    private String TAGS;
    private String DETAILSID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPLAYTIME() {
        return PLAYTIME;
    }

    public void setPLAYTIME(String PLAYTIME) {
        this.PLAYTIME = PLAYTIME;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public String getCHANNEL() {
        return CHANNEL;
    }

    public void setCHANNEL(String CHANNEL) {
        this.CHANNEL = CHANNEL;
    }

    public int getDURATION() {
        return DURATION;
    }

    public void setDURATION(int DURATION) {
        this.DURATION = DURATION;
    }

    public String getPICPATH() {
        return PICPATH;
    }

    public void setPICPATH(String PICPATH) {
        this.PICPATH = PICPATH;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getTAGS() {
        return TAGS;
    }

    public void setTAGS(String TAGS) {
        this.TAGS = TAGS;
    }

    public String getDETAILSID() {
        return DETAILSID;
    }

    public void setDETAILSID(String DETAILSID) {
        this.DETAILSID = DETAILSID;
    }
}