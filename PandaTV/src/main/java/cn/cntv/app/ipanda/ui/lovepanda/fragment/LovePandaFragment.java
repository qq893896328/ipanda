package cn.cntv.app.ipanda.ui.lovepanda.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.WebAddressEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.ui.BaseFragment;
import cn.cntv.app.ipanda.ui.lovepanda.entity.LpandaData;
import cn.cntv.app.ipanda.ui.lovepanda.entity.LpandaTablist;
import cn.cntv.app.ipanda.utils.SharePreferenceUtil;

/**
 * @author：wangrp
 * @Description:熊猫直播
 */
public class LovePandaFragment extends BaseFragment implements OnClickListener {

	private static final String TAG = LovePandaFragment.class.getSimpleName();
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private static final int LPANDA_DATA = 0;
	private static final int LPANDA_TAB = 1;
	private static final int LPANDA_LIVE_DATA = 3;
	List<LpandaTablist> tabs;
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private FragmentManager fm;
	static final String key_selected_index = "select_index";
	Fragment fragment;
	FragmentTransaction transaction;
	// //保持上一个fragment 用于销毁视频
	// Fragment lastFragment;

	LovePandaTableView lovePandaTableView;
	RelativeLayout lpanda_main_layout;

	public LovePandaFragment() {
		// Required empty public constructor
	}

	public static LovePandaFragment newInstance() {
		LovePandaFragment fragment = new LovePandaFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		initTitle();
	}

	private void initTitle() {

		// 判断 TAB_URL是否为空 2016-1-23
		if (isConnected()) {
			showLoadingDialog();

			SharePreferenceUtil tPreferenceUtil = new SharePreferenceUtil(
					getActivity());
			String tUrl = tPreferenceUtil
					.getWebAddress(WebAddressEnum.WEB_PANDA_LIVE.toString());

			if (tUrl == null || tUrl.equals("")) {

				Toast.makeText(getActivity(), R.string.load_fail, Toast.LENGTH_SHORT)
						.show();
			} else {

				mHandler.getHttpJson(tUrl, LpandaData.class, LPANDA_DATA);
			}
		} else {
			Toast.makeText(getActivity(), R.string.network_invalid, Toast.LENGTH_SHORT).show();
			lpanda_no_net.setVisibility(view.VISIBLE);
		}
	}

	private XjlHandler<LpandaData> mHandler = new XjlHandler<LpandaData>(
			new HandlerListener() {
				@Override
				public void handlerMessage(HandlerMessage msg) {
					// TODO Auto-generated method stub
					switch (msg.what) {
					case Integer.MAX_VALUE:
						break;
					case LPANDA_DATA:
						lpanda_no_net.setVisibility(View.GONE);
						LpandaData lpandaTab = (LpandaData) msg.obj;
						if(null != lpandaTab){
							
							getTabData(lpandaTab);
						}
						
						break;
					}
				}

				/**
				 * 获取熊猫直播scroll title
				 */
				private void getTabData(LpandaData data) {
					// TODO Auto-generated method stub

					if(null != data && null != data.getTablist()){
						tabs = data.getTablist();

						if (tabs != null && tabs.size() > 0) {
							lovePandaTableView = new LovePandaTableView(
									LovePandaFragment.this, data);
							lovePandaTableView.initData();
							lpanda_main_layout.addView(
									lovePandaTableView.getContentView(), 0);
						}
					}
					
				}
			});
	private RelativeLayout lpanda_no_net;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.lpanda_sroll, container, false);

		fm = getChildFragmentManager();
		if (savedInstanceState != null) {
			int tLpandaTabIndex = savedInstanceState.getInt(key_selected_index,
					0);
		}
		lpanda_no_net = (RelativeLayout) view.findViewById(R.id.lpanda_no_net_image);
		lpanda_no_net.setOnClickListener(this);
		lpanda_main_layout = (RelativeLayout) view
				.findViewById(R.id.lpanda_main_layout);

			lpanda_no_net.setVisibility(View.VISIBLE);

		return view;
	}
	/**
	 * 直播视频全屏下titile处理
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if (lovePandaTableView != null) {
				lovePandaTableView.getContentView()
						.findViewById(R.id.lpanda_tab_layout)
						.setVisibility(View.GONE);
			}
		} else {
			if (lovePandaTableView != null) {
				lovePandaTableView.getContentView()
						.findViewById(R.id.lpanda_tab_layout)
						.setVisibility(View.VISIBLE);
			}
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lpanda_no_net_image:
			if (isConnected()) {
				lpanda_no_net.setVisibility(view.GONE);

				initTitle();
			}
			break;
		}
	}

	public void destoryPlay() {
		if (lovePandaTableView != null) {
			lovePandaTableView.destoryPLay();
		}
	}

	public boolean onkeyDownBack() {
		if (lovePandaTableView != null) {
			return lovePandaTableView.onKeyDownBack();
		}
		return false;
	}

}
