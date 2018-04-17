package cn.cntv.app.ipanda.ui.livechina.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.LinkedList;
import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.CollectPageSourceEnum;
import cn.cntv.app.ipanda.constant.CollectTypeEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.ui.BaseFragment;
import cn.cntv.app.ipanda.ui.livechina.adapter.LiveChinaTabItemAdapter;
import cn.cntv.app.ipanda.ui.livechina.adapter.LiveChinaTabItemAdapter.LiveChinaItemClickImple;
import cn.cntv.app.ipanda.ui.livechina.entity.LiveChinaBean;
import cn.cntv.app.ipanda.ui.livechina.entity.LiveChinaTabItem;
import cn.cntv.app.ipanda.ui.livechina.entity.LiveChineItem;
import cn.cntv.app.ipanda.ui.play.PlayLiveController;
import cn.cntv.app.ipanda.ui.play.entity.PlayLiveEntity;
import cn.cntv.app.ipanda.utils.TimeHelper;
import cn.cntv.app.ipanda.xlistview.XListView;
import cn.cntv.app.ipanda.xlistview.XListView.IXListViewListener;

/**
 * 直播中国每个item页
 * @author wuguicheng
 *
 */
public class LiveChinaItemFragment extends BaseFragment implements IXListViewListener, LiveChinaItemClickImple, OnClickListener{

	//获取列表
	private static final int GET_CHINA_ITEM_LIST = 1;
	
	private XListView mXListView;
	private LiveChinaTabItemAdapter mLiveChinaTabItemAdapter;
	private List<LiveChineItem> mLiveChineItems;
	private LiveChineItem liveChineItem;
	
	//是否初始化view
	private boolean isInitView = false;
	//是否已经将pullrefreshview动态添加进去了
	private boolean isInitPullView= false;
	
	private View view;
	
	public LiveChinaTabItem liveChinaTabItem;
	//在tab中的位置 
	private int mPosition;
	
	
	String urlString = "http://asp.cntv.lxdns.com/asp/hls/850/0303000a/3/default/29978c0b08964a59a927b90836dd7485/850.m3u8";
	
	
	//移动的播放器
	private RelativeLayout scrollView;
	private FrameLayout live_cont_layout;
	//播放器控制器
	private PlayLiveController giraffePlayer;
	
	//无网络
	View notNetImg;
	
	//是否第一次获取到数据
	private boolean isFirstGetData=true;
	
	//视频集合
	private List<PlayLiveEntity> playLiveEntities;
	private String title;

	public LiveChinaItemFragment(){}
	
	
	private XjlHandler<LiveChinaBean> mHandler = new XjlHandler<LiveChinaBean>(
			new HandlerListener() {



				@Override
				public void handlerMessage(HandlerMessage msg) {
					switch (msg.what) {
					case GET_CHINA_ITEM_LIST:
						if(getActivity()==null){
							return;
						}
						if(notNetImg.getVisibility()==View.VISIBLE){
							notNetImg.setVisibility(View.GONE);
						}
						
						if(mPosition!=0){
							dismissLoadDialog();
						}else {
							BaseFragment parentFragment = (BaseFragment) getParentFragment();
							parentFragment.dismissLoadDialog();
						}
						LiveChinaBean liveChinaBean = (LiveChinaBean) msg.obj;
						if(liveChinaBean!=null&&liveChinaBean.live.size()>0){
							
							//存视频集合
							playLiveEntities.clear();
							for (int i = 0; i < liveChinaBean.live.size(); i++) {
								 liveChineItem = liveChinaBean.live.get(i);
						        PlayLiveEntity playLiveEntity = new PlayLiveEntity(
						        		liveChineItem.id, 
						        		liveChineItem.title, 
						        		liveChineItem.image,
						        		null, 
						        		null,
						        		CollectTypeEnum.PD.value()+"",
						        		CollectPageSourceEnum.ZBZG.value(),
						        		false
						        		);
						        playLiveEntities.add(playLiveEntity);
							}
							
							mLiveChineItems.clear();
							mLiveChineItems.addAll(liveChinaBean.live);
							/*MobileAppTracker.trackEvent(liveChinaTabItem.title, "列表", "直播中国", 0, liveChineItem.id, "视频播放", getContext());
							MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
							Log.i("统计：",liveChinaTabItem.title);
							Log.i("统计：",liveChineItem.id);*/
							mLiveChinaTabItemAdapter.notifyDataSetChanged();
							if(!isFirstGetData){
								destoryPlay();
							}
							
						}
						mXListView.stopRefresh();
						mXListView.stopLoadMore();
						mXListView.setRefreshTime(TimeHelper.getCurrentData());
						break;

					}
					
				}
    });

	@Override
	public void doFirstLoad(LiveChineItem liveChineItem,RelativeLayout live_play_layout) {
		
		
		scrollView = (RelativeLayout) View.inflate(getActivity(), R.layout.giraffe_player_live, null);
//		live_cont_layout.addView(scrollView);
	
		live_play_layout.addView(scrollView);
		mLiveChinaTabItemAdapter.setcurrentPlayLayoutParent(live_play_layout);
		mLiveChinaTabItemAdapter.setplayPositon(1);
		
		//默认播放第一个视频
		giraffePlayer = new PlayLiveController(
         		getActivity(),
         		scrollView
         		);
        giraffePlayer.setScale16_9(scrollView);
//		giraffePlayer.play(urlString);
		
        PlayLiveEntity playLiveEntity = new PlayLiveEntity(
        		liveChineItem.id, 
        		liveChineItem.title, 
        		liveChineItem.image,
        		null, 
        		null,
        		CollectTypeEnum.PD.value()+"",
        		CollectPageSourceEnum.ZBZG.value(),
        		false
        		);
        
        
        giraffePlayer.requestLive(playLiveEntity,playLiveEntities);
		
		isFirstGetData = false;
		
	}
	
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		
		view = inflater.inflate(R.layout.fragment_live_china_item, container, false);
		isInitView = true;
		
		
		
		if(getUserVisibleHint()){
			//setUserVisibleHint在onCreateView会在之前，调用此方法实现懒加载
			setUserVisibleHint(true);
		}
		
		return view;
	}
	

	private void initView(){
		if(getActivity()==null){
			return;
		}
		
		notNetImg = view.findViewById(R.id.live_china_item_not_net);
		notNetImg.setOnClickListener(this);
//		scrollView = (RelativeLayout) view.findViewById(R.id.text_live_play_video);  
		live_cont_layout = (FrameLayout) view.findViewById(R.id.live_cont_layout);
		
		
		mXListView = (XListView) View.inflate(getActivity(), R.layout.live_china_item_xlistview, null);
		

		
		mXListView.setXListViewListener(this);
		
		
		mXListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case SCROLL_STATE_TOUCH_SCROLL:
					break;
				case SCROLL_STATE_IDLE:
					break;
				default:
					break;
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				
				if((mLiveChinaTabItemAdapter.getPlayPostion()<firstVisibleItem 
						|| mLiveChinaTabItemAdapter.getPlayPostion()>mXListView.getLastVisiblePosition())
					 ){
					
					destoryPlay();
//					isFirstScroll = true;
	           	} 
//				else {
//	           		if(mLiveChinaTabItemAdapter.getcurrentPlayParentLayout()!=null){
//						if(giraffePlayer!=null){
//								changeLocation();
//						}
//					}
//	           	}
				
			}
		});
		
	}
	
	//点击每一个条目的回调方法
	@Override
	public void initViewLocation(LiveChineItem liveChineItem,RelativeLayout playContentLayout) {
		if(giraffePlayer!=null){
			giraffePlayer.doPause();
			
			 ViewGroup p = (ViewGroup) scrollView.getParent(); 
	         if (p != null) { 
	             p.removeView(scrollView);
	         } 
	         playContentLayout.addView(scrollView);
		}else {
			if(scrollView==null){
				scrollView = (RelativeLayout) View.inflate(getActivity(), R.layout.giraffe_player_live, null);
			}else {
				ViewGroup p = (ViewGroup) scrollView.getParent(); 
		         if (p != null) { 
		             p.removeView(scrollView);
		         } 
			}
			
//			live_cont_layout.addView(scrollView);
	         playContentLayout.addView(scrollView);
	         
			giraffePlayer = new PlayLiveController(
	         		getActivity(),
	         		scrollView
	         		);
			giraffePlayer.setScale16_9(scrollView);
		}
		isFirstScroll = true;
		PlayLiveEntity playLiveEntity = new PlayLiveEntity(
        		liveChineItem.id, 
        		liveChineItem.title, 
        		liveChineItem.image,
        		null, 
        		null,
        		CollectTypeEnum.PD.value()+"",
        		CollectPageSourceEnum.ZBZG.value(),
        		false
				);
		giraffePlayer.requestLive(playLiveEntity,playLiveEntities);
//		changeLocation();
	}
	
	/**
	 * 实时更新播放器的位置
	 */
	public void changeLocation(){
		
//		View playLayout = mLiveChinaTabItemAdapter.getcurrentPlayParentLayout();
//		int top = playLayout.getTop();
//		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) scrollView
//		        .getLayoutParams();
//		params.topMargin = top;
//		scrollView.setLayoutParams(params);
		
		
//		LinearLayout playLayout = mLiveChinaTabItemAdapter.getcurrentPlayParentLayout();
//		View play = playLayout.findViewById(R.id.play_layout);
//		scrollView.layout(playLayout.getLeft(), 
//				playLayout.getTop(), 
//				playLayout.getRight(), 
//				playLayout.getBottom()-(playLayout.getHeight()-play.getHeight()));
//		scrollView.offsetTopAndBottom(-playLayout.getTop());
		
//		LinearLayout playLayout = mLiveChinaTabItemAdapter.getcurrentPlayParentLayout();
//		View play = playLayout.findViewById(R.id.play_layout);
//		TranslateAnimation animation = new TranslateAnimation(0, 0,0, -playLayout.getTop()); 
//		scrollView.setAnimation(animation);
//		animation.setDuration(1000);//设置动画持续时间 
//		animation.setRepeatCount(1);//设置重复次数 
//		animation.startNow(); 
	}
	
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
			
		if(isVisibleToUser){
			
			if(isInitView){
				//第二个条目之后的预加载
				if(!isInitPullView){
					initView();
					initData();

				}
			}
		}
	}
	
	private void initData(){
		Bundle bundle = getArguments();
		if(bundle!=null){
			playLiveEntities = new LinkedList<PlayLiveEntity>();
			mPosition = bundle.getInt("positon",-1);

			liveChinaTabItem = (LiveChinaTabItem) bundle.getSerializable("chananelUrl");

			Log.i("aaaa",liveChinaTabItem.title);
			mLiveChineItems = new LinkedList<LiveChineItem>();
			mLiveChinaTabItemAdapter = new LiveChinaTabItemAdapter(getActivity(), mLiveChineItems,liveChinaTabItem.title.toString());
			mLiveChinaTabItemAdapter.setLiveChinaItemClickImple(this);
//		giraffePlayer.setListViewAdapter(mLiveChinaTabItemAdapter);
//		giraffePlayer.setConnctionListView(mXListView);

			mXListView.setAdapter(mLiveChinaTabItemAdapter);
			mXListView.setPullLoadEnable(false);
			mXListView.setPullRefreshEnable(true);
			isInitPullView = true;

			((RelativeLayout)view).addView(mXListView,0);







			if(!TextUtils.isEmpty(liveChinaTabItem.url)){
				if(isConnected()){
					getHttpDataTabList();
					if(mPosition!=0){
						showLoadingDialog();
					}
				}else {
					notNetImg.setVisibility(View.VISIBLE);
				}
			}else {
				showToast("服务器暂无返回数据，稍后再试");
				if(mPosition==0){
					BaseFragment parentFragment = (BaseFragment) getParentFragment();
					parentFragment.dismissLoadDialog();
				}
				notNetImg.setVisibility(View.VISIBLE);
			}
			
		}
	}
	
	private void getHttpDataTabList(){
		mHandler.getHttpJson(liveChinaTabItem.url,
					LiveChinaBean.class, GET_CHINA_ITEM_LIST);
	}

	@Override
	public void onRefresh() {
		if(liveChinaTabItem!=null){
			getHttpDataTabList();
		}
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void onRightSlip() {
//	}
//
//	@Override
//	public void onLeftSlip() {
//	}
	
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
//    	mLiveChinaTabItemAdapter.onConfigurationChanged(newConfig);
    	super.onConfigurationChanged(newConfig);
        if(getUserVisibleHint()){
        	if(mLiveChinaTabItemAdapter!=null){
            	
        		
        		
        		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        			LiveChinaFragment parentFragment = (LiveChinaFragment) getParentFragment();
        			parentFragment.setChinaTabViewViewsible(View.GONE);
        			if(scrollView==null){
        				return;
        			}
        			
        			ViewGroup p = (ViewGroup) scrollView.getParent(); 
                    if (p != null) { 
                        p.removeView(scrollView); 
                    } 
                    live_cont_layout.addView(scrollView);
//        			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) scrollView
//        			        .getLayoutParams();
//        			params.topMargin = 0;
//        			scrollView.setLayoutParams(params);
        			mXListView.setVisibility(View.GONE);
//        			scrollView.offsetTopAndBottom(0);  
        			
        		}else {
        			LiveChinaFragment parentFragment = (LiveChinaFragment) getParentFragment();
        			parentFragment.setChinaTabViewViewsible(View.VISIBLE);
//        			changeLocation();
//        			View playLayout = mLiveChinaTabItemAdapter.getcurrentPlayParentLayout();
//        			int top = playLayout.getTop();
//        			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) scrollView
//        			        .getLayoutParams();
//        			params.topMargin = top;
//        			scrollView.setLayoutParams(params);
        			mXListView.setVisibility(View.VISIBLE);
        			
        			if(scrollView!=null){
        				ViewGroup p = (ViewGroup) scrollView.getParent(); 
                        if (p != null) { 
                            p.removeView(scrollView); 
                        } 
                        mLiveChinaTabItemAdapter.getcurrentPlayParentLayout().addView(scrollView);
        			}
    			}
            	
        		if(giraffePlayer!=null){
        			giraffePlayer.onConfigurationChanged(newConfig);
//        			giraffePlayer.doStart();
        			giraffePlayer.getIsStatusLoding();
        		}
            }
        }
        
        
        
    }
    
	@Override
    public void onPause() {
        super.onPause();
        if(giraffePlayer!=null){
//        	if(giraffePlayer.getIsPlayUrl()){
        		giraffePlayer.onPause();
//        	}
        }
    }
	
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(giraffePlayer!=null){
//        	if(giraffePlayer.getIsPlayUrl()){
        		giraffePlayer.onDestroy();
//        	}
        }
    }
	
    @Override
    public void onResume() {
        super.onResume();
        if(getUserVisibleHint()){
        	if(giraffePlayer!=null){
            	giraffePlayer.onResume();
            }
        }
    }
    
    public boolean isFirstScroll = true;
//    public synchronized void pausePlay(){
//		if(mLiveChinaTabItemAdapter.getcurrentPlayParentLayout()!=null){
//			if(isFirstScroll){
//				isFirstScroll = false;
//				if(giraffePlayer!=null){
//					giraffePlayer.getPlayLayout().setVisibility(View.GONE);
//					mLiveChinaTabItemAdapter.setcurrentPlayLayoutParent(null);
//	    			mLiveChinaTabItemAdapter.setplayPositon(-1);
//					if(giraffePlayer.getIsPlayUrl()){
//		    			giraffePlayer.onPause();
//		    			
//		    		}
//				}	
//			}
//		}
//    }
    
    /**
     * 切换tab的时候销毁视频
     */
    public synchronized void destoryPlay(){
    	
    	if(giraffePlayer!=null&&mLiveChinaTabItemAdapter.getcurrentPlayParentLayout()!=null){
//    		if(giraffePlayer.getIsPlayUrl()){
    			giraffePlayer.onPause();
            	giraffePlayer.onDestroy();
//    		}
    		
//        	giraffePlayer.getPlayLayout().setVisibility(View.GONE);
        	giraffePlayer = null;
        	
        	mLiveChinaTabItemAdapter.getcurrentPlayParentLayout().removeView(scrollView);
    	}
    }


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.live_china_item_not_net:
			if(!TextUtils.isEmpty(liveChinaTabItem.url)){
				if(isConnected()){
					getHttpDataTabList();
				}
			}else {
				showToast("服务器暂无返回数据，稍后再试");
				
			}
			break;
		}
		
	}


	public boolean onKeyDownBack() {
    	if(giraffePlayer!=null){
    		return giraffePlayer.onKeyDownBack();
    	}
    	return false;
	}











    

}
