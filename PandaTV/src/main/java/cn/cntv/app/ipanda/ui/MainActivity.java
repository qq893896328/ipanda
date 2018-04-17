package cn.cntv.app.ipanda.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.storage.StorageManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gridsum.mobiledissector.MobileAppTracker;
import com.gridsum.mobiledissector.util.StringUtil;
import com.umeng.common.message.UmengMessageDeviceConfig;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;

import cn.cntv.app.ipanda.AppContext;
import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.Constants;
import cn.cntv.app.ipanda.constant.HomeTabIndexEnum;
import cn.cntv.app.ipanda.constant.PandaEyeAddressEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.ui.home.activity.InteractionActivity;
import cn.cntv.app.ipanda.ui.home.activity.SearchActivity;
import cn.cntv.app.ipanda.ui.home.fragment.HomeFragment;
import cn.cntv.app.ipanda.ui.livechina.fragment.LiveChinaFragment;
import cn.cntv.app.ipanda.ui.lovepanda.fragment.LovePandaFragment;
import cn.cntv.app.ipanda.ui.pandaculture.fragment.PandaCultureFragment;
import cn.cntv.app.ipanda.ui.pandaeye.activity.CeLueData;
import cn.cntv.app.ipanda.ui.pandaeye.fragment.PandaEyeFragment;
import cn.cntv.app.ipanda.ui.personal.activity.PersonalActivity;
import cn.cntv.app.ipanda.ui.personal.entity.PIUpdateData;
import cn.cntv.app.ipanda.ui.personal.entity.PIUpdateModel;
import cn.cntv.app.ipanda.utils.CacheUtil;
import cn.cntv.app.ipanda.utils.ToastUtil;
import cn.cntv.app.ipanda.utils.UpgradeTask;
import cn.cntv.play.core.CBoxP2P;
import cn.cntv.play.core.CBoxStaticParam;

public class MainActivity extends BaseActivity implements OnClickListener {

    static final String key_selected_index = "select_index";

    private TextView mineView,mSearchView;

    private TextView titleCnter, titleRight, titleLeft, titleHuDong;

    private static final int TAB_DATA = 0;
    private static final int LIST_DATA1 = 1;
    private static final int LIST_DATA2 = 2;
    private static final int LIST_DATA3 = 3;
    private static final int UPDATE_APK = 4;

    private static final int STOP_P2P_DEALAY = 9527;

    /**
     * 用于对 Fragment 进行管理
     */
    private FragmentManager mFragmentManager;

    /**
     * 当前选中的 Fragment 索引
     */
    private HomeTabIndexEnum mHomeTabIndexEnum;

    public static CBoxP2P pPlugin;

    private RadioButton mRbLovePanda;
    private RadioButton mRbLiveChina;

    // 纪录上一个fragmen 切换tab的时候停止掉视频
    private HomeTabIndexEnum lastHomeTabIndexEnum;
    private PIUpdateModel mPIUpdateModel = null;
    private static final int UPGRADE_APK_CODE = 1;
    private XjlHandler mHandler1 = new XjlHandler(new HandlerListener() {

        @Override
        public void handlerMessage(HandlerMessage msg) {
            dismissLoadDialog();

            switch (msg.what) {
                case Integer.MAX_VALUE:
                    // 错误处理
                    // Log.e("test", msg.obj.toString());
                    break;
                case LIST_DATA1:
                    // 处理tab数据
                    CeLueData data = (CeLueData) msg.obj;
                    if (null != data) {

                        AppContext.setLiveRestrictBeans(data.getChannel_info());
                    }
                    break;
                case UPDATE_APK:
                    PIUpdateData data1 = (PIUpdateData) msg.obj;

                    if (null != data1 && null != data1.getData()) {
                        mPIUpdateModel = data1.getData();
                        if (mPIUpdateModel == null) {
                            return;
                        }
                        Constants.DOWNLOAD_URL = mPIUpdateModel.getVersionsUrl();
                        int versioncode;
                        try {
                            versioncode = getPackageManager().getPackageInfo(
                                    getPackageName(), 0).versionCode;
                            if (Integer.parseInt(mPIUpdateModel.getVersionsNum()) > versioncode)
                                showDialog(UPGRADE_APK_CODE);
                            // else
                            // showToast("已经是最新版本了！");
                        } catch (NameNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                    break;

            }
        }
    });

    protected Dialog onCreateDialog(int id) {
        View viewDialog = (View) LayoutInflater.from(this).inflate(
                R.layout.update_dialog, null);
        final Dialog dialog = new Dialog(this, R.style.loginDialogTheme);


        dialog.setCancelable(false);
        TextView dialogCancel, dialogRightsure, dialog_title, dialog_conTextView;
        dialogRightsure = (TextView) viewDialog
                .findViewById(R.id.login_right_sure);
        dialogCancel = (TextView) viewDialog
                .findViewById(R.id.login_cancel_but);
        dialog_title = (TextView) viewDialog.findViewById(R.id.del_title_tv);
        dialog_conTextView = (TextView) viewDialog
                .findViewById(R.id.del_content_tv);
        dialog.setContentView(viewDialog);
//1.2.0全部强制升级--1.4全部变回可选升级,有服务器后台控制
        switch (id) {
            case UPGRADE_APK_CODE: {
                dialogCancel.setText("下次再说");
                // dialogCancel.setVisibility(View.GONE);
                dialogRightsure.setText("现在去更新");
                dialogRightsure.setTextColor(getResources().getColor(R.color.home_upgrade));
                if (mPIUpdateModel != null) {
                    if ("0".equals(mPIUpdateModel.getVersionsUpdate())) {
                        // "versionsUpdate": "是否强制更新：0为否，1为是"
                        dialogCancel.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                dialog.dismiss();
                            }
                        });
                       /* dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                if (keyCode == KeyEvent.KEYCODE_BACK){
                                    System.exit(0);
                                }
                                return false;
                            }
                        });*/

                    } else {
                        // 强制升级，点击取消退出应用
                       /* dialogCancel.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                System.exit(0);

                            }
                        });*/
                        dialogCancel.setVisibility(View.GONE);
                        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                if (keyCode == KeyEvent.KEYCODE_BACK){
//                                    System.exit(0);
                                    dialog.dismiss();
                                }
                                return false;
                            }
                        });
                    }
                    dialog_title.setText("新版本更新V"
                            + mPIUpdateModel.getVersionsName());
                    dialog_conTextView.setVisibility(View.VISIBLE);
                    dialog_conTextView.setText(StringUtil
                            .isNullOrEmpty(mPIUpdateModel.getVersionsinfo()) ? ""
                            : mPIUpdateModel.getVersionsinfo());
                } else {
                    dialog_title.setText("新版本更新");
                    dialog_conTextView.setVisibility(View.VISIBLE);
                    dialog_conTextView.setText("");
                }
                dialogRightsure.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View arg0) {
                          dialog.dismiss();
//                        ConnectivityManager connectivityManager = (ConnectivityManager) MainActivity.this
//                                .getSystemService(Context.CONNECTIVITY_SERVICE);
//                        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
//                        if (info == null || !connectivityManager.getBackgroundDataSetting()){
//                            ToastUtil.showShort(MainActivity.this,"网络无法连接，请检查网络");
//                        }else {
//                            new UpgradeTask(MainActivity.this, mPIUpdateModel)
//                                    .execute();}
                    }
                });

                return dialog;
            }
            default:
                return null;
        }
    }

    private PushAgent mPushAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 防止fragmetn切换闪烁
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        mFragmentManager = getSupportFragmentManager();

        mHomeTabIndexEnum = HomeTabIndexEnum.TAB_HOME_INDEX;

        if (savedInstanceState != null) {

            int tHomeTabIndex = savedInstanceState
                    .getInt(key_selected_index, 0);
            // 需要设置view的选中状态
            // 需要设置标题的显示状态

            HomeTabIndexEnum[] tHomeTabIndexEnum = HomeTabIndexEnum.values();
            mHomeTabIndexEnum = tHomeTabIndexEnum[tHomeTabIndex];
        }

        initViews();
        String path = getpath_reflect(this);
        // Toast.makeText(this, path+"地址：dsadasfasfsaf", 1000).show();
        initP2PPlugin();
        setCurrentFragment(mHomeTabIndexEnum);

        mPushAgent = PushAgent.getInstance(this);
        // mPushAgent.setPushCheck(true); //默认不检查集成配置文件
        // mPushAgent.setLocalNotificationIntervalLimit(false);
        // //默认本地通知间隔最少是10分钟

        // sdk开启通知声音
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        // sdk关闭通知声音
        // mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        // 通知声音由服务端控制
        // mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER);

        // mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        // mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);

        // 应用程序启动统计
        // 参考集成文档的1.5.1.2
        // http://dev.umeng.com/push/android/integration#1_5_1
        mPushAgent.onAppStart();

        // 开启推送并设置注册的回调处理
        mPushAgent.enable(mRegisterCallback);

        if (!AppContext.isPush) {
            requestHisData();
        } else {
            AppContext.isPush = false;
        }
    }


    public static Boolean mP2pInitSuccess = false;
    public static Boolean mP2pInitComplete = false;
    public static boolean mIsP2pFail = true;
    private int tryCount = 30;// P2P重启总次数
    private int tryNum = 0;// P2P重启次数
    private Handler p2pHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int msgNum = msg.what;
            switch (msgNum) {
                case CBoxStaticParam.P2P_INIT_UPDATE:
                    // Log.e(TAG, "P2P_INIT_UPDATE");
                    String displayInfo = (String) msg.obj;
                    if (displayInfo != null && !"".equals(displayInfo)) {
                        // if (mDialog.isShowing()) {
                        // mDialog.setMessage(displayInfo);
                        // }else {
                        // mDialog.show();
                        // mDialog.setCancelable(false);
                        // }
                    }
                    pPlugin.Start();
                    break;
                case CBoxStaticParam.P2P_INIT:
                    // Log.e(TAG, "P2P_INIT");
                    tryNum++;
                    if (tryNum >= tryCount) {
                        mIsP2pFail = true;
                        return;
                    }
                    pPlugin.Start();
                    break;
                case CBoxStaticParam.P2P_INIT_SUCCESS:
                    mIsP2pFail = false;
                    // Log.e(TAG, "P2P_INIT_SUCCESS");
                    // Log.e("wang", "P2P_INIT_SUCCESS");
                    mP2pInitComplete = true;
                    mP2pInitSuccess = true;
                    // if (mDialog.isShowing()) {
                    // mDialog.cancel();
                    // }
                    // if (mIsNetChannged) {
                    // // 发送广播
                    // Intent mIntent = new Intent(Constants.ACTION_NETWORK_OK_P2P);
                    // sendBroadcast(mIntent);
                    // }
                    break;
                case CBoxStaticParam.P2P_INIT_FAIL:
                    // Log.e(TAG, "P2P_INIT_FAIL");

                    mP2pInitComplete = true;
                    mIsP2pFail = true;
                    // if (mDialog.isShowing()) {
                    // mDialog.cancel();
                    // }
                    pPlugin.Start();
                    mP2pInitSuccess = false;
                    break;
                case STOP_P2P_DEALAY:
                    // Log.e(TAG, "STOP_P2P_DEALAY");
                    initP2PPlugin();
                    break;
                default: {
                    // Log.e(TAG, "default");
                    super.handleMessage(msg);
                }
            }
        }

    };

    public void initP2PPlugin() {
        pPlugin = new CBoxP2P(p2pHandler);
        pPlugin.Start();
    }

    /**
     * 加载控件
     */
    private void initViews() {

        getLiveRestrict();

        mSearchView = (TextView) this.findViewById(R.id.tv_search);
        titleCnter = (TextView) this.findViewById(R.id.title_center);
        titleRight = (TextView) this.findViewById(R.id.title_right);
        titleLeft = (TextView) this.findViewById(R.id.title_left);
        titleHuDong = (TextView) this.findViewById(R.id.title_inter);
        titleHuDong.setOnClickListener(this);
        mSearchView.setOnClickListener(this);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.tabGroup);

        mRbLovePanda = (RadioButton) this.findViewById(R.id.rbTabLovePanda);
        mRbLiveChina = (RadioButton) this.findViewById(R.id.rbTabLiveChina);

        CacheUtil.mRbLovePanda = mRbLovePanda;
        CacheUtil.mRbLiveChina = mRbLiveChina;
        mineView = (TextView) this.findViewById(R.id.title_right);

        mineView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent mineIntent = new Intent(MainActivity.this,
                        PersonalActivity.class);
                startActivity(mineIntent);
            }
        });

        radioGroup
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        onTabBtnClick(checkedId);
                    }
                });

        TextView tTvTitleInter = (TextView) this.findViewById(R.id.title_inter);
        tTvTitleInter.setOnClickListener(this);
    }

    /**
     * 设置当前选中的Fragment
     *
     * @param
     */
    private void setCurrentFragment(HomeTabIndexEnum homeTabIndex) {

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hiddenAllTabFragment(transaction);

        String tFragmentTag = homeTabIndex.toString();
        Fragment fragment = mFragmentManager.findFragmentByTag(tFragmentTag);

        if (lastHomeTabIndexEnum != null) {
            Fragment lastFragment = mFragmentManager
                    .findFragmentByTag(lastHomeTabIndexEnum.toString());
            if (lastFragment != null) {

                if (lastHomeTabIndexEnum == HomeTabIndexEnum.TAB_HOME_LIVE_CHINA) {
                    LiveChinaFragment liveChinaFragment = (LiveChinaFragment) lastFragment;
                    liveChinaFragment.destoryPlay();
                } else if (lastHomeTabIndexEnum == HomeTabIndexEnum.TAB_HOME_PANDA_LIVE) {
                    LovePandaFragment lovePandaFragment = (LovePandaFragment) lastFragment;
                    lovePandaFragment.destoryPlay();
                }
            }
        }
        lastHomeTabIndexEnum = homeTabIndex;

        if (fragment == null) {

            switch (homeTabIndex) {

                case TAB_HOME_INDEX: // 跳转到首页面

                    fragment = HomeFragment.newInstance();

                    break;

                case TAB_HOME_PANDA_LIVE: // 跳转到熊猫直播页面

                    fragment = LovePandaFragment.newInstance();

                    break;
                case TAB_HOME_LIVE_CHINA: // 跳转到直播中国页面

                    fragment = LiveChinaFragment.newInstance();

                    break;

                case TAB_HOME_PANDA_CULTURE: // 跳转到熊猫文化页面
                    fragment = PandaCultureFragment.newInstance();
                    break;

                case TAB_HOME_PANDA_EYE: // 跳转到熊猫眼页面

                    fragment = PandaEyeFragment.newInstance();
                    break;
            }

            transaction.add(R.id.flHomeContent, fragment,homeTabIndex.toString());
        } else {

            transaction.show(fragment);
        }

        transaction.commitAllowingStateLoss();
        mHomeTabIndexEnum = homeTabIndex;
    }

    private void requestHisData() {
        if (isConnected()) {
            // showLoadingDialog();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("applyName", "1426217325");

            mHandler1.getHttpJson(
                    mHandler1.appendParameter(
                            PandaEyeAddressEnum.PE_UPDATEURL.toString(), map),
                    PIUpdateData.class, UPDATE_APK);
        } else {
            // Toast.makeText(this, "检测升级失败",
            // Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * tab 样式activity 隐藏所有fragment
     */
    public void hiddenAllTabFragment(FragmentTransaction transaction) {

        Fragment fragment;

        for (HomeTabIndexEnum tag : HomeTabIndexEnum.values()) {

            fragment = mFragmentManager.findFragmentByTag(tag.toString());

            if (fragment != null) {

                transaction.hide(fragment);
            }
        }
    }

    /**
     * RadioButton点击
     *
     * @param viewId 点击的viewId
     */
    private void onTabBtnClick(int viewId) {

        HomeTabIndexEnum tHomeTabIndexEnum = null;

        switch (viewId) {
            case R.id.rbTabHome:
                tHomeTabIndexEnum = HomeTabIndexEnum.TAB_HOME_INDEX;
                titleLeft.setVisibility(View.VISIBLE);
                titleHuDong.setVisibility(View.VISIBLE);
                titleRight.setVisibility(View.VISIBLE);
                titleCnter.setVisibility(View.GONE);
                break;
            case R.id.rbTabLovePanda:
                tHomeTabIndexEnum = HomeTabIndexEnum.TAB_HOME_PANDA_LIVE;
                titleCnter.setText("熊猫直播");
                titleLeft.setVisibility(View.GONE);
                titleHuDong.setVisibility(View.GONE);
                titleRight.setVisibility(View.VISIBLE);
                titleCnter.setVisibility(View.VISIBLE);
                break;
            case R.id.rbTabLiveChina:
                tHomeTabIndexEnum = HomeTabIndexEnum.TAB_HOME_LIVE_CHINA;
                titleCnter.setText("直播中国");
                titleLeft.setVisibility(View.GONE);
                titleHuDong.setVisibility(View.GONE);
                titleRight.setVisibility(View.VISIBLE);
                titleCnter.setVisibility(View.VISIBLE);
                break;
            case R.id.rbTabPandCulture:
                tHomeTabIndexEnum = HomeTabIndexEnum.TAB_HOME_PANDA_CULTURE;
                titleCnter.setText("熊猫文化");
                titleLeft.setVisibility(View.GONE);
                titleHuDong.setVisibility(View.GONE);
                titleRight.setVisibility(View.VISIBLE);
                titleCnter.setVisibility(View.VISIBLE);
                break;
            case R.id.rbTabPandaEye:
                tHomeTabIndexEnum = HomeTabIndexEnum.TAB_HOME_PANDA_EYE;
                titleLeft.setVisibility(View.GONE);
                titleHuDong.setVisibility(View.GONE);
                titleRight.setVisibility(View.VISIBLE);
                titleCnter.setVisibility(View.VISIBLE);
                titleCnter.setText("熊猫观察");
                break;
        }
        if (tHomeTabIndexEnum == mHomeTabIndexEnum) {

            return;
        } else {

            setCurrentFragment(tHomeTabIndexEnum);
        }

    }

    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(key_selected_index, mHomeTabIndexEnum.ordinal());
        super.onSaveInstanceState(outState);
    }

    public static String getpath_reflect(Context mContext) {
        boolean flag = true;
        String extSdCard = "";
        StorageManager sm = (StorageManager) mContext
                .getSystemService(STORAGE_SERVICE);
        // 获取sdcard的路径：外置和内置
        try {
            String[] paths = (String[]) sm.getClass()
                    .getMethod("getVolumePaths").invoke(sm);
            String esd = Environment.getExternalStorageDirectory().getPath();
            for (int i = 0; i < paths.length; i++) {
                if (paths[i].equals(esd)) {
                    continue;
                }
                File sdFile = new File(paths[i]);
                if (sdFile.canWrite()) {
                    extSdCard = paths[i];
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if ("".equals(extSdCard)) {
            flag = false;
        }

        return extSdCard;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.title_inter:

                Intent tIntent = new Intent(MainActivity.this,
                        InteractionActivity.class);
                //统计
                MobileAppTracker.trackEvent("互动", "", "首页", 0, null, null, MainActivity.this);
                MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                Log.e("统计","事件名称:"+"互动"+"***事件类别:"+"null"+"***事件标签:"+"首页");
                // 熊猫观察图文底层页
                this.startActivity(tIntent);
                break;
            case R.id.tv_search:
                Intent searchIntent = new Intent(MainActivity.this,
                        SearchActivity.class);
                this.startActivity(searchIntent);

                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            findViewById(R.id.activity_main_title).setVisibility(View.GONE);
            findViewById(R.id.ieMainBottom).setVisibility(View.GONE);
        } else {
            findViewById(R.id.activity_main_title).setVisibility(View.VISIBLE);
            findViewById(R.id.ieMainBottom).setVisibility(View.VISIBLE);
        }

    }

    // 播放有关
    public void getLiveRestrict() {

        // JSONObject object = new JSONObject();
        HashMap<String, String> map = new HashMap<String, String>();

        mHandler1
                .getHttpPostJson(
                        "http://player.cntv.cloudcdn.net:81/liveconfig/livechina_000.app",
                        map, CeLueData.class, LIST_DATA1);

    }

    // 此处是注册的回调处理
    // 参考集成文档的1.7.10
    // http://dev.umeng.com/push/android/integration#1_7_10
    public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {

        @Override
        public void onRegistered(String registrationId) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    updateStatus();
                }
            });
        }
    };

    public Handler handler = new Handler();
    // 此处是注销的回调处理
    // 参考集成文档的1.7.10
    // http://dev.umeng.com/push/android/integration#1_7_10
    public IUmengUnregisterCallback mUnregisterCallback = new IUmengUnregisterCallback() {

        @Override
        public void onUnregistered(String registrationId) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    updateStatus();
                }
            });
        }
    };

    private void switchPush() {
        String info = String.format("enabled:%s  isRegistered:%s",
                mPushAgent.isEnabled(), mPushAgent.isRegistered());

        // btnEnable.setClickable(false);
        if (mPushAgent.isEnabled()
                || UmengRegistrar.isRegistered(MainActivity.this)) {
            // 开启推送并设置注册的回调处理
            mPushAgent.disable(mUnregisterCallback);
        } else {
            // 关闭推送并设置注销的回调处理
            mPushAgent.enable(mRegisterCallback);
        }
    }

    private void updateStatus() {
        String pkgName = getApplicationContext().getPackageName();
        String info = String.format(
                "enabled:%s  isRegistered:%s  DeviceToken:%s "
                        + "SdkVersion:%s AppVersionCode:%s AppVersionName:%s",
                mPushAgent.isEnabled(), mPushAgent.isRegistered(),
                mPushAgent.getRegistrationId(), MsgConstant.SDK_VERSION,
                UmengMessageDeviceConfig.getAppVersionCode(this),
                UmengMessageDeviceConfig.getAppVersionName(this));
        copyToClipBoard();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    private void copyToClipBoard() {
        if (Build.VERSION.SDK_INT < 11)
            return;
        String deviceToken = mPushAgent.getRegistrationId();
        if (!TextUtils.isEmpty(deviceToken)) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboard.setText(deviceToken);
            // toast("DeviceToken已经复制到剪贴板了");
        }
    }


    private long mLastBackTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (mHomeTabIndexEnum != null) {
                // 将返回键分给fragment处理
                Fragment currentFragment = mFragmentManager
                        .findFragmentByTag(mHomeTabIndexEnum.toString());
                if (mHomeTabIndexEnum == HomeTabIndexEnum.TAB_HOME_LIVE_CHINA) {
                    LiveChinaFragment liveChinaFragment = (LiveChinaFragment) currentFragment;
                    // 消费了返回事件
                    boolean onkeyDownBack = liveChinaFragment.onkeyDownBack();
                    if (onkeyDownBack) {
                        return true;
                    }
                } else if (mHomeTabIndexEnum == HomeTabIndexEnum.TAB_HOME_PANDA_LIVE) {
                    LovePandaFragment lovePandaFragment = (LovePandaFragment) currentFragment;
                    // 消费了返回事件
                    boolean onkeyDownBack = lovePandaFragment.onkeyDownBack();
                    if (onkeyDownBack) {
                        return true;
                    }
                }
            }
            long now = new Date().getTime();
            if (now - mLastBackTime < 2000) {

                // getSharedPreferences("config",
                // MODE_PRIVATE).edit().clear().commit;
                return super.onKeyDown(keyCode, event);
            } else {
                mLastBackTime = now;
                Toast.makeText(MainActivity.this, R.string.click_two_exit_app,
                        Toast.LENGTH_SHORT).show();

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!AppContext.isPush) {
            requestHisData();
        } else {
            AppContext.isPush = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheUtil.mRbLovePanda = null;
        CacheUtil.mRbLiveChina = null;
    }
}
