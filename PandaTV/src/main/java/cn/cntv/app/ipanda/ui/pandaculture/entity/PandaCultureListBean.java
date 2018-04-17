package cn.cntv.app.ipanda.ui.pandaculture.entity;

/**
 * Created by maqingwei on 2016/5/18.
 */
public class PandaCultureListBean {
    private String url;
    private String image;//熊猫文化列表图片
    private String title;//熊猫文化列表标题
    private String brief;//熊猫文化列表简介
    private String videoLength;//熊猫文化列表视频时长
    private String id;//熊猫文化列表视频集ID
    private String order;//序号

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;//视频类型  1 单视频  2 视频集

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

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(String videoLength) {
        this.videoLength = videoLength;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

}
