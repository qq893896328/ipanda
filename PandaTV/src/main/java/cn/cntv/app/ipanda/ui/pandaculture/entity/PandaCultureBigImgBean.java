package cn.cntv.app.ipanda.ui.pandaculture.entity;

/**
 * Created by maqingwei on 2016/5/17.
 */
public class PandaCultureBigImgBean {
    private String url;
    private String image;//熊猫文化轮播图图片
    private String title;//熊猫文化轮播图标题
    private String id;// 辅助判断，若值中以arti开头的，是图文
    private String type;// 1为直播 2为视频 3为视频集 4为专题 5为正文
    private String stype;// 1为熊猫单视角直播 2为熊猫直播 3为直播中国单视角直播 4为直播中国直播 5为CCTV直播"

    private String pid;//视频ID
    private String vid;//视频集ID
    private String order;//序号

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
