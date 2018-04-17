package cn.cntv.app.ipanda.constant;

/**
 * @ClassName: HomeAdapterTypeEnum
 * @author Xiao JinLai
 * @Date Dec 25, 2015 10:37:38 PM
 * @Description：Home adapter type enum
 */
public enum HomeTypeEnum {

	GROUP_TYPE(0),

	BIGIMG_TYPE(1),

	LIVE_TYPE(2),

	INTERACTIVEONE_TYPE(3),

	INTERACTIVETWO_TYPE(4),

	HOME_VIDEOLIST1_TYPE(5),

	HOME_VIDEOLIST2_TYPE(6),

	HOME_SSUBJECT_TYPE(7),

	HOME_PANDAEYE_TYPE(8),

	HOME_PANDAEYE2_TYPE(9),

	HOME_PANDALIVE_TYPE(10),

	HOME_STYLE1_TYPE(11),

	HOME_STYLE2_TYPE(12),

	HOME_AREA_TYPE(13),

	HOME_GREATWALL_TYPE(14),

	HOME_LIVECHINA_TYPE(15),

	HOME_LIVE_TYPE(16),

	HOME_LIVE2_TYPE(17),

	HOME_ADAPTER_COUNT(18); // Home type count

	private int mCode;

	private HomeTypeEnum(int code) {

		this.mCode = code;
	}

	public int value() {

		return this.mCode;
	}
}
