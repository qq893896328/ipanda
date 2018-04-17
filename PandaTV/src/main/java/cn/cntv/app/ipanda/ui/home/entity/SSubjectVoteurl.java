package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

/**
 * @ClassName: SSubjectVoteurl
 * @author Xiao JinLai
 * @Date Dec 31, 2015 9:02:34 PM
 * @Description：Home special subject voteurl
 */
public class SSubjectVoteurl {

	private String title;
	private List<SSubjectVoteItem> itemlist;

	public SSubjectVoteurl() {
		super();
	}

	public SSubjectVoteurl(String title, List<SSubjectVoteItem> itemlist) {
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

	public List<SSubjectVoteItem> getItemlist() {
		return itemlist;
	}

	public void setItemlist(List<SSubjectVoteItem> itemlist) {
		this.itemlist = itemlist;
	}

}
