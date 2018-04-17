package cn.cntv.app.ipanda.ui.home.entity;

/**
 * @ClassName: HomeLiveList
 * @author Xiao JinLai
 * @Date Jan 14, 2016 4:17:59 PM
 * @Description：
 */
public class HomeLiveList {

	private String title;
	private String id;
	private String order;

	public HomeLiveList() {
		super();
	}

	public HomeLiveList(String title, String id, String order) {
		super();
		this.title = title;
		this.id = id;
		this.order = order;
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

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

}
