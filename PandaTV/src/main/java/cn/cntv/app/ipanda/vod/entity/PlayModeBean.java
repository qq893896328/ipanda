package cn.cntv.app.ipanda.vod.entity;

public class PlayModeBean {

	/*
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 117603458854093358L;
	private String title;
	private boolean isChecked;
	private String playUrl;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public String getPlayUrl() {
		return playUrl;
	}
	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}
	@Override
	public String toString() {
		return "PlayModeBean [title=" + title + ", isChecked=" + isChecked
				+ ", playUrl=" + playUrl + "]";
	}
	
	
	
	
	
}
