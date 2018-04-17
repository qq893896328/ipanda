package cn.cntv.app.ipanda.ui.play.entity;

import java.io.Serializable;

public class PlayLiveEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String channeId;// 直播频道ID
    private String title;// 直播标题
    private String image;// 直播头像
    private String url;
    private String guid;
    private String type;// 收藏类型
    private String channel; //统计栏目名称
    private int pageSource;// 视频页面来源：熊猫直播、直播中国、频道直播，标识收藏列表的直播页签中的列表项点击跳转到哪、
    private boolean isSingleVideo;// 是否为单视频，标识收藏列表的精彩看点页签中的列表项点击跳转到哪、
    private int sign;//为0标示未被点击，为1表示被点击

    public PlayLiveEntity() {
        super();
    }

    /**
     * @param channeId      //直播频道ID
     * @param title         //直播标题
     * @param image         //直播头像
     * @param url
     * @param guid
     * @param type          //收藏类型
     * @param pageSource    视频页面来源：熊猫直播-0、直播中国-1、频道直播-2， 标识收藏列表的直播页签中的列表项点击跳转到哪、
     * @param isSingleVideo 是否为单视频，标识收藏列表的精彩看点页签中的列表项点击跳转到哪、
     */
    public PlayLiveEntity(String channeId, String title, String image,
                          String url, String guid, String type, int pageSource,
                          boolean isSingleVideo) {
        super();
        this.channeId = channeId;
        this.title = title;
        this.image = image;
        this.url = url;
        this.guid = guid;
        this.type = type;
        this.pageSource = pageSource;
        this.isSingleVideo = isSingleVideo;
    }


    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public String getChanneId() {
        return channeId;
    }

    public void setChanneId(String channeId) {
        this.channeId = channeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPageSource() {
        return pageSource;
    }

    public void setPageSource(int pageSource) {
        this.pageSource = pageSource;
    }

    public boolean isSingleVideo() {
        return isSingleVideo;
    }

    public void setSingleVideo(boolean isSingleVideo) {
        this.isSingleVideo = isSingleVideo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "PlayLiveEntity [channeId=" + channeId + ", title=" + title
                + ", image=" + image + ", url=" + url + ", guid=" + guid
                + ", type=" + type + ", pageSource=" + pageSource
                + ", isSingleVideo=" + isSingleVideo + ", sign=" + sign + "]";
    }


}
