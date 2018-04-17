package cn.cntv.app.ipanda.ui.pandaeye.entity;

import java.io.Serializable;

public class PandaEyeTopData implements Serializable{

	private String image;
	private String title;
	private String url;
	private String id;
	private String type; // 首页轮播：1为直播 2为视频 3为视频集 4为专题 5为正文
	private String stype;//"1为熊猫单视角直播 2为熊猫直播 3为直播中国单视角直播 4为直播中国直播 5为CCTV直播",
    private String pid;
    private String vid;
	private String order;
	
	   
	
	
	
	   
	
	
	
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
	public String getStype() {
		return stype;
	}
	public void setStype(String stype) {
		this.stype = stype;
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
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "PandaEyeTopData [image=" + image + ", title=" + title
				+ ", url=" + url + ", id=" + id + ", type=" + type + ", pid="
				+ pid + ", vid=" + vid + ", order=" + order + ", stype="
				+ stype + "]";
	}
	
	
	
	
	
	
	
	
	
	
	
}
