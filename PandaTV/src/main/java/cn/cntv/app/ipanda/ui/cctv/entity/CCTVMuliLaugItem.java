package cn.cntv.app.ipanda.ui.cctv.entity;

import java.io.Serializable;

/**
 * @author： pj
 * @Date： 2015年12月30日 下午12:38:22
 * @Description:多语种列表项的数据对象
 */
public class CCTVMuliLaugItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title;// CCTV多语种直播标题
	private String brief;// CCTV多语种直播简介
	private String image;// CCTV多语种直播图片
	private String liveimage;// CCTV多语种直播下标
	private String id;// CCTV多语种直播流id
	private String order;// 序号
	private String type;// "1为文字左对齐，2为文字右对齐",

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

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}

	public void setLiveimage(String liveimage) {
		this.liveimage = liveimage;
	}

	public String getLiveimage() {
		return liveimage;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}