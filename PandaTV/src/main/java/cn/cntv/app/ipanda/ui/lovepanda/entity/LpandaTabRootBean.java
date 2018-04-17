package cn.cntv.app.ipanda.ui.lovepanda.entity;

import java.util.List;

public class LpandaTabRootBean {

    private List<LpandaTablist> tablist;
    public void setTablist(List<LpandaTablist> tablist) {
         this.tablist = tablist;
     }
   public List<LpandaTablist> getTablist() {
	return tablist;
}
}