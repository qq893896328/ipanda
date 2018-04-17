package cn.cntv.app.ipanda.constant;

/**
 * @author: Xiao JinLai
 * @Date: 2016-04-28 20:43
 * @Description:
 */
public enum UMengEnum {

    WX_APP_ID("wx6d326b2506cc7ae1"),

    WX_APP_SECRET("a3d7eee3316d3d86ccb616306d42a1e7");

    private String mCode;

    UMengEnum(String code) {

        this.mCode = code;
    }

    @Override
    public String toString() {
        return String.valueOf(this.mCode);
    }
}
