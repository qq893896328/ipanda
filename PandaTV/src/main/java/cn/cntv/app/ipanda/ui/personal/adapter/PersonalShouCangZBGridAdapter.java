package cn.cntv.app.ipanda.ui.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.db.entity.FavoriteEntity;

/**
 * @author pj
 * @Date： 2016年1月14日 下午5:51:53
 * @Description:我的收藏页签一顶部表格
 */
public class PersonalShouCangZBGridAdapter extends BaseAdapter {
	// 搜索结果界面的gridview的adapter
	private Context context;
	private List<FavoriteEntity> mDatas;
	private LayoutInflater inflater;

	private int displayHeight;
	private int displayWidth;

	public PersonalShouCangZBGridAdapter(Context context,
			List<FavoriteEntity> datas, int displayWidth, int displayHeight) {
		this.context = context;
		this.mDatas = mDatas;
		inflater = LayoutInflater.from(context);
		this.displayHeight = displayHeight;
		this.displayWidth = displayWidth;

	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		FavoriteEntity entity = mDatas.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.personal_shoucang_grid_item, parent, false);
			initView(convertView, holder);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		setInfo(position, entity, holder, convertView);
		return convertView;
	}

	private void initView(View convertView, ViewHolder holder) {
		holder.imgIv = (ImageView) convertView
				.findViewById(R.id.personal_shoucang_grid_item_img);
		holder.videoLengthTv = (TextView) convertView
				.findViewById(R.id.personal_shoucang_grid_videoLength);
		holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
		holder.chkBox = (CheckBox) convertView
				.findViewById(R.id.personal_shoucang_grid_state_img);
	}

	private void setInfo(int position, FavoriteEntity entity,
			ViewHolder viewHolder, View view) {
		viewHolder.imgIv.setImageDrawable(null);

		Glide.with(context)
				.load(entity.getObject_logo())
				.placeholder(R.drawable.def_no_iv)
				.error(R.drawable.def_no_iv)
				.into(viewHolder.imgIv);
		viewHolder.titleTv.setText(entity.getObject_title());
		viewHolder.videoLengthTv.setText(entity.getVideoLength());
	}

	class ViewHolder {
		private ImageView imgIv;
		private TextView videoLengthTv;
		private TextView titleTv;
		private CheckBox chkBox;
	}

}
