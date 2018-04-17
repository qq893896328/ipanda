package cn.cntv.app.ipanda.ui.pandaeye.entity;

import java.util.List;

public class PandaEyeHomeData {

	private List<PandaEyeTopData> bigImg;
	private String listurl;
	public List<PandaEyeTopData> getBigImg() {
		return bigImg;
	}
	public void setBigImg(List<PandaEyeTopData> bigImg) {
		this.bigImg = bigImg;
	}
	public String getListurl() {
		return listurl;
	}
	public void setListurl(String listurl) {
		this.listurl = listurl;
	}
	@Override
	public String toString() {
		return "PandaEyeHomeData [bigImg=" + bigImg + ", listurl=" + listurl
				+ "]";
	}
	
	
	
	
}
