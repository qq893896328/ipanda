package cn.cntv.app.ipanda.ui.home.entity;

/**
 * @ClassName: HomeBigImg
 * @author Xiao JinLai
 * @Date Dec 25, 2015 11:44:45 AM
 * @Description：Home carousel image detail info class
 */
public class BigImg {

	private String image;
	private String title;
	private String url;
	private String id;
	private String type;
	private String stype;
	private String pid;
	private String vid;
	private int order;

	public BigImg() {
		super();
	}

	public BigImg(String image, String title, String url, String id,
			String type, String stype, String pid, String vid, int order) {
		super();
		this.image = image;
		this.title = title;
		this.url = url;
		this.id = id;
		this.type = type;
		this.stype = stype;
		this.pid = pid;
		this.vid = vid;
		this.order = order;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getStype() {
		return stype;
	}

	public void setStype(String stype) {
		this.stype = stype;
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

}
