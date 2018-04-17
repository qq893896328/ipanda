package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

/**
 * @ClassName: HomeData
 * @author Xiao JinLai
 * @Date Dec 25, 2015 10:51:37 AM
 * @Description：home data
 */
public class HomeData {

	private List<BigImg> bigImg;
	private HomeAreaData area;
	private HomePandaEye pandaeye;
	private HomeHotLive pandalive;
	private HomeHotLive walllive;
	private HomeHotLive chinalive;
	private HomeInterData interactive;
	private HomeLiveData cctv;
	private List<HomeStyle> list;

	public HomeData() {
		super();
	}

	public HomeData(List<BigImg> bigImg, HomeAreaData area,
			HomePandaEye pandaeye, HomeHotLive pandalive, HomeHotLive walllive,
			HomeHotLive chinalive, HomeInterData interactive,
			HomeLiveData cctv, List<HomeStyle> list) {
		super();
		this.bigImg = bigImg;
		this.area = area;
		this.pandaeye = pandaeye;
		this.pandalive = pandalive;
		this.walllive = walllive;
		this.chinalive = chinalive;
		this.interactive = interactive;
		this.cctv = cctv;
		this.list = list;
	}

	public List<BigImg> getBigImg() {
		return bigImg;
	}

	public void setBigImg(List<BigImg> bigImg) {
		this.bigImg = bigImg;
	}

	public HomeAreaData getArea() {
		return area;
	}

	public void setArea(HomeAreaData area) {
		this.area = area;
	}

	public HomePandaEye getPandaeye() {
		return pandaeye;
	}

	public void setPandaeye(HomePandaEye pandaeye) {
		this.pandaeye = pandaeye;
	}

	public HomeHotLive getPandalive() {
		return pandalive;
	}

	public void setPandalive(HomeHotLive pandalive) {
		this.pandalive = pandalive;
	}

	public HomeHotLive getWalllive() {
		return walllive;
	}

	public void setWalllive(HomeHotLive walllive) {
		this.walllive = walllive;
	}

	public HomeHotLive getChinalive() {
		return chinalive;
	}

	public void setChinalive(HomeHotLive chinalive) {
		this.chinalive = chinalive;
	}

	public HomeInterData getInteractive() {
		return interactive;
	}

	public void setInteractive(HomeInterData interactive) {
		this.interactive = interactive;
	}

	public HomeLiveData getCctv() {
		return cctv;
	}

	public void setCctv(HomeLiveData cctv) {
		this.cctv = cctv;
	}

	public List<HomeStyle> getList() {
		return list;
	}

	public void setList(List<HomeStyle> list) {
		this.list = list;
	}

}
