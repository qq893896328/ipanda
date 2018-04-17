package cn.cntv.app.ipanda.ui.lovepanda.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.cntv.app.ipanda.ui.lovepanda.entity.LpandaData;
import cn.cntv.app.ipanda.ui.lovepanda.fragment.LpandaColumnFragment;
import cn.cntv.app.ipanda.ui.lovepanda.fragment.LpandaKnownFragment;
import cn.cntv.app.ipanda.ui.lovepanda.fragment.LpandaLiveFragment;

public class  LpandaTabAdapter extends FragmentStatePagerAdapter{

	private LpandaData lpandaData;
	private List<Fragment> fragments;

	public LpandaTabAdapter(FragmentManager fm,LpandaData lpandaData) {
		super(fm);
		this.lpandaData = lpandaData;
		fragments = new ArrayList<Fragment>();


	}

	
	@Override
	public Fragment getItem(int arg0) {
		String viewTitle = lpandaData.getTablist().get(arg0).getTitle();

		Fragment fragment =null;
		Bundle bundle = new Bundle();
		if(viewTitle.equals("直播")){
			fragment=LpandaLiveFragment.newInstance();
			bundle.putString("lpanda_talk_id_live", lpandaData.getTablist().get(arg0).getId());
		}else if(viewTitle.equals("专题事件直播")){
			fragment = LpandaKnownFragment.newInstance();
			bundle.putString("lpandaurl", lpandaData.getTablist().get(arg0).getUrl());
			bundle.putString("lpanda_talk_id", lpandaData.getTablist().get(arg0).getId());
		}else if(viewTitle.equals("熊勒个猫")){
			fragment = LpandaColumnFragment.newInstance();
			bundle.putString("lpandaId",lpandaData.getTablist().get(arg0).getId());
			bundle.putString("urlStr", "http://api.cntv.cn/video/videolistById?");
			bundle.putString("title",viewTitle);
			//Log.i("aaaaa",viewTitle);
		}
		else {
			fragment = LpandaColumnFragment.newInstance();
			bundle.putString("lpandaId",lpandaData.getTablist().get(arg0).getId());
			bundle.putString("title",viewTitle);
			bundle.putString("urlStr", "http://api.cntv.cn/video/videolistById?");
		}
		
		bundle.putInt("positon", arg0);
		fragment.setArguments(bundle);
		fragments.add(fragment);
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lpandaData.getTablist().size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return lpandaData.getTablist().get(position).getTitle();
	}

	public Fragment getFragment(int position) {
		return fragments.get(position);
	}

}
