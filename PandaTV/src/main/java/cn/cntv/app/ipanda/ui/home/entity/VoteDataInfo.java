package cn.cntv.app.ipanda.ui.home.entity;

/**
 * @ClassName: VoteData
 * @author Xiao JinLai
 * @Date Jan 15, 2016 2:58:13 PM
 * @Description：Vote data class
 */
public class VoteDataInfo {

	private int code;
	private String msg;
	private VoteData data;

	public VoteDataInfo() {
		super();
	}

	public VoteDataInfo(int code, String msg, VoteData data) {
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

	public VoteData getData() {
		return data;
	}

	public void setData(VoteData data) {
		this.data = data;
	}

}
