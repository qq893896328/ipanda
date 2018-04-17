package cn.cntv.app.ipanda.ui.lovepanda.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gridsum.mobiledissector.MobileAppTracker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.CollectTypeEnum;
import cn.cntv.app.ipanda.data.net.HttpTools;
import cn.cntv.app.ipanda.data.net.retrofit.Call;
import cn.cntv.app.ipanda.data.net.retrofit.Callback;
import cn.cntv.app.ipanda.data.net.retrofit.Response;
import cn.cntv.app.ipanda.ui.BaseFragment;
import cn.cntv.app.ipanda.ui.lovepanda.adapter.LpandaColumnCatAdapter;
import cn.cntv.app.ipanda.ui.lovepanda.entity.LpandaColumnCatList;
import cn.cntv.app.ipanda.ui.lovepanda.entity.PandaBean;
import cn.cntv.app.ipanda.ui.lovepanda.entity.VideoBean;
import cn.cntv.app.ipanda.ui.lovepanda.entity.VideosetBean;
import cn.cntv.app.ipanda.ui.play.PlayVodFullScreeActivity;
import cn.cntv.app.ipanda.ui.play.entity.PlayVodEntity;
import cn.cntv.app.ipanda.xlistview.XListView;
import cn.cntv.app.ipanda.xlistview.XListView.IXListViewListener;

public class LpandaColumnFragment extends BaseFragment implements OnClickListener{
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private static final int LPANDA_COLUMN_DATA = 1;
	private String mParam1;
	private String mParam2;
	Bundle mBundle;
	View view;
	private LayoutInflater mInflater;
	private List<VideoBean> columnList;
	private XListView lpanda_column_cat_list;
	private ImageView lpanda_no_net_column;
	private LpandaColumnCatAdapter adapter;
	Boolean isRefresh = false;//刷新
	Boolean isLoadMore = false;//加载
	private int  page=1; // 页数
	// 是否初始化view
	private boolean isInitView = false;
	// 是否已经将pullrefreshview动态添加进去了
	private boolean isInitPullView = false;
	
	private TextView xlistview_header_last_time;
	private String lmtitle;

	public LpandaColumnFragment() {
		// Required empty public constructor
	}

	public static LpandaColumnFragment newInstance() {
		LpandaColumnFragment fragment = new LpandaColumnFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
			lpandaId = getArguments().getString("lpandaId");
			lmtitle =getArguments().getString("title");
			LPANDA_COLUMN_URL = getArguments().getString("urlStr");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.lpanda_list, container, false);
		isInitView = true;

		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub
		mInflater = LayoutInflater.from(getActivity());
		lpanda_column_cat_list = (XListView) view.findViewById(R.id.lpanda_column_list);
		lpanda_column_cat_list.setPullLoadEnable(false);
		lpanda_column_cat_list.setPullRefreshEnable(true);
		xlistview_header_last_time = (TextView) view.findViewById(R.id.xlistview_header_last_time);
		xlistview_header_last_time.setText("");
		lpanda_no_net_column=(ImageView) view.findViewById(R.id.lpanda_no_net_lan);
		lpanda_no_net_column.setOnClickListener(this);
		showLoadingDialog();
		isRefresh = true;
		initListener();
		//		ComonUtils.setScale16_9(getActivity(), lpanda_column_cat_list);
		isInitPullView = true;
		
		columnList = new ArrayList<VideoBean>();
		adapter = new LpandaColumnCatAdapter(getActivity(), columnList);
		lpanda_column_cat_list.setAdapter(adapter);
	}
	String LPANDA_COLUMN_URL;
	private void initData(final int pnum) {

		// TODO Auto-generated method stub
		/**
		 * 判断是否联网
		 */
		if (isConnected()) {
			
			
			if(LPANDA_COLUMN_URL == null && LPANDA_COLUMN_URL.equals("")){
				Toast.makeText(getActivity(), R.string.load_fail, Toast.LENGTH_SHORT)
				.show();
			}else{

						HttpTools.get(LPANDA_COLUMN_URL+"vsid="+lpandaId+"&n=7&serviceId=panda&o=desc&of=time"
								+"&p="+Integer.toString(pnum),PandaBean.class,null).enqueue(new Callback<PandaBean>() {
							@Override
							public void onResponse(Call<PandaBean> call, Response<PandaBean> response) {
								lpanda_column_cat_list.stopLoadMore();
								lpanda_column_cat_list.stopRefresh();
								PandaBean bean = response.body();
								dismissLoadDialog();
								if(bean!=null){
									Log.i("aaa",""+bean.getVideo().size());
									if (bean.getVideo().size()>5){
										lpanda_column_cat_list.setPullLoadEnable(true);
									}
									if(page != 1 && null!= bean){
										if( null!=bean.getVideo()&&bean.getVideo().size()>0){
											columnList.addAll(bean.getVideo());
											adapter.notifyDataSetChanged();
										}else{
//							lpanda_column_cat_list.setVisibility(View.GONE);
											lpanda_column_cat_list.setPullLoadEnable(false);
											Toast.makeText(getActivity(),R.string.no_more_data,Toast.LENGTH_SHORT).show();
											//Toast.makeText(getActivity(), "暂无更多数据",0).show();

										}
									}else{
										if(columnList!=null&& columnList.size()!=0){
											columnList.clear();
										}
										columnList.addAll(bean.getVideo());
										adapter.notifyDataSetChanged();
									}
								}
							}

							@Override
							public void onFailure(Call<PandaBean> call, Throwable t) {
								Toast.makeText(getActivity(), R.string.load_try_again, Toast.LENGTH_SHORT).show();
								dismissLoadDialog();
//								lpanda_no_net_column.setVisibility(View.VISIBLE);
//								lpanda_column_cat_list.setVisibility(View.GONE);
							}

						});


			}
		}
		
	}

	
	/**
	 * 栏目的item点击事件
	 */
	OnItemClickListener itemClick = new OnItemClickListener() {

		private PlayVodEntity vodModel;

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			// TODO Auto-generated method stub
			try{
			
			position = position-1;
			String channelId=columnList.get(position).getVsid();
			String channelGuid=columnList.get(position).getVid();
			String channelTitle=columnList.get(position).getT();
			String channelUrl=columnList.get(position).getUrl();
			String channelPicUrl=columnList.get(position).getImg();
			String channelLength=columnList.get(position).getLen();
				Log.e("mTag",channelLength);

				//统计
				MobileAppTracker.trackEvent("熊猫直播"+channelTitle+"视频", "", lmtitle, 0, channelGuid, "视频观看",getActivity());
				MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
				Log.e("统计","事件名称:"+channelTitle+"***事件类别:"+"列表"+"***事件标签:"+"熊猫直播*"+lmtitle+"***类型:"+"视频观看"+"*****ID="+channelGuid);
			
			vodModel = new PlayVodEntity(
						CollectTypeEnum.SP.value()+"",
						channelGuid, 
						null,
						channelUrl,
						channelPicUrl,
						channelTitle, 
						channelGuid, 4,
						channelLength);
			playVodEntities=new ArrayList<PlayVodEntity>();
			for (int i = 0; i < columnList.size(); i++) {
				
				PlayVodEntity playvodentity = new PlayVodEntity(
						CollectTypeEnum.SP.value()+"",
						columnList.get(i).getVid(),
						null,
						columnList.get(i).getUrl(),
						columnList.get(i).getImg(),
						columnList.get(i).getT(),
						columnList.get(i).getVid(), 2,
						columnList.get(position).getLen());

				playVodEntities.add(playvodentity);
			}
			
			Intent intent = new Intent(getActivity(),PlayVodFullScreeActivity.class);
			intent.putExtra("vid", vodModel);
			intent.putExtra("list", (Serializable)playVodEntities);
//			intent.putExtra("list",
//					playVodEntities != null ? ((Serializable) playVodEntities) : null);
			getActivity().startActivity(intent);
			
			}catch(Exception e){

			}
		
		}
	};
	private List<PlayVodEntity> playVodEntities;
	
	/**
	 * initListener
	 */
	private void initListener() {
		// TODO Auto-generated method stub
		lpanda_column_cat_list.setOnItemClickListener(itemClick);
		lpanda_column_cat_list.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if (columnList != null) {
					page=1;
					initData(page);
				}
			}
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				if (columnList != null) {
					page++;
					
					isLoadMore = true;
					//加载更多没有更多数据
					//Toast.makeText(getActivity(), "暂无更多数据", 0).show();
					initData(page);

				}
			}
		});
	}

	
	private String lpandaId;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
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

					initData(page);
				}
			}
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lpanda_no_net_lan:
			if (isConnected()) {
//				mHandler.getHttpJson(WebAddressEnum.LPANDA_COLUMN.toString(),
//						LpandaColumnCat.class, LPANDA_COLUMN_DATA);
			
			lpanda_no_net_column.setVisibility(view.GONE);
			lpanda_column_cat_list.setVisibility(view.VISIBLE);
			initData(page);
			}
			break;
		}
	}
}

