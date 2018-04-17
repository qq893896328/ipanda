package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

import cn.cntv.app.ipanda.constant.HomeTypeEnum;

/**
 * @ClassName: HomeStyle1
 * @author Xiao JinLai
 * @Date Dec 29, 2015 3:40:44 PM
 * @Description：Home Style one data class
 */
public class HomeStyle1 implements AdapterData {

	private List<HomeStyleData> styles1;

	public HomeStyle1() {
		super();
	}

	public HomeStyle1(List<HomeStyleData> styles1) {
		super();
		this.styles1 = styles1;
	}

	public List<HomeStyleData> getStyles1() {
		return styles1;
	}

	public void setStyles1(List<HomeStyleData> styles1) {
		this.styles1 = styles1;
	}

	@Override
	public int getAdapterType() {
		return HomeTypeEnum.HOME_STYLE1_TYPE.value();
	}
}
