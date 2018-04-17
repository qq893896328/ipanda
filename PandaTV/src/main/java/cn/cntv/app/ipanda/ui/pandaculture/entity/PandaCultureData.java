package cn.cntv.app.ipanda.ui.pandaculture.entity;

import java.util.List;

/**
 * Created by maqingwei on 2016/5/17.
 */
public class PandaCultureData {


    private List<PandaCultureBigImgBean> bigImg;//熊猫文化大轮播图

    private List<PandaCultureListBean> list;//熊猫文化视频列表

    public List<PandaCultureBigImgBean> getBigImg() {
        return bigImg;
    }
    public void setBigImg(List<PandaCultureBigImgBean> bigImg) {
        this.bigImg = bigImg;
    }

    public List<PandaCultureListBean> getList() {
        return list;
    }

    public void setList(List<PandaCultureListBean> list) {
        this.list = list;
    }


}
