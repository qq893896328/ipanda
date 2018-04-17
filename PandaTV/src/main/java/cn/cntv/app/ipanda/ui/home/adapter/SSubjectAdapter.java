package cn.cntv.app.ipanda.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.SSubjectTypeEnum;
import cn.cntv.app.ipanda.ui.home.auto.view.AutoScrollViewPager;
import cn.cntv.app.ipanda.ui.home.entity.AdapterData;
import cn.cntv.app.ipanda.ui.home.entity.GroupData;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterBigImg;
import cn.cntv.app.ipanda.ui.home.entity.Interaction;
import cn.cntv.app.ipanda.ui.home.entity.InteractionOne;
import cn.cntv.app.ipanda.ui.home.entity.InteractionTwo;
import cn.cntv.app.ipanda.ui.home.entity.Live;
import cn.cntv.app.ipanda.ui.home.entity.SSLiveVideoList;
import cn.cntv.app.ipanda.ui.home.entity.SSVideoList;
import cn.cntv.app.ipanda.ui.home.entity.SSVideoList1;
import cn.cntv.app.ipanda.ui.home.entity.SSVideoList2;
import cn.cntv.app.ipanda.ui.home.entity.SSVideoList3;
import cn.cntv.app.ipanda.ui.home.entity.SSVideoList4;
import cn.cntv.app.ipanda.ui.home.entity.SSubjectAdapterLVList;
import cn.cntv.app.ipanda.ui.home.entity.SSubjectBanner;
import cn.cntv.app.ipanda.ui.home.entity.SSubjectVote;
import cn.cntv.app.ipanda.ui.home.listener.HomeViewPagerListener;
import cn.cntv.app.ipanda.ui.home.listener.SSubjectItem2Listener;
import cn.cntv.app.ipanda.ui.home.listener.SSubjectItemListener;
import cn.cntv.app.ipanda.utils.AutoUtils;
import cn.cntv.app.ipanda.utils.ViewHolder;
import cn.cntv.app.ipanda.view.HorizontalListView;
import cn.cntv.app.ipanda.view.PointView;

/**
 * @ClassName: SSubjectAdapter
 * @author Xiao JinLai
 * @Date Dec 31, 2015 8:07:34 PM
 * @Description：Home special subject adapter
 */
public class SSubjectAdapter extends MyBaseAdapter {

	private ArrayList<AdapterData> mDatas;

	private LayoutInflater mInflater;

	private SSubjectItemListener mListener;
	private SSubjectItem2Listener mItem2Listener;
	private int tTitleWidth;

	public SSubjectAdapter(Context context, ArrayList<AdapterData> datas) {

		super(context);

		this.mDatas = datas;

		this.mInflater = LayoutInflater.from(context);

		this.mListener = new SSubjectItemListener(context, mDatas);
		this.mItem2Listener = new SSubjectItem2Listener(context);

		this.mContext = context;
	}

	@Override
	public int getItemViewType(int position) {

		return mDatas.get(position).getAdapterType();
	}

	@Override
	public int getViewTypeCount() {

		return SSubjectTypeEnum.SSUBJECT_ADAPTER_COUNT.value();
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

	public void updateData(ArrayList<AdapterData> datas) {

		this.mDatas.clear();
		this.mDatas = datas;

		mListener.setData(datas);

		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (getItemViewType(position) == SSubjectTypeEnum.GROUP_TYPE.value()) {

			convertView = initGroup(convertView, position, parent);

		} else if (getItemViewType(position) == SSubjectTypeEnum.SSUBJECT_BANNER
				.value()) {

			convertView = initBanner(convertView, position, parent);
		} else if (getItemViewType(position) == SSubjectTypeEnum.BIGIMG_TYPE
				.value()) {

			convertView = initBigImg(convertView, position, parent);
		} else if (getItemViewType(position) == SSubjectTypeEnum.LIVE_TYPE
				.value()) {

			convertView = initSSubjectLive(convertView, position, parent);

		} else if (getItemViewType(position) == SSubjectTypeEnum.SSUBJECT_LIVE_LIST
				.value()) {

			convertView = initLiveList(convertView, position, parent);
		} else if (getItemViewType(position) == SSubjectTypeEnum.SSUBJECT_LIST1
				.value()) {

			convertView = initList1(convertView, position, parent);

		} else if (getItemViewType(position) == SSubjectTypeEnum.SSUBJECT_LIST2
				.value()) {

			convertView = initList2(convertView, position, parent);

		} else if (getItemViewType(position) == SSubjectTypeEnum.SSUBJECT_LIST3
				.value()) {

			convertView = initList3(convertView, position, parent);
		} else if (getItemViewType(position) == SSubjectTypeEnum.SSUBJECT_LIST4
				.value()) {

			convertView = initList4(convertView, position, parent);

		} else if (getItemViewType(position) == SSubjectTypeEnum.SSUBJECT_VOTEURL
				.value()) {

			convertView = initVoteUrl(convertView, position, parent);

		} else if (getItemViewType(position) == SSubjectTypeEnum.INTERACTIVEONE_TYPE
				.value()) {

			convertView = initHomeInteractiveOne(convertView, position, parent);

		} else if (getItemViewType(position) == SSubjectTypeEnum.INTERACTIVETWO_TYPE
				.value()) {

			convertView = initHomeInteractiveTwo(convertView, position, parent);
		}

		return convertView;
	}

	private View initGroup(View convertView, int position, ViewGroup parent) {

		convertView = getConverView(convertView, R.layout.home_group_item,
				parent);

		GroupData tGroupData = (GroupData) mDatas.get(position);

		LinearLayout tRlHomeGroup = ViewHolder.get(convertView,
				R.id.llHomeGroupItem);

		if (tGroupData.getTitle() == null || tGroupData.getTitle().equals("")) {

			tRlHomeGroup.setVisibility(View.GONE);
		} else {

			tRlHomeGroup.setVisibility(View.VISIBLE);

			TextView tTvHomeGroup = ViewHolder.get(convertView,
					R.id.tvHomeGroupItem);
			tTvHomeGroup.setText(tGroupData.getTitle());

			ImageView tIvHomeGroup = ViewHolder.get(convertView,
					R.id.ivHomeGroupItem);

			if (tGroupData.getImage() == null
					|| tGroupData.getImage().equals("")) {

				tIvHomeGroup.setVisibility(View.GONE);
			} else {

				tIvHomeGroup.setVisibility(View.VISIBLE);

				Glide.with(mContext)
						.load(tGroupData.getImage())
						.asBitmap()
						.placeholder(R.drawable.def_no_iv)
						.error(R.drawable.def_no_iv)
						.into(tIvHomeGroup);
			}
		}

		return convertView;
	}

	private View initBanner(View convertView, int position, ViewGroup parent) {

		convertView = getConverView(convertView, R.layout.ssubject_banner_item,
				parent);

		SSubjectBanner tBanner = (SSubjectBanner) mDatas.get(position);

		ImageView tIvBanner = (ImageView) convertView;

		Glide.with(mContext)
				.load(tBanner.getBannerimage())
				.asBitmap()
				.placeholder(R.drawable.def_no_iv)
				.error(R.drawable.def_no_iv)
				.into(tIvBanner);
		// tIvBanner.setTag(position + "," + 0);
		// tIvBanner.setOnClickListener(mListener);

		return convertView;
	}

	private View initBigImg(View convertView, int position, ViewGroup parent) {

		convertView = getConverView(convertView,
				R.layout.view_banner_viewpager, parent);

		HomeAdapterBigImg tBigImg = (HomeAdapterBigImg) mDatas.get(position);
		int tBigImgSize = tBigImg.getBigImgs().size();

		AutoScrollViewPager tBigImgPager = ViewHolder.get(convertView,
				R.id.viewPager);

		PointView tPointView = ViewHolder.get(convertView, R.id.pointview);
		tBigImgPager.addOnPageChangeListener(new HomeViewPagerListener(
				tPointView));
if(tBigImgSize<2){
	 tTitleWidth = tPointView.setPointCount(tBigImgSize-1);
}else{
		 tTitleWidth = tPointView.setPointCount(tBigImgSize);}

		tBigImgPager.setAdapter(new AutoScrollAdapter(tBigImg.getBigImgs(),
				mContext, mListener, tTitleWidth, position));
		tBigImgPager.startAutoScroll();

		tPointView.setCurrentIndex(0);
		tBigImgPager.setCurrentItem(0);

		return convertView;
	}

	private View initSSubjectLive(View convertView, int position,
			ViewGroup parent) {

		convertView = getConverView(convertView, R.layout.home_live_item,
				parent);

		Live tLive = (Live) mDatas.get(position);

		LinearLayout tLlLive = ViewHolder.get(convertView, R.id.llHomeLive);
		tLlLive.setTag(position + "," + 0);
		tLlLive.setOnClickListener(mListener);

		ImageView tIvLive = ViewHolder.get(convertView, R.id.ivHomeLive);

		Glide.with(mContext)
				.load(tLive.getImage())
				.asBitmap()
				.placeholder(R.drawable.def_no_iv)
				.error(R.drawable.def_no_iv)
				.into(tIvLive);

		TextView tTvLiveTitle = ViewHolder.get(convertView,
				R.id.tvHomeLiveTitle);
		tTvLiveTitle.setText(tLive.getTitle());

		return convertView;
	}

	private View initLiveList(View convertView, int position, ViewGroup parent) {

		convertView = getConverView(convertView, R.layout.home_hotlive_item,
				parent);

		SSubjectAdapterLVList tLiveList = (SSubjectAdapterLVList) mDatas
				.get(position);
		List<SSLiveVideoList> tLiveLists = tLiveList.getLiveVideoLists();

		LinearLayout tLlhotLiveGroup1 = ViewHolder.get(convertView,
				R.id.llHomeHLGroup1);
		tLlhotLiveGroup1.setTag(position + "," + 0);
		tLlhotLiveGroup1.setOnClickListener(mListener);

		ImageView tIvHotLive1 = ViewHolder.get(convertView,
				R.id.ivHomeHLBigImg1);

		Glide.with(mContext)
				.load(tLiveLists.get(0).getImage())
				.asBitmap()
				.placeholder(R.drawable.def_no_iv)
				.error(R.drawable.def_no_iv)
				.into(tIvHotLive1);

		TextView tTvHotLive1 = ViewHolder.get(convertView, R.id.tvHomeHLTitle1);
		tTvHotLive1.setText(tLiveLists.get(0).getTitle());
		tTvHotLive1.setTag(position + "," + 0);
		tTvHotLive1.setOnClickListener(mListener);

		LinearLayout tLlhotLiveGroup2 = ViewHolder.get(convertView,
				R.id.llHomeHLGroup2);
		LinearLayout tLlhotLiveGroup3 = ViewHolder.get(convertView,
				R.id.llHomeHLGroup3);

		if (tLiveLists.size() <= 1) {

			tLlhotLiveGroup2.setVisibility(View.GONE);
			tLlhotLiveGroup3.setVisibility(View.GONE);
		} else {

			tLlhotLiveGroup2.setVisibility(View.VISIBLE);
			tLlhotLiveGroup2.setTag(position + "," + 1);
			tLlhotLiveGroup2.setOnClickListener(mListener);

			ImageView tIvHotLive2 = ViewHolder.get(convertView,
					R.id.ivHomeHLBigImg2);

			Glide.with(mContext)
					.load(tLiveLists.get(1).getImage())
					.asBitmap()
					.placeholder(R.drawable.def_no_iv)
					.error(R.drawable.def_no_iv)
					.into(tIvHotLive2);
			tIvHotLive2.setTag(position + "," + 1);
			tIvHotLive2.setOnClickListener(mListener);

			TextView tTvHotLive2 = ViewHolder.get(convertView,
					R.id.tvHomeHLTitle2);
			tTvHotLive2.setText(tLiveLists.get(1).getTitle());
			tTvHotLive2.setTag(position + "," + 1);
			tTvHotLive2.setOnClickListener(mListener);

			if (tLiveLists.size() <= 2) {

				tLlhotLiveGroup2.setVisibility(View.GONE);

			} else {

				tLlhotLiveGroup3.setVisibility(View.VISIBLE);
				tLlhotLiveGroup3.setTag(position + "," + 2);
				tLlhotLiveGroup3.setOnClickListener(mListener);

				ImageView tIvHotLive3 = ViewHolder.get(convertView,
						R.id.ivHomeHLBigImg3);

				Glide.with(mContext)
						.load(tLiveLists.get(2).getImage())
						.asBitmap()
						.placeholder(R.drawable.def_no_iv)
						.error(R.drawable.def_no_iv)
						.into(tIvHotLive3);
				tIvHotLive3.setTag(position + "," + 2);
				tIvHotLive3.setOnClickListener(mListener);

				TextView tTvHotLive3 = ViewHolder.get(convertView,
						R.id.tvHomeHLTitle3);
				tTvHotLive3.setText(tLiveLists.get(2).getTitle());
				tTvHotLive3.setTag(position + "," + 2);
				tTvHotLive3.setOnClickListener(mListener);

			}
		}

		return convertView;
	}

	private View initList1(View convertView, int position, ViewGroup parent) {

		convertView = getConverView(convertView, R.layout.home_videolist1_item,
				parent);

		SSVideoList1 tVideoList = (SSVideoList1) mDatas.get(position);

		List<SSVideoList> tVideoLists = tVideoList.getVideoLists1();

		LinearLayout tLlHomeVLGroup1 = ViewHolder.get(convertView,
				R.id.llHomeVLGroup1);
		tLlHomeVLGroup1.setTag(position + ",0");
		tLlHomeVLGroup1.setOnClickListener(mListener);

		SSVideoList tVideoList1 = tVideoLists.get(0);

		// layout one
		ImageView tIvHomeVLBigImg1 = ViewHolder.get(convertView,
				R.id.ivHomeVLBigImg1);

		Glide.with(mContext)
				.load(tVideoList1.getImage())
				.asBitmap()
				.placeholder(R.drawable.def_no_iv)
				.error(R.drawable.def_no_iv)
				.into(tIvHomeVLBigImg1);

		RelativeLayout tRlPlayTime1 = ViewHolder.get(convertView,
				R.id.rlHomeVLPlayTime1);

		String tVideoLength = tVideoList1.getVideoLength();

		if (tVideoLength == null || tVideoLength.equals("")) {

			tRlPlayTime1.setVisibility(View.GONE);
		} else {

			tRlPlayTime1.setVisibility(View.VISIBLE);

			TextView tTvHomeVlPlayTime1 = ViewHolder.get(convertView,
					R.id.tvHomeVlPlayTime1);
			tTvHomeVlPlayTime1.setText(tVideoLength);
		}

		TextView tTvHomeVlTitle1 = ViewHolder.get(convertView,
				R.id.tvHomeVlTitle1);
		tTvHomeVlTitle1.setText(tVideoList1.getTitle());

		LinearLayout tLlHomeVLGroup2 = ViewHolder.get(convertView,
				R.id.llHomeVLGroup2);

		if (tVideoLists.size() == 1) {

			tLlHomeVLGroup2.setVisibility(View.GONE);
		} else {

			tLlHomeVLGroup2.setVisibility(View.VISIBLE);
			tLlHomeVLGroup2.setTag(position + "," + 1);
			tLlHomeVLGroup2.setOnClickListener(mListener);

			SSVideoList tVideoList2 = tVideoLists.get(1);

			// layout two
			ImageView tIvHomeVLBigImg2 = ViewHolder.get(convertView,
					R.id.ivHomeVLBigImg2);

			Glide.with(mContext)
					.load(tVideoList2.getImage())
					.asBitmap()
					.placeholder(R.drawable.def_no_iv)
					.error(R.drawable.def_no_iv)
					.into(tIvHomeVLBigImg2);

			RelativeLayout tRlPlayTime2 = ViewHolder.get(convertView,
					R.id.rlHomeVLPlayTime2);

			String tVideoLength2 = tVideoList2.getVideoLength();

			if (tVideoLength2 == null || tVideoLength2.equals("")) {

				tRlPlayTime2.setVisibility(View.GONE);
			} else {

				tRlPlayTime2.setVisibility(View.VISIBLE);

				TextView tTvHomeVlPlayTime2 = ViewHolder.get(convertView,
						R.id.tvHomeVlPlayTime2);
				tTvHomeVlPlayTime2.setText(tVideoLength2);
			}

			TextView tTvHomeVlTitle2 = ViewHolder.get(convertView,
					R.id.tvHomeVlTitle2);
			tTvHomeVlTitle2.setText(tVideoList2.getTitle());
		}
		return convertView;
	}

	private View initList2(View convertView, int position, ViewGroup parent) {

		convertView = getConverView(convertView, R.layout.ssubject_list2_item,
				parent);

		List<SSVideoList> tVideoLists = ((SSVideoList2) mDatas.get(position))
				.getLists2();

		HorizontalListView tHorizontalListView = ViewHolder.get(convertView,
				R.id.hlvItem);

		tHorizontalListView.setAdapter(new SSubjectList2Adapter(mInflater,mContext,
				tVideoLists));

		mItem2Listener.setData(tVideoLists);

		tHorizontalListView.setOnItemClickListener(mItem2Listener);

		return convertView;
	}

	private View initList3(View convertView, int position, ViewGroup parent) {

		convertView = getConverView(convertView, R.layout.home_videolist2_item,
				parent);

		SSVideoList3 tVideoList3 = (SSVideoList3) mDatas.get(position);

		SSVideoList tVideoList = tVideoList3.getVideoList3();

		LinearLayout tLlHomeVL2Group = ViewHolder.get(convertView,
				R.id.llHomeVL2Group);
		tLlHomeVL2Group.setTag(position + "," + 0);
		tLlHomeVL2Group.setOnClickListener(mListener);

		ImageView tIvHomeVL2BigImg = ViewHolder.get(convertView,
				R.id.ivHomeVL2BigImg);
		Glide.with(mContext)
				.load(tVideoList.getImage())
				.asBitmap()
				.placeholder(R.drawable.def_no_iv)
				.error(R.drawable.def_no_iv)
				.into(tIvHomeVL2BigImg);

		RelativeLayout tRlPlayTime = ViewHolder.get(convertView,
				R.id.rlHomeVL2PlayTime);

		String tVideoLength = tVideoList.getVideoLength();

		if (tVideoLength == null || tVideoLength.equals("")) {

			tRlPlayTime.setVisibility(View.GONE);
		} else {

			tRlPlayTime.setVisibility(View.VISIBLE);

			TextView tTvHomeVl2PlayTime = ViewHolder.get(convertView,
					R.id.tvHomeVl2PlayTime);
			tTvHomeVl2PlayTime.setText(tVideoLength);
		}

		TextView tTvHomeVl2Title = ViewHolder.get(convertView,
				R.id.tvHomeVl2Title);
		tTvHomeVl2Title.setText(tVideoList.getTitle());

		return convertView;
	}

	private View initList4(View convertView, int position, ViewGroup parent) {

		convertView = getConverView(convertView, R.layout.ssubject_list4_item,
				parent);

		SSVideoList4 tSSvideos = (SSVideoList4) mDatas.get(position);
		SSVideoList tSSvideo = tSSvideos.getVideoList4();

		LinearLayout tLlSSubList4Group = ViewHolder.get(convertView,
				R.id.llSSubList4Group);
		tLlSSubList4Group.setTag(position + "," + 0);
		tLlSSubList4Group.setOnClickListener(mListener);

		ImageView tIvBigImg = ViewHolder.get(convertView,
				R.id.ivSSubList4BigImg);

		if (tSSvideo.getImage() == null || tSSvideo.getImage().equals("")) {

			tIvBigImg.setVisibility(View.GONE);
		} else {

			tIvBigImg.setVisibility(View.VISIBLE);

			Glide.with(mContext)
					.load(tSSvideo.getImage())
					.asBitmap()
					.placeholder(R.drawable.def_no_iv)
					.error(R.drawable.def_no_iv)
					.into(tIvBigImg);
		}

		TextView tTvTitle = ViewHolder.get(convertView, R.id.tvSSubList4Title);
		tTvTitle.setText(tSSvideo.getTitle());

		TextView tTvBrief = ViewHolder.get(convertView, R.id.tvSSubList4Brief);
		tTvBrief.setText(tSSvideo.getBrief());

		return convertView;
	}

	private View initVoteUrl(View convertView, int position, ViewGroup parent) {

		convertView = getConverView(convertView, R.layout.ssubject_vote_item,
				parent);

		SSubjectVote tVote = (SSubjectVote) mDatas.get(position);

		TextView tTvVoteTitle = ViewHolder
				.get(convertView, R.id.tvSSuVoteTitle);
		tTvVoteTitle.setText(tVote.getTitle());

		ImageView tIvVote = ViewHolder.get(convertView, R.id.ivSSuVoteImg);
		Glide.with(mContext)
				.load(tVote.getVote().getImage())
				.asBitmap()
				.placeholder(R.drawable.def_no_iv)
				.error(R.drawable.def_no_iv)
				.into(tIvVote);

		tIvVote.setTag(position + "," + 0);
		tIvVote.setOnClickListener(mListener);

		TextView tTvVote = ViewHolder.get(convertView, R.id.tvSSuVote);
		tTvVote.setTag(position + "," + 0);
		tTvVote.setOnClickListener(mListener);

		return convertView;
	}

	private View initHomeInteractiveOne(View convertView, int position,
			ViewGroup parent) {

		convertView = getConverView(convertView,
				R.layout.home_interactive_one_item, parent);

		InteractionOne tHomeInteractionOne = (InteractionOne) mDatas
				.get(position);

		ImageView tIvInterOne = ViewHolder
				.get(convertView, R.id.ivHomeInterOne);

		Glide.with(mContext)
				.load(tHomeInteractionOne.getInteraction()
						.getImage())
				.asBitmap()
				.placeholder(R.drawable.def_no_iv)
				.error(R.drawable.def_no_iv)
				.into(tIvInterOne);
		tIvInterOne.setTag(position + "," + 0);
		tIvInterOne.setOnClickListener(mListener);

		TextView tTvInterOne = ViewHolder.get(convertView, R.id.tvHomeInterOne);
		tTvInterOne.setText(tHomeInteractionOne.getInteraction().getTitle());
		tTvInterOne.setTag(position + "," + 0);
		tTvInterOne.setOnClickListener(mListener);

		return convertView;
	}
	//修改glide

	private View initHomeInteractiveTwo(View convertView, int position,
			ViewGroup parent) {

		convertView = getConverView(convertView,
				R.layout.home_interactive_two_item, parent);

		InteractionTwo tHomeInteractionTwo = (InteractionTwo) mDatas
				.get(position);

		List<Interaction> tInters = tHomeInteractionTwo.getInteraction();

		ImageView tIvInterTwo1 = ViewHolder.get(convertView,
				R.id.ivHomeInterTwo1);

		Glide.with(mContext)
				.load(tInters.get(0).getImage())
				.asBitmap()
				.placeholder(R.drawable.def_no_iv)
				.error(R.drawable.def_no_iv)
				.into(tIvInterTwo1);
		tIvInterTwo1.setTag(position + "," + 0);
		tIvInterTwo1.setOnClickListener(mListener);

		TextView tTvInterTwo1 = ViewHolder.get(convertView,
				R.id.tvHomeInterTwo1);
		tTvInterTwo1.setText(tInters.get(0).getTitle());
		tTvInterTwo1.setTag(position + "," + 0);
		tTvInterTwo1.setOnClickListener(mListener);

		LinearLayout tLlInterTwo = ViewHolder.get(convertView,
				R.id.llHomeInterTwoGroup);

		if (tInters.size() == 1) {

			tLlInterTwo.setVisibility(View.GONE);
		} else {

			tLlInterTwo.setVisibility(View.VISIBLE);

			ImageView tIvInterTwo2 = ViewHolder.get(convertView,
					R.id.ivHomeInterTwo2);

			Glide.with(mContext)
					.load(tInters.get(1).getImage())
					.asBitmap()
					.placeholder(R.drawable.def_no_iv)
					.error(R.drawable.def_no_iv)
					.into(tIvInterTwo2);
			tIvInterTwo2.setTag(position + "," + 1);
			tIvInterTwo2.setOnClickListener(mListener);

			TextView tTvInterTwo2 = ViewHolder.get(convertView,
					R.id.tvHomeInterTwo2);
			tTvInterTwo2.setText(tInters.get(1).getTitle());
			tTvInterTwo2.setTag(position + "," + 1);
			tTvInterTwo2.setOnClickListener(mListener);
		}
		return convertView;
	}

	/**
	 * 
	 * @param convertView
	 * @param layoutId
	 * @param parent
	 * @return
	 */
	private View getConverView(View convertView, int layoutId, ViewGroup parent) {

		if (convertView == null) {

			convertView = mInflater.inflate(layoutId, parent, false);
			AutoUtils.autoSize(convertView);
		}

		return convertView;
	}
}
