package cn.cntv.app.ipanda.ui.home.entity;

/**
 * @ClassName: HomeItemListener
 * @author Xiao JinLai
 * @Date Dec 31, 2015 4:35:29 PM
 * @Description：Home adapter item click listener data class
 */
public class HomeItemClickData {

	private String url;
	private String id;

	public HomeItemClickData() {
		super();
	}

	public HomeItemClickData(String url, String id) {
		super();
		this.url = url;
		this.id = id;
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

}
