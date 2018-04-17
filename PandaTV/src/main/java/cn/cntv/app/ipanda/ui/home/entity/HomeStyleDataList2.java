package cn.cntv.app.ipanda.ui.home.entity;

/**
 * @ClassName: HomeStyleDataList1
 * @author Xiao JinLai
 * @Date Dec 31, 2015 2:50:47 PM
 * @Description：Home style one data list
 */
public class HomeStyleDataList2 {

	private HomeStyleDataItem2 pagelist;

	public HomeStyleDataList2() {
		super();
	}

	public HomeStyleDataList2(HomeStyleDataItem2 pagelist) {
		super();
		this.pagelist = pagelist;
	}

	public HomeStyleDataItem2 getPagelist() {
		return pagelist;
	}

	public void setPagelist(HomeStyleDataItem2 pagelist) {
		this.pagelist = pagelist;
	}

}
