package cn.cntv.app.ipanda.ui.home.entity;

/**
 * @ClassName: VoteData
 * @author Xiao JinLai
 * @Date Jan 18, 2016 5:26:41 PM
 * @Description：Home vote data
 */
public class VoteData {

	private VoteBody body;

	public VoteData() {
		super();
	}

	public VoteData(VoteBody body) {
		super();
		this.body = body;
	}

	public VoteBody getBody() {
		return body;
	}

	public void setBody(VoteBody body) {
		this.body = body;
	}

}
