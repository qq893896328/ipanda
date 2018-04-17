package cn.cntv.app.ipanda.ui.cctv.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVWellKnowItem;

/**
 * @author： pj
 * @Date： 2015年12月30日 上午10:19:21
 * @Description: 央视名栏列表适配器
 */
public class CCTVWellKnowAdapter extends CCTVBaseAdapter {
	private List<CCTVWellKnowItem> mEntityList;
	private LayoutInflater mInflater;

	public CCTVWellKnowAdapter(Context context, List<CCTVWellKnowItem> datas,
			LayoutInflater inflater) {
		mContext = context;
		mEntityList = datas;
		mInflater = inflater;
		setmContext(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		CCTVWellKnowItem entity = mEntityList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.cctv_wellknow_item, null,
					false);
			viewHolder = new ViewHolder();
			initView(position, convertView, viewHolder);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		setInfo(position, entity, viewHolder, convertView);
		return convertView;
	}

	private void initView(int position, View convertView, ViewHolder viewHolder) {
		viewHolder.imgView = (ImageView) convertView
				.findViewById(R.id.imageView1);
		viewHolder.titleView = (TextView) convertView.findViewById(R.id.title);
		viewHolder.briefView = (TextView) convertView.findViewById(R.id.brief);
		viewHolder.videoLengthView = (TextView) convertView
				.findViewById(R.id.videoLength);
	}

	private void setInfo(int position, CCTVWellKnowItem entity,
			ViewHolder viewHolder, View view) {
		viewHolder.imgView.setImageDrawable(null);
		// viewHolder.imgView.setScaleType(StringUtil.isNullOrEmpty(entity
		// .getImage()) ? ScaleType.FIT_XY : ScaleType.CENTER_CROP);

		Glide.with(mContext)
				.load(entity.getImage())
				.asBitmap()
				.placeholder(R.drawable._tw)
				.error(R.drawable._tw)
				.into(viewHolder.imgView);
		viewHolder.titleView.setText(entity.getTitle());
		viewHolder.briefView.setText(entity.getBrief());
		viewHolder.videoLengthView.setText(entity.getVideoLength());
	}

	class ViewHolder {
		ImageView imgView;// 直播图片
		TextView titleView;// 直播标题
		TextView briefView;// 直播简介
		TextView videoLengthView;// 直播时长
	}

	@Override
	public int getCount() {
		return mEntityList.size();
	}

	@Override
	public Object getItem(int index) {
		if (index >= getCount()) {
			return null;
		}
		return mEntityList.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}
}
