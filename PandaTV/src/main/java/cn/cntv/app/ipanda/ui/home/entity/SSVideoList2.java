package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

import cn.cntv.app.ipanda.constant.SSubjectTypeEnum;

/**
 * @ClassName: SSVideoList2
 * @author Xiao JinLai
 * @Date Dec 31, 2015 8:35:21 PM
 * @Description：Home special subject list2
 */
public class SSVideoList2 implements AdapterData {

	private List<SSVideoList> lists2;

	public SSVideoList2() {
		super();
	}

	public SSVideoList2(List<SSVideoList> lists2) {
		super();
		this.lists2 = lists2;
	}

	public List<SSVideoList> getLists2() {
		return lists2;
	}

	public void setLists2(List<SSVideoList> lists2) {
		this.lists2 = lists2;
	}

	@Override
	public int getAdapterType() {

		return SSubjectTypeEnum.SSUBJECT_LIST2.value();
	}

}
