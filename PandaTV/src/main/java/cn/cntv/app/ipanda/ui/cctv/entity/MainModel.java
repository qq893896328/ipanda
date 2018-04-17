package cn.cntv.app.ipanda.ui.cctv.entity;

public class MainModel {

	private String title;
	private String noimage;
	private String image;
	private String url;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNoimage() {
		return noimage;
	}
	public void setNoimage(String noimage) {
		this.noimage = noimage;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "MainModel [title=" + title + ", noimage=" + noimage
				+ ", image=" + image + ", url=" + url + "]";
	}
	
	
	
	
}
