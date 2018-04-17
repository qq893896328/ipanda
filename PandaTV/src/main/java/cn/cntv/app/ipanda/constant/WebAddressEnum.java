package cn.cntv.app.ipanda.constant;

/**
 * @ClassName: WebAddressEnum
 * @author Xiao JinLai
 * @Date Dec 28, 2015 6:34:29 PM
 * @Description：Servers address constant class
 */

public enum WebAddressEnum {

	WEB_HOME("首页"),

	WEB_PANDA_EYE("熊猫观察"),

	WEB_PANDA_LIVE("熊猫直播"),

	WEB_LIVE_CHINA("直播中国"),

	WEB_CCTV("CCTV"),
	WEB_CULTYRE("熊猫文化"),

	HOME_ADDRESS(
			"http://www.ipanda.com/kehuduan/PAGE14501749764071042/index.json"),

	HOME_INTERACTION(
			"http://www.ipanda.com/kehuduan/PAGE14501767715521482/index.json"),

	HOME_SSUBJECT(
			"http://www.ipanda.com/kehuduan/PAGE14502569486109162/index.json"),

	HOME_VOTE("http://api.itv.cntv.cn/commonvote/getvote/vid/"),

	HOME_VOTE_GET("http://api.itv.cntv.cn/commonvote/GetUserVote"),

	HOME_COMIT_VOTE("http://api.itv.cntv.cn/commonvote/PostVote"),

	HOME_COMIT_VOTE_STAGE("http://api.itv.cntv.cn/commonvote/PostStageVote"),

	CCTV_TAB("http://www.ipanda.com/kehuduan/PAGE14501777938482582/index.json"),

	PANDA_CULTURE("http://www.ipanda.com/kehuduan/xmwh/index.json"),

	CCTV_DETAIL("http://api.cntv.cn/video/videolistById"),

	CCTV_COLLECT_STATUS("http://col.apps.cntv.cn/api/getstatus"),

	CCTV_COLLECT_ADD("http://col.apps.cntv.cn/api/addCollection"),

	CCTV_COLLECT_CANCEL("http://col.apps.cntv.cn/api/delCollect"),

	CCTV_ZBD("http://api.cntv.cn/epg/nowepg"),
	
	PERSONAL_COLLECT_GET("http://col.apps.cntv.cn/api/getCollect"),

	LPANDA_TAB(
			"http://www.ipanda.com/kehuduan/PAGE14501772263221982/index.json"),

	LPANDA_LIVE(
			"http://www.ipanda.com/kehuduan/PAGE14501769230331752/index.json"),

	LPANDA_COLUMN(
			"http://api.cntv.cn/apicommon/index?path=iphoneInterface/general/getArticleAndVideoListInfo.json&primary_id=PAGE1449807494852603,PAGE1451473625420136&page=1&pageSize=10&serviceId=panda"), LPANDA_SPECIAL(
			"http://www.ipanda.com/kehuduan/PAGE14501769230331752/zhuantishijianzhibo/index.shtml"), LPANDA_COLUMN_FRAGMENT(
			"http://api.cntv.cn/apicommon/index?path=iphoneInterface/general/getArticleAndVideoListInfo.json&primary_id=PAGE1431953341813851&serviceId=panda");

	private String mCode;

	private WebAddressEnum(String code) {
		this.mCode = code;
	}

	@Override
	public String toString() {

		return String.valueOf(this.mCode);
	}
}
