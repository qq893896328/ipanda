package cn.cntv.app.ipanda.ui.home.entity;

import cn.cntv.app.ipanda.constant.HomeTypeEnum;

/**
 * @ClassName: HomeRecoSSubject
 * @author Xiao JinLai
 * @Date Dec 29, 2015 2:39:45 PM
 * @Description：Home recommend special subject data class
 */
public class HomeRecoSSubject implements AdapterData {

	private String image;
	private String listurl;
	private String title;
	private int order;

	public HomeRecoSSubject() {
		super();
	}

	public HomeRecoSSubject(String image, String listurl, String title,
			int order) {
		super();
		this.image = image;
		this.listurl = listurl;
		this.title = title;
		this.order = order;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getListurl() {
		return listurl;
	}

	public void setListurl(String listurl) {
		this.listurl = listurl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getAdapterType() {
		return HomeTypeEnum.HOME_SSUBJECT_TYPE.value();
	}
}
