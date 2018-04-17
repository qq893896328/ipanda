package cn.cntv.app.ipanda.ui.home.entity;

/**
 * @ClassName: VoteCode
 * @author Xiao JinLai
 * @Date Jan 21, 2016 3:44:57 PM
 * @Description：投票状态类
 */
public class VoteCode {

	private int code;
	private String msg;

	public VoteCode() {
		super();
	}

	public VoteCode(int code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
