package cn.cntv.app.ipanda.ui.livechina.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.gridsum.mobiledissector.MobileAppTracker;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.livechina.adapter.LiveChinaTabAdapter;
import cn.cntv.app.ipanda.ui.livechina.entity.LiveChinaAllTablist;
import cn.cntv.app.ipanda.ui.livechina.view.NoInteruViewPager;
import cn.cntv.app.ipanda.view.LiveChinaTabPageIndicator;

public class LiveChinaTabView {
	protected View contentView;
	private NoInteruViewPager xiYouViewPager;
	private LiveChinaTabPageIndicator tabPagerIndicatorView;
//	private Context context;
	private Fragment fragment;
	private LiveChinaAllTablist allTablist;
	
	private LiveChinaTabAdapter liveChinaTabAdapter;
	private int lastPosition;
	private Context context;
	public static String title;


	public LiveChinaTabView(Fragment fragment,
			LiveChinaAllTablist allTablist) {
		this.fragment = fragment;
		this.allTablist = allTablist;
		contentView = initView((LayoutInflater) fragment.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		xiYouViewPager = (NoInteruViewPager)contentView.findViewById(R.id.live_china_viewPager);
		tabPagerIndicatorView = (LiveChinaTabPageIndicator) contentView.findViewById(R.id.live_china_indicator);
	}
	
	protected View initView(LayoutInflater inflater){
		View view = inflater.inflate(R.layout.live_china_tab_view, null);
		return view;
	}


	public void initData(){
		title="直播中国";

		liveChinaTabAdapter = new LiveChinaTabAdapter(fragment.getChildFragmentManager(),allTablist);
		xiYouViewPager.setAdapter(liveChinaTabAdapter);
		xiYouViewPager.setOffscreenPageLimit(allTablist.tablist.size());
		tabPagerIndicatorView.setViewPager(xiYouViewPager);
		tabPagerIndicatorView.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				destoryPLay();
				lastPosition = arg0;

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			
		});
	}

	public void destoryPLay(){
		LiveChinaItemFragment item = (LiveChinaItemFragment)liveChinaTabAdapter.getFragment(lastPosition);
		if(item!=null){
			item.destoryPlay();
		}
	}
	
	
	public View getContentView() {
		return contentView;
	}
	
	public void notifyDataChange(){
		liveChinaTabAdapter.notifyDataSetChanged();
	}
	
	public boolean onKeyDownBack(){
		LiveChinaItemFragment item = (LiveChinaItemFragment)liveChinaTabAdapter.getFragment(lastPosition);
		if(item!=null){
			return item.onKeyDownBack();
		}
		return false;
		
	}


}
