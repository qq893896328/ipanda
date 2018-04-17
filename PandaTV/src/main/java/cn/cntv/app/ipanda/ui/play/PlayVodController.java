package cn.cntv.app.ipanda.ui.play;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gridsum.mobiledissector.util.StringUtil;
import com.gridsum.videotracker.GSVideoState;
import com.gridsum.videotracker.VideoTracker;
import com.gridsum.videotracker.entity.VideoInfo;
import com.gridsum.videotracker.entity.VodMetaInfo;
import com.gridsum.videotracker.play.VodPlay;
import com.gridsum.videotracker.provider.IVodInfoProvider;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cntv.app.ipanda.AppConfig;
import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.WebAddressEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.db.DBInterface;
import cn.cntv.app.ipanda.db.entity.FavoriteEntity;
import cn.cntv.app.ipanda.db.entity.PlayHistoryEntity;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVData;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVVideo;
import cn.cntv.app.ipanda.ui.play.entity.PlayVodEntity;
import cn.cntv.app.ipanda.utils.ComonUtils;
import cn.cntv.app.ipanda.utils.NetUtil;
import cn.cntv.app.ipanda.utils.PopWindowUtils;
import cn.cntv.app.ipanda.utils.VodUtil;
import cn.cntv.app.ipanda.vod.IjkVideoView;
import cn.cntv.app.ipanda.vod.entity.PlayModeBean;
import cn.cntv.app.ipanda.vod.entity.VodHlsModel;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by tcking on 15/10/27. 直播播放控制
 */
public class PlayVodController {

    /**
     * 可能会剪裁,保持原视频的大小，显示在中心,当原视频的大小超过view的大小超过部分裁剪处理
     */
    public static final String SCALETYPE_FITPARENT = "fitParent";
    /**
     * 可能会剪裁,等比例放大视频，直到填满View为止,超过View的部分作裁剪处理
     */
    public static final String SCALETYPE_FILLPARENT = "fillParent";
    /**
     * 将视频的内容完整居中显示，如果视频大于view,则按比例缩视频直到完全显示在view中
     */
    public static final String SCALETYPE_WRAPCONTENT = "wrapContent";
    /**
     * 不剪裁,非等比例拉伸画面填满整个View
     */
    public static final String SCALETYPE_FITXY = "fitXY";
    /**
     * 不剪裁,非等比例拉伸画面到16:9,并完全显示在View中
     */
    public static final String SCALETYPE_16_9 = "16:9";

//    private static final String TAG = "@@@PlayVodController";

    /**
     * 不剪裁,非等比例拉伸画面到4:3,并完全显示在View中
     */
    public static final String SCALETYPE_4_3 = "4:3";
    private static final int MESSAGE_SHOW_PROGRESS = 1;
    private static final int MESSAGE_FADE_OUT = 2;
    private static final int MESSAGE_SEEK_NEW_POSITION = 3;
    private static final int MESSAGE_HIDE_CENTER_BOX = 4;
    private static final int MESSAGE_RESTART_PLAY = 5;
    private final Activity activity;
    private final IjkVideoView videoView;
    private final SeekBar seekBar; // 播放进度条
    private final SeekBar volumeSeekBar; // 声音进度条

    private final AudioManager audioManager;
    private final int mMaxVolume;
    private String url;
    private Query $;
    private static final int STATUS_ERROR = -1;
    public static final int STATUS_IDLE = 0;
    private static final int STATUS_LOADING = 1;
    public static final  int STATUS_PLAYING = 2;
    private static final int STATUS_PAUSE = 3;
    public static final int STATUS_COMPLETED = 4;
    private long pauseTime;
    public  int status = STATUS_IDLE;
    private boolean isLive = true;// 是否为直播（待定）
    private OrientationEventListener orientationEventListener;
    private int initHeight;
    private int defaultTimeout = 5000;
    private int screenWidthPixels;

    // 网络状态判断 音量监听
    IntetnetReceiver internetVolumeReceiver;
    private int lastInternetType = -1;
    private static final int INTER_WIFI = 11;
    private static final int INTER_OTHER = 12;
    private static final int INTER_NONET = 13;
    Dialog internetDialog;

    private Boolean hasGaoqing = false;//是否有高清
    private Boolean isBiaoqing = false;//是否标清
    private TextView liuchang, biaoqing, gaoqing;
    // private ListView stateListView;

    // 播放器网络数据
    private String playUrl = "http://vdn.apps.cntv.cn/api/getVideoInfoForCBox.do";

    private String mplayUrl;// 点播一级播放地址，根据该地址，重新网络请求，获取不同码率视频地址

    private static final int LIST_DATA1 = 20;
    private static final int PLAYSIGN = 21;
    private static final int GAOQING3 = 22;
    private static final int COLLECTION = 23;

    private ArrayList<PlayModeBean> playUrls;

    private List<CCTVVideo> listVideos;

    private int playPosition;// 记录播放位置

    private String currentID;// 当前播放单视频ID

    // private int lastItemClickPosition; //记录播放的第几个视频

    private ChannelChangeVodAdapter channelChangeAdapter;
    private StateChangeAdapter stateAdapter;
    // private List<StateEntity> listSign;//存放标清，高清

    private String videoType;
    private String videoSign = "no";// yes表示视频ID与视频集ID都存在，no表示只存在视频集ID

    private ListView listView;// 频道切换view

    // 清晰度切换
    private PopupWindow mPopupQingXiWindow;
    private static final byte QING_XI_BIAOQING = 1;
    private static final byte QING_XI_GAOQING = 2;
    private byte currentQingXiDu = QING_XI_GAOQING;

    // 频道列表切换
    private PopupWindow mPopupChannelChange;

    // 频道列表偏移量
    int pingdaoxoff;

    private String shareTitle = "", shareText, shareUrl,
            shareImgPath = AppConfig.DEFAULT_IMAGE_PATH + "share_logo.png";

    private PlayVodEntity entity;
    private int collectLocalFlag = -1;// 本地收藏标识(0，执行收藏；1，执行取消收藏)
    private ImageView collectIv;

    // 判断是否监听了广播（包含网络，声音）
    private boolean isRegisterReceiver;
    // 第一次进来播放视频（处理第一次注册广播）
    private boolean isFirstrequestVod = true;

    // 是否自动播放下一集
    private boolean isAutoPlayNextVideo;

    // 播放的视频检查是否在移动网络
    private boolean isCheckNetWork = true;
    private boolean isHavePlayUrl = false;// 视频已经播放了

    // 4g下取消视频播放（解决onResume时候，重复弹出是否继续播放的框）
    private boolean isCancelVideo = false;

    // //第一次状态为status_playing
    // private boolean isFirstSTATUS_PLAYING = true;

    private long playtime = System.currentTimeMillis(); // 视频开始播放的时间

    //统计部分
    private VideoTracker mTracker;
    private VodPlay mVodPlay;
    private VodMetaInfo mVodMetaInfo;
    private boolean mIsBuffer;

    private int mDefinition = 0; //记录视频清晰度,0为没有手动设置清晰度，1为标清，2为高清

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.app_video_qingxidu_bianqing||v.getId() ==R.id.tv_biaoqing_btn) {
                mDialog.dismiss();

                if (currentQingXiDu == QING_XI_BIAOQING) {
                    mPopupQingXiWindow.dismiss();
                    return;
                }

                mDefinition = 1;
                for (int i = 0; i < playUrls.size(); i++) {
                    PlayModeBean modeBean = playUrls.get(i);
                    if (null != modeBean) {
                        if ("标清".equals(modeBean.getTitle())) {
                            isBiaoqing = true;
                            currentQingXiDu = QING_XI_BIAOQING;
                            currentPosition = videoView.getCurrentPosition();
                            play(modeBean.getPlayUrl());
                            videoView.seekTo(currentPosition);
                            if(mPopupQingXiWindow != null)
                                mPopupQingXiWindow.dismiss();

                            $.id(R.id.app_video_qingxidu).text("标清");
                            showNetToast("您已切换至标清播放");
                        }
                    }
                }

            } else if (v.getId() == R.id.app_video_qingxidu_gaoqing) {
                if (currentQingXiDu == QING_XI_GAOQING) {
                    mPopupQingXiWindow.dismiss();
                    return;
                }

                mDefinition = 2;
                for (int i = 0; i < playUrls.size(); i++) {
                    PlayModeBean modeBean = playUrls.get(i);
                    if (null != modeBean) {
                        if ("高清".equals(modeBean.getTitle())) {
                            currentQingXiDu = QING_XI_GAOQING;
                            currentPosition = videoView.getCurrentPosition();
                            play(modeBean.getPlayUrl());
                            videoView.seekTo(currentPosition);
                            mPopupQingXiWindow.dismiss();

                            $.id(R.id.app_video_qingxidu).text("高清");
                            showNetToast("您已切换至高清播放");

                        }
                    }
                }

            }else if(v.getId() ==R.id.tv_close_btn) {
                mDialog.dismiss();
            }else if (v.getId() == R.id.app_video_fullscreen) {
                if (getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                }
                updateFullScreenButton();
            } else if (v.getId() == R.id.app_video_play) {
                doPauseResume();
                show(defaultTimeout);
            } else if (v.getId() == R.id.app_video_play_bottom) {
                doPauseResume();
                show(defaultTimeout);
            } else if (v.getId() == R.id.app_video_replay_icon) {
                videoView.seekTo(0);
                videoView.start();
                doPauseResume();
            } else if (v.getId() == R.id.app_video_finish) {
                if (!fullScreenOnly && !portrait) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    activity.finish();
                }
            } else if (v.getId() == R.id.app_video_collect) {
                if (collectLocalFlag == 0) {
                    // 本地添加收藏
                    localAddCollect();
                } else if (collectLocalFlag == 1) {
                    // 本地取消收藏
                    localCancelCollect();
                }
            } else if (v.getId() == R.id.app_video_share) {
                String videoLength=entity.getTimeLegth();

                if (TextUtils.isEmpty(videoLength)) {
                    videoLength=generateTime(videoView.getDuration());
                }

                //修改分享
                shareController.showPopWindowVideo(v,
                        entity.getTitle(), entity.getVid(), "3",videoLength , entity.getImg(), entity.getVid());

                Log.e("时长",videoLength);
            } else if (v.getId() == R.id.app_video_menu) {

                if (null != listVideos) {
                    // 显示频道菜单
                    // Toast.makeText(activity, "点击了菜单", 0).show();

                    if (mPopupChannelChange != null) {

                        mPopupChannelChange.showAsDropDown(
                                $.id(R.id.app_video_top_box).view, pingdaoxoff,
                                0);
                        showBottomControl(false);
                    } else {
                        View pingdaoview = View.inflate(activity,
                                R.layout.popupwindow_channel_change_vod, null);
                        listView = (ListView) pingdaoview
                                .findViewById(R.id.channel_change_listview);
                        listView.setOnScrollListener(new OnScrollListener() {

                            @Override
                            public void onScrollStateChanged(AbsListView view,
                                                             int scrollState) {
                                switch (scrollState) {
                                    case SCROLL_STATE_TOUCH_SCROLL:
                                        show(3600000);
                                        break;
                                    case SCROLL_STATE_IDLE:
                                        show(defaultTimeout);
                                        break;
                                }
                            }

                            @Override
                            public void onScroll(AbsListView view,
                                                 int firstVisibleItem, int visibleItemCount,
                                                 int totalItemCount) {

                            }
                        });

                        channelChangeAdapter = new ChannelChangeVodAdapter(
                                activity, listVideos);
                        listView.setAdapter(channelChangeAdapter);
                        listView.setOnItemClickListener(channelItemClick);

                        // 定位判断，当前播放那条，跳到当前播放那条的位置
                        if (null != currentID) {
                            for (int i = 0; i < listVideos.size(); i++) {

                                CCTVVideo videoModel = listVideos.get(i);
                                if (null == videoModel) {
                                    continue;
                                } else {
                                    if (currentID.equals(videoModel.getVid()
                                            .trim())) {
                                        videoModel.setSign(1);

                                        if (null != channelChangeAdapter) {

                                            listView.setSelection(i);

                                        }
                                    } else {
                                        videoModel.setSign(0);
                                    }
                                }
                            }
                            channelChangeAdapter.notifyDataSetChanged();
                            channelChangeAdapter.notifyDataSetInvalidated();
                        }

                        int[] display = ComonUtils.getDisplay(activity);

                        int width = ComonUtils.dip2px(activity, 200);

                        pingdaoxoff = display[0] - width;
                        mPopupChannelChange = new PopupWindow(pingdaoview,
                                width, LayoutParams.MATCH_PARENT, true);
                        mPopupChannelChange.setOutsideTouchable(true);
                        mPopupChannelChange
                                .setBackgroundDrawable(new ColorDrawable(0));
                        mPopupChannelChange
                                .setAnimationStyle(R.style.popwin_anim_style_media);
                        mPopupChannelChange.showAsDropDown(
                                $.id(R.id.app_video_top_box).view, pingdaoxoff,
                                0);
                        showBottomControl(false);
                        mPopupChannelChange
                                .setOnDismissListener(new OnDismissListener() {

                                    @Override
                                    public void onDismiss() {
                                        hide(false);
                                    }
                                });
                    }
                }

            } else if (v.getId() == R.id.app_video_qingxidu) {
                //
                View view = View.inflate(activity, R.layout.popwindow_qingxidu,
                        null);

                liuchang = (TextView) view
                        .findViewById(R.id.app_video_qingxidu_liuchang);
                biaoqing = (TextView) view
                        .findViewById(R.id.app_video_qingxidu_bianqing);
                gaoqing = (TextView) view
                        .findViewById(R.id.app_video_qingxidu_gaoqing);

                biaoqing.setOnClickListener(onClickListener);
                gaoqing.setOnClickListener(onClickListener);

                liuchang.setVisibility(View.GONE);
                view.findViewById(R.id.app_video_qingxidu_liuchang_underline)
                        .setVisibility(View.GONE);
                view.findViewById(R.id.app_video_qingxidu_gaoqing_underline)
                        .setVisibility(View.GONE);
                if (currentQingXiDu == QING_XI_BIAOQING) {
                    gaoqing.setBackgroundColor(activity.getResources()
                            .getColor(R.color.transparent_half_up));
                } else if (currentQingXiDu == QING_XI_GAOQING) {
                    biaoqing.setBackgroundColor(activity.getResources()
                            .getColor(R.color.transparent_half_up));
                }

                int vieheight = $.id(R.id.app_video_qingxidu).view.getHeight();
                int vieWidth = $.id(R.id.app_video_qingxidu).view.getWidth();
                mPopupQingXiWindow = new PopupWindow(view, vieWidth + vieWidth
                        * 2 / 8, LayoutParams.WRAP_CONTENT, true);
                mPopupQingXiWindow.setOutsideTouchable(true);
                mPopupQingXiWindow.setBackgroundDrawable(new ColorDrawable(0));

                mPopupQingXiWindow.showAsDropDown(
                        $.id(R.id.app_video_qingxidu).view,
                        0 - vieWidth * 1 / 8, 0 - vieheight * 4);

            }
        }
    };

    private OnItemClickListener channelItemClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            CCTVVideo cVideo = (CCTVVideo) parent.getItemAtPosition(position);

            PlayVodEntity playVodEntity = new PlayVodEntity(entity.getType()
                    + "", cVideo.getVid(), cVideo.getVsid(), cVideo.getUrl(),
                    cVideo.getImg(), cVideo.getT(), null,
                    entity.getVideoType(), cVideo.getLen());

            doChannelItemClickHistory(playVodEntity);
            setEntity(playVodEntity);

            if (null != cVideo) {
                if (null != cVideo.getVid()
                        && cVideo.getVid().trim().length() != 0) {
                    doPause();
                    requestChangeVod(cVideo.getVid());
                    setTitle(cVideo.getT());

                    currentID = cVideo.getVid();

                    // onPause();
                    // f
                    if (mPopupChannelChange != null) {
                        mPopupChannelChange.dismiss();
                    }

                } else {
                    Toast.makeText(activity, R.string.video_data_deletion, Toast.LENGTH_SHORT)
                            .show();
                }

                for (int j = 0; j < listVideos.size(); j++) {

                    if (j == position) {
                        CCTVVideo cModel = listVideos.get(j);
                        cModel.setSign(1);
                    } else {
                        CCTVVideo cModel = listVideos.get(j);
                        cModel.setSign(0);
                    }
                }

                channelChangeAdapter.notifyDataSetChanged();

            }

        }
    };

    private boolean isShowing;
    private boolean portrait;
    private float brightness = -1;
    private int volume = -1;
    private long newPosition = -1;
    private long defaultRetryTime = 5000;
    private ShareController shareController = new ShareController();

    /**
     * try to play when error(only for live video)
     *
     * @param defaultRetryTime millisecond,0 will stop retry,default is 5000 millisecond
     */
    public void setDefaultRetryTime(long defaultRetryTime) {
        this.defaultRetryTime = defaultRetryTime;
    }

    private int currentPosition;
    private boolean fullScreenOnly;

    public void setTitle(CharSequence title) {
        $.id(R.id.app_video_title).text(title);
    }

    private void doPauseResume() {
        if (isCancelVideo) {
            showNetworkChangeDialgo();
        } else if (status == STATUS_COMPLETED) {
            $.id(R.id.app_video_replay).gone();
            videoView.seekTo(0);
            videoView.start();

            if (mVodPlay != null) {
                mVodPlay.beginPerparing();
                mIsBuffer = false;
            }

            status = STATUS_PLAYING;
            updatePausePlay(true);
        } else if (videoView.isPlaying()) {

            if (mVodPlay != null) {
                mVodPlay.onStateChanged(GSVideoState.PAUSED);
            }
            statusChange(STATUS_PAUSE);
            videoView.pause();
            updatePausePlay(false);
        } else {
            videoView.start();
            status = STATUS_PLAYING;
            updatePausePlay(true);
        }
        // updatePausePlay();
    }

    /**
     * 暂停视频播放(视频播放的时候点击收藏或分享,登录)
     */
    public void doPause() {
        if (videoView.isPlaying()) {
            statusChange(STATUS_PAUSE);
            videoView.pause();
        }
        updatePausePlay(false);
    }

    /**
     * 播放视频播放(视频播放的时候点击收藏或分享,登录)
     */
    public void doStart() {
        status = STATUS_PLAYING;
        videoView.start();
        updatePausePlay(true);
    }

    private void updatePausePlay(boolean isPlaying) {
        // if (videoView.isPlaying()) {
        if (isPlaying) {
            isCancelVideo = false;
            $.id(R.id.app_video_play).image(R.drawable.pla_pause);
            $.id(R.id.app_video_play_bottom).image(R.drawable.pla_pause);
        } else {
            $.id(R.id.app_video_play).image(R.drawable.pla_continue);
            $.id(R.id.app_video_play_bottom).image(R.drawable.pla_continue);
        }
    }

    /**
     * @param timeout
     */
    public void show(int timeout) {
        if (!isShowing) {

            // if (!isLive) {
            if (!portrait) {
                $.id(R.id.app_video_top_box).visible();
                Animation animation = AnimationUtils.loadAnimation(activity,
                        R.anim.media_slow_in_top);
                $.id(R.id.app_video_top_box).view.startAnimation(animation);
            }
            showBottomControl(true);
            // }
            if (!fullScreenOnly && portrait) {
                $.id(R.id.app_video_fullscreen).visible();
            }
            isShowing = true;
        }
        if (videoView.isPlaying()) {
            updatePausePlay(true);
        } else {
            updatePausePlay(false);
        }
        handler.sendEmptyMessage(MESSAGE_SHOW_PROGRESS);
        handler.removeMessages(MESSAGE_FADE_OUT);
        if (timeout != 0) {
            handler.sendMessageDelayed(handler.obtainMessage(MESSAGE_FADE_OUT),
                    timeout);
        }
    }

    private void showBottomControl(boolean show) {
        if (show) {
            $.id(R.id.app_video_bottom_box).visible();
            Animation animation = AnimationUtils.loadAnimation(activity,
                    R.anim.media_slow_in_bottom);
            $.id(R.id.app_video_bottom_box).view.startAnimation(animation);
        } else {
            if ($.id(R.id.app_video_bottom_box).view.getVisibility() == View.VISIBLE) {
                Animation animation = AnimationUtils.loadAnimation(activity,
                        R.anim.media_slow_out_bottom);
                animation.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        $.id(R.id.app_video_bottom_box).gone();
                    }
                });
                $.id(R.id.app_video_bottom_box).view.startAnimation(animation);
            }
        }
        // $.id(R.id.app_video_play).visibility(show ? View.VISIBLE :
        // View.GONE);
        if (portrait && show) {
            $.id(R.id.app_video_play).visibility(View.VISIBLE);
            $.id(R.id.app_video_play_bottom).visibility(View.GONE);
        } else if (!portrait && show) {
            $.id(R.id.app_video_play).visibility(View.GONE);
            $.id(R.id.app_video_play_bottom).visibility(View.VISIBLE);
        } else if (!show) {
            $.id(R.id.app_video_play).visibility(View.GONE);
            // $.id(R.id.app_video_play_bottom).visibility(View.GONE);
        }
        //
        // $.id(R.id.app_video_currentTime).visibility(
        // show ? View.VISIBLE : View.GONE);
        // $.id(R.id.app_video_endTime)
        // .visibility(show ? View.VISIBLE : View.GONE);
        // $.id(R.id.app_video_seekBar)
        // .visibility(show ? View.VISIBLE : View.GONE);
    }

    private long duration;
    private boolean instantSeeking;
    private boolean isDragging;

    /**
     * 播放进度播放监听
     */
    private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (!fromUser)
                return;
            $.id(R.id.app_video_status).gone();// 移动时隐藏掉状态image
            int newPosition = (int) ((duration * progress * 1.0) / 1000);
            String time = generateTime(newPosition);
            if (instantSeeking) {
                videoView.seekTo(newPosition);
            }
            $.id(R.id.app_video_currentTime).text(time);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isDragging = true;
            handler.removeMessages(MESSAGE_FADE_OUT);
            handler.removeMessages(MESSAGE_SHOW_PROGRESS);
            if (instantSeeking) {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (!instantSeeking) {
                int position = (int) ((duration * seekBar.getProgress() * 1.0) / 1000);
                if (position == duration) {
                    position = (int) (duration - 1);
                }
                videoView.seekTo(position);
            }
            handler.removeMessages(MESSAGE_SHOW_PROGRESS);
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            isDragging = false;
            handler.sendEmptyMessageDelayed(MESSAGE_SHOW_PROGRESS, 1000);
            handler.sendMessageDelayed(handler.obtainMessage(MESSAGE_FADE_OUT),
                    defaultTimeout);
        }
    };

    /**
     * 声音播放监听
     */
    private final SeekBar.OnSeekBarChangeListener mVolumeSeekBarListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (!fromUser)
                return;
            $.id(R.id.app_video_status).gone();// 移动时隐藏掉状态image
            audioManager
                    .setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            show(3600000);

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            show(defaultTimeout);
        }

    };

    @SuppressWarnings("HandlerLeak")
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_FADE_OUT:
                    hide(false);
                    break;
                case MESSAGE_HIDE_CENTER_BOX:
                    $.id(R.id.app_video_volume_box).gone();
                    $.id(R.id.app_video_brightness_box).gone();
                    $.id(R.id.app_video_fastForward_box).gone();
                    break;
                case MESSAGE_SEEK_NEW_POSITION:
                    if (!isLive && newPosition >= 0) {
                        videoView.seekTo((int) newPosition);
                        newPosition = -1;
                    }
                    break;
                case MESSAGE_SHOW_PROGRESS:
                    long pos = setProgress();
                    if (!isDragging && isShowing) {
                        msg = obtainMessage(MESSAGE_SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                        if (videoView.isPlaying()) {
                            updatePausePlay(true);
                        } else {
                            updatePausePlay(false);
                        }
                    }
                    break;
                case MESSAGE_RESTART_PLAY:
                    play(url);
                    break;
            }
        }
    };

    //接收弱网时 发送的消息
    private Handler mHandelr = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 111){
                if(mDialog != null && !mDialog.isShowing()&&status == STATUS_LOADING&& isBiaoqing == false &&hasGaoqing ==true){
                    try {
                        mDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    };

    /**
     * 得到屏幕高度
     *
     * @return 高度
     */
    public int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }

    private RelativeLayout playLayout;
    private Dialog mDialog;//弱网下切换 “标清” 提示

    public PlayVodController(final Activity activity,
                             RelativeLayout relativeLayout) {
        playLayout = relativeLayout;
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        this.activity = activity;
        screenWidthPixels = activity.getResources().getDisplayMetrics().widthPixels;
        $ = new Query(activity);

        //创建弱网提示dialog
        mDialog = new Dialog(activity,R.style.dialog_no_dark);
        mDialog.setContentView(R.layout.player_change_qingxidu_tips);


        //初始化空间并设置点击事件
        TextView tQXD = (TextView) mDialog.findViewById(R.id.tv_biaoqing_btn);
        TextView tCancel = (TextView) mDialog.findViewById(R.id.tv_close_btn);

        tQXD.setOnClickListener(onClickListener);//此标清切换同按钮标签切换点击一样
        tCancel.setOnClickListener(onClickListener);

        videoView = (IjkVideoView) playLayout.findViewById(R.id.video_view);
        videoView
                .setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(IMediaPlayer mp) {
                        //视频播放完成
                        if(mDialog.isShowing()){
                            mDialog.dismiss();
                        }
                        statusChange(STATUS_COMPLETED);
                        if (mVodPlay != null) {
                            mVodPlay.onStateChanged(GSVideoState.STOPPED);
                            mVodPlay.endPlay();
                        }
                    }
                });

        videoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                statusChange(STATUS_ERROR);
                if (mVodPlay != null) {
                    setMetaInfoData();
                    mVodPlay.endPerparing(false, mVodMetaInfo);
                    mVodPlay.onStateChanged(GSVideoState.ERROR_END);
                }
                return true;
            }
        });
        videoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {

                int screenOrientation = getScreenOrientation();
                int screenHeight = getScreenHeight();

                Window window = mDialog.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();

                if(screenOrientation ==1){//如果是竖屏说明非全屏显示
                    lp.y = screenHeight-videoView.getHeight()-100;//lp.y表示距离屏幕下方显示的Y轴距离
                }else{
                    lp.y = 100;//全屏下提示显示在距屏幕下方100px处
                }
                window.setGravity(Gravity.BOTTOM);

                isLive = !videoView.canPause();
            }
        });
        videoView.setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                //在缓冲时，超过5s即发送网络不好 （弱网）消息
                if(!mDialog.isShowing() && hasGaoqing && status != STATUS_PLAYING){

                    mHandelr.sendEmptyMessageDelayed(111,5000);
                }
            }
        });
        videoView.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                if (mVodPlay != null) {
                    mVodPlay.onStateChanged(GSVideoState.SEEKING);
                }
            }
        });
        videoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:

                        mVodPlay.onStateChanged(GSVideoState.BUFFERING);

                        statusChange(STATUS_LOADING);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        statusChange(STATUS_PLAYING);
                        if (mVodPlay != null) {
                            setMetaInfoData();
                            mVodPlay.endPerparing(true, mVodMetaInfo);
                            mVodPlay.onStateChanged(GSVideoState.PLAYING);
                            mIsBuffer = true;
                        }
                        break;
                    case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                        // 显示 下载速度
                        // Toaster.show("download rate:" + extra);
                        break;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        statusChange(STATUS_PLAYING);
                        break;
                }
                return false;
            }
        });

        seekBar = (SeekBar) playLayout.findViewById(R.id.app_video_seekBar);
        seekBar.setMax(1000);
        seekBar.setOnSeekBarChangeListener(mSeekListener);

        $.id(R.id.app_video_play).clicked(onClickListener);
        $.id(R.id.app_video_play_bottom).clicked(onClickListener);
        $.id(R.id.app_video_fullscreen).clicked(onClickListener);
        $.id(R.id.app_video_finish).clicked(onClickListener);
        $.id(R.id.app_video_replay_icon).clicked(onClickListener);
        $.id(R.id.app_video_collect).clicked(onClickListener);
        $.id(R.id.app_video_share).clicked(onClickListener);
        $.id(R.id.app_video_menu).clicked(onClickListener);
        $.id(R.id.app_video_qingxidu).clicked(onClickListener);

        audioManager = (AudioManager) activity
                .getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        volumeSeekBar = (SeekBar) playLayout
                .findViewById(R.id.app_video_volume_seekBar);
        volumeSeekBar.setOnSeekBarChangeListener(mVolumeSeekBarListener);
        volumeSeekBar.setMax(mMaxVolume);
        volumeSeekBar.setProgress(audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC));
        setVolumeImage(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        final GestureDetector gestureDetector = new GestureDetector(activity,
                new PlayerGestureListener());

        View liveBox = playLayout;
        liveBox.setClickable(true);
        liveBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (gestureDetector.onTouchEvent(motionEvent))
                    return true;

                // 处理手势结束
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        endGesture();
                        break;
                }

                return false;
            }
        });

        orientationEventListener = new OrientationEventListener(activity) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation >= 0 && orientation <= 30 || orientation >= 330
                        || (orientation >= 150 && orientation <= 210)) {
                    // 竖屏
                    if (portrait) {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        orientationEventListener.disable();
                    }
                } else if ((orientation >= 90 && orientation <= 120)
                        || (orientation >= 240 && orientation <= 300)) {
                    if (!portrait) {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        orientationEventListener.disable();
                    }
                }
            }
        };
        if (fullScreenOnly) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        portrait = getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        initHeight = playLayout.getLayoutParams().height;

        // 初始化收藏的view
        initCollectView();

        SharedPreferences pSp = activity.getSharedPreferences("set",
                Activity.MODE_PRIVATE);
        String playSign = pSp.getString("play", "close");

        if ("close".equals(playSign)) {
            isAutoPlayNextVideo = false;
        } else {
            isAutoPlayNextVideo = true;
        }

        $.id(R.id.app_video_top_box).gone();
        $.id(R.id.app_video_bottom_box).gone();

        hideAllExcendToolBar();




        playLayout.findViewById(R.id.app_video_bottom_box).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        if (!portrait) {
            show(defaultTimeout);
        }

    }

    /**
     * 手势结束
     */
    private void endGesture() {
        volume = -1;
        brightness = -1f;
        if (newPosition >= 0) {
            handler.removeMessages(MESSAGE_SEEK_NEW_POSITION);
            handler.sendEmptyMessage(MESSAGE_SEEK_NEW_POSITION);
        }
        handler.removeMessages(MESSAGE_HIDE_CENTER_BOX);
        handler.sendEmptyMessageDelayed(MESSAGE_HIDE_CENTER_BOX, 500);

    }

    private synchronized void statusChange(int newStatus) {
        status = newStatus;
        if (!isLive && newStatus == STATUS_COMPLETED) {
            if (!isAutoPlayNextVideo) {
                hideAllExcendToolBar();
                $.id(R.id.app_video_replay).visible();
            } else {

                if (null != listVideos) {
                    // 播放下一集
                    doPlayNextVideo();
                } else {
                    hideAllExcendToolBar();
                    $.id(R.id.app_video_replay).visible();
                }

            }
        } else if (newStatus == STATUS_ERROR) {
            hideAllExcendToolBar();
            if (isLive) {
                showStatus("播放出了点小问题,正在重试...");
                if (defaultRetryTime > 0) {
                    handler.sendEmptyMessageDelayed(MESSAGE_RESTART_PLAY,
                            defaultRetryTime);
                }
            } else {
                showStatus("不能播放此视频");
            }
        } else if (newStatus == STATUS_LOADING) {
            hideAllExcendToolBar();
            $.id(R.id.app_video_loading).visible();
        } else if (newStatus == STATUS_PLAYING) {
            if (!isHavePlayUrl) {
                // if(!isCheckNetWork&&isHavePlayUrl){
                // //在播放的时候切换为4g
                // }else {
                long lastPlayTime = getLastPlayTime();
                if (lastPlayTime > -1) {
                    showNetToast("上次播放到" + generateTime(lastPlayTime)
                            + ",即将为您继续播放");
                    if (lastPlayTime > 0) {
                        videoView.seekTo((int) lastPlayTime);
                    }
                }
                // }
                isHavePlayUrl = true;
            }
            hideAllExcendToolBar();
        }

    }

    /**
     * 自动播放下一集
     */
    public void doPlayNextVideo() {
        currentPosition = -1;
        setPlayHistoryPosition();

        if (mVodPlay != null) {
            mVodPlay.endPlay();
        }

        for (int i = 0; i < listVideos.size(); i++) {
            CCTVVideo cVideo = listVideos.get(i);
            if (null == cVideo) {
                continue;
            } else {
                if (currentID.equals(cVideo.getVid().trim())) {
                    if (i < listVideos.size() - 1) { // 如果当前视频集中的最后一个视频 播放下一集
                        CCTVVideo nextVideo = listVideos.get(i + 1);
                        if (null != nextVideo.getVid()
                                && nextVideo.getVid().trim().length() != 0) {
                            doPause();

                            PlayVodEntity playVodEntity = new PlayVodEntity(
                                    entity.getType() + "", nextVideo.getVid(),
                                    nextVideo.getVsid(), nextVideo.getUrl(),
                                    nextVideo.getImg(), nextVideo.getT(), null,
                                    entity.getVideoType(), nextVideo.getLen());
                            setEntity(playVodEntity);

                            requestChangeVod(nextVideo.getVid());
                            currentID = nextVideo.getVid();
                            setTitle(nextVideo.getT());

                            if (channelChangeAdapter != null) {
                                for (int j = 0; j < listVideos.size(); j++) {

                                    if (j == i + 1) {
                                        CCTVVideo cModel = listVideos.get(j);
                                        cModel.setSign(1);
                                    } else {
                                        CCTVVideo cModel = listVideos.get(j);
                                        cModel.setSign(0);
                                    }
                                }
                                listView.setSelection(i + 1);
                                channelChangeAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(activity, R.string.video_data_deletion,
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else if (i == listVideos.size() - 1) {// 为最后一集
                        hideAllExcendToolBar();
                        $.id(R.id.app_video_replay).visible();
                    }

                    break;
                }
            }
        }
    }

    private void hideAllExcendToolBar() {
        $.id(R.id.app_video_replay).gone();
        // $.id(R.id.app_video_top_box).gone();
        $.id(R.id.app_playvideo_bg).gone();
        $.id(R.id.app_video_loading).gone();
        //  $.id(R.id.app_video_fullscreen).gone();
        $.id(R.id.app_video_status).gone();
//        $.id(R.id.app_video_bottom_box).gone();
        $.id(R.id.app_video_play).gone();
//         showBottomControl(false);
    }

    public void onPause() {
        pauseTime = System.currentTimeMillis();
        show(0);// 把系统状态栏显示出来
        mHandelr.removeMessages(111);
        if (status == STATUS_PLAYING) {
            videoView.pause();
            if (!isLive) {

            }
        }
        if (status == STATUS_COMPLETED) {
            currentPosition = -1;
        } else {
            currentPosition = videoView.getCurrentPosition();
        }
        doUnregisterReceiver();

        if (mVodPlay != null) {
            mVodPlay.onStateChanged(GSVideoState.PAUSED);
            mVodPlay.setVisibility(false);
        }
    }

    public synchronized void doUnregisterReceiver() {
        try {
            if (internetVolumeReceiver != null) {
                if (isRegisterReceiver) {
                    activity.unregisterReceiver(internetVolumeReceiver);
                    isRegisterReceiver = false;
                }
            }
        } catch (Exception e) {
        }

    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPositionSeekTo(int position) {
        videoView.seekTo(position);
    }

    public void onResume() {
        pauseTime = 0;
        if (status == STATUS_PLAYING) {
            if (isLive) {
                videoView.seekTo(0);
            } else {
                if (currentPosition > 0) {
                    videoView.seekTo(currentPosition);
                }
            }
            videoView.start();
        }
        if (status== STATUS_COMPLETED) {
            videoView.seekTo(-1);
            $.id(R.id.app_video_replay).visible();
        }

        lastInternetType = -1;
        if (!isFirstrequestVod) {
            registerInterVolume();
        }

        if (isShowing) {
            handler.removeMessages(MESSAGE_FADE_OUT);
            handler.sendMessageDelayed(handler.obtainMessage(MESSAGE_FADE_OUT),
                    defaultTimeout);
        }

        if (mVodPlay != null) {
            mVodPlay.setVisibility(true);
        }
    }

    public synchronized void registerInterVolume() {

        try {
            if (!isRegisterReceiver) {
                internetVolumeReceiver = new IntetnetReceiver();
                IntentFilter filter = new IntentFilter();
                // 网络监听
                filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                // 音量监听
                filter.addAction("android.media.VOLUME_CHANGED_ACTION");
                activity.registerReceiver(internetVolumeReceiver, filter);
                isRegisterReceiver = true;
            }
        } catch (Exception e) {
        }

    }

    public void onConfigurationChanged(final Configuration newConfig) {
        portrait = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT;
        doOnConfigurationChanged(portrait);
    }

    private void doOnConfigurationChanged(final boolean portrait) {
        if (videoView != null && !fullScreenOnly) {
            // hideAllExcendToolBar();
            // handler.post(new Runnable() {
            // @Override
            // public void run() {
            if (!portrait) {
                show(defaultTimeout);
            }
            tryFullScreen(!portrait);
            if (portrait) {
                $.id(R.id.app_video_top_box).invisible();
                $.id(R.id.app_video_fullscreen).visible();
                $.id(R.id.app_video_play).visibility(View.VISIBLE);
                $.id(R.id.app_video_play_bottom).visibility(View.GONE);

                $.id(R.id.app_video_qingxidu).gone();
                $.id(R.id.app_video_volume_seekBar).gone();
                $.id(R.id.app_video_play_volume_icon).gone();
                // 图相对小点
                $.id(R.id.app_playvideo_bg).image(R.drawable.def_no_play);
                $.getCurrent(playLayout).height(initHeight, false);
            } else {
                $.id(R.id.app_video_top_box).visible();
                $.id(R.id.app_video_fullscreen).gone();
                $.id(R.id.app_video_play).visibility(View.GONE);
                $.id(R.id.app_video_play_bottom).visibility(View.VISIBLE);

                $.id(R.id.app_video_qingxidu).visible();
                $.id(R.id.app_video_volume_seekBar).visible();
                $.id(R.id.app_video_play_volume_icon).visible();
                // 图相对大点
                $.id(R.id.app_playvideo_bg).image(R.drawable.def_no_play_h);
                $.getCurrent(playLayout)
                        .height(activity.getResources().getDisplayMetrics().heightPixels,
                                false);
            }
            // }
            // });
            // 不随重力感应屏幕方向
            // orientationEventListener.enable();
        }
    }

    private void tryFullScreen(boolean fullScreen) {
        if (activity instanceof AppCompatActivity) {
            ActionBar supportActionBar = ((AppCompatActivity) activity)
                    .getSupportActionBar();
            if (supportActionBar != null) {
                if (fullScreen) {
                    supportActionBar.hide();
                } else {
                    supportActionBar.show();
                }
            }
        }
        setFullScreen(fullScreen);
    }

    private void setFullScreen(boolean fullScreen) {
        if (activity != null) {
            WindowManager.LayoutParams attrs = activity.getWindow()
                    .getAttributes();
            if (fullScreen) {
                attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                activity.getWindow().setAttributes(attrs);
            } else {
                attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                activity.getWindow().setAttributes(attrs);
            }
        }

    }


    /**
     * 设置上次播放的历史位置
     */
    public void setPlayHistoryPosition() {
        PlayHistoryEntity history = new PlayHistoryEntity();
        history.setPid(entity.getVid());
        history.setTitle(entity.getTitle());
        history.setVid(entity.getVideosId());
        history.setPageurl("");
        history.setVideoImg(entity.getImg());
        if (!TextUtils.isEmpty(entity.getTimeLegth())) {
            history.setTimeLenth(entity.getTimeLegth());
        } else {
            history.setTimeLenth(generateTime(videoView.getDuration()));
        }
        history.setVideoType(String.valueOf(entity.getVideoType()));
        history.setPosition(String.valueOf(currentPosition));
        history.setClienttype("2");
        history.setPlaytime(playtime);
        DBInterface.getInstance().insertOrUpdate(history);
    }

    public void doChannelItemClickHistory(PlayVodEntity playVodEntity) {
        if (status != STATUS_COMPLETED) {
            currentPosition = videoView.getCurrentPosition();
        } else {
            currentPosition = -1;
        }
        isHavePlayUrl = false;

        setPlayHistoryPosition();

    }

    public void onDestroy() {
        setPlayHistoryPosition();

        try {
            orientationEventListener.disable();
            handler.removeMessages(MESSAGE_RESTART_PLAY);
            handler.removeMessages(MESSAGE_FADE_OUT);
            handler.removeMessages(MESSAGE_SEEK_NEW_POSITION);
            videoView.stopPlayback();
        } catch (Exception e) {
        }

        if (mVodPlay != null) {
            mVodPlay.endPlay();
        }
    }

    private void showStatus(String statusText) {
        $.id(R.id.app_video_status).visible();
        $.id(R.id.app_video_status_text).text(statusText);
    }

    public synchronized void play(String url) {

        this.url = url;

        videoView.setVideoPath(url);

        if (isFirstrequestVod) {

            // 第一次注册广播 第一次广播会接受到当前的网络
            registerInterVolume();

            isFirstrequestVod = false;

            NetworkInfo info = getNetworkInfo(activity);

            if (info != null) {

                int type = info.getType();

                if (type == ConnectivityManager.TYPE_MOBILE) {

                    return;
                }
            }

        } else {

            // 多视频时，点击频道切换时
            if (isCheckNetWork) {

                // 是否需要检查网络（用户再弹出的网络提醒对话框中，点击继续观看为true）
                NetworkInfo info = getNetworkInfo(activity);

                if (info != null) {

                    int type = info.getType();

                    if (type == ConnectivityManager.TYPE_MOBILE) {
                        showNetworkChangeDialgo();
                        return;
                    }
                }
            }

        }

        videoView.start();

        if (mVodPlay != null) {
            mVodPlay.beginPerparing();
            mIsBuffer = false;
        }
    }

    /**
     * 获取上次播放的时间点
     */
    private long getLastPlayTime() {
        List<PlayHistoryEntity> findAll =
                DBInterface.getInstance().getPlayHistoryByPid(entity.getVid());
        if (findAll != null) {
            if (findAll.size() == 0) {
                setHistoryInit();
                return -1;
            } else if (findAll.size() == 1) {

                PlayHistoryEntity playHistoryEntity = findAll.get(0);
                playtime = System.currentTimeMillis();
                playHistoryEntity.setPlaytime(playtime);
                DBInterface.getInstance().insertOrUpdate(playHistoryEntity);
                return Integer.parseInt(playHistoryEntity.getPosition());
            }
        } else {
            setHistoryInit();
            return -1;
        }

        return -1;
    }

    /**
     * 初始化历史记录
     */
    private void setHistoryInit() {
        PlayHistoryEntity history = new PlayHistoryEntity();
        history.setPid(entity.getVid());
        history.setTitle(entity.getTitle());
        history.setVid(entity.getVideosId());
        history.setPageurl("");
        history.setVideoImg(entity.getImg());
        if (!TextUtils.isEmpty(entity.getTimeLegth())) {
            history.setTimeLenth(entity.getTimeLegth());
        } else {
            history.setTimeLenth(Integer.toString(videoView.getDuration()));
        }
        history.setVideoType(String.valueOf(entity.getVideoType()));
        history.setPosition(String.valueOf("0"));
        history.setClienttype("2");
        playtime = System.currentTimeMillis();
        history.setPlaytime(playtime);
        DBInterface.getInstance().insertOrUpdate(history);
    }

    private String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes,
                seconds) : String.format("00:%02d:%02d", minutes, seconds);
    }

    private int getScreenOrientation() {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180)
                && height > width
                || (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270)
                && width > height) {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (volume == -1) {
            volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (volume < 0)
                volume = 0;
        }
        hide(true);

        int index = (int) (percent * mMaxVolume) + volume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        // 变更声音
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        // 变更进度条
        int i = (int) (index * 1.0 / mMaxVolume * 100);
        String s = i + "%";
        if (i == 0) {
            s = "关闭";
        }
        // 显示
        $.id(R.id.app_video_volume_icon).image(
                i == 0 ? R.drawable.ic_volume_off_white_36dp
                        : R.drawable.ic_volume_up_white_36dp);
        $.id(R.id.app_video_brightness_box).gone();
        $.id(R.id.app_video_volume_box).visible();
        $.id(R.id.app_video_volume_box).visible();
        $.id(R.id.app_video_volume).text(s).visible();
    }

    private void onProgressSlide(float percent) {
        long position = videoView.getCurrentPosition();
        long duration = videoView.getDuration();
        long deltaMax = Math.min(100 * 1000, duration - position);
        long delta = (long) (deltaMax * percent);

        newPosition = delta + position;
        if (newPosition > duration) {
            newPosition = duration;
        } else if (newPosition <= 0) {
            newPosition = 0;
            delta = -position;
        }
        int showDelta = (int) delta / 1000;
        if (showDelta != 0) {
            $.id(R.id.app_video_fastForward_box).visible();
            String text = showDelta > 0 ? ("+" + showDelta) : "" + showDelta;
            $.id(R.id.app_video_fastForward).text(text + "s");
            $.id(R.id.app_video_fastForward_target).text(
                    generateTime(newPosition) + "/");
            $.id(R.id.app_video_fastForward_all).text(generateTime(duration));
        }
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (brightness < 0) {
            brightness = activity.getWindow().getAttributes().screenBrightness;
            if (brightness <= 0.00f) {
                brightness = 0.50f;
            } else if (brightness < 0.01f) {
                brightness = 0.01f;
            }
        }
        Log.d(this.getClass().getSimpleName(), "brightness:" + brightness
                + ",percent:" + percent);
        $.id(R.id.app_video_brightness_box).visible();
        WindowManager.LayoutParams lpa = activity.getWindow().getAttributes();
        lpa.screenBrightness = brightness + percent;
        if (lpa.screenBrightness > 1.0f) {
            lpa.screenBrightness = 1.0f;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }
        $.id(R.id.app_video_brightness).text(
                ((int) (lpa.screenBrightness * 100)) + "%");
        activity.getWindow().setAttributes(lpa);

    }

    private long setProgress() {
        if (isDragging) {
            return 0;
        }

        long position = videoView.getCurrentPosition();
        long duration = videoView.getDuration();
        if (seekBar != null) {
            if (duration > 0) {
                long pos = 1000L * position / duration;
                seekBar.setProgress((int) pos);
            }
            int percent = videoView.getBufferPercentage();
            seekBar.setSecondaryProgress(percent * 10);
        }

        this.duration = duration;
        $.id(R.id.app_video_currentTime).text(generateTime(position));
        $.id(R.id.app_video_endTime).text(generateTime(this.duration));
        return position;
    }

    private void hideTopController() {
        if ($.id(R.id.app_video_top_box).view.getVisibility() == View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(activity,
                    R.anim.media_slow_out_top);
            animation.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    $.id(R.id.app_video_top_box).gone();
                }
            });
            $.id(R.id.app_video_top_box).view.startAnimation(animation);
        }
    }

    public void hide(boolean force) {
        if (force || isShowing) {
            handler.removeMessages(MESSAGE_SHOW_PROGRESS);
            showBottomControl(false);
            hideTopController();
            // $.id(R.id.app_video_fullscreen).gone();
            isShowing = false;
            if (mPopupQingXiWindow != null && mPopupQingXiWindow.isShowing()) {
                mPopupQingXiWindow.dismiss();
                mPopupQingXiWindow = null;
            }
            if (mPopupChannelChange != null && mPopupChannelChange.isShowing()) {
                mPopupChannelChange.dismiss();
                mPopupChannelChange = null;
            }
        }
    }

    private void updateFullScreenButton() {
        if (getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            $.id(R.id.app_video_fullscreen)
                    .image(R.drawable.lpanda_live_screen);
        } else {
            $.id(R.id.app_video_fullscreen)
                    .image(R.drawable.lpanda_live_screen);
        }
    }

    public void setFullScreenOnly(boolean fullScreenOnly) {
        this.fullScreenOnly = fullScreenOnly;
        tryFullScreen(fullScreenOnly);
        if (fullScreenOnly) {
            $.id(R.id.app_video_qingxidu).visible();
            $.id(R.id.app_video_volume_seekBar).visible();
            $.id(R.id.app_video_play_volume_icon).visible();
            $.id(R.id.app_playvideo_bg).image(R.drawable.def_no_play_h);
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    // 设置缩放比例16:9
    public void setScale16_9(final View view) {
        // handler.post(new Runnable() {
        //
        // @Override
        // public void run() {
        int[] display = ComonUtils.getDisplay(activity);
        LayoutParams layoutParams = view.getLayoutParams();
        // 小窗口的比例
        float ratio = (float) 16 / 9;
        initHeight = (int) Math.ceil((float) display[0] / ratio);
        layoutParams.height = initHeight;
        playLayout.setLayoutParams(layoutParams);

        // }
        // });

    }

    /**
     * <pre>
     *     fitParent:可能会剪裁,保持原视频的大小，显示在中心,当原视频的大小超过view的大小超过部分裁剪处理
     *     fillParent:可能会剪裁,等比例放大视频，直到填满View为止,超过View的部分作裁剪处理
     *     wrapContent:将视频的内容完整居中显示，如果视频大于view,则按比例缩视频直到完全显示在view中
     *     fitXY:不剪裁,非等比例拉伸画面填满整个View
     *     16:9:不剪裁,非等比例拉伸画面到16:9,并完全显示在View中
     *     4:3:不剪裁,非等比例拉伸画面到4:3,并完全显示在View中
     * </pre>
     *
     * @param scaleType
     */
    public void setScaleType(String scaleType) {
        // if (SCALETYPE_FITPARENT.equals(scaleType)) {
        // videoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
        // }else if (SCALETYPE_FILLPARENT.equals(scaleType)) {
        // videoView.setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT);
        // }else if (SCALETYPE_WRAPCONTENT.equals(scaleType)) {
        // videoView.setAspectRatio(IRenderView.AR_ASPECT_WRAP_CONTENT);
        // }else if (SCALETYPE_FITXY.equals(scaleType)) {
        // videoView.setAspectRatio(IRenderView.AR_MATCH_PARENT);
        // }else if (SCALETYPE_16_9.equals(scaleType)) {
        // videoView.setAspectRatio(IRenderView.AR_16_9_FIT_PARENT);
        // }else if (SCALETYPE_4_3.equals(scaleType)) {
        // videoView.setAspectRatio(IRenderView.AR_4_3_FIT_PARENT);
        // }
    }

    public View getPlayLayout() {
        return playLayout;
    }

    /**
     * 是否显示左上导航图标(一般有actionbar or appToolbar时需要隐藏)
     *
     * @param show
     */
    public void setShowNavIcon(boolean show) {
        $.id(R.id.app_video_finish).visibility(show ? View.VISIBLE : View.GONE);
    }

    class Query {
        private final Activity activity;
        private View view;

        public Query(Activity activity) {
            this.activity = activity;
        }

        public Query id(int id) {
            view = playLayout.findViewById(id);
            return this;
        }

        public Query getCurrent(View view) {
            this.view = view;
            return this;
        }

        public Query image(int resId) {
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(resId);
            }
            return this;
        }

        public Query visible() {
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
            return this;
        }

        public Query gone() {
            if (view != null) {
                view.setVisibility(View.GONE);
            }
            return this;
        }

        public Query invisible() {
            if (view != null) {
                view.setVisibility(View.INVISIBLE);
            }
            return this;
        }

        public Query clicked(View.OnClickListener handler) {
            if (view != null) {
                view.setOnClickListener(handler);
            }
            return this;
        }

        public Query text(CharSequence text) {
            if (view != null && view instanceof TextView) {
                ((TextView) view).setText(text);
            }
            return this;
        }

        public Query visibility(int visible) {
            if (view != null) {
                view.setVisibility(visible);
            }
            return this;
        }

        private void size(boolean width, int n, boolean dip) {

            if (view != null) {

                ViewGroup.LayoutParams lp = view.getLayoutParams();

                if (n > 0 && dip) {
                    n = dip2pixel(activity, n);
                }

                if (width) {
                    lp.width = n;
                } else {
                    lp.height = n;
                }

                view.setLayoutParams(lp);

            }

        }

        public View getCurrentView() {
            return view;
        }

        public void height(int height, boolean dip) {
            size(false, height, dip);
        }

        public int dip2pixel(Context context, float n) {
            int value = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, n, context.getResources()
                            .getDisplayMetrics());
            return value;
        }

        public float pixel2dip(Context context, float n) {
            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            float dp = n / (metrics.densityDpi / 160f);
            return dp;

        }
    }

    public class PlayerGestureListener extends
            GestureDetector.SimpleOnGestureListener {
        private boolean firstTouch;
        private boolean volumeControl;
        private boolean toSeek;

        /**
         * 双击
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // videoView.toggleAspectRatio();
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            firstTouch = true;
            return super.onDown(e);

        }

        /**
         * 滑动
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            float deltaY = mOldY - e2.getY();
            float deltaX = mOldX - e2.getX();
            if (firstTouch) {
                toSeek = Math.abs(distanceX) >= Math.abs(distanceY);
                volumeControl = mOldX > screenWidthPixels * 0.5f;
                firstTouch = false;
            }

            if (toSeek) {
                if (status == STATUS_COMPLETED) {
                    return super.onScroll(e1, e2, distanceX, distanceY);
                }

                if (!isLive) {
                    onProgressSlide(-deltaX / videoView.getWidth());
                }
            } else {
                float percent = deltaY / videoView.getHeight();
                if (volumeControl) {
                    onVolumeSlide(percent);
                } else {
                    onBrightnessSlide(percent);
                }

            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (isShowing) {
                hide(false);
            } else {
                show(defaultTimeout);
            }
            return true;
        }
    }

    class IntetnetReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {

                // 网络状态变化
                if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                    NetworkInfo info = getNetworkInfo(context);

                    if (info != null) {

                        int type = info.getType();
                        if (type != ConnectivityManager.TYPE_WIFI) {
                            // //3G 2G 4G网络
                            if (lastInternetType != INTER_OTHER) {
                                if (!isCancelVideo) {
                                    showNetworkChangeDialgo();
                                }
                            }

                        } else {
                            // wifi网络
                            showHaveChangWifi();
                        }
                    } else {
                        // 无网络
                        showINTER_NONET();
                    }
                }
            }

            // 如果音量发生变化则更改volumeSeekbar的位置
            if (intent.getAction()
                    .equals("android.media.VOLUME_CHANGED_ACTION")) {
                int currVolume = audioManager
                        .getStreamVolume(AudioManager.STREAM_MUSIC);// 当前的媒体音量
                volumeSeekBar.setProgress(currVolume);
                setVolumeImage(currVolume);
            }
        }
    }

    public synchronized void showHaveChangWifi() {

        if (lastInternetType != -1 && lastInternetType != INTER_WIFI) {

            showNetToast("已切换至WiFi");

            isCheckNetWork = true;

            if (internetDialog != null && internetDialog.isShowing()) {
                internetDialog.dismiss();
            }

            String tDefinition = "高清";
            byte tDefinitionByte = QING_XI_GAOQING;

            if (mDefinition == 1) {

                tDefinition = "标清";
                tDefinitionByte = QING_XI_BIAOQING;
            }

            phoneNetChange(tDefinition, tDefinitionByte);
        }

        lastInternetType = INTER_WIFI;
    }

    private synchronized void phoneNetChange(String definition, byte definitionByte) {

        if (null == playUrls) {
            return;
        }

        try {
            for (int i = 0; i < playUrls.size(); i++) {
                PlayModeBean modeBean = playUrls.get(i);
                if (null != modeBean) {
                    if (definition.equals(modeBean.getTitle())) {
                        currentQingXiDu = definitionByte;
                        currentPosition = videoView.getCurrentPosition();
                        isCheckNetWork = false;
                        play(modeBean.getPlayUrl());

                        videoView.seekTo(currentPosition);

                        $.id(R.id.app_video_qingxidu).text(definition);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized void showINTER_NONET() {
        if (lastInternetType != INTER_NONET) {
            showNetToast("网络无法连接，请检查网络");
            doPause();
        }
        lastInternetType = INTER_NONET;
    }

    private void showNetToast(String text) {
        Toast toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, getTopHeight());
        toast.show();
    }

    /**
     * 获取播放器中心点坐标
     *
     * @return
     */
    public int getTopHeight() {
        if (portrait) {
            int[] location = new int[2];
            playLayout.getLocationInWindow(location);
            int x = location[0];
            int y = location[1];

            // 状态栏高度
            Rect frame = new Rect();
            activity.getWindow().getDecorView()
                    .getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            return y - statusBarHeight + playLayout.getHeight() / 2;
        } else {
            return playLayout.getHeight() / 2;
        }

    }

    /**
     * 设置声音图片
     *
     * @param currVolume
     */
    public void setVolumeImage(int currVolume) {
        if (currVolume <= 0) {
            $.id(R.id.app_video_play_volume_icon).image(R.drawable.volume_no);
        } else {
            $.id(R.id.app_video_play_volume_icon).image(
                    R.drawable.volume_continue);
        }
    }

    private NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info;
    }

    /**
     * 显示网络状态
     */
    private synchronized void showNetworkChangeDialgo() {

        if (internetDialog != null && internetDialog.isShowing()) {
            return;
        }

        if (videoView.isPlaying()) {
            statusChange(STATUS_PAUSE);
            videoView.pause();
        }

        View view = View
                .inflate(activity, R.layout.dialog_internet_tishi, null);
        TextView tishiCancel = (TextView) view
                .findViewById(R.id.play_continue_cancel);
        TextView tishiSure = (TextView) view
                .findViewById(R.id.play_continue_sure);
        tishiSure.setTextColor(0xffff0000);
        internetDialog = new Dialog(activity, R.style.dialog_no_dark);

        internetDialog.setContentView(view);
        internetDialog.setCanceledOnTouchOutside(false);

        tishiCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                internetDialog.dismiss();
                isCheckNetWork = true;
                $.id(R.id.app_video_loading).gone();
                isCancelVideo = true;
            }
        });
        tishiSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playLayout.setVisibility(View.VISIBLE);

                String tDefinition = "标清";
                byte tDefinitionByte = QING_XI_BIAOQING;

                if (mDefinition == 2) {

                    tDefinition = "高清";
                    tDefinitionByte = QING_XI_GAOQING;
                }

                phoneNetChange(tDefinition, tDefinitionByte);

                internetDialog.dismiss();
                isCancelVideo = false;
            }
        });

        view.measure(0, 0);
        int height = view.getMeasuredHeight();
        Window dialogWindow = internetDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.TOP);
        lp.y = getTopHeight() - height / 2;
        dialogWindow.setAttributes(lp);
        try {
            if (!activity.isFinishing()) {
                internetDialog.show();
            }
        } catch (Exception e) {
        }

        lastInternetType = INTER_OTHER;
    }

    // 点播数据处理
    private XjlHandler mHandler1 = new XjlHandler(new HandlerListener() {
        @Override
        public void handlerMessage(HandlerMessage msg) {

            switch (msg.what) {
                case Integer.MAX_VALUE:
                    break;
                case LIST_DATA1:
                    // VodHlsModel data = (VodHlsModel) msg.obj;
                    VodHlsModel data = (VodHlsModel) msg.obj;
                    if (null != data) {
                        if (null == data.getHls_url()
                                || data.getHls_url().trim().length() == 0) {
                            Toast.makeText(activity, R.string.video_data_deletion, Toast.LENGTH_SHORT)
                                    .show();
                        } else {

                            if (data.getIs_invalid_copyright() != null
                                    && !"".equals(data.getIs_invalid_copyright())
                                    && "0".equals(data.getIs_invalid_copyright())) {// 版权未保护

                                mplayUrl = data.getHls_url();
                                requestSecVod(data.getHls_url());

                            } else {
                                Toast.makeText(activity, R.string.no_copyright,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        return;
                    }

                    break;
                case COLLECTION:
                    CCTVData datas = (CCTVData) msg.obj;
                    if (null != datas) {
                        listVideos = datas.getVideo();
                        if (videoSign.equals("no")) {
                            // Log.e("vod", "..single..video....");
                            // 只存在视频集ID
                            for (int i = 0; i < listVideos.size(); i++) {

                                CCTVVideo videoModel = listVideos.get(i);
                                if (null == videoModel) {
                                    continue;
                                } else {
                                    videoModel.setSign(1);
                                    requestChangeVod(videoModel.getVid().trim());
                                    if (null != channelChangeAdapter) {

                                        listView.setSelection(i);
                                        channelChangeAdapter.notifyDataSetChanged();
                                        channelChangeAdapter
                                                .notifyDataSetInvalidated();
                                    }
                                }
                            }
                        } else {

                            // 单视频和视频集都存在
                            if (null != currentID && listVideos != null) {
                                for (int i = 0; i < listVideos.size(); i++) {

                                    CCTVVideo videoModel = listVideos.get(i);
                                    if (null == videoModel) {
                                        continue;
                                    } else {
                                        if (currentID.equals(videoModel.getVid()
                                                .trim())) {
                                            videoModel.setSign(1);
                                            // requestChangeVod(videoModel.getVid().trim());
                                            if (null != channelChangeAdapter) {

                                                listView.setSelection(i);

                                                channelChangeAdapter
                                                        .notifyDataSetChanged();
                                                channelChangeAdapter
                                                        .notifyDataSetInvalidated();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
            }
        }
    });

    private Handler handlerXX = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case GAOQING3:
                    hasGaoqing = true;
                    final InputStream is = (InputStream) msg.obj;
                    new Thread() {
                        public void run() {
                            ArrayList<PlayModeBean> playList = VodUtil.setBitRate(
                                    mplayUrl, is);

                            Message playMsg = handlerXX.obtainMessage();
                            playMsg.what = PLAYSIGN;
                            playMsg.obj = playList;
                            handlerXX.sendMessage(playMsg);
                        }

                    }.start();
                    break;
                case PLAYSIGN:
                    ArrayList<PlayModeBean> playList = (ArrayList<PlayModeBean>) msg.obj;

                    if (null == playList) {
                        // 如果没有拿到不同码率的地址，尝试播放一级视频地址
                        if (mplayUrl != null) {
                            play(mplayUrl);
                            setTitle(entity.getTitle());

                        }
                    } else {
                        // 可以通知播放....
                        playUrls = playList;

                        if (null != playList) {
                            Log.e("eye", "playList=" + playList.toString());
                        }

                        for (int i = 0; i < playList.size(); i++) {
                            PlayModeBean modeBean = playList.get(i);
                            if (null != modeBean) {

                                if ("高清".equals(modeBean.getTitle())) {

                                    // 默认播放标清
                                    play(modeBean.getPlayUrl());
                                }
                            }
                        }
                    }
                    break;
            }
        }
    };

    // 获取点播分辨率地址，标清，高清等
    private void requestSecVod(final String path) {
        new Thread() {
            public void run() {
                try {
                    URL myURL = new URL(path);
                    System.out.println(path);
                    URLConnection ucon = myURL.openConnection();
                    InputStream is = ucon.getInputStream();
                    Message msg = new Message();
                    msg.what = GAOQING3;
                    msg.obj = is;
                    handlerXX.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    // 单视频播放
    public void requestChangeVod(String videoId) {

        if (isConnected()) {

            $.id(R.id.app_video_loading).visible();
            $.id(R.id.app_playvideo_bg).visible();

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("pid", videoId);
            // map.put("from", "000news");
            String url = mHandler1.appendParameter(playUrl, map);
            mHandler1.getHttpJson(url, VodHlsModel.class, LIST_DATA1);

        } else {
            Toast.makeText(activity, R.string.network_invalid, Toast.LENGTH_SHORT).show();
        }
    }

    // 熊猫直播，情况特殊，专门设置一个接数据方法
    public void requestVodBy(PlayVodEntity vodEntity, List<CCTVVideo> videos) {

        if (null != vodEntity) {

            shareController.setVodController(this);

            if (null != vodEntity.getTitle()) {
                // 设置title
                setTitle(vodEntity.getTitle());
            }

            setEntity(vodEntity);

            if (null != vodEntity.getVid()) {

                currentID = vodEntity.getVid();

                if (null == videos || videos.size() == 0) {
                    videoType = "yes";
                    $.id(R.id.app_video_menu).getCurrentView()
                            .setEnabled(false);
                    $.id(R.id.app_video_menu).image(
                            R.drawable.play_fullscree_menu_pressed);
                } else {
                    listVideos = videos;
                    videoType = "no";
                    if (isConnected()) {
                        playLayout.setVisibility(View.VISIBLE);
                        $.id(R.id.app_video_loading).visible();
                        $.id(R.id.app_playvideo_bg).visible();

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("pid", vodEntity.getVid());
                        // map.put("from", "000news");
                        String url = mHandler1.appendParameter(playUrl, map);
                        mHandler1.getHttpJson(url, VodHlsModel.class,
                                LIST_DATA1);
                    } else {
                        Toast.makeText(activity, R.string.network_invalid,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

        } else {
            Toast.makeText(activity, R.string.video_data_deletion, Toast.LENGTH_SHORT).show();
        }
    }

    // 获取点播地址,listVideos为null表示为单视频播放
    public void requestVod(PlayVodEntity vodEntity) {
        if (null != vodEntity) {

            shareController.setVodController(this);

            if (null != vodEntity.getTitle()) {
                // 设置title
                setTitle(vodEntity.getTitle());
            }

            setEntity(vodEntity);

            if (null == vodEntity.getVideosId() && null != vodEntity.getVid()) {
                videoType = "yes";

                $.id(R.id.app_video_menu).getCurrentView().setVisibility(View.GONE);

                if (isConnected()) {
                    playLayout.setVisibility(View.VISIBLE);
                    $.id(R.id.app_video_loading).visible();
                    $.id(R.id.app_playvideo_bg).visible();

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("pid", vodEntity.getVid());

                    String url = mHandler1.appendParameter(playUrl, map);
                    mHandler1.getHttpJson(url, VodHlsModel.class, LIST_DATA1);
                } else {
                    Toast.makeText(activity, R.string.network_invalid, Toast.LENGTH_SHORT)
                            .show();
                }

            } else if (null != vodEntity.getVideosId()
                    && null == vodEntity.getVid()) {

                // 说明，视频集直接播放,有右边菜单
                videoType = "no";
                videoSign = "no";

                if (isConnected()) {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("vsid", vodEntity.getVideosId());
                    params.put("serviceId", "panda");
                    params.put("n", "150");
                    params.put("p", "1");

                    mHandler1.getHttpJson(mHandler1.appendParameter(
                            WebAddressEnum.CCTV_DETAIL.toString(), params),
                            CCTVData.class, COLLECTION);

                } else {
                    Toast.makeText(activity, R.string.network_invalid, Toast.LENGTH_SHORT)
                            .show();
                }

            } else if (null != vodEntity.getVideosId()
                    && null != vodEntity.getVid()) {
                // 同时存在，单视频，视频集
                videoType = "no";
                videoSign = "yes";

                if (isConnected()) {

                    currentID = vodEntity.getVid();

                    $.id(R.id.app_video_loading).visible();
                    $.id(R.id.app_playvideo_bg).visible();
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("pid", vodEntity.getVid());
                    // map.put("from", "000news");
                    String url = mHandler1.appendParameter(playUrl, map);
                    mHandler1.getHttpJson(url, VodHlsModel.class, LIST_DATA1);

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("vsid", vodEntity.getVideosId());
                    params.put("serviceId", "panda");
                    params.put("n", "150");
                    params.put("p", "1");

                    mHandler1.getHttpJson(mHandler1.appendParameter(
                            WebAddressEnum.CCTV_DETAIL.toString(), params),
                            CCTVData.class, COLLECTION);

                } else {
                    Toast.makeText(activity, R.string.network_invalid, Toast.LENGTH_SHORT)
                            .show();
                }

            } else {
                Toast.makeText(activity, R.string.video_data_deletion, Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * 判断手机是否有网络
     *
     * @return true 有网络
     */
    public boolean isConnected() {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) activity
                    .getSystemService(activity.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();

                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    private void initCollectView() {
        View v = $.id(R.id.app_video_collect).view;
        if (v instanceof ImageView) {
            collectIv = (ImageView) v;
        }
    }

    // 验证本地是否收藏
    public void validLocalIsCollect() {


        if (entity == null) {
            return;
        }
        String favId = entity.getVideoType() == 2 ? entity.getVid() : entity.getVideosId();
        FavoriteEntity favorite = DBInterface.getInstance().getFavorite(favId);
        if (favorite != null) {
            collectIv.setImageDrawable(activity.getResources().getDrawable(R.drawable.play_fullsrcee_collect_true));
            collectLocalFlag = 1;
        } else {
            collectIv.setImageDrawable(activity.getResources().getDrawable(R.drawable.play_fullsrcee_collect));
            collectLocalFlag = 0;
        }

    }

    /**
     * 本地添加收藏
     */
    private void localAddCollect() {
        if (entity == null) {
            return;
        }
        // 2 单视频，3视频集，4，按单视频处理
        String id = entity.getVideoType() == 2 || entity.getVideoType() == 4 ? entity
                .getVid() : entity.getVideosId();
        FavoriteEntity entity = new FavoriteEntity();
        entity.setCollect_id(id);
        entity.setObject_id(id);
        entity.setObject_title(this.entity.getTitle());
        entity.setObject_logo(this.entity.getImg());
        entity.setObject_url(this.entity.getUrl());
        entity.setVideo_pid(this.entity.getVid());
        entity.setCollect_date(System.currentTimeMillis());
        entity.setCollect_type(!StringUtil.isNullOrEmpty(this.entity.getType()) ? Integer
                .parseInt(this.entity.getType()) : -1);// 收藏类型
        entity.setIsSingleVideo(this.entity.getVideoType() == 2);// 设置是否为单视频
        if (!TextUtils.isEmpty(this.entity.getTimeLegth())) {
            entity.setVideoLength(this.entity.getTimeLegth());
        } else {
            entity.setVideoLength(generateTime(videoView.getDuration()));
        }

        DBInterface.getInstance().insertOrUpdateFavorite(entity);

        PopWindowUtils.getInstance().showPopWindowCenter(activity,
                collectIv, R.layout.ppw_define_cue_center,
                activity.getResources().getString(R.string.collect_yes),
                true, 2000);
        // 设置收藏图片
        collectIv.setImageDrawable(activity.getResources().getDrawable(
                R.drawable.play_fullsrcee_collect_true));

        collectLocalFlag = 1;

    }

    /**
     * 本地取消收藏
     */
    private void localCancelCollect() {
        if (entity == null) {
            return;
        }
        String id = entity.getVideoType() == 2 ? entity.getVid() : entity
                .getVideosId();
        DBInterface.getInstance().deleteFavorite(id);

        PopWindowUtils.getInstance().showPopWindowCenter(activity,
                collectIv, R.layout.ppw_define_cue_center,
                activity.getResources().getString(R.string.collect_no),
                true, 2000);
        // 设置收藏图片
        collectIv.setImageDrawable(activity.getResources().getDrawable(
                R.drawable.play_fullsrcee_collect));

        collectLocalFlag = 0;
    }

    public void setEntity(PlayVodEntity vodEntity) {
        this.entity = vodEntity;
        validLocalIsCollect();
    }

    public void setTracker(VideoTracker tracker) {

        this.mTracker = tracker;
    }

    public void loadTracker(PlayVodEntity vodEntity, String version) {

        VideoInfo tVideoInfo = new VideoInfo(vodEntity.getVid());
        tVideoInfo.VideoName = vodEntity.getTitle();
        tVideoInfo.VideoUrl = vodEntity.getUrl();
        tVideoInfo.extendProperty1 = "熊猫频道_Android";
        tVideoInfo.extendProperty2 = "熊猫频道_Android" + version;

        if (NetUtil.isWifi(activity)) {

            tVideoInfo.extendProperty3 = "wifi";
        } else {

            tVideoInfo.extendProperty3 = "数据流量";
        }

        IVodInfoProvider tIVodInfoProvider = new IVodInfoProvider() {

            @Override
            public double getPosition() {

                DecimalFormat tDecimalFormat = new DecimalFormat("0.00");
                double tPosition = Double.valueOf(tDecimalFormat.format((double) videoView.getCurrentPosition() / 1000));

                return tPosition;
            }

            @Override
            public double getFramesPerSecond() {
                return 0;
            }

            @Override
            public double getBitrate() {
                return 0;
            }
        };


        mVodPlay = mTracker.newVodPlay(tVideoInfo, tIVodInfoProvider);
    }

    private void setMetaInfoData() {

        if (videoView == null || mVodMetaInfo != null) {
            return;
        }

        // 点播
        mVodMetaInfo = new VodMetaInfo();

        // 播放时长单位改为秒
        long duration = videoView.getDuration() / 1000;

        mVodMetaInfo.videoDuration = duration;
        mVodMetaInfo.setBitrateKbps(mVodMetaInfo.getBitrateKbps());
        mVodMetaInfo.setFramesPerSecond(mVodMetaInfo.getFramesPerSecond());
        mVodMetaInfo.setIsBitrateChangeable(true);

    }

}
