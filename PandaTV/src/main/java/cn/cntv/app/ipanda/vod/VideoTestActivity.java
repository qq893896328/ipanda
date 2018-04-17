package cn.cntv.app.ipanda.vod;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.ui.MainActivity;
import cn.cntv.app.ipanda.ui.pandaeye.activity.CeLueChannelInfoModel;
import cn.cntv.app.ipanda.ui.play.PlayLiveController;
import cn.cntv.app.ipanda.vod.entity.PlayModeBean;
import cn.cntv.play.core.CBoxP2P;
import cn.cntv.play.core.CBoxStaticParam;

public class VideoTestActivity extends BaseActivity{

	
	private String mVideoPath;
	private AndroidMediaController mMediaController;
	
	private TextView biaoqing,gaoqing;
	private TextView live;
	
	//直播播放控制
	PlayLiveController player;
	
	private ArrayList<PlayModeBean>  playList;
	
	private String livePath;
   
	
	
	@Override   
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_test);
		
		biaoqing = (TextView)this.findViewById(R.id.biaoqing);
		gaoqing = (TextView)this.findViewById(R.id.gaoqing);
		live = (TextView)this.findViewById(R.id.live);
		
		
		RelativeLayout playLayout = (RelativeLayout) this.findViewById(R.id.lpanda_giraffe_player);
		player = new PlayLiveController(VideoTestActivity.this,playLayout);
		player.setScale16_9(playLayout);
		//String url = "http://asp.cntv.lxdns.com/asp/hls/850/0303000a/3/default/29978c0b08964a59a927b90836dd7485/850.m3u8";
		
		playLayout.setVisibility(View.VISIBLE);
		
		if (MainActivity.mP2pInitSuccess) {
            pPlugin = MainActivity.pPlugin;
           // P2PPlay(mP2pUrl);
            
            P2PPlay("pa://cctv_p2p_hdcctv1");
            Log.e("直播", "网络切换转到p2p模式直播");
        } else {   
           // playCdnUrl();
            Log.e("直播", "网络切换转到cdn模式直播");
        }
		
		         
	}
	
	
	
	





private boolean isPriorityP2p = true;// false 优先cdn 默认情况
private boolean isSupportP2p = true;
private boolean isSupportCdn = true;
private boolean isPlayError = false;

private boolean mIsShowGQ = false;//是否支持高清
private boolean mIsLoadAdPic = false;// 是否成功加载出暂停图片广告

private CeLueChannelInfoModel bean;
private String mLiveURl, mShareUrl, mChannelTitle, mChannelId, mEPGId, mChannelType, mMultiChannelUrl;








protected CBoxP2P pPlugin;

private int tryCount = 0; // p2p缓冲404持续计数
private int tryNum = 50; // p2p缓冲404持续次数上限
public Boolean isP2pStartFail = false;
private Boolean mP2pInitSuccess = false;
private Boolean mP2pBufferSuccess = false;



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
			// p2p init server fail 直接进行cdn播放
			if (MainActivity.mIsP2pFail) {
//				pPlugin.P2PEnd(mP2pUrl, "r=" + r + "&cr=" + cr);
				isP2pStartFail = true;
				tryCount = 0;
				mP2pBufferSuccess = false;
				// Log.e(TAG + "getPlayLiveUrl",
				// "P2P_BUFFER->p2p init server 启动错误,直接切换cdn播放,调用getPlayLiveUrl");
				// getPlayLiveUrl(mVdnUrlHead + mP2pUrl
				// + Constants.CLIENT_ID, 0, true);
				playHandler.removeMessages(CBoxStaticParam.P2P_BUFFER);
				return;
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
				Log.e("直播", "P2P_BUFFER_FAIL->p2p启动错误,pPlugin == null");
				return;
			}
			/*** 2013-12-20 新增 404超时监测 ***/
			tryCount = 0;
			String errorInfo;
			try {
				errorInfo = msg.obj + "";
				Log.e("直播", "P2P_BUFFER_FAIL->p2p启动错误," + errorInfo);
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
//				getPlayLiveUrl(mVdnUrlHead + mP2pUrl + Constants.CLIENT_ID,
//						0, true);// 获取cdn播放地址
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
//			getPlayLiveUrl(mVdnUrlHead + mP2pUrl + Constants.CLIENT_ID, 0,
//					false);// 获取cdn播放地址
			mP2pBufferSuccess = true;
			String url = pPlugin.createPlayUrl();   
			/* 统计 */
			/** 2013-12-23 去除结束统计 移至beginPlay **/
			// r = cr = "500";
			// pPlugin.P2PEnd(mP2pUrl,"r="+r+"&cr="+cr);
//			videoPlay(url);
			//mVideoView.setVideoPath(url);
			player.play(url);
			break;
		case CBoxStaticParam.P2P_STATS_BEGIN:
			// pPlugin.P2PBegin(mP2pUrl);
			Log.e("直播", "P2P_STATS_BEGIN->p2p启动开始");
			
			
			String urlocal = pPlugin.createPlayUrl();
			//mVideoView.setVideoPath(urlocal);
			player.play(urlocal);
			Log.e("p2p","urlocal="+ urlocal);
			   
			
			/* 统计 */
			if (mP2pInitSuccess) {
				Log.e("直播", "anr5");
//				pPlugin.P2PBegin(mP2pUrl, statsInfo());
			}
			break;

		default:
			break;
		}
	};
};

private String mP2pUrl;//直播频道ID

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

@Override
public void onBackPressed() {
	//mBackPressed = true;

	super.onBackPressed();
}

@Override
protected void onResume() {
	super.onResume();
	// mVideoView.start();
}

@Override
protected void onStop() {
	super.onStop();

}

@Override
protected void onDestroy() {
	super.onDestroy();
//	if (mBackPressed || !mVideoView.isBackgroundPlayEnabled()) {
//		mVideoView.stopPlayback();
//		mVideoView.release(true);
//		mVideoView.stopBackgroundPlay();
//	} else {
//		mVideoView.enterBackground();
//	}
	// IjkMediaPlayer.native_profileEnd();
}












	
	
}
