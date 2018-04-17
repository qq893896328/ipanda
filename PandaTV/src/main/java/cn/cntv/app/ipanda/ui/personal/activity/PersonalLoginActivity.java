package cn.cntv.app.ipanda.ui.personal.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gridsum.mobiledissector.MobileAppTracker;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.cntv.app.ipanda.AppContext;
import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.api.PandaApi;
import cn.cntv.app.ipanda.bean.LoginBean;
import cn.cntv.app.ipanda.bean.UserBean;
import cn.cntv.app.ipanda.constant.Constants;
import cn.cntv.app.ipanda.constant.UMengEnum;
import cn.cntv.app.ipanda.constant.Variables;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.data.net.HttpTools;
import cn.cntv.app.ipanda.data.net.retrofit.Call;
import cn.cntv.app.ipanda.data.net.retrofit.Callback;
import cn.cntv.app.ipanda.data.net.retrofit.HttpParams;
import cn.cntv.app.ipanda.data.net.retrofit.Response;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.manager.UserManager;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.ui.home.activity.VoteActivity;
import cn.cntv.app.ipanda.ui.personal.entity.SinaOpenID;
import cn.cntv.app.ipanda.ui.personal.entity.SinaUrl;
import cn.cntv.app.ipanda.utils.HttpHeaderHelper;
import cn.cntv.app.ipanda.utils.ToastUtil;
import cn.cntv.app.ipanda.wxapi.WXEntryActivity;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class PersonalLoginActivity extends BaseActivity implements OnClickListener {

    private static final int USER_TICKET_SUCCESS = 1;
    private static final int USER_NICK_NAME_SUCCESS = 2;

    private static final int HANDLER_ERROR = 500;

    private static final int MSG_LOGIN_IN_ERROR = 401;
    private static final int MSG_LGOIN_IN_GET_NICKNAME = 402;
    private static final int MSG_GET_NICKNAME_SUCCESS = 403;

    private static final int HANDLER_GET_URL_BY_TOKEN = 404;
    private static final int HANDLER_GET_OPENID_SUCCESS = 405;
    private static final int HANDLER_GETNICKNAME_SUCCESS = 406;

    public static final String PLATFORM = "platform";

    private EditText mEditAccount, mEditPassword;
    private TextView mTxtTishiAccount, mTxtTishiPassword;

    private LinearLayout mWeiXinLogin, mQQLogin, mSinaLogin;

    private String mUserSeqId;
    private String mUserId;
    private String mNickName;
    private String mFace;

    private String mVerifycode;
    private UserManager mUserManager;

    private int mLogTips;//区分点击的是微信（11） 新浪（22）登陆
    private IWXAPI mWXApi;

    // 从熊猫点播界面过来
    public static final String FromLpandaKnownPage = "fromLpandaKnownPage";
    public static final String FromWhere = "fromwhere";

    // 从专题页面来的数据
    public static final String FromSSubjectPage = "fromSSubjectPage";

    private String mFromTag;

    private SsoHandler mSsoHandler;

    private static class LoginHandler extends Handler {

        private final WeakReference<PersonalLoginActivity> mActivity;

        public LoginHandler(PersonalLoginActivity activity) {

            mActivity = new WeakReference<PersonalLoginActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            PersonalLoginActivity tLoginActivity = mActivity.get();

            switch (msg.what) {
                case MSG_LOGIN_IN_ERROR:
                    tLoginActivity.dismissLoadDialog();
                    if (msg.obj != null) {
                        if (msg.obj instanceof String) {
                            String err = (String) msg.obj;
                            if (err.equals("105")) {
                                tLoginActivity.mTxtTishiPassword.setText("密码错误，请重输");
                            }
                            if (err.equals("104")) {
                                tLoginActivity.mTxtTishiAccount.setText("该账号未注册。");
                            }
                            if (err.equals("106")) {
                                tLoginActivity.mTxtTishiAccount.setText("很抱歉，您的账号还没有激活，请激活后再登录。");
                            }
                            if (err.equals("102")) {
                                tLoginActivity.mTxtTishiAccount.setText("缺少参数");
                            }

                        }
                    }
                    break;
                case HANDLER_GET_OPENID_SUCCESS:

                    SinaOpenID sianId = (SinaOpenID) msg.obj;
                    String sinaId = sianId.getUser_seq_id();
                    tLoginActivity.mUserSeqId = sinaId;
                    tLoginActivity.getUserInfo(sinaId);

                    break;

                case HANDLER_GETNICKNAME_SUCCESS://新浪获取的Nickname

                  /*  tLoginActivity.dismissLoadDialog();
                    MobileAppTracker.trackEvent("登陆", null, "个人中心", 0, tLoginActivity.mUserId, "登陆", tLoginActivity);

                    SharedPreferences spt1 = tLoginActivity.getSharedPreferences("user_info", 0);
                    spt1.edit().putString("nicknm", tLoginActivity.mNickName).commit();
                    spt1.edit().putString("user_seq_id", tLoginActivity.mUserSeqId).commit();
                    spt1.edit().putString("verifycode",tLoginActivity.mVerifycode).commit();

                    AppContext.getInstance().getSpUtil()
                            .setUserVerifycode(tLoginActivity.mVerifycode);
                    AppContext.getInstance().getSpUtil()
                            .setUserId(tLoginActivity.mUserSeqId);
                    AppContext.getInstance().getSpUtil()
                            .setUserTickName(tLoginActivity.mNickName);

//                    Intent sinaintent = new Intent(tLoginActivity,
//                            PersonalActivity.class);
//                    tLoginActivity.startActivity(sinaintent);
                    tLoginActivity.finish();*/
                    break;
                case HANDLER_ERROR:

                    tLoginActivity.dismissLoadDialog();
                    Toast.makeText(tLoginActivity, R.string.login_error_try_again, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_LGOIN_IN_GET_NICKNAME:
                    tLoginActivity.getUserInfo(tLoginActivity.mUserSeqId);

                    break;
                case MSG_GET_NICKNAME_SUCCESS://通行证获取的Nickname


                    break;
            }
        }
    }

    private LoginHandler mHandler = new LoginHandler(this);

    private XjlHandler<Object> mXjlHandler = new XjlHandler<Object>(new HandlerListener() {
        @Override
        public void handlerMessage(HandlerMessage msg) {

            switch (msg.what) {

                case Integer.MAX_VALUE:

                    if (msg.whatTag == USER_TICKET_SUCCESS) {

                        Message tMsgError = mHandler
                                .obtainMessage(MSG_LOGIN_IN_ERROR);
                        tMsgError.obj = "网络异常，请稍后重试!";
                        mHandler.sendMessage(tMsgError);
                    }
                    break;
                case HANDLER_GET_URL_BY_TOKEN:

                    SinaUrl tSinaUrl = (SinaUrl) msg.obj;

                    if (tSinaUrl.getErrorCode().equals("0")) {
                        String tUrlTokenId = tSinaUrl.getUrl();

                        Map<String, String> tHeaders = new HashMap<String, String>();

                        try {
                            tHeaders.put("Referer",
                                    URLEncoder.encode("iPanda.Android", "UTF-8"));
                            tHeaders.put("User-Agent", URLEncoder.encode(
                                    "CNTV_APP_CLIENT_CYNTV_MOBILE", "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        getIdByToken(tUrlTokenId);
                    } else {

                        Message tMesg = new Message();
                        tMesg.what = HANDLER_ERROR;
                        if (!tSinaUrl.getErrorCode().equals(""))
                            tMesg.obj = tSinaUrl.getErrMsg();
                        mHandler.sendMessage(tMesg);
                    }

                    break;
                case USER_TICKET_SUCCESS:

                    UserBean bean = (UserBean) msg.obj;
                    if (bean.code == Constants.CODE_SUCCEED) {

                        mNickName = bean.content.getNickname();

                        if (mNickName == null || mNickName.equals(""))
                            mNickName = "default";

                        mHandler.sendEmptyMessage(MSG_GET_NICKNAME_SUCCESS);
                    } else {

                        Message tMesg = new Message();
                        tMesg.what = MSG_LOGIN_IN_ERROR;
                        if (!bean.error.equals(""))
                            tMesg.obj = bean.error;
                        mHandler.sendMessage(tMesg);
                    }
                    break;
                case USER_NICK_NAME_SUCCESS:
                    break;
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_personal_login);

        initView();

    }


    private void initView() {
        mUserManager = UserManager.getInstance();

        TextView titleLeft = (TextView) this.findViewById(R.id.common_title_left);
        TextView titleCenter = (TextView) this.findViewById(R.id.common_title_center);
        TextView titleRight = (TextView) this.findViewById(R.id.common_title_right);
        TextView fPwd = (TextView) this.findViewById(R.id.personal_login_forget_pwd);

        mWeiXinLogin = (LinearLayout) this.findViewById(R.id.llweixinlogin);
        mQQLogin = (LinearLayout) this.findViewById(R.id.llqqlogin);
        mSinaLogin = (LinearLayout) this.findViewById(R.id.llsinalogin);

        fPwd.setText(getString(R.string.login_forgetpaswd)+"?");
        fPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        fPwd.getPaint().setAntiAlias(true);//抗锯齿
        fPwd.setTextColor(Color.parseColor("#1f539e"));

        titleCenter.setText(getString(R.string.dialog_login));
        titleRight.setText(getString(R.string.login_regist));

        mWeiXinLogin.setOnClickListener(this);
        mQQLogin.setOnClickListener(this);
        mSinaLogin.setOnClickListener(this);

        titleLeft.setOnClickListener(this);
        titleRight.setOnClickListener(this);
        fPwd.setOnClickListener(this);

        TextView btnLogin = (TextView) findViewById(R.id.loding_btn);
        btnLogin.setOnClickListener(this);
        mEditAccount = (EditText) findViewById(R.id.edit_account);
        mEditPassword = (EditText) findViewById(R.id.edit_password);
        mTxtTishiAccount = (TextView) findViewById(R.id.hint_account);
        mTxtTishiPassword = (TextView) findViewById(R.id.hint_password);

        mEditAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mTxtTishiAccount.setText("");
                } else {
                    if (!checkEmailAndPhone()) {
                        return;
                    }
                }

            }
        });

        mEditPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mEditPassword.getText().toString().length() > 0) {
                    }
                    mTxtTishiPassword.setText("");
                } else {
                }
            }
        });

        //注册到微信
        mWXApi = WXAPIFactory.createWXAPI(PersonalLoginActivity.this, UMengEnum.WX_APP_ID.toString(), true);


        if (getIntent() != null) {
            if (getIntent().getStringExtra(FromWhere) != null) {
                mFromTag = getIntent().getStringExtra(FromWhere);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal_login_forget_pwd:

                Intent pwdIntent = new Intent(PersonalLoginActivity.this,
                        PersonalForgetPwdActivity.class);
                startActivity(pwdIntent);

                break;
            case R.id.common_title_right:
                Intent regIntent = new Intent(PersonalLoginActivity.this,
                        PersonalRegActivity.class);
                startActivity(regIntent);
                break;
            case R.id.common_title_left:

                finish();
                break;

            case R.id.llweixinlogin:
                mLogTips = 11;

                if (!isConnected()) {
                    showToast(R.string.network_invalid);
                    return;
                }

                doLogin(mWeiXinLogin, true);
                break;
            case R.id.llqqlogin:
                if (!isConnected()) {
                    showToast(R.string.network_invalid);
                    return;
                }
                qqLogin();
                break;
            case R.id.llsinalogin:
                mLogTips = 22;
                if (!isConnected()) {
                    showToast(R.string.network_invalid);
                    return;
                }
                doLogin(mSinaLogin, true);
                break;
            // 登录
            case R.id.loding_btn:
                mEditPassword.clearFocus();
                if (!isConnected()) {
                    showToast(R.string.network_invalid);
                    return;
                }

                if (!checkEmailAndPhone()) {
                    return;
                }
                mTxtTishiAccount.setText("");

                if (checkEmpty(mEditPassword) == false) {
                    mTxtTishiPassword.setText(getString(R.string.regist_null_paswd_tips));
                    return;
                }
                showLoadingDialog();
                mTxtTishiAccount.setText("");
                mTxtTishiPassword.setText("");
                goLogin();
                break;

        }
    }


    private void qqLogin() {
        //QQ登陆
        startActivity(new Intent(this, QQLogingActivity.class));
    }


    private void weixinLogin() {

        //微信登陆
        if (mWXApi.isWXAppInstalled() && mWXApi.isWXAppSupportAPI()) { //判断是否安装和版本是否支持 给予提示
            mWXApi.registerApp(UMengEnum.WX_APP_ID.toString());

        } else {

            ToastUtil.showShort(PersonalLoginActivity.this, "请安装新版本客户端！");
        }

        Variables.LOGIN = true;

        //发送请求
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";//这里填的snsapi_userinfo，用snsapi_base提示没权限。
        req.state = "wechat_sdk_demo_test";//用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验。
        mWXApi.sendReq(req);

    }


    private void
    sinaLogin() {

        showLoadingDialog();

        //新浪登陆
        AuthInfo tAuthInfo = new AuthInfo(PersonalLoginActivity.this, Constants.tSinaAppId, Constants.tSinaRedirectUrl, Constants.tSinaScope);

        if (mSsoHandler == null && tAuthInfo != null)
            mSsoHandler = new SsoHandler(PersonalLoginActivity.this, tAuthInfo);

        if (mSsoHandler != null) {

            mSsoHandler.authorize(new WeiboAuthListener() {
                @Override
                public void onComplete(Bundle bundle) {

                    String tSinaToken = bundle.getString("access_token");

                    String urlString = "http://oauth.passport.cntv.cn/OAuthSinaClient/OAuthSinaAppServlet.do?cntv_from=http://ipanda_mobile.regclientuser.cntv.cn&accesstoken=" + tSinaToken;

                    mXjlHandler.getHttpJson(urlString, SinaUrl.class, HANDLER_GET_URL_BY_TOKEN);
                }

                @Override
                public void onWeiboException(WeiboException e) {

                    dismissLoadDialog();
                    Toast.makeText(PersonalLoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    dismissLoadDialog();
                    Toast.makeText(PersonalLoginActivity.this,
                            R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 第二步，获取openid和unionid和user_seq_id
     */
    private void getIdByToken(final String url) {

        HttpParams params = new HttpParams();
        params.putHeaders("Referer", PandaApi.REFERER);
        params.putHeaders("User-Agent", "CNTV_APP_CLIENT_CYNTV_MOBILE");

        HttpTools.get(url, SinaOpenID.class, params).enqueue(new Callback<SinaOpenID>() {
            @Override
            public void onResponse(Call<SinaOpenID> call, Response<SinaOpenID> response) {
                mVerifycode = HttpHeaderHelper.parseVerifyCode(response.headers());
                SinaOpenID sianId = response.body();
                String sinaId = sianId.getUser_seq_id();
                mUserSeqId = sinaId;
                MobileAppTracker.trackEvent("登录", null, "个人中心", 0, mUserSeqId, "登录", PersonalLoginActivity.this);
                Log.e("统计","事件名称:"+"登陆"+"***事件类别:"+"个人中心"+"**ID"+mUserSeqId);
                getUserInfo(sinaId);
            }

            @Override
            public void onFailure(Call<SinaOpenID> call, Throwable t) {

            }
        });
    }

    public void getUserInfo(final String userId) {

        Call<UserBean> call = PandaApi.getNickNameAndFace(userId);
        call.enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                UserBean bean = response.body();
                handleUserInfo(bean);
                dismissLoadDialog();

            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {
                dismissLoadDialog();
            }

        });

    }


    /**
     * 提示框
     *
     * @param parent
     * @param strId
     */
    public void showTipPop(final View parent, int strId) {
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.player_tip, null, false);
        // 提示信息
        final TextView tipTv = (TextView) dialogView.findViewById(R.id.tv_tip);
        tipTv.setText(this.getResources().getString(strId));
        // 创建弹出对话框，设置弹出对话框的背景为圆角
        final PopupWindow tipPw = new PopupWindow(dialogView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        // 响应返回键
        tipPw.setFocusable(true);
        // Cancel按钮及其处理事件
        final TextView btnCancel = (TextView) dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setText(this.getResources().getString(R.string.cancel));
        btnCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (tipPw != null && tipPw.isShowing()) {
                    tipPw.dismiss();// 关闭
                }
            }
        });
        final TextView btnOk = (TextView) dialogView.findViewById(R.id.btn_ok);
        btnOk.setText(this.getResources().getString(R.string.sure));
        btnOk.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (tipPw != null && tipPw.isShowing()) {
                    tipPw.dismiss();// 关闭
                }
                if (mLogTips == 11) {
                    AppContext.getInstance().getSpUtil().setIsFirstLoginWx(false);
                }
                if (mLogTips == 22) {
                    AppContext.getInstance().getSpUtil().setIsFirstLoginSinaWeib(false);
                }

                doLogin(parent, false);
            }
        });


        // 显示RoundCorner对话框
        tipPw.showAtLocation(parent, Gravity.CENTER, 0, 0);

    }

    private void doLogin(View v, boolean flag) {

        if (v.getId() == R.id.llweixinlogin) {
            // 登录到微信和朋友圈，第一次给出弹框提示
            if (flag && AppContext.getInstance().getSpUtil().getIsFirstLoginWx()) {
                // 首次登录至微信好友、微信朋友圈，点击图标，弹出想要打开“微信”的弹框

                showTipPop(v, R.string.share_tip2);

                return;
            }
        }
        if (v.getId() == R.id.llsinalogin) {
            // 登录到新浪微博，第一次给出弹框提示
            if (flag && AppContext.getInstance().getSpUtil().getIsFirstLoginSinaWeib()) {
                // 首次登录至新浪微博，点击图标，弹出想要打开“微博”的弹框

                showTipPop(v, R.string.share_tip3);

                return;
            }

        }
        switch (v.getId()) {

            case R.id.llweixinlogin:
                if (!isConnected()) {
                    showToast(R.string.network_invalid);
                    return;
                }
                showLoadingDialog();
                weixinLogin();
                break;
            case R.id.llsinalogin:
                if (!isConnected()) {
                    showToast(R.string.network_invalid);
                    return;
                }
                sinaLogin();
                break;
        }

    }

    // 检查邮箱、手机号
    private boolean checkEmailAndPhone() {
        String emailString = mEditAccount.getText().toString().trim();
        if (TextUtils.isEmpty(emailString)) {
            mTxtTishiAccount.setText(getString(R.string.regist_account_null));
            return false;
        }
        String tEmail = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        if (emailString.indexOf("@") == -1) {
            tEmail = "^1[3578]\\d{9}$";
        }
        Pattern pattern = Pattern
                .compile(tEmail);
        Matcher matcher = pattern.matcher(emailString);
        if (matcher.matches()) {
            mTxtTishiAccount.setText("");
            return true;
        } else {
            mTxtTishiAccount.setText(getString(R.string.regist_account_error));
            return false;
        }
    }

    /**
     * 检查是否为空
     *
     * @param editText
     * @return
     */
    private boolean checkEmpty(EditText editText) {
        String testTxt = editText.getText().toString();
        if (testTxt == null || "".equals(testTxt)) {
            return false;
        }
        return true;
    }

    /**
     * 登录
     */
    private void goLogin() {

        String userName = mEditAccount.getText().toString()
                .trim();
        String password = mEditPassword.getText().toString();

        PandaApi.login(userName, password).getResult()
                .flatMap(new Func1<Response<LoginBean>, Observable<Response<UserBean>>>() {
                    @Override
                    public Observable<Response<UserBean>> call(Response<LoginBean> response) {
                        LoginBean bean = response.body();
                        if ("0".equals(bean.errType)) {
                            mVerifycode = HttpHeaderHelper.parseVerifyCode(response.headers());
                            mUserSeqId = bean.user_seq_id;
                            mUserId = bean.usrid;
                            MobileAppTracker.trackEvent("登录", null, "个人中心", 0, mUserSeqId, "登录", PersonalLoginActivity.this);
                            Log.e("统计","事件名称:"+"登陆"+"***事件类别:"+"个人中心"+"***ID"+mUserSeqId);
                            return PandaApi.getNickNameAndFace(mUserSeqId).getResult();
                        } else {
                            return Observable.error(new IllegalStateException(bean.errType));
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<UserBean>>() {
                    @Override
                    public void onCompleted() {
                        dismissLoadDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IllegalStateException) {
                            loginErrorMsg(e.getMessage());
                            //Toast.makeText(PersonalLoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                        dismissLoadDialog();
                    }

                    @Override
                    public void onNext(Response<UserBean> response) {
                        UserBean user = response.body();
                        handleUserInfo(user);
                    }
                });
    }

    private void handleUserInfo(UserBean bean) {
        if (bean.code == Constants.CODE_SUCCEED) {
            mNickName = bean.content.getNickname();
            mFace = bean.content.getUserface();

            mUserManager.saveUserId(mUserSeqId);
            mUserManager.saveNickName(mNickName);
            mUserManager.saveUserFace(mFace);
            mUserManager.saveVerifycode(mVerifycode);
            mUserManager.setUserInfoRetrieved(true);
            if (mFromTag != null && mFromTag.equals(FromLpandaKnownPage)) {
                finish();
            } else if (mFromTag != null && mFromTag.equals(FromSSubjectPage)) {

                String tVid = getIntent().getStringExtra("vid");
                Intent intent = new Intent(PersonalLoginActivity.this,
                        VoteActivity.class);
                intent.putExtra("vid", tVid);
                startActivity(intent);
                finish();

            } else {
                finish();
            }

        }
    }

    private void  loginErrorMsg(String errtype){
        if (errtype.equals("105")) {
            mTxtTishiPassword.setText("密码错误，请重输");
        }
        if (errtype.equals("104")) {
            mTxtTishiAccount.setText("该账号未注册。");
        }
        if (errtype.equals("106")) {
            mTxtTishiAccount.setText("很抱歉，您的账号还没有激活，请激活后再登录。");
        }
        if (errtype.equals("102")) {
            mTxtTishiAccount.setText("缺少参数");
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mEditAccount = null;
        mEditPassword = null;
        mTxtTishiAccount = null;
        mTxtTishiPassword = null;
        mWeiXinLogin = null;
        mQQLogin = null;
        mSinaLogin = null;
        mWeiXinLogin = null;

        mVerifycode = null;
        mWXApi = null;
    }


    @Override
    protected void onStop() {
        super.onStop();
        dismissLoadDialog();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean success = intent.getBooleanExtra(PLATFORM, false);
        if (success) {
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {
            Log.e("hhh", "wx......");
        }

        /**使用SSO授权必须添加如下代码 */
        if (mSsoHandler != null) {

            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

}
