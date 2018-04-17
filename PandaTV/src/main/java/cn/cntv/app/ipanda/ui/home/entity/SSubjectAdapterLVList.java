package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

import cn.cntv.app.ipanda.constant.SSubjectTypeEnum;


/**
 * @ClassName: SSubjectAdapterLVList
 * @author Xiao JinLai
 * @Date Dec 31, 2015 10:44:07 PM
 * @Description：Home special subject adapter live video list
 */
public class SSubjectAdapterLVList implements AdapterData {

	private List<SSLiveVideoList> liveVideoLists;

	public SSubjectAdapterLVList() {
		super();
	}

	public SSubjectAdapterLVList(List<SSLiveVideoList> liveVideoLists) {
		super();
		this.liveVideoLists = liveVideoLists;
	}

	public List<SSLiveVideoList> getLiveVideoLists() {
		return liveVideoLists;
	}

	public void setLiveVideoLists(List<SSLiveVideoList> liveVideoLists) {
		this.liveVideoLists = liveVideoLists;
	}

	@Override
	public int getAdapterType() {
		return SSubjectTypeEnum.SSUBJECT_LIVE_LIST.value();
	}

}
