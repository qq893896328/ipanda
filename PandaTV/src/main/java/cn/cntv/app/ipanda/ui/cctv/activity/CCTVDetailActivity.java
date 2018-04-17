package cn.cntv.app.ipanda.ui.cctv.activity;

import android.annotation.SuppressLint;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gridsum.mobiledissector.MobileAppTracker;
import com.gridsum.mobiledissector.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cntv.app.ipanda.AppConfig;
import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.CollectPageSourceEnum;
import cn.cntv.app.ipanda.constant.CollectTypeEnum;
import cn.cntv.app.ipanda.constant.Constants;
import cn.cntv.app.ipanda.constant.JMPTypeEnum;
import cn.cntv.app.ipanda.constant.WebAddressEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.ResponseData;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.db.DBInterface;
import cn.cntv.app.ipanda.db.entity.FavoriteEntity;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.manager.UserManager;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.ui.cctv.adapter.CCTVDetailAdapter;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVDtlPageData;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVPageData;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVTabItem;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVVideo;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVVideosFir;
import cn.cntv.app.ipanda.ui.play.PlayVodController;
import cn.cntv.app.ipanda.ui.play.ShareController;
import cn.cntv.app.ipanda.ui.play.entity.PlayVodEntity;
import cn.cntv.app.ipanda.utils.L;
import cn.cntv.app.ipanda.utils.ListUtils;
import cn.cntv.app.ipanda.utils.Logs;
import cn.cntv.app.ipanda.utils.NetUtil;
import cn.cntv.app.ipanda.utils.PopWindowUtils;
import cn.cntv.app.ipanda.utils.TimeHelper;
import cn.cntv.app.ipanda.utils.ToastUtil;
import cn.cntv.app.ipanda.vod.IjkVideoView;
import cn.cntv.app.ipanda.xlistview.XListView;
import cn.cntv.app.ipanda.xlistview.XListView.IXListViewListener;

/**
 * @author： pj
 * @Date： 2015年12月30日 下午7:31:05
 * @Description:央视名栏底层页
 */
public class CCTVDetailActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = CCTVDetailActivity.class.getSimpleName();
	private View titleView;
	private TextView titleLeft, titleCenter, titleCenterCCTV;
	private TextView specTv, brefTv;
	private ImageView imgIv, operateIv, arrowIv, collectIv;
	private static final int DETAIL_DATA = 0;
	private static final int CQ_DATA = 1;
	private static final int JQ_DATA = 2;
	private String mflag;
	private static final int LIST_DATA1 = 3;
	private static final int LIST_DATA2 = 4;
	private static final int COLLECT_DATA = 5;
	private static final int TAB_LENGTH = 2;// tab固定是两个
	private static final int VALID_DATA = 6;
	private static final int ADD_DATA = 7;
	private static final int CANCEL_DATA = 8;

	private LayoutInflater mInflater;
	private TabHost tabHost = null;
	private LocalActivityManager manager = null;

	private String[] tag_id = { "high", "look" };// tab的tag
	private String[] tab_text = { "高清完整", "精彩看点" };// 每个tab对应的文字
	private String[] tab_url = { "", "" };// 每个tab对应的请求链接

	private XListView mListView1, mListView2;// 多语种列表、央视名栏列表
	private CCTVDetailAdapter mAdapter1;// 多语种列表适配器
	private CCTVDetailAdapter mAdapter2;// 央视名栏列表适配器

	private List<CCTVVideo> mEntityList1;// 多语种列表数据集
	private List<CCTVVideo> mEntityList2;// 央视名栏列表数据集

	private int totalCount1 = 100, totalCount2 = 100;// 列表1和列表2的总条数
	private int pageSize = 6;// 列表每次加载的条数

	private boolean isInit = true, isTitleInit = true;// 标识是否为初始化
	private boolean isRefresh;// 标识是否为下拉刷新
	private boolean isLoadMore;// 标识是否为加载更多
	RelativeLayout cctvdetail_jianjie;


	// 分享窗口对象
	private PopupWindow pw;
	private ProgressDialog mDlg;
	private PopupWindow tipPw, tipNetPw;

	private String shareTitle = "", shareText, shareUrl,
			shareImgPath = AppConfig.DEFAULT_IMAGE_PATH + "share_logo.png";

	private int cqCount, jqCount;// 粗切、精切的总条数
	private Map<String, String> params = new HashMap<String, String>();// 请求参数
	private Map<String, String> params2 = new HashMap<String, String>();// 请求参数
	private String vsid, em;// 视频集ID、精粗切参数，不指定默认返回全部(1为粗切， 2为精切)
	private int curPage1 = 1, curPage2 = 1;// 当前页数

	private ScrollView scrollView;

	private int collectFlag = -1;// 收藏标识(0，收藏；1，取消收藏)
	private int collectLocalFlag = -1;// 本地收藏标识(0，收藏；1，取消收藏)

	private String collectId, userId;// 收藏的Id，当前登陆的用户Id

	private ImageView noNetView;
	private View mainView, bottomView;

	// 视频相关对象
	private PlayVodController player;
	private RelativeLayout playLayout;
	private IjkVideoView videoView;

	// 广播对象，监听网络状态变化
	private ConnectionChangeReceiver myReceiver;

	private CCTVVideo curEntity;// 当前要播放的视频对象
	private List<CCTVVideo> videoList = new ArrayList<CCTVVideo>();// 传递给播放器的视频集中视频列表数据对象

	private long hisPlayTime;// 用户开始播放视频的时间（时间戳)

	private boolean isClickList2;

	private String id, title, image, videoLength;// 视频集Id，名栏名称，最新一期节目的缩略图，视频时长

	private boolean isFullScreen;

	private DBInterface dbInterface = DBInterface.getInstance();

	private XjlHandler<CCTVDtlPageData> mHandler1 = new XjlHandler<CCTVDtlPageData>(
			new HandlerListener() {

				@Override
				public void handlerMessage(HandlerMessage msg) {
					Gson tGson = new Gson();
					CCTVDtlPageData data = null;
					if (msg.what == CQ_DATA || msg.what == JQ_DATA
							|| msg.what == LIST_DATA1 || msg.what == LIST_DATA2) {
						data = (CCTVDtlPageData) msg.obj;
						if (data != null
								&& (msg.what == CQ_DATA || msg.what == JQ_DATA)) {
							if (data.getVideo() != null) {
								videoList.addAll(data.getVideo());
							}
						}
						Map<String, Object> ivset = data.getVideoset();
						if (ivset != null) {
							String json = tGson.toJson(ivset.get("0"));
							CCTVVideosFir vsetInfo = tGson.fromJson(json,
									CCTVVideosFir.class);
							String title = vsetInfo.getName();

							titleCenterCCTV.setText(title);
							data.setVsetInfo(vsetInfo);
							int count = Integer.parseInt(ivset.get("count")
									+ "");
							data.setCount(count);
						}

					}
					switch (msg.what) {
					case Integer.MAX_VALUE:
						// 错误处理
						Logs.e(TAG, "");
						break;
					case CQ_DATA:
						// 处理详情页粗切数据
						dealCQData(data);
						break;
					case JQ_DATA:
						// 处理详情页精切数据
						dealJQData(data);
						break;
					case LIST_DATA1:
						// 处理第一个tab对应的列表数据
						if (data != null) {
							if (isLoadMore) {
								// 加载更多
								mEntityList1.addAll(data.getVideo());
								mAdapter1.notifyDataSetChanged();
								isLoadMore = false;
							} else if (isRefresh) {
								mEntityList1 = data.getVideo();
								mAdapter1 = new CCTVDetailAdapter(
										CCTVDetailActivity.this, mEntityList1,
										mInflater);
								mListView1.setAdapter(mAdapter1);
								isRefresh = false;
							}
							onLoad1();
						}
						break;
					case LIST_DATA2:
						// 处理第二个tab对应的列表数据
						// CCTVDtlPageData data3 = (CCTVDtlPageData) msg.obj;
						if (data != null) {
							if (isLoadMore) {
								// 加载更多
								mEntityList2.addAll(data.getVideo());
								mAdapter2.notifyDataSetChanged();
								isLoadMore = false;
							} else if (isRefresh) {
								mEntityList2 = data.getVideo();
								mAdapter2 = new CCTVDetailAdapter(
										CCTVDetailActivity.this, mEntityList2,
										mInflater);
								mListView2.setAdapter(mAdapter2);
								isRefresh = false;
							}
							onLoad2();
						}
						break;
					}
				}
			});
	private String type;
	private ShareController shareController = new ShareController();

	/**
	 * 处理粗切数据
	 * 
	 * @param cqData
	 */
	private void dealCQData(CCTVDtlPageData cqData) {
		if (cqData != null && cqData.getVideoset() != null) {
			// cqCount = cqData.getVideoset().getCount();
			cqCount = cqData.getCount();
			// 请求精切数据
			requestJQData(JQ_DATA);
			// 处理详情数据
			dealDtlData(cqData.getVsetInfo());
			if (cqCount > 0) {
				// 有粗切数据，设置结果集
				mEntityList1 = cqData.getVideo();
			} else {
				// 设置粗切无数据的底图
			}
		} else {
			dismissLoadDialog();
			setNetView(false);
		}
	}

	/**
	 * 处理播放器部分的详情数据
	 * 
	 * @param video
	 */
	private void dealDtlData(CCTVVideosFir video) {

		Glide.with(this)
				.load(video.getImg())
				.asBitmap()
				.placeholder(R.drawable.def_no_play)
				.error(R.drawable.def_no_play)
				.into(imgIv);
		specTv.setText(getString(R.string.live_firsttime) + video.getSbsj());
		brefTv.setText(video.getDesc());
	}

	/**
	 * 处理精切数据
	 * 
	 * @param jqData
	 */
	private void dealJQData(CCTVDtlPageData jqData) {
		if (jqData != null && jqData.getVideoset() != null) {
			// jqCount = jqData.getVideoset().getCount();
			jqCount = jqData.getCount();
			if (jqCount > 0) {
				// 有精切数据，设置结果集
				mEntityList2 = jqData.getVideo();
			} else {
				// 设置精切无数据的底图
				//隐藏上啦加载更多
				mListView1.setPullLoadEnable(false);
				mListView2.setPullLoadEnable(false);
			}
			// 根据粗切、精切的数量初始化tab
			initTab();
		}
	}

	/**
	 * 处理列表1数据
	 * 
	 *
	 */
	private void dealList1Data() {
		try {
			mAdapter1 = new CCTVDetailAdapter(this, mEntityList1, mInflater);
			if (mEntityList1 != null && mEntityList1.size() > 0) {
				mListView1.setAdapter(mAdapter1);
				new ListUtils(this)
						.setListViewHeightBasedOnChildren(mListView1);
			} else {
				// 无匹配数据时的处理
			}
			if (isRefresh) {
				// 停止下拉刷新
				mListView1.stopRefresh();
				isRefresh = false;
			}

		} catch (Exception e) {
			L.e(TAG, e.toString());
		}
		dismissLoadDialog();
		initPlayerData();
	}

	/**
	 * 处理列表2数据
	 * 
	 *
	 */
	private void dealList2Data() {
		try {
			mAdapter2 = new CCTVDetailAdapter(this, mEntityList2, mInflater);
			if (mEntityList2 != null && mEntityList2.size() > 0) {
				mListView2.setAdapter(mAdapter2);
				new ListUtils(this)
						.setListViewHeightBasedOnChildren(mListView2);
			} else {
				// 无匹配数据时的处理
			}
			if (isRefresh) {
				// 停止下拉刷新
				mListView2.stopRefresh();
				isRefresh = false;
			}
		} catch (Exception e) {
			L.e(TAG, e.toString());
		}
	}

	/**
	 * 处理服务器返回的tab数据
	 * 
	 * @param data
	 */
	private void dealTabData(CCTVPageData data) {
		if (data == null || data.getTablist() == null) {
			return;
		}
		List<CCTVTabItem> datas = data.getTablist();
		if (datas.size() == TAB_LENGTH) {
			tab_text[0] = datas.get(0).getTitle();
			tab_text[1] = datas.get(1).getTitle();
			tab_url[0] = datas.get(0).getUrl();
			tab_url[1] = datas.get(1).getUrl();
			setTabText(2);
			// 因默认是第一个列表，所以请求第一个列表数据
			requestFirListData();
		}
	}

	/**
	 * 请求第一个页签对应的列表数据
	 */
	private void requestFirListData() {
		requestCQData(LIST_DATA1);
	}

	/**
	 * 请求第二个页签对应的列表数据
	 */
	private void requestSecListData() {
		requestJQData(JQ_DATA);
	}

	/**
	 * 设置tab页签的文本
	 */
	@SuppressLint("NewApi")
	private void setTabText(int tabCount) {
		for (int i = 0; i < tabCount; i++) {
			View childView = tabHost.getTabWidget().getChildAt(i);
			TextView tvTab;
			RelativeLayout.LayoutParams params;
			if (childView != null) {
				tvTab = (TextView) childView.findViewById(R.id.tv_title);
				params = (RelativeLayout.LayoutParams) tvTab.getLayoutParams();
				if (tabCount == 1) {
					// 只有一个tab，左对齐
					childView
							.findViewById(R.id.layout_tab_bottom_line)
							.setVisibility(View.GONE);
					childView
							.findViewById(R.id.layout_tab_bottom_line_grey)
							.setVisibility(View.VISIBLE);
					params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					params.addRule(RelativeLayout.CENTER_VERTICAL);
					params.removeRule(RelativeLayout.CENTER_HORIZONTAL);
					params.leftMargin = 30;
					tvTab.setTextColor(Color.parseColor("#000000"));
				}
				tvTab.setLayoutParams(params);
				tvTab.setText(tab_text[i]);
			}

		}
	}

	private XjlHandler<ResponseData<Object>> mHandler2 = new XjlHandler<ResponseData<Object>>(
			new HandlerListener() {

				@Override
				public void handlerMessage(HandlerMessage msg) {
					ResponseData<Object> responseData = (ResponseData<Object>) msg.obj;
					boolean isSuccess = Constants.RESPONSE_STATUS_SUCCESS
							.equalsIgnoreCase(responseData.getStatus());
					boolean isCollected = false;
					if (!isSuccess) {
						// 提示服务器返回的错误信息
						ToastUtil.showLong(CCTVDetailActivity.this,
								responseData.getMsg());
					}
					switch (msg.what) {
					case Integer.MAX_VALUE:
						// 错误处理
						Logs.e(TAG, "");
						break;
					case VALID_DATA:
						// 验证是否收藏
						if (isSuccess) {
							if (Constants.RESPONSE_MSG_OK
									.equalsIgnoreCase(responseData.getMsg())) {
								// 已收藏，获取收藏的ID
								collectId = responseData.getCollect_id();
								isCollected = true;
							} else {
								// 未收藏
								collectId = null;
								isCollected = false;
							}
						}
						collectIv.setImageDrawable(getResources().getDrawable(
								isCollected ? R.drawable.collect_yes
										: R.drawable.collect_no));
						collectFlag = isCollected ? 1 : 0;
						break;
					case ADD_DATA:
						// 添加收藏
						// 给出提示
						dismissLoadDialog();
						if (isSuccess) {
							PopWindowUtils.getInstance().showPopWindowCenter(
									CCTVDetailActivity.this,
									collectIv,
									R.layout.ppw_define_cue_center,
									getResources().getString(
											R.string.collect_yes), true, 2000);
							// 设置收藏图片
							collectIv.setImageDrawable(getResources()
									.getDrawable(R.drawable.collect_yes));
							// 已收藏，获取收藏的ID
							collectId = responseData.getCollect_id();
							collectFlag = 1;
						}
						break;
					case CANCEL_DATA:
						// 取消收藏
						// 给出提示
						dismissLoadDialog();
						if (isSuccess) {
							PopWindowUtils.getInstance().showPopWindowCenter(
									CCTVDetailActivity.this,
									collectIv,
									R.layout.ppw_define_cue_center,
									getResources().getString(
											isSuccess ? R.string.collect_no
													: R.string.collect_yes),
									true, 2000);
							// 设置收藏图片
							collectIv.setImageDrawable(getResources()
									.getDrawable(R.drawable.collect_no));
							collectId = null;
							collectFlag = 0;
						}
						break;
					}
				}
			});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cctv_wellknow_item_dtl);
		// registerReceiver();
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		initView();
		initData();
		initListener();
		//addWXPlatform();
	}



	@Override
	public void onPause() {
		super.onPause();
		if (player != null) {
			player.onPause();
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (isFullScreen) {
			setFullScreenView();
		} else {
			setHalfScreenView();
		}
		validLocalIsCollect();
		if (player != null) {
			player.onResume();
			player.doStart();
		}
	}
	@Override
	protected void onRestart() {
		super.onRestart();
	}

	private void setFullScreenView() {
		titleView.setVisibility(View.GONE);
		bottomView.setVisibility(View.GONE);
		specTv.setVisibility(View.GONE);
		scrollView.setVisibility(View.GONE);
		isFullScreen = true;
	}

	private void setHalfScreenView() {
		titleView.setVisibility(View.VISIBLE);
		bottomView.setVisibility(View.VISIBLE);
		specTv.setVisibility(View.VISIBLE);
		scrollView.setVisibility(View.VISIBLE);
		isFullScreen = false;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setFullScreenView();
		} else {
			setHalfScreenView();
		}
		if (player != null) {
			player.onConfigurationChanged(newConfig);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 记录之前的播放记录
		// addPlayHistory();
		if (player != null) {
			player.onDestroy();
		}
		// unregisterReceiver();
	}

	/**
	 * 添加历史播放记录
	 */
	private void addPlayHistory() {
		if (player != null) {
			player.doPause();
			PlayVodEntity playVodEntity = new PlayVodEntity(
					CollectTypeEnum.SP.value() + "", curEntity.getVid(),
					curEntity.getVsid(), curEntity.getUrl(),
					curEntity.getImg(), curEntity.getT(), null,
					JMPTypeEnum.VIDEO_VOD.value(), curEntity.getLen());
			player.doChannelItemClickHistory(playVodEntity);
		}
	}

	private void initView() {

		mInflater = LayoutInflater.from(this);
		titleView = findViewById(R.id.layout_common_title);
		titleLeft = (TextView) this.findViewById(R.id.common_title_left);
		titleCenter = (TextView) this.findViewById(R.id.common_title_center);
		titleCenterCCTV = (TextView) this
				.findViewById(R.id.common_title_center);
		titleCenter.setVisibility(View.GONE);
		titleCenterCCTV.setVisibility(View.VISIBLE);
		specTv = (TextView) findViewById(R.id.spec);
		brefTv = (TextView) findViewById(R.id.bref);
		imgIv = (ImageView) findViewById(R.id.img);
		operateIv = (ImageView) findViewById(R.id.operate);
		cctvdetail_jianjie= (RelativeLayout) findViewById(R.id.cctvdetail_jianjie);
		arrowIv = (ImageView) findViewById(R.id.arrow);
		collectIv = (ImageView) findViewById(R.id.collect);
		titleLeft.setOnClickListener(new ViewClick());

		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup(manager);
		tabHost.setOnTabChangedListener(mTabChange);

		scrollView = (ScrollView) findViewById(R.id.scroll_wrapList);
		mListView1 = (XListView) findViewById(R.id.cctv_listview1);
		mListView2 = (XListView) findViewById(R.id.cctv_listview2);
		mListView1.setPullLoadEnable(true);
		mListView1.setPullRefreshEnable(true);
		mListView2.setPullLoadEnable(true);
		mListView2.setPullRefreshEnable(true);

		noNetView = (ImageView) findViewById(R.id.ivNoNet);
		noNetView.setOnClickListener(this);
		mainView = findViewById(R.id.layout_main);
		bottomView = findViewById(R.id.layout_bottom);
	}

	private void initData() {
		if (NetUtil.isNetConnected(this)) {
			showLoadingDialog();
			setNetView(true);
			dealPageData();
		} else {
			setNetView(false);
		}
	}

	/**
	 * 根据网络连接情况设置相应的显示的view
	 * 
	 * @param flag
	 */
	private void setNetView(boolean flag) {
		mainView.setVisibility(flag ? View.VISIBLE : View.GONE);
		bottomView.setVisibility(flag ? View.VISIBLE : View.GONE);
		noNetView.setVisibility(flag ? View.GONE : View.VISIBLE);
	}

	private void dealPageData() {
		Intent intent = getIntent();
		vsid = intent.getStringExtra("id");
		title = intent.getStringExtra("title");
		image = intent.getStringExtra("image");


		requestCQData(CQ_DATA);
	}

	private void initPlayerData() {

		playLayout = (RelativeLayout) findViewById(R.id.lpanda_giraffe_player);
		videoView = (IjkVideoView) playLayout.findViewById(R.id.video_view);

		player = new PlayVodController(this, playLayout);
		player.setScale16_9(playLayout);
		player.setTracker(addVodTracker());

		if (videoList != null && videoList.get(0) != null) {
			// 默认播放视频集的一个子视频
			curEntity = videoList.get(0);
			doPlay(0);
		}
	}

	private void wifiPlay(int flag) {
		// 记录开始播放视频的时间
		hisPlayTime = System.currentTimeMillis();
		operateIv.setVisibility(View.GONE);
		imgIv.setVisibility(View.INVISIBLE);
		playLayout.setVisibility(View.VISIBLE);
		// 默认播放视频集的一个子视频
		PlayVodEntity tEntity = new PlayVodEntity(CollectTypeEnum.SP.value()
				+ "", curEntity.getVid(), curEntity.getVsid(),
				curEntity.getUrl(), curEntity.getImg(), curEntity.getT(), null,
				JMPTypeEnum.VIDEO_VOD.value(), curEntity.getLen());
		player.loadTracker(tEntity,getVersion());
		player.requestVod(tEntity);
//		if (!isTitleInit) {
//			if (curEntity != null) {
//				titleCenterCCTV.setText(StrUtils.subStrLength16(
//						title, 13));
//			}
//		}
//		isTitleInit = false;
	}

	/**
	 * 
	 * @param flag
	 *            1-精彩看点，第二个参数传null
	 */
	private void doPlay(int flag) {
		if (videoList != null && curEntity != null) {
			// if (NetUtil.isWifiConnected(this)) {
			// WIFI环境下，默认自动播放
			wifiPlay(flag);
			// } else {
			// // 非WIFI环境下，弹出播放器自行判断的提示框
			// operateIv.setVisibility(View.VISIBLE);
			// imgIv.setVisibility(View.VISIBLE);
			// playLayout.setVisibility(View.GONE);
			// showTipPop(imgIv);
			// }
		}
	}

	/**
	 * 处理是否为wifi情况下默认图片、操作按钮、播放器可见性的变化
	 * 
	 *
	 */
	private void dealNetPlayer() {
		if (NetUtil.isWifiConnected(this)) {
			// WIFI环境下，默认自动播放
			operateIv.setVisibility(View.GONE);
			imgIv.setVisibility(View.INVISIBLE);
			playLayout.setVisibility(View.VISIBLE);
		} else {
			operateIv.setVisibility(View.VISIBLE);
			imgIv.setVisibility(View.VISIBLE);
			playLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 处理是否为wifi情况下默认图片、操作按钮、播放器可见性的变化，并在wifi下自动播放，在非wifi下显示提示框
	 * 
	 * @param tUrl
	 */
	private void dealNetPlayer1(String tUrl) {
		if (NetUtil.isWifiConnected(this)) {
			// WIFI环境下，默认自动播放
			operateIv.setVisibility(View.GONE);
			imgIv.setVisibility(View.GONE);
			playLayout.setVisibility(View.VISIBLE);
			player.play(tUrl);
		} else {
			// 非WiFi环境下，不直接播放视频，并弹出提示框提醒用户播放该视频会消耗流量并询问是否继续
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					showTipPop(imgIv);
				}
			}, 1000);
		}
	}

	/**
	 * 显示提示框
	 * 
	 * @param parent
	 */
	public void showTipPop(View parent) {
		final View dialogView = mInflater.inflate(R.layout.player_tip, null,
				false);
		// 提示信息
		final TextView tipTv = (TextView) dialogView.findViewById(R.id.tv_tip);
		// 创建弹出对话框，设置弹出对话框的背景为圆角
		tipPw = new PopupWindow(dialogView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		// 响应返回键
		tipPw.setFocusable(true);
		// Cancel按钮及其处理事件
		final TextView btnCancel = (TextView) dialogView
				.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (tipPw != null && tipPw.isShowing()) {
					tipPw.dismiss();// 关闭
				}
			}
		});
		final TextView btnOk = (TextView) dialogView.findViewById(R.id.btn_ok);
		btnOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (tipPw != null && tipPw.isShowing()) {
					tipPw.dismiss();// 关闭
				}
				wifiPlay(0);
			}
		});

		// 显示RoundCorner对话框
		tipPw.showAtLocation(parent, Gravity.TOP | Gravity.CENTER, 0, 0);

	}

	// 验证本地是否收藏
	private void validLocalIsCollect() {

		FavoriteEntity entity = dbInterface.getFavorite(vsid);
		if (entity != null) {
			collectIv.setImageDrawable(getResources().getDrawable(R.drawable.collect_yes));
			collectLocalFlag = 1;
		} else {
			collectIv.setImageDrawable(getResources().getDrawable(R.drawable.collect_no));
			collectLocalFlag = 0;
		}
	}

	// 验证是否收藏
	private void validIsCollect() {
		// params2.clear();
		// JSONObject object = new JSONObject();
		// try {
		// object.put("object_id", id);
		// object.put("user_id", userId != null ? Integer.parseInt(userId)
		// : null);
		// object.put("collect_type", CollectTypeEnum.SP.value());
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// params2.put("data", object.toString());
		// mHandler2.getHttpPostJson(
		// WebAddressEnum.CCTV_COLLECT_STATUS.toString(), params2,
		// ResponseData.class, VALID_DATA);

	}

	/**
	 * 请求粗切数据
	 */
	private void requestCQData(int flag) {
		em = "1";
		params.clear();
		params.put("serviceId", Constants.SERVICEID);
		params.put("vsid", vsid);
		params.put("em", em);
		params.put("n", Constants.PAGESIZE);
		params.put("p", curPage1 + "");
		String url = mHandler1.appendParameter(
				WebAddressEnum.CCTV_DETAIL.toString(), params);
		mHandler1.getHttpJson(url, CCTVDtlPageData.class, flag);
	}

	/**
	 * 请求精切数据
	 */
	private void requestJQData(int flag) {
		params.clear();
		em = "2";
		params.put("serviceId", Constants.SERVICEID);
		params.put("vsid", vsid);
		params.put("em", em);
		params.put("n", Constants.PAGESIZE);
		params.put("p", curPage2 + "");
		String url = mHandler1.appendParameter(
				WebAddressEnum.CCTV_DETAIL.toString(), params);
		mHandler1.getHttpJson(url, CCTVDtlPageData.class, flag);
	}

	private void onLoad1() {
		mListView1.stopRefresh();
		mListView1.stopLoadMore();
		mListView1.setRefreshTime(TimeHelper.getCurrentData());
	}

	private void onLoad2() {
		mListView2.stopRefresh();
		mListView2.stopLoadMore();
		mListView2.setRefreshTime(TimeHelper.getCurrentData());
	}

	IXListViewListener xListViewListener1 = new IXListViewListener() {

		@Override
		public void onRefresh() {
			if (NetUtil.isNetConnected(CCTVDetailActivity.this)) {
				curPage1 = 1;
				isRefresh = true;
				requestCQData(LIST_DATA1);
			} else {
				mListView1.stopRefresh();
				isRefresh = false;
				ToastUtil.showShort(CCTVDetailActivity.this, R.string.network_invalid);
			}
		}

		@Override
		public void onLoadMore() {
			if (NetUtil.isNetConnected(CCTVDetailActivity.this)) {
				curPage1++;
				isLoadMore = true;
				requestCQData(LIST_DATA1);
			} else {
				mListView1.stopLoadMore();
				isLoadMore = false;
				ToastUtil.showShort(CCTVDetailActivity.this, "请连接网络");
			}
		}
	};

	IXListViewListener xListViewListener2 = new IXListViewListener() {

		@Override
		public void onRefresh() {
			if (NetUtil.isNetConnected(CCTVDetailActivity.this)) {
				curPage2 = 1;
				isRefresh = true;
				requestJQData(LIST_DATA2);
			} else {
				mListView2.stopRefresh();
				isRefresh = false;
				ToastUtil.showShort(CCTVDetailActivity.this, R.string.network_invalid);
			}
		}

		@Override
		public void onLoadMore() {
			if (NetUtil.isNetConnected(CCTVDetailActivity.this)) {
				curPage2++;
				isLoadMore = true;
				requestJQData(LIST_DATA2);
			} else {
				mListView2.stopLoadMore();
				isLoadMore = false;
				ToastUtil.showShort(CCTVDetailActivity.this, R.string.network_invalid);
			}
		}
	};

	OnItemClickListener itemClick1 = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			if (mEntityList1 != null && mEntityList1.size() > 0) {
				try {
					isClickList2 = false;
					curEntity = mEntityList1.get(position
							- mListView1.getHeaderViewsCount());

					//统计
					MobileAppTracker.trackEvent(curEntity.getT(), "列表", "CCTV", 0, curEntity.getVid(), "视频观看",CCTVDetailActivity.this);
					MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);

					Log.i("统计","CCTV央视名栏详情"+curEntity.getT()+"*****"+curEntity.getVid());
					if (videoList != null && curEntity != null) {
						// 记录之前的播放记录
						addPlayHistory();
						doPlay(0);
					}
				} catch (Exception e) {
					L.e(e.toString());
				}
			}
		}
	};

	OnItemClickListener itemClick2 = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			if (mEntityList2 != null && mEntityList2.size() > 0) {
				try {
					isClickList2 = true;
					curEntity = mEntityList2.get(position
							- mListView2.getHeaderViewsCount());

					mEntityList2.get(position).getT();
					//统计
					MobileAppTracker.trackEvent(mEntityList2.get(position).getT(), "列表", "CCTV", 0, curEntity.getVid(), "视频观看",CCTVDetailActivity.this);
					MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
					Log.i("统计：","视频ID："+curEntity.getVid());
					Log.i("统计: ","点击了"+"CCTV央视名栏详情"+tab_text[1].toString()+"视频");
					if (videoList != null && curEntity != null) {
						// 记录之前的播放记录
						addPlayHistory();
						doPlay(1);
					}
				} catch (Exception e) {
					L.e(e.toString());
				}
			}
		}
	};

	private void initListener() {
		mListView1.setOnItemClickListener(itemClick1);
		mListView2.setOnItemClickListener(itemClick2);
		mListView1.setXListViewListener(xListViewListener1);
		mListView2.setXListViewListener(xListViewListener2);
	}

	/**
	 * 设置tab相关数据(tab对应的activity设置不可见，这里只需要tab的样式)
	 */
	private void initTab() {
		int tabCount = 0;
		if (cqCount > 0) {
			tabCount++;
		}
		if (jqCount > 0) {
			tabCount++;
		}
		if (tabCount == 0) {
			// 无粗切、精切数据
			return;
		}
		Intent intent;
		RelativeLayout tabIndicator;
		View bottomLine, bottomLineGrey;
		for (int i = 0; i < tabCount; i++) {
			intent = new Intent(this, EmptyActivity.class);
			tabIndicator = (RelativeLayout) mInflater.inflate(
					R.layout.cctv_tab_item, null);
			bottomLine = tabIndicator.findViewById(R.id.layout_tab_bottom_line);
			bottomLineGrey = tabIndicator
					.findViewById(R.id.layout_tab_bottom_line_grey);
			bottomLine.setVisibility(i == 0 ? View.VISIBLE : View.GONE);
			bottomLineGrey.setVisibility(i == 0 ? View.GONE : View.VISIBLE);
			tabHost.addTab(tabHost.newTabSpec(tag_id[i])
					.setIndicator(tabIndicator).setContent(intent));
		}
		setTabText(tabCount);
		dealList1Data();
	}

	/**
	 * 从服务器获取tab数据
	 */
	private void requestTabData() {
		// mHandler1.getHttpJson(WebAddressEnum.CCTV_TAB.toString(),
		// CCTVPageData.class, TAB_DATA);
	}

	OnTabChangeListener mTabChange = new OnTabChangeListener() {

		@Override
		public void onTabChanged(String tabId) {
			cleanBottomLine();
			for (int i = 0; i < tag_id.length; i++) {
				if (tag_id[i].equals(tabId)) {
					setBottomLine(i);
				} else {
					setBottomGreyLine(i);
				}
			}
			if (tag_id[0].equals(tabId)) {
				// 切换到第一个tab
				mListView1.setVisibility(View.VISIBLE);
				mListView2.setVisibility(View.GONE);
			} else {
				// 切换到第二个tab
				mListView1.setVisibility(View.GONE);
				mListView2.setVisibility(View.VISIBLE);
				if (isInit) {
					// 第一次切换到第二个tab，设置列表2的适配器
					dealList2Data();
					isInit = false;
				}
			}

		}
	};

	/**
	 * 清除tab底部线
	 */
	private void cleanBottomLine() {
		View bottomLine, bottomGreyLine;
		for (int i = 0; i < tag_id.length; i++) {
			View childView = tabHost.getTabWidget().getChildAt(i);
			if (childView != null) {
				bottomLine = childView
						.findViewById(R.id.layout_tab_bottom_line);
				bottomGreyLine = childView
						.findViewById(R.id.layout_tab_bottom_line_grey);
				if (bottomLine != null) {
					bottomLine.setVisibility(View.GONE);
				}
				if (bottomGreyLine != null) {
					bottomGreyLine.setVisibility(View.GONE);
				}
			}

		}
	}

	/**
	 * 显示指定位置的tab的底部线
	 * 
	 * @param position
	 */
	private void setBottomLine(int position) {
		View bottomLine;
		View childView = tabHost.getTabWidget().getChildAt(position);
		if (childView != null) {
			bottomLine = childView.findViewById(R.id.layout_tab_bottom_line);
			if (bottomLine != null) {
				bottomLine.setVisibility(View.VISIBLE);
			}
		}

	}

	/**
	 * 显示指定位置的tab的灰色底部线
	 * 
	 * @param position
	 */
	private void setBottomGreyLine(int position) {
		View bottomGreyLine;
		View childView = tabHost.getTabWidget().getChildAt(position);
		if (childView != null) {
			bottomGreyLine = childView
					.findViewById(R.id.layout_tab_bottom_line_grey);
			if (bottomGreyLine != null) {
				bottomGreyLine.setVisibility(View.VISIBLE);
			}
		}

	}



	/**
	 * 点击播放视频
	 */
	public void onPlayClick(View v) {
		wifiPlay(isClickList2 ? 1 : 0);
	}

	/**
	 * 展开或收缩栏目介绍
	 * 
	 * @param v
	 */
	public void onExpandClick(View v) {
		if (brefTv.getVisibility() == View.VISIBLE) {
			brefTv.setVisibility(View.GONE);// 栏目介绍可见，收缩
			arrowIv.setImageDrawable(getResources().getDrawable(
					R.drawable.lpanda_show));// 设置向下箭头
		} else {
			// 栏目介绍不可见，展开
			brefTv.setVisibility(View.VISIBLE);
			arrowIv.setImageDrawable(getResources().getDrawable(
					R.drawable.lpanda_off));// 设置向上箭头
		}
	}

	/**
	 * 本地添加收藏
	 */
	private void localAddCollect() {
		// 本地收藏：收藏Id和视频Id保持一直，便于在页面初始化时判断该视频是否已收藏
		FavoriteEntity favorite = new FavoriteEntity();
		favorite.setCollect_id(vsid);
		favorite.setObject_id(vsid);
		String a[] = title.split("》");
		StringBuffer sb = new StringBuffer();
		sb.append(a[0]);
		favorite.setObject_title(sb.toString()+"》");
		Log.i("jacktitle",sb.toString()+"》");
		favorite.setObject_logo(image);
		// collect.setObject_url(source == 0 ? entity.getUrl() : bigImgEntity
		// .getUrl());
		favorite.setVideo_pid(null);
		favorite.setIsShowEditUi(false);
		favorite.setObject_title2(curEntity.getT());
		favorite.setFlag(9);
		favorite.setCollect_date(System.currentTimeMillis());
		favorite.setCollect_type(CollectTypeEnum.SP.value());// 收藏类型
		favorite.setVideoLength(curEntity.getLen());// 视频时长
		favorite.setPageSource(CollectPageSourceEnum.YSML.value());
		favorite.setIsSingleVideo(false);// 不是单视频

		DBInterface.getInstance().insertOrUpdateFavorite(favorite);

		PopWindowUtils.getInstance().showPopWindowCenter(this,
				collectIv, R.layout.ppw_define_cue_center,
				this.getResources().getString(R.string.collect_yes),
				true, 2000);
		// 设置收藏图片
		collectIv.setImageDrawable(this.getResources().getDrawable(
				R.drawable.collect_yes));

		collectLocalFlag = 1;

		dismissLoadDialog();
	}

	/**
	 * 本地取消收藏
	 */
	private void localCancelCollect() {

		dbInterface.deleteFavorite(vsid);

		PopWindowUtils.getInstance().showPopWindowCenter(this,
				collectIv, R.layout.ppw_define_cue_center,
				this.getResources().getString(R.string.collect_no),
				true, 2000);
		// 设置收藏图片
		collectIv.setImageDrawable(getResources().getDrawable(
				R.drawable.collect_no));

		collectLocalFlag = 0;

		dismissLoadDialog();
	}

	/**
	 * 点击收藏
	 */
	public void onCollectClick(View v) {
		// 当前没有登录用户，添加收藏到本地
		showLoadingDialog();
		if (collectLocalFlag == 0) {
			// 本地添加收藏
			MobileAppTracker.trackEvent(title, "列表", "CCTV", 0, vsid, "收藏", this);
			MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
			Log.i("统计","收藏Title："+title+"****ID:"+vsid);
			localAddCollect();
		} else if (collectLocalFlag == 1) {
			// 本地取消收藏
			localCancelCollect();
		}
		return;
		// if (collectFlag == -1) {
		// return;
		// }
		// params2.clear();
		// JSONObject object = new JSONObject();
		// switch (collectFlag) {
		// case 0:
		// // 收藏
		// try {
		// object.put("user_id", userId != null ? Integer.parseInt(userId)
		// : null);// 用户ID
		// object.put("object_id", source == 0 ? entity.getId()
		// : bigImgEntity.getId());// 视频ID/视频集Vid/栏目ID必传项）
		// object.put("object_title", source == 0 ? entity.getTitle()
		// : bigImgEntity.getTitle());// 视频/视频集/栏目标题
		//
		// object.put("object_logo", source == 0 ? entity.getImage()
		// : bigImgEntity.getImage());// 视频/视频集/栏目图标
		// // object.put("object_url", source == 0 ? entity.getUrl()
		// // : bigImgEntity.getUrl());// 视频/视频集/栏目链接地5740
		// object.put("video_pid", null);// 视频播放guid(注：收藏视频时传送)
		// object.put("collect_date", System.currentTimeMillis() + "");// 收藏时间
		// object.put("collect_type", CollectTypeEnum.SP.value() + "");// 收藏类型
		// object.put("source", "2");// 视频来源(1：PC,2：手机,3：平板, 4：TV，5:网页端)
		// object.put("product", "8");// 产品业务线(1：CBOX,2：手机版央视影音,3：CBOX
		// // TV版,4:手机央视网，5:多语种网页端,6:维语手机端，7：哈语手机端
		// // 8、熊猫移动客户端)
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// break;
		// case 1:
		// // 取消收藏
		// try {
		// object.put("collect_id", collectId);
		// object.put("user_id", userId != null ? Integer.parseInt(userId)
		// : null);
		// object.put("collect_type", CollectTypeEnum.SP.value() + "");
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// break;
		// default:
		// break;
		// }
		// params2.put("data", object.toString());
		// showLoadingDialog();
		// mHandler2.getHttpPostJson(
		// collectFlag == 0 ? WebAddressEnum.CCTV_COLLECT_ADD.toString()
		// : WebAddressEnum.CCTV_COLLECT_CANCEL.toString(),
		// params2, ResponseData.class, collectFlag == 0 ? ADD_DATA
		// : CANCEL_DATA);
	}

	/**
	 * 点击分享
	 */
	public void onShareClick(View v) {
		MobileAppTracker.trackEvent(title, "列表", "CCTV", 0, vsid, "分享", this);
		MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
		Log.i("统计","分享Title："+title+"****ID:"+vsid);
		//修改分享
		shareController.showPopWindow(v, curEntity.getT(),vsid,"1",videoLength,image,curEntity.getVid());
	}


	class ViewClick implements OnClickListener {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.common_title_left:
				finish();
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivNoNet:
			if (NetUtil.isNetConnected(this)) {
				setNetView(true);
				dealPageData();
				userId = UserManager.getInstance().getUserId();
				if (!StringUtil.isNullOrEmpty(userId)) {
					// 如果当前登录有用户，
					// 验证是否收藏
					validIsCollect();
				}
			}
			break;
		}
	}

	private void registerReceiver() {
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		myReceiver = new ConnectionChangeReceiver();
		this.registerReceiver(myReceiver, filter);
	}

	private void unregisterReceiver() {
		this.unregisterReceiver(myReceiver);
	}

	public class ConnectionChangeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mobNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

			if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				// ///WiFi网络 
				// 从非WiFi环境切换至WiFi环境，WiFi环境变化不影响节目播放状态，弹出提示框“已切换至WiFi”，同时自动调整清晰度
				PopWindowUtils.getInstance().showPopWindowCenter(
						CCTVDetailActivity.this, imgIv,
						R.layout.player_tip_net,
						getResources().getString(R.string.player_tip7), true,
						2000);
				// 调整清晰度

			} else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				// ///////3g网络 
				if (videoView != null && videoView.isPlaying()) {
					// 从WiFi环境切换至非WiFi环境，如有正在播放的视频，暂停该视频，并弹出提示框提醒用户播放该视频会消耗流量并询问是否继续
					player.onPause();
					if (tipPw != null && !tipPw.isShowing()) {
						tipPw.showAtLocation(imgIv, Gravity.TOP
								| Gravity.CENTER, 0, 0);
					} else {
						showTipPop(imgIv);
					}
				}
			}
			if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
				PopWindowUtils.getInstance().showPopWindowCenter(
						CCTVDetailActivity.this, imgIv,
						R.layout.player_tip_net,
						getResources().getString(R.string.player_tip2), true,
						2000);
			}
			// if (mobNetInfo.isConnected()) {
			// if (videoView != null && videoView.isPlaying()) {
			// // 从WiFi环境切换至非WiFi环境，如有正在播放的视频，暂停该视频，并弹出提示框提醒用户播放该视频会消耗流量并询问是否继续
			// player.onPause();
			// if (tipPw != null && !tipPw.isShowing()) {
			// tipPw.showAtLocation(imgIv, Gravity.TOP
			// | Gravity.CENTER, 0, 0);
			// } else {
			// showTipPop(imgIv);
			// }
			// }
			// } else if (wifiNetInfo.isConnected()) {
			// // 从非WiFi环境切换至WiFi环境，WiFi环境变化不影响节目播放状态，弹出提示框“已切换至WiFi”，同时自动调整清晰度
			// PopWindowUtils.getInstance().showPopWindowCenter(
			// CCTVDetailActivity.this, imgIv,
			// R.layout.player_tip_net,
			// getResources().getString(R.string.player_tip7), true,
			// 2000);
			// // 调整清晰度
			// }
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
