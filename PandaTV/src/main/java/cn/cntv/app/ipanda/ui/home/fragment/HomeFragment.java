package cn.cntv.app.ipanda.ui.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.WebAddressEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.ui.BaseFragment;
import cn.cntv.app.ipanda.ui.home.adapter.HomeAdapter;
import cn.cntv.app.ipanda.ui.home.entity.AdapterData;
import cn.cntv.app.ipanda.ui.home.entity.GroupData;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterArea;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterBigImg;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterChinaLive;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterLive;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterPandaLive;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterVideoList1;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterVideoList2;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterWallLive;
import cn.cntv.app.ipanda.ui.home.entity.HomeData;
import cn.cntv.app.ipanda.ui.home.entity.HomeDataInfo;
import cn.cntv.app.ipanda.ui.home.entity.HomeHotLiveList;
import cn.cntv.app.ipanda.ui.home.entity.HomeLive2;
import cn.cntv.app.ipanda.ui.home.entity.HomeLiveList;
import cn.cntv.app.ipanda.ui.home.entity.HomePandaEyeList;
import cn.cntv.app.ipanda.ui.home.entity.HomeStyle1;
import cn.cntv.app.ipanda.ui.home.entity.HomeStyle2;
import cn.cntv.app.ipanda.ui.home.entity.HomeStyleData;
import cn.cntv.app.ipanda.ui.home.entity.HomeStyleDataInfo;
import cn.cntv.app.ipanda.ui.home.entity.HomeVideoList;
import cn.cntv.app.ipanda.ui.home.entity.Interaction;
import cn.cntv.app.ipanda.ui.home.entity.InteractionOne;
import cn.cntv.app.ipanda.ui.home.entity.InteractionTwo;
import cn.cntv.app.ipanda.utils.SharePreferenceUtil;
import cn.cntv.app.ipanda.utils.TimeHelper;
import cn.cntv.app.ipanda.utils.ToastUtil;
import cn.cntv.app.ipanda.xlistview.XListView;

/**
 * @ClassName: HomeFragment
 * @author Xiao JinLai
 * @Date Dec 24, 2015 2:28:52 PM
 * @Description：Home page
 */
public class HomeFragment extends BaseFragment implements XListView.IXListViewListener,
		OnClickListener {

	private static final int HOME_DATA = 0;
	private static final int HOME_DATA_MORE1 = 1;
	private static final int HOME_DATA_MORE2 = 2;
	private static final int PANDAEYE = 3;
	private static final int CCTV = 4;

	private XListView mListView;
	private RelativeLayout mIvNoNet;
	private HomeAdapter mAdapter;
	private ArrayList<AdapterData> mDatas;

	private boolean mBolRefersh;

	private String mStyleTitle1;
	private String mStyleTitle2;

	private HomeData mHomeData;

	private XjlHandler<Object> mHandler = new XjlHandler<Object>(
			new HandlerListener() {

				@Override
				public void handlerMessage(HandlerMessage msg) {

					switch (msg.what) {
					case Integer.MAX_VALUE:

						if (msg.whatTag == PANDAEYE) {

							handlerPandaEyeRemainData(mHomeData);
						} else if (msg.whatTag == CCTV) {

							handlerCCTVRemainData(mHomeData);
						} else if (msg.whatTag == HOME_DATA_MORE1) {

							handlerStyle1RemainData(mHomeData);
						} else if (msg.whatTag == HOME_DATA) {

							dataError();
						}

						break;

					case HOME_DATA:

						HomeDataInfo tHomeDataInfo = (HomeDataInfo) msg.obj;

						if (tHomeDataInfo == null
								|| tHomeDataInfo.getData() == null) {

							mListView.stopRefresh();
						} else {

							mListView.stopRefresh();

							mHomeData = tHomeDataInfo.getData();

							handlerHomeData(mHomeData);
						}
						break;

					case PANDAEYE:

						handlerPandaEyeList((HomePandaEyeList) msg.obj);
						handlerPandaEyeRemainData(mHomeData);

						break;
					case CCTV:

						handlerCCTVList((HomeStyleDataInfo) msg.obj);
						handlerCCTVRemainData(mHomeData);

						break;

					case HOME_DATA_MORE1:

						handlerStyleData1((HomeStyleDataInfo) msg.obj);
						handlerStyle1RemainData(mHomeData);
						break;

					case HOME_DATA_MORE2:

						handlerStyleData2((HomeStyleDataInfo) msg.obj);
						initAdapterData();
						break;
					}
				}
			});

	private void handlerHomeData(HomeData homeData) {

		if (homeData == null) {

			return;
		}

		mDatas = new ArrayList<AdapterData>();

		if (homeData.getBigImg() != null && !homeData.getBigImg().isEmpty()) {

//			mDatas.add(new GroupData());

			// add bigimg
			mDatas.add(new HomeAdapterBigImg(homeData.getBigImg()));
		}

		if (homeData.getArea() != null) {

			if (homeData.getArea().getListscroll() != null
					&& !homeData.getArea().getListscroll().isEmpty()) {

				mDatas.add(new GroupData(homeData.getArea().getTitle(),
						homeData.getArea().getImage(), homeData.getArea()
								.getUrl()));

				mDatas.add(new HomeAdapterArea(homeData.getArea()
						.getListscroll()));
			}

			if (homeData.getArea().getListh() != null
					&& !homeData.getArea().getListh().isEmpty()) {

				mDatas.add(new GroupData());

				// add video list 1
				int tVideoListSize1 = homeData.getArea().getListh().size();

				for (int i = 0; i < tVideoListSize1; i += 2) {

					List<HomeVideoList> tAddList = new ArrayList<HomeVideoList>();

					tAddList.add(homeData.getArea().getListh().get(i));

					int tTwo = i + 1;

					if (tTwo < tVideoListSize1) {

						tAddList.add(homeData.getArea().getListh().get((i + 1)));
					}

					HomeAdapterVideoList1 tHomeAdapterVideoList1 = new HomeAdapterVideoList1(
							tAddList);

					mDatas.add(tHomeAdapterVideoList1);
				}
			}

			if (homeData.getArea().getLists() != null
					&& !homeData.getArea().getLists().isEmpty()) {

				// add null group
				mDatas.add(new GroupData());

				// add video list 2
				int tVideoListSize2 = homeData.getArea().getLists().size();

				for (int i = 0; i < tVideoListSize2; i++) {

					mDatas.add(new HomeAdapterVideoList2(homeData.getArea()
							.getLists().get(i)));
				}
			}

			if (!homeData.getArea().getTopiclist().isEmpty()) {

				// add special subject
				mDatas.add(homeData.getArea().getTopiclist().get(0));
			}
		}

		if (homeData.getPandaeye() != null) {

			// add null group
			mDatas.add(new GroupData(homeData.getPandaeye().getTitle()));

			// add panda eye
			mDatas.add(homeData.getPandaeye());

			if (homeData.getPandaeye().getPandaeyelist() == null
					|| homeData.getPandaeye().getPandaeyelist().equals("")) {

				handlerPandaEyeRemainData(homeData);
			} else {

				mHandler.getHttpJson(homeData.getPandaeye().getPandaeyelist(),
						HomePandaEyeList.class, PANDAEYE);
			}
		}

	}

	private void handlerPandaEyeList(HomePandaEyeList pandaEye2) {

		if (pandaEye2 == null || pandaEye2.getList() == null
				|| pandaEye2.getList().isEmpty()) {

			return;
		}

		int tSize = pandaEye2.getList().size();

		for (int i = 0; i < tSize; i++) {

			mDatas.add(pandaEye2.getList().get(i));
		}
	}

	private void handlerPandaEyeRemainData(HomeData homeData) {

		if (homeData == null) {

			return;
		}

		if (homeData.getPandalive() != null
				&& homeData.getPandalive().getList() != null
				&& !homeData.getPandalive().getList().isEmpty()) {

			// add hot live group
			mDatas.add(new GroupData(homeData.getPandalive().getTitle()));

			// add hot live
			int tPandaLiveSize = homeData.getPandalive().getList().size();

			for (int i = 0; i < tPandaLiveSize; i += 3) {

				List<HomeHotLiveList> tAddHotlives = new ArrayList<HomeHotLiveList>();

				tAddHotlives.add(homeData.getPandalive().getList().get(i));

				int tTwo = i + 1;

				if (tTwo < tPandaLiveSize) {

					tAddHotlives.add(homeData.getPandalive().getList()
							.get(tTwo));

					int tThree = tTwo + 1;

					if (tThree < tPandaLiveSize) {

						tAddHotlives.add(homeData.getPandalive().getList()
								.get(tThree));
					}
				}

				mDatas.add(new HomeAdapterPandaLive(tAddHotlives));
			}
		}

		if (homeData.getWalllive() != null
				&& homeData.getWalllive().getList() != null
				&& !homeData.getWalllive().getList().isEmpty()) {

			// add hot live group
			mDatas.add(new GroupData(homeData.getWalllive().getTitle()));

			// add hot live
			int tWallLiveSize = homeData.getWalllive().getList().size();

			for (int i = 0; i < tWallLiveSize; i += 3) {

				List<HomeHotLiveList> tAddWalllives = new ArrayList<HomeHotLiveList>();

				tAddWalllives.add(homeData.getWalllive().getList().get(i));

				int tTwo = i + 1;

				if (tTwo < tWallLiveSize) {

					tAddWalllives.add(homeData.getWalllive().getList()
							.get(tTwo));

					int tThree = tTwo + 1;

					if (tThree < tWallLiveSize) {

						tAddWalllives.add(homeData.getWalllive().getList()
								.get(tThree));
					}
				}

				mDatas.add(new HomeAdapterWallLive(tAddWalllives));
			}
		}

		if (homeData.getChinalive() != null
				&& homeData.getChinalive().getList() != null
				&& !homeData.getChinalive().getList().isEmpty()) {

			// add hot live group
			mDatas.add(new GroupData(homeData.getChinalive().getTitle()));

			// add hot live
			int tChinaLiveSize = homeData.getChinalive().getList().size();

			for (int i = 0; i < tChinaLiveSize; i += 3) {

				List<HomeHotLiveList> tAddHotlives = new ArrayList<HomeHotLiveList>();

				tAddHotlives.add(homeData.getChinalive().getList().get(i));

				int tTwo = i + 1;

				if (tTwo < tChinaLiveSize) {

					tAddHotlives.add(homeData.getChinalive().getList()
							.get(tTwo));

					int tThree = tTwo + 1;

					if (tThree < tChinaLiveSize) {

						tAddHotlives.add(homeData.getChinalive().getList()
								.get(tThree));
					}
				}

				mDatas.add(new HomeAdapterChinaLive(tAddHotlives));
			}
		}

		if (homeData.getInteractive() != null) {

			// add interaction group
			mDatas.add(new GroupData(homeData.getInteractive().getTitle()));

			if (homeData.getInteractive().getInteractiveone() != null
					&& !homeData.getInteractive().getInteractiveone().isEmpty()) {

				// add interaction one
				mDatas.add(new InteractionOne(homeData.getInteractive()
						.getInteractiveone().get(0)));
			}

			if (homeData.getInteractive().getInteractivetwo() != null
					&& homeData.getInteractive().getInteractivetwo().size() > 0) {

				// add interaction two
				List<Interaction> tInteractions = new ArrayList<Interaction>();
				tInteractions.add(homeData.getInteractive().getInteractivetwo()
						.get(0));

				if (homeData.getInteractive().getInteractivetwo().size() > 1) {

					tInteractions.add(homeData.getInteractive()
							.getInteractivetwo().get(1));
				}

				mDatas.add(new InteractionTwo(tInteractions));
			}
		}

		if (homeData.getCctv() != null) {

			mDatas.add(new GroupData("CCTV"));

			if (homeData.getCctv().getListlive() != null
					&& !homeData.getCctv().getListlive().isEmpty()) {

				// add live
				int tLiveSize = homeData.getCctv().getListlive().size();

				List<HomeLiveList> tLiveLists = new ArrayList<HomeLiveList>();

				for (int i = 0; i < tLiveSize; i++) {

					tLiveLists.add(homeData.getCctv().getListlive().get(i));

					if (((i + 1) % 5) == 0) {

						mDatas.add(new HomeAdapterLive(tLiveLists));
						tLiveLists = new ArrayList<HomeLiveList>();
					}
				}
			}

			String tUrlList = homeData.getCctv().getListurl();

			if (tUrlList == null || tUrlList.equals("")) {

				handlerCCTVRemainData(homeData);
			} else {

				mHandler.getHttpJson(tUrlList, HomeStyleDataInfo.class, CCTV);
			}
		}

	}

	private void handlerCCTVList(HomeStyleDataInfo obj) {

		if (obj == null || obj.getList() == null || obj.getList().isEmpty()) {

			return;
		}

		int tStyleSize = obj.getList().size();

		for (int i = 0; i < tStyleSize; i += 2) {

			List<HomeStyleData> tAddDatas = new ArrayList<HomeStyleData>();
			tAddDatas.add(obj.getList().get(i));

			int tTwo = i + 1;

			if (tTwo < tStyleSize) {

				tAddDatas.add(obj.getList().get(tTwo));
			}

			mDatas.add(new HomeLive2(tAddDatas));
		}
	}

	/**
	 * 处理CCTV剩下的数据
	 * 
	 * @param homeData
	 */
	private void handlerCCTVRemainData(HomeData homeData) {

		if (homeData == null || homeData.getList() == null
				|| homeData.getList().isEmpty()) {

			initAdapterData();
			return;
		}

		String tUrl = null;

		int tSize = homeData.getList().size();

		for (int i = 0; i < tSize; i++) {

			if (homeData.getList().get(i).getType() == 1) {

				tUrl = homeData.getList().get(i).getListUrl();
				mStyleTitle1 = homeData.getList().get(i).getTitle();
			}
		}

		if (tUrl == null || tUrl.equals("")) {

			handlerStyle1RemainData(homeData);
		} else {

			mHandler.getHttpJson(tUrl, HomeStyleDataInfo.class, HOME_DATA_MORE1);
		}
	}

	/**
	 * 处理样式1剩下的数据
	 * 
	 * @param homeData
	 */
	private void handlerStyle1RemainData(HomeData homeData) {

		if (homeData == null || homeData.getList() == null
				|| homeData.getList().isEmpty()) {

			initAdapterData();
			return;
		}

		String tUrl = null;

		int tSize = homeData.getList().size();

		for (int i = 0; i < tSize; i++) {

			if (homeData.getList().get(i).getType() == 2) {

				tUrl = homeData.getList().get(i).getListUrl();
				mStyleTitle2 = homeData.getList().get(i).getTitle();
			}
		}

		if (tUrl == null || tUrl.equals("")) {

			handlerStyle1RemainData(homeData);
		} else {

			mHandler.getHttpJson(tUrl, HomeStyleDataInfo.class, HOME_DATA_MORE2);
		}
	}

	/**
	 * 处理样式1数据
	 * 
	 * @param obj
	 */
	private void handlerStyleData1(HomeStyleDataInfo obj) {

		if (obj == null) {

			return;
		}

		boolean tIsData = true;
		boolean tIsPage = true;

		if (obj.getList() != null && !obj.getList().isEmpty()) {

			tIsPage = false;
			tIsData = false;
		} else if (obj.getPagelist() != null && !obj.getPagelist().isEmpty()) {

			tIsData = false;
		}

		if (tIsData) {

			return;
		}

		int tStyleSize;

		if (tIsPage) {

			tStyleSize = obj.getPagelist().size();
		} else {

			tStyleSize = obj.getList().size();
		}

		mDatas.add(new GroupData(mStyleTitle1));

		if (tIsPage) {

			for (int i = 0; i < tStyleSize; i += 2) {

				List<HomeStyleData> tAddDatas = new ArrayList<HomeStyleData>();
				tAddDatas.add(obj.getPagelist().get(i));

				int tTwo = i + 1;

				if (tTwo < tStyleSize) {

					tAddDatas.add(obj.getPagelist().get(tTwo));
				}

				mDatas.add(new HomeStyle1(tAddDatas));
			}
		} else {

			for (int i = 0; i < tStyleSize; i += 2) {

				List<HomeStyleData> tAddDatas = new ArrayList<HomeStyleData>();
				tAddDatas.add(obj.getList().get(i));

				int tTwo = i + 1;

				if (tTwo < tStyleSize) {

					tAddDatas.add(obj.getList().get(tTwo));
				}

				mDatas.add(new HomeStyle1(tAddDatas));
			}
		}

	}

	/**
	 * 处理样式 2 数据
	 * 
	 * @param obj
	 */
	private void handlerStyleData2(HomeStyleDataInfo obj) {

		if (obj == null) {

			return;
		}

		boolean tIsData = true;
		boolean tIsPage = true;

		if (obj.getList() != null && !obj.getList().isEmpty()) {

			tIsPage = false;
			tIsData = false;
		} else if (obj.getPagelist() != null && !obj.getPagelist().isEmpty()) {

			tIsData = false;
		}

		if (tIsData) {

			return;
		}

		int tStyleSize;

		if (tIsPage) {

			tStyleSize = obj.getPagelist().size();
		} else {

			tStyleSize = obj.getList().size();
		}

		mDatas.add(new GroupData(mStyleTitle2));

		if (tIsPage) {

			for (int i = 0; i < tStyleSize; i++) {

				mDatas.add(new HomeStyle2(obj.getPagelist().get(i)));
			}
		} else {

			for (int i = 0; i < tStyleSize; i++) {

				mDatas.add(new HomeStyle2(obj.getList().get(i)));
			}
		}

		mListView.stopLoadMore();

		if (mAdapter != null) {

			mAdapter.notifyDataSetChanged();
		}
	}

	private void initAdapterData() {

//		if (mBolRefersh && mAdapter != null) {
//
//			mAdapter.updateData(mDatas);
//			mBolRefersh = false;
//			mListView.setPullRefreshEnable(true);
//		} else {

			mAdapter = new HomeAdapter(getActivity(), mDatas);
			mListView.setAdapter(mAdapter);
//		}
		dismissLoadDialog();
	}

	/**
	 * 实例化方法，如果传入参数必须实现setArguments
	 * 
	 * @return
	 */
	public static HomeFragment newInstance() {
		HomeFragment fragment = new HomeFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_home, container, false);

		mListView = (XListView) view.findViewById(R.id.xlvHome);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setRefreshTime(TimeHelper.getCurrentData());
		mListView.setXListViewListener(this);

		mIvNoNet = (RelativeLayout) view.findViewById(R.id.ivHomeNoNet);
		mIvNoNet.setOnClickListener(this);

		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initData();
	}

	private void initData() {

		showLoadingDialog();

		if (isConnected()) {

			mIvNoNet.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);

			SharePreferenceUtil tPreferenceUtil = new SharePreferenceUtil(
					getActivity());
			String tUrl = tPreferenceUtil.getWebAddress(WebAddressEnum.WEB_HOME
					.toString());

			if (tUrl == null || tUrl.equals("")) {

				dataError();
			} else {

				mHandler.getHttpJson(tUrl, HomeDataInfo.class, HOME_DATA);
			}

		} else {

			ToastUtil.showShort(getActivity(), R.string.network_invalid);
			dataError();
		}
	}

	private void dataError() {

		mIvNoNet.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
		dismissLoadDialog();
	}

	@Override
	public void onRefresh() {
		mListView.setRefreshTime(TimeHelper.getCurrentData());
		mBolRefersh = true;
		initData();
	}

	@Override
	public void onLoadMore() {

	}


	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ivHomeNoNet:

			initData();
			break;
		}
	}
}
