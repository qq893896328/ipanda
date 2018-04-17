package cn.cntv.app.ipanda.ui.cctv.entity;

import java.util.List;
import java.util.Map;

/**
 * @author： pj
 * @Date： 2016年1月4日 下午6:34:16
 * @Description:央视名栏详情页面数据
 */
public class CCTVDtlPageData {

	private Map<String,Object> videoset;
	private List<CCTVVideo> video;
	private CCTVVideosFir vsetInfo;
	private int count;

	public Map<String, Object> getVideoset() {
		return videoset;
	}

	public void setVideoset(Map<String, Object> videoset) {
		this.videoset = videoset;
	}

	public List<CCTVVideo> getVideo() {
		return video;
	}

	public void setVideo(List<CCTVVideo> video) {
		this.video = video;
	}

	public CCTVVideosFir getVsetInfo() {
		return vsetInfo;
	}

	public void setVsetInfo(CCTVVideosFir vsetInfo) {
		this.vsetInfo = vsetInfo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}