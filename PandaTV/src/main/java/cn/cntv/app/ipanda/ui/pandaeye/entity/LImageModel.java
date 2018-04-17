package cn.cntv.app.ipanda.ui.pandaeye.entity;

public class LImageModel {

	
	private String phoneImg;
	private String phone5Img;
	private String contentUrl;
	private String padImg;
	public String getPhoneImg() {
		return phoneImg;
	}
	public void setPhoneImg(String phoneImg) {
		this.phoneImg = phoneImg;
	}
	public String getPhone5Img() {
		return phone5Img;
	}
	public void setPhone5Img(String phone5Img) {
		this.phone5Img = phone5Img;
	}
	public String getContentUrl() {
		return contentUrl;
	}
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	public String getPadImg() {
		return padImg;
	}
	public void setPadImg(String padImg) {
		this.padImg = padImg;
	}
	@Override
	public String toString() {
		return "LImageModel [phoneImg=" + phoneImg + ", phone5Img=" + phone5Img
				+ ", contentUrl=" + contentUrl + ", padImg=" + padImg + "]";
	}
	
	
	
	
	
}
