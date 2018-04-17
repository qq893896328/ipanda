package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

import cn.cntv.app.ipanda.constant.HomeTypeEnum;

/**
 * @ClassName: HomeStyle1
 * @author Xiao JinLai
 * @Date Dec 29, 2015 3:40:44 PM
 * @Description：Home Style one data class
 */
public class HomeLive2 implements AdapterData {

	private List<HomeStyleData> live2;

	public HomeLive2() {
		super();
	}

	public HomeLive2(List<HomeStyleData> live2) {
		super();
		this.live2 = live2;
	}

	public List<HomeStyleData> getLive2() {
		return live2;
	}

	public void setLive2(List<HomeStyleData> live2) {
		this.live2 = live2;
	}

	@Override
	public int getAdapterType() {
		return HomeTypeEnum.HOME_LIVE2_TYPE.value();
	}
}
