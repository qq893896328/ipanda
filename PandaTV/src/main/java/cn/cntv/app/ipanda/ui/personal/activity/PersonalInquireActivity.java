package cn.cntv.app.ipanda.ui.personal.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.BaseActivity;

public class PersonalInquireActivity extends BaseActivity implements View.OnClickListener{

    private TextView mLeftTitle,mCenterTitle;
    private WebView mInquireWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_inquire);
        init();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init(){

        mLeftTitle = (TextView) this.findViewById(R.id.common_title_left);
        mCenterTitle = (TextView) this.findViewById(R.id.common_title_center);
        mInquireWebView = (WebView) this.findViewById(R.id.wvInquire);

        mLeftTitle.setOnClickListener(this);
        mCenterTitle.setText(getString(R.string.user_enquiry));

        mInquireWebView.getSettings().setJavaScriptEnabled(true);
        mInquireWebView.getSettings().setLoadWithOverviewMode(true);
        mInquireWebView.getSettings().setUseWideViewPort(true);
        mInquireWebView.getSettings().setBuiltInZoomControls(false);
        mInquireWebView.getSettings().setSupportZoom(false);
        mInquireWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mInquireWebView.getSettings().setDomStorageEnabled(true);
        mInquireWebView.getSettings().setDatabaseEnabled(true);
        mInquireWebView.getSettings().setSupportZoom(true);  //支持缩放
        mInquireWebView.getSettings().setAllowFileAccess(true);
        mInquireWebView.getSettings().setAllowContentAccess(true);
        mInquireWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mInquireWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mInquireWebView.getSettings().setLoadsImagesAutomatically(true);
        mInquireWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mInquireWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mInquireWebView.loadUrl("http://m.utovr.com/video/cfaaqodjuswp7exv.html");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_title_left:
                finish();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
