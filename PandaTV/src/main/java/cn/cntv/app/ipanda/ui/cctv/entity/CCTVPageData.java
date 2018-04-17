package cn.cntv.app.ipanda.ui.cctv.entity;

import java.util.List;

/**
 * @author： pj
 * @Date： 2015年12月30日 下午12:30:22
 * @Description:CCTV页签主页面所有的数据对象
 */
public class CCTVPageData {
	private List<CCTVTabItem> tablist;// 页签数据
	private List<CCTVMuliLaugItem> list;// 多语种列表数据

	public List<CCTVTabItem> getTablist() {
		return tablist;
	}

	public void setTablist(List<CCTVTabItem> tablist) {
		this.tablist = tablist;
	}

	public List<CCTVMuliLaugItem> getList() {
		return list;
	}

	public void setList(List<CCTVMuliLaugItem> list) {
		this.list = list;
	}

}
