package cn.cntv.app.ipanda.ui.play;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
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
import com.gridsum.videotracker.entity.LiveMetaInfo;
import com.gridsum.videotracker.entity.VideoInfo;
import com.gridsum.videotracker.play.LivePlay;
import com.gridsum.videotracker.provider.ILiveInfoProvider;

import java.util.HashMap;
import java.util.List;

import cn.cntv.app.ipanda.AppConfig;
import cn.cntv.app.ipanda.AppContext;
import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.Constants;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.db.DBInterface;
import cn.cntv.app.ipanda.db.entity.FavoriteEntity;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.ui.MainActivity;
import cn.cntv.app.ipanda.ui.pandaeye.activity.CeLueChannelInfoModel;
import cn.cntv.app.ipanda.ui.play.entity.PlayLiveEntity;
import cn.cntv.app.ipanda.utils.ComonUtils;
import cn.cntv.app.ipanda.utils.NetUtil;
import cn.cntv.app.ipanda.utils.PopWindowUtils;
import cn.cntv.app.ipanda.vod.IjkVideoView;
import cn.cntv.app.ipanda.vod.entity.LiveData;
import cn.cntv.play.core.CBoxP2P;
import cn.cntv.play.core.CBoxStaticParam;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by tcking on 15/10/27. 直播播放控制
 */
public class PlayLiveController {
    /**
     * 可能会剪¬裁,保持原视频的大小，显示在中心,当原视频的大小超过view的大小超过部分裁剪处理
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
    /**
     * 不剪裁,非等比例拉伸画面到4:3,并完全显示在View中
     */
    public static final String SCALETYPE_4_3 = "4:3";

    private static final int MESSAGE_FADE_OUT = 2;
    private static final int MESSAGE_SEEK_NEW_POSITION = 3;
    private static final int MESSAGE_HIDE_CENTER_BOX = 4;
    private static final int MESSAGE_RESTART_PLAY = 5;
    private final Activity activity;
    private IjkVideoView videoView;
    // private final SeekBar seekBar;
    private final AudioManager audioManager;
    private final int mMaxVolume;
    private String url;
    private Query $;
    private int STATUS_ERROR = -1;
    private int STATUS_IDLE = 0;
    private int STATUS_LOADING = 1;
    private int STATUS_PLAYING = 2;
    private int STATUS_PAUSE = 3;
    private int STATUS_COMPLETED = 4;
    private long pauseTime;
    private int status = STATUS_IDLE;
    private boolean isLive = true;// 是否为直播（待定）
    private OrientationEventListener orientationEventListener;
    private int initHeight;
    private int defaultTimeout = 5000;
    private int screenWidthPixels;

    // 清晰度view
    private TextView liuchang, biaoqing, gaoqing;

    private View liuchangLine;

    // 网络状态判断
    IntetnetReceiver internetVolumeReceiver;
    private int lastInternetType = -1;
    private static final int INTER_WIFI = 11;
    private static final int INTER_OTHER = 12;
    private static final int INTER_NONET = 13;
    Dialog internetDialog;

    // 直播网络视频网络请求
    private static final int LIST_DATA3 = 15;
    private String livePath;// 高清地址

    // p2p播放逻辑
    protected CBoxP2P pPlugin;
    private int tryCount = 0; // p2p缓冲404持续计数
    private int tryNum = 50; // p2p缓冲404持续次数上限
    public Boolean isP2pStartFail = false;
    private Boolean mP2pInitSuccess = false;
    private Boolean mP2pBufferSuccess = false;

    private String p2pLocalUrl;

    private boolean isSurpotP2p, defaultP2p;

    private static final byte QING_XI_JISHU = 1;
    private static final byte QING_XI_GAOQING = 2;
    private byte currentQingXiDu = QING_XI_GAOQING;

    View pingdaoview;

    // 清晰度
    PopupWindow mPopupQingXiWindow;
    // 频道列表切换
    PopupWindow mPopupChannelChange;
    // 频道列表偏移量
    int pingdaoxoff;

    private final SeekBar volumeSeekBar; // 声音进度条

    // private XListView mListView;//直播中国做的特殊处理
    // private LiveChinaTabItemAdapter maAdapter;//直播中国做的特殊处理

    // 已经获取到视频的真正地址 ，并播放play(String url)
    // 为true 后才可调用onDestory方法
    private boolean isPlayUrl;

    private String shareTitle = "", shareText, shareUrl,
            shareImgPath = AppConfig.DEFAULT_IMAGE_PATH + "share_logo.png";

    private PlayLiveEntity entity;
    private int collectLocalFlag = -1;// 本地收藏标识(0，执行收藏；1，执行取消收藏)
    private ImageView collectIv;

    // 判断是否监听了广播（包含网络，声音）
    private boolean isRegisterReceiver;
    // 第一次进来播放视频（处理第一次注册广播）
    private boolean isFirstrequestVod = true;

    private List<PlayLiveEntity> listEntitys;
    private ChannelChangeLiveAdapter channelChangeAdapter;
    private ListView listView;// 频道切换view
    private String currentChannel;// 当前播放频道号

    //播放的视频是否检查是否在移动网络
    private boolean isCheckNetWork = true;

    //4g下取消视频播放（解决onResume时候，重复弹出是否继续播放的框）
    private boolean isCancelVideo = false;

    //统计部分
    private VideoTracker mTracker;
    private LivePlay mLivePlay;
    private LiveMetaInfo mLiveMetaInfo;
    private boolean mIsBuffer;

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.app_video_qingxidu_liuchang) {
                if (currentQingXiDu == QING_XI_JISHU) {
                    mPopupQingXiWindow.dismiss();
                    return;
                }

                // 走p2p播放
                if (MainActivity.mP2pInitSuccess) {
                    pPlugin = MainActivity.pPlugin;
                    // P2PPlay(mP2pUrl);
                    P2PPlay(p2pLocalUrl);
                    Log.e("live", "网络切换转到p2p模式直播");
                    $.id(R.id.app_video_qingxidu).text("极速");
                    showNetToast("您已切换至极速播放");
                    currentQingXiDu = QING_XI_JISHU;
                } else {
                    // playCdnUrl();
                    Toast.makeText(activity,R.string.p2ptocdn,
                            Toast.LENGTH_SHORT).show();
                    Log.e("live", "网络切换转到cdn模式直播");
                    play(livePath);
                    $.id(R.id.app_video_qingxidu).text("高清");
                    showNetToast("您已切换至高清播放");
                    currentQingXiDu = QING_XI_GAOQING;
                }

                mPopupQingXiWindow.dismiss();

            } else if (v.getId() == R.id.app_video_qingxidu_gaoqing) {
                if (currentQingXiDu == QING_XI_GAOQING) {
                    mPopupQingXiWindow.dismiss();
                    return;
                }

                if (null != livePath) {
                    play(livePath);

                    $.id(R.id.app_video_qingxidu).text("高清");

                    currentQingXiDu = QING_XI_GAOQING;
                } else {
                    if (MainActivity.mP2pInitSuccess) {
                        pPlugin = MainActivity.pPlugin;
                        // P2PPlay(mP2pUrl);
                        P2PPlay(p2pLocalUrl);
                        Log.e("live", "网络切换转到p2p模式直播");
                        currentQingXiDu = QING_XI_JISHU;
                    } else {
                        Toast.makeText(activity, R.string.try_again_play,
                                Toast.LENGTH_SHORT).show();
                    }
                }
                mPopupQingXiWindow.dismiss();

            } else if (v.getId() == R.id.app_video_fullscreen) {
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
                doFinish();
            } else if (v.getId() == R.id.app_video_collect) {
                if (collectLocalFlag == 0) {
                    // 本地添加收藏
                    localAddCollect();
                } else if (collectLocalFlag == 1) {
                    // 本地取消收藏
                    localCancelCollect();
                }
            } else if (v.getId() == R.id.app_video_share) {

                initShareData();

                //修改分享
                shareController.showPopWindowVideo(v,
                        entity.getTitle(), entity.getChanneId(), "2", "", entity.getImage(), entity.getChanneId());

            } else if (v.getId() == R.id.app_video_menu) {
                // 显示频道菜单

//                if (mPopupChannelChange != null) {
//
//                    showBottomControl(false);
//                    mPopupChannelChange.showAsDropDown(
//                            $.id(R.id.app_video_top_box).view, pingdaoxoff, 0);
//                    showBottomControl(false);
//                } else {


                // 定位判断，当前播放那条，跳到当前播放那条的位置
                if (null != currentChannel) {
                    for (int j = 0; j < listEntitys.size(); j++) {

                        PlayLiveEntity liveMod = listEntitys.get(j);
                        if (null != liveMod) {

                            if (currentChannel.equals(liveMod.getChanneId()
                                    .trim())) {
                                liveMod.setSign(1);
                                if (null != channelChangeAdapter) {

                                    listView.setSelection(j);

                                }

                            }else{
                                liveMod.setSign(0);
                            }

                        }
                    }
                }

                channelChangeAdapter.setData(listEntitys);

                int[] display = ComonUtils.getDisplay(activity);

                int width = ComonUtils.dip2px(activity, 200);

                pingdaoxoff = display[0] - width;
                mPopupChannelChange = new PopupWindow(pingdaoview, width,
                        LayoutParams.MATCH_PARENT, true);
                mPopupChannelChange.setOutsideTouchable(true);
                mPopupChannelChange
                        .setBackgroundDrawable(new ColorDrawable(0));
                showBottomControl(false);
                mPopupChannelChange
                        .setAnimationStyle(R.style.popwin_anim_style_media);
                mPopupChannelChange.showAsDropDown(
                        $.id(R.id.app_video_top_box).view, pingdaoxoff, 0);
                showBottomControl(false);
                mPopupChannelChange
                        .setOnDismissListener(new OnDismissListener() {

                            @Override
                            public void onDismiss() {
                                hide(false);
                            }
                        });


            } else if (v.getId() == R.id.app_video_qingxidu) {
                // int width
                // =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                // int height
                // =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                View view = View.inflate(activity, R.layout.popwindow_qingxidu,
                        null);
                // view.measure(width,height);
                // height= view.getMeasuredHeight();
                // width=view.getMeasuredWidth();

                liuchang = (TextView) view
                        .findViewById(R.id.app_video_qingxidu_liuchang);
                biaoqing = (TextView) view
                        .findViewById(R.id.app_video_qingxidu_bianqing);
                gaoqing = (TextView) view
                        .findViewById(R.id.app_video_qingxidu_gaoqing);

                liuchangLine = view.findViewById(R.id.app_video_qingxidu_liuchang_underline);

                biaoqing.setVisibility(View.GONE);
                view.findViewById(R.id.app_video_qingxidu_bianqing_underline)
                        .setVisibility(View.GONE);
                view.findViewById(R.id.app_video_qingxidu_gaoqing_underline)
                        .setVisibility(View.GONE);
                liuchang.setText(activity.getString(R.string.faster));
                if (currentQingXiDu == QING_XI_JISHU) {
                    gaoqing.setBackgroundColor(activity.getResources()
                            .getColor(R.color.transparent_half_up));
                } else if (currentQingXiDu == QING_XI_GAOQING) {
                    liuchang.setBackgroundColor(activity.getResources()
                            .getColor(R.color.transparent_half_up));
                }

                liuchang.setOnClickListener(onClickListener);
                gaoqing.setOnClickListener(onClickListener);

                int vieheight = $.id(R.id.app_video_qingxidu).view.getHeight();
                int vieWidth = $.id(R.id.app_video_qingxidu).view.getWidth();
                mPopupQingXiWindow = new PopupWindow(view, vieWidth + vieWidth
                        * 2 / 8, LayoutParams.WRAP_CONTENT, true);
                mPopupQingXiWindow.setOutsideTouchable(true);
                mPopupQingXiWindow.setBackgroundDrawable(new ColorDrawable(0));

                setState();

                mPopupQingXiWindow.showAsDropDown(
                        $.id(R.id.app_video_qingxidu).view,
                        0 - vieWidth * 1 / 8, 0 - vieheight * 4);
            }
        }
    };
    private ShareController shareController = new ShareController();

    /**
     * 是否消费掉按钮点击返回事件
     *
     * @return
     */
    public boolean doFinish() {
        if (!fullScreenOnly && !portrait) {
            // 播放界面支持全屏 半屏 ，此时为全屏
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return true;
        } else if (!fullScreenOnly && portrait) {
            // 播放界面支持全屏 半屏 ，此时为半屏 不做操作
            return false;
        } else {
            // 播放界面只支持全屏
            activity.finish();
            return false;
        }
    }

    // 验证本地是否收藏
    private void validLocalIsCollect() {
        if (entity == null) {
            return;
        }

        FavoriteEntity favorite = DBInterface.getInstance().getFavorite(entity.getChanneId());
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
        FavoriteEntity favorite = new FavoriteEntity();
        favorite.setCollect_id(entity.getChanneId());
        favorite.setObject_id(entity.getChanneId());
        favorite.setObject_title(entity.getTitle());
        favorite.setObject_logo(entity.getImage());
        favorite.setObject_url(entity.getUrl());
        favorite.setVideo_pid(null);
        favorite.setCollect_date(System.currentTimeMillis());
        favorite.setCollect_type(!StringUtil.isNullOrEmpty(entity.getType()) ? Integer
                .parseInt(entity.getType()) : -1);// 收藏类型
        favorite.setIsSingleVideo(entity.isSingleVideo());// 设置是否为单视频
        favorite.setPageSource(entity.getPageSource());// 页面来源

        DBInterface.getInstance().insertOrUpdateFavorite(favorite);

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

        DBInterface.getInstance().deleteFavorite(entity.getChanneId());

        PopWindowUtils.getInstance().showPopWindowCenter(activity,
                collectIv, R.layout.ppw_define_cue_center,
                activity.getResources().getString(R.string.collect_no),
                true, 2000);
        // 设置收藏图片
        collectIv.setImageDrawable(activity.getResources().getDrawable(
                R.drawable.play_fullsrcee_collect));
        collectLocalFlag = 0;
    }

    private void initCollectView() {
        View v = $.id(R.id.app_video_collect).view;
        if (v instanceof ImageView) {
            collectIv = (ImageView) v;
        }
    }

    private boolean isShowing;
    private boolean portrait;
    private float brightness = -1;
    private int volume = -1;
    private long newPosition = -1;
    private long defaultRetryTime = 5000;

    private OnItemClickListener channelItemClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            PlayLiveEntity liveEntity = (PlayLiveEntity) parent
                    .getItemAtPosition(position);
            if (null != liveEntity) {

                requestLive(liveEntity);

                for (int i = 0; i < listEntitys.size(); i++) {
                    if (i == position) {
                        PlayLiveEntity liveModel = listEntitys.get(i);
                        liveModel.setSign(1);
                    } else {
                        PlayLiveEntity liveModel = listEntitys.get(i);
                        liveModel.setSign(0);
                    }
                }

                channelChangeAdapter.setData(listEntitys);

            }

            if (mPopupChannelChange != null) {
                mPopupChannelChange.dismiss();
            }
        }
    };

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
        } else {
            if (status == STATUS_COMPLETED) {
                $.id(R.id.app_video_replay).gone();
                videoView.seekTo(0);
                videoView.start();

                if (mLivePlay != null)
                    mLivePlay.beginPerparing();

                status = STATUS_PLAYING;
                updatePausePlay(true);
            } else if (videoView.isPlaying()) {
                statusChange(STATUS_PAUSE);
                videoView.pause();
                updatePausePlay(false);
            } else {
                videoView.start();
                status = STATUS_PLAYING;
                updatePausePlay(true);
            }
        }
//		updatePausePlay();
    }

    /**
     * 暂停视频播放(视频播放的时候点击收藏或分享,登录)
     */
    public void doPause() {
        if (videoView.isPlaying()) {
            statusChange(STATUS_PAUSE);
            videoView.pause();
            updatePausePlay(false);
        }
//		updatePausePlay();
    }

    /**
     * 播放视频播放(视频播放的时候点击收藏或分享,登录)
     */
    public void doStart() {
        videoView.start();
        status = STATUS_PLAYING;
        updatePausePlay(true);
    }

    private void updatePausePlay(boolean isPlaying) {
//		if (videoView.isPlaying()) {


        if (isPlaying) {
//			isCancelVideo = false;
            $.id(R.id.app_video_play).image(R.drawable.pla_pause);
            $.id(R.id.app_video_play_bottom).image(R.drawable.pla_pause);
            $.id(R.id.app_video_oval_point).image(R.drawable.custom_oval);
            $.id(R.id.app_video_seekBar).view.setBackgroundColor(Color.parseColor("#CB0000"));
        } else {
            $.id(R.id.app_video_play).image(R.drawable.pla_continue);
            $.id(R.id.app_video_play_bottom).image(R.drawable.pla_continue);
            $.id(R.id.app_video_oval_point).image(R.drawable.custom_oval_white);
            $.id(R.id.app_video_seekBar).view.setBackgroundColor(0xFFFFFFFF);
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

        if (portrait && show) {
            $.id(R.id.app_video_play).visibility(View.VISIBLE);
            $.id(R.id.app_video_play_bottom).visibility(View.GONE);
        } else if (!portrait && show) {
            $.id(R.id.app_video_play).visibility(View.GONE);
            $.id(R.id.app_video_play_bottom).visibility(View.VISIBLE);
        } else if (!show) {
            $.id(R.id.app_video_play).visibility(View.GONE);
            $.id(R.id.app_video_play_bottom).visibility(View.GONE);
        }

        $.id(R.id.app_video_currentTime).visibility(
                show ? View.VISIBLE : View.GONE);
        $.id(R.id.app_video_endTime)
                .visibility(show ? View.VISIBLE : View.GONE);
    }

    private long duration;
    private boolean instantSeeking;
    private boolean isDragging;
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
            show(3600000);
            if (instantSeeking) {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (!instantSeeking) {
                videoView
                        .seekTo((int) ((duration * seekBar.getProgress() * 1.0) / 1000));
            }
            show(defaultTimeout);
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            isDragging = false;
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
                    break;
                case MESSAGE_SEEK_NEW_POSITION:
                    if (!isLive && newPosition >= 0) {
                        videoView.seekTo((int) newPosition);
                        newPosition = -1;
                    }
                    break;
                case MESSAGE_RESTART_PLAY:
                    play(url);
                    break;
            }
        }
    };

    private RelativeLayout playLayout;

    @SuppressLint("NewApi")
    public PlayLiveController(final Activity activity,
                              RelativeLayout relativeLayout) {


        playLayout = relativeLayout;
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        this.activity = activity;
        screenWidthPixels = activity.getResources().getDisplayMetrics().widthPixels;
        $ = new Query(activity);
        videoView = (IjkVideoView) playLayout.findViewById(R.id.video_view);

        videoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                if (mLivePlay != null) {
                    mLivePlay.onStateChanged(GSVideoState.STOPPED);
                    mLivePlay.endPlay();
                }
            }
        });
        videoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                statusChange(STATUS_ERROR);
                if (mLivePlay != null) {
                    setMetaInfoData();
                    mLivePlay.endPerparing(false, mLiveMetaInfo);
                    mLivePlay.onStateChanged(GSVideoState.ERROR_END);
                }
                return true;
            }
        });
        videoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                isLive = !videoView.canPause();
            }
        });
        videoView.setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                if (mLivePlay != null && mIsBuffer)
                    mLivePlay.onStateChanged(GSVideoState.BUFFERING);
            }
        });
        videoView.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                if (mLivePlay != null)
                    mLivePlay.onStateChanged(GSVideoState.SEEKING);
            }
        });
        videoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        statusChange(STATUS_LOADING);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        statusChange(STATUS_PLAYING);
                        if (mLivePlay != null) {
                            setMetaInfoData();
                            mLivePlay.endPerparing(true, mLiveMetaInfo);
                            mLivePlay.onStateChanged(GSVideoState.PLAYING);
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

        $.id(R.id.app_video_play).clicked(onClickListener);
        $.id(R.id.app_video_play_bottom).clicked(onClickListener);
        $.id(R.id.app_video_fullscreen).clicked(onClickListener);
        $.id(R.id.app_video_finish).clicked(onClickListener);
        $.id(R.id.app_video_replay_icon).clicked(onClickListener);
        $.id(R.id.app_video_collect).clicked(onClickListener);
        $.id(R.id.app_video_share).clicked(onClickListener);
        $.id(R.id.app_video_menu).clicked(onClickListener);
        $.id(R.id.app_video_qingxidu).clicked(onClickListener);
        $.id(R.id.cancelTv).clicked(onClickListener);

        audioManager = (AudioManager) activity
                .getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        final GestureDetector gestureDetector = new GestureDetector(activity,
                new PlayerGestureListener());

        volumeSeekBar = (SeekBar) playLayout
                .findViewById(R.id.app_video_volume_seekBar);
        volumeSeekBar.setOnSeekBarChangeListener(mVolumeSeekBarListener);
        volumeSeekBar.setMax(mMaxVolume);
        volumeSeekBar.setProgress(audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC));
        setVolumeImage(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        View liveBox = playLayout;
        liveBox.setClickable(true);
        liveBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //直播中国处理listview拦截事件
                view.getParent().requestDisallowInterceptTouchEvent(true);

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


        hideAll();
        // 初始化收藏的view
        initCollectView();

        initChannelView();

        initChannel();

    }

    private void initChannelView() {

        pingdaoview = View.inflate(activity,
                R.layout.popupwindow_channel_change_live, null);
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
    }

    private void initChannel() {

        channelChangeAdapter = new ChannelChangeLiveAdapter(activity);
        listView.setAdapter(channelChangeAdapter);
        listView.setOnItemClickListener(channelItemClick);
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

    private void statusChange(int newStatus) {
        status = newStatus;
        if (!isLive && newStatus == STATUS_COMPLETED) {
            // hideAll();
            // $.id(R.id.app_video_replay).visible();
        } else if (newStatus == STATUS_ERROR) {
            hideAll();
            $.id(R.id.app_playvideo_bg).visible();
            $.id(R.id.app_video_loading).visible();
            if (isLive) {
                // showStatus("播放出了点小问题,正在重试...");
//				if (defaultRetryTime > 0) {
//					handler.sendEmptyMessageDelayed(MESSAGE_RESTART_PLAY,
//							defaultRetryTime);
//				}

            } else {
                // showStatus("不能播放此视频");
            }
        } else if (newStatus == STATUS_LOADING) {
            if (!portrait) {
                hideFirstAll();}else{
                hideAll();
            }
            $.id(R.id.app_video_loading).visible();
        } else if (newStatus == STATUS_PLAYING) {
            if (!portrait) {
                hideFirstAll();}else{
                hideAll();
            }

        }

    }

    /**
     * 直播中国横竖屏做的特殊处理
     */
    public void getIsStatusLoding() {
        if (status == STATUS_LOADING) {
            hideAll();
            $.id(R.id.app_video_loading).visible();
        } else if (status == STATUS_IDLE) {
            hideAll();
            $.id(R.id.app_playvideo_bg).visible();
            $.id(R.id.app_video_loading).visible();
        } else if (status == STATUS_ERROR) {
            hideAll();
            $.id(R.id.app_playvideo_bg).visible();
            $.id(R.id.app_video_loading).visible();
        } else if (status == STATUS_PLAYING) {
        } else if (status == STATUS_PAUSE) {
            doStart();
        }
    }

    private void hideAll() {
        $.id(R.id.app_video_replay).gone();
        $.id(R.id.app_video_top_box).gone();
        $.id(R.id.app_playvideo_bg).gone();
        $.id(R.id.app_video_loading).gone();
        $.id(R.id.app_video_fullscreen).gone();
        $.id(R.id.app_video_status).gone();
        $.id(R.id.app_video_bottom_box).gone();
        $.id(R.id.app_video_play).gone();
        // showBottomControl(false);

    }
    private void hideFirstAll() {
        $.id(R.id.app_video_replay).gone();
        $.id(R.id.app_playvideo_bg).gone();
        $.id(R.id.app_video_loading).gone();
        $.id(R.id.app_video_fullscreen).gone();
        $.id(R.id.app_video_status).gone();
        $.id(R.id.app_video_play).gone();
        // showBottomControl(false);

    }

    /**
     * 跟谁activity的生命周期onPause
     */
    public void onPause() {
        pauseTime = System.currentTimeMillis();
        show(0);// 把系统状态栏显示出来
        if (status == STATUS_PLAYING) {
            videoView.pause();
            updatePausePlay(false);
            if (!isLive) {
                currentPosition = videoView.getCurrentPosition();
            }
        }

        doUnregisterReceiver();

        if (mLivePlay != null)
            mLivePlay.setVisibility(false);
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

    // 暂停但不去取消网络状态的广播
    public void onPauseNotRegister() {
        pauseTime = System.currentTimeMillis();
        show(0);// 把系统状态栏显示出来
        if (status == STATUS_PLAYING) {
            videoView.pause();
            if (!isLive) {
                currentPosition = videoView.getCurrentPosition();
            }
        }
    }

    public void onResume() {
        pauseTime = 0;
        if (status == STATUS_PLAYING) {
            videoView.start();
            updatePausePlay(true);
        }

        if (!isFirstrequestVod) {
            registerInterVolume();
        }

        if (isShowing) {
            handler.removeMessages(MESSAGE_FADE_OUT);
            handler.sendMessageDelayed(handler.obtainMessage(MESSAGE_FADE_OUT),
                    defaultTimeout);
        }

        if (mLivePlay != null)
            mLivePlay.setVisibility(true);
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
        showBottomControl(true);
        show(defaultTimeout);
    }

    // public void setConnctionListView(XListView listView){
    // mListView = listView;
    // }
    //
    // public void setListViewAdapter(LiveChinaTabItemAdapter adapter){
    // maAdapter = adapter;
    // }

    private void doOnConfigurationChanged(final boolean portrait) {
        if (videoView != null && !fullScreenOnly) {
//			hideAll();
            // handler.post(new Runnable() {
            // @Override
            // public void run() {
            tryFullScreen(!portrait);
            if (portrait) {
                $.id(R.id.app_video_top_box).invisible();
                $.id(R.id.app_video_fullscreen).visible();
                $.id(R.id.app_video_play).visibility(View.VISIBLE);
                $.id(R.id.app_video_play_bottom).visibility(View.GONE);

                $.id(R.id.app_video_qingxidu).gone();
                $.id(R.id.app_video_seekBar).visible();
                $.id(R.id.app_video_oval_point).visible();
                $.id(R.id.app_video_oval_txt).visible();
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
                $.id(R.id.app_video_seekBar).invisible();
                $.id(R.id.app_video_volume_seekBar).visible();
                $.id(R.id.app_video_play_volume_icon).visible();
                $.id(R.id.app_video_oval_point).invisible();
                $.id(R.id.app_video_oval_txt).invisible();
                // 图相对大点
                $.id(R.id.app_playvideo_bg).image(R.drawable.def_no_play_h);

                DisplayMetrics dm = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
                $.getCurrent(playLayout)
                        .height(dm.heightPixels,
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
     * 跟谁activity的生命周期onPause
     */
    public void onDestroy() {
        try {
            orientationEventListener.disable();
            handler.removeMessages(MESSAGE_RESTART_PLAY);
            handler.removeMessages(MESSAGE_FADE_OUT);
            handler.removeMessages(MESSAGE_SEEK_NEW_POSITION);
            videoView.stopPlayback();
        } catch (Exception e) {
        }

        if (mLivePlay != null)
            mLivePlay.endPlay();
    }

    private void showStatus(String statusText) {
        $.id(R.id.app_video_status).visible();
        $.id(R.id.app_video_status_text).text(statusText);
    }

    public synchronized void play(String url) {
        if (url == null) {
            // 为空直接返回
            return;
        }

        this.url = url;

        videoView.setVideoPath(url);

        if (isFirstrequestVod) {
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
            if (isCheckNetWork) {
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


        playLayout.setVisibility(View.VISIBLE);
        // videoView.setVideoPath(url);
        videoView.start();

        // 设置为true
        isPlayUrl = true;

        if (mLivePlay != null) {
            mLivePlay.beginPerparing();
            mIsBuffer = false;
        }
    }

    /**
     * 返回是否已经调用play(String url)
     *
     * @return
     */
    public boolean getIsPlayUrl() {
        return isPlayUrl;
    }

    private String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes,
                seconds) : String.format("%02d:%02d", minutes, seconds);
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

    public void hideTopController() {
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
            showBottomControl(false);
            hideTopController();

//			$.id(R.id.app_video_fullscreen).gone();
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
            portrait = false;
            $.id(R.id.app_video_qingxidu).visible();
            $.id(R.id.app_video_volume_seekBar).visible();
            $.id(R.id.app_video_play_volume_icon).visible();
            $.id(R.id.app_video_seekBar).invisible();
            $.id(R.id.app_video_oval_point).invisible();
            $.id(R.id.app_video_oval_txt).invisible();
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
                if (!isLive) {
                    // onProgressSlide(-deltaX / videoView.getWidth());
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
                if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                    NetworkInfo info = getNetworkInfo(context);

                    if (info != null) {

                        int type = info.getType();
                        if (type != ConnectivityManager.TYPE_WIFI) {
                            Log.i("Wuguicheng", "isCheckNetWork==" + isCheckNetWork);
                            if (isCheckNetWork) {
                                showNetworkChangeDialgo();
                            }

                            //此时用的手机网播放，切换到p2p播放

//							phoneNetChangePlay();

                        } else {
                            showHaveChangWifi();
                            //自动切到WiFi播放
//							wifiNetChangePlay();
                        }
                    } else {
                        showINTER_NONET();
                    }
                }

                // 如果音量发生变化则更改volumeSeekbar的位置
                if (intent.getAction().equals(
                        "android.media.VOLUME_CHANGED_ACTION")) {
                    int currVolume = audioManager
                            .getStreamVolume(AudioManager.STREAM_MUSIC);// 当前的媒体音量
                    volumeSeekBar.setProgress(currVolume);
                    setVolumeImage(currVolume);
                }
            }
        }
    }


//	private void wifiNetChangePlay(){
//
//
//		if (null != livePath) {
//			play(livePath);
//
//			$.id(R.id.app_video_qingxidu).text("高清");
//			currentQingXiDu = QING_XI_GAOQING;
//		} else {
//			if (MainActivity.mP2pInitSuccess) {
//				pPlugin = MainActivity.pPlugin;
//				// P2PPlay(mP2pUrl);
//				P2PPlay(p2pLocalUrl);
//				Log.e("live", "网络切换转到p2p模式直播");
//				currentQingXiDu = QING_XI_JISHU;
//			} else {
//				Toast.makeText(activity, "当前视频无法播放,请重新离开重试!",
//						Toast.LENGTH_SHORT).show();
//			}
//		}
//	}


    private synchronized void phoneNetChangePlay() {

        if (null == p2pLocalUrl || null == livePath) {
            return;
        }


        try {
            if (MainActivity.mP2pInitSuccess) {
                pPlugin = MainActivity.pPlugin;
                // P2PPlay(mP2pUrl);
                isCheckNetWork = false;
                P2PPlay(p2pLocalUrl);
                Log.e("live", "网络切换转到p2p模式直播");
                $.id(R.id.app_video_qingxidu).text("极速");
                currentQingXiDu = QING_XI_JISHU;
            } else {
                // playCdnUrl();
                Toast.makeText(activity, R.string.p2ptocdn,
                        Toast.LENGTH_SHORT).show();
                Log.e("live", "网络切换转到cdn模式直播");
                isCheckNetWork = false;
                play(livePath);
                $.id(R.id.app_video_qingxidu).text("高清");
                currentQingXiDu = QING_XI_GAOQING;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public synchronized void showHaveChangWifi() {
        if (lastInternetType != -1
                && lastInternetType != INTER_WIFI) {
            showNetToast("已切换至WiFi");
            isCheckNetWork = true;
            if (internetDialog != null
                    && internetDialog.isShowing()) {
                internetDialog.dismiss();
            }
            if (status == STATUS_PAUSE
                    || status == STATUS_COMPLETED) {
                videoView.start();
                // $.id(R.id.app_video_play).image(R.drawable.ic_
                updatePausePlay(true);
            }
        }
        lastInternetType = INTER_WIFI;

    }

    public synchronized void showINTER_NONET() {
        if (lastInternetType != INTER_NONET) {
            showNetToast("网络无法连接，请检查网络");
            doPause();
        }
        lastInternetType = INTER_NONET;
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

    private void showNetToast(String text) {
        Toast toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, getTopHeight());
        toast.show();
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
                $.id(R.id.app_video_loading).gone();
                isCheckNetWork = false;
                isCancelVideo = true;
            }
        });
        tishiSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playLayout.setVisibility(View.VISIBLE);
                doPauseResume();
                internetDialog.dismiss();
                phoneNetChangePlay();
                isCheckNetWork = false;
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

    // 直播数据处理

    private XjlHandler mHandler1 = new XjlHandler(new HandlerListener() {

        @Override
        public void handlerMessage(HandlerMessage msg) {

            switch (msg.what) {
                case Integer.MAX_VALUE:
                    // 错误处理
                    if (MainActivity.mP2pInitSuccess) {
                        pPlugin = MainActivity.pPlugin;
                        // P2PPlay(mP2pUrl);
                        P2PPlay(p2pLocalUrl);
                        Log.e("live", "网络切换转到p2p模式直播");
                    } else {
                        // playCdnUrl();
//					Toast.makeText(activity, "p2p初始化失败,请退出重试!",
//							Toast.LENGTH_SHORT).show();
                        Log.e("live", "网络切换转到cdn模式直播");
                        play(livePath);
                    }

                    break;
                case LIST_DATA3:

                    LiveData data3 = (LiveData) msg.obj;
                    if (null == data3) {
                        return;
                    }

                    if (null != data3 && null != data3.getHls_url()) {

                        livePath = data3.getHls_url().getHls2();

                        Log.e("live", "livePath=" + data3.toString());

                        if (null != livePath) {
                            play(livePath);
                        }

                    } else {

                        // 走p2p播放
                        if (MainActivity.mP2pInitSuccess) {
                            pPlugin = MainActivity.pPlugin;
                            // P2PPlay(mP2pUrl);

                            P2PPlay(p2pLocalUrl);
                            Log.e("live", "网络切换转到p2p模式直播");
                        } else {
                            // playCdnUrl();
                            Log.e("live", "网络切换转到cdn模式直播");
                        }
                    }

                    break;
            }
        }
    });

    // 点击频道切换时，进行相应的频道切换
    private void requestLive(PlayLiveEntity liveModel) {
        this.entity = liveModel;
        // 验证是否收藏
        // validLocalIsCollect();
        //显示浮层
        if (!portrait) {
            show(defaultTimeout);
        }

        if (null == liveModel.getChanneId()) {
            // activity.finish();
            return;
        }

        $.id(R.id.app_video_loading).visible();
        $.id(R.id.app_playvideo_bg).visible();

        if (null != liveModel.getTitle()) {

            setTitle(liveModel.getTitle());
            currentChannel = liveModel.getChanneId();

        }

        p2pLocalUrl = "pa://cctv_p2p_hd" + liveModel.getChanneId();

        if (isConnected()) {

            HashMap<String, String> map = new HashMap<String, String>();

            // map.put("channel","pa://cctv_p2p_hdcctv1");
            map.put("channel", p2pLocalUrl);
            map.put("client", "androidapp");


//			mHandler1.getHttpPostJson("http://vdn.live.cntv.cn/api2/live.do",
//					map, LiveData.class, LIST_DATA3);


            mHandler1.getHttpJson(
                    mHandler1.appendParameter("http://vdn.live.cntv.cn/api2/live.do", map),
                    LiveData.class, LIST_DATA3);


        } else {
            Toast.makeText(activity, R.string.network_invalid, Toast.LENGTH_SHORT).show();
        }

        checkRes(liveModel.getChanneId());
        setState();

    }

    // 直播数据获取
    public void requestLive(PlayLiveEntity liveModel, List<PlayLiveEntity> listEntitys) {


//		if(isFirstrequestVod){
//			registerInterVolume();
//		}
//		isFirstrequestVod = false;


        this.listEntitys = listEntitys;

        this.entity = liveModel;
        // 验证是否收藏
        validLocalIsCollect();
        //显示浮层
        if (!portrait) {
            show(defaultTimeout);
        } else {
            hideAll();
        }
        if (null == liveModel.getChanneId()) {
            // activity.finish();
            return;
        }

        $.id(R.id.app_video_loading).visible();
        $.id(R.id.app_playvideo_bg).visible();

        if (null == listEntitys) {
            // 菜单禁用
          /*  $.id(R.id.app_video_menu).getCurrentView().setEnabled(false);
            $.id(R.id.app_video_menu).image(
                    R.drawable.play_fullscree_menu_pressed);*/
            $.id(R.id.app_video_menu).getCurrentView().setVisibility(View.GONE);
        } else {
            // 菜单启动

            $.id(R.id.app_video_menu).getCurrentView().setEnabled(true);
            $.id(R.id.app_video_menu).image(R.drawable.play_fullscree_menu);
        }

        if (null != liveModel.getTitle()) {

            setTitle(liveModel.getTitle());
            currentChannel = liveModel.getChanneId();
        }

        p2pLocalUrl = "pa://cctv_p2p_hd" + liveModel.getChanneId();

        if (isConnected()) {

            HashMap<String, String> map = new HashMap<String, String>();

            map.put("channel", p2pLocalUrl);
            map.put("client", "androidapp");

            mHandler1.getHttpJson(
                    mHandler1.appendParameter("http://vdn.live.cntv.cn/api2/live.do", map),
                    LiveData.class, LIST_DATA3);


        } else {
            Toast.makeText(activity, R.string.network_invalid, Toast.LENGTH_SHORT).show();
        }

        checkRes(liveModel.getChanneId());
        setState();

        shareController.setLiveController(this);
    }

    // 根据频道获取策略值
    private void checkRes(String channelId) {
        List<CeLueChannelInfoModel> celueList = AppContext
                .getLiveRestrictBeans();
        if (null != celueList && celueList.size() > 0) {
            for (int i = 0; i < celueList.size(); i++) {
                CeLueChannelInfoModel celueModel = celueList.get(i);
                if (null != celueModel) {
                    if (channelId.equals(celueModel.getChannel().trim())) {

                        if (celueModel.getP2p().trim().equals("1")) {// 表示支持p2p

                            isSurpotP2p = true;

                            if (celueModel.getPriority().trim().equals("0")) {

                                defaultP2p = true;
                            } else {
                                defaultP2p = false;
                            }
                        } else {
                            isSurpotP2p = false;
                        }

                    } else {
                        continue;
                    }
                }

            }
        }
    }

    // 根据是否支持P2p，来判定是否显示极速
    private void setState() {
        if (!isSurpotP2p) {
            if (null != liuchang) {
                liuchang.setVisibility(View.INVISIBLE);
            }

            if (null != liuchangLine) {
                liuchangLine.setVisibility(View.INVISIBLE);
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

    private Handler playHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (playHandler == null) {
                return;
            }
            String r;
            String cr;
            switch (msg.what) {
                case CBoxStaticParam.P2P_BUFFER:
                    Log.e("直播", "P2P_BUFFER->p2p加载重启第" + tryCount + "次");

                    if (($.id(R.id.app_video_loading).view).getVisibility() != View.VISIBLE) {
                        $.id(R.id.app_video_loading).visible();
                    }

                    // $.id(R.id.app_playvideo_bg).visible();

                    if (tryCount >= 50) {
                        play(livePath);
                        $.id(R.id.app_video_qingxidu).text("高清");
                        currentQingXiDu = QING_XI_GAOQING;

                        showNetToast("p2p初始化失败,切换到cdn播放!");
                        isP2pStartFail = true;
                        tryCount = 0;
                        mP2pBufferSuccess = false;
                        playHandler.removeMessages(CBoxStaticParam.P2P_BUFFER);
                        return;
                    }

                    // p2p init server fail 直接进行cdn播放
                    if (MainActivity.mIsP2pFail) {
                        Log.i("Wuguicheng", "MainActivity.mIsP2pFail == true");
                        // pPlugin.P2PEnd(mP2pUrl, "r=" + r + "&cr=" + cr);
                        isP2pStartFail = true;
                        tryCount = 0;
                        mP2pBufferSuccess = false;
                        // Log.e(TAG + "getPlayLiveUrl",
                        // "P2P_BUFFER->p2p init server 启动错误,直接切换cdn播放,调用getPlayLiveUrl");
                        // getPlayLiveUrl(mVdnUrlHead + mP2pUrl
                        // + Constants.CLIENT_ID, 0, true);
                        playHandler.removeMessages(CBoxStaticParam.P2P_BUFFER);

                        return;
                    } else {
                        Log.i("Wuguicheng", "MainActivity.mIsP2pFail == false");
                    }

                    if (pPlugin == null) {
                        Log.e("jsx==P2P_BUFFER", "pPlugin == null");
                        return;
                    }
                    /*** 2013-12-20 新增 404超时监测 ***/
                    int bufferNum = 0;
                    try {
                        if (android.os.Build.VERSION.SDK_INT < 21) {
                            bufferNum = (Integer) msg.obj; // 5.0系统出现ANR...
                        } else {
                            String code = (String) msg.obj;
                            bufferNum = Integer.parseInt(code);
                        }
                        // String code = (String) msg.obj;
                        // bufferNum = Integer.parseInt(code);
                    } catch (Exception e) {
                    }
                    if (bufferNum == 404) {
                        tryCount++;
                    }
                    // 如果策略文件中配置该频道不支持cdn不进行切换
                    // if (bean != null && tryCount >= tryNum
                    // && !"0".equals(bean.getSd())) {
                    // if (MainActivity.isOpenP2PStatic) {
                    // pPlugin.P2PEnd(mP2pUrl, "r=" + r + "&cr=" + cr);
                    // }
                    // isP2pStartFail = true;
                    // tryCount = 0;
                    // mP2pBufferSuccess = false;
                    // Log.e("直播",
                    // "P2P_BUFFER->p2p启动错误,直接切换cdn播放,调用getPlayLiveUrl");
                    // Log.e(TAG + "getPlayLiveUrl",
                    // "P2P_BUFFER->p2p启动错误,直接切换cdn播放,调用getPlayLiveUrl");
                    // getPlayLiveUrl(mVdnUrlHead + mP2pUrl + Constants.CLIENT_ID,
                    // 0, true);
                    // playHandler.removeMessages(CBoxStaticParam.P2P_BUFFER);
                    // } else {
                    pPlugin.Play(mP2pUrl, playHandler);
                    Log.e("直播", "P2P_BUFFER->直接播放p2p地址");
                    // }
                    break;
                case CBoxStaticParam.P2P_BUFFER_FAIL:
                    Log.e("jsx==", "P2P_BUFFER_FAIL");
                    if (pPlugin == null) {// 20140114

                        showNetToast("p2p初始化失败,切换到cdn播放!");
                        play(livePath);
                        $.id(R.id.app_video_qingxidu).text("高清");
                        currentQingXiDu = QING_XI_GAOQING;

                        Log.e("直播", "P2P_BUFFER_FAIL->p2p启动错误,pPlugin == null");
                        return;
                    }
                    /*** 2013-12-20 新增 404超时监测 ***/
                    tryCount = 0;
                    String errorInfo;
                    try {
                        errorInfo = msg.obj + "";
                        Log.e("直播", "P2P_BUFFER_FAIL->p2p启动错误," + errorInfo);


//                        showNetToast("p2p初始化失败,切换到cdn播放!");
                        play(livePath);
                        $.id(R.id.app_video_qingxidu).text("高清");
                        currentQingXiDu = QING_XI_GAOQING;
                    } catch (Exception e) {

                        errorInfo = "3";
                    }
                    /*** 2014-1-6 增加统计错误码 ***/
                    pPlugin.P2PSetInfo("code=" + errorInfo);
                /*
                 * 2013-12-20 统计2014-1-6 修改错误时 统计结束
				 */
                    // pPlugin.P2PEnd(mP2pUrl,"r="+r+"&cr="+cr);
                    pPlugin.P2PFinish(mP2pUrl);

                    if ("501".equals(errorInfo) || "504".equals(errorInfo)) {
                        playHandler.removeMessages(CBoxStaticParam.BUFFER_DISPLAY);// 清除Buffer显示的Timer
                        // //
                        // ***2013-12-24****
                    } else {
                        Log.e("p2p", "P2P初始化失败,调用getPlayLiveUrl");
                        Log.e("p2p" + "getPlayLiveUrl", "P2P初始化失败,调用getPlayLiveUrl");
                        // getPlayLiveUrl(mVdnUrlHead + mP2pUrl +
                        // Constants.CLIENT_ID,
                        // 0, true);// 获取cdn播放地址
                    }
                    mP2pInitSuccess = false;
                    mP2pBufferSuccess = false;
                    break;
                case CBoxStaticParam.P2P_BUFFER_SUCCESS:
                    Log.e("直播", "P2P_BUFFER_SUCCESS->p2p启动成功");
                    /*** 2013-12-20 新增 404超时监测 ***/
                    tryCount = 0;
                    // pPlugin.P2PEnd(mP2pUrl);
                    Log.e("直播", "P2P初始化成功,调用getPlayLiveUrl");
                    Log.e("p2p" + "getPlayLiveUrl", "P2P初始化成功,调用getPlayLiveUrl");
                    // getPlayLiveUrl(mVdnUrlHead + mP2pUrl + Constants.CLIENT_ID,
                    // 0,
                    // false);// 获取cdn播放地址
                    mP2pBufferSuccess = true;
                    String url = pPlugin.createPlayUrl();
                /* 统计 */
                    /** 2013-12-23 去除结束统计 移至beginPlay **/
                    // r = cr = "500";
                    // pPlugin.P2PEnd(mP2pUrl,"r="+r+"&cr="+cr);
                    // videoPlay(url);
                    // mVideoView.setVideoPath(url);
                    // player.play(url);

                    play(url);

                    break;
                case CBoxStaticParam.P2P_STATS_BEGIN:
                    // pPlugin.P2PBegin(mP2pUrl);
                    Log.e("直播", "P2P_STATS_BEGIN->p2p启动开始");

                    String urlocal = pPlugin.createPlayUrl();
                    // mVideoView.setVideoPath(urlocal);
                    // player.play(urlocal);

                    play(urlocal);

                    Log.e("p2p", "urlocal=" + urlocal);

				/* 统计 */
                    if (mP2pInitSuccess) {
                        Log.e("直播", "anr5");
                        // pPlugin.P2PBegin(mP2pUrl, statsInfo());
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };

    private String mP2pUrl;// 直播频道ID

    private void P2PPlay(String id) {
        mP2pUrl = id;
        Log.e("直播", "P2PPlay->" + id);
        playHandler.removeMessages(CBoxStaticParam.P2P_STATS_BEGIN);
        playHandler.removeMessages(CBoxStaticParam.P2P_BUFFER);
        playHandler.removeMessages(CBoxStaticParam.BUFFER_DISPLAY);
        mP2pUrl = id;

        // 配置文件获取失败情况 直接启动p2p
        playHandler.sendEmptyMessage(CBoxStaticParam.BUFFER_DISPLAY);
        playHandler.sendEmptyMessage(CBoxStaticParam.P2P_BUFFER);
        playHandler.sendEmptyMessageDelayed(CBoxStaticParam.P2P_STATS_BEGIN,
                200);

    }

    /**
     * 初始化分享数据
     */
    private void initShareData() {

        shareTitle = StringUtil.isNullOrEmpty(shareTitle) ? "\n" : shareTitle;
        shareText = shareTitle + ",熊猫频道邀请您一同观看！"
                + Constants.DOWNLOAD_URL;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public boolean onKeyDownBack() {
       /* if (ShareController.getInstance().isShow()) {
            ShareController.getInstance().resumePlay();}*/
        return doFinish();
    }

    public void setTracker(VideoTracker tracker) {

        this.mTracker = tracker;
    }

    public void loadTracker(PlayLiveEntity liveEntity, String version) {

        VideoInfo tVideoInfo = new VideoInfo(liveEntity.getChanneId());
        tVideoInfo.VideoName = liveEntity.getTitle();
        tVideoInfo.VideoUrl = liveEntity.getUrl();
        tVideoInfo.VideoTVChannel = liveEntity.getTitle();

        tVideoInfo.extendProperty1 = "熊猫频道_Android";
        tVideoInfo.extendProperty2 = "熊猫频道_Android" + version;

        if (NetUtil.isWifi(activity)) {

            tVideoInfo.extendProperty3 = "wifi";
        } else {

            tVideoInfo.extendProperty3 = "数据流量";
        }

        ILiveInfoProvider tILiveInfoProvider = new ILiveInfoProvider() {
            @Override
            public double getFramesPerSecond() {
                return 0;
            }

            @Override
            public double getBitrate() {
                return 0;
            }
        };

        mLivePlay = mTracker.newLivePlay(tVideoInfo, tILiveInfoProvider);
    }

    private void setMetaInfoData() {

        if (mLiveMetaInfo != null)
            return;

        // 直播
        mLiveMetaInfo = new LiveMetaInfo();

        // 结束视频直播放返回值
        mLiveMetaInfo.setBitrateKbps(0);
        mLiveMetaInfo.setFramesPerSecond(0);
        mLiveMetaInfo.setIsBitrateChangeable(false);
    }
}
