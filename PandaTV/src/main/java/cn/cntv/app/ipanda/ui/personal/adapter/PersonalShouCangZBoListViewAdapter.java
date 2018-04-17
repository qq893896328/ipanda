package cn.cntv.app.ipanda.ui.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.CollectPageSourceEnum;
import cn.cntv.app.ipanda.db.entity.FavoriteEntity;
import cn.cntv.app.ipanda.utils.L;

/**
 * @author： pj
 * @Date： 2016年1月15日 下午4:41:52
 * @Description:我的收藏：直播列表适配器
 */
public class PersonalShouCangZBoListViewAdapter extends BaseAdapter {

	private List<FavoriteEntity> mEntityList;
	private Context mContext;
	private OnCheckedChangeListener mOnCheckedChangeListener;

	public PersonalShouCangZBoListViewAdapter(Context context,
			List<FavoriteEntity> entityList,
			OnCheckedChangeListener onCheckedChangeListener) {
		this.mEntityList = entityList;
		this.mContext = context;
		this.mOnCheckedChangeListener = onCheckedChangeListener;
	}

	@Override
	public int getCount() {
		return mEntityList.size();
	}

	@Override
	public Object getItem(int position) {
		return mEntityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;
		FavoriteEntity entity = mEntityList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.personal_shoucang_zblistview_item, null);
			initView(position, convertView, viewHolder);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		setInfo(position, entity, viewHolder, convertView);
		initListener(viewHolder);
		return convertView;

	}

	private void initView(int position, View convertView, ViewHolder viewHolder) {
		viewHolder.imgIv = (ImageView) convertView
				.findViewById(R.id.personal_hy_item_img);
		viewHolder.chkBox = (CheckBox) convertView
				.findViewById(R.id.personal_chk);
		viewHolder.titleTv = (TextView) convertView
				.findViewById(R.id.personal_sc_zb_listview_item_title);
	}

	private void setInfo(int position, FavoriteEntity entity,
			ViewHolder viewHolder, View view) {
		viewHolder.chkBox.setTag(entity);

		Boolean isShowEditUi = entity.getIsShowEditUi();
		if (isShowEditUi == null) {
			isShowEditUi = false;
		}
		viewHolder.chkBox.setVisibility(isShowEditUi ? View.VISIBLE
				: View.GONE);
		if (isShowEditUi) {
			viewHolder.chkBox.setChecked(entity.getIsChecked());
		}
		viewHolder.imgIv.setImageDrawable(null);
		viewHolder.imgIv.setBackgroundColor(mContext.getResources().getColor(
				R.color.white));
		if (entity.getPageSource() != null && entity.getPageSource() == CollectPageSourceEnum.PDZB.value()) {
			// 频道直播使用台标
			int imgid = mContext.getResources().getIdentifier(
					entity.getCollect_id(), "drawable",
					mContext.getPackageName());
			viewHolder.imgIv.setScaleType(ScaleType.CENTER_INSIDE);
			viewHolder.imgIv.setBackgroundColor(mContext.getResources()
					.getColor(R.color.cctv_grey));
			try {
				viewHolder.imgIv.setImageDrawable(mContext.getResources()
						.getDrawable(imgid));
			} catch (Exception e) {
				L.e(e.toString());
			}
		} else {
			viewHolder.imgIv.setScaleType(ScaleType.FIT_XY);
			Glide.with(mContext)
					.load(entity.getObject_logo())
					.asBitmap()
					.placeholder(R.drawable._no_img)
					.error(R.drawable._no_img)
					.into(viewHolder.imgIv);
		}
		viewHolder.titleTv.setText(entity.getObject_title());
	}

	private void initListener(ViewHolder viewHolder) {
		viewHolder.chkBox.setOnCheckedChangeListener(mOnCheckedChangeListener);
	}

	class ViewHolder {
		private CheckBox chkBox;
		private ImageView imgIv;
		private TextView titleTv;

	}

}
