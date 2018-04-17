package cn.cntv.app.ipanda.ui.pandaeye.activity;

import java.io.Serializable;

public class CeLuePlayerModel implements Serializable{

	private String url;
	private String version;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@Override
	public String toString() {
		return "CeLuePlayerModel [url=" + url + ", version=" + version + "]";
	}
	
	
	
}
