package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

/**
 * @ClassName: HomeStyleDataItem
 * @author Xiao JinLai
 * @Date Dec 31, 2015 2:50:09 PM
 * @Description：Home style data items
 */
public class HomeStyleDataInfo {

	private List<HomeStyleData> list;
	private List<HomeStyleData> pagelist;

	public HomeStyleDataInfo() {
		super();
	}

	public HomeStyleDataInfo(List<HomeStyleData> list,
			List<HomeStyleData> pagelist) {
		super();
		this.list = list;
		this.pagelist = pagelist;
	}

	public List<HomeStyleData> getList() {
		return list;
	}

	public void setList(List<HomeStyleData> list) {
		this.list = list;
	}

	public List<HomeStyleData> getPagelist() {
		return pagelist;
	}

	public void setPagelist(List<HomeStyleData> pagelist) {
		this.pagelist = pagelist;
	}

}
