package cn.cntv.app.ipanda.constant;

/**
 * @ClassName: HomeAdapterTypeEnum
 * @author Xiao JinLai
 * @Date Dec 25, 2015 10:37:38 PM
 * @Description：Home adapter vote type enum
 */
public enum JMPTypeEnum {

	VIDEO_TYPE(0),

	VIDEO_LIVE(1),

	VIDEO_VOD(2),

	VIDEO_LIST(3),

	VIDEO_SSUBJECT(4),

	VIDEO_BODY(5),

	VIDEO_BODY6(6),

	JMP_CCTV(6);

	private int mCode;

	private JMPTypeEnum(int code) {

		this.mCode = code;
	}

	public int value() {

		return this.mCode;
	}

	@Override
	public String toString() {

		return String.valueOf(this.mCode);
	}
}
