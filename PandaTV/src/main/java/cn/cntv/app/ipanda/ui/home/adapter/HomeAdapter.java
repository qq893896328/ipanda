package cn.cntv.app.ipanda.ui.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
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
import cn.cntv.app.ipanda.constant.HomeTypeEnum;
import cn.cntv.app.ipanda.ui.home.auto.view.AutoScrollViewPager;
import cn.cntv.app.ipanda.ui.home.entity.AdapterData;
import cn.cntv.app.ipanda.ui.home.entity.GroupData;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterArea;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterBigImg;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterChinaLive;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterLive;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterPandaLive;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterVideoList1;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterVideoList2;
import cn.cntv.app.ipanda.ui.home.entity.HomeAdapterWallLive;
import cn.cntv.app.ipanda.ui.home.entity.HomeHotLiveList;
import cn.cntv.app.ipanda.ui.home.entity.HomeLive2;
import cn.cntv.app.ipanda.ui.home.entity.HomeLiveList;
import cn.cntv.app.ipanda.ui.home.entity.HomePandaEye;
import cn.cntv.app.ipanda.ui.home.entity.HomePandaEye2;
import cn.cntv.app.ipanda.ui.home.entity.HomePandaEyeItem;
import cn.cntv.app.ipanda.ui.home.entity.HomeRecoSSubject;
import cn.cntv.app.ipanda.ui.home.entity.HomeStyle1;
import cn.cntv.app.ipanda.ui.home.entity.HomeStyle2;
import cn.cntv.app.ipanda.ui.home.entity.HomeStyleData;
import cn.cntv.app.ipanda.ui.home.entity.HomeVideoList;
import cn.cntv.app.ipanda.ui.home.entity.Interaction;
import cn.cntv.app.ipanda.ui.home.entity.InteractionOne;
import cn.cntv.app.ipanda.ui.home.entity.InteractionTwo;
import cn.cntv.app.ipanda.ui.home.entity.SSVideoList;
import cn.cntv.app.ipanda.ui.home.listener.HomeItemListener;
import cn.cntv.app.ipanda.ui.home.listener.HomeViewPagerListener;
import cn.cntv.app.ipanda.ui.home.listener.SSubjectItem2Listener;
import cn.cntv.app.ipanda.utils.AutoUtils;
import cn.cntv.app.ipanda.utils.MatcherUtils;
import cn.cntv.app.ipanda.utils.ViewHolder;
import cn.cntv.app.ipanda.view.BorderTextView;
import cn.cntv.app.ipanda.view.HorizontalListView;
import cn.cntv.app.ipanda.view.PointView;

/**
 * @author Xiao JinLai
 * @ClassName: HomeAdapter
 * @Date Dec 23, 2015 5:57:13 PM
 * @Description：
 */
public class HomeAdapter extends MyBaseAdapter {

    private ArrayList<AdapterData> mDatas;

    private LayoutInflater mInflater;

    private HomeItemListener mListener;
    private SSubjectItem2Listener mItem2Listener;

    public HomeAdapter(Context context, ArrayList<AdapterData> datas) {

        super(context);

        this.mDatas = datas;

        this.mInflater = LayoutInflater.from(context);

        this.mListener = new HomeItemListener(context, mDatas);
        this.mItem2Listener = new SSubjectItem2Listener(context);

        this.mContext = context;

    }

    @Override
    public int getItemViewType(int position) {

        return mDatas.get(position).getAdapterType();
    }

    @Override
    public int getViewTypeCount() {

        return HomeTypeEnum.HOME_ADAPTER_COUNT.value();
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

        mListener.setDatas(datas);

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (getItemViewType(position) == HomeTypeEnum.GROUP_TYPE.value()) {

            convertView = initGroup(convertView, position, parent);
        } else if (getItemViewType(position) == HomeTypeEnum.BIGIMG_TYPE
                .value()) {
            //首页大图下方模块
            convertView = initBigImg(convertView, position, parent);
        } else if (getItemViewType(position) == HomeTypeEnum.HOME_AREA_TYPE
                .value()) {

            convertView = initHomeArea(convertView, position, parent);
        } else if (getItemViewType(position) == HomeTypeEnum.HOME_VIDEOLIST1_TYPE
                .value()) {

            convertView = initHomeVideo1(convertView, position, parent);
        } else if (getItemViewType(position) == HomeTypeEnum.HOME_VIDEOLIST2_TYPE
                .value()) {
            //专题页
            convertView = initHomeVideo2(convertView, position, parent);
        } else if (getItemViewType(position) == HomeTypeEnum.HOME_SSUBJECT_TYPE
                .value()) {

            convertView = initHomeSSubject(convertView, position, parent);
        } else if (getItemViewType(position) == HomeTypeEnum.HOME_PANDAEYE_TYPE
                .value()) {

            convertView = initHomePandaEye(convertView, position, parent);
        } else if (getItemViewType(position) == HomeTypeEnum.HOME_PANDAEYE2_TYPE
                .value()) {

            convertView = initHomePandaEye2(convertView, position, parent);
        } else if (getItemViewType(position) == HomeTypeEnum.HOME_PANDALIVE_TYPE
                .value()) {

            convertView = initHomeHotLive(convertView, position, parent);
        } else if (getItemViewType(position) == HomeTypeEnum.HOME_GREATWALL_TYPE
                .value()) {

            convertView = initHomeGradtWall(convertView, position, parent);
        } else if (getItemViewType(position) == HomeTypeEnum.HOME_LIVECHINA_TYPE
                .value()) {

            convertView = initHomeLiveChina(convertView, position, parent);
        } else if (getItemViewType(position) == HomeTypeEnum.INTERACTIVEONE_TYPE
                .value()) {
            //特别策划1
            convertView = initHomeInteractiveOne(convertView, position, parent);
        } else if (getItemViewType(position) == HomeTypeEnum.INTERACTIVETWO_TYPE
                .value()) {
            //特别策划2
            convertView = initHomeInteractiveTwo(convertView, position, parent);
        } else if (getItemViewType(position) == HomeTypeEnum.HOME_LIVE_TYPE
                .value()) {
            //CCTV模块1
            convertView = initHomeLive(convertView, position, parent);
        } else if (getItemViewType(position) == HomeTypeEnum.HOME_LIVE2_TYPE
                .value()) {
            //CCTV模块2
            convertView = initHomeLive2(convertView, position, parent);
        } else if (getItemViewType(position) == HomeTypeEnum.HOME_STYLE1_TYPE
                .value()) {
            //专题推荐1
            convertView = initHomeStyle1(convertView, position, parent);
        } else if (getItemViewType(position) == HomeTypeEnum.HOME_STYLE2_TYPE
                .value()) {
            //专题推荐2
            convertView = initHomeStyle2(convertView, position, parent);
        }

        return convertView;
    }

    private View initGroup(View convertView, int position, ViewGroup parent) {

        convertView = getConverView(convertView, R.layout.home_group_item,
                parent);

        GroupData tGroupData = (GroupData) mDatas.get(position);

        LinearLayout tRlHomeGroup = ViewHolder.get(convertView,
                R.id.llHomeGroupItem);
        tRlHomeGroup.setTag(position + "," + 0);
        tRlHomeGroup.setOnClickListener(mListener);

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

    private View initBigImg(View convertView, int position, ViewGroup parent) {

        if (convertView != null) {
            return convertView;
        }
        convertView = getConverView(convertView,
                R.layout.view_banner_viewpager, parent);

        HomeAdapterBigImg tBigImg = (HomeAdapterBigImg) mDatas.get(position);
        int tBigImgSize = tBigImg.getBigImgs().size();

        AutoScrollViewPager tBigImgPager = ViewHolder.get(convertView,
                R.id.viewPager);

        PointView tPointView = ViewHolder.get(convertView, R.id.pointview);
        tBigImgPager.addOnPageChangeListener(new HomeViewPagerListener(
                tPointView));

        int tTitleWidth = tPointView.setPointCount(tBigImgSize);

        tBigImgPager.setAdapter(new AutoScrollAdapter(tBigImg.getBigImgs(),
                mContext, mListener, tTitleWidth, position));
        tBigImgPager.startAutoScroll();

        tPointView.setCurrentIndex(0);
        tBigImgPager.setCurrentItem(0);

        return convertView;
    }

    private View initHomeArea(View convertView, int position, ViewGroup parent) {

        if (convertView != null) {
            return convertView;
        }
        convertView = getConverView(convertView, R.layout.ssubject_list2_item,
                parent);

        List<SSVideoList> tVideoLists = ((HomeAdapterArea) mDatas.get(position))
                .getList();

        HorizontalListView tHorizontalListView = ViewHolder.get(convertView,
                R.id.hlvItem);

        tHorizontalListView.setAdapter(new SSubjectList2Adapter(mInflater, mContext,
                tVideoLists));
        mItem2Listener.setData(tVideoLists);
        AdapterData adapterData = mDatas.get(position - 1);
        if (adapterData instanceof GroupData) {
            mItem2Listener.setGroupTitle(((GroupData) adapterData).getTitle());
        }

        tHorizontalListView.setOnItemClickListener(mItem2Listener);

        return convertView;
    }

    private View initHomeLive(View convertView, int position, ViewGroup parent) {

        convertView = getConverView(convertView, R.layout.home_live_item2,
                parent);

        HomeAdapterLive tAdapterLive = (HomeAdapterLive) mDatas.get(position);

        List<HomeLiveList> tLiveLists = tAdapterLive.getHomeLiveLists();
        int tLiveSize = tLiveLists.size();
        if (position == 26) {
            RelativeLayout RL = ViewHolder.get(convertView, R.id.llLiveItem1);
            RL.setBackgroundResource(R.drawable.home_live_border3);
        }

        TextView tTvLiveItem1 = ViewHolder.get(convertView, R.id.tvLiveItem1);
        tTvLiveItem1.setText(tLiveLists.get(0).getTitle());
        tTvLiveItem1.setTag(position + "," + 0);
        tTvLiveItem1.setOnClickListener(mListener);

        if (tLiveSize > 1) {
            if (position == 26) {
                RelativeLayout RL = ViewHolder.get(convertView, R.id.llLiveItem2);
                RL.setBackgroundResource(R.drawable.home_live_border3);
            }

            TextView tTvLiveItem2 = ViewHolder.get(convertView,
                    R.id.tvLiveItem2);
            tTvLiveItem2.setText(tLiveLists.get(1).getTitle());
            tTvLiveItem2.setTag(position + "," + 1);
            tTvLiveItem2.setOnClickListener(mListener);

            if (tLiveSize > 2) {
                if (position == 26) {
                    RelativeLayout RL = ViewHolder.get(convertView, R.id.llLiveItem3);
                    RL.setBackgroundResource(R.drawable.home_live_border3);
                }

                TextView tTvLiveItem3 = ViewHolder.get(convertView,
                        R.id.tvLiveItem3);
                tTvLiveItem3.setText(tLiveLists.get(2).getTitle());
                tTvLiveItem3.setTag(position + "," + 2);
                tTvLiveItem3.setOnClickListener(mListener);

                if (tLiveSize > 3) {
                    if (position == 26) {
                        RelativeLayout RL = ViewHolder.get(convertView, R.id.llLiveItem4);
                        RL.setBackgroundResource(R.drawable.home_live_border3);
                    }

                    TextView tTvLiveItem4 = ViewHolder.get(convertView,
                            R.id.tvLiveItem4);
                    tTvLiveItem4.setText(tLiveLists.get(3).getTitle());
                    tTvLiveItem4.setTag(position + "," + 3);
                    tTvLiveItem4.setOnClickListener(mListener);

                    if (tLiveSize > 4) {
                        if (position == 26) {
                            RelativeLayout RL = ViewHolder.get(convertView, R.id.llLiveItem5);
                            RL.setBackgroundResource(R.drawable.home_live_border4);
                        }

                        TextView tTvLiveItem5 = ViewHolder.get(convertView,
                                R.id.tvLiveItem5);
                        tTvLiveItem5.setText(tLiveLists.get(4).getTitle());
                        tTvLiveItem5.setTag(position + "," + 4);
                        tTvLiveItem5.setOnClickListener(mListener);
                    }
                }
            }
        }

        return convertView;
    }

    private View initHomeVideo1(View convertView, int position, ViewGroup parent) {

        convertView = getConverView(convertView, R.layout.home_videolist1_item,
                parent);

        HomeAdapterVideoList1 tVideoList = (HomeAdapterVideoList1) mDatas
                .get(position);

        HomeVideoList tVideoList1 = tVideoList.getVideoLists().get(0);

        LinearLayout tLlHomeVLGroup1 = ViewHolder.get(convertView,
                R.id.llHomeVLGroup1);
        tLlHomeVLGroup1.setTag(position + ",0");
        tLlHomeVLGroup1.setOnClickListener(mListener);

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

        if (tVideoList.getVideoLists().size() > 1) {

            tLlHomeVLGroup2.setVisibility(View.VISIBLE);

            tLlHomeVLGroup2.setTag(position + ",1");
            tLlHomeVLGroup2.setOnClickListener(mListener);

            HomeVideoList tVideoList2 = tVideoList.getVideoLists().get(1);

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
        } else {

            tLlHomeVLGroup2.setVisibility(View.GONE);
        }

        return convertView;
    }

    private View initHomeVideo2(View convertView, int position, ViewGroup parent) {

        convertView = getConverView(convertView, R.layout.home_videolist2_item,
                parent);

        HomeAdapterVideoList2 tVideoList2 = (HomeAdapterVideoList2) mDatas
                .get(position);
        HomeVideoList tVideoList = tVideoList2.getVideoList();

        LinearLayout tLlHomeVL2Group = ViewHolder.get(convertView,
                R.id.llHomeVL2Group);
        tLlHomeVL2Group.setTag(position + ",0");
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

    private View initHomeSSubject(View convertView, int position,
                                  ViewGroup parent) {

        convertView = getConverView(convertView, R.layout.home_ssubject_item,
                parent);

        HomeRecoSSubject tSSubject = (HomeRecoSSubject) mDatas.get(position);

        RelativeLayout tRlHomeSSubject = ViewHolder.get(convertView,
                R.id.rlHomeSSubject);

        tRlHomeSSubject.setTag(position + "," + 0);
        tRlHomeSSubject.setOnClickListener(mListener);

        TextView tTvHomeSSubject = ViewHolder.get(convertView,
                R.id.tvHomeSSubject);
        tTvHomeSSubject.setText(tSSubject.getTitle() + ">");

        return convertView;
    }

    private View initHomePandaEye(View convertView, int position,
                                  ViewGroup parent) {

        convertView = getConverView(convertView, R.layout.home_panda_eye_item,
                parent);

        HomePandaEye tPandaEye = (HomePandaEye) mDatas.get(position);

        ImageView tIvPandaEyeIcon = ViewHolder.get(convertView,
                R.id.ivHomePandaEye);

        Glide.with(mContext)
                .load(tPandaEye.getPandaeyelogo())
                .asBitmap()
                .placeholder(R.drawable.def_no_iv)
                .error(R.drawable.def_no_iv)
                .into(tIvPandaEyeIcon);

        List<HomePandaEyeItem> tPandaEyeItems = tPandaEye.getItems();

        LinearLayout tLlPandaEyeGroup1 = ViewHolder.get(convertView,
                R.id.llHomePandaEyeGroup1);

        if (tPandaEyeItems == null || tPandaEyeItems.size() == 0) {

            tLlPandaEyeGroup1.setVisibility(View.GONE);
        } else {

            tLlPandaEyeGroup1.setVisibility(View.VISIBLE);

            HomePandaEyeItem tPandaEyeItem1 = tPandaEyeItems.get(0);
            String tTextColor = tPandaEyeItem1.getBgcolor();

            int tColor;

            if (MatcherUtils.isColor(tTextColor)) {

                tColor = Color.parseColor(tTextColor);
            } else {

                tColor = convertView.getContext().getResources()
                        .getColor(R.color.home_panda_eye_type);
            }

            BorderTextView tBtvPandaEyeType1 = ViewHolder.get(convertView,
                    R.id.btvPandaEyeTitleType1);
            tBtvPandaEyeType1.setTextColor(tColor);

            TextView tTvPandaEyeType1 = ViewHolder.get(convertView,
                    R.id.tvPandaEyeTitleType1);
            //TextUtils.isEmpty(tPandaEyeItem1.getBrief())
            if (!TextUtils.isEmpty(tPandaEyeItem1.getBrief())) {
                tTvPandaEyeType1.setText(tPandaEyeItem1.getBrief());
            } else {
                tTvPandaEyeType1.setText("时评");
            }

            tTvPandaEyeType1.setTextColor(tColor);

            TextView tTvPandaEyeTitle1 = ViewHolder.get(convertView,
                    R.id.tvPandaEyeTitle1);
            tTvPandaEyeTitle1.setText(tPandaEyeItem1.getTitle());
            tTvPandaEyeTitle1.setTag(position + "," + 0);
            tTvPandaEyeTitle1.setOnClickListener(mListener);

            LinearLayout tLlPandaEyeGroup2 = ViewHolder.get(convertView,
                    R.id.llHomePandaEyeGroup2);

            if (tPandaEyeItems.size() == 1) {

                tLlPandaEyeGroup2.setVisibility(View.GONE);
            } else {

                tLlPandaEyeGroup2.setVisibility(View.VISIBLE);

                HomePandaEyeItem tPandaEyeItem2 = tPandaEyeItems.get(1);
                String tTextColor2 = tPandaEyeItem2.getBgcolor();

                int tColor2;

                if (MatcherUtils.isColor(tTextColor2)) {

                    tColor2 = Color.parseColor(tTextColor2);
                } else {

                    tColor2 = convertView.getContext().getResources()
                            .getColor(R.color.home_panda_eye_type);
                }

                BorderTextView tBtvPandaEyeType2 = ViewHolder.get(convertView,
                        R.id.btvPandaEyeTitleType2);
                tBtvPandaEyeType2.setTextColor(tColor2);

                TextView tTvPandaEyeType2 = ViewHolder.get(convertView,
                        R.id.tvPandaEyeTitleType2);
                if (!TextUtils.isEmpty(tPandaEyeItem2.getBrief())) {
                    tTvPandaEyeType2.setText(tPandaEyeItem2.getBrief());
                }

                tTvPandaEyeType2.setTextColor(tColor2);

                TextView tTvPandaEyeTitle2 = ViewHolder.get(convertView,
                        R.id.tvPandaEyeTitle2);
                tTvPandaEyeTitle2.setText(tPandaEyeItem2.getTitle());
                tTvPandaEyeTitle2.setTag(position + "," + 1);
                tTvPandaEyeTitle2.setOnClickListener(mListener);

            }
        }
        return convertView;
    }

    private View initHomePandaEye2(View convertView, int position,
                                   ViewGroup parent) {

        convertView = getConverView(convertView, R.layout.home_style2_item,
                parent);

        HomePandaEye2 tPandaEye2 = (HomePandaEye2) mDatas.get(position);

        LinearLayout tLlHomeStyle2 = ViewHolder.get(convertView,
                R.id.llHomeSt2Group);
        tLlHomeStyle2.setTag(position + "," + 0);
        tLlHomeStyle2.setOnClickListener(mListener);

        ImageView tIvStyle2Img = ViewHolder.get(convertView,
                R.id.ivHomeSt2BigImg);

        Glide.with(mContext)
                .load(tPandaEye2.getImage())
                .asBitmap()
                .placeholder(R.drawable.def_no_iv)
                .error(R.drawable.def_no_iv)
                .into(tIvStyle2Img);

        RelativeLayout tRlPlayTime = ViewHolder.get(convertView,
                R.id.rlHomeSt2PlayTime);

        String tVideoLength = tPandaEye2.getVideoLength();

        if (tVideoLength == null || tVideoLength.equals("")) {

            tRlPlayTime.setVisibility(View.GONE);
        } else {

            tRlPlayTime.setVisibility(View.VISIBLE);

            TextView tTvHomeSt2PlayTime = ViewHolder.get(convertView,
                    R.id.tvHomeSt2PlayTime);
            tTvHomeSt2PlayTime.setText(tVideoLength);
        }

        TextView tTvStyle2Title = ViewHolder.get(convertView,
                R.id.tvHomeSt2Title);
        tTvStyle2Title.setText(tPandaEye2.getTitle());

        TextView tTvStyle2Date = ViewHolder
                .get(convertView, R.id.tvHomeSt2Date);
        tTvStyle2Date.setText(tPandaEye2.getDaytime());

        return convertView;
    }

    private View initHomeHotLive(View convertView, int position,
                                 ViewGroup parent) {

        convertView = getConverView(convertView, R.layout.home_hotlive_item,
                parent);

        HomeAdapterPandaLive tAdapterHotLive = (HomeAdapterPandaLive) mDatas
                .get(position);
        List<HomeHotLiveList> tHotlives = tAdapterHotLive.getHotlives();

        LinearLayout tLlhotLiveGroup1 = ViewHolder.get(convertView,
                R.id.llHomeHLGroup1);
        tLlhotLiveGroup1.setTag(position + "," + 0);
        tLlhotLiveGroup1.setOnClickListener(mListener);

        ImageView tIvHotLive1 = ViewHolder.get(convertView,
                R.id.ivHomeHLBigImg1);

        Glide.with(mContext)
                .load(tHotlives.get(0).getImage())
                .asBitmap()
                .placeholder(R.drawable.def_no_iv)
                .error(R.drawable.def_no_iv)
                .into(tIvHotLive1);

        TextView tTvHotLive1 = ViewHolder.get(convertView, R.id.tvHomeHLTitle1);
        tTvHotLive1.setText(tHotlives.get(0).getTitle());

        LinearLayout tLlhotLiveGroup2 = ViewHolder.get(convertView,
                R.id.llHomeHLGroup2);
        LinearLayout tLlhotLiveGroup3 = ViewHolder.get(convertView,
                R.id.llHomeHLGroup3);

        if (tHotlives.size() <= 1) {

            tLlhotLiveGroup2.setVisibility(View.GONE);
            tLlhotLiveGroup3.setVisibility(View.GONE);
        } else {

            tLlhotLiveGroup2.setVisibility(View.VISIBLE);
            tLlhotLiveGroup2.setTag(position + "," + 1);
            tLlhotLiveGroup2.setOnClickListener(mListener);

            ImageView tIvHotLive2 = ViewHolder.get(convertView,
                    R.id.ivHomeHLBigImg2);

            Glide.with(mContext)
                    .load(tHotlives.get(1).getImage())
                    .asBitmap()
                    .placeholder(R.drawable.def_no_iv)
                    .error(R.drawable.def_no_iv)
                    .into(tIvHotLive2);

            TextView tTvHotLive2 = ViewHolder.get(convertView,
                    R.id.tvHomeHLTitle2);
            tTvHotLive2.setText(tHotlives.get(1).getTitle());

            if (tHotlives.size() <= 2) {

                tLlhotLiveGroup3.setVisibility(View.GONE);

            } else {

                tLlhotLiveGroup3.setVisibility(View.VISIBLE);
                tLlhotLiveGroup3.setTag(position + "," + 2);
                tLlhotLiveGroup3.setOnClickListener(mListener);

                ImageView tIvHotLive3 = ViewHolder.get(convertView,
                        R.id.ivHomeHLBigImg3);

                Glide.with(mContext)
                        .load(tHotlives.get(2).getImage())
                        .asBitmap()
                        .placeholder(R.drawable.def_no_iv)
                        .error(R.drawable.def_no_iv)
                        .into(tIvHotLive3);

                TextView tTvHotLive3 = ViewHolder.get(convertView,
                        R.id.tvHomeHLTitle3);
                tTvHotLive3.setText(tHotlives.get(2).getTitle());

            }
        }

        return convertView;
    }

    private View initHomeGradtWall(View convertView, int position,
                                   ViewGroup parent) {
        convertView = getConverView(convertView, R.layout.home_hotlive_item,
                parent);

        HomeAdapterWallLive tAdapterHotLive = (HomeAdapterWallLive) mDatas
                .get(position);
        List<HomeHotLiveList> tHotlives = tAdapterHotLive.getHotlives();

        LinearLayout tLlhotLiveGroup1 = ViewHolder.get(convertView,
                R.id.llHomeHLGroup1);
        tLlhotLiveGroup1.setTag(position + "," + 0);
        tLlhotLiveGroup1.setOnClickListener(mListener);

        ImageView tIvHotLive1 = ViewHolder.get(convertView,
                R.id.ivHomeHLBigImg1);
        Glide.with(mContext)
                .load(tHotlives.get(0).getImage())
                .asBitmap()
                .placeholder(R.drawable.def_no_iv)
                .error(R.drawable.def_no_iv)
                .into(tIvHotLive1);

        TextView tTvHotLive1 = ViewHolder.get(convertView, R.id.tvHomeHLTitle1);
        tTvHotLive1.setText(tHotlives.get(0).getTitle());

        LinearLayout tLlhotLiveGroup2 = ViewHolder.get(convertView,
                R.id.llHomeHLGroup2);
        LinearLayout tLlhotLiveGroup3 = ViewHolder.get(convertView,
                R.id.llHomeHLGroup3);

        if (tHotlives.size() <= 1) {

            tLlhotLiveGroup2.setVisibility(View.GONE);
            tLlhotLiveGroup3.setVisibility(View.GONE);
        } else {

            tLlhotLiveGroup2.setVisibility(View.VISIBLE);
            tLlhotLiveGroup2.setTag(position + "," + 1);
            tLlhotLiveGroup2.setOnClickListener(mListener);

            ImageView tIvHotLive2 = ViewHolder.get(convertView,
                    R.id.ivHomeHLBigImg2);

            Glide.with(mContext)
                    .load(tHotlives.get(1).getImage())
                    .asBitmap()
                    .placeholder(R.drawable.def_no_iv)
                    .error(R.drawable.def_no_iv)
                    .into(tIvHotLive2);

            TextView tTvHotLive2 = ViewHolder.get(convertView,
                    R.id.tvHomeHLTitle2);
            tTvHotLive2.setText(tHotlives.get(1).getTitle());

            if (tHotlives.size() <= 2) {

                tLlhotLiveGroup3.setVisibility(View.GONE);

            } else {

                tLlhotLiveGroup3.setVisibility(View.VISIBLE);
                tLlhotLiveGroup3.setTag(position + "," + 2);
                tLlhotLiveGroup3.setOnClickListener(mListener);

                ImageView tIvHotLive3 = ViewHolder.get(convertView,
                        R.id.ivHomeHLBigImg3);

                Glide.with(mContext)
                        .load(tHotlives.get(2).getImage())
                        .asBitmap()
                        .placeholder(R.drawable.def_no_iv)
                        .error(R.drawable.def_no_iv)
                        .into(tIvHotLive3);


                TextView tTvHotLive3 = ViewHolder.get(convertView,
                        R.id.tvHomeHLTitle3);
                tTvHotLive3.setText(tHotlives.get(2).getTitle());

            }
        }

        return convertView;
    }

    private View initHomeLiveChina(View convertView, int position,
                                   ViewGroup parent) {
        convertView = getConverView(convertView, R.layout.home_hotlive_item,
                parent);

        HomeAdapterChinaLive tAdapterHotLive = (HomeAdapterChinaLive) mDatas
                .get(position);
        List<HomeHotLiveList> tHotlives = tAdapterHotLive.getHotlives();

        LinearLayout tLlhotLiveGroup1 = ViewHolder.get(convertView,
                R.id.llHomeHLGroup1);
        tLlhotLiveGroup1.setTag(position + "," + 0);
        tLlhotLiveGroup1.setOnClickListener(mListener);

        ImageView tIvHotLive1 = ViewHolder.get(convertView,
                R.id.ivHomeHLBigImg1);

        Glide.with(mContext)
                .load(tHotlives.get(0).getImage())
                .asBitmap()
                .placeholder(R.drawable.def_no_iv)
                .error(R.drawable.def_no_iv)
                .into(tIvHotLive1);

        TextView tTvHotLive1 = ViewHolder.get(convertView, R.id.tvHomeHLTitle1);
        tTvHotLive1.setText(tHotlives.get(0).getTitle());

        LinearLayout tLlhotLiveGroup2 = ViewHolder.get(convertView,
                R.id.llHomeHLGroup2);
        LinearLayout tLlhotLiveGroup3 = ViewHolder.get(convertView,
                R.id.llHomeHLGroup3);

        if (tHotlives.size() <= 1) {

            tLlhotLiveGroup2.setVisibility(View.GONE);
            tLlhotLiveGroup3.setVisibility(View.GONE);
        } else {

            tLlhotLiveGroup2.setVisibility(View.VISIBLE);
            tLlhotLiveGroup2.setTag(position + "," + 1);
            tLlhotLiveGroup2.setOnClickListener(mListener);

            ImageView tIvHotLive2 = ViewHolder.get(convertView,
                    R.id.ivHomeHLBigImg2);

            Glide.with(mContext)
                    .load(tHotlives.get(1).getImage())
                    .asBitmap()
                    .placeholder(R.drawable.def_no_iv)
                    .error(R.drawable.def_no_iv)
                    .into(tIvHotLive2);

            TextView tTvHotLive2 = ViewHolder.get(convertView,
                    R.id.tvHomeHLTitle2);
            tTvHotLive2.setText(tHotlives.get(1).getTitle());

            if (tHotlives.size() <= 2) {

                tLlhotLiveGroup3.setVisibility(View.GONE);

            } else {

                tLlhotLiveGroup3.setVisibility(View.VISIBLE);
                tLlhotLiveGroup3.setTag(position + "," + 2);
                tLlhotLiveGroup3.setOnClickListener(mListener);

                ImageView tIvHotLive3 = ViewHolder.get(convertView,
                        R.id.ivHomeHLBigImg3);

                Glide.with(mContext)
                        .load(tHotlives.get(2).getImage())
                        .asBitmap()
                        .placeholder(R.drawable.def_no_iv)
                        .error(R.drawable.def_no_iv)
                        .into(tIvHotLive3);

                TextView tTvHotLive3 = ViewHolder.get(convertView,
                        R.id.tvHomeHLTitle3);
                tTvHotLive3.setText(tHotlives.get(2).getTitle());

            }
        }

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
                .load(tHomeInteractionOne.getInteraction().getImage())
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

    private View initHomeLive2(View convertView, int position, ViewGroup parent) {

        convertView = getConverView(convertView, R.layout.home_style1_item,
                parent);

        HomeLive2 tHomeLive2 = (HomeLive2) mDatas.get(position);

        List<HomeStyleData> tStyleDatas = tHomeLive2.getLive2();
        HomeStyleData tStyleData1 = tStyleDatas.get(0);

        LinearLayout tLlStGruop1 = ViewHolder.get(convertView,
                R.id.llHomeStGroup1);
        tLlStGruop1.setTag(position + "," + 0);
        tLlStGruop1.setOnClickListener(mListener);

        ImageView tIvStyleImg1 = ViewHolder.get(convertView,
                R.id.ivHomeStBigImg1);

        Glide.with(mContext)
                .load(tStyleData1.getImage())
                .asBitmap()
                .placeholder(R.drawable.def_no_iv)
                .error(R.drawable.def_no_iv)
                .into(tIvStyleImg1);

        RelativeLayout tRlPlayTime = ViewHolder.get(convertView,
                R.id.rlHomeStPlayTime1);

        String tVideoLength = tStyleData1.getVideoLength();

        if (tVideoLength == null || tVideoLength.equals("")) {

            tRlPlayTime.setVisibility(View.GONE);
        } else {

            tRlPlayTime.setVisibility(View.VISIBLE);

            TextView tTvStylePlayTime1 = ViewHolder.get(convertView,
                    R.id.tvHomeStPlayTime1);
            tTvStylePlayTime1.setText(tStyleData1.getVideoLength());
        }

        TextView tTvStyleTitle1 = ViewHolder.get(convertView,
                R.id.tvHomeStTitle1);
        tTvStyleTitle1.setText(tStyleData1.getTitle());

        TextView tTvStyleDate1 = ViewHolder
                .get(convertView, R.id.tvHomeStDate1);
        tTvStyleDate1.setText(tStyleData1.getDaytime());

        LinearLayout tLlHomeStGroup2 = ViewHolder.get(convertView,
                R.id.llHomeStGroup2);

        if (tStyleDatas.size() == 1) {

            tLlHomeStGroup2.setVisibility(View.GONE);
        } else {

            tLlHomeStGroup2.setVisibility(View.VISIBLE);
            tLlHomeStGroup2.setTag(position + "," + 1);
            tLlHomeStGroup2.setOnClickListener(mListener);

            HomeStyleData tStyleData2 = tStyleDatas.get(1);

            ImageView tIvStyleImg2 = ViewHolder.get(convertView,
                    R.id.ivHomeStBigImg2);

            Glide.with(mContext)
                    .load(tStyleData2.getImage())
                    .asBitmap()
                    .placeholder(R.drawable.def_no_iv)
                    .error(R.drawable.def_no_iv)
                    .into(tIvStyleImg2);

            RelativeLayout tRlPlayTime2 = ViewHolder.get(convertView,
                    R.id.rlHomeStPlayTime1);

            String tVideoLength2 = tStyleData2.getVideoLength();

            if (tVideoLength2 == null || tVideoLength2.equals("")) {

                tRlPlayTime2.setVisibility(View.GONE);
            }
//            else {
//
//                tRlPlayTime2.setVisibility(View.VISIBLE);
//
//                TextView tTvStylePlayTime1 = ViewHolder.get(convertView,
//                        R.id.tvHomeStPlayTime1);
//                tTvStylePlayTime1.setText(tVideoLength2);
//            }

            TextView tTvStylePlayTime2 = ViewHolder.get(convertView,
                    R.id.tvHomeStPlayTime2);
            tTvStylePlayTime2.setText(tStyleData2.getVideoLength());

            TextView tTvStyleTitle2 = ViewHolder.get(convertView,
                    R.id.tvHomeStTitle2);
            tTvStyleTitle2.setText(tStyleData2.getTitle());

            TextView tTvStyleDate2 = ViewHolder.get(convertView,
                    R.id.tvHomeStDate2);
            tTvStyleDate2.setText(tStyleData2.getDaytime());
        }
        return convertView;
    }

    private View initHomeStyle1(View convertView, int position, ViewGroup parent) {

        convertView = getConverView(convertView, R.layout.home_style1_item,
                parent);

        HomeStyle1 tHomeStyle1 = (HomeStyle1) mDatas.get(position);

        List<HomeStyleData> tStyleDatas = tHomeStyle1.getStyles1();
        HomeStyleData tStyleData1 = tStyleDatas.get(0);

        LinearLayout tLlStGruop1 = ViewHolder.get(convertView,
                R.id.llHomeStGroup1);
        tLlStGruop1.setTag(position + "," + 0);
        tLlStGruop1.setOnClickListener(mListener);

        ImageView tIvStyleImg1 = ViewHolder.get(convertView,
                R.id.ivHomeStBigImg1);

        Glide.with(mContext)
                .load(tStyleData1.getImage())
                .asBitmap()
                .placeholder(R.drawable.def_no_iv)
                .error(R.drawable.def_no_iv)
                .into(tIvStyleImg1);

        RelativeLayout tRlPlayTime = ViewHolder.get(convertView,
                R.id.rlHomeStPlayTime1);

        String tVideoLength = tStyleData1.getVideoLength();

        if (tVideoLength == null || tVideoLength.equals("")) {

            tRlPlayTime.setVisibility(View.GONE);
        } else {

            tRlPlayTime.setVisibility(View.VISIBLE);

            TextView tTvStylePlayTime1 = ViewHolder.get(convertView,
                    R.id.tvHomeStPlayTime1);
            tTvStylePlayTime1.setText(tStyleData1.getVideoLength());
        }

        TextView tTvStyleTitle1 = ViewHolder.get(convertView,
                R.id.tvHomeStTitle1);
        tTvStyleTitle1.setText(tStyleData1.getTitle());

        TextView tTvStyleDate1 = ViewHolder
                .get(convertView, R.id.tvHomeStDate1);

        String daytime1 = tStyleData1.getDaytime();
        if (daytime1 == null || daytime1.equals("")) {
            tTvStyleDate1.setVisibility(View.GONE);
        } else {
            tTvStyleDate1.setVisibility(View.VISIBLE);
            tTvStyleDate1.setText(tStyleData1.getDaytime());
        }

        LinearLayout tLlHomeStGroup2 = ViewHolder.get(convertView,
                R.id.llHomeStGroup2);

        if (tStyleDatas.size() == 1) {

            tLlHomeStGroup2.setVisibility(View.GONE);
        } else {

            tLlHomeStGroup2.setVisibility(View.VISIBLE);
            tLlHomeStGroup2.setTag(position + "," + 1);
            tLlHomeStGroup2.setOnClickListener(mListener);

            HomeStyleData tStyleData2 = tStyleDatas.get(1);

            ImageView tIvStyleImg2 = ViewHolder.get(convertView,
                    R.id.ivHomeStBigImg2);

            Glide.with(mContext)
                    .load(tStyleData2.getImage())
                    .asBitmap()
                    .placeholder(R.drawable.def_no_iv)
                    .error(R.drawable.def_no_iv)
                    .into(tIvStyleImg2);

            RelativeLayout tRlPlayTime2 = ViewHolder.get(convertView,
                    R.id.rlHomeStPlayTime2);

            String tVideoLength2 = tStyleData2.getVideoLength();

            if (tVideoLength2 == null || tVideoLength2.equals("")) {

                tRlPlayTime2.setVisibility(View.GONE);
            } else {

                tRlPlayTime2.setVisibility(View.VISIBLE);

//                TextView tTvStylePlayTime1 = ViewHolder.get(convertView,
//                        R.id.tvHomeStPlayTime1);
//                tTvStylePlayTime1.setText(tVideoLength2);
            }

            TextView tTvStylePlayTime2 = ViewHolder.get(convertView,
                    R.id.tvHomeStPlayTime2);
            tTvStylePlayTime2.setText(tStyleData2.getVideoLength());

            TextView tTvStyleTitle2 = ViewHolder.get(convertView,
                    R.id.tvHomeStTitle2);
            tTvStyleTitle2.setText(tStyleData2.getTitle());

            TextView tTvStyleDate2 = ViewHolder.get(convertView,
                    R.id.tvHomeStDate2);
            String daytime2 = tStyleData2.getDaytime();

            if (daytime2 == null || tStyleData2.getDaytime().equals("")) {
                tTvStyleDate2.setVisibility(View.GONE);
            } else {
                tTvStyleDate2.setVisibility(View.VISIBLE);
                tTvStyleDate2.setText(tStyleData2.getDaytime());
            }


        }
        return convertView;
    }

    private View initHomeStyle2(View convertView, int position, ViewGroup parent) {

        convertView = getConverView(convertView, R.layout.home_style2_item,
                parent);

        HomeStyle2 tHomeStyle = (HomeStyle2) mDatas.get(position);
        HomeStyleData tStyleData = tHomeStyle.getStyleData();

        LinearLayout tLlHomeStyle2 = ViewHolder.get(convertView,
                R.id.llHomeSt2Group);
        tLlHomeStyle2.setTag(position + "," + 0);
        tLlHomeStyle2.setOnClickListener(mListener);

        ImageView tIvStyle2Img = ViewHolder.get(convertView,
                R.id.ivHomeSt2BigImg);

        Glide.with(mContext)
                .load(tStyleData.getImage())
                .asBitmap()
                .placeholder(R.drawable.def_no_iv)
                .error(R.drawable.def_no_iv)
                .into(tIvStyle2Img);

        RelativeLayout tRlPlayTime = ViewHolder.get(convertView,
                R.id.rlHomeSt2PlayTime);

        String tVideoLength = tStyleData.getVideoLength();

        if (tVideoLength == null || tVideoLength.equals("")) {

            tRlPlayTime.setVisibility(View.GONE);
        } else {

            tRlPlayTime.setVisibility(View.VISIBLE);

            TextView tTvHomeSt2PlayTime = ViewHolder.get(convertView,
                    R.id.tvHomeSt2PlayTime);
            tTvHomeSt2PlayTime.setText(tVideoLength);
        }

        TextView tTvStyle2Title = ViewHolder.get(convertView,
                R.id.tvHomeSt2Title);
        tTvStyle2Title.setText(tStyleData.getTitle());

        TextView tTvStyle2Date = ViewHolder
                .get(convertView, R.id.tvHomeSt2Date);
        tTvStyle2Date.setText(tStyleData.getDaytime());

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

            convertView = mInflater.inflate(layoutId, parent, false);
            AutoUtils.autoSize(convertView);
        }

        return convertView;
    }
}
