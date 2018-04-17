package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

/**
 * @ClassName: SSLiveVideoList
 * @author Xiao JinLai
 * @Date Dec 31, 2015 8:26:49 PM
 * @Description：Home special subject list
 */
public class SSVideoListItem {

	private String title;
	private List<SSVideoList> itemlist;

	public SSVideoListItem() {
		super();
	}

	public SSVideoListItem(String title, List<SSVideoList> itemlist) {
		super();
		this.title = title;
		this.itemlist = itemlist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<SSVideoList> getItemlist() {
		return itemlist;
	}

	public void setItemlist(List<SSVideoList> itemlist) {
		this.itemlist = itemlist;
	}

}
