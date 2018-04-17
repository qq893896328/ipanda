package cn.cntv.app.ipanda.constant;

/**
 * @author： pj
 * @Date： 2016年1月23日 下午3:03:44
 * @Description:添加收藏的页面来源枚举
 */
public enum CollectPageSourceEnum {
	XMZB(0), // 熊猫直播

	ZBZG(1), // 直播中国

	PDZB(2), // 频道直播
	
	YSML(3); // 央视名栏

	private int mCode;

	private CollectPageSourceEnum(int code) {

		this.mCode = code;
	}

	public int value() {

		return this.mCode;
	}

}
