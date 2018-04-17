package cn.cntv.app.ipanda.ui.home.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

import com.gridsum.mobiledissector.MobileAppTracker;

import java.lang.reflect.Field;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.ui.play.ShareController;

/**
 * @author Xiao JinLai
 * @ClassName: InterDetailActivity
 * @Date Jan 20, 2016 3:12:06 PM
 * @Description：互动详情页面
 */
public class InterDetailActivity extends BaseActivity implements
        OnClickListener {

    private static final String TAG = "InterDetailActivity";

    private WebView mWebView;
    private String mUrl;
    private String mTitle;

    private AudioManager mAudioManager;
    private boolean isPause;

    private String image;
    private ShareController shareController = new ShareController();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        initView();
    }


    /**
     * 加载View
     */
    @SuppressLint("NewApi")
    private void initView() {

        setContentView(R.layout.activity_inter);


        mTitle = getIntent().getStringExtra("title");
        TextView tTvTitle = (TextView) this
                .findViewById(R.id.tvInterDetailTitle);
        tTvTitle.setText(mTitle);

        this.findViewById(R.id.tvInterDetailBack).setOnClickListener(this);
        this.findViewById(R.id.tvInterDetailShare).setOnClickListener(this);

        mUrl = getIntent().getStringExtra("url");
        image = getIntent().getStringExtra("image");
        mWebView = (WebView) this.findViewById(R.id.wvInter);
        if (Integer.parseInt(Build.VERSION.SDK) >= 11) {// 用于判断是否为Android 3.0系统,
            // 然后隐藏缩放控件
            // webView.getSettings().setDisplayZoomControls(false);
        } else {
            setZoomControlGone(mWebView);
        }


        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
// 添加新参数
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 16) {
            webSettings.setMediaPlaybackRequiresUserGesture(false);
        }

        mWebView.loadUrl(mUrl);
        shareController.setLoad(this.mWebView);

    }

    // Android 3.0(11) 以下使用以下方法:
    // 利用java的反射机制
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tvInterDetailBack:

                InterDetailActivity.this.finish();
                break;

            case R.id.tvInterDetailShare:

                MobileAppTracker.trackEvent(mTitle, "分享", "互动", 0, mTitle, "分享", this);
                MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                Log.i("统计", "互动mTitle = " + mTitle);
                shareController.showPopWindow(v, mTitle, "4", image, mUrl);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mWebView.reload();

    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;

        hideKeyBoard();

    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;

        requestAudioFocus();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
        mAudioManager.abandonAudioFocus(audioFocusChangeListener);
    }

    private void requestAudioFocus() {

        int result = mAudioManager.requestAudioFocus(audioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.e(TAG, "audio focus been granted");
        }

    }

    private OnAudioFocusChangeListener audioFocusChangeListener = new OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.e(TAG, "focusChange: " + focusChange);

            if (isPause && focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                requestAudioFocus();
            }
        }
    };
    /**
     * 隐藏键盘
     */
    private void hideKeyBoard() {

        View tView = getWindow().peekDecorView();

        if (tView != null) {
            // 获取输入法接口
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            // 强制隐藏键盘
            imm.hideSoftInputFromWindow(tView.getWindowToken(), 0);
        }
    }


}
