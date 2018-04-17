package cn.cntv.app.ipanda.ui.play;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.ui.play.entity.PlayLiveEntity;

/**
 * 视频直播界面：只有全屏 不可切换为半屏
 *
 * @author wuguicheng
 */
public class PlayLiveActivity extends BaseActivity {

    // 直播ID 服务器返回的
    public static final String PLAY_LIVE_ID = "PLAY_LIVE_ID";

    private PlayLiveController mPlayer;
    private PlayLiveEntity liveModel;
    private List<PlayLiveEntity> listModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_play_full_screen);
        Intent intent = getIntent();
        if (intent != null) {

            liveModel = (PlayLiveEntity) intent.getSerializableExtra("live");
            listModels = (List<PlayLiveEntity>) intent.getSerializableExtra("listlive");

            if (null == liveModel) {

                Toast.makeText(PlayLiveActivity.this, R.string.video_address_not_exist, Toast.LENGTH_SHORT).show();
            } else {

                init(liveModel);
            }
        }
    }


    public void init(PlayLiveEntity liveModel) {

        RelativeLayout playLayout = (RelativeLayout) findViewById(R.id.giraffe_player);
        mPlayer = new PlayLiveController(this, playLayout);

        // 设置全屏播放
        mPlayer.setFullScreenOnly(true);

        mPlayer.setTracker(addVodTracker());
        mPlayer.loadTracker(liveModel, getVersion());

        if (null == listModels) {
            mPlayer.requestLive(liveModel, null);
        } else {
            mPlayer.requestLive(liveModel, listModels);
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
            mPlayer.doStart();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mPlayer != null)
            mPlayer.doStart();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
