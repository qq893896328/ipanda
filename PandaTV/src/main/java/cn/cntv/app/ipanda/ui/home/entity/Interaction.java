package cn.cntv.app.ipanda.ui.home.entity;

/**
 * @ClassName: HomeInteraction
 * @author Xiao JinLai
 * @Date Dec 25, 2015 8:16:52 PM
 * @Description：Home interaction ancestor class
 */
public class Interaction {

	private String image;
	private String title;
	private String url;
	private int order;

	public Interaction() {
		super();
	}

	public Interaction(String image, String title, String url, int order) {
		super();
		this.image = image;
		this.title = title;
		this.url = url;
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

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
