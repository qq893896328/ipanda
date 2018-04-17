package cn.cntv.app.ipanda.ui.personal.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

import com.gridsum.mobiledissector.MobileAppTracker;

import java.lang.reflect.Field;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.api.PandaApi;
import cn.cntv.app.ipanda.bean.UserBean;
import cn.cntv.app.ipanda.data.net.retrofit.Call;
import cn.cntv.app.ipanda.data.net.retrofit.Callback;
import cn.cntv.app.ipanda.data.net.retrofit.Response;
import cn.cntv.app.ipanda.manager.UserManager;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.utils.Logs;
import cn.cntv.app.ipanda.utils.ToastUtil;

/**
 * Created by maqingwei on 2016/4/17.
 */
public class QQLogingActivity extends BaseActivity implements View.OnClickListener{

    private String mPath = "http://oauth.passport.cntv.cn/OAuthQzoneClient/OAuthQZoneClientServlet.do?method=login&cntv_callback=my";
    private String back_url = "http://my.cntv.cn";

    private String userSeqId;
    private String verifycode;
    private WebView mWebView;
    private String userNickName;
    private UserManager mUserManager;
    private String userFace;

    private TextView mTitleLeft,mTitleCenter,mTitleRight;

    //获取用户昵称成功、失败
    private static final int MSG_LOGIN_IN_SUCCESS = 1001;
    private static final int HANDLER_ERROR = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__qq_login);

        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private  void init(){

        mUserManager = UserManager.getInstance();

        mWebView = (WebView) findViewById(R.id.wv_qqlogin);
        mTitleLeft = (TextView) findViewById(R.id.common_title_left);
        mTitleCenter = (TextView) findViewById(R.id.common_title_center);
        mTitleRight = (TextView) findViewById(R.id.common_title_right);

        mTitleRight.setText(getString(R.string.login_regist));
        mTitleCenter.setText(getString(R.string.login_btn));

        mTitleLeft.setOnClickListener(this);
        mTitleRight.setOnClickListener(this);

        String ua = mWebView.getSettings().getUserAgentString();
        mWebView.getSettings().setUserAgentString(ua + ";cntv_app_client_cbox_mobile");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        if (Integer.parseInt(Build.VERSION.SDK) >= 11) {// 用于判断是否为Android 3.0系统,

            mWebView.getSettings().setDisplayZoomControls(false);

        } else {

            setZoomControlGone(mWebView);
        }
        showLoadingDialog();
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(mPath);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.common_title_left:
                Intent intent = new Intent(this,PersonalLoginActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.common_title_right:
                startActivity(new Intent(this,PersonalRegActivity.class));
                break;
        }
    }



    public void setZoomControlGone(View view) {
        Class classType;
        Field field;
        try {
            classType = WebView.class;
            field = classType.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(
                    view);
            mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
            try {
                field.set(view, mZoomButtonsController);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


    public void getUserNickName( String seqId) {

        if (!isConnected()) {
            showToast(R.string.network_invalid);
        }

        PandaApi.getNickNameAndFace(seqId).enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                dismissLoadDialog();
                UserBean bean =  response.body();
                userNickName = bean.content.getNickname();
                userFace = bean.content.getUserface();

                mUserManager.saveNickName(userNickName);
                mUserManager.saveUserId(userSeqId);
                mUserManager.saveVerifycode(verifycode);
                mUserManager.saveUserFace(userFace);

                MobileAppTracker.trackEvent("登录", null, "个人中心", 0, userSeqId, "登录", QQLogingActivity.this);
                Log.e("统计","事件名称:"+"登陆"+"***事件类别:"+"个人中心"+"**ID"+userSeqId);
                Intent intent = new Intent(QQLogingActivity.this, PersonalLoginActivity.class);
                intent.putExtra(PersonalLoginActivity.PLATFORM, true);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {
                ToastUtil.showShort(QQLogingActivity.this, "登录出错");
                finish();

            }
        });

    }



    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

        Log.i("QQQQ","login_url"+url);

            if (!TextUtils.isEmpty(userSeqId) && !TextUtils.isEmpty(verifycode)) {
                return true;
            }
            try {
                if (url.startsWith(back_url)) {
                    CookieManager cookieManager = CookieManager.getInstance();

                    if (TextUtils.isEmpty(cookieManager.getCookie(url))) {
                        return true;
                    }
                    String CookieStr = cookieManager.getCookie(url).replaceAll(" ", "");

                    if (!TextUtils.isEmpty(CookieStr)) {
                        String[] data = CookieStr.split(";");

                        for (int i = 0; i < data.length; i++) {
                            if (data[i].length() > 0) {
                                String str = data[i];
                                if (str.split("=").length > 1) {
                                    String key = str.split("=")[0];
                                    String value = str.split("=")[1];
                                    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                                        if (key.equals("userSeqId")) {
                                            userSeqId = value;
                                        }
                                        if (key.equals("verifycode")) {
                                            verifycode = value;
                                            UserManager.getInstance().saveVerifycode(verifycode);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Logs.d("malus", "CookieStr null");
                    }
                   // showLoadingDialog();

                    getUserNickName(userSeqId);
                    return true;
                }
            } catch (Exception e) {
                ToastUtil.showShort(QQLogingActivity.this, "登录失败！");
                e.printStackTrace();
            }

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.e("malus", "start url:" + url);
            Log.i("AAA","onPageStarted");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        public void onPageFinished(WebView view, String url) {
            dismissLoadDialog();
            Log.i("AAA","onPageFinished");
            Log.e("sunzn", "onPageFinished url = " + url);

        }
    }
}
