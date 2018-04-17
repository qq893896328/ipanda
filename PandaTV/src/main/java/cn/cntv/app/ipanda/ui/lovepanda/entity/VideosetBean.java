package cn.cntv.app.ipanda.ui.lovepanda.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author ma qingwei
 * @ClassName: VideosetBean
 * @Date on 2016/7/14 10:16
 * @Description：
 */
public class VideosetBean {

    @SerializedName("0")
    private O o;
    private String count;


    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public O getO() {
        return o;
    }

    public void setO(O o) {
        this.o = o;
    }
}
