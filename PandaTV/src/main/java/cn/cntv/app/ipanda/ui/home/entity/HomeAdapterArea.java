package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

import cn.cntv.app.ipanda.constant.HomeTypeEnum;

/**
 * @ClassName: SSVideoList2
 * @author Xiao JinLai
 * @Date Dec 31, 2015 8:35:21 PM
 * @Description：Home adapter area class
 */
public class HomeAdapterArea implements AdapterData {

	private List<SSVideoList> list;

	public HomeAdapterArea() {
		super();
	}

	public HomeAdapterArea(List<SSVideoList> list) {
		super();
		this.list = list;
	}

	public List<SSVideoList> getList() {
		return list;
	}

	public void setList(List<SSVideoList> list) {
		this.list = list;
	}

	@Override
	public int getAdapterType() {

		return HomeTypeEnum.HOME_AREA_TYPE.value();
	}

}
