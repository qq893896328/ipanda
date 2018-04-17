package cn.cntv.app.ipanda.constant;

/**
 * @ClassName: HomeAdapterTypeEnum
 * @author Xiao JinLai
 * @Date Dec 25, 2015 10:37:38 PM
 * @Description：Home adapter type enum
 */
public enum SSubjectTypeEnum {

	GROUP_TYPE(0),

	BIGIMG_TYPE(1),

	LIVE_TYPE(2),

	INTERACTIVEONE_TYPE(3),

	INTERACTIVETWO_TYPE(4),

	SSUBJECT_BANNER(5),

	SSUBJECT_LIVE_LIST(6),

	SSUBJECT_LIST1(7),

	SSUBJECT_LIST2(8),

	SSUBJECT_LIST3(9),

	SSUBJECT_LIST4(10),

	SSUBJECT_VOTEURL(11),

	SSUBJECT_ADAPTER_COUNT(12);

	private int mCode;

	private SSubjectTypeEnum(int code) {

		this.mCode = code;
	}

	public int value() {

		return this.mCode;
	}
}
