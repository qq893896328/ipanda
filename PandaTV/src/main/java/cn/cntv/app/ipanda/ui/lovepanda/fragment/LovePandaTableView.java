package cn.cntv.app.ipanda.ui.lovepanda.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.livechina.view.NoInteruViewPager;
import cn.cntv.app.ipanda.ui.lovepanda.adapter.LpandaTabAdapter;
import cn.cntv.app.ipanda.ui.lovepanda.entity.LpandaData;
import cn.cntv.app.ipanda.view.LiveChinaTabPageIndicator;

public class LovePandaTableView {
	protected View contentView;
	private NoInteruViewPager xiYouViewPager;
	private LiveChinaTabPageIndicator tabPagerIndicatorView;
	private LpandaData lpandaData;
	private Fragment fragment;
	public String title;
	private LpandaTabAdapter lpandaTabAdatper;
	private int lastPosition;

	private Context context;
	public LovePandaTableView(Fragment fragment,
			LpandaData lpandaData) {
		this.fragment = fragment;
		this.lpandaData = lpandaData;
		contentView = initView((LayoutInflater) fragment.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		xiYouViewPager = (NoInteruViewPager)contentView.findViewById(R.id.lpanda_viewPager);
		tabPagerIndicatorView = (LiveChinaTabPageIndicator) contentView.findViewById(R.id.lpanda_indicator);
	}
	
	protected View initView(LayoutInflater inflater){
		View view = inflater.inflate(R.layout.lpanda_tab_view, null);
		return view;
	}
	
	public void initData(){
		title="熊猫直播";
		if(null == lpandaData && null == lpandaData.getTablist()){
			return;
		}
		
		lpandaTabAdatper = new LpandaTabAdapter(fragment.getChildFragmentManager(),lpandaData);
		xiYouViewPager.setAdapter(lpandaTabAdatper);
		xiYouViewPager.setOffscreenPageLimit(lpandaData.getTablist().size());
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
	
	public View getContentView() {
		return contentView;
	}
	
	public void notifyDataChange(){
		lpandaTabAdatper.notifyDataSetChanged();
	}
	
	public void destoryPLay(){
		Fragment lastFragment = lpandaTabAdatper.getFragment(lastPosition);
		if(lastFragment!=null){
			if(lastFragment instanceof LpandaLiveFragment){
				LpandaLiveFragment lpandaLiveFragment = (LpandaLiveFragment) lastFragment;
				lpandaLiveFragment.onPause();
				lpandaLiveFragment.onDestroy();
			}else if(lastFragment instanceof LpandaKnownFragment){
				LpandaKnownFragment lpandaKnownFragment = (LpandaKnownFragment) lastFragment;
				lpandaKnownFragment.onPause();
				lpandaKnownFragment.onDestroy();
			}
		}
	}

	public boolean onKeyDownBack() {
		Fragment item = lpandaTabAdatper.getFragment(lastPosition);
		if(item!=null){
			if(item instanceof LpandaLiveFragment){
				LpandaLiveFragment lpandaLiveFragment = (LpandaLiveFragment) item;
				return lpandaLiveFragment.onKeyDownBack();
			}else if(item instanceof LpandaKnownFragment){
				LpandaKnownFragment lpandaKnownFragment = (LpandaKnownFragment) item;
				return lpandaKnownFragment.onKeyDownBack();
			}
			
			
		}
		return false;
	}
	
}
