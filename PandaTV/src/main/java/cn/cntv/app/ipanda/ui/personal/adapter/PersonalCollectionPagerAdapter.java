package cn.cntv.app.ipanda.ui.personal.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by maqingwei on 16/7/28.
 */
public class PersonalCollectionPagerAdapter extends PagerAdapter{

    private List<View> mViews;
    private List<String> mTitles;

    public PersonalCollectionPagerAdapter(List<View> views,List<String> titles){
        this.mViews = views;
        this.mTitles = titles;
    }
    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(mViews.get(position), 0);
        return mViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        ((ViewPager) container).removeView(mViews.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
