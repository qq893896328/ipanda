package cn.cntv.app.ipanda.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.gridsum.mobiledissector.MobileAppTracker;
import com.gridsum.videotracker.VideoTracker;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

import cn.cntv.app.ipanda.AppContext;
import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.di.components.ApplicationComponent;
import cn.cntv.app.ipanda.di.modules.ActivityModule;
import cn.cntv.app.ipanda.manager.UserManager;
import cn.cntv.app.ipanda.ui.home.auto.layout.AutoLayoutActivity;
import cn.cntv.app.ipanda.utils.NetUtil;

public class BaseActivity extends AutoLayoutActivity {

    private ProgressDialog progressDialog;
    private Dialog loadDialog;
    private int dialogNum;
    private UserManager mUserManager = UserManager.getInstance();

    public int displayHeight;
    public int displayWidth;
    private UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

    protected void onCreate(android.os.Bundle savedInstanceState) {

        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        this.getApplicationComponent().inject(this);

        String tUserId = mUserManager.getUserId();
        if (tUserId != null && !tUserId.equals("")) {
            MobileAppTracker.setUserId(tUserId, this.getApplicationContext());
        }
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        displayHeight = outMetrics.heightPixels;
        displayWidth = outMetrics.widthPixels;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("BaseActivity", "onActivityResult: " );
    }

    /**
     * 得到屏幕宽度
     *
     * @return 宽度
     */
    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        BaseActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    /*
     * 获取应用版本号
     *
     * @author GaoMing
     *
     * @return 返回版本号
     */
    public String getVersion() {
        try {
            PackageInfo info = BaseActivity.this.getPackageManager()
                    .getPackageInfo(BaseActivity.this.getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 得到屏幕高度
     *
     * @return 高度
     */
    public int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        BaseActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }

    /**
     * 是否全屏和显示标题，true为全屏和无标题，false为无标题，请在setContentView()方法前调用
     *
     * @param fullScreen
     */
    public void setFullScreen(boolean fullScreen) {
        if (fullScreen) {
            BaseActivity.this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            BaseActivity.this.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            BaseActivity.this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

    }

    /**
     * 短时间显示Toast
     *
     * @param info 显示的内容
     */
    public void showToast(String info) {
        Toast.makeText(BaseActivity.this, info, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param info 显示的内容
     */
    public void showToastLong(String info) {
        Toast.makeText(BaseActivity.this, info, Toast.LENGTH_LONG).show();
    }

    /**
     * 短时间显示Toast
     *
     * @param resId 显示的内容
     */
    public void showToast(int resId) {
        Toast.makeText(BaseActivity.this, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param resId 显示的内容
     */
    public void showToastLong(int resId) {
        Toast.makeText(BaseActivity.this, resId, Toast.LENGTH_LONG).show();
    }

    /**
     * 判断手机是否有网络
     *
     * @return true 有网络
     */
    public boolean isConnected() {
        return NetUtil.isNetConnected(this);
    }

    /**
     * 显示正在加载的进度条
     */
    public void showLoadingDialog() {
        dialogNum++;
        if (loadDialog != null && loadDialog.isShowing()) {
            loadDialog.dismiss();
            loadDialog = null;
        }
        loadDialog = new Dialog(BaseActivity.this, R.style.dialog);
        loadDialog.setCanceledOnTouchOutside(false);

        loadDialog.setContentView(R.layout.layout_dialog);
        try {
            loadDialog.show();
        } catch (BadTokenException exception) {
            exception.printStackTrace();
        }
    }

    public void showProgressDialog(String msg) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = new ProgressDialog(BaseActivity.this);
        progressDialog.setMessage(msg);
        try {
            progressDialog.show();
        } catch (BadTokenException exception) {
            exception.printStackTrace();
        }
    }


    public ProgressDialog createProgressDialog(String msg) {
        ProgressDialog progressDialog = new ProgressDialog(BaseActivity.this);
        progressDialog.setMessage(msg);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    /**
     * 隐藏正在加载的进度条
     */
    public void dismissLoadDialog() {
        dialogNum--;
        if (dialogNum > 0) {
            return;
        }
        if (null != loadDialog && loadDialog.isShowing() == true) {
            loadDialog.dismiss();
            loadDialog = null;
        }
    }

    /**
     * 获取控件的宽高
     *
     * @param view
     * @return
     */
    public int[] getWigetWidthHeight(View view) {
        int[] array = new int[2];
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        array[0] = width;
        array[1] = height;
        return array;
    }

    /**
     * 对于个别app不需要根据系统字体的大小来改变的，
     * 可以在activity基类（app中所有的activity都应该有继承于我们自己定义的一个BaseActivity类）
     * 中加上以下code：
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobileAppTracker.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobileAppTracker.onPause(this);
    }


    /**
     * 视频统计初始化
     */
    protected VideoTracker addVodTracker() {

        VideoTracker.setConfigSource("http://app.cntv.cn/special/gridsum/");
        VideoTracker.setBackupConfigSource("http://cfg-vd.gridsumdissector.com/");

        VideoTracker tTracker = VideoTracker.getInstance("GVD-200082", " GSD-200082", getApplicationContext());

        String tUserId = mUserManager.getUserId();

        if (tUserId != null && !tUserId.equals("")) {

            tTracker.setUserId(tUserId);
        }

        tTracker.setMfrs(android.os.Build.MODEL);
        tTracker.setDevice("Android");
        tTracker.setChip("Android_" + android.os.Build.VERSION.RELEASE);

        return tTracker;
    }


    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super .onTouchEvent(event);
    }


    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment The fragment to be added.
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    protected ApplicationComponent getApplicationComponent() {
        return AppContext.getInstance().getApplicationComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
