package cn.cntv.app.ipanda.ui.cctv.entity;

import java.util.List;

/**
 * @author： pj
 * @Date： 2015年12月30日 下午12:41:03
 * @Description:只为适应接口返回数据格式创建的Bean
 */
public class CCTVBigImg {

	private List<CCTVBigImgItem> items;

	public void setItems(List<CCTVBigImgItem> items) {
		this.items = items;
	}

	public List<CCTVBigImgItem> getItems() {
		return items;
	}

}