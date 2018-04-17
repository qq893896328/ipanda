package cn.cntv.app.ipanda.ui.livechina.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.gridsum.mobiledissector.MobileAppTracker;

import java.util.LinkedList;
import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.WebAddressEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.db.DBInterface;
import cn.cntv.app.ipanda.db.entity.LiveChinaChannelEntity;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.ui.BaseFragment;
import cn.cntv.app.ipanda.ui.livechina.activity.LiveChinaSelectChannelActivity;
import cn.cntv.app.ipanda.ui.livechina.entity.LiveChinaAllTablist;
import cn.cntv.app.ipanda.ui.livechina.entity.LiveChinaTabItem;
import cn.cntv.app.ipanda.utils.SharePreferenceUtil;

/**
 * 直播中国
 * 
 * @author wuguicheng
 */
public class LiveChinaFragment extends BaseFragment implements OnClickListener {

	// private static final String TAB_URL =
	// "http://www.ipanda.com/kehuduan/PAGE14501775094142282/index.json";
	private static String TAB_URL;
	private static final int HTTP_GET_TAB_DATA = 1;
	private static final int FOR_ACTIVIY_RESULT = 2;


	private FrameLayout mainFragmentLayout;
	private ImageView liveChannelImg;

	// 包括未定义的频道和订阅的频道
	private LiveChinaAllTablist allTablist;

	private DBInterface dbInterface = DBInterface.getInstance();

	LiveChinaTabView liveChinaTabView;
	// 无网络
	View notNetImg;

	private XjlHandler<LiveChinaAllTablist> mHandler = new XjlHandler<LiveChinaAllTablist>(
			new HandlerListener() {

				@Override
				public void handlerMessage(HandlerMessage msg) {
					switch (msg.what) {
					case HTTP_GET_TAB_DATA:
						if (getActivity() == null) {
							return;
						}

						if (notNetImg.getVisibility() == View.VISIBLE) {
							notNetImg.setVisibility(View.GONE);
						}

						allTablist = (LiveChinaAllTablist) msg.obj;

						if (allTablist != null && allTablist.alllist.size() > 0
								&& allTablist.tablist.size() > 0) {

							try {
								// 查询数据库
								List<LiveChinaChannelEntity> findAll = dbInterface
										.loadAllLiveChinaChannel();
								if (findAll != null && findAll.size() > 0) {
									// 已经存到数据库

									// if(findAll.size()!=0){
									// for (int i = 0; i < findAll.size(); i++)
									// {
									// List<LiveChinaTabItem> allList =
									// allTablist.alllist;
									// dbManager.delete(LiveChinaTabItemDb.class,
									// WhereBuilder.b("id", " = ", "2"));
									// }
									// }

									allTablist.tablist = new LinkedList<LiveChinaTabItem>();
									for (int i = 0; i < findAll.size(); i++) {
										LiveChinaChannelEntity liveChinaTabItemDb = findAll
												.get(i);
										LiveChinaTabItem liveChinaTabItem = new LiveChinaTabItem();
										liveChinaTabItem.order = liveChinaTabItemDb
												.getOrder();
										liveChinaTabItem.title = liveChinaTabItemDb
												.getTitle();
										liveChinaTabItem.type = liveChinaTabItemDb
												.getType();
										liveChinaTabItem.url = liveChinaTabItemDb
												.getUrl();
										allTablist.tablist
												.add(liveChinaTabItem);
									}
									liveChinaTabView = new LiveChinaTabView(
											LiveChinaFragment.this, allTablist);
									liveChinaTabView.initData();
									mainFragmentLayout.addView(
											liveChinaTabView.getContentView(),
											0);
									liveChannelImg = (ImageView) (liveChinaTabView
											.getContentView())
											.findViewById(R.id.live_china_add_channel);
									// loding_progress.setVisibility(View.GONE);
									liveChannelImg
											.setOnClickListener(LiveChinaFragment.this);
								} else {
									// 第一次安装软件 未存到数据库内
									liveChinaTabView = new LiveChinaTabView(
											LiveChinaFragment.this, allTablist);
									liveChinaTabView.initData();
									mainFragmentLayout.addView(
											liveChinaTabView.getContentView(),
											0);
									liveChannelImg = (ImageView) (liveChinaTabView
											.getContentView())
											.findViewById(R.id.live_china_add_channel);
									// loding_progress.setVisibility(View.GONE);
									liveChannelImg
											.setOnClickListener(LiveChinaFragment.this);

									for (int i = 0; i < allTablist.tablist
											.size(); i++) {
										LiveChinaTabItem liveChinaTabItem = allTablist.tablist
												.get(i);

										LiveChinaChannelEntity liveChinaTabItemDb = new LiveChinaChannelEntity();
										// liveChinaTabItemDb.setOrder(liveChinaTabItem.order);
										liveChinaTabItemDb.setOrder(Integer
												.toString(i + 1));
										liveChinaTabItemDb
												.setTitle(liveChinaTabItem.title);
										liveChinaTabItemDb
												.setType(liveChinaTabItem.type);
										liveChinaTabItemDb
												.setUrl(liveChinaTabItem.url);

										// 增加行数据
										dbInterface.insertOrUpdate(liveChinaTabItemDb);
									}
								}

							} catch (Exception e) {
								e.printStackTrace();
							}

						}
						break;
					}

				}

			});

	public LiveChinaFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 *
	 * @return A new instance of fragment LiveChinaFragment.
	 */
	public static LiveChinaFragment newInstance() {
		LiveChinaFragment fragment = new LiveChinaFragment();
		return fragment;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_live_china, container,
				false);
		initView(view);
		initData();
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void initView(View view) {
		mainFragmentLayout = (FrameLayout) view
				.findViewById(R.id.live_china_main_fragment);
		notNetImg = view.findViewById(R.id.live_china_item_not_net);
		notNetImg.setOnClickListener(this);
	}

	private void initData() {

		SharePreferenceUtil tPreferenceUtil = new SharePreferenceUtil(
				getActivity());
		String tUrl = tPreferenceUtil.getWebAddress(WebAddressEnum.WEB_LIVE_CHINA
				.toString());

		if (tUrl == null || tUrl.equals("")) {

			Toast.makeText(getActivity(), R.string.load_fail, Toast.LENGTH_SHORT).show();
		} else {

			TAB_URL = tUrl;
		}

		if (isConnected()) {
			// 获取tab标签
			showLoadingDialog();
			getInitData();
		} else {
			notNetImg.setVisibility(View.VISIBLE);
		}
	}

	private void getInitData() {
		mHandler.getHttpJson(TAB_URL, LiveChinaAllTablist.class,
				HTTP_GET_TAB_DATA);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	public void reloadTab(LiveChinaAllTablist allTablist) {
		this.allTablist.tablist = allTablist.tablist;
		mainFragmentLayout.removeViewAt(0);

		liveChinaTabView = new LiveChinaTabView(LiveChinaFragment.this,
				allTablist);
		liveChinaTabView.initData();
		mainFragmentLayout.addView(liveChinaTabView.getContentView(), 0);
		liveChannelImg = (ImageView) (liveChinaTabView.getContentView())
				.findViewById(R.id.live_china_add_channel);
		// loding_progress.setVisibility(View.GONE);
		liveChannelImg.setOnClickListener(LiveChinaFragment.this);

		try {
			// 删除表格数据
			dbInterface.deleteAllLiveChinaChannel();
			List<LiveChinaTabItem> tablist = allTablist.tablist;
			for (int i = 0; i < tablist.size(); i++) {
				LiveChinaTabItem liveChinaTabItem = tablist.get(i);

				LiveChinaChannelEntity liveChinaChannelEntity = new LiveChinaChannelEntity();
				// liveChinaTabItemDb.setOrder(liveChinaTabItem.order);
				liveChinaChannelEntity.setOrder(Integer.toString(i + 1));
				liveChinaChannelEntity.setTitle(liveChinaTabItem.title);
				liveChinaChannelEntity.setType(liveChinaTabItem.type);
				liveChinaChannelEntity.setUrl(liveChinaTabItem.url);

				// 增加行数据
				dbInterface.insertOrUpdate(liveChinaChannelEntity);
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.live_china_add_channel:
			destoryPlay();
			//统计
			MobileAppTracker.trackEvent("更多", "", "直播中国", 0, null, "",getContext());
			MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
			Log.e("统计","事件名称:"+"更多"+"***事件类别:"+"直播中国导航栏"+"***事件标签:"+"直播中国"+"***类型:"+"null");
			Intent intent = new Intent(getActivity(),
					LiveChinaSelectChannelActivity.class);

			// 重新生成一个对象
			LiveChinaAllTablist liveChinaAllTablist = new LiveChinaAllTablist();
			liveChinaAllTablist.alllist = new LinkedList<LiveChinaTabItem>();
			liveChinaAllTablist.tablist = new LinkedList<LiveChinaTabItem>();
			liveChinaAllTablist.alllist.addAll(allTablist.alllist);
			liveChinaAllTablist.tablist.addAll(allTablist.tablist);

			intent.putExtra("LiveChinaAllTablist", liveChinaAllTablist);
			startActivityForResult(intent, FOR_ACTIVIY_RESULT);
			break;

		case R.id.live_china_item_not_net:
			if (isConnected()) {
				showLoadingDialog();
				getInitData();
			}
			break;

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == FOR_ACTIVIY_RESULT) {
			if (resultCode == Activity.RESULT_OK) {
				LiveChinaAllTablist liveChinaAllTablist = (LiveChinaAllTablist) data
						.getExtras()
						.getSerializable(
								LiveChinaSelectChannelActivity.RESUTL_TABLIST_STRING);
				reloadTab(liveChinaAllTablist);
			}
		}
	}

	public void setChinaTabViewViewsible(int visible) {
		liveChinaTabView.getContentView()
				.findViewById(R.id.live_china_tab_layout)
				.setVisibility(visible);
	}

	public void destoryPlay() {
		if (liveChinaTabView != null) {
			liveChinaTabView.destoryPLay();
		}
	}

	/**
	 * 处理返回键 如视频切换半屏
	 */
	public boolean onkeyDownBack() {

		if (liveChinaTabView != null) {
			return liveChinaTabView.onKeyDownBack();
		}
		return false;
	}

}
