package cn.cntv.app.ipanda.ui.home.entity;

/**
 * @ClassName: SSubjectDataInfo
 * @author Xiao JinLai
 * @Date Dec 31, 2015 9:06:07 PM
 * @Description：Home special subject template class
 */
public class SSubjectDataInfo {

	private SSubjectData data;

	public SSubjectDataInfo() {
		super();
	}

	public SSubjectDataInfo(SSubjectData data) {
		super();
		this.data = data;
	}

	public SSubjectData getData() {
		return data;
	}

	public void setData(SSubjectData data) {
		this.data = data;
	}

}
