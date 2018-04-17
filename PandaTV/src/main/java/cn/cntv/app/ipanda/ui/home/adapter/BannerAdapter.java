package cn.cntv.app.ipanda.ui.home.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class BannerAdapter extends PagerAdapter {

	private List<View> mList;
	private int mSize;

	public BannerAdapter(List<View> views) {

		this.mList = views;
		this.mSize = views.size();
	}

	public void setList(List<View> list) {
		mList = list;
		notifyDataSetChanged();
	}

	/**
	 * Return the number of views available.
	 */
	@Override
	public int getCount() {
		return mSize == 1 ? 1 : Integer.MAX_VALUE;
	}

	/**
	 * Remove a page for the given position. 滑动过后就销毁 ，销毁当前页的前一个的前一个的页！
	 * instantiateItem(View container, int position) This method was deprecated
	 * in API level . Use instantiateItem(ViewGroup, int)
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// View v = mList.get(position % mSize);
		// container.removeView(v/* mList.get(position) */);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	/**
	 * Create the page for the given position.
	 */
	@Override
	public Object instantiateItem(final ViewGroup container, int position) {
		View view = mList.get(position % mSize);
		if (view.getParent() != null)
			(container).removeView(view/* mList.get(position) */);
		container.addView(view/* mList.get(position) */);
		return view/* mList.get(position) */;

	}
}
