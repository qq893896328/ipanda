package cn.cntv.app.ipanda.ui.home.entity;

import cn.cntv.app.ipanda.constant.HomeTypeEnum;

/**
 * @ClassName: HomePandaEye2
 * @author Xiao JinLai
 * @Date Jan 23, 2016 9:49:58 AM
 * @Description：Home panda eye type 2
 */
public class HomePandaEye2 implements AdapterData {

	private String url;
	private String image;
	private String title;
	private String videoLength;
	private String id;
	private String daytime;
	private String type;
	private String pid;
	private String vid;
	private int order;

	public HomePandaEye2() {
		super();
	}

	public HomePandaEye2(String url, String image, String title,
			String videoLength, String id, String daytime, String type,
			String pid, String vid, int order) {
		super();
		this.url = url;
		this.image = image;
		this.title = title;
		this.videoLength = videoLength;
		this.id = id;
		this.daytime = daytime;
		this.type = type;
		this.pid = pid;
		this.vid = vid;
		this.order = order;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getVideoLength() {
		return videoLength;
	}

	public void setVideoLength(String videoLength) {
		this.videoLength = videoLength;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDaytime() {
		return daytime;
	}

	public void setDaytime(String daytime) {
		this.daytime = daytime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getAdapterType() {

		return HomeTypeEnum.HOME_PANDAEYE2_TYPE.value();
	}

}
