package cn.cntv.app.ipanda.ui.cctv.entity;

import java.io.Serializable;

/**
 * @author： pj
 * @Date： 2015年12月30日 下午12:40:38
 * @Description:央视名栏大图数据对象
 */
public class CCTVBigImgItem implements Serializable {

	private static final long serialVersionUID = -2385850168538050866L;
	private String image;// 央视名栏大图图片
	private String title;// 标题
	private String id;// 辅助判断，若值中以arti开头的，是图文
	private String type;// 1为直播 2为视频 3为视频集 4为专题 5为正文
	private String stype;// 1为熊猫单视角直播 2为熊猫直播 3为直播中国单视角直播 4为直播中国直播 5为CCTV直播",
	private String url;

	private String pid;// 视频id
	private String vid;// 视频集id

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

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrder() {
		return order;
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

}