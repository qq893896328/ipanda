package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

import cn.cntv.app.ipanda.constant.HomeTypeEnum;

/**
 * @ClassName: HomeAdapterHotLive
 * @author Xiao JinLai
 * @Date Dec 29, 2015 2:50:03 PM
 * @Description：Home adapter hot live data class
 */
public class HomeAdapterWallLive implements AdapterData {

	private List<HomeHotLiveList> hotlives;

	@Override
	public int getAdapterType() {
		return HomeTypeEnum.HOME_GREATWALL_TYPE.value();
	}

	public HomeAdapterWallLive() {
		super();
	}

	public HomeAdapterWallLive(List<HomeHotLiveList> hotlives) {
		super();
		this.hotlives = hotlives;
	}

	public List<HomeHotLiveList> getHotlives() {
		return hotlives;
	}

	public void setHotlives(List<HomeHotLiveList> hotlives) {
		this.hotlives = hotlives;
	}

}
