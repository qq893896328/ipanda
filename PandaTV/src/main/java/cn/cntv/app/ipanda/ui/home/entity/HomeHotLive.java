package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

/**
 * @ClassName: HomeHotLive
 * @author Xiao JinLai
 * @Date Jan 18, 2016 11:31:27 AM
 * @Description：Home hot live class
 */
public class HomeHotLive {

	private String title;
	private List<HomeHotLiveList> list;

	public HomeHotLive() {
		super();
	}

	public HomeHotLive(String title, List<HomeHotLiveList> list) {
		super();
		this.title = title;
		this.list = list;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<HomeHotLiveList> getList() {
		return list;
	}

	public void setList(List<HomeHotLiveList> list) {
		this.list = list;
	}

}
