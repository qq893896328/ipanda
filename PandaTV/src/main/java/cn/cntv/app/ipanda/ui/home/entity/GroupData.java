package cn.cntv.app.ipanda.ui.home.entity;

import cn.cntv.app.ipanda.constant.HomeTypeEnum;

/**
 * @ClassName: HomeGroupData
 * @author Xiao JinLai
 * @Date Dec 30, 2015 3:32:00 PM
 * @Description：Home group title class
 */
public class GroupData implements AdapterData {

	private String title;
	private String image;
	private String url;

	public GroupData() {
		super();
	}

	public GroupData(String title) {
		super();
		this.title = title;
	}

	public GroupData(String title, String image) {
		super();
		this.title = title;
		this.image = image;
	}

	public GroupData(String title, String image, String url) {
		super();
		this.title = title;
		this.image = image;
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int getAdapterType() {
		return HomeTypeEnum.GROUP_TYPE.value();
	}

}
