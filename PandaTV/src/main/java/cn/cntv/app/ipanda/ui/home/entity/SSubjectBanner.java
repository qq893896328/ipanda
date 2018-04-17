package cn.cntv.app.ipanda.ui.home.entity;

import cn.cntv.app.ipanda.constant.SSubjectTypeEnum;

/**
 * @ClassName: SSubjectBanner
 * @author Xiao JinLai
 * @Date Dec 31, 2015 8:21:02 PM
 * @Description：Home special subject banner class
 */
public class SSubjectBanner implements AdapterData {

	private String bannerimage;

	public SSubjectBanner() {
		super();
	}

	public SSubjectBanner(String bannerimage) {
		super();
		this.bannerimage = bannerimage;
	}

	public String getBannerimage() {
		return bannerimage;
	}

	public void setBannerimage(String bannerimage) {
		this.bannerimage = bannerimage;
	}

	@Override
	public int getAdapterType() {
		return SSubjectTypeEnum.SSUBJECT_BANNER.value();
	}

}
