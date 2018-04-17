package cn.cntv.app.ipanda.ui.pandaeye.entity;

import java.util.List;

public class ImageData {

	private List<LImageModel> data;

	public List<LImageModel> getData() {
		return data;
	}

	public void setData(List<LImageModel> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ImageData [data=" + data + "]";
	}
	
	
	
	
}
