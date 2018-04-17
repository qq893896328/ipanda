package cn.cntv.app.ipanda.ui.home.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.VoteTypeEnum;
import cn.cntv.app.ipanda.ui.home.entity.AdapterData;
import cn.cntv.app.ipanda.ui.home.entity.GroupData;
import cn.cntv.app.ipanda.ui.home.entity.VoteHead;
import cn.cntv.app.ipanda.ui.home.entity.VoteOption;
import cn.cntv.app.ipanda.ui.home.entity.VoteSGData;
import cn.cntv.app.ipanda.ui.home.listener.VoteListener;
import cn.cntv.app.ipanda.utils.AutoUtils;
import cn.cntv.app.ipanda.utils.TimeUtil;
import cn.cntv.app.ipanda.utils.ViewHolder;

/**
 * @ClassName: InteractionAdapter
 * @author Xiao JinLai
 * @Date Jan 5, 2016 1:39:11 PM
 * @Description：Interaction adapter
 */
public class VoteAdapter extends MyBaseAdapter {

	private List<AdapterData> mDatas;

	private SparseArray<RadioGroup> mRadioGroups = new SparseArray<RadioGroup>();
	private HashMap<String, String> mComitValue = new HashMap<String, String>();

	private VoteListener mListener;

	public VoteAdapter(Context context, List<AdapterData> datas) {
		super(context);

		this.mDatas = datas;
		this.mContext = context;
		this.mListener = (VoteListener) mContext;
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
		} else if (getItemViewType(position) == VoteTypeEnum.VOTE_OPTION
				.value()) {

			convertView = initOption(convertView, position, parent);
		} else if (getItemViewType(position) == VoteTypeEnum.VOTE_FOOT.value()) {

			convertView = initFoot(convertView, position, parent);
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

	private View initOption(View convertView, int position, ViewGroup parent) {

		convertView = getConverView(convertView, R.layout.vote_option_item,
				parent);

		final VoteOption tOption = (VoteOption) mDatas.get(position);

		RadioGroup tRgVoteur = ViewHolder.get(convertView, R.id.rgVoteur);

		mRadioGroups.put(tOption.getKey(), tRgVoteur);

		final List<VoteSGData> tSgDatas = tOption.getVoteSGData();

		tRgVoteur.removeAllViews();

		tRgVoteur.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				for (int i = 0; i < tSgDatas.size(); i++) {

					int tId = i + 10;

					if (checkedId == tId) {

						tSgDatas.get(i).setCheck(true);
						String tKey = "itemid[" + tOption.getId() + "]";
						mComitValue.put(tKey, tSgDatas.get(i).getId());
					} else {

						tSgDatas.get(i).setCheck(false);
					}
				}
			}
		});

		for (int i = 0; i < tSgDatas.size(); i++) {

			int tId = i + 10;
			RadioButton tRadioButton = new RadioButton(tRgVoteur.getContext());
			tRadioButton.setId(tId);
			tRadioButton.setText(tSgDatas.get(i).getTitle());
			tRadioButton.setChecked(tSgDatas.get(i).isCheck());
			tRgVoteur.addView(tRadioButton);
		}

		return convertView;
	}

	private View initFoot(View convertView, int position, ViewGroup parent) {

		convertView = getConverView(convertView, R.layout.vote_foot_item,
				parent);

		Button tBtnComit = ViewHolder.get(convertView, R.id.btnVoteComit);
		tBtnComit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mListener.comitVote(mComitValue);

			}
		});

		Button tBtnReelect = ViewHolder.get(convertView, R.id.btnVoteReelect);
		tBtnReelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				for (int i = 0; i < mRadioGroups.size(); i++) {

					RadioGroup tRadioGroup = mRadioGroups.valueAt(i);
					tRadioGroup.clearCheck();
				}

				mComitValue.clear();
			}
		});

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
