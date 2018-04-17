package cn.cntv.app.ipanda.constant;

/**
 * @ClassName: HomeAdapterTypeEnum
 * @author Xiao JinLai
 * @Date Dec 25, 2015 10:37:38 PM
 * @Description：Home adapter vote type enum
 */
public enum JMPType2Enum {

	TELETEXT("ARTI"),

	VIDEO_VALUE("VIDE"),

	VIDEO_LIST_VALUE("VIDA");

	private String mCode;

	private JMPType2Enum(String code) {

		this.mCode = code;
	}

	@Override
	public String toString() {

		return String.valueOf(this.mCode);
	}
}
