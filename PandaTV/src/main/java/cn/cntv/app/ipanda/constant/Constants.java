package cn.cntv.app.ipanda.constant;

public class Constants {
	public static final String SERVICEID = "panda";// 接口文档标识为应用ID
	public static final String PAGESIZE = "6";// 列表每页加载的数量
	public static final String RESPONSE_STATUS_SUCCESS = "success";
	public static final String RESPONSE_MSG_OK = "ok";
	public static String DOWNLOAD_URL = "";// 应用的下载地址
	public static String cache = "/data/data/cn.cntv.app.ipanda/cache";
	public static String tSinaAppId = "725697741"; //   3903945792
	public static String SINA_APP_SECRET = "a525af32e29def84e55446eeeba4a900 "; //  529304d23deb09ef451a095920ce8d15
	public static String tSinaRedirectUrl = "http://www.ipanda.com";
	public static String tSinaScope =
			"email,direct_messages_read,direct_messages_write,"
					+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
					+ "follow_app_official_microblog,"
					+ "invitation_write";

	/** 成功 */
	public static final int CODE_SUCCEED 	= 0;
	/** 失败 */
	public static final int CODE_FAILED		= 1;

	/** 用户未登录 */
	public static final int CODE_USER_NOT_LOGGED	= -100;

}
