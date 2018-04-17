package cn.cntv.app.ipanda.ui.cctv.entity;

import java.io.Serializable;

/**
 * @author： pj
 * @Date： 2015年12月30日 下午12:41:42
 * @Description:央视名栏列表项数据对象
 */
public class CCTVWellKnowItem implements Serializable {
	private static final long serialVersionUID = -1207231169890094420L;
	private String image;// 央视名栏列表图片
	private String title;// 央视名栏列表标题
	private String brief;// 央视名栏列表简介
	private String videoLength;// 央视名栏列表视频时长
	private String id;// 央视名栏列表视频集id
	private String order;// 序号

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getBrief() {
		return brief;
	}

	public void setVideoLength(String videoLength) {
		this.videoLength = videoLength;
	}

	public String getVideoLength() {
		return videoLength;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrder() {
		return order;
	}
}