package cn.cntv.app.ipanda.ui.cctv.entity;

import java.util.List;

/**
 * @author： pj
 * @Date： 2015年12月30日 下午12:39:09
 * @Description:只为适应接口返回数据格式创建的Bean
 */
public class CCTVTabtlist {

	private List<CCTVTabItem> items;

	public void setItems(List<CCTVTabItem> items) {
		this.items = items;
	}

	public List<CCTVTabItem> getItems() {
		return items;
	}

}