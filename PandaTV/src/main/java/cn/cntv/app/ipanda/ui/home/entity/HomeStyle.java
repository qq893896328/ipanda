package cn.cntv.app.ipanda.ui.home.entity;

/**
 * @ClassName: HomeStyle
 * @author Xiao JinLai
 * @Date Dec 29, 2015 3:37:45 PM
 * @Description：Home style ancestor class
 */
public class HomeStyle {

	private String listUrl;
	private String title;
	private int type;
	private int order;

	public HomeStyle() {
		super();
	}

	public HomeStyle(String listUrl, String title, int type, int order) {
		super();
		this.listUrl = listUrl;
		this.title = title;
		this.type = type;
		this.order = order;
	}

	public String getListUrl() {
		return listUrl;
	}

	public void setListUrl(String listUrl) {
		this.listUrl = listUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
