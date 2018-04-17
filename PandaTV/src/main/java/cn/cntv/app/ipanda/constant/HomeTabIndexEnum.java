package cn.cntv.app.ipanda.constant;

/**
 * @ClassName: HomeTabIndex
 * @author Xiao JinLai
 * @Date Dec 25, 2015 8:53:25 PM
 * @Description：首页选项卡索引下标
 */
public enum HomeTabIndexEnum {

	TAB_HOME_INDEX("tab0"),

	TAB_HOME_PANDA_LIVE("tab1"),

	TAB_HOME_LIVE_CHINA("tab2"),

	TAB_HOME_PANDA_CULTURE("tab3"),

	TAB_HOME_PANDA_EYE("tab4");

	private String mCode;

	HomeTabIndexEnum(String code) {

		this.mCode = code;
	}

	@Override
	public String toString() {
		return String.valueOf(this.mCode);
	}
}
