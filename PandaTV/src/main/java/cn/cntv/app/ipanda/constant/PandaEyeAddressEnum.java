package cn.cntv.app.ipanda.constant;

/**
 * @ClassName: WebAddressEnum
 * @author Xiao JinLai
 * @Date Dec 28, 2015 6:34:29 PM
 * @Description：Servers address constant class
 */

public enum PandaEyeAddressEnum {
	
	
	
	PE_HOME_ADDRESS(
			"http://www.ipanda.com/kehuduan/PAGE14503485387528442/index.json"),
	ALL_MODEL_URL(
				"http://www.ipanda.com/kehuduan/xzbqy/index.json"),
	PE_LUNCHIMG(
			"http://www.ipanda.com/kehuduan/pdqdt/index.json"),
	PE_UPDATEURL(
					"http://115.182.9.124/index.php?action=release-GetNewVersions"),
	PE_HISTORYDATA(
			"http://history.apps.cntv.cn/interface/serviceformobile.php"),
	
	PE_FEEDBACK_MYQUESTION("http://115.182.9.124/index.php?action=feedbacknew-creat"),
	 
	PE_FEEDBACK_COMMONQUESTION("http://www.ipanda.com/kehuduan/faq/index.json");
	
	private String mCode;

	private PandaEyeAddressEnum(String code) {
		this.mCode = code;
	}

	@Override
	public String toString() {

		return String.valueOf(this.mCode);
	}
}
