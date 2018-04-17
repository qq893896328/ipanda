package cn.cntv.app.ipanda.ui.cctv.fragment;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.gridsum.mobiledissector.MobileAppTracker;
import com.gridsum.mobiledissector.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.CollectPageSourceEnum;
import cn.cntv.app.ipanda.constant.CollectTypeEnum;
import cn.cntv.app.ipanda.constant.Constants;
import cn.cntv.app.ipanda.constant.JMPTypeEnum;
import cn.cntv.app.ipanda.constant.WebAddressEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.ui.BaseFragment;
import cn.cntv.app.ipanda.ui.cctv.activity.CCTVDetailActivity;
import cn.cntv.app.ipanda.ui.cctv.activity.EmptyActivity;
import cn.cntv.app.ipanda.ui.cctv.adapter.CCTVMuliLaugAdapter;
import cn.cntv.app.ipanda.ui.cctv.adapter.CCTVViewPagerAdapter;
import cn.cntv.app.ipanda.ui.cctv.adapter.CCTVWellKnowAdapter;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVBigImgItem;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVMuliLaugItem;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVPageData;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVTabItem;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVWellKnow;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVWellKnowItem;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVZBD;
import cn.cntv.app.ipanda.ui.home.activity.HomeSSubjectActivity;
import cn.cntv.app.ipanda.ui.home.auto.view.AutoScrollViewPager;
import cn.cntv.app.ipanda.ui.home.entity.BigImg;
import cn.cntv.app.ipanda.ui.home.listener.HomeViewPagerListener;
import cn.cntv.app.ipanda.ui.pandaeye.activity.PandaEyeDetailActivity;
import cn.cntv.app.ipanda.ui.pandaeye.adapter.PEyeAutoScrollAdapter;
import cn.cntv.app.ipanda.ui.play.PlayLiveActivity;
import cn.cntv.app.ipanda.ui.play.PlayVodFullScreeActivity;
import cn.cntv.app.ipanda.ui.play.entity.PlayLiveEntity;
import cn.cntv.app.ipanda.ui.play.entity.PlayVodEntity;
import cn.cntv.app.ipanda.utils.CacheUtil;
import cn.cntv.app.ipanda.utils.L;
import cn.cntv.app.ipanda.utils.Logs;
import cn.cntv.app.ipanda.utils.NetUtil;
import cn.cntv.app.ipanda.utils.PopWindowUtils;
import cn.cntv.app.ipanda.utils.SharePreferenceUtil;
import cn.cntv.app.ipanda.utils.TimeHelper;
import cn.cntv.app.ipanda.utils.ToastUtil;
import cn.cntv.app.ipanda.utils.ViewHolder;
import cn.cntv.app.ipanda.view.PointView;
import cn.cntv.app.ipanda.xlistview.XListView;
import cn.cntv.app.ipanda.xlistview.XListView.IXListViewListener;

/**
 * @author： pj
 * @Date： 2015年12月30日 下午3:53:50
 * @Description:CCTV的Tab功能页
 */
public class CCTVFragment extends BaseFragment implements
		ViewPager.OnPageChangeListener, OnClickListener {
	private static final String TAG = CCTVFragment.class.getSimpleName();
	private static final int TAB_DATA = 0;
	private static final int LIST_DATA1 = 1;
	private static final int LIST_DATA2 = 2;
	private static final int TAB_LENGTH = 2;// tab固定是两个
	private static final int ZBD_DATA = 3;
	private LayoutInflater mInflater;
	private TabHost tabHost = null;
	private LocalActivityManager manager = null;

	private String[] tag_id = { "muliLaug", "wellKnow" };// tab的tag
	private String[] tab_text = { "CCTV多语种", "央视名栏" };// 每个tab对应的文字
	private String[] tab_url = { "", "" };// 每个tab对应的请求链接

	private View view;
	private XListView mListView1, mListView2;// 多语种列表、央视名栏列表
	private CCTVMuliLaugAdapter mAdapter1;// 多语种列表适配器
	private CCTVWellKnowAdapter mAdapter2;// 央视名栏列表适配器

	private List<CCTVMuliLaugItem> mEntityList1;// 多语种列表数据集
	private List<CCTVWellKnowItem> mEntityList2;// 央视名栏列表数据集

	private boolean isInit = true;// 标识是否为初始化
	private boolean isInitNoNet;// 标识是否为初始化页面时即没有网络
	private View headView;// 央视名栏的图片轮播view
	private static final long INTERVAL = 5000;// 轮播时长
	private AutoScrollViewPager viewPager;
	private PointView mPointView;
	private TextView mCueView;
	List<View> views = new ArrayList<View>();// 轮播图的view
	private List<CCTVBigImgItem> imgItems;// 轮播图数据集
	private List<CCTVZBD> zbdEntityList = new ArrayList<CCTVZBD>();
	private CCTVViewPagerAdapter bannerAdapter;


	private boolean isRefresh;// 标识是否为下拉刷新

	private ImageView noNetView;

	private TextView titleCnter;
	private View convertView;

	private RadioButton mRbLovePanda;
	private RadioButton mRbLiveChina;
	private Map<String, String> params = new HashMap<String, String>();// 请求参数


	/**
	 * 用于对 Fragment 进行管理
	 */
	private FragmentManager mFragmentManager;

	private XjlHandler<CCTVPageData> mHandler1 = new XjlHandler<CCTVPageData>(
			new HandlerListener() {

				@Override
				public void handlerMessage(HandlerMessage msg) {

					switch (msg.what) {
					case Integer.MAX_VALUE:
						// 错误处理
						Logs.e(TAG, "");
						break;
					case TAB_DATA:
						// 处理tab数据
						CCTVPageData data1 = (CCTVPageData) msg.obj;
						dealTabData(data1);
						break;
					case LIST_DATA1:
						// 处理第一个tab对应的列表数据
						CCTVPageData data2 = (CCTVPageData) msg.obj;
						if (data2 != null) {
							mEntityList1 = data2.getList();
						}
						sortList();
						break;
					}
				}
			});

	private XjlHandler<CCTVZBD> mHandler3 = new XjlHandler<CCTVZBD>(
			new HandlerListener() {

				@Override
				public void handlerMessage(HandlerMessage msg) {

					switch (msg.what) {
					case Integer.MAX_VALUE:
						// 错误处理
						Logs.e(TAG, "");
						break;
					case ZBD_DATA:
						if (msg.obj != null && msg.obj instanceof CCTVZBD[]) {
							CCTVZBD[] arr = (CCTVZBD[]) msg.obj;
							if (arr != null && arr.length > 0) {
								zbdEntityList = Arrays.asList(arr);
								if (zbdEntityList.size() == mEntityList1.size()) {
									// 节目单已请求完毕
									dealZbd();
								} else {
									dealList1Data();
								}
							} else {
								dealList1Data();
							}
						} else {
							dealList1Data();
						}

						break;
					}
				}
			});

	private XjlHandler<CCTVWellKnow> mHandler2 = new XjlHandler<CCTVWellKnow>(
			new HandlerListener() {

				@Override
				public void handlerMessage(HandlerMessage msg) {

					switch (msg.what) {
					case Integer.MAX_VALUE:
						// 错误处理
						Logs.e(TAG, "");
						break;
					case LIST_DATA2:
						// 处理第二个tab对应的列表数据
						CCTVWellKnow data3 = (CCTVWellKnow) msg.obj;
						if (data3 != null) {
							mEntityList2 = data3.getList();
						}
						imgItems = data3.getBigImg();
						if (imgItems != null && imgItems.size() > 0) {
							// initViewpager();// 设置轮播图
							if (convertView != null) {
								convertView.setVisibility(View.VISIBLE);
							}
							initBigImg();

						} else {
							// 无轮播图数据时不显示
							mListView2.removeHeaderView(convertView);
							if (convertView != null) {
								convertView.setVisibility(View.GONE);
							}
						}
						dealList2Data();
						break;
					}
				}
			});
	private TextView tvTab;

	/**
	 * 处理多语种数据
	 * 
	 * @param
	 */
	private void dealList1Data() {
		try {
			mAdapter1 = new CCTVMuliLaugAdapter(getActivity(), mEntityList1,
					mInflater);
			if (mEntityList1 != null && mEntityList1.size() > 0) {
				mListView1.setAdapter(mAdapter1);
			} else {
				// 无匹配数据时的处理
			}
			if (isRefresh) {
				// 停止下拉刷新
				onLoad1();
				isRefresh = false;
			}
		} catch (Exception e) {
			L.e(TAG, e.toString());
		}
		dismissLoadDialog();
	}

	private void sortList() {
		CCTVMuliLaugItem entity = null;
		String ids = "";
		if (mEntityList1 != null && mEntityList1.size() > 0) {
			boolean hasAyu = false;
			for (int i = 0; i < mEntityList1.size(); i++) {
				entity = mEntityList1.get(i);
				if ("2".equals(entity.getType())) {
					hasAyu = true;
					mEntityList1.remove(entity);
					break;
				}
			}
			if (hasAyu) {
				mEntityList1.add(entity);
			}
		}
		// 使用排序后的集合请求直播单数据
		if (mEntityList1 != null) {
			for (int i = 0; i < mEntityList1.size(); i++) {
				ids += mEntityList1.get(i).getId() + ",";
				if (i == mEntityList1.size() - 1 && ids.length() > 0) {
					ids = ids.substring(0, ids.length() - 1);
				}
			}
			requestZbd(ids);
		}
	}

	private void requestZbd(String ids) {
		// 根据频道Id设置直播单内容
		params.clear();
		params.put("serviceId", Constants.SERVICEID);
		params.put("c", ids);
		String url = mHandler1.appendParameter(
				WebAddressEnum.CCTV_ZBD.toString(), params);
		mHandler3.getHttpJson(url, CCTVZBD[].class, ZBD_DATA);
	}

	private void dealZbd() {
		CCTVMuliLaugItem tEntity;
		CCTVZBD zbdEntity;
		if (mEntityList1 != null && zbdEntityList != null
				&& zbdEntityList.size() > 0) {
			for (int i = 0; i < mEntityList1.size(); i++) {
				tEntity = mEntityList1.get(i);
				for (int j = 0; j < zbdEntityList.size(); j++) {
					zbdEntity = zbdEntityList.get(j);
					if (zbdEntity != null && tEntity.getId() != null
							&& tEntity.getId().equals(zbdEntity.getC())) {
						tEntity.setBrief(StringUtil.isNullOrEmpty(zbdEntity
								.getT()) ? "" : zbdEntity.getT());
					}
				}
			}
		}
		dealList1Data();
	}

	/**
	 * 处理央视名栏列表数据
	 * 
	 * @param
	 */
	private void dealList2Data() {
		try {
			mAdapter2 = new CCTVWellKnowAdapter(getActivity(), mEntityList2,
					mInflater);
			if (mEntityList2 != null && mEntityList2.size() > 0) {
				mListView2.setAdapter(mAdapter2);
			} else {
				// 无匹配数据时的处理
			}
			if (isRefresh) {
				// 停止下拉刷新
				onLoad2();
				isRefresh = false;
			}
		} catch (Exception e) {
			L.e(TAG, e.toString());
		}
		dismissLoadDialog();
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
			setTabText();
			// 因默认是第一个列表，所以请求第一个列表数据
			requestFirListData();
		}
	}

	/**
	 * 请求第一个页签对应的列表数据
	 */
	private void requestFirListData() {
		mHandler1.getHttpJson(tab_url[0], CCTVPageData.class, LIST_DATA1);
	}

	/**
	 * 请求第二个页签对应的列表数据
	 */
	private void requestSecListData() {
		mHandler2.getHttpJson(tab_url[1], CCTVWellKnow.class, LIST_DATA2);
	}

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param
	 *
	 * @param
	 *
	 * @return A new instance of fragment LookTVFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static CCTVFragment newInstance() {
		CCTVFragment fragment = new CCTVFragment();
		return fragment;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		manager = new LocalActivityManager(getActivity(), true);
		manager.dispatchCreate(savedInstanceState);
		tabHost = (TabHost) view.findViewById(android.R.id.tabhost);
		tabHost.setup(manager);
		tabHost.setOnTabChangedListener(mTabChange);
		mListView1 = (XListView) view.findViewById(R.id.cctv_listview1);
		mListView2 = (XListView) view.findViewById(R.id.cctv_listview2);
		mListView1.setPullLoadEnable(false);// 禁用上拉加载更多
		mListView2.setPullLoadEnable(false);
		noNetView = (ImageView) view.findViewById(R.id.ivNoNet);
		noNetView.setOnClickListener(this);
		mFragmentManager = getActivity().getSupportFragmentManager();

		titleCnter = (TextView) getActivity().findViewById(R.id.title_center);
		mRbLovePanda = (RadioButton) getActivity().findViewById(
				R.id.rbTabLovePanda);
		mRbLiveChina = (RadioButton) getActivity().findViewById(
				R.id.rbTabLiveChina);
		// 缓存熊猫直播按钮对象的引用
		if (CacheUtil.mRbLovePanda == null) {
			CacheUtil.mRbLovePanda = mRbLovePanda;
		}
		// 缓存直播中国按钮对象的引用
		if (CacheUtil.mRbLiveChina == null) {
			CacheUtil.mRbLiveChina = mRbLiveChina;
		}
		titleCnter.setText("CCTV");
		if (NetUtil.isNetConnected(getActivity())) {
			dealPageData();
			noNetView.setVisibility(View.GONE);
			isInitNoNet = false;
		} else {
			isInitNoNet = true;
			noNetView.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 处理页面数据
	 */
	private void dealPageData() {
		showLoadingDialog();
		initTab();
		initCarouselView();
		initListener();
	}

	private void initListener() {
		mListView1.setOnItemClickListener(itemClick1);
		mListView1.setXListViewListener(xListViewListener1);
		mListView2.setOnItemClickListener(itemClick2);
		mListView2.setXListViewListener(xListViewListener2);
	}

	IXListViewListener xListViewListener1 = new IXListViewListener() {

		@Override
		public void onRefresh() {
			if (NetUtil.isNetConnected(getActivity())) {
				isRefresh = true;
				requestFirListData();
				noNetView.setVisibility(View.GONE);
			} else {
				onLoad1();
				isRefresh = false;
				noNetView.setVisibility(View.VISIBLE);
				ToastUtil.showShort(getActivity(), R.string.network_invalid);
			}
		}

		@Override
		public void onLoadMore() {
		}
	};

	IXListViewListener xListViewListener2 = new IXListViewListener() {

		@Override
		public void onRefresh() {
			if (NetUtil.isNetConnected(getActivity())) {
				isRefresh = true;
				requestSecListData();
				noNetView.setVisibility(View.GONE);
			} else {
				onLoad2();
				isRefresh = false;
				noNetView.setVisibility(View.VISIBLE);
				ToastUtil.showShort(getActivity(), "请连接网络");
			}
		}

		@Override
		public void onLoadMore() {
		}
	};

	/**
	 * 多语种列表项点击
	 */
	OnItemClickListener itemClick1 = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			if (mEntityList1 != null && mEntityList1.size() > 0) {
				try {
					// 跳到直播全屏界面播放
					CCTVMuliLaugItem entity = mEntityList1.get(position
							- mListView1.getHeaderViewsCount());


					if (StringUtil.isNullOrEmpty(entity.getId())) {
						ToastUtil.showShort(getActivity(), "视频地址不存在!");
						return;
					}
					Intent intent = new Intent(getActivity(),
							PlayLiveActivity.class);
					PlayLiveEntity playLiveEntity = new PlayLiveEntity(
							entity.getId(), entity.getTitle(),
							entity.getImage(), null, null,
							CollectTypeEnum.PD.value() + "",
							CollectPageSourceEnum.PDZB.value(), true);
					intent.putExtra("live", playLiveEntity);
					List<PlayLiveEntity> datas = changeListType(mEntityList1);
					//统计
					MobileAppTracker.trackEvent(entity.getTitle(), "列表", "CCTV", 0, entity.getId(), "视频观看",getActivity());
					MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
					Log.i("统计：","视频ID："+entity.getId());
					Log.i("统计: ","点击了"+"CCTV"+entity.getTitle()+"******"+entity.getId());
					intent.putExtra("listlive",
							datas != null ? ((Serializable) datas) : null);
					startActivity(intent);
				} catch (Exception e) {
					L.e(e.toString());
				}
			}
		}
	};

	private List<PlayLiveEntity> changeListType(
			List<CCTVMuliLaugItem> mEntityList) {
		if (mEntityList == null || mEntityList.size() == 0) {
			return null;
		}
		List<PlayLiveEntity> entityList = new ArrayList<PlayLiveEntity>();
		CCTVMuliLaugItem entity;
		PlayLiveEntity playLiveEntity;
		for (int i = 0; i < mEntityList.size(); i++) {
			entity = mEntityList.get(i);
			playLiveEntity = new PlayLiveEntity(entity.getId(),
					entity.getTitle(), entity.getImage(), null, null,
					CollectTypeEnum.PD.value() + "",
					CollectPageSourceEnum.PDZB.value(), true);
			entityList.add(playLiveEntity);
		}
		return entityList;
	}

	/**
	 * 央视名栏列表项点击
	 */
	OnItemClickListener itemClick2 = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			if (mEntityList2 != null && mEntityList2.size() > 0) {
				try {
					CCTVWellKnowItem entity = mEntityList2.get(position
							- mListView2.getHeaderViewsCount());

					Intent intent = new Intent(getActivity(),
							CCTVDetailActivity.class);
					intent.putExtra("id", entity.getId());
					intent.putExtra("title", entity.getTitle());
					intent.putExtra("image", entity.getImage());
					intent.putExtra("videoLength", entity.getVideoLength());
					//统计
					MobileAppTracker.trackEvent(entity.getTitle(), "列表", "CCTV", 0, entity.getId(), "视频观看",getActivity());
					MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
					Log.i("统计","视频ID："+entity.getId());
					Log.i("统计","点击了"+"CCTV 央"+entity.getTitle()+"******"+entity.getId());
					startActivity(intent);
				} catch (Exception e) {
				}
			}
		}
	};
	protected PopWindowUtils 推荐名栏视频集;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_cctv, container, false);
		mInflater = inflater;
		return view;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	private void dealBigImgEvent1(CCTVBigImgItem entity) {
		if ("1".equals(entity.getType())) {
			// 1为视频，进入“名栏点播全屏页”
			Intent intent = new Intent(getActivity(),
					PlayVodFullScreeActivity.class);
			intent.putExtra(PlayVodFullScreeActivity.PLAY_VOD_ID,
					entity.getId());
			getActivity().startActivity(intent);
		} else if ("2".equals(entity.getType())) {
			// 2为视频集，进入“央视名栏底层页”
			Intent intent = new Intent(getActivity(), CCTVDetailActivity.class);
			intent.putExtra("bigImgEntity", entity);
			intent.putExtra("source", 1);
			startActivity(intent);

		}
	}

	private void dealBigImgEvent(CCTVBigImgItem entity) {
		// "type": "1为直播 2为视频  3为视频集 4为专题 5为正文",
		// stype":"1为熊猫单视角直播 2为熊猫直播 3为直播中国单视角直播 4为直播中国直播 5为CCTV直播"
		String typeStr = entity.getType();
		if (!StringUtil.isNullOrEmpty(typeStr)) {
			int type = Integer.parseInt(typeStr);
			switch (type) {
			case 1:
				// 处理直播跳转
				dealLiveJump(entity);
				break;
			case 2:
				// 视频(点击进入所对应的视频全屏页)
				PlayVodEntity tEntity = new PlayVodEntity(
						CollectTypeEnum.SP.value() + "", entity.getPid(), null,
						entity.getUrl(), entity.getImage(), entity.getTitle(),
						null, JMPTypeEnum.VIDEO_VOD.value(), "");
				Intent i2 = new Intent(getActivity(),
						PlayVodFullScreeActivity.class);
				i2.putExtra("vid", tEntity);
				startActivity(i2);
				break;
			case 3:
				// 视频集(只支持名栏视频集收藏) (点击进入3.5.8央视名栏底层页)
				Intent i3 = new Intent(getActivity(), CCTVDetailActivity.class);
				i3.putExtra("id", entity.getVid());
				i3.putExtra("title", entity.getTitle());
				i3.putExtra("image", entity.getImage());
				i3.putExtra("type", entity.getType());
				startActivity(i3);
				break;
			case 4:
				// 跳转到专题页
				Intent i4 = new Intent(getActivity(),
						HomeSSubjectActivity.class);
				startActivity(i4);
				break;
			case 5:
				// 正文，跳转到图文底层页
				// 图文(点击进入3.6.2熊猫观察图文底层页)
				Intent i5 = new Intent(getActivity(),
						PandaEyeDetailActivity.class);
				i5.putExtra(PandaEyeDetailActivity.TYPE, PandaEyeDetailActivity.TYPE_ARTICLE);
				i5.putExtra("id", entity.getId());
				i5.putExtra("url", entity.getUrl());
				i5.putExtra("pic", entity.getImage());
				i5.putExtra("title", entity.getTitle());
				i5.putExtra("timeval", "");
				startActivity(i5);

				break;
			case 6:

				Intent i6 = new Intent(getActivity(),
						PandaEyeDetailActivity.class);
				i6.putExtra("url", entity.getUrl());
				i6.putExtra("title", entity.getTitle());
				i6.putExtra("pic", entity.getImage());
				i6.putExtra("id", entity.getId());
				i6.putExtra("timeval", "");
				// Log.e("eye",
				// topData.getUrl()+"title="+topData.getTitle()+"pic="+topData.getImage());

				startActivity(i6);

				break;
			}
		}
	}

	private void dealLiveJump(CCTVBigImgItem entity) {
		if (!StringUtil.isNullOrEmpty(entity.getStype())) {
			int sType = Integer.parseInt(entity.getStype());
			switch (sType) {
			case 1:
			case 3:
			case 5:
				if (StringUtil.isNullOrEmpty(entity.getPid())) {
					ToastUtil.showShort(getActivity(), "视频地址不存在!");
					return;
				}
				int pageSource = sType == 1 ? CollectPageSourceEnum.XMZB
						.value() : (sType == 3 ? CollectPageSourceEnum.ZBZG
						.value() : CollectPageSourceEnum.PDZB.value());
				// 1为熊猫单视角直播，点击进入该视角的“熊猫直播全屏页3.3.4
				// 3为直播中国单视角直播，点击进入该视角的直播中国全屏页3。4.4
				// 5为CCTV直播，点击进入“频道直播全屏页3.5.2”。
				Intent intent = new Intent(getActivity(),
						PlayLiveActivity.class);
				PlayLiveEntity playLiveEntity = new PlayLiveEntity(
						entity.getPid(), entity.getTitle(), entity.getImage(),
						null, null, CollectTypeEnum.PD.value() + "",
						pageSource, true);
				intent.putExtra("live", playLiveEntity);
				startActivity(intent);
				break;
			case 2:
				// 2为熊猫直播，点击进入该熊猫直播频道页3.3.1
				mRbLovePanda.performClick();
				break;
			case 4:
				// 4为直播中国直播，点击进入该“直播中国频道页3.4.1”。
				mRbLiveChina.performClick();
				break;
			}
		}
	}

	class OnClickListener implements View.OnClickListener {

		CCTVBigImgItem model;

		public OnClickListener(CCTVBigImgItem models) {
			model = models;
		}

		@Override
		public void onClick(View view) {

			CCTVBigImgItem entity = model;
			dealBigImgEvent(entity);

		}

	}

	// class OnClickListener onImgClickListener = new OnClickListener() {
	//
	// CCTVBigImgItem model;
	//
	// public OnClickListener(CCTVBigImgItem modelva){
	// model = modelva;
	// }
	//
	// @Override
	// public void onClick(View view) {
	// int position = Integer.parseInt(view.getTag() + "");
	// if (imgItems != null && imgItems.size() > position) {
	// CCTVBigImgItem entity = imgItems.get(position);
	// dealBigImgEvent(entity);
	// }
	// }
	// }

	// private void initViewpager() {
	// views.clear();
	// ImageView imageView;
	// CCTVBigImgItem item;
	//
	// ComonUtils.setScale16_9(getActivity(), viewPager);
	// for (int i = 0, j = imgItems.size(); i < j; i++) {
	// item = imgItems.get(i);
	// if (item == null) {
	// continue;
	// }
	// if (i == 0) {
	// // 初始化第一个图片的提示
	// String cueStr = "";
	// if (item != null && item.getTitle() != null) {
	// cueStr = item.getTitle().length() > 16 ? item.getTitle()
	// .substring(0, 16) + "..." : item.getTitle();
	// }
	// mCueView.setText(cueStr);
	// }
	// imageView = (ImageView) mInflater.inflate(
	// R.layout.cctv_wellknow_viewpager_iv, null);
	// // imageView.setScaleType(StringUtil.isNullOrEmpty(imgItems.get(i)
	// // .getImage()) ? ScaleType.FIT_XY : ScaleType.CENTER_CROP);
	// mImageLoader.displayImage(item.getImage(), imageView, mOptions);
	// views.add(imageView);
	// final CCTVBigImgItem fianlEntity = item;
	// imageView.setTag(String.valueOf(i));
	// }
	// if (bannerAdapter == null) {
	//
	// mPointView.setPointCount(imgItems.size());
	//
	// bannerAdapter = new CCTVViewPagerAdapter(views, getActivity(),
	// onImgClickListener);
	// bannerAdapter.setData(imgItems);
	// viewPager.setAdapter(bannerAdapter);
	// viewPager.startAutoScroll();
	//
	// } else {
	// bannerAdapter.setList(views);
	// }
	// mPointView.setCurrentIndex(0);
	// if (imgItems != null && imgItems.size() >= 0) {
	// mPointView.setVisibility(View.VISIBLE);
	// } else {
	// mPointView.setVisibility(View.GONE);
	// }
	// viewPager.setCurrentItem(0);
	// }

	private void initCarouselView() {
		// headView = (View) mInflater.inflate(R.layout.cctv_wellknow_lv_head,
		// null);
		// viewPager = (AutoScrollViewPager)
		// headView.findViewById(R.id.viewPager);
		// viewPager.setOnPageChangeListener(this);
		// viewPager.setInterval(INTERVAL);
		// viewPager.isCycle();
		// viewPager.startAutoScroll();
		//
		// mPointView = (PointView) headView.findViewById(R.id.cctv_pointview);
		// mCueView = (TextView) headView.findViewById(R.id.cue);

		// mListView2.addHeaderView(headView);
		convertView = LayoutInflater.from(getActivity()).inflate(
				R.layout.view_banner_viewpager, null);
		mListView2.addHeaderView(convertView);
	}

	private void initBigImg() {

		if (null == imgItems || imgItems.size() == 0) {
			return;
		}

		List<BigImg> bigImgs = new ArrayList<BigImg>();

		for (int i = 0; i < imgItems.size(); i++) {

			CCTVBigImgItem ptData = imgItems.get(i);
			if (null == ptData) {
				continue;
			} else {
				BigImg bImg = new BigImg();
				bImg.setId(ptData.getId());
				bImg.setImage(ptData.getImage());
				bImg.setOrder(Integer.parseInt(ptData.getOrder()));
				bImg.setPid(ptData.getPid());
				bImg.setStype(ptData.getStype());
				bImg.setTitle(ptData.getTitle());
				bImg.setType(ptData.getType());
				bImg.setUrl(ptData.getUrl());
				bImg.setVid(ptData.getVid());
				bigImgs.add(bImg);
			}
		}

		List<View> tViews = new ArrayList<View>();

		// HomeAdapterBigTwoImg tBigImg = (HomeAdapterBigTwoImg)
		// mDatas.get(position);
		int tBigImgSize = imgItems.size();

		AutoScrollViewPager tBigImgPager = ViewHolder.get(convertView,
				R.id.viewPager);

		PointView tPointView = ViewHolder.get(convertView, R.id.pointview);
		/*tBigImgPager.addOnPageChangeListener(new HomeViewPagerListener(
				tPointView));*/

		int tTitleWidth = 0;

		if (tBigImgSize == 1) {
			tTitleWidth = tPointView.setPointCount(0);
		} else {
			tTitleWidth = tPointView.setPointCount(tBigImgSize);
		}

		tBigImgPager.addOnPageChangeListener(new HomeViewPagerListener(
				tPointView));

		tBigImgPager.setAdapter(new PEyeAutoScrollAdapter(bigImgs,
				getActivity(), null, tTitleWidth, 2));
		tBigImgPager.startAutoScroll();

		tPointView.setCurrentIndex(0);
		tBigImgPager.setCurrentItem(0);

		// int tTitleWidth = tPointView.setPointCount(tBigImgSize);
		// if (tBigImgSize > 1 && tBigImgSize < 4) {
		// imgItems.addAll(imgItems);
		// }
		// tBigImgSize = imgItems.size();
		// for (int i = 0; i < tBigImgSize; i++) {
		//
		// View tBannerView = LayoutInflater.from(getActivity()).inflate(
		// R.layout.view_banner_viewpager_item, null);
		//
		// AutoUtils.autoSize(tBannerView);
		//
		// ImageView tIvBigImg = (ImageView) tBannerView
		// .findViewById(R.id.ivBigImg);
		// // tIvBigImg.setTag(position + "," + i);
		// tIvBigImg.setOnClickListener(new OnClickListener(imgItems.get(i)));
		// mImageLoader.displayImage(imgItems.get(i).getImage(), tIvBigImg,
		// mOptions);
		//
		// TextView tTvBigImgTitle = (TextView) tBannerView
		// .findViewById(R.id.tvBigImgTitle);
		// tTvBigImgTitle.setText(imgItems.get(i).getTitle());
		// tTvBigImgTitle.setWidth(tTitleWidth);
		//
		// tViews.add(tBannerView);
		// }
		//
		// BannerAdapter tBannerAdapter = new BannerAdapter(tViews);
		// tBigImgPager.setAdapter(tBannerAdapter);
		// tBigImgPager.startAutoScroll();
		//
		// tPointView.setCurrentIndex(0);
		// tPointView.forceLayout();
		// tBigImgPager.setCurrentItem(tBigImgPager.getCurrentItem());
	}

	/**
	 * 从服务器获取tab数据
	 */
	private void requestTabData() {

		SharePreferenceUtil tPreferenceUtil = new SharePreferenceUtil(
				getActivity());
		String tUrl = tPreferenceUtil.getWebAddress(WebAddressEnum.WEB_CCTV
				.toString());

		if (tUrl == null || tUrl.equals("")) {

			Toast.makeText(getActivity(), R.string.load_fail, Toast.LENGTH_SHORT).show();
		} else {

			mHandler1.getHttpJson(tUrl, CCTVPageData.class, TAB_DATA);
		}
	}

	/**
	 * 设置tab页签的文本
	 */
	private void setTabText() {
		for (int i = 0; i < tag_id.length; i++) {
			View childView = tabHost.getTabWidget().getChildAt(i);
			if (childView != null) {
				 tvTab = (TextView) childView
						.findViewById(R.id.tv_title);
				tvTab.setText(tab_text[i]);
			}

		}
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
	 * 设置tab相关数据(tab对应的activity设置不可见，这里只需要tab的样式)
	 */
	private void initTab() {
		Intent intent;
		RelativeLayout tabIndicator;
		View bottomLine, bottomLineGrey;
		for (int i = 0; i < tag_id.length; i++) {
			intent = new Intent(getActivity(), EmptyActivity.class);
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
		requestTabData();
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
					if (NetUtil.isNetConnected(getActivity())) {
						showLoadingDialog();
						// 对加载轮播图所要使用到的ImageLoader进行初始化
						// 第一次切换到第二个tab，向服务器请求列表数据，之后切换不再请求，手动刷新
						requestSecListData();
						isInit = false;
						noNetView.setVisibility(View.GONE);
					} else {
						noNetView.setVisibility(View.VISIBLE);
						ToastUtil.showShort(getActivity(), R.string.network_invalid);
					}
				}
			}

		}
	};

	/**
	 * 对加载轮播图所要使用到的ImageLoader进行初始化
	 */


	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		mPointView.setCurrentIndex(position);
		if (imgItems != null && imgItems.size() > position) {
			String cueStr = "";
			if (imgItems.get(position) != null
					&& imgItems.get(position).getTitle() != null) {
				String _title = imgItems.get(position).getTitle();
				cueStr = _title.length() > 16 ? _title.substring(0, 16) + "..."
						: _title;
			}
			mCueView.setText(cueStr);
		}

	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivNoNet:
			if (NetUtil.isNetConnected(getActivity())) {
				noNetView.setVisibility(View.GONE);
				if (isInitNoNet) {
					dealPageData();
					isInitNoNet = false;
				} else {
					showLoadingDialog();
					if (tabHost.getCurrentTab() == 0) {
						// 刷新第一个tab
						requestFirListData();

					} else {
						if (isInit) {
							// 对加载轮播图所要使用到的ImageLoader进行初始化
							// 第一次切换到第二个tab，向服务器请求列表数据，之后切换不再请求，手动刷新
							requestSecListData();
							isInit = false;
						} else {
							// 刷新第二个tab
							requestSecListData();
						}
					}
				}
				// dealPageData();
			}
			break;
		}
	}
}
