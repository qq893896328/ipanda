package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

/**
 * @ClassName: VoteSingleGroup
 * @author Xiao JinLai
 * @Date 2016-1-29 下午7:41:10
 * @Description：投票单组剩余次数类
 */
public class VoteSingleGroup {

	private int code;
	private String msg;
	private List<VoteGetData> data;

	public VoteSingleGroup() {
		super();
	}

	public VoteSingleGroup(int code, String msg, List<VoteGetData> data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
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

	public List<VoteGetData> getData() {
		return data;
	}

	public void setData(List<VoteGetData> data) {
		this.data = data;
	}

}
