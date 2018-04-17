package cn.cntv.app.ipanda.ui.home.entity;

import cn.cntv.app.ipanda.constant.SSubjectTypeEnum;

/**
 * @ClassName: SSubjectVote
 * @author Xiao JinLai
 * @Date Jan 22, 2016 4:09:23 PM
 * @Description：
 */
public class SSubjectVote implements AdapterData {

	private String title;
	private SSubjectVoteItem vote;

	public SSubjectVote() {
		super();
	}

	public SSubjectVote(String title, SSubjectVoteItem vote) {
		super();
		this.title = title;
		this.vote = vote;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public SSubjectVoteItem getVote() {
		return vote;
	}

	public void setVote(SSubjectVoteItem vote) {
		this.vote = vote;
	}

	@Override
	public int getAdapterType() {

		return SSubjectTypeEnum.SSUBJECT_VOTEURL.value();
	}

}
