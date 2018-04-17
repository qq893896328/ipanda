package cn.cntv.app.ipanda.ui.home.entity;

import cn.cntv.app.ipanda.constant.SSubjectTypeEnum;

/**
 * @ClassName: SSVideoList1
 * @author Xiao JinLai
 * @Date Dec 31, 2015 8:35:21 PM
 * @Description：Home special subject list1
 */
public class SSLiveVideoList implements AdapterData {

	private String image;
	private String url;
	private String title;
	private String videoLength;
	private String liveimage;
	private String id;
	private int order;

	public SSLiveVideoList() {
		super();
	}

	public SSLiveVideoList(String image, String url, String title,
			String videoLength, String liveimage, String id, int order) {
		super();
		this.image = image;
		this.url = url;
		this.title = title;
		this.videoLength = videoLength;
		this.liveimage = liveimage;
		this.id = id;
		this.order = order;
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

	public String getLiveimage() {
		return liveimage;
	}

	public void setLiveimage(String liveimage) {
		this.liveimage = liveimage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getAdapterType() {

		return SSubjectTypeEnum.SSUBJECT_LIVE_LIST.value();
	}

}
