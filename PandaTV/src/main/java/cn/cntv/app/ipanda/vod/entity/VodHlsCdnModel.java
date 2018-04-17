package cn.cntv.app.ipanda.vod.entity;

public class VodHlsCdnModel {

	private String cdn_code;
	private String cdn_vip;
	private String cdn_name;
	public String getCdn_code() {
		return cdn_code;
	}
	public void setCdn_code(String cdn_code) {
		this.cdn_code = cdn_code;
	}
	public String getCdn_vip() {
		return cdn_vip;
	}
	public void setCdn_vip(String cdn_vip) {
		this.cdn_vip = cdn_vip;
	}
	public String getCdn_name() {
		return cdn_name;
	}
	public void setCdn_name(String cdn_name) {
		this.cdn_name = cdn_name;
	}
	@Override
	public String toString() {
		return "VodHlsCdnModel [cdn_code=" + cdn_code + ", cdn_vip=" + cdn_vip
				+ ", cdn_name=" + cdn_name + "]";
	}
	
	
	
	
	
	
}
