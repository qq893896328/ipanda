package cn.cntv.app.ipanda.ui.lovepanda.entity;

import java.util.List;

public class LpanaTalkData {
	private String total;
	private List<LpandaTalkContentInfo> content;
	public List<LpandaTalkContentInfo> getContent() {
		return content;
	}
	public String getTotal() {
		return total;
	}
	public void setContent(List<LpandaTalkContentInfo> content) {
		this.content = content;
	}
	public void setTotal(String total) {
		this.total = total;
	}
}
