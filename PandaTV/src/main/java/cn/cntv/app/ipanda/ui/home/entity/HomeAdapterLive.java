package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

import cn.cntv.app.ipanda.constant.HomeTypeEnum;

/**
 * @ClassName: HomeAdapterLive
 * @author Xiao JinLai
 * @Date Jan 14, 2016 4:52:36 PM
 * @Description：
 */
public class HomeAdapterLive implements AdapterData {

	private List<HomeLiveList> homeLiveLists;

	public HomeAdapterLive() {
		super();
	}

	public HomeAdapterLive(List<HomeLiveList> homeLiveLists) {
		super();
		this.homeLiveLists = homeLiveLists;
	}

	public List<HomeLiveList> getHomeLiveLists() {
		return homeLiveLists;
	}

	public void setHomeLiveLists(List<HomeLiveList> homeLiveLists) {
		this.homeLiveLists = homeLiveLists;
	}

	@Override
	public int getAdapterType() {
		return HomeTypeEnum.HOME_LIVE_TYPE.value();
	}

}
