package cn.cntv.app.ipanda.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import cn.cntv.app.ipanda.R;

public class ListUtils {
	private Context context;

	public ListUtils(Context context) {
		this.context = context;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		int count = listAdapter.getCount() > 3 ? 3 : listAdapter.getCount();
		// 暂定为3条，不然需要屏幕高度减去其他容器高度，然后将剩余的高度除以item高度获得条数
		for (int i = 0; i < count; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public void setListViewHeightBasedOnChildrenVer(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = dip2px(context, 65) * 2 + 60;
		/*
		 * for (int i = 0; i < listAdapter.getCount(); i++) { //
		 * listAdapter.getCount()返回数据项的数目 totalHeight += dip2px(context, 65); //
		 * 统计所有子项的总高度 }
		 */
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listView
				.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
