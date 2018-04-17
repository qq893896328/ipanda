package cn.cntv.app.ipanda.ui.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gridsum.mobiledissector.MobileAppTracker;

import java.util.ArrayList;
import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.ui.home.adapter.SSubjectAdapter;
import cn.cntv.app.ipanda.ui.home.entity.AdapterData;
import cn.cntv.app.ipanda.ui.home.entity.GroupData;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterBigImg;
import cn.cntv.app.ipanda.ui.home.entity.Interaction;
import cn.cntv.app.ipanda.ui.home.entity.InteractionOne;
import cn.cntv.app.ipanda.ui.home.entity.InteractionTwo;
import cn.cntv.app.ipanda.ui.home.entity.Live;
import cn.cntv.app.ipanda.ui.home.entity.SSLiveVideoList;
import cn.cntv.app.ipanda.ui.home.entity.SSVideoList;
import cn.cntv.app.ipanda.ui.home.entity.SSVideoList1;
import cn.cntv.app.ipanda.ui.home.entity.SSVideoList2;
import cn.cntv.app.ipanda.ui.home.entity.SSVideoList3;
import cn.cntv.app.ipanda.ui.home.entity.SSVideoList4;
import cn.cntv.app.ipanda.ui.home.entity.SSubjectAdapterLVList;
import cn.cntv.app.ipanda.ui.home.entity.SSubjectBanner;
import cn.cntv.app.ipanda.ui.home.entity.SSubjectData;
import cn.cntv.app.ipanda.ui.home.entity.SSubjectDataInfo;
import cn.cntv.app.ipanda.ui.home.entity.SSubjectVote;
import cn.cntv.app.ipanda.ui.play.PlayVodController;
import cn.cntv.app.ipanda.ui.play.ShareController;
import cn.cntv.app.ipanda.xlistview.XListView;

/**
 * @ClassName: HomeSSubjectActivity
 * @author Xiao JinLai
 * @Date Dec 31, 2015 5:10:55 PM
 * @Description：Home special subject template class
 */
public class HomeSSubjectActivity extends BaseActivity implements
		XListView.IXListViewListener, OnClickListener {

	private static final int SSUBJECT_DATA = 0;

	private XListView mListView;
	private ImageView mIvSSubjectNoNet;
	private SSubjectAdapter mAdapter;
	private ArrayList<AdapterData> mDatas;
	private String shareTitle = "";

	private boolean mBolRefersh;
	private PlayVodController player;


	private XjlHandler<Object> mHandler = new XjlHandler<Object>(
			new HandlerListener() {

				@Override
				public void handlerMessage(HandlerMessage msg) {

					switch (msg.what) {
					case Integer.MAX_VALUE:

						dataError();
						break;

					case SSUBJECT_DATA:

						SSubjectDataInfo tDataInfo = (SSubjectDataInfo) msg.obj;

						if (tDataInfo == null || tDataInfo.getData() == null) {

							dataError();
							return;
						} else {

							handlerSSubjectData(tDataInfo.getData());

							mListView.stopRefresh();

							if (mBolRefersh && mAdapter != null) {

								mAdapter.updateData(mDatas);
								mBolRefersh = false;
							} else {

								mAdapter = new SSubjectAdapter(
										HomeSSubjectActivity.this, mDatas);
								mListView.setAdapter(mAdapter);
							}
						}

						dismissLoadDialog();
						break;
					}
				}
			});
	private String mUrl;
	private ShareController shareController = new ShareController();

	private void handlerSSubjectData(SSubjectData data) {

		mDatas = new ArrayList<AdapterData>();

		if (data.getBannerimage() != null && !data.getBannerimage().equals("")) {

			mDatas.add(new SSubjectBanner(data.getBannerimage()));
		}

		if (data.getBigImg() != null && data.getBigImg().size() > 0) {

			mDatas.add(new HomeAdapterBigImg(data.getBigImg()));
		}

		mDatas.add(new GroupData());

		if (data.getLive() != null && data.getLive().size() > 0) {

			// add live
			int tLiveSize = data.getLive().size();

			if (tLiveSize >= 2)
				tLiveSize = 2;

			for (int i = 0; i < tLiveSize; i++) {

				Live tHomeLive = data.getLive().get(i);

				mDatas.add(tHomeLive);
			}
		}

		if (data.getLivevideolist() != null
				&& data.getLivevideolist().size() > 0) {

			// add live video
			int tHotLiveSize = data.getLivevideolist().size();

			for (int i = 0; i < tHotLiveSize; i += 3) {

				List<SSLiveVideoList> tAddHotlives = new ArrayList<SSLiveVideoList>();

				tAddHotlives.add(data.getLivevideolist().get(i));

				int tTwo = i + 1;

				if (tTwo < tHotLiveSize) {

					tAddHotlives.add(data.getLivevideolist().get(tTwo));

					int tThree = tTwo + 1;

					if (tThree < tHotLiveSize) {

						tAddHotlives.add(data.getLivevideolist().get(tThree));
					}
				}

				mDatas.add(new SSubjectAdapterLVList(tAddHotlives));
			}
		}

		if (data.getList1() != null && !data.getList1().getItemlist().isEmpty()) {

			mDatas.add(new GroupData(data.getList1().getTitle()));

			// add video list 1
			int tVideoListSize1 = data.getList1().getItemlist().size();

			for (int i = 0; i < tVideoListSize1; i += 2) {

				List<SSVideoList> tAddList = new ArrayList<SSVideoList>();

				tAddList.add(data.getList1().getItemlist().get(i));

				int tTwo = i + 1;

				if (tTwo < tVideoListSize1) {

					tAddList.add(data.getList1().getItemlist().get((i + 1)));
				}

				mDatas.add(new SSVideoList1(tAddList));
			}
		}

		if (data.getList2() != null && !data.getList2().getItemlist().isEmpty()) {

			mDatas.add(new GroupData(data.getList2().getTitle()));

			mDatas.add(new SSVideoList2(data.getList2().getItemlist()));
		}

		if (data.getList3() != null && !data.getList3().getItemlist().isEmpty()) {

			mDatas.add(new GroupData(data.getList3().getTitle()));

			int tVideoListSize3 = data.getList3().getItemlist().size();

			for (int i = 0; i < tVideoListSize3; i++) {

				mDatas.add(new SSVideoList3(data.getList3().getItemlist()
						.get(i)));
			}
		}

		if (data.getList4() != null && !data.getList4().getItemlist().isEmpty()) {

			mDatas.add(new GroupData(data.getList4().getTitle()));

			int tVideoListSize4 = data.getList4().getItemlist().size();

			for (int i = 0; i < tVideoListSize4; i++) {

				mDatas.add(new SSVideoList4(data.getList4().getItemlist()
						.get(i)));
			}
		}

		if (data.getVoteurl() != null
				&& !data.getVoteurl().getItemlist().isEmpty()) {

			for (int i = 0; i < data.getVoteurl().getItemlist().size(); i++) {

				mDatas.add(new SSubjectVote(data.getVoteurl().getTitle(), data
						.getVoteurl().getItemlist().get(i)));
			}
		}

		if (data.getInteractive() != null) {

			boolean tIsGroup = true;

			if (data.getInteractive().getInteractiveone() != null
					&& !data.getInteractive().getInteractiveone().isEmpty()) {

				// add interaction group
				mDatas.add(new GroupData(data.getInteractive().getTitle()));

				tIsGroup = false;

				int tOneSize = data.getInteractive().getInteractiveone().size();

				for (int i = 0; i < tOneSize; i++) {

					// add interaction one
					mDatas.add(new InteractionOne(data.getInteractive()
							.getInteractiveone().get(i)));
				}
			}

			if (data.getInteractive().getInteractivetwo() != null
					&& data.getInteractive().getInteractivetwo().size() > 0) {

				if (tIsGroup) {

					// add interaction group
					mDatas.add(new GroupData(data.getInteractive().getTitle()));
				}

				int tTwoSize = data.getInteractive().getInteractivetwo().size();

				for (int i = 0; i < tTwoSize; i += 2) {

					// add interaction two
					List<Interaction> tInteractions = new ArrayList<Interaction>();

					tInteractions.add(data.getInteractive().getInteractivetwo()
							.get(i));

					int tTwo = i + 1;

					if (tTwo < tTwoSize) {

						tInteractions.add(data.getInteractive()
								.getInteractivetwo().get(tTwo));
					}

					mDatas.add(new InteractionTwo(tInteractions));
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//addWXPlatform();
		initView();
		initData();
	}

	private void initView() {

		setContentView(R.layout.activity_home_ssubject);

		mListView = (XListView) this.findViewById(R.id.xlvSSubject);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);

		mIvSSubjectNoNet = (ImageView) this.findViewById(R.id.ivSSubjectNoNet);
		mIvSSubjectNoNet.setOnClickListener(this);

		String tTitle = getIntent().getStringExtra("title");
		 mUrl = getIntent().getStringExtra("url");
		if (tTitle != null && !tTitle.equals("")) {

			TextView tTvTitle = (TextView) this
					.findViewById(R.id.tvSSubjectTitle);
			tTvTitle.setText(tTitle);
			shareTitle = tTitle;
		}

		this.findViewById(R.id.tvSSubjectBack).setOnClickListener(this);
		this.findViewById(R.id.tvSSubjectShare).setOnClickListener(this);
	}

	private void initData() {

		showLoadingDialog();

		if (isConnected()) {

			mIvSSubjectNoNet.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);

			String tUrl = getIntent().getStringExtra("url");

			if (tUrl == null || tUrl.equals("")) {

				dataError();
			}

			mHandler.getHttpJson(tUrl, SSubjectDataInfo.class, SSUBJECT_DATA);
		} else {

			dataError();
		}
	}

	/**
	 * 数据错误处理
	 */
	private void dataError() {

		mIvSSubjectNoNet.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
		mListView.stopRefresh();

		dismissLoadDialog();
	}



	@Override
	public void onRefresh() {

		mBolRefersh = true;

		initData();
	}

	@Override
	public void onLoadMore() {

	}


	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ivSSubjectNoNet:

			initData();
			break;
		case R.id.tvSSubjectBack:

			HomeSSubjectActivity.this.finish();
			break;
		case R.id.tvSSubjectShare:
			MobileAppTracker.trackEvent(shareTitle, "列表", "首页", 0, shareTitle, "分享", this);
			MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
			Log.i("统计","熊猫观察图文浏览详情=  "+shareTitle);
//			ShareController.getInstance().showPopWindow(mListView, "专题名称");
			shareController.showPopWindow(mListView, shareTitle,"5",null, mUrl);

			break;
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		if (player != null) {
			player.onPause();
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
				super.onResume();
		if (player != null) {
			player.onResume();
			player.doStart();

		}
				
				if(null != mListView){
					
					//如果系统键盘还在，则隐藏系统键盘
					InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);  
					imm.hideSoftInputFromWindow(mListView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);	
				}
				
	}

	@Override
	protected void onRestart() {
		super.onRestart();

	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (player != null) {
			player.onDestroy();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	

}
