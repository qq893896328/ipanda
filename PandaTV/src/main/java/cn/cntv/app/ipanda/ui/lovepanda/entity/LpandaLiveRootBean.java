package cn.cntv.app.ipanda.ui.lovepanda.entity;

import java.util.List;

public class LpandaLiveRootBean {

    private List<LpandaLive> live;
    private LpandaBookmark bookmark;
    public void setLive(List<LpandaLive> live) {
         this.live = live;
     }
     public List<LpandaLive> getLive() {
         return live;
     }

    public void setBookmark(LpandaBookmark bookmark) {
         this.bookmark = bookmark;
     }
     public LpandaBookmark getBookmark() {
         return bookmark;
     }

}