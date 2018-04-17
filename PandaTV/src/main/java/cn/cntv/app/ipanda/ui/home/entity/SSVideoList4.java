package cn.cntv.app.ipanda.ui.home.entity;

import cn.cntv.app.ipanda.constant.SSubjectTypeEnum;

/**
 * @ClassName: SSVideoList4
 * @author Xiao JinLai
 * @Date Dec 31, 2015 8:35:21 PM
 * @Description：Home special subject list4
 */
public class SSVideoList4 implements AdapterData {

	private SSVideoList videoList4;

	public SSVideoList4() {
		super();
	}

	public SSVideoList4(SSVideoList videoList4) {
		super();
		this.videoList4 = videoList4;
	}

	public SSVideoList getVideoList4() {
		return videoList4;
	}

	public void setVideoList4(SSVideoList videoList4) {
		this.videoList4 = videoList4;
	}

	@Override
	public int getAdapterType() {

		return SSubjectTypeEnum.SSUBJECT_LIST4.value();
	}

}
