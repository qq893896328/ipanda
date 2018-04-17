package cn.cntv.app.ipanda.ui.cctv.entity;

import java.io.Serializable;
import java.util.List;

public class CCTVData implements Serializable{

	private List<CCTVVideo> video;

	public List<CCTVVideo> getVideo() {
		return video;
	}

	public void setVideo(List<CCTVVideo> video) {
		this.video = video;
	}

	@Override
	public String toString() {
		return "CCTVData [video=" + video + "]";
	}
	
	
	
	
}
