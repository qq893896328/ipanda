package cn.cntv.app.ipanda.constant;

/**
 * @ClassName: HomeTabIndex
 * @author Xiao JinLai
 * @Date Dec 25, 2015 8:53:25 PM
 * @Description：首页选项卡索引下标
 */
public enum ErrorEnum {

	HOME_DATA_ERROR("视频地址不存在"),

	VOTE_VALUE_NULL("您还没有选择投票选项，请选择后提交并查看结果"),

	VOTE_VALUE_DELETION("您还有投票没有选择，请选择后继续投票并查看结果"),

	VOTE_OK("投票成功，感谢您的参与！正在跳转，请稍候...");

	private String mCode;

	private ErrorEnum(String code) {

		this.mCode = code;
	}

	@Override
	public String toString() {
		return String.valueOf(this.mCode);
	}
}
