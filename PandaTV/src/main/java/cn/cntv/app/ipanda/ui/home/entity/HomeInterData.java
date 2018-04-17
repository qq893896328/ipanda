package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

/**
 * @ClassName: HomeInterData
 * @author Xiao JinLai
 * @Date Jan 14, 2016 4:24:58 PM
 * @Description：
 */
public class HomeInterData {

	private String title;

	private List<Interaction> interactiveone;
	private List<Interaction> interactivetwo;

	public HomeInterData() {
		super();
	}

	public HomeInterData(String title, List<Interaction> interactiveone,
			List<Interaction> interactivetwo) {
		super();
		this.title = title;
		this.interactiveone = interactiveone;
		this.interactivetwo = interactivetwo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Interaction> getInteractiveone() {
		return interactiveone;
	}

	public void setInteractiveone(List<Interaction> interactiveone) {
		this.interactiveone = interactiveone;
	}

	public List<Interaction> getInteractivetwo() {
		return interactivetwo;
	}

	public void setInteractivetwo(List<Interaction> interactivetwo) {
		this.interactivetwo = interactivetwo;
	}

}
