package cn.cntv.app.ipanda.ui.cctv.entity;

import java.io.Serializable;

/**
 * @author： pj
 * @Date： 2016年1月4日 下午6:37:22
 * @Description:央视名栏底层页的tab列表数据对象
 */
public class CCTVVideo implements Serializable {

    private String vsid;
    private String order;
    private String vid;
    private String t;
    private String url;
    private String ptime;
    private String img;
    private String len;
    private String em;
    private int sign;//标示为0标示未点击，为1标示处于点击状态

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public void setVsid(String vsid) {
        this.vsid = vsid;
    }

    public String getVsid() {
        return vsid;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrder() {
        return order;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getVid() {
        return vid;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getT() {
        return t;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getPtime() {
        return ptime;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setLen(String len) {
        this.len = len;
    }

    public String getLen() {
        return len;
    }

    public void setEm(String em) {
        this.em = em;
    }

    public String getEm() {
        return em;
    }

    @Override
    public String toString() {
        return "CCTVVideo [vsid=" + vsid + ", order=" + order + ", vid=" + vid
                + ", t=" + t + ", url=" + url + ", ptime=" + ptime + ", img="
                + img + ", len=" + len + ", em=" + em + ", sign=" + sign + "]";
    }


}