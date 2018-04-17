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
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVVideo;

/**
 * @author： pj
 * @Date： 2016年1月4日 下午3:04:23
 * @Description:央视名栏底层页的列表适配器
 */
public class CCTVDetailAdapter extends CCTVBaseAdapter {
	private List<CCTVVideo> mEntityList;
	private LayoutInflater mInflater;

	public CCTVDetailAdapter(Context context, List<CCTVVideo> datas,
			LayoutInflater inflater) {
		mContext = context;
		mEntityList = datas;
		mInflater = inflater;
		setmContext(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		CCTVVideo entity = mEntityList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.cctv_detail_item, null,
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
		viewHolder.videoLengthView = (TextView) convertView
				.findViewById(R.id.videoLength);
	}

	private void setInfo(int position, CCTVVideo entity, ViewHolder viewHolder,
			View view) {
		viewHolder.imgView.setImageDrawable(null);
		// viewHolder.imgView.setScaleType(StringUtil.isNullOrEmpty(entity
		// .getImg()) ? ScaleType.FIT_XY : ScaleType.CENTER_CROP);


		Glide.with(mContext)
				.load(entity.getImg())
				.asBitmap()
				.placeholder(R.drawable._tw)
				.error(R.drawable._tw)
				.into(viewHolder.imgView);
		viewHolder.titleView.setText(entity.getT());
		viewHolder.videoLengthView.setText(entity.getLen());
	}

	class ViewHolder {
		ImageView imgView;// 图片
		TextView titleView;// 标题
		TextView videoLengthView;// 时长
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
