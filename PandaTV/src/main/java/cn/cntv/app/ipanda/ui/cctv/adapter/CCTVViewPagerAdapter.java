package cn.cntv.app.ipanda.ui.cctv.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.List;

import cn.cntv.app.ipanda.ui.cctv.entity.CCTVBigImgItem;

public class CCTVViewPagerAdapter extends PagerAdapter {
	private List<View> mList;
	private Context context;
	private List<CCTVBigImgItem> mEntityList;
	private OnClickListener mOnImgClickListener;

	public CCTVViewPagerAdapter(List<View> views, Context context,
			OnClickListener onImgClickListener) {
		mList = views;
		this.context = context;
		this.mOnImgClickListener = onImgClickListener;
	}

	public void setList(List<View> list) {
		mList = list;
		notifyDataSetChanged();
	}

	public void setData(List<CCTVBigImgItem> entityList) {
		mEntityList = entityList;
	}

	/**
	 * Return the number of views available.
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	/**
	 * Remove a page for the given position. 滑动过后就销毁 ，销毁当前页的前一个的前一个的页！
	 * instantiateItem(View container, int position) This method was deprecated
	 * in API level . Use instantiateItem(ViewGroup, int)
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(mList.get(position));
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	/**
	 * Create the page for the given position.
	 */
	@Override
	public Object instantiateItem(final ViewGroup container, final int position) {
		container.removeView(mList.get(position));
		container.addView(mList.get(position));
		mList.get(position).setTag(position);
		mList.get(position).setOnClickListener(mOnImgClickListener);

		return mList.get(position);

	}
}
