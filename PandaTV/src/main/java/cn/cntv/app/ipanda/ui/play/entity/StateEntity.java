package cn.cntv.app.ipanda.ui.play.entity;

public class StateEntity {

	private int sign;
	private String playUrl;
	private String title;
	private String check;
	
	
	
	
	
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getSign() {
		return sign;
	}
	public void setSign(int sign) {
		this.sign = sign;
	}
	public String getPlayUrl() {
		return playUrl;
	}
	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}
	@Override
	public String toString() {
		return "StateEntity [sign=" + sign + ", playUrl=" + playUrl
				+ ", title=" + title + ", check=" + check + "]";
	}
	
	
	
	
	
	
	
	
	
	
	
}
