package cn.cntv.app.ipanda.ui.pandaeye.activity;

import java.io.Serializable;
import java.util.List;

public class CeLueChannelInfoModel implements Serializable{

	private String db;
	private List<String> lb_area;
	private String audio;
	private String dbandroid;
	private List<String> multi_area;
	private String sd;
	private String lb;
	private String channel;
	private String p2p;
	private String Priority;
	private List<String> db_area;
	public String getDb() {
		return db;
	}
	public void setDb(String db) {
		this.db = db;
	}
	public List<String> getLb_area() {
		return lb_area;
	}
	public void setLb_area(List<String> lb_area) {
		this.lb_area = lb_area;
	}
	public String getAudio() {
		return audio;
	}
	public void setAudio(String audio) {
		this.audio = audio;
	}
	public String getDbandroid() {
		return dbandroid;
	}
	public void setDbandroid(String dbandroid) {
		this.dbandroid = dbandroid;
	}
	public List<String> getMulti_area() {
		return multi_area;
	}
	public void setMulti_area(List<String> multi_area) {
		this.multi_area = multi_area;
	}
	public String getSd() {
		return sd;
	}
	public void setSd(String sd) {
		this.sd = sd;
	}
	public String getLb() {
		return lb;
	}
	public void setLb(String lb) {
		this.lb = lb;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getP2p() {
		return p2p;
	}
	public void setP2p(String p2p) {
		this.p2p = p2p;
	}
	public String getPriority() {
		return Priority;
	}
	public void setPriority(String priority) {
		Priority = priority;
	}
	public List<String> getDb_area() {
		return db_area;
	}
	public void setDb_area(List<String> db_area) {
		this.db_area = db_area;
	}
	@Override
	public String toString() {
		return "CeLueChannelInfoModel [db=" + db + ", lb_area=" + lb_area
				+ ", audio=" + audio + ", dbandroid=" + dbandroid
				+ ", multi_area=" + multi_area + ", sd=" + sd + ", lb=" + lb
				+ ", channel=" + channel + ", p2p=" + p2p + ", Priority="
				+ Priority + ", db_area=" + db_area + "]";
	}
	
	
	
	
}
