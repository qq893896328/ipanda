package cn.cntv.app.ipanda.constant;

/**
 * @author pj
 * @Date： 2016年1月11日 上午10:12:57
 * @Description:收藏类型枚举类
 */
public enum CollectTypeEnum {

	SP(1), // 视频

	PD(3), // 频道

	HK(4), // 回看

	TW(5), // 图文

	TJ(6), // 图集

	LkTW(7); // 本地图文

	private int mCode;

	private CollectTypeEnum(int code) {

		this.mCode = code;
	}

	public int value() {

		return this.mCode;
	}
}
