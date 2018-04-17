package cn.cntv.app.ipanda.ui.home.entity;

/**
 * @ClassName: HomeHotlive
 * @author Xiao JinLai
 * @Date Dec 25, 2015 7:56:23 PM
 * @Description：Home hot live list class
 */
public class HomeHotLiveList {

	private String image;
	private String url;
	private String title;
	private String id;
	private String vid;
	private int order;

	public HomeHotLiveList() {
		super();
	}

	public HomeHotLiveList(String image, String url, String title, String id,
			String vid, int order) {
		super();
		this.image = image;
		this.url = url;
		this.title = title;
		this.id = id;
		this.vid = vid;
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

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
