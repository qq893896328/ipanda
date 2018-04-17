package cn.cntv.app.ipanda.ui.cctv.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gridsum.mobiledissector.util.StringUtil;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVMuliLaugItem;

/**
 * @author： pj
 * @Date： 2015年12月30日 上午10:19:21
 * @Description: 多语种列表适配器
 */
public class CCTVMuliLaugAdapter extends CCTVBaseAdapter {
	private List<CCTVMuliLaugItem> mEntityList;
	private LayoutInflater mInflater;

	public CCTVMuliLaugAdapter(Context context, List<CCTVMuliLaugItem> datas,
			LayoutInflater inflater) {
		mContext = context;
		mEntityList = datas;
		mInflater = inflater;
		setmContext(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		CCTVMuliLaugItem entity = mEntityList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.cctv_mulilaug_item, null,
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
		viewHolder.lableIv = (ImageView) convertView.findViewById(R.id.lableIv);
	}

	private void setInfo(int position, CCTVMuliLaugItem entity,
			ViewHolder viewHolder, View view) {
		viewHolder.imgView.setImageDrawable(null);
		String imgStr = entity.getImage();
		// if (StringUtil.isNullOrEmpty(imgStr)) {
		Glide.with(mContext)
				.load(imgStr)
				.asBitmap()
				.placeholder(R.drawable._tw)
				.error(R.drawable._tw)
				.into(viewHolder.imgView);
		// } else {
		// mImageLoader.displayImage(
		// imgStr + (imgStr.contains("?") ? "&" : "?") + "a="
		// + System.currentTimeMillis(), viewHolder.imgView,
		// mOptions_410x231);
		// }

		viewHolder.lableIv.setImageDrawable(null);
		if (!StringUtil.isNullOrEmpty(entity.getLiveimage())) {

			Glide.with(mContext)
					.load(entity.getLiveimage())
					.asBitmap()
					.into(viewHolder.lableIv);
			viewHolder.lableIv.setVisibility(View.VISIBLE);
		} else {
			viewHolder.lableIv.setVisibility(View.GONE);
		}
		viewHolder.titleView.setText(entity.getTitle());
		viewHolder.briefView.setText(entity.getBrief());
		if ("2".equals(entity.getType())) {
			viewHolder.briefView.setGravity(Gravity.RIGHT);
		} else {
			viewHolder.briefView.setGravity(Gravity.LEFT);
		}
		// 列表直播内容不显示,一期不显示
		// viewHolder.briefView.setVisibility(View.GONE);
	}

	class ViewHolder {
		ImageView imgView;// 直播图片
		TextView titleView;// 直播标题
		TextView briefView;// 直播简介
		ImageView lableIv;// 直播标签图片
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
