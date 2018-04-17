package cn.cntv.app.ipanda.ui.home.entity;

/**
 * @ClassName: ViedeoList1
 * @author Xiao JinLai
 * @Date Dec 25, 2015 7:42:47 PM
 * @Description：Video list ancestor class
 */
public class HomeVideoList {

	private String image;
	private String url;
	private String title;
	private String videoLength;
	private String id;
	private String vid;
	private String pid;
	private int order;

	public HomeVideoList() {
		super();
	}

	public HomeVideoList(String image, String url, String title,
			String videoLength, String id, String vid, String pid, int order) {
		super();
		this.image = image;
		this.url = url;
		this.title = title;
		this.videoLength = videoLength;
		this.id = id;
		this.vid = vid;
		this.pid = pid;
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
