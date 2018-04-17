
package cn.cntv.app.ipanda.wxapi;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gridsum.mobiledissector.MobileAppTracker;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import cn.cntv.app.ipanda.AppContext;
import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.api.PandaApi;
import cn.cntv.app.ipanda.bean.UserBean;
import cn.cntv.app.ipanda.constant.UMengEnum;
import cn.cntv.app.ipanda.constant.Variables;
import cn.cntv.app.ipanda.data.net.HttpTools;
import cn.cntv.app.ipanda.data.net.retrofit.Call;
import cn.cntv.app.ipanda.data.net.retrofit.HttpParams;
import cn.cntv.app.ipanda.data.net.retrofit.Response;
import cn.cntv.app.ipanda.manager.UserManager;
import cn.cntv.app.ipanda.ui.personal.activity.PersonalLoginActivity;
import cn.cntv.app.ipanda.ui.personal.entity.WeiChatID;
import cn.cntv.app.ipanda.ui.personal.entity.WeiChatUrl;
import cn.cntv.app.ipanda.utils.HttpHeaderHelper;
import cn.cntv.app.ipanda.utils.ToastUtil;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class WXEntryActivity extends WXCallbackActivity {

    private String mCode;
    private IWXAPI api;
    private String userSeqId;
    private String userNickName;
    private String mFace;

    public String verifycode = null;

    private UserManager mUserManager = UserManager.getInstance();

    //获取微信nickname成功
    private Boolean mFirstWeiXin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, UMengEnum.WX_APP_ID.toString(), false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onResp(BaseResp resp) {


        int result = 0;

        switch (resp.errCode) {

            case BaseResp.ErrCode.ERR_OK:
                try {

                    if (Variables.LOGIN) {

                        SendAuth.Resp sResp = (SendAuth.Resp) resp;
                        mCode = sResp.code;
                        getUrl();
                        AppContext.getInstance().getSpUtil().saveWeChatCode(sResp.code);
                        result = R.string.errcode_loginsuccess;

                    } else {
                        result = R.string.errcode_success;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }
        if (result != 0 && mFirstWeiXin == true) {

            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

        }
        mFirstWeiXin = false;
        onFinish();
    }

    //利用token获取url
    private void getUrl() {
        Call<WeiChatUrl> call = PandaApi.getWxOAuthUrl(mCode);

        Observable<Response<WeiChatUrl>> observable = call.getResult();
        observable.flatMap(new Func1<Response<WeiChatUrl>, Observable<Response<WeiChatID>>>() {
            @Override
            public Observable<Response<WeiChatID>> call(Response<WeiChatUrl> response) {
                WeiChatUrl weiChatUrl = response.body();
                String url = weiChatUrl.getUrl();

                HttpParams params = new HttpParams();
                params.putHeaders("Referer", PandaApi.REGCLINET_USER_URL);
                params.putHeaders("User-Agent", "CNTV_APP_CLIENT_CBOX_MOBILE");
                Call<WeiChatID> call = HttpTools.get(url, WeiChatID.class, params);
                return call.getResult();
            }
        }).flatMap(new Func1<Response<WeiChatID>, Observable<Response<UserBean>>>() {
            @Override
            public Observable<Response<UserBean>> call(Response<WeiChatID> response) {
                verifycode = HttpHeaderHelper.parseVerifyCode(response.headers());
                WeiChatID weiChatId = response.body();
                userSeqId = weiChatId.getUser_seq_id();

                Call<UserBean> call = PandaApi.getNickNameAndFace(userSeqId);
                return call.getResult();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<UserBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showShort(WXEntryActivity.this, "登陆出错");
                        onFinish();
                    }

                    @Override
                    public void onNext(Response<UserBean> response) {
                        UserBean bean = response.body();
                        userNickName = bean.content.getNickname();
                        mFace = bean.content.getUserface();

                        mUserManager.saveNickName(userNickName);
                        mUserManager.saveUserFace(mFace);
                        mUserManager.saveUserId(userSeqId);
                        mUserManager.saveVerifycode(verifycode);
                        mUserManager.setUserInfoRetrieved(true);

                        MobileAppTracker.trackEvent("登录", null, "个人中心", 0, userSeqId, "登录", WXEntryActivity.this);
                        Log.e("统计","事件名称:"+"登陆"+"***事件类别:"+"个人中心"+"**ID"+userSeqId);
                        Intent intent = new Intent(WXEntryActivity.this, PersonalLoginActivity.class);
                        intent.putExtra(PersonalLoginActivity.PLATFORM, true);
                        startActivity(intent);

                    }
                });
    }

    private void onFinish() {

        Variables.LOGIN = false;
        WXEntryActivity.this.finish();
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {

        Log.e("wwww", "onReq......");

    }
    //检查网络状态
    private NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info;
    }

}
