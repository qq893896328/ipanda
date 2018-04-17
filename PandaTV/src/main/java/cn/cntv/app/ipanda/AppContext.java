package cn.cntv.app.ipanda;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.gridsum.mobiledissector.MobileAppTracker;
import com.gridsum.mobiledissector.util.StringUtil;
import com.gridsum.videotracker.VideoTracker;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import cn.cntv.app.ipanda.constant.CollectPageSourceEnum;
import cn.cntv.app.ipanda.constant.CollectTypeEnum;
import cn.cntv.app.ipanda.constant.JMPTypeEnum;
import cn.cntv.app.ipanda.di.components.ApplicationComponent;
import cn.cntv.app.ipanda.di.components.DaggerApplicationComponent;
import cn.cntv.app.ipanda.di.modules.ApplicationModule;
import cn.cntv.app.ipanda.manager.WeiboShareManager;
import cn.cntv.app.ipanda.ui.MainActivity;
import cn.cntv.app.ipanda.ui.cctv.activity.CCTVDetailActivity;
import cn.cntv.app.ipanda.ui.cctv.entity.MainModel;
import cn.cntv.app.ipanda.ui.home.activity.HomeSSubjectActivity;
import cn.cntv.app.ipanda.ui.home.activity.InterDetailActivity;
import cn.cntv.app.ipanda.ui.home.auto.layout.config.AutoLayoutConifg;
import cn.cntv.app.ipanda.ui.pandaeye.activity.CeLueChannelInfoModel;
import cn.cntv.app.ipanda.ui.pandaeye.activity.PandaEyeDetailActivity;
import cn.cntv.app.ipanda.ui.play.PlayLiveActivity;
import cn.cntv.app.ipanda.ui.play.PlayVodFullScreeActivity;
import cn.cntv.app.ipanda.ui.play.entity.PlayLiveEntity;
import cn.cntv.app.ipanda.ui.play.entity.PlayVodEntity;
import cn.cntv.app.ipanda.utils.CrashHandler;
import cn.cntv.app.ipanda.utils.SharePreferenceUtil;

public class AppContext extends Application {

    private static AppContext mApplication;
    public static final String SP_FILE_NAME = "SP_FILE";

    private SharePreferenceUtil mSpUtil;
    private PushAgent mPushAgent;
    //public UMSocialService mController;
    private static List<CeLueChannelInfoModel> liveRestrictBeans;
    private static List<MainModel> tablist;

    public static boolean isPush;// 标识是否为推送


    private ApplicationComponent applicationComponent;

    public static List<MainModel> getTablist() {
        return tablist;
    }

    public static void setTablist(List<MainModel> tablist) {
        AppContext.tablist = tablist;
    }

    public static List<CeLueChannelInfoModel> getLiveRestrictBeans() {
        return liveRestrictBeans;
    }

    public static void setLiveRestrictBeans(
            List<CeLueChannelInfoModel> liveRestrictBeans) {
        AppContext.liveRestrictBeans = liveRestrictBeans;
    }

    public synchronized static AppContext getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        LeakCanary.install(this);

        MobileAppTracker.initialize(this);
        //数据发送地址
        VideoTracker.setConfigSource("http://app.cntv.cn/special/gridsum/");
        //备份地址
        VideoTracker.setBackupConfigSource("http://cfg-vd.gridsumdissector.com/");
        MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);


        AutoLayoutConifg.getInstance().useDeviceSize().init(this);
        mApplication = this;

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        initData();

        AppConfig.getAppConfig().init(this);

        WeiboShareManager.get(this).onCreate();


        // 初始化友盟推送
        initUMengPush();

        initializeInjector();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }


    private void initializeInjector() {

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    private void initData() {
        mSpUtil = new SharePreferenceUtil(this, SP_FILE_NAME);
    }

    public synchronized SharePreferenceUtil getSpUtil() {
        if (mSpUtil == null)
            mSpUtil = new SharePreferenceUtil(this, SP_FILE_NAME);
        return mSpUtil;
    }

    public void initUMengPush() {
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(false);

        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 参考集成文档的1.6.3 http://dev.umeng.com/push/android/integration#1_6_3
             * */
            @Override
            public void dealWithCustomMessage(final Context context,
                                              final UMessage msg) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            // 自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext())
                                    .trackMsgClick(msg);
                        } else {
                            // 自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext())
                                    .trackMsgDismissed(msg);
                        }
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            /**
             * 参考集成文档的1.6.4 http://dev.umeng.com/push/android/integration#1_6_4
             * */
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                switch (msg.builder_id) {
                    case 1:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                                context);
                        RemoteViews myNotificationView = new RemoteViews(
                                context.getPackageName(),
                                R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title,
                                msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text,
                                msg.text);
                        myNotificationView.setImageViewBitmap(
                                R.id.notification_large_icon,
                                getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(
                                R.id.notification_small_icon,
                                getSmallIconId(context, msg));
                        builder.setContent(myNotificationView);
                        builder.setContentTitle(msg.title).setContentText(msg.text)
                                .setTicker(msg.ticker).setAutoCancel(true);
                        Notification mNotification = builder.build();
                        // 由于Android
                        // v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                        mNotification.contentView = myNotificationView;
                        return mNotification;
                    default:
                        // 默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK 参考集成文档的1.6.2
         * http://dev.umeng.com/push/android/integration#1_6_2
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                JSONObject json;
                try {
                    json = new JSONObject(msg.custom);
                    if (json == null) {
                        return;
                    }
                    String flag = "-1";
                    if (json.has("id")) {
                        flag = json.getString("id");
                    }
                    String id = "";
                    if (json.has("url")) {
                        id = json.getString("url");
                    }
                    String title = "";
                    if (json.has("aps")) {
                        String aps = json.getString("aps");
                        if (!StringUtil.isNullOrEmpty(aps)) {
                            JSONObject apsJson = new JSONObject(aps);
                            if (apsJson != null && apsJson.has("alert")) {
                                title = apsJson.getString("alert");
                            }
                        }
                    }
                    String image = "";
                    if (json.has("image")) {
                        image = json.getString("image");
                    }
                    String videoLength = "";
                    if (json.has("videoLength")) {
                        videoLength = json.getString("videoLength");
                    }
                    String url = "";
                    if (json.has("url")) {
                        url = json.getString("url");
                    }
                    if (!StringUtil.isNullOrEmpty(flag)) {
                        Intent intent1;
                        intent1 = new Intent(context, MainActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        isPush = true;
                        switch (Integer.parseInt(flag)) {
                            case 1:
                                // 用户点击消息：启动点播详情页；
                                intent1 = new Intent(context,
                                        CCTVDetailActivity.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent1.putExtra("id", id);
                                intent1.putExtra("title", title);
                                intent1.putExtra("image", image);
                                intent1.putExtra("videoLength", videoLength);
                                startActivity(intent1);
                                break;
                            case 2:
                                // 用户点击消息:直播播放器
                                intent1 = new Intent(context,
                                        PlayLiveActivity.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                PlayLiveEntity playLiveEntity = new PlayLiveEntity(
                                        id, title, image, null, null,
                                        CollectTypeEnum.PD.value() + "",
                                        CollectPageSourceEnum.PDZB.value(), true);
                                intent1.putExtra("live", playLiveEntity);
                                intent1.putExtra("listlive", (Serializable) null);
                                startActivity(intent1);
                                break;
                            case 3:
                                // 用户点击消息:点播播放器
                                PlayVodEntity tEntity = new PlayVodEntity(
                                        CollectTypeEnum.SP.value() + "", id, null,
                                        null, image, title, null,
                                        JMPTypeEnum.VIDEO_VOD.value(), videoLength);
                                intent1 = new Intent(context,
                                        PlayVodFullScreeActivity.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent1.putExtra("vid", tEntity);
                                startActivity(intent1);
                                break;
                            case 4:
                                // 用户点击消息:H5页面地址内部浏览器
                                intent1 = new Intent(context,
                                        InterDetailActivity.class);
                                intent1.putExtra("url", url);
                                intent1.putExtra("title", title);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent1);
                                break;
                            case 5:
                                // 用户点击消息:专题
                                intent1 = new Intent(context,
                                        HomeSSubjectActivity.class);
                                intent1.putExtra("url", url);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent1);
                                break;
                            case 6:
                                // 图文底层页
                                intent1 = new Intent(context,
                                        PandaEyeDetailActivity.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent1.putExtra(PandaEyeDetailActivity.TYPE, PandaEyeDetailActivity.TYPE_ARTICLE);

                                intent1.putExtra("url", "");
                                intent1.putExtra("title", title);
                                intent1.putExtra("pic", image);
                                intent1.putExtra("timeval", videoLength);
                                intent1.putExtra("id", id);
                                startActivity(intent1);
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        // 使用自定义的NotificationHandler，来结合友盟统计处理消息通知
        // 参考http://bbs.umeng.com/thread-11112-1-1.html
        // CustomNotificationHandler notificationClickHandler = new
        // CustomNotificationHandler();
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

    }

}
