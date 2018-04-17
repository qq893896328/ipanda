package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

import cn.cntv.app.ipanda.constant.HomeTypeEnum;

/**
 * @ClassName: HomePandaEye
 * @author Xiao JinLai
 * @Date Dec 28, 2015 5:26:59 PM
 * @Description：Home panda eye data class
 */
public class HomePandaEye implements AdapterData {

	private String title;
	private String pandaeyelogo;
	private List<HomePandaEyeItem> items;
	private String pandaeyelist;

	public HomePandaEye() {
		super();
	}

	public HomePandaEye(String title, String pandaeyelogo,
			List<HomePandaEyeItem> items, String pandaeyelist) {
		super();
		this.title = title;
		this.pandaeyelogo = pandaeyelogo;
		this.items = items;
		this.pandaeyelist = pandaeyelist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPandaeyelogo() {
		return pandaeyelogo;
	}

	public void setPandaeyelogo(String pandaeyelogo) {
		this.pandaeyelogo = pandaeyelogo;
	}

	public List<HomePandaEyeItem> getItems() {
		return items;
	}

	public void setItems(List<HomePandaEyeItem> items) {
		this.items = items;
	}

	public String getPandaeyelist() {
		return pandaeyelist;
	}

	public void setPandaeyelist(String pandaeyelist) {
		this.pandaeyelist = pandaeyelist;
	}

	@Override
	public int getAdapterType() {

		return HomeTypeEnum.HOME_PANDAEYE_TYPE.value();
	}

}
