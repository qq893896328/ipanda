package cn.cntv.app.ipanda.ui.pandaeye.entity;
import java.io.Serializable;
import java.util.List;
public class PEListData implements Serializable{

	private String total;
	private List<PEListDetail> list;
	private String serverTime;
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public List<PEListDetail> getList() {
		return list;
	}
	public void setList(List<PEListDetail> list) {
		this.list = list;
	}
	public String getServerTime() {
		return serverTime;
	}
	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}
	@Override
	public String toString() {
		return "PEListData [total=" + total + ", list=" + list
				+ ", serverTime=" + serverTime + "]";
	}
	
	
	
	
	
	
	
}
