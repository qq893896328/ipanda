package cn.cntv.app.ipanda.ui.home.entity;

import cn.cntv.app.ipanda.constant.HomeTypeEnum;

/**
 * @ClassName: HomeLive
 * @author Xiao JinLai
 * @Date Dec 29, 2015 2:26:56 PM
 * @Description：Home live data class
 */
public class Live implements AdapterData {

	private String image;
	private String title;
	private String id;

	public Live() {
		super();
	}

	public Live(String image, String title, String id) {
		super();
		this.image = image;
		this.title = title;
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int getAdapterType() {
		return HomeTypeEnum.LIVE_TYPE.value();
	}

}
