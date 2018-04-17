package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

import cn.cntv.app.ipanda.constant.HomeTypeEnum;
import cn.cntv.app.ipanda.ui.pandaeye.entity.PandaEyeTopData;

/**
 * @ClassName: HomeAdapterBigImg
 * @author Xiao JinLai
 * @Date Dec 29, 2015 2:22:44 PM
 * @Description：Home adapter big image data class
 */
public class HomeAdapterBigTwoImg implements AdapterData {

	private List<PandaEyeTopData> bigImgs;

	public HomeAdapterBigTwoImg() {
		super();
	}

	public HomeAdapterBigTwoImg(List<PandaEyeTopData> bigImgs) {
		super();
		this.bigImgs = bigImgs;
	}

	public List<PandaEyeTopData> getBigImgs() {
		return bigImgs;
	}

	public void setBigImgs(List<PandaEyeTopData> bigImgs) {
		this.bigImgs = bigImgs;
	}

	@Override
	public int getAdapterType() {

		return HomeTypeEnum.BIGIMG_TYPE.value();
	}

}
