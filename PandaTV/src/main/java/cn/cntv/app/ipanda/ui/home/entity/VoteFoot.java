package cn.cntv.app.ipanda.ui.home.entity;

import android.util.SparseArray;

import cn.cntv.app.ipanda.constant.VoteTypeEnum;

/**
 * @ClassName: VoteFoot
 * @author Xiao JinLai
 * @Date Jan 15, 2016 3:55:14 PM
 * @Description：Vote adapter foot data class
 */
public class VoteFoot implements AdapterData {

	private SparseArray<String> commit;

	public VoteFoot() {
		super();
	}

	public VoteFoot(SparseArray<String> commit) {
		super();
		this.commit = commit;
	}

	public SparseArray<String> getCommit() {
		return commit;
	}

	public void setCommit(SparseArray<String> commit) {
		this.commit = commit;
	}

	@Override
	public int getAdapterType() {
		return VoteTypeEnum.VOTE_FOOT.value();
	}

}
