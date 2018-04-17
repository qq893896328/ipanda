package cn.cntv.app.ipanda.ui.cctv.entity;

import java.util.List;

public class MainData {

	private List<MainModel> tab ;

	public List<MainModel> getTab() {
		return tab;
	}

	public void setTab(List<MainModel> tab) {
		this.tab = tab;
	}

	@Override
	public String toString() {
		return "MainData [tab=" + tab + "]";
	}
	
	
	
}
