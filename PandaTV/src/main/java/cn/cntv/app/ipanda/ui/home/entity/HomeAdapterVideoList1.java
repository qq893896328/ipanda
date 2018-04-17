package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

import cn.cntv.app.ipanda.constant.HomeTypeEnum;
/**
 * @ClassName: HomeAdapterVideoList1
 * @author Xiao JinLai
 * @Date Dec 29, 2015 2:30:02 PM
 * @Description：Home adapter video list one data class
 */
public class HomeAdapterVideoList1 implements AdapterData {

	private List<HomeVideoList> videoLists;

	public HomeAdapterVideoList1() {
		super();
	}

	public HomeAdapterVideoList1(List<HomeVideoList> videoLists) {
		super();
		this.videoLists = videoLists;
	}

	public List<HomeVideoList> getVideoLists() {
		return videoLists;
	}

	public void setVideoLists(List<HomeVideoList> videoLists) {
		this.videoLists = videoLists;
	}

	@Override
	public int getAdapterType() {

		return HomeTypeEnum.HOME_VIDEOLIST1_TYPE.value();
	}

}
