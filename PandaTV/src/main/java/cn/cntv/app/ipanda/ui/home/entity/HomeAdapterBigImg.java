package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

import cn.cntv.app.ipanda.constant.HomeTypeEnum;

/**
 * @ClassName: HomeAdapterBigImg
 * @author Xiao JinLai
 * @Date Dec 29, 2015 2:22:44 PM
 * @Description：Home adapter big image data class
 */
public class HomeAdapterBigImg implements AdapterData {

	private List<BigImg> bigImgs;

	public HomeAdapterBigImg() {
		super();
	}

	public HomeAdapterBigImg(List<BigImg> bigImgs) {
		super();
		this.bigImgs = bigImgs;
	}

	public List<BigImg> getBigImgs() {
		return bigImgs;
	}

	public void setBigImgs(List<BigImg> bigImgs) {
		this.bigImgs = bigImgs;
	}

	@Override
	public int getAdapterType() {

		return HomeTypeEnum.BIGIMG_TYPE.value();
	}

}
