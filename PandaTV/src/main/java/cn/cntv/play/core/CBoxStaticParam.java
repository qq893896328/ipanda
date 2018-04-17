package cn.cntv.play.core;

import android.content.pm.ActivityInfo;

public class CBoxStaticParam {
   
	public static final int VIDEO_MODE_NONE = 0;
	public static final int VIDEO_MODE_FIT = 1;
	public static final int VIDEO_MODE_FILL = 2;
	public static final int SPLIT_VERSTION = 13;
	public static final int VIDEOPLAYER_CLICK_PERMISSION = 7010;
	public static final int VIDEOPLAYER_CONTROLLER_DISMISS = 7012;
	public static final int VIDEOPLAYER_SOFT_DECODE_PLAY_SUCCESS = 7013;

	public static final int MSG_ID_SYS_DECODE_DELAY = 7017;
	public static final int UPDATA_DOWN_ERROR = 7018;
	public static final int ONLINE_LOADING_ERROR = 7019;
	public static final int ONLINE_LOADING_SUCEESS = 7020;
	public static final int ONLINE_LOADING_SUCEESS_SYSPLAY = 7021;
	public static final int ONLINE_LOADING_WEBITEM_SUCEESS = 7022;
	public static final int PLAYER_TIMER_TASK_UPDATE_SYS_UI = 7026;
	public static final int MESSAGE_NOTIFY = 2016;
	public static final int MESSAGE_ID_SEQ_ON_CLIKC = 2017;
	public static final int MESSAGE_ID_DEFINITION_CLICK = 2018;
	public static final int MESSAGE_ID_ALBUM_LIST_CLICK = 2019;
	public static final int MESSAGE_ID_SITE_LIST_CLICK = 2020;
	public static final int MESSAGE_DECODE_MODE_SYS_CLICK = 2021;
	public static final int MESSAGE_DECODE_MODE_SOFT_CLICK = 2022;
	public static final int VIDEO_PLAYER_PLAY_COMMPLETION = 2023;
	
	
	public static final int P2P_INIT = 100;
	public static final int P2P_INIT_SUCCESS = 101;
	public static final int P2P_INIT_FAIL = 102;
	public static final int P2P_INIT_UPDATE = 103;
	
	public static final int P2P_BUFFER = 200;
	public static final int P2P_BUFFER_SUCCESS = 201;
	public static final int P2P_BUFFER_FAIL = 202;
	
	public static final int P2P_STATS_BEGIN = 300;
	
	public static final int BUFFER_DISPLAY = 1000;
	public static final int MEDIA_PLAY_TIME = 1001;
	public static final int SYSTEM_AUTO_PLAY = 1002;
	public static final int XML_DOWNLOAD = 1003;
	public static final int XML_DOWNLOAD_SUCCESS = 1004;
	public static final int TOGGLE_STATUS_BAR = 1005;
	public static final int DALAY_HIDE_CONTROLS = 1006;
	public static final int MSG_UPDATE_EVENT = 1007;
	
	public static final int EPG_DOWNLOAD_SUCCESS = 1008;
	
	//public static final int DEFAULT_SCREEN = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
	public static final int DEFAULT_SCREEN = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	
	public static final String CHANNEL_DATA_TAG = "com.cntv.cbox.player.channelData";
	public static final int SYSTEM_SETURL = 6122;//jsx
	public static final int DALAY_EXIT_FULLSCREEN = 6160;
}
