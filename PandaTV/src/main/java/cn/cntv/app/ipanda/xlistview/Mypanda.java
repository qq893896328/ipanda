package cn.cntv.app.ipanda.xlistview;

/**
 * Created by Jack on 2016/4/13.
 */
public class Mypanda {


    /**
     * id : 3
     * image :  http://p1.img.cctvpic.com/photoAlbum/page/performance/img/2016/4/5/1459849647449_463.jpg
     * url : e0789c70cfe44896b3756592916e1fee
     * title : jcshph
     * videoLength : e0789c70cfe44896b3756592
     */

    private String id;
    private String image;
    private String url;
    private String title;
    private String guid;
    private String videoLength;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGuid() {
        return guid ;
    }

    public void setGuid(String guid) {
        this.guid = guid;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(String videoLength) {
        this.videoLength = videoLength;
    }
}
