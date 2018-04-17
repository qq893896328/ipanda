package cn.cntv.app.ipanda.ui.home.entity;

import cn.cntv.app.ipanda.constant.HomeTypeEnum;

/**
 * @ClassName: HomeAdapterVideoList2
 * @author Xiao JinLai
 * @Date Dec 29, 2015 2:35:21 PM
 * @Description：Home adapter video list two data class
 */
public class HomeAdapterVideoList2 implements AdapterData {

	private HomeVideoList videoList;

	public HomeAdapterVideoList2() {
		super();
	}

	public HomeAdapterVideoList2(HomeVideoList videoList) {
		super();
		this.videoList = videoList;
	}

	public HomeVideoList getVideoList() {
		return videoList;
	}

	public void setVideoList(HomeVideoList videoList) {
		this.videoList = videoList;
	}

	@Override
	public int getAdapterType() {
		return HomeTypeEnum.HOME_VIDEOLIST2_TYPE.value();
	}

}
