package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

/**
 * @ClassName: HomePandaEyeList
 * @author Xiao JinLai
 * @Date Jan 18, 2016 12:15:47 PM
 * @Description：Home padna eye list
 */
public class HomePandaEyeList {

	private List<HomePandaEye2> list;

	public HomePandaEyeList() {
		super();
	}

	public HomePandaEyeList(List<HomePandaEye2> list) {
		super();
		this.list = list;
	}

	public List<HomePandaEye2> getList() {
		return list;
	}

	public void setList(List<HomePandaEye2> list) {
		this.list = list;
	}

}
