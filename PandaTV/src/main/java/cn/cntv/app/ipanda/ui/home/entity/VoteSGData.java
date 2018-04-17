package cn.cntv.app.ipanda.ui.home.entity;

/**
 * @ClassName: VoteSGData
 * @author Xiao JinLai
 * @Date Jan 15, 2016 2:33:34 PM
 * @Description：Home vote single group data class
 */
public class VoteSGData {

	private String id;
	private String vid;
	private String title;
	private int voted_cnt;
	private boolean isCheck;

	public VoteSGData() {
		super();
	}

	public VoteSGData(String id, String vid, String title, int voted_cnt,
			boolean isCheck) {
		super();
		this.id = id;
		this.vid = vid;
		this.title = title;
		this.voted_cnt = voted_cnt;
		this.isCheck = isCheck;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getVoted_cnt() {
		return voted_cnt;
	}

	public void setVoted_cnt(int voted_cnt) {
		this.voted_cnt = voted_cnt;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

}
