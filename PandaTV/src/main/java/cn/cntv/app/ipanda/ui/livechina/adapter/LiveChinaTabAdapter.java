package cn.cntv.app.ipanda.ui.livechina.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.cntv.app.ipanda.ui.livechina.entity.LiveChinaAllTablist;
import cn.cntv.app.ipanda.ui.livechina.fragment.LiveChinaItemFragment;

public class LiveChinaTabAdapter extends FragmentStatePagerAdapter {
	private LiveChinaAllTablist allTablist;
	private List<Fragment> fragments;
	
	public LiveChinaTabAdapter(FragmentManager fm,LiveChinaAllTablist allTablist) {
		super(fm);
		this.allTablist = allTablist;
		fragments = new ArrayList<Fragment>();
	}

	@Override
	public Fragment getItem(int positon) {
		Fragment fragment= new LiveChinaItemFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("chananelUrl", allTablist.tablist.get(positon));
		bundle.putInt("positon", positon);
		fragment.setArguments(bundle);
		fragments.add(fragment);
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return allTablist.tablist.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return allTablist.tablist.get(position).title;
	}

	public Fragment getFragment(int position){
		return fragments.get(position);
	}
	
	
	
}
