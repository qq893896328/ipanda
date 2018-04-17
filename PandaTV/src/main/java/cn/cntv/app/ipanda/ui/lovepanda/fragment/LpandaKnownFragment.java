package cn.cntv.app.ipanda.ui.lovepanda.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gridsum.mobiledissector.MobileAppTracker;
import com.gridsum.mobiledissector.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.CollectPageSourceEnum;
import cn.cntv.app.ipanda.constant.CollectTypeEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.manager.UserManager;
import cn.cntv.app.ipanda.ui.BaseFragment;
import cn.cntv.app.ipanda.ui.cctv.activity.EmptyActivity;
import cn.cntv.app.ipanda.ui.cctv.adapter.CCTVDetailAdapter;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVPageData;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVTabItem;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVVideo;
import cn.cntv.app.ipanda.ui.lovepanda.adapter.LpandaKnowListAdapter;
import cn.cntv.app.ipanda.ui.lovepanda.adapter.LpandaTalkAdapter;
import cn.cntv.app.ipanda.ui.lovepanda.entity.LpanaTalkData;
import cn.cntv.app.ipanda.ui.lovepanda.entity.LpandaBookmark;
import cn.cntv.app.ipanda.ui.lovepanda.entity.LpandaColimnList;
import cn.cntv.app.ipanda.ui.lovepanda.entity.LpandaData;
import cn.cntv.app.ipanda.ui.lovepanda.entity.LpandaLiveRootBean;
import cn.cntv.app.ipanda.ui.lovepanda.entity.LpandaMultiple;
import cn.cntv.app.ipanda.ui.lovepanda.entity.LpandaTalkContentInfo;
import cn.cntv.app.ipanda.ui.lovepanda.entity.LpandaWatchTalkData;
import cn.cntv.app.ipanda.ui.lovepanda.entity.LpandaWatchtalk;
import cn.cntv.app.ipanda.ui.lovepanda.entity.Lpanda_comment;
import cn.cntv.app.ipanda.ui.personal.activity.PersonalLoginActivity;
import cn.cntv.app.ipanda.ui.play.PlayLiveController;
import cn.cntv.app.ipanda.ui.play.entity.PlayLiveEntity;
import cn.cntv.app.ipanda.utils.ComonUtils;
import cn.cntv.app.ipanda.utils.ListUtils;
import cn.cntv.app.ipanda.view.ClearEditText;
import cn.cntv.app.ipanda.view.VerticalScrollview;
import cn.cntv.app.ipanda.xlistview.MyGridView;
import cn.cntv.app.ipanda.xlistview.MyXListView;
import cn.cntv.app.ipanda.xlistview.MyXListView.IXListViewListener;


/**
 * @author：wangrp
 * @Description:熊猫专题事件直播页
 */
public class LpandaKnownFragment extends BaseFragment implements
		OnClickListener {
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private static final String TAG = LpandaKnownFragment.class.getSimpleName();
	private View view;
	private TextView specTv, brefTv;
	private ImageView imgIv, operateIv, arrowIv;
	private static final int LPANDA_LIVE_DATA = 0;
	private static final int JQ_DATA = 2;
	private static final int LIST_DATA1 = 3;
	private static final int LIST_DATA2 = 4;
	private static final int TAB_DATA = 6;
	private static final int LPANDA_TALK_DATA = 1;
	private static final int LPANDA_COMMENT_DATA = 5;

	private static final int TAB_LENGTH = 2;// tab固定是两个
	private LayoutInflater mInflater;
	private TabHost tabHost = null;
	private LocalActivityManager manager = null;
	private Dialog internetDialog;
	private UserManager mUserManager = UserManager.getInstance();

	private String[] tag_id = { "muliti", "talk" };// tab的tag
	private String[] tab_text = { "aa", "bb" };// 每个tab对应的文字
	private String[] tab_url = { "", "" };// 每个tab对应的请求链接

	private CCTVDetailAdapter mAdapter1;// 多语种列表适配器
	private CCTVDetailAdapter mAdapter2;// 央视名栏列表适配器

	private List<CCTVVideo> mEntityList1;// 多语种列表数据集
	private List<CCTVVideo> mEntityList2;// 央视名栏列表数据集

	private boolean isInit = true;// 标识是否为初始化
	private boolean isRefresh = false;// 标识是否为下拉刷新
	private boolean isLoadMore;// 标识是否为加载更多


	private Map<String, String> params = new HashMap<String, String>();// 请求参数

	private List<cn.cntv.app.ipanda.ui.lovepanda.entity.LpandaLive> columnList;
	private Map<String, String> param = new HashMap<String, String>();// 请求参数
	private List<LpandaTalkContentInfo> mtalkContentInfo;
	private List<LpanaTalkData> talklist;
	private List<LpandaMultiple> lpandaMultiple;
	private List<LpandaWatchtalk> lpandaWatchalk;
	private List<LpanaTalkData> mTalkContent;
	// private List<LpandaTalkContent> talkContentInfo;
	private String lpanda_known_talklist_url;

	private LpandaBookmark bookmark;
	private List<LpandaColimnList> listData;// 表格数据集合
	private Lpanda_comment commentState;
	private LpandaTalkAdapter lpandaTalkAdapter;

	private RelativeLayout lpanda_specail_watchtalk;
	private RelativeLayout livepanda_second_jieje;
	private RelativeLayout lpanda_watchtalk;
	private LinearLayout lpanda_special_layout;
	// private Boolean mRefrash;
	private ScrollView lpanda_scroll_list;
//	private MyXListView mListView2;
	private MyGridView mListView2;
	private MyXListView mTalkList;
	private Button lpanda_btn;
	private ClearEditText lpanda_input;
	private ImageView lpanda_no_net_special;
	Lpanda_comment lpandaComment;
	String playurl = "http://asp.cntv.lxdns.com/asp/hls/850/0303000a/3/default/29978c0b08964a59a927b90836dd7485/850.m3u8";
	String lpanda_comment_url = "http://newcomment.cntv.cn/comment/post";
	private LpandaKnowListAdapter listAdapter;
	List<PlayLiveEntity> playLiveEntities;
	private String mParam1;
	private String mParam2;
	// 直播播放控制
	PlayLiveController playerController;
	// 视频播放的布局
	RelativeLayout playLayout;
	// 包裹视频
	RelativeLayout contentPlayView;

	// 大图的视频播放id
	String channleID;

	private String isShow;
	private int page = 1; // 边看边聊列表 页数
	private String lpanda_known_url;// 边看边聊列表 url
	// 是否初始化view
	private boolean isInitView = false;
	// 是否已经将pullrefreshview动态添加进去了
	private boolean isInitPullView = false;
	private VerticalScrollview scroll;

	// 评论分层
	private int commentpageNum = 0;

	private static final int ERROR = Integer.MAX_VALUE; // 请求错误数值

	private View footView;// 列表占位用的footview

	public LpandaKnownFragment() {
		// Required empty public constructor
	}

	// TODO: Rename and change types and number of parameters
	public static LpandaKnownFragment newInstance() {
		LpandaKnownFragment fragment = new LpandaKnownFragment();
		return fragment;
	}

	private void getTabData(LpandaLiveRootBean lpandaLiveData) {

		if (null == lpandaLiveData) {
			return;
		}

		try {
			columnList = lpandaLiveData.getLive();
			bookmark = lpandaLiveData.getBookmark();
			channleID = columnList.get(0).getId();
			isShow = columnList.get(0).getIsshow();
			specTv.setText(getString(R.string.live_now)+ columnList.get(0).getTitle());
			brefTv.setText(columnList.get(0).getBrief());

			if (null != columnList
					&& null != columnList.get(0)
					&& !StringUtil.isNullOrEmpty(columnList.get(0).getImage()
							.trim())) {
				imgIv.setScaleType(ScaleType.CENTER_CROP);


			} else {
				imgIv.setScaleType(ScaleType.FIT_XY);
			}

			// player.play(playurl);
			if (null == channleID && channleID.trim().equals("")) {
				Toast.makeText(getActivity(), R.string.video_data_deletion, Toast.LENGTH_SHORT)
						.show();
			} else {
				if (null == playerController) {
					initPlayView();
				}
				playLiveEntityfrist = new PlayLiveEntity(channleID, columnList
						.get(0).getTitle(), columnList.get(0).getImage(),
						columnList.get(0).getUrl(), null,
						CollectTypeEnum.PD.value() + "",
						CollectPageSourceEnum.XMZB.value(), false);

			}
			if (bookmark != null) {
				initTab();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private XjlHandler<LpandaData> mHandler2 = new XjlHandler<LpandaData>(
			new HandlerListener() {
				@Override
				public void handlerMessage(HandlerMessage msg) {
					switch (msg.what) {
					case LIST_DATA1:
						LpandaData lpandaTabList = (LpandaData) msg.obj;
						if (null != lpandaTabList) {
							if (lpandaTabList.getList() != null
									&& lpandaTabList.getList().size() > 0) {
								playLiveEntities.clear();
								// playLiveEntities.add(playLiveEntityfrist);
								listData = lpandaTabList.getList();
								for (int i = 0; i < listData.size(); i++) {
									try {
										PlayLiveEntity playLiveEntity = new PlayLiveEntity(
												listData.get(i).getId(),
												listData.get(i).getTitle(),
												listData.get(i).getImage(),
												listData.get(i).getUrl(),
												null,
												CollectTypeEnum.PD.value() + "",
												CollectPageSourceEnum.XMZB
														.value(), false);
										playLiveEntities.add(playLiveEntity);
										Glide.with(getActivity())
												.load(listData.get(0).getImage())
												.placeholder(R.drawable._no_img)
												.error(R.drawable._no_img)
												.into(imgIv);
									} catch (Exception e) {
										e.printStackTrace();
										continue;
									}
								}
							}
						}
						if (playLiveEntityfrist != null
								&& playLiveEntities != null) {
							playerController.requestLive(playLiveEntityfrist,
									playLiveEntities);
						}
						dealListData();
						break;
					}
				}
			});
	private XjlHandler<LpandaWatchTalkData> mTalkHandler = new XjlHandler<LpandaWatchTalkData>(
			new HandlerListener() {
				@Override
				public void handlerMessage(HandlerMessage msg) {
					// TODO Auto-generated method stub
					switch (msg.what) {
					// case ERROR:
					// mTalkList.stopLoadMore();
					// showToastLong("服务器繁忙，可以稍后再获取评论！！");
					// break;

					case LPANDA_TALK_DATA:
						mTalkList.stopLoadMore();
						LpandaWatchTalkData lpandaTalk = (LpandaWatchTalkData) msg.obj;
						if (lpandaTalk != null) {
							if (lpandaTalk.getData().getContent() != null
									&& lpandaTalk.getData().getContent().size() > 0) {

								if (mtalkContentInfo.size() > 0) {
									LpandaTalkContentInfo getLastLpandaTalkContentInfo = lpandaTalk
											.getData()
											.getContent()
											.get(lpandaTalk.getData()
													.getContent().size() - 1);
									LpandaTalkContentInfo currLastLpandaTalkContentInfo = mtalkContentInfo
											.get(mtalkContentInfo.size() - 1);

									if (getLastLpandaTalkContentInfo
											.getPid()
											.equals(currLastLpandaTalkContentInfo
													.getPid())) {
										Toast.makeText(getActivity(), R.string.no_more_data,
												Toast.LENGTH_SHORT).show();
										// mTalkList.setPullLoadEnable(false);
										return;
									}
								} else {
									lpandaTalkAdapter.setCommentCount(Integer
											.parseInt(lpandaTalk.getData()
													.getTotal()));
								}

								mtalkContentInfo.addAll(lpandaTalk.getData()
										.getContent());
								// getTalkData();
								lpandaTalkAdapter.notifyDataSetChanged();
							} else {
								Toast.makeText(getActivity(), R.string.no_more_data, Toast.LENGTH_SHORT)
										.show();
								mTalkList.stopLoadMore();
							}
						}
					}
				}
			});

	// private void getTalkData() {
	//
	// if(null != mtalkContentInfo){
	// // lpandaTalkAdapter = new LpandaTalkAdapter(getActivity(),
	// // mtalkContentInfo);
	// // mTalkList.setAdapter(lpandaTalkAdapter);
	// // new ListUtils(getActivity())
	// // .setListViewHeightBasedOnChildrenVer(mTalkList);
	// }
	//
	// }

	private void dealListData() {

		if (null != listData) {
			int totalHeight = dip2px(getActivity(), 65) * 4 + 60;
			listAdapter = new LpandaKnowListAdapter(getActivity(), listData);
			if (columnList != null && columnList.size() > 0) {
				footView.setVisibility(View.INVISIBLE);
//				mListView2.addFooterView(footView);
				mListView2.setAdapter(listAdapter);
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mListView2
						.getLayoutParams();
				params.height = totalHeight
						+ (500* (listAdapter.getCount() - 1));
				mListView2.setLayoutParams(params);
//				new ListUtils(getActivity())
//						.setListViewHeightBasedOnChildrenVer(mListView2);
//				Log.i("lpanda:", arg1)

			} else {
				// 无数据处理
			}
		}
	}
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	LpandaLiveRootBean lpandaLive;
	private XjlHandler<LpandaLiveRootBean> mHandler1 = new XjlHandler<LpandaLiveRootBean>(
			new HandlerListener() {
				@Override
				public void handlerMessage(HandlerMessage msg) {
					switch (msg.what) {
					case Integer.MAX_VALUE:
						break;
					case LPANDA_LIVE_DATA:
						lpandaLive = (LpandaLiveRootBean) msg.obj;
						if (null != lpandaLive && null != lpandaLive.getLive()) {
							columnList = lpandaLive.getLive();
							getTabData(lpandaLive);
						}
						dismissLoadDialog();
						break;
					}
				}
			});

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
			tab_url[1] = datas.get(0).getUrl();
			setTabText(2);
			// 因默认是第一个列表，所以请求第一个列表数据
			requestFirListData();
		}
	}

	/**
	 * 请求第一个页签对应的列表数据
	 */
	private void requestFirListData() {
		String url = tab_url[0];
		mHandler2.getHttpJson(url, LpandaData.class, LIST_DATA1);
	}

	/**
	 * 请求第二个页签对应的列表数据
	 */
	private void requestTalkList() {
		// TODO Auto-generated method stub
		commentpageNum++;

		String talkUrl = "http://newcomment.cntv.cn/comment/list";
		// if(columnList.get(0).getUrl()!=null){

		params.clear();
		params.put("app", "ipandaApp");
		// params.put("itemid", "http://live.ipanda.com/stream/");
		// params.put("itemid", columnList.get(0).getUrl());
		if (bookmark.getWatchTalk().get(0).getUrl() != null) {
			params.put("itemid", bookmark.getWatchTalk().get(0).getUrl());
		}
		params.put("nature", 1 + "");
		params.put("page", Integer.toString(commentpageNum));
		mTalkHandler.getHttpJson(mTalkHandler.appendParameter(talkUrl, params),
				LpandaWatchTalkData.class, LPANDA_TALK_DATA);
		// }else{
		// Toast.makeText(getActivity(), "暂无相关评论", 0).show();
		// mTalkList.setPullLoadEnable(false);
		// }
		System.out.println("lpanda:gettalk" + talkUrl + params);
	}

	/**
	 * 设置tab页签的文本
	 */
	@SuppressLint("NewApi")
	private void setTabText(int tabCount) {
		for (int i = 0; i < tabCount; i++) {
			View childView = tabHost.getTabWidget().getChildAt(i);
			LayoutParams para;
			if (childView != null) {
				TextView tvTab = (TextView) childView
						.findViewById(R.id.tv_title);
				para = (LayoutParams) tvTab.getLayoutParams();
				if (isShow.equals("false")) {
					if (i == 0) {
						tab_url[i] = lpandaMultiple.get(0).getUrl();
						tvTab.setText(lpandaMultiple.get(0).getTitle());
						tvTab.setTextColor(Color.BLACK);
						para.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
						para.removeRule(RelativeLayout.CENTER_HORIZONTAL);
						para.leftMargin = 30;
						para.topMargin = 40;
					}
					tvTab.setLayoutParams(para);
					tvTab.setText(tab_text[i]);

				} else {
					if (i == 0) {
						tab_url[i] = lpandaMultiple.get(0).getUrl();
						tvTab.setText(lpandaMultiple.get(0).getTitle());
					} else if (i == 1) {
						tab_url[i] = lpandaWatchalk.get(0).getUrl();
						tvTab.setText(lpandaWatchalk.get(0).getTitle());
					}
				}

			}

		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
			lpandaurl = getArguments().getString("lpandaurl");
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		manager = new LocalActivityManager(getActivity(), true);
		manager.dispatchCreate(savedInstanceState);
		// initView();
		// initData();
		// initListener();
		if (getUserVisibleHint()) {
			// setUserVisibleHint在onCreateView会在之前，调用此方法实现懒加载
			setUserVisibleHint(true);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.lpandalive_known, container, false);
		isInitView = true;
		return view;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (isInitView) {
				// 第二个条目之后的预加载
				if (!isInitPullView) {
					initView();
					initData();
					// initListener();
				}
			}
		}
	}

	private void initView() {
		mInflater = LayoutInflater.from(getActivity());
		// lpanda_load_view = mInflater
		// .inflate(R.layout.lpanda_loadmore_btn, null);
		specTv = (TextView) view.findViewById(R.id.spec);
		brefTv = (TextView) view.findViewById(R.id.bref);
		imgIv = (ImageView) view.findViewById(R.id.img);
		operateIv = (ImageView) view.findViewById(R.id.operate);
		operateIv.setOnClickListener(this);
		arrowIv = (ImageView) view.findViewById(R.id.arrow);
		livepanda_second_jieje= (RelativeLayout) view.findViewById(R.id.livepanda_second_jieje);
		// lpanda_scroll_list=(ScrollView)
		// getActivity().findViewById(R.id.lpanda_scrollList);
		scroll = (VerticalScrollview) view.findViewById(R.id.lpanda_scrollList);
		tabHost = (TabHost) view.findViewById(android.R.id.tabhost);
		tabHost.setup(manager);
		tabHost.setOnTabChangedListener(mTabChange);
		lpanda_no_net_special = (ImageView) view
				.findViewById(R.id.lpanda_no_net_special);
		lpanda_no_net_special.setOnClickListener(this);
		lpanda_watchtalk = (RelativeLayout) view
				.findViewById(R.id.lpanda_specail_watchtalk);
		lpanda_special_layout = (LinearLayout) view
				.findViewById(R.id.lpanda_special_layout);
		lpanda_special_layout.setOnClickListener(this);
		mListView2 = (MyGridView) view.findViewById(R.id.lpanda_listview2);
		lpanda_btn = (Button) view.findViewById(R.id.lpanda_btn);
		lpanda_btn.setOnClickListener(this);
		lpanda_input = (ClearEditText) view.findViewById(R.id.lpanda_input);

		mTalkList = (MyXListView) view.findViewById(R.id.lpanda_watchtalk_list);
		// mTalkList添加footer
		mTalkList.setPullRefreshEnable(false);
		mTalkList.setPullLoadEnable(true);
		// mListView2.setPullLoadEnable(true);
		
		isRefresh = true;
		initmTalkListListener();
		initListener();
		contentPlayView = (RelativeLayout) view
				.findViewById(R.id.lpandaknown_content_play);
		// ComonUtils.setScale16_9(getActivity(), contentPlayView);
		ComonUtils.setScale16_9(getActivity(), imgIv);
		ComonUtils.setScale16_9(getActivity(), mListView2);
		isInitPullView = true;

		footView = mInflater.inflate(R.layout.lpanda_watchtalk_listitem, null,
				false);
	}

	private void initmTalkListListener() {
		// TODO Auto-generated method stub
		mTalkList.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				// if (mtalkContentInfo != null) {
				// requestTalkList(page);
				// }
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				if (mtalkContentInfo != null) {
					// page++;
					isLoadMore = true;
					requestTalkList();

				}
			}
		});
	}

	private void initData() {
		if (isConnected()) {
			showLoadingDialog();
			String lpandaKnownUrl = lpandaurl.toString().trim();
			if (null == lpandaKnownUrl && lpandaKnownUrl.equals("")) {
				Toast.makeText(getActivity(), R.string.load_fail, Toast.LENGTH_SHORT)
						.show();
			} else {
				mHandler1.getHttpJson(lpandaKnownUrl.toString().trim(),
						LpandaLiveRootBean.class, LPANDA_LIVE_DATA);
			}
		} else {
			dismissLoadDialog();
			Toast.makeText(getActivity(), R.string.network_invalid, Toast.LENGTH_SHORT).show();
			lpanda_no_net_special.setVisibility(View.VISIBLE);
			lpanda_special_layout.setVisibility(View.GONE);
		}

		// 非wifi不播放
		// playLayout.setVisibility(View.VISIBLE);
		mtalkContentInfo = new ArrayList<LpandaTalkContentInfo>();
		lpandaTalkAdapter = new LpandaTalkAdapter(getActivity(),
				mtalkContentInfo);
		mTalkList.setAdapter(lpandaTalkAdapter);
		new ListUtils(getActivity())
				.setListViewHeightBasedOnChildrenVer(mTalkList);
		playLiveEntities = new LinkedList<PlayLiveEntity>();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (playerController != null) {
			playerController.onPause();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (playerController != null) {
			playerController.onResume();
		}
	}


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (playerController != null) {
			playerController.onConfigurationChanged(newConfig);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (playerController != null) {
			playerController.onDestroy();
			if (playLayout != null) {
				contentPlayView
						.removeViewAt(contentPlayView.getChildCount() - 1);
			}
			playerController = null;
		}
	}

	IXListViewListener xListViewListener2 = new IXListViewListener() {

		@Override
		public void onRefresh() {
			if (listData != null) {
				dealListData();
			}
		}

		@Override
		public void onLoadMore() {
			if (listData != null) {
				isLoadMore = true;
				Toast.makeText(getActivity(), R.string.no_more_data, Toast.LENGTH_SHORT).show();
				dealListData();
			}
		}
	};

	private void initListener() {
//		mListView2.setXListViewListener(xListViewListener2);
		mListView2.setOnItemClickListener(lpandaKnowListItemClick);
//		arrowIv.setOnClickListener(onExpandClickListener);
		livepanda_second_jieje.setOnClickListener(onExpandClickListener);
	}

	/**
	 * 多视角下list的item点击进入半屏
	 */
	OnItemClickListener lpandaKnowListItemClick = new OnItemClickListener() {

		private Bundle mBundle;

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			/**
			 * 点播全屏页
			 */
//			position = position - 1;
			String chann = listData.get(position).getId();
			String channTitle = listData.get(position).getTitle();
			String channImage = listData.get(position).getImage();
			String channUrl = listData.get(position).getUrl();
			PlayLiveEntity playLiveEntity = new PlayLiveEntity(chann,
					channTitle, channImage, channUrl, null,
					CollectTypeEnum.PD.value() + "",
					CollectPageSourceEnum.XMZB.value(), false);
			Glide.with(getActivity())
					.load(channImage)
					.placeholder(R.drawable._no_img)
					.error(R.drawable._no_img)
					.into(imgIv);
			//统计
			MobileAppTracker.trackEvent("熊猫直播"+channTitle+"视频", "", "专题事件直播", 0, listData.get(position).getId(), "视频观看",getActivity());
			MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
			android.util.Log.i("统计：","视频ID："+listData.get(position).getId());
			android.util.Log.i("统计: ","点击了"+"熊猫直播"+channTitle+"视频");
			if (playerController == null) {
				initPlayView();
			} else {
				playerController.doPause();
			}
			if (null != playLiveEntity && null != playLiveEntities) {
				playerController.requestLive(playLiveEntity, playLiveEntities);
				specTv.setText(getString(R.string.live_now) +listData.get(position).getTitle());
				Log.i("lpanda:", listData.get(position).getTitle());
				playerController.onResume();
			}
		}
	};

	/**
	 * 设置tab相关数据(tab对应的activity设置不可见，这里只需要tab的样式)
	 */
	private void initTab() {
		int tabCount = 0;
		lpandaMultiple = bookmark.getMultiple();
		lpandaWatchalk = bookmark.getWatchTalk();
		if (lpandaMultiple != null && lpandaMultiple.get(0) != null) {
			tabCount++;
		}
		if (lpandaWatchalk != null && lpandaWatchalk.get(0) != null) {
			tabCount++;
		}
		if (tabCount == 0) {
			return;
		}
		Intent intent;
		RelativeLayout tabIndicator;
		View bottomLine, bottomLineGrey;
		if (isShow.equals("false")) {
			for (int i = 0; i < 1; i++) {
				intent = new Intent(getActivity(), EmptyActivity.class);
				tabIndicator = (RelativeLayout) mInflater.inflate(
						R.layout.lpanda_tab_item, null);
				bottomLine = tabIndicator
						.findViewById(R.id.layout_tab_bottom_line);
				bottomLine.setVisibility(i == 0 ? View.VISIBLE : View.GONE);
				tabHost.addTab(tabHost.newTabSpec(tag_id[i])
						.setIndicator(tabIndicator).setContent(intent));
				cleanBottomLine();
			}
		} else {
			for (int i = 0; i < tabCount; i++) {
				intent = new Intent(getActivity(), EmptyActivity.class);
				tabIndicator = (RelativeLayout) mInflater.inflate(
						R.layout.lpanda_tab_item, null);
				bottomLine = tabIndicator
						.findViewById(R.id.layout_tab_bottom_line);
				bottomLine.setVisibility(i == 0 ? View.VISIBLE : View.GONE);
				tabHost.addTab(tabHost.newTabSpec(tag_id[i])
						.setIndicator(tabIndicator).setContent(intent));
			}
		}
		setBottomGreyLine(1);
		setTabText(tabCount);
		requestFirListData();
		requestTalkList();
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
//统计
				MobileAppTracker.trackEvent("更多直播", null, "专题事件直播", 0, null, "",getActivity());
				MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
				Log.e("统计","事件名称:"+"更多直播"+"***事件标签:"+"熊猫直播*tab"+"专题事件直播"+"***类型:"+"null");
				mListView2.setVisibility(View.VISIBLE);
				lpanda_watchtalk.setVisibility(View.GONE);
				mTalkList.setVisibility(View.GONE);
				// getTalkData();

			} else {
				// 切换到第二个tab
				//统计
				MobileAppTracker.trackEvent("边看边聊", null,"专题事件直播", 0, null, "",getActivity());
				MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
				Log.e("统计","事件名称:"+"边看边聊"+"***事件标签:"+"熊猫直播*tab"+"专题事件直播"+"***类型:"+"null");
				mListView2.setVisibility(View.GONE);
				lpanda_watchtalk.setVisibility(View.VISIBLE);
				mTalkList.setVisibility(View.VISIBLE);
				if (isInit) {
					isInit = false;
				}

			}

		}
	};

	// 初始化视频播放器
	private void initPlayView() {
		playLayout = (RelativeLayout) View.inflate(getActivity(),
				R.layout.giraffe_player_live, null);
		contentPlayView = (RelativeLayout) view
				.findViewById(R.id.lpandaknown_content_play);
		contentPlayView.addView(playLayout);
		playerController = new PlayLiveController(getActivity(), playLayout);
		playerController.setScale16_9(playLayout);

	}

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
	public void onPlayClick() {

	}

	OnClickListener onExpandClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
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
	};

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


	String userId = mUserManager.getUserId();
	String userName =mUserManager.getNickName();

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.operate:
			// 点击播放视频
			if (channleID != null && columnList.get(0).getTitle() != null) {
				if (playerController == null) {
					initPlayView();
				}
				PlayLiveEntity playLiveEntity = new PlayLiveEntity(channleID,
						columnList.get(0).getTitle(), columnList.get(0)
								.getImage(), columnList.get(0).getUrl(), null,
						CollectTypeEnum.PD.value() + "",
						CollectPageSourceEnum.XMZB.value(), false);

				playerController.requestLive(playLiveEntity, playLiveEntities);
				playerController.onResume();
			}
			break;

		case R.id.lpanda_no_net_special:
			if (isConnected()) {
				lpanda_no_net_special.setVisibility(view.GONE);
				lpanda_special_layout.setVisibility(view.VISIBLE);
				initData();
			}
			break;
		case R.id.lpanda_btn:
			if (!StringUtil.isNullOrEmpty(mUserManager.getUserId())) {
				if (lpanda_input.getText().toString().trim().equals("")) {
					Toast.makeText(getActivity(), R.string.please_enter_the_content, Toast.LENGTH_SHORT).show();
				} else {
					goPinglun();
					lpanda_input.setText("");
					// if(lpandaComment.getCode().equals(20000)){
					Toast.makeText(getActivity(), R.string.show_your_comments, Toast.LENGTH_SHORT).show();
					// }
				}
			} else {
				if (playerController != null) {
					playerController.doPause();
				}

				View view = View.inflate(getActivity(),
						R.layout.dialog_internet_tishi, null);
				TextView playcontent = (TextView) view
						.findViewById(R.id.play_continue_content);
				TextView tishiCancel = (TextView) view
						.findViewById(R.id.play_continue_cancel);
				TextView tishiSure = (TextView) view
						.findViewById(R.id.play_continue_sure);
				playcontent.setText(getString(R.string.no_login_tips));
				playcontent.setPadding(80, 50, 80, 50);
				tishiCancel.setText(getString(R.string.cancel));
				tishiSure.setText(getString(R.string.sure));
				internetDialog = new Dialog(getActivity(), R.style.dialog);

				internetDialog.setContentView(view);
				internetDialog.setCanceledOnTouchOutside(true);

				tishiCancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						internetDialog.dismiss();
						if (playerController != null) {
							playerController.doStart();
						}

					}
				});
				tishiSure.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent login_intent = new Intent(getActivity(),
								PersonalLoginActivity.class);
						login_intent.putExtra(PersonalLoginActivity.FromWhere,
								PersonalLoginActivity.FromLpandaKnownPage);
						startActivity(login_intent);
						internetDialog.dismiss();
					}
				});
				internetDialog.show();
			}
			break;
		}
	}

	private XjlHandler<Lpanda_comment> mPingHandler1 = new XjlHandler<Lpanda_comment>(
			new HandlerListener() {

				@Override
				public void handlerMessage(HandlerMessage msg) {
					switch (msg.what) {
					case LPANDA_LIVE_DATA:
						lpandaComment = (Lpanda_comment) msg.obj;
						break;
					}
				}
			});
	private PlayLiveEntity playLiveEntityfrist;
	private String lpandaurl;

	private void goPinglun() {
		if (null == lpanda_comment_url && lpanda_comment_url.equals("")) {
			Toast.makeText(getActivity(), R.string.load_fail, Toast.LENGTH_SHORT).show();
		} else {
			params.clear();
			params.put("app", "ipandaApp");
			params.put("itemid", columnList.get(0).getUrl());
			params.put("data", getData());
			params.put("message", lpanda_input.getText().toString());
			params.put("authorid", mUserManager.getUserId());
			params.put("author", mUserManager.getUserId());
			mPingHandler1.getHttpJson(
					mPingHandler1.appendParameter(lpanda_comment_url, params),
					Lpanda_comment.class, LPANDA_COMMENT_DATA);

		}
	}

	private String getData() {
		// TODO Auto-generated method stub
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

	public boolean onKeyDownBack() {
		if (playerController != null) {
			return playerController.onKeyDownBack();
		}
		return false;
	}
}
