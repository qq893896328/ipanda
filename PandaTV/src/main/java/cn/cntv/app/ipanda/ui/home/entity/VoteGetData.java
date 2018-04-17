package cn.cntv.app.ipanda.ui.home.entity;

/**
 * @ClassName: VoteGet
 * @author Xiao JinLai
 * @Date 2016-1-29 下午7:41:10
 * @Description：投票获取信息次数类
 */
public class VoteGetData {

	private int left_num;

	public VoteGetData() {
		super();
	}

	public VoteGetData(int left_num) {
		super();
		this.left_num = left_num;
	}

	public int getLeft_num() {
		return left_num;
	}

	public void setLeft_num(int left_num) {
		this.left_num = left_num;
	}

}
