package cn.cntv.app.ipanda.api;


import java.io.File;

import cn.cntv.app.ipanda.bean.Entity;
import cn.cntv.app.ipanda.bean.LoginBean;
import cn.cntv.app.ipanda.bean.UserBean;
import cn.cntv.app.ipanda.data.net.HttpTools;
import cn.cntv.app.ipanda.data.net.retrofit.ApiConnection;
import cn.cntv.app.ipanda.data.net.retrofit.Call;
import cn.cntv.app.ipanda.data.net.retrofit.HttpParams;
import cn.cntv.app.ipanda.manager.UserManager;
import cn.cntv.app.ipanda.ui.home.entity.AutoSearchListData;
import cn.cntv.app.ipanda.ui.pandaeye.entity.PEArticle;
import cn.cntv.app.ipanda.ui.personal.entity.NickName;
import cn.cntv.app.ipanda.ui.personal.entity.WeiChatUrl;
import rx.Observable;

/**
 * Created by yuerq on 2016/5/20.
 */
public class PandaApi {

    public final static String SEARCH_URL = "http://so.cntv.cn/service/panda.php";

    public final static String BASE_URL = "http://my.cntv.cn/intf/napi/api.php";

    public final static String LOGIN_URL = "https://reg.cntv.cn/login/login.action";

    public final static String VERFIICATION_URL = "http://reg.cntv.cn/simple/verificationCode.action";

    public final static String CNTV_WX_OAUTH_URL = "http://oauth.passport.cntv.cn/OauthClientWeixin/OAuthMobileWeixinServlet.do";

    public final static String REGCLINET_USER_URL = "http://cbox.regclientuser.cntv.cn";

    public final static String ARTICLE_URL = "http://api.cntv.cn/article/contentinfo";

    public static final String CLIENT = "ipanda_mobile";

    public static final String REFERER = "iPanda.Android";

    public static final String USER_AGENT = "CNTV_APP_CLIENT_CYNTV_MOBILE";

    /**
     * 用户登录
     *
     * @param userName
     * @param password
     * @return
     */
    public static Call<LoginBean> login(String userName, String password) {

        HttpParams params = new HttpParams();

        params.putHeaders("Referer", LOGIN_URL);
        params.putHeaders("User-Agent", USER_AGENT);
        params.put("from", LOGIN_URL);
        params.put("service", "client_transaction");
        params.put("username", userName);
        params.put("password", password);

        return HttpTools.get(LOGIN_URL, LoginBean.class, false,  params);
    }

    /**
     * 获取用户昵称和头像
     *
     * @param userId
     */
    public static Call<UserBean> getNickNameAndFace(String userId) {
        HttpParams params = Factory.getAuthParams();
        params.put("client", PandaApi.CLIENT);
        params.put("method", "user.getNickNameAndFace");
        params.put("userid", userId);
        return HttpTools.get(BASE_URL, UserBean.class, params);
    }


    public static Call<NickName> alterNickName(String userId, String nickName) {
        HttpParams params = Factory.getAuthParams();
        params.put("client", CLIENT);
        params.put("method", "user.alterNickName");
        params.put("userid", userId);
        params.put("nickname", nickName);

        return HttpTools.post(BASE_URL, NickName.class, params);
    }

    public static Call<Entity> uploadProfile(String userId, File file) {

        HttpParams params = Factory.getAuthParams();
        params.put("client", CLIENT);
        params.put("method", "user.alterUserFace");
        params.put("userid", userId);
        params.put("facefile", file);

        return HttpTools.post(BASE_URL, Entity.class, params);

    }

    public static Call<WeiChatUrl> getWxOAuthUrl(String code) {

        HttpParams params = new HttpParams();
        params.put("code", code);
        params.put("appid", "wx6d326b2506cc7ae1");
        params.put("from", CLIENT);
        return HttpTools.get(CNTV_WX_OAUTH_URL, WeiChatUrl.class, params);
    }

    public static Call<byte[]> getSecurityCode() {
        return HttpTools.get(VERFIICATION_URL, byte[].class, false, null);
    }

    public static Call<AutoSearchListData> autoSearch(String text) {

        HttpParams parmas = new HttpParams();
        parmas.put("text", text);

        return HttpTools.get(SEARCH_URL, AutoSearchListData.class, parmas);
    }


    public static Call<AutoSearchListData> itemClickSearch(String qtext, int num, int page) {
        //http://so.cntv.cn/service/panda.php?text=%E7%86%8A%E7%8C%AB&num=4
        // &sort=SCORE&format=JSON&page=1&highlight=1&objtype=video&a=query&
        // duration=5&daytime=0
        HttpParams params = new HttpParams();
        params.put("text", qtext);
        params.put("mun", num);
        params.put("sort", "SCORE");
        params.put("objtype", "video");
        params.put("page", page);

        return HttpTools.get(SEARCH_URL, AutoSearchListData.class, params);
    }

    public static Observable<PEArticle> getArticle(String articleId) {
        HttpParams params = new HttpParams();
        params.put("id", articleId);
        params.put("serviceId", "panda");
        return ApiConnection.createGet(PEArticle.class)
                .url(ARTICLE_URL)
                .params(params)
                .requestCall().toObservable();
    }


    public static class Factory {

        public static HttpParams getAuthParams() {
            HttpParams params = new HttpParams();
            params.putHeaders("Referer", "iPanda.Android");
            params.putHeaders("User-Agent", "CNTV_APP_CLIENT_CBOX_MOBILE");
            params.putHeaders("Cookie", "verifycode=" + getVerifyCode());
            return params;
        }


        private static String getVerifyCode() {
            return UserManager.getInstance().getVerifycode();
        }
    }



}
