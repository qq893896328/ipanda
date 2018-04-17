package cn.cntv.app.ipanda.ui.lovepanda.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gridsum.mobiledissector.MobileAppTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.CollectPageSourceEnum;
import cn.cntv.app.ipanda.constant.CollectTypeEnum;
import cn.cntv.app.ipanda.constant.WebAddressEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.manager.UserManager;
import cn.cntv.app.ipanda.ui.BaseFragment;
import cn.cntv.app.ipanda.ui.cctv.activity.EmptyActivity;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVPageData;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVTabItem;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVVideo;
import cn.cntv.app.ipanda.ui.lovepanda.adapter.LpandaGridAdapter;
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
import cn.cntv.app.ipanda.utils.Base64;
import cn.cntv.app.ipanda.utils.ComonUtils;
import cn.cntv.app.ipanda.utils.ListUtils;
import cn.cntv.app.ipanda.view.ClearEditText;
import cn.cntv.app.ipanda.view.VerticalScrollview;
import cn.cntv.app.ipanda.xlistview.MyXListView;
import cn.cntv.app.ipanda.xlistview.MyXListView.IXListViewListener;

/**
 * @author： wangrp
 * @Date：
 * @Description:爱熊猫直播页
 */
public class LpandaLiveFragment extends BaseFragment implements OnClickListener {
	private static final String TAG = LpandaLiveFragment.class.getSimpleName();
	private String mParam1;
	private String mParam2;
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private View view;
	private TextView specTv, brefTv;
	private ImageView imgIv, operateIv, arrowIv;
	private RelativeLayout arrowLayout;
	private static final int LPANDA_LIVE_DATA = 0;
	private static final int JQ_DATA = 2;
	private static final int LIST_DATA1 = 3;
	private static final int LIST_DATA2 = 4;
	private static final int TAB_DATA = 6;
	private static final int LPANDA_TALK_DATA = 1;
	private static final int LPANDA_COMMENT_DATA = 5;
	private UserManager mUserManager = UserManager.getInstance();

	private static final int TAB_LENGTH = 2;// tab固定是两个
	private LayoutInflater mInflater;
	private TabHost tabHost = null;
	private LocalActivityManager manager = null;

	private String[] tag_id = { "muliti", "talk" };// tab的tag
	private String[] tab_text ;// 每个tab对应的文字
	private String[] tab_url = { "", "" };// 每个tab对应的请求链接

	private List<CCTVVideo> mEntityList1;// 多语种列表数据集
	private List<CCTVVideo> mEntityList2;// 央视名栏列表数据集

	private int page = 1; // 边看边聊列表 页数
	private String lpanda_live_talklist_url;// 边看边聊列表 url

	private boolean isInit = true;// 标识是否为初始化
	private boolean isRefresh;// 标识是否为下拉刷新
	private boolean isLoadMore;// 标识是否为加载更多
	String channleID;

	// 分享窗口对象
	private PopupWindow pw;
	private ProgressDialog mDlg;

	private String shareTitle = "", shareText, shareUrl,
			shareImgPath = "share_logo.png";
	private String channleShow;//true 显示边看边聊
	private Map<String, String> params = new HashMap<String, String>();// 请求参数

	private int curPage1 = 1, curPage2 = 1;// 当前页数

	private List<cn.cntv.app.ipanda.ui.lovepanda.entity.LpandaLive> columnList;
	//更多直播
	private List<LpandaMultiple> lpandaMultiple;
	//边看边聊
	private List<LpandaWatchtalk> lpandaWatchalk;
	private LpandaBookmark bookmark;
	//tab1 griddata
	private List<LpandaColimnList> gridListData;// 表格数据集合
	private List<LpandaTalkContentInfo> mtalkContentInfo;
	private List<LpanaTalkData> talklist;

	private LpandaGridAdapter lpandaGridAdapter;
	private LpandaTalkAdapter talkAdapter;
	private Dialog internetDialog;
	Lpanda_comment lpandaComment;

	private GridView lpanda_gridview;
	private RelativeLayout lpanda_watchtalk;
	private ImageView lpanda_no_net;
	private RelativeLayout livepanda_jianjie;
	private LinearLayout lpanda_live_layout;
	private MyXListView lpanda_watcktalk_lists;
	private Button lpanda_btn;
	private ClearEditText lpanda_input;
	private VerticalScrollview lpanda_live_scroll;

	String lpanda_comment_url = "http://newcomment.cntv.cn/comment/post";
	List<PlayLiveEntity> playLiveEntities;
	// 直播播放控制
	PlayLiveController playerController;

	// 视频播放的布局
	RelativeLayout playLayout;
	// 包裹视频
	RelativeLayout contentPlayView;

	// 是否初始化view
	private boolean isInitView = false;
	// 是否已经将pullrefreshview动态添加进去了
	private boolean isInitPullView = false;

	//大图的视频
	PlayLiveEntity playLiveEntityFirst;

	//评论分层
	private int commentpageNum = 0;

	public LpandaLiveFragment() {
		// Required empty public constructor
	}

	// TODO: Rename and change types and number of parameters
	public static LpandaLiveFragment newInstance() {
		LpandaLiveFragment fragment = new LpandaLiveFragment();
		return fragment;
	}

	/**
	 * 熊猫直播
	 *
	 * @param lpandaLiveData
	 */
	public void getTabData(LpandaLiveRootBean lpandaLiveData) {
		columnList = lpandaLiveData.getLive();
		bookmark = lpandaLiveData.getBookmark();
		channleID = columnList.get(0).getId();
		channleShow = columnList.get(0).getIsshow();
		// channleID="TITE1451991022695401";

		specTv.setText(getString(R.string.live_now) + columnList.get(0).getTitle());
//		for (int i = 0; i < gridListData.size(); i++) {
////			specTv.setText("[正在直播] " + gridListData.get(i).getTitle());
//			specTv.setText("[正在直播] " + columnList.get(0).getTitle());
//		}

		brefTv.setText(columnList.get(0).getBrief());


		if (bookmark != null) {
			initTab();
		}
		if (null == channleID && channleID.equals("")) {
			Toast.makeText(getActivity(), R.string.video_address_not_exist, Toast.LENGTH_SHORT)
			.show();
		} else {
			if(playerController==null){
				initPlayView();
			}
			playLiveEntityFirst = new PlayLiveEntity(channleID,
					columnList.get(0).getTitle(), columnList.get(0).getImage(),
					columnList.get(0).getUrl(), null, CollectTypeEnum.PD.value()+"",
					CollectPageSourceEnum.XMZB.value(), false);
			//			playerController.requestLive(liveModel, playLiveEntity);

		}

	}

	private XjlHandler<LpandaData> mHandler2 = new XjlHandler<LpandaData>(
			new HandlerListener() {
				@Override
				public void handlerMessage(HandlerMessage msg) {
					switch (msg.what) {
					case LIST_DATA1:
						LpandaData lpandaTabList = (LpandaData) msg.obj;
						if (lpandaTabList!= null) {
							if(lpandaTabList.getList()!=null&&lpandaTabList.getList().size()>0){

								playLiveEntities.clear();
								//playLiveEntities.add(playLiveEntityFirst);
								gridListData = lpandaTabList.getList();

								for (int i = 0; i < gridListData.size(); i++) {
									PlayLiveEntity playLiveEntity = new PlayLiveEntity(
											gridListData.get(i).getId(),
											gridListData.get(i).getTitle(),
											gridListData.get(i).getImage(),
											gridListData.get(i).getUrl(), null, CollectTypeEnum.PD.value()+"",
											CollectPageSourceEnum.XMZB.value(), false);

									playLiveEntities.add(playLiveEntity);

									Glide.with(getActivity()).load(gridListData.get(0).getImage())
											.error(R.drawable._no_img)
											.placeholder(R.drawable._no_img)
											.into(imgIv);
								}
							}
						}


						if(null !=playLiveEntityFirst && null !=playLiveEntities){
							if(playerController!=null){
								playerController.requestLive(playLiveEntityFirst, playLiveEntities);


							}
						}

						if (null != gridListData)
							dealGridData();
						// onLoad2();
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
					case LPANDA_TALK_DATA:
						lpanda_watcktalk_lists.stopLoadMore();
						LpandaWatchTalkData lpandaTalk = (LpandaWatchTalkData) msg.obj;
						if(lpandaTalk!=null){
							if(lpandaTalk.getData().getContent()!=null &&
									lpandaTalk.getData().getContent().size()>0){
								    if(mtalkContentInfo.size()>0){
								    	LpandaTalkContentInfo getLastLpandaTalkContentInfo =
											    lpandaTalk.getData().getContent().get(lpandaTalk.getData().getContent().size()-1);
									    LpandaTalkContentInfo currLastLpandaTalkContentInfo =
									    		mtalkContentInfo.get(mtalkContentInfo.size()-1);

										if(getLastLpandaTalkContentInfo.getPid().equals(currLastLpandaTalkContentInfo.getPid())){
											Toast.makeText(getActivity(), R.string.no_more_data, Toast.LENGTH_SHORT).show();
//											lpanda_watcktalk_lists.setPullLoadEnable(false);
											return;
										}
								    }else {
								    	talkAdapter.setCommentCount(Integer.parseInt(lpandaTalk.getData().getTotal()));
									}

									mtalkContentInfo.addAll(lpandaTalk.getData().getContent());
//									getTalkData();
									talkAdapter.notifyDataSetChanged();
							}else{
								Toast.makeText(getActivity(), R.string.no_more_data, Toast.LENGTH_SHORT).show();
								lpanda_watcktalk_lists.stopLoadMore();
							}
						}
						break;
					}
				}
			});

	/**
	 * dealGridData
	 */
	private void dealGridData() {

		if(null != gridListData){
			int totalHeight = dip2px(getActivity(), 65) * 4 + 60;
//			gridListData.addAll( gridListData.subList(0, 2));
			lpandaGridAdapter = new LpandaGridAdapter(getActivity(), gridListData);
			if (columnList != null && columnList.size() > 0) {
				lpanda_gridview.setAdapter(lpandaGridAdapter);
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lpanda_gridview
						.getLayoutParams();
				params.height = totalHeight
						+ (500* (lpandaGridAdapter.getCount() - 1));
				lpanda_gridview.setLayoutParams(params);

			} else {
				// 无数据处理
			}
		}

	}
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}



//	private void getTalkData() {
//		// TODO Auto-generated method stub
//
//		if(null != mtalkContentInfo){
//
////			talkAdapter = new LpandaTalkAdapter(getActivity(), mtalkContentInfo);
////			lpanda_watcktalk_lists.setAdapter(talkAdapter);
//			talkAdapter.notifyDataSetChanged();
////			new ListUtils(getActivity()).
////			setListViewHeightBasedOnChildrenVer(lpanda_watcktalk_lists);
//		}
//	}

	private XjlHandler<LpandaLiveRootBean> mHandler1 = new XjlHandler<LpandaLiveRootBean>(
			new HandlerListener() {

				@Override
				public void handlerMessage(HandlerMessage msg) {
					switch (msg.what) {
					case Integer.MAX_VALUE:
						break;
					case LPANDA_LIVE_DATA:
						LpandaLiveRootBean lpandaLive = (LpandaLiveRootBean) msg.obj;

						if ( null != lpandaLive && null != lpandaLive.getLive()) {
							columnList = lpandaLive.getLive();
							BaseFragment parentFragment = (BaseFragment) getParentFragment();
							parentFragment.dismissLoadDialog();

							dismissLoadDialog();
							getTabData(lpandaLive);
						}

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
		if (null == data|| null == data.getTablist()) {
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

		try{

			mHandler2.getHttpJson(tab_url[0], LpandaData.class, LIST_DATA1);
		}catch(Exception e){
           e.printStackTrace();
		}
	}

	private void requestTalkList() {
		commentpageNum++;
		String talkUrl = "http://newcomment.cntv.cn/comment/list";
		params.clear();
		params.put("app", "ipandaApp");
//		params.put("itemid", columnList.get(0).getUrl());
		if(bookmark.getWatchTalk() != null && bookmark.getWatchTalk().get(0).getUrl()!=null){
			params.put("itemid", bookmark.getWatchTalk().get(0).getUrl());
		}

		params.put("nature", "1");
		params.put("page",Integer.toString(commentpageNum));
		params.put("prepage", "20");
		mTalkHandler.getHttpJson(
				mTalkHandler.appendParameter(talkUrl, params),
				LpandaWatchTalkData.class, LPANDA_TALK_DATA);
	}

	/**
	 * 设置tab页签的文本
	 * channleShow－false设置居左，
	 * true 两个都居中显示
	 */
	@SuppressLint("NewApi")
	private void setTabText(int tabCount) {
		for (int i = 0; i < tabCount; i++) {
			View childView = tabHost.getTabWidget().getChildAt(i);
			TextView tvTab;
			LayoutParams para;
			if (childView != null) {
				tvTab = (TextView) childView
						.findViewById(R.id.tv_title);
				para=(LayoutParams) tvTab.getLayoutParams();

				try{
					if (channleShow.equals("false")) {
						if (i == 0) {
							tab_url[i] = lpandaMultiple.get(0).getUrl();
							tvTab.setText(lpandaMultiple.get(0).getTitle());
							tvTab.setTextColor(Color.BLACK);
							para.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
							para.removeRule(RelativeLayout.CENTER_HORIZONTAL);
							para.leftMargin=30;
							para.topMargin=40;
						}
						tvTab.setLayoutParams(para);
						tvTab.setText(tab_text[i]);
					}else{
					if (i == 0) {
						tab_url[i] = lpandaMultiple.get(0).getUrl();
						tvTab.setText(lpandaMultiple.get(0).getTitle());

					} else if (i == 1) {
						tab_url[i] = lpandaWatchalk.get(0).getUrl();
						tvTab.setText(lpandaWatchalk.get(0).getTitle());
					}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}

		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tab_text = new String[]{getActivity().getString(R.string.duoshijiaozhibo), getActivity().getString(R.string.biankanbianliao) };// 每个tab对应的文字

		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
			lpanda_talk_id = getArguments().getString("lpanda_talk_id_live");
		}

	}
	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		manager = new LocalActivityManager(getActivity(), true);
		manager.dispatchCreate(savedInstanceState);
		if (getUserVisibleHint()) {
			// setUserVisibleHint在onCreateView会在之前，调用此方法实现懒加载
			setUserVisibleHint(true);
		}
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
				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.lpandalive_home, container, false);
		isInitView = true;
		return view;
	}



	private void initView() {
		mInflater = LayoutInflater.from(getActivity());
		livepanda_jianjie= (RelativeLayout) view.findViewById(R.id.livepanda_jianjie);
		specTv = (TextView) view.findViewById(R.id.lpanda_live_spec);
		brefTv = (TextView) view.findViewById(R.id.lpanda_live_bref);
		imgIv = (ImageView) view.findViewById(R.id.lpanda_live_img);
		operateIv = (ImageView) view.findViewById(R.id.lpanda_live_operate);
		operateIv.setOnClickListener(this);
		arrowIv = (ImageView) view.findViewById(R.id.lpanda_live_arrow);
		arrowLayout = (RelativeLayout) view.findViewById(R.id.arrow_layout);
		lpanda_no_net = (ImageView) view.findViewById(R.id.lpanda_no_net);
		lpanda_no_net.setOnClickListener(this);
		lpanda_live_layout = (LinearLayout) view
				.findViewById(R.id.lpanda_live_layout);
		lpanda_live_scroll=(VerticalScrollview) view.findViewById(R.id.lpanda_live_scroll);

		tabHost = (TabHost) view.findViewById(android.R.id.tabhost);
		tabHost.setup(manager);
		tabHost.setOnTabChangedListener(mTabChange);
		tabHost.requestFocus();
		lpanda_gridview = (GridView) view.findViewById(R.id.lpanda_gridview);
		lpanda_gridview.setFocusable(false);
		lpanda_watchtalk = (RelativeLayout) view
				.findViewById(R.id.lpanda_watchtalk);
		lpanda_watcktalk_lists = (MyXListView) view
				.findViewById(R.id.lpanda_watcktalk_listview);
		lpanda_watcktalk_lists.setPullRefreshEnable(false);
		lpanda_watcktalk_lists.setPullLoadEnable(true);
		lpanda_btn = (Button) view.findViewById(R.id.lpanda_btn);
		lpanda_btn.setOnClickListener(this);
		lpanda_input = (ClearEditText)view.findViewById(R.id.lpanda_input);
		initTalkListListener();
		initListener();
		contentPlayView = (RelativeLayout) view
				.findViewById(R.id.lpanda_live_play_layout);
		ComonUtils.setScale16_9(getActivity(), imgIv);
		ComonUtils.setScale16_9(getActivity(), lpanda_gridview);

		isInitPullView = true;
	}

	private void initTalkListListener() {
		// TODO Auto-generated method stub
		lpanda_watcktalk_lists.setXListViewListener(new IXListViewListener() {
//
			@Override
			public void onRefresh() {
//				// TODO Auto-generated method stub
//				if (mtalkContentInfo != null) {
//					requestTalkList(page);
//				}
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				if (mtalkContentInfo != null) {
//					page++;
					isLoadMore = true;
					requestTalkList();
				}
			}
		});
	}

	private void initData() {
		/**
		 * 判断网络是否连接
		 */
		if (isConnected()) {
			String lpandaLiveUrl=WebAddressEnum.LPANDA_LIVE.toString();
			if(lpandaLiveUrl == null && lpandaLiveUrl.equals("")){
				Toast.makeText(getActivity(), R.string.load_fail, Toast.LENGTH_SHORT)
				.show();
			}else{
				mHandler1.getHttpJson(lpandaLiveUrl,
						LpandaLiveRootBean.class, LPANDA_LIVE_DATA);
			}
		} else {
			Toast.makeText(getActivity(), R.string.network_invalid, Toast.LENGTH_SHORT).show();
			lpanda_no_net.setVisibility(View.VISIBLE);
			lpanda_live_layout.setVisibility(View.GONE);
		}

		String url = "http://asp.cntv.lxdns.com/asp/hls/850/0303000a/3/default/29978c0b08964a59a927b90836dd7485/850.m3u8";
		mtalkContentInfo = new ArrayList<LpandaTalkContentInfo>();
		talkAdapter = new LpandaTalkAdapter(getActivity(), mtalkContentInfo);
		lpanda_watcktalk_lists.setAdapter(talkAdapter);
		new ListUtils(getActivity())
		.setListViewHeightBasedOnChildrenVer(lpanda_watcktalk_lists);

		gridListData = new ArrayList<LpandaColimnList>();
		playLiveEntities=new LinkedList<PlayLiveEntity>();
	}

	/**
	 * onPause
	 */
	@Override
	public void onPause() {
		super.onPause();
		if ( null != playerController) {
			playerController.onPause();

		}
	}

	/**
	 * onResume
	 */
	@Override
	public void onResume() {
		super.onResume();
		if (null != playerController) {
			playerController.onResume();
		}
	}


	/**
	 * 屏幕切换
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if ( null != playerController) {
			playerController.onConfigurationChanged(newConfig);
		}
	}

	/**
	 * onDestroy
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		if ( null!= playerController) {
			playerController.onDestroy();
			if ( null!= playLayout) {
				contentPlayView
				.removeViewAt(contentPlayView.getChildCount() - 1);
			}
			playerController = null;
		}
	}
	String channTitle;
	/**
	 * 多视角下grid的item点击进入全屏
	 */
	OnItemClickListener lpandaLiveGridVeiw = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub


			try{
				String channId = gridListData.get(position).getId();
				 channTitle = gridListData.get(position).getTitle();
				Glide.with(getActivity()).load(gridListData.get(position).getImage())
						.error(R.drawable._no_img)
						.placeholder(R.drawable._no_img)
						.into(imgIv);
				//统计
				MobileAppTracker.trackEvent("熊猫直播"+channTitle+"视频", "", "直播", 0, channId, "视频观看",getActivity());
				MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
				android.util.Log.i("统计：","视频ID："+channId);
				android.util.Log.i("统计: ","点击了"+"熊猫直播*直播"+channTitle+"视频");
				if (null == channId && channId.equals("")) {
					Toast.makeText(getActivity(), R.string.video_data_deletion, Toast.LENGTH_SHORT)
					.show();
				} else {
					if (playerController == null) {
						initPlayView();
					}else{
						playerController.doPause();
					}

					PlayLiveEntity playLiveEntity = new PlayLiveEntity(channId,
							channTitle, gridListData.get(position).getImage(),
							gridListData.get(position).getUrl(), null, CollectTypeEnum.PD.value()+"",
							CollectPageSourceEnum.XMZB.value(), false);
					playerController.requestLive(playLiveEntity,playLiveEntities);
					specTv.setText(getString(R.string.live_now) +gridListData.get(position).getTitle());
					playerController.onResume();
				}
			}catch(Exception e){
				e.printStackTrace();
			}

		}
	};

	// 初始化视频播放器
	private void initPlayView() {
		playLayout = (RelativeLayout) View.inflate(getActivity(),
				R.layout.giraffe_player_live, null);
		contentPlayView = (RelativeLayout) view
				.findViewById(R.id.lpanda_live_play_layout);
		contentPlayView.addView(playLayout);
		playerController = new PlayLiveController(getActivity(), playLayout);
		playerController.setScale16_9(playLayout);

	}

	private void initListener() {
		// mListView2.setXListViewListener(xListViewListener2);
		lpanda_gridview.setOnItemClickListener(lpandaLiveGridVeiw);

//		arrowLayout.setOnClickListener(onExpandClickListener);
		livepanda_jianjie.setOnClickListener(onExpandClickListener);
	}

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
		if (channleShow.equals("false")) {
			for (int i = 0; i < 1; i++) {
				intent = new Intent(getActivity(), EmptyActivity.class);
				tabIndicator = (RelativeLayout) mInflater.inflate(
						R.layout.lpanda_tab_item, null);
				bottomLine = tabIndicator
						.findViewById(R.id.layout_tab_bottom_line);
				bottomLineGrey = tabIndicator
						.findViewById(R.id.layout_tab_bottom_line_grey);
				bottomLine.setVisibility(i == 0 ? View.VISIBLE : View.GONE);
				bottomLineGrey.setVisibility(i == 0 ? View.GONE : View.VISIBLE);
				tabHost.addTab(tabHost.newTabSpec(tag_id[i])
						.setIndicator(tabIndicator).setContent(intent));
				//				tvTab.setTextColor(Color.BLACK);
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
				//统计
				MobileAppTracker.trackEvent("多视角直播", null, "直播", 0, null, "",getActivity());
				MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
				Log.e("统计","事件名称:"+"多视角直播"+"***事件标签:"+"熊猫直播*"+"直播"+"***类型:"+"null");
				// 切换到第一个tab
				lpanda_gridview.setVisibility(View.VISIBLE);
				lpanda_watcktalk_lists.setVisibility(View.GONE);
				lpanda_watchtalk.setVisibility(View.GONE);
				// mListView2.setVisibility(View.GONE);
				//

			} else {
				//统计
				MobileAppTracker.trackEvent("边看边聊", null, "直播", 0, null, "",getActivity());
				MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
				Log.e("统计","事件名称:"+"边看边聊"+"***事件标签:"+"熊猫直播*"+"直播"+"***类型:"+"null");
				// 切换到第二个tab
				lpanda_gridview.setVisibility(View.GONE);
				lpanda_watchtalk.setVisibility(View.VISIBLE);
				lpanda_watcktalk_lists.setVisibility(View.VISIBLE);
				// mListView2.setVisibility(View.VISIBLE);
				if (isInit) {
					// 第一次切换到第二个tab，设置列表2的适配器
					// dealList2Data();
					dealGridData();
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.lpanda_live_operate:
			// 点击播放视频
			if (channleID != null && columnList.get(0).getTitle() != null) {
				if(playerController==null){
					initPlayView();
				}
				PlayLiveEntity playLiveEntity = new PlayLiveEntity(channleID,
						columnList.get(0).getTitle(), columnList.get(0)
						.getImage(), columnList.get(0).getUrl(), null,
						CollectTypeEnum.PD.value()+"",
						CollectPageSourceEnum.XMZB.value(), false);
				playerController.requestLive(playLiveEntity,playLiveEntities);
				playerController.onResume();
			}
			break;

		case R.id.lpanda_no_net:
			if (isConnected()) {
				showLoadingDialog();
				String lpandaLiveUrl=WebAddressEnum.LPANDA_LIVE.toString();
				if(lpandaLiveUrl == null && lpandaLiveUrl.equals("")){
					Toast.makeText(getActivity(), R.string.load_fail, Toast.LENGTH_SHORT)
					.show();
				}else{
					mHandler1.getHttpJson(WebAddressEnum.LPANDA_LIVE.toString(),
							LpandaLiveRootBean.class, LPANDA_LIVE_DATA);
				}
			}else {
				Toast.makeText(getActivity(), R.string.network_invalid, Toast.LENGTH_SHORT).show();
				lpanda_no_net.setVisibility(View.VISIBLE);
				lpanda_live_layout.setVisibility(View.GONE);
			}
			break;
		case R.id.lpanda_btn:
			/**
			 * 根据uid判断是否登录，已登录发表评论等待审核，
			 */

				if (lpanda_input.getText().toString().equals("")) {
					Toast.makeText(getActivity(), R.string.please_enter_the_content, Toast.LENGTH_SHORT).show();
				} else {
					if(!mUserManager.isUserLogged()){
						addDialog();
						return;
					}

					goPinglun();
					lpanda_input.setText("");
					// if(lpandaComment.getCode().equals(20000)){
					Toast.makeText(getActivity(), R.string.show_your_comments, Toast.LENGTH_SHORT).show();
					// }
				}

				if(playerController!=null){
					playerController.doPause();
				}


			break;
		}
	}

	private void addDialog() {
		// TODO Auto-generated method stub
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
				if(playerController!=null){
					playerController.doStart();
				}

			}
		});
		tishiSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent login_intent = new Intent(getActivity(),
						PersonalLoginActivity.class);
				login_intent.putExtra(PersonalLoginActivity.FromWhere, PersonalLoginActivity.FromLpandaKnownPage);
				startActivity(login_intent);
				internetDialog.dismiss();
			}
		});
		internetDialog.show();
	}

	private XjlHandler<Lpanda_comment> mPingHandler1 = new XjlHandler<Lpanda_comment>(
			new HandlerListener() {

				@Override
				public void handlerMessage(HandlerMessage msg) {
					switch (msg.what) {
						case LPANDA_LIVE_DATA:
							lpandaComment = (Lpanda_comment) msg.obj;
							break;
						case LPANDA_COMMENT_DATA:
							lpandaComment = (Lpanda_comment) msg.obj;
							break;
					}

				}
			});

	private TextView tvTab;
	private String lpanda_talk_id;

	// 评论参数 app＝ipandaApp，itemid，authorid，author
	private void goPinglun() {
		params.clear();
		params.put("app", "ipandaApp");
//		params.put("itemid", columnList.get(0).getUrl());
		if(bookmark.getWatchTalk() != null && bookmark.getWatchTalk().get(0).getUrl()!=null){
			params.put("itemid", bookmark.getWatchTalk().get(0).getUrl());
		}
		params.put("authorid",mUserManager.getUserId());
		params.put("author",mUserManager.getNickName());
		params.put("data", getData());
		params.put("message",lpanda_input.getText().toString());
		mPingHandler1.getHttpPostJson(
				lpanda_comment_url, params,
				Lpanda_comment.class, LPANDA_COMMENT_DATA);
		Log.i("lpanda:", lpanda_comment_url+params.toString());
	}

	private String getData() {
		String s = "uid=" + mUserManager.getUserId() + "&time=" + System.currentTimeMillis()/1000;
		String base64 = new String(Base64.encodeToString(s.getBytes(), false));
		return base64;
	}

	public boolean onKeyDownBack() {
		if( null != playerController){
			return playerController.onKeyDownBack();
		}
		return false;
	}
}
