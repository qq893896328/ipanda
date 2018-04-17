package cn.cntv.app.ipanda.ui.home.entity;

/**
 * @ClassName: HomePandaEyeItem
 * @author Xiao JinLai
 * @Date Dec 28, 2015 5:27:57 PM
 * @Description：Home panda eye item class
 */
public class HomePandaEyeItem {

	private String title;
	private String bgcolor; // type background
	private String brief; // title type
	private String url;
	private String id;
	private String vid;
	private String pid;
	private int order;
	private String videoLength;
	private String image;

	public HomePandaEyeItem() {
		super();
	}

	public HomePandaEyeItem(String title, String bgcolor, String brief,
			String url, String id, String vid, String pid, int order,String videoLength,String image) {
		super();
		this.title = title;
		this.bgcolor = bgcolor;
		this.brief = brief;
		this.url = url;
		this.id = id;
		this.vid = vid;
		this.pid = pid;
		this.order = order;
		this.image = image;
		this.videoLength = videoLength;
	}
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	public String getVideoLength() {
		return videoLength;
	}

	public void setVideoLength(String videoLength) {
		this.videoLength = videoLength;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBgcolor() {
		return bgcolor;
	}

	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
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

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
