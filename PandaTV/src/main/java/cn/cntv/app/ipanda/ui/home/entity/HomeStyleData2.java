package cn.cntv.app.ipanda.ui.home.entity;

/**
 * @ClassName: HomeStyleDetail1
 * @author Xiao JinLai
 * @Date Dec 31, 2015 2:14:52 PM
 * @Description：Home style detail one class
 */
public class HomeStyleData2 {

	private String image;
	private String title;
	private String id;
	private String daytime;
	private int order;

	public HomeStyleData2() {
		super();
	}

	public HomeStyleData2(String image, String title, String id,
			String daytime, int order) {
		super();
		this.image = image;
		this.title = title;
		this.id = id;
		this.daytime = daytime;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDaytime() {
		return daytime;
	}

	public void setDaytime(String daytime) {
		this.daytime = daytime;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
