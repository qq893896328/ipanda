package cn.cntv.app.ipanda.ui.lovepanda.entity;

import java.util.List;

/**
 * @author ma qingwei
 * @ClassName: PandaBean
 * @Date on 2016/7/14 10:48
 * @Description：
 */
public class PandaBean {

    private VideosetBean videoset;

    private List<VideoBean> video;

    public List<VideoBean> getVideo() {
        return video;
    }

    public void setVideo(List<VideoBean> video) {
        this.video = video;
    }

    public VideosetBean getVideoset() {
        return videoset;
    }

    public void setVideoset(VideosetBean videoset) {
        this.videoset = videoset;
    }
}
