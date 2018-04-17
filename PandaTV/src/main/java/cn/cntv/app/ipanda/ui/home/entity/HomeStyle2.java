package cn.cntv.app.ipanda.ui.home.entity;

import cn.cntv.app.ipanda.constant.HomeTypeEnum;

/**
 * @ClassName: HomeStyle2
 * @author Xiao JinLai
 * @Date Dec 29, 2015 3:44:02 PM
 * @Description：Home style two data class
 */
public class HomeStyle2 implements AdapterData {

	private HomeStyleData styleData;

	public HomeStyle2() {
		super();
	}

	public HomeStyle2(HomeStyleData styleData) {
		super();
		this.styleData = styleData;
	}

	public HomeStyleData getStyleData() {
		return styleData;
	}

	public void setStyleData(HomeStyleData styleData) {
		this.styleData = styleData;
	}

	@Override
	public int getAdapterType() {
		return HomeTypeEnum.HOME_STYLE2_TYPE.value();
	}

}
