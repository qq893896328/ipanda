package cn.cntv.app.ipanda.ui.lovepanda.entity;

import java.util.List;

public class LpandaBookmark {

	private List<LpandaMultiple> multiple;
	private List<LpandaWatchtalk> watchTalk;

	public void setMultiple(List<LpandaMultiple> multiple) {
		this.multiple = multiple;
	}

	public List<LpandaMultiple> getMultiple() {
		return multiple;
	}

	public List<LpandaWatchtalk> getWatchTalk() {
		return watchTalk;
	}

	public void setWatchTalk(List<LpandaWatchtalk> watchTalk) {
		this.watchTalk = watchTalk;
	}

}