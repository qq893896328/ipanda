package cn.cntv.app.ipanda.ui.home.entity;

import cn.cntv.app.ipanda.constant.SSubjectTypeEnum;

/**
 * @ClassName: SSVideoList3
 * @author Xiao JinLai
 * @Date Dec 31, 2015 8:35:21 PM
 * @Description：Home special subject list3
 */
public class SSVideoList3 implements AdapterData {

	private SSVideoList videoList3;

	public SSVideoList3() {
		super();
	}

	public SSVideoList3(SSVideoList videoList3) {
		super();
		this.videoList3 = videoList3;
	}

	public SSVideoList getVideoList3() {
		return videoList3;
	}

	public void setVideoList3(SSVideoList videoList3) {
		this.videoList3 = videoList3;
	}

	@Override
	public int getAdapterType() {

		return SSubjectTypeEnum.SSUBJECT_LIST3.value();
	}

}
