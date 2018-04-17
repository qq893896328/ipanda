package cn.cntv.app.ipanda.ui.pandaeye.entity;

import java.io.Serializable;

public class PEListDetailImgs  implements Serializable{

	private String imgUrl1;
	private String imgUrl3;
	private String imgUrl2;
	public String getImgUrl1() {
		return imgUrl1;
	}
	public void setImgUrl1(String imgUrl1) {
		this.imgUrl1 = imgUrl1;
	}
	public String getImgUrl3() {
		return imgUrl3;
	}
	public void setImgUrl3(String imgUrl3) {
		this.imgUrl3 = imgUrl3;
	}
	public String getImgUrl2() {
		return imgUrl2;
	}
	public void setImgUrl2(String imgUrl2) {
		this.imgUrl2 = imgUrl2;
	}
	@Override
	public String toString() {
		return "PEListDetailImgs [imgUrl1=" + imgUrl1 + ", imgUrl3=" + imgUrl3
				+ ", imgUrl2=" + imgUrl2 + "]";
	}
	
	
	
	
	
}
