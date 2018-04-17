package cn.cntv.app.ipanda.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.CollectPageSourceEnum;
import cn.cntv.app.ipanda.constant.CollectTypeEnum;
import cn.cntv.app.ipanda.constant.Constants;
import cn.cntv.app.ipanda.constant.JMPTypeEnum;
import cn.cntv.app.ipanda.constant.PandaEyeAddressEnum;
import cn.cntv.app.ipanda.constant.WebAddressEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.ui.cctv.activity.CCTVDetailActivity;
import cn.cntv.app.ipanda.ui.cctv.entity.MainData;
import cn.cntv.app.ipanda.ui.cctv.entity.MainModel;
import cn.cntv.app.ipanda.ui.home.activity.HomeSSubjectActivity;
import cn.cntv.app.ipanda.ui.home.activity.InterDetailActivity;
import cn.cntv.app.ipanda.ui.pandaeye.activity.PandaEyeDetailActivity;
import cn.cntv.app.ipanda.ui.pandaeye.entity.ImageData;
import cn.cntv.app.ipanda.ui.play.PlayLiveActivity;
import cn.cntv.app.ipanda.ui.play.PlayVodFullScreeActivity;
import cn.cntv.app.ipanda.ui.play.entity.PlayLiveEntity;
import cn.cntv.app.ipanda.ui.play.entity.PlayVodEntity;
import cn.cntv.app.ipanda.utils.PEDPUtils;
import cn.cntv.app.ipanda.utils.PermissionUtil;
import cn.cntv.app.ipanda.utils.SharePreferenceUtil;
import cn.cntv.app.ipanda.utils.ToastUtil;
import cn.cntv.app.ipanda.xlistview.Mypanda;

public class WelcomActivity extends Activity {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 10;

    private static final int LUNCHIMG = 2;

    public int displayHeight;
    public int displayWidth;
    private Bitmap bmp;
    private ImageView loadImg;
    private SharePreferenceUtil mPreference;

    private static final int LIST_DATA3 = 3;

    private ScheduledExecutorService mScheduledExecutorService;

    private Object lock = new Object();
    private boolean toRequestPm;

    private boolean isNetError;

    private Handler mHandler = new Handler();

    private Runnable go = new Runnable() {

        @Override
        public void run() {

            try {
                synchronized (lock) {
                    while (toRequestPm) {
                        lock.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isFinishing()) {
                return;
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isNetError) {
                        Intent intent = new Intent(WelcomActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        WelcomActivity.this.finish();
                    } else {
                        nav();
                    }
                }
            });

        }
    };


    private void nav() {
        String tUrl = mPreference.getWebAddress(WebAddressEnum.WEB_HOME
                .toString());

        if (tUrl == null || tUrl.equals("")) {

            Toast.makeText(WelcomActivity.this, R.string.load_try_again,
                    Toast.LENGTH_SHORT).show();
            WelcomActivity.this.finish();

        } else {

            SharedPreferences guid = getSharedPreferences("guide",
                    Activity.MODE_PRIVATE);

            String times = guid.getString("val", "no");

            if (times.equals("no")) {
                Intent intent = new Intent(WelcomActivity.this,
                        MyGuideActivity.class);
                startActivity(intent);
                finish();
            } else {
                if (isFinishing()) {
                    return;
                }

                String versionCodeSave = guid.getString("code", "no");

                String tVersionCode = "";
                try {
                    tVersionCode = getPackageManager().getPackageInfo(
                            getPackageName(), 0).versionCode + "";
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                if (!versionCodeSave.equals(tVersionCode) || versionCodeSave == null) {
                    Intent intent = new Intent(WelcomActivity.this,
                            MyGuideActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    Intent intent = new Intent(WelcomActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    WelcomActivity.this.finish();
                    //获得URL数据
                    getUrlValue(getIntent());
                }
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {

                for (int grant : grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        ToastUtil.showShort(this, "请授权必要的权限");
                        finish();
                        break;
                    }
                }
                synchronized (lock) {
                    toRequestPm = false;
                    lock.notifyAll();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private XjlHandler<Object> mHandler1 = new XjlHandler<Object>(
            new HandlerListener() {

                @Override
                public void handlerMessage(HandlerMessage msg) {

                    // dismissLoadDialog();

                    switch (msg.what) {
                        case Integer.MAX_VALUE:
                            isNetError = true;
                            mScheduledExecutorService.schedule(go, 2000, TimeUnit.MILLISECONDS);
                            break;
                        case LUNCHIMG:
                            // 处理tab数据
                            ImageData data1 = (ImageData) msg.obj;

                            if (null != data1) {
                                String imgPath = data1.getData().get(0)
                                        .getPhoneImg().trim();

                                if (imgPath.length() != 0) {

                                    PEDPUtils.getLoadImage(getCacheDir().getPath(),
                                            imgPath, new ImageView(
                                                    WelcomActivity.this));

                                    String img_file_path = Constants.cache
                                            + imgPath.substring(
                                            imgPath.lastIndexOf("/") + 1,
                                            imgPath.length());
                                    bmp = BitmapFactory.decodeFile(img_file_path);
                                    mSharedPreferences
                                            .edit()
                                            .putString("welcome_img_path",
                                                    img_file_path).commit();
                                    if (bmp != null) {
                                        bmp = Bitmap.createScaledBitmap(bmp,
                                                displayWidth, displayHeight, true);
                                        loadImg.setImageBitmap(bmp);
                                    }
                                }

                            }

                            break;

                        case LIST_DATA3:

                            MainData data3 = (MainData) msg.obj;

                            if (null != data3 && data3.getTab().size() > 0) {

                                for (int i = 0; i < data3.getTab().size(); i++) {

                                    MainModel mainModel = (MainModel) data3
                                            .getTab().get(i);
                                    mPreference.setWeb(mainModel.getTitle().trim(),
                                            mainModel.getUrl().trim());

                                }
                                mScheduledExecutorService.schedule(go, 2500, TimeUnit.MILLISECONDS);
//                                handler.sendEmptyMessageDelayed(1, 1500);
                            } else {
                                Toast.makeText(WelcomActivity.this, R.string.load_try_again,
                                        Toast.LENGTH_SHORT).show();
                                WelcomActivity.this.finish();
                            }

                            // Log.e("model","data3="+ data3.toString());

                            break;

                    }
                }
            });

    SharedPreferences mSharedPreferences;
    String welcome_img_path = "";
    private ImageView collectIv;
    private String url;
    private String image;
    private String title;
    private String videoLength;
    private String guid;


    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        setContentView(R.layout.activity_welcom);

        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        mPreference = new SharePreferenceUtil(WelcomActivity.this);

        getAllModelBaseUrl();

        if (PermissionUtil.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_ASK_PERMISSIONS)) {
            toRequestPm = true;
        }
        initView();

        // requestTopData();

        displayWidth = getResources().getDisplayMetrics().widthPixels;
        displayHeight = getResources().getDisplayMetrics().heightPixels;
        mSharedPreferences = getSharedPreferences("welcome_page",
                Context.MODE_PRIVATE);
        String imgfile;
        if (!TextUtils.isEmpty(imgfile = mSharedPreferences.getString(
                "welcome_img_path", ""))) {
            bmp = BitmapFactory.decodeFile(imgfile);
            bmp = Bitmap.createScaledBitmap(bmp, displayWidth, displayHeight,
                    true);
            loadImg.setImageBitmap(bmp);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView() {

        loadImg = (ImageView) this.findViewById(R.id.load_img);
        collectIv = (ImageView) this.findViewById(R.id.app_video_collect);

    }

    private void getAllModelBaseUrl() {
        mHandler1.getHttpJson(PandaEyeAddressEnum.ALL_MODEL_URL.toString(),
                MainData.class, LIST_DATA3);
    }


    /*得到ＵＲＬ并获取到数数据*/
    public void getUrlValue(Intent i_getvalue) {
        String action = i_getvalue.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {

            Uri uri = i_getvalue.getData();
            Log.i("URL2", "url:" + uri);
            if (uri != null) {
                final String data = uri.getQueryParameter("data");
                Log.i("URL1", "data:" + data);
                Gson gson = new Gson();
                Mypanda mpanda = gson.fromJson(data.toString(), Mypanda.class);
                String id = mpanda.getId();
                image = mpanda.getImage();
                url = mpanda.getUrl();
                title = mpanda.getTitle();
                videoLength = mpanda.getVideoLength();
                guid = mpanda.getGuid();
                Log.i("URL1", "uri:" + uri);
                Log.i("URL1", "uri:" + uri);
                Log.i("URL1", "id:" + id);
                Log.i("URL1", "url:" + url);
                Log.i("URL1", "image:" + image);
                Log.i("URL1", "title:" + title);
                Log.i("URL1", "videoLength:" + videoLength);
                if (id != null & Integer.parseInt(id) == 1) {
                    // 视频集播放点播
                    Intent tIntent = new Intent(WelcomActivity.this,
                            CCTVDetailActivity.class);
                    tIntent.putExtra("id", url);
                    tIntent.putExtra("title", title);
                    tIntent.putExtra("image", image);
                    tIntent.putExtra("videoLength", videoLength);
                    startActivity(tIntent);
                } else if (id != null & Integer.parseInt(id) == 2) {
                    //视频直播
                    Intent tIntent = new Intent(WelcomActivity.this,
                            PlayLiveActivity.class);
                    tIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PlayLiveEntity playLiveEntity = new PlayLiveEntity(
                            url, title, image, null, guid,
                            CollectTypeEnum.PD.value() + "",
                            CollectPageSourceEnum.PDZB.value(), true);
                    tIntent.putExtra("live", playLiveEntity);
                    tIntent.putExtra("listlive", (Serializable) null);
                    startActivity(tIntent);

                } else if (id != null & Integer.parseInt(id) == 3) {
                    // 单视频
                    // 用户点击消息:点播播放器
                    PlayVodEntity tEntity = new PlayVodEntity(
                            CollectTypeEnum.SP.value() + "", url, null,
                            null, image, title, guid,
                            JMPTypeEnum.VIDEO_VOD.value(), videoLength);
                    Intent tIntent = new Intent(WelcomActivity.this,
                            PlayVodFullScreeActivity.class);
                    //tIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    tIntent.putExtra("vid", tEntity);
                    startActivity(tIntent);
                } else if (id != null & Integer.parseInt(id) == 4) {
                    // 用户点击消息:H5页面地址内部浏览器
                    Intent tIntent = new Intent(WelcomActivity.this,
                            InterDetailActivity.class);
                    tIntent.putExtra("url", url);
                    tIntent.putExtra("title", title);
                    tIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(tIntent);

                } else if (id != null & Integer.parseInt(id) == 5) {
                    // 用户点击消息:专题
                    Intent tIntent = new Intent(WelcomActivity.this,
                            HomeSSubjectActivity.class);
                    tIntent.putExtra("url", url);
                    tIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(tIntent);
                } else if (id != null & Integer.parseInt(id) == 6) {
                    // 图文底层页
                    Intent tIntent = new Intent(WelcomActivity.this,
                            PandaEyeDetailActivity.class);
                    tIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    tIntent.putExtra(PandaEyeDetailActivity.TYPE, PandaEyeDetailActivity.TYPE_ARTICLE);
                    tIntent.putExtra("url", guid);
                    tIntent.putExtra("title", title.replace("\\\"", "\""));
                    tIntent.putExtra("pic", image);
                    tIntent.putExtra("timeval", videoLength);
                    tIntent.putExtra("id", url);
                    startActivity(tIntent);
                }


            }
        }
    }


}
