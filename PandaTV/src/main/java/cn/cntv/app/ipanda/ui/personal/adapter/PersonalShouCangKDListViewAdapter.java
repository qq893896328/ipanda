package cn.cntv.app.ipanda.ui.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gridsum.mobiledissector.util.StringUtil;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.CollectTypeEnum;
import cn.cntv.app.ipanda.db.entity.FavoriteEntity;
import cn.cntv.app.ipanda.utils.TimeUtil;

/**
 * @author： pj
 * @Date： 2016年1月15日 下午4:41:52
 * @Description:我的收藏：精彩看点列表适配器
 */
public class PersonalShouCangKDListViewAdapter extends BaseAdapter {

	private List<FavoriteEntity> mEntityList;
	private Context context;
	private OnCheckedChangeListener mOnCheckedChangeListener;

	public PersonalShouCangKDListViewAdapter(Context context,
			List<FavoriteEntity> entityList,
			OnCheckedChangeListener onCheckedChangeListener) {
		this.mEntityList = entityList;
		this.context = context;
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.personal_shoucang_kd_listview_item, null);
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
		viewHolder.chkBox = (CheckBox) convertView
				.findViewById(R.id.personal_chk);
		viewHolder.itemImg = (ImageView) convertView
				.findViewById(R.id.personal_hy_item_img);
		viewHolder.title = (TextView) convertView
				.findViewById(R.id.personal_hy_listview_title);
		viewHolder.noImgTitle = (TextView) convertView
				.findViewById(R.id.personal_hy_listview_title2);
		viewHolder.time = (TextView) convertView
				.findViewById(R.id.personal_hy_listview_time);
		viewHolder.videoLength = (TextView) convertView
				.findViewById(R.id.personal_sc_listview_videoLength);
	}

	private void setInfo(int position, FavoriteEntity entity,
			ViewHolder viewHolder, View view) {
		viewHolder.chkBox.setTag(entity);
		Boolean showEditUi = entity.getIsShowEditUi();
		if (showEditUi == null) showEditUi = false;
		viewHolder.chkBox.setVisibility(showEditUi ? View.VISIBLE
				: View.GONE);
		if (showEditUi) {
			viewHolder.chkBox.setChecked(entity.getIsChecked());
		}

		viewHolder.title.setVisibility(View.VISIBLE);
		viewHolder.noImgTitle.setVisibility(View.GONE);
		if (entity.getCollect_type() == CollectTypeEnum.TW.value()
				|| entity.getCollect_type() == CollectTypeEnum.LkTW.value()) {
			if (StringUtil.isNullOrEmpty(entity.getObject_logo())) {
				// 无图则不显示图片，右侧文字自动左对齐
				viewHolder.itemImg.setVisibility(View.GONE);
				viewHolder.videoLength.setVisibility(View.GONE);
				//viewHolder.title.setVisibility(View.GONE);
				viewHolder.noImgTitle.setVisibility(View.VISIBLE);
			} else {
				viewHolder.itemImg.setImageDrawable(null);
				viewHolder.itemImg.setVisibility(View.VISIBLE);
				viewHolder.videoLength.setVisibility(View.VISIBLE);

				Glide.with(context)
						.load(entity.getObject_logo())
						.placeholder(R.drawable._no_img)
						.error(R.drawable._no_img)
						.into(viewHolder.itemImg);
			}
		} else {
			viewHolder.itemImg.setImageDrawable(null);
			viewHolder.itemImg.setVisibility(View.VISIBLE);
			viewHolder.videoLength.setVisibility(View.VISIBLE);

			Glide.with(context)
					.load(entity.getObject_logo())
					.placeholder(R.drawable._no_img)
					.error(R.drawable._no_img)
					.into(viewHolder.itemImg);

		}
		if(entity.getFlag() != null && entity.getFlag() ==9){
			viewHolder.noImgTitle.setVisibility(View .VISIBLE);
			viewHolder.noImgTitle.setText(entity.getObject_title2());
			viewHolder.time.setVisibility(View.GONE);

		}else{
			viewHolder.noImgTitle.setVisibility(View.GONE);
			viewHolder.time.setVisibility(View.VISIBLE);
			viewHolder.time.setText(TimeUtil.getTime4(entity.getCollect_date()));

		}
		if (StringUtil.isNullOrEmpty(entity.getVideoLength())) {
			viewHolder.videoLength.setVisibility(View.GONE);
		} else {
			viewHolder.videoLength.setVisibility(View.VISIBLE);

			viewHolder.videoLength.setText(TimeUtil.getTime3(entity.getVideoLength()));
		}

		viewHolder.title.setText(entity.getObject_title());

	}

	private void initListener(ViewHolder viewHolder) {
		viewHolder.chkBox.setOnCheckedChangeListener(mOnCheckedChangeListener);
	}

	class ViewHolder {
		private ImageView itemImg;
		private CheckBox chkBox;
		private TextView videoLength, title, time, noImgTitle;
	}

}
