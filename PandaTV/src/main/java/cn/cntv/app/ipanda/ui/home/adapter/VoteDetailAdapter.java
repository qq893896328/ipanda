package cn.cntv.app.ipanda.ui.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.VoteTypeEnum;
import cn.cntv.app.ipanda.ui.home.entity.AdapterData;
import cn.cntv.app.ipanda.ui.home.entity.GroupData;
import cn.cntv.app.ipanda.ui.home.entity.VoteHead;
import cn.cntv.app.ipanda.ui.home.entity.VoteOptionValue;
import cn.cntv.app.ipanda.utils.AutoUtils;
import cn.cntv.app.ipanda.utils.ComonUtils;
import cn.cntv.app.ipanda.utils.TimeUtil;
import cn.cntv.app.ipanda.utils.ViewHolder;

/**
 * @ClassName: InteractionAdapter
 * @author Xiao JinLai
 * @Date Jan 5, 2016 1:39:11 PM
 * @Description：Interaction adapter
 */
public class VoteDetailAdapter extends MyBaseAdapter {

	protected static final String TAG = null;
	private List<AdapterData> mDatas;
	private Context mContext;
	private String[] mIetter = new String[] { "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N" };

	public VoteDetailAdapter(Context context, List<AdapterData> datas) {
		super(context);

		this.mDatas = datas;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return mDatas == null ? 0 : mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void updateData(List<AdapterData> datas) {

		this.mDatas.clear();
		this.mDatas = datas;
		notifyDataSetInvalidated();
	}

	@Override
	public int getItemViewType(int position) {

		return mDatas.get(position).getAdapterType();
	}

	@Override
	public int getViewTypeCount() {

		return VoteTypeEnum.VOTE_ADAPTER_COUNT.value();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (getItemViewType(position) == VoteTypeEnum.VOTE_GROUP.value()) {

			convertView = initGroup(convertView, position, parent);
		} else if (getItemViewType(position) == VoteTypeEnum.VOTE_HEAD.value()) {

			convertView = initHead(convertView, position, parent);
		} else if (getItemViewType(position) == VoteTypeEnum.VOTE_OPTION_VALUE
				.value()) {

			convertView = initOptionValue(convertView, position, parent);
		}

		return convertView;
	}

	private View initGroup(View convertView, int position, ViewGroup parent) {

		convertView = getConverView(convertView, R.layout.vote_group_item,
				parent);

		GroupData tGroupData = (GroupData) mDatas.get(position);

		LinearLayout tRlHomeGroup = ViewHolder.get(convertView,
				R.id.llVoteGroupItem);

		if (tGroupData.getTitle() == null || tGroupData.getTitle().equals("")) {

			tRlHomeGroup.setVisibility(View.GONE);
		} else {

			tRlHomeGroup.setVisibility(View.VISIBLE);

			TextView tTvHomeGroup = ViewHolder.get(convertView,
					R.id.tvVoteGroupItem);
			tTvHomeGroup.setText(tGroupData.getTitle());
		}

		return convertView;
	}

	private View initHead(View convertView, int position, ViewGroup parent) {

		convertView = getConverView(convertView, R.layout.vote_head_item,
				parent);

		VoteHead tVoteHead = (VoteHead) mDatas.get(position);

		TextView tTvVoteHeadTitle = ViewHolder.get(convertView,
				R.id.tvVoteHeadTitle);
		tTvVoteHeadTitle.setText(tVoteHead.getTitle());

		TextView tTvVoteHeadDate = ViewHolder.get(convertView,
				R.id.tvVoteHeadDate);
		tTvVoteHeadDate.setText(TimeUtil.convertString2DateString(tVoteHead
				.getDate()));

		TextView tTvVoteHeadDesc = ViewHolder.get(convertView,
				R.id.tvVoteHeadDesc);
		tTvVoteHeadDesc.setText(tVoteHead.getDesc());

		ImageView tIvVoteHeadImg = ViewHolder.get(convertView,
				R.id.ivVoteHeadImg);
		
		if(null == tVoteHead.getImage() || tVoteHead.getImage().trim().length() == 0){
			tIvVoteHeadImg.setVisibility(View.GONE);
		}else{

			Glide.with(mContext)
					.load(tVoteHead.getImage())
					.asBitmap()
					.placeholder(R.drawable.def_no_iv)
					.error(R.drawable.def_no_iv)
					.into(tIvVoteHeadImg);
		}
		

		return convertView;
	}

	private View initOptionValue(View convertView, int position,
			ViewGroup parent) {

		convertView = getConverView(convertView,
				R.layout.vote_option_detail_item, parent);

		VoteOptionValue tOption = (VoteOptionValue) mDatas.get(position);

		TextView tTvIetter = ViewHolder.get(convertView,
				R.id.tvVoteOptiValueIetter);
		tTvIetter.setText(mIetter[tOption.getIetter()]);

		TextView tTvTitle = ViewHolder.get(convertView,
				R.id.tvVoteOptiValueTitle);
		tTvTitle.setText(tOption.getTitle());

		TextView tTvPCT = ViewHolder.get(convertView, R.id.tvVoteOptiValuePCT);
		tTvPCT.setText(tOption.getPct());

		TextView tTvTik = ViewHolder.get(convertView, R.id.tvVoteOptiValueTik);
		tTvTik.setText(tOption.getTik());

		LinearLayout tLayout = ViewHolder.get(convertView, R.id.llVoteNum);

		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		tLayout.measure(w, h);

		int tNumWidth = tLayout.getMeasuredWidth();
		int tDisWidth = ComonUtils.getDisplayWidth((Activity) mContext);
		int tVoteWidth = (int) ((tDisWidth - tNumWidth - 20)
				* tOption.getWidth() / 100);

		TextView tTvPrb = ViewHolder.get(convertView, R.id.tvVotePrb);
		tTvPrb.setWidth(tVoteWidth);

		LinearLayout.LayoutParams tParams = (LayoutParams) tTvPrb
				.getLayoutParams();
		tParams.width = tVoteWidth;

		return convertView;
	}

	/**
	 * Get layout files
	 * 
	 * @param convertView
	 * @param layoutId
	 * @param parent
	 * @return
	 */
	private View getConverView(View convertView, int layoutId, ViewGroup parent) {

		if (convertView == null) {

			convertView = LayoutInflater.from(mContext).inflate(layoutId,
					parent, false);
			AutoUtils.autoSize(convertView);
		}

		return convertView;
	}
}
