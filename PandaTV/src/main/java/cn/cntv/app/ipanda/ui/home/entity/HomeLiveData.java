package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

/**
 * @ClassName: HomeLive
 * @author Xiao JinLai
 * @Date Jan 14, 2016 4:20:23 PM
 * @Description：
 */
public class HomeLiveData {

	private String title;
	private List<HomeLiveList> listlive;
	private String listurl;

	public HomeLiveData() {
		super();
	}

	public HomeLiveData(String title, List<HomeLiveList> listlive,
			String listurl) {
		super();
		this.title = title;
		this.listlive = listlive;
		this.listurl = listurl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<HomeLiveList> getListlive() {
		return listlive;
	}

	public void setListlive(List<HomeLiveList> listlive) {
		this.listlive = listlive;
	}

	public String getListurl() {
		return listurl;
	}

	public void setListurl(String listurl) {
		this.listurl = listurl;
	}

}
