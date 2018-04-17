package cn.cntv.app.ipanda.ui.cctv.entity;

/**
 * @author： pj
 * @Date： 2015年12月30日 下午12:38:43
 * @Description:Tab页的数据对象
 */
public class CCTVTabItem {

	private String title;// CCTV多语种标签标题
	private String url;// CCTV多语种标签标题接口url", (维护多语种直播对应列表接口)
	private String id;// 视频集id(没有可忽略)
	private String order;// 序号

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrder() {
		return order;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}