package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

import cn.cntv.app.ipanda.constant.SSubjectTypeEnum;

/**
 * @ClassName: SSVideoList1
 * @author Xiao JinLai
 * @Date Dec 31, 2015 8:35:21 PM
 * @Description：Home special subject list1
 */
public class SSVideoList1 implements AdapterData {

	private List<SSVideoList> videoLists1;

	public SSVideoList1() {
		super();
	}

	public SSVideoList1(List<SSVideoList> videoLists1) {
		super();
		this.videoLists1 = videoLists1;
	}

	public List<SSVideoList> getVideoLists1() {
		return videoLists1;
	}

	public void setVideoLists1(List<SSVideoList> videoLists1) {
		this.videoLists1 = videoLists1;
	}

	@Override
	public int getAdapterType() {

		return SSubjectTypeEnum.SSUBJECT_LIST1.value();
	}

}
