package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

/**
 * @ClassName: HomeAreaData
 * @author Xiao JinLai
 * @Date Jan 14, 2016 4:10:03 PM
 * @Description：
 */
public class HomeAreaData {

	private String title;
	private String image;
	private String url;
	private String id;

	private List<SSVideoList> listscroll;
	private List<HomeVideoList> listh;
	private List<HomeVideoList> lists;
	private List<HomeRecoSSubject> topiclist;

	public HomeAreaData() {
		super();
	}

	public HomeAreaData(String title, String image, String url, String id,
			List<SSVideoList> listscroll, List<HomeVideoList> listh,
			List<HomeVideoList> lists, List<HomeRecoSSubject> topiclist) {
		super();
		this.title = title;
		this.image = image;
		this.url = url;
		this.id = id;
		this.listscroll = listscroll;
		this.listh = listh;
		this.lists = lists;
		this.topiclist = topiclist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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

	public List<SSVideoList> getListscroll() {
		return listscroll;
	}

	public void setListscroll(List<SSVideoList> listscroll) {
		this.listscroll = listscroll;
	}

	public List<HomeVideoList> getListh() {
		return listh;
	}

	public void setListh(List<HomeVideoList> listh) {
		this.listh = listh;
	}

	public List<HomeVideoList> getLists() {
		return lists;
	}

	public void setLists(List<HomeVideoList> lists) {
		this.lists = lists;
	}

	public List<HomeRecoSSubject> getTopiclist() {
		return topiclist;
	}

	public void setTopiclist(List<HomeRecoSSubject> topiclist) {
		this.topiclist = topiclist;
	}

}
