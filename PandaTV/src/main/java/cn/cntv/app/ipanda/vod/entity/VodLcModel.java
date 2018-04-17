package cn.cntv.app.ipanda.vod.entity;

public class VodLcModel {

	private String city_code;
	private String country_code;
	private String ip;
	private String provice_code;
	private String isp_code;
	public String getCity_code() {
		return city_code;
	}
	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getProvice_code() {
		return provice_code;
	}
	public void setProvice_code(String provice_code) {
		this.provice_code = provice_code;
	}
	public String getIsp_code() {
		return isp_code;
	}
	public void setIsp_code(String isp_code) {
		this.isp_code = isp_code;
	}
	@Override
	public String toString() {
		return "VodLcModel [city_code=" + city_code + ", country_code="
				+ country_code + ", ip=" + ip + ", provice_code="
				+ provice_code + ", isp_code=" + isp_code + "]";
	}
	
	
	
	
}
