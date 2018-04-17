package cn.cntv.app.ipanda.constant;

/**
 * @ClassName: HomeAdapterTypeEnum
 * @author Xiao JinLai
 * @Date Dec 25, 2015 10:37:38 PM
 * @Description：Home adapter vote type enum
 */
public enum VoteTypeEnum {

	VOTE_GROUP(0),

	VOTE_HEAD(1),

	VOTE_OPTION(2),

	VOTE_OPTION_VALUE(3),

	VOTE_FOOT(4),

	VOTE_ADAPTER_COUNT(5);

	private int mCode;

	private VoteTypeEnum(int code) {

		this.mCode = code;
	}

	public int value() {

		return this.mCode;
	}
}
