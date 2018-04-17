package cn.cntv.app.ipanda.ui.personal.entity;

public class PIUpdateData {

	private String status;
	private PIUpdateModel data;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public PIUpdateModel getData() {
		return data;
	}
	public void setData(PIUpdateModel data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "PIUpdateData [status=" + status + ", data=" + data + "]";
	}
	
	
	
	
	
}
