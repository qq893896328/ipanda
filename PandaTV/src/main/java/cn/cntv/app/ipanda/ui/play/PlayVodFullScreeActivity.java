package cn.cntv.app.ipanda.ui.play;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVVideo;
import cn.cntv.app.ipanda.ui.play.entity.PlayVodEntity;

/**
 * 点播视频界面：只有全屏 不可切换为半屏
 *
 * @author wuguicheng
 */
public class PlayVodFullScreeActivity extends BaseActivity {

    // 视频ID 服务器返回的
    public static final String PLAY_VOD_ID = "PLAY_LIVE_ID";

    private PlayVodController mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vod_full_screen_play_full_screen);

        RelativeLayout playLayout = (RelativeLayout) findViewById(R.id.giraffe_player);

        // 设置全屏播放
        mPlayer = new PlayVodController(this, playLayout);

        mPlayer.setFullScreenOnly(true);

        Intent intent = getIntent();

        if (intent != null) {

            PlayVodEntity tVodEntity = (PlayVodEntity) intent.getSerializableExtra("vid");

            if (null == tVodEntity) {

                return;
            } else {

                mPlayer.setTracker(addVodTracker());
                mPlayer.loadTracker(tVodEntity, getVersion());

                if (tVodEntity.getVideoType() == 0) {
                    return;
                } else if (tVodEntity.getVideoType() == 2) {//单视频

                    mPlayer.requestVod(tVodEntity);

                } else if (tVodEntity.getVideoType() == 3) {//视频集

                    mPlayer.requestVod(tVodEntity);
                } else if (tVodEntity.getVideoType() == 4) {
                    //熊猫直播的，点播比较特殊没有视频集ID
                    List<PlayVodEntity> listData = (List<PlayVodEntity>) intent.getSerializableExtra("list");

                    if (null != listData && listData.size() != 0) {

                        List<CCTVVideo> listVideos = new ArrayList<CCTVVideo>();

                        for (int i = 0; i < listData.size(); i++) {

                            PlayVodEntity catModel = listData.get(i);
                            if (null != catModel) {

                                CCTVVideo videoModel = new CCTVVideo();
                                try {
                                    videoModel.setVid(catModel.getGuid());
                                    videoModel.setT(catModel.getTitle());
                                    videoModel.setImg(catModel.getImg());
                                    videoModel.setLen(catModel.getTimeLegth());
                                    videoModel.setUrl(catModel.getUrl());
                                } catch (Exception e) {
                                }

                                listVideos.add(videoModel);
                            } else {
                                continue;
                            }
                        }
                        tVodEntity.setVideoType(2);

                        mPlayer.requestVodBy(tVodEntity, listVideos);
                    }
                }
            }
        }

    }

    /**
     * 视频暂停
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.onPause();
        }
    }

    /**
     * 重新加载
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mPlayer != null) {
            mPlayer.onResume();
        }
    }

    /**
     * 销毁播放
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.onDestroy();
        }
    }
    /**
     * 重新计算播放器尺寸
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mPlayer != null) {
            mPlayer.onConfigurationChanged(newConfig);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

}
