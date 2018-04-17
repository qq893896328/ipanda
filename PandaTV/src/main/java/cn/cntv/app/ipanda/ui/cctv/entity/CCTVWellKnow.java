package cn.cntv.app.ipanda.ui.cctv.entity;

import java.util.List;

/**
 * @author： pj
 * @Date： 2015年12月30日 下午12:41:30
 * @Description:只为适应接口返回数据格式创建的Bean
 */
public class CCTVWellKnow {

	private List<CCTVBigImgItem> bigImg;
	private List<cn.cntv.app.ipanda.ui.cctv.entity.CCTVWellKnowItem> list;

	public List<CCTVBigImgItem> getBigImg() {
		return bigImg;
	}

	public void setBigImg(List<CCTVBigImgItem> bigImg) {
		this.bigImg = bigImg;
	}

	public void setList(
			List<cn.cntv.app.ipanda.ui.cctv.entity.CCTVWellKnowItem> list) {
		this.list = list;
	}

	public List<cn.cntv.app.ipanda.ui.cctv.entity.CCTVWellKnowItem> getList() {
		return list;
	}

}