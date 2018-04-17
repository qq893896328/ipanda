package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

import cn.cntv.app.ipanda.constant.VoteTypeEnum;

/**
 * @ClassName: VoteOption
 * @author Xiao JinLai
 * @Date Jan 15, 2016 3:47:12 PM
 * @Description：Vote adapter option data class
 */
public class VoteOption implements AdapterData {

	private int key;
	private String id;
	private List<VoteSGData> voteSGData;

	public VoteOption() {
		super();
	}

	public VoteOption(int key, String id, List<VoteSGData> voteSGData) {
		super();
		this.key = key;
		this.id = id;
		this.voteSGData = voteSGData;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<VoteSGData> getVoteSGData() {
		return voteSGData;
	}

	public void setVoteSGData(List<VoteSGData> voteSGData) {
		this.voteSGData = voteSGData;
	}

	@Override
	public int getAdapterType() {

		return VoteTypeEnum.VOTE_OPTION.value();
	}

}
