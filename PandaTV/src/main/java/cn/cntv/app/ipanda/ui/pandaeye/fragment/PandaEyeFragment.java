package cn.cntv.app.ipanda.ui.pandaeye.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gridsum.mobiledissector.MobileAppTracker;

import java.util.ArrayList;
import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.CollectPageSourceEnum;
import cn.cntv.app.ipanda.constant.CollectTypeEnum;
import cn.cntv.app.ipanda.constant.WebAddressEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.ui.PEBaseFragment;
import cn.cntv.app.ipanda.ui.home.auto.view.AutoScrollViewPager;
import cn.cntv.app.ipanda.ui.home.entity.BigImg;
import cn.cntv.app.ipanda.ui.home.listener.HomeViewPagerListener;
import cn.cntv.app.ipanda.ui.pandaeye.activity.PandaEyeDetailActivity;
import cn.cntv.app.ipanda.ui.pandaeye.adapter.PEyeAutoScrollAdapter;
import cn.cntv.app.ipanda.ui.pandaeye.adapter.PandaEyeHomeListViewAdapter;
import cn.cntv.app.ipanda.ui.pandaeye.entity.PEListData;
import cn.cntv.app.ipanda.ui.pandaeye.entity.PEListDetail;
import cn.cntv.app.ipanda.ui.pandaeye.entity.PandaEyePageData;
import cn.cntv.app.ipanda.ui.pandaeye.entity.PandaEyeTopData;
import cn.cntv.app.ipanda.ui.play.PlayLiveActivity;
import cn.cntv.app.ipanda.ui.play.PlayVodFullScreeActivity;
import cn.cntv.app.ipanda.ui.play.entity.PlayLiveEntity;
import cn.cntv.app.ipanda.ui.play.entity.PlayVodEntity;
import cn.cntv.app.ipanda.utils.SharePreferenceUtil;
import cn.cntv.app.ipanda.utils.TimeHelper;
import cn.cntv.app.ipanda.utils.ViewHolder;
import cn.cntv.app.ipanda.view.PointView;
import cn.cntv.app.ipanda.xlistview.XListView;
import cn.cntv.app.ipanda.xlistview.XListView.IXListViewListener;

/**
 * 熊猫观察
 *
 * @author wanghaofei
 */
public class PandaEyeFragment extends PEBaseFragment {
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    private static final int TAB_DATA = 0;
    //    private static final int LIST_DATA1 = 1;
    private static final int LIST_DATA2 = 2;

    private RelativeLayout mNetImg;

    //    private PandaEyeViewPagerAdapter bannerAdapter;
    private XListView mListView;
    public int mDisplayHeight;
    public int mDisplayWidth;
    private PandaEyeHomeListViewAdapter mPhViewAdapter;
    private List<PEListDetail> mListDs;
    private int mPage = 1;
    private String mSecUrl;
    private PandaEyeTopData mPtData;

    List<PandaEyeTopData> mImgUrls;

    private View mConvertView;


//    private static final long INTERVAL = 5000;

    public PandaEyeFragment() {
        // Required empty public constructor
    }

    private XjlHandler mHandler1 = new XjlHandler(new HandlerListener() {

        @Override
        public void handlerMessage(HandlerMessage msg) {


            dismissLoadDialog();

            switch (msg.what) {
                case Integer.MAX_VALUE:
                    mNetImg.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    break;
                case TAB_DATA:
                    // 处理tab数据


                    mListView.stopRefresh();

                    PandaEyePageData data1 = (PandaEyePageData) msg.obj;

                    if (null == data1 || null == data1.getData()) {
                        mNetImg.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                        return;
                    }

                    //	Log.e("wang", data1.toString());

                    mImgUrls = data1.getData().getBigImg();

                    Log.e("peye", "imgUrls.size=" + mImgUrls.size());

                    //	Log.e("eye", "url="+imgUrls.get(0).getUrl());

                    //initViewpager(data1.getData().getBigImg());
                    //加载大图
                    initBigImg();

                    if (null == data1.getData().getListurl() || data1.getData().getListurl().trim().length() == 0) {
                        return;
                    } else {
//							secUrl = data1.getData().getListurl().substring(0,data1.getData().getListurl().lastIndexOf("&n"));
//							secUrl = secUrl+"&n=6&p=";

                        try {
                            mSecUrl = data1.getData().getListurl().trim() + "&pageSize=6&page=";
                        } catch (Exception e) {
                        }

                    }

                    requestListData(mPage);
                    break;
                case LIST_DATA2:
                    mListView.stopLoadMore();
                    // 处理第一个tab对应的列表数据
                    final PEListData data2 = (PEListData) msg.obj;

                    //Log.e("eye","data2="+ data2.toString());

                    if (null == data2 || null == data2.getList()) {
                        return;
                    }

                    mNetImg.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);

                    if (mPage != 1 && null != data2) {

                        mListDs.addAll(data2.getList());
                        mPhViewAdapter.notifyDataSetChanged();
                    } else {

                        //刷新之后，清空，重新加载
                        if (null != mListDs && mListDs.size() != 0) {
                            mListDs.clear();
                        }


                        mListDs = data2.getList();
                        mPhViewAdapter = new PandaEyeHomeListViewAdapter(
                                getActivity(), mListDs);
                        mListView.setAdapter(mPhViewAdapter);
                        mListView.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int positions,
                                                    long arg3) {
//									if( positions == listDs.size() - 3){
//
//									}else{

                                PEListDetail detail = (PEListDetail) parent.getItemAtPosition(positions);
                                if (null != detail) {
                                    if (detail.getDatatype().trim().equals("video")) {

                                        PlayVodEntity vodModel = new PlayVodEntity(CollectTypeEnum.SP.value() + "", detail.getGuid(), null, detail.getUrl(), detail.getPicurl(), detail.getTitle(), detail.getGuid(), 2, detail.getVideolength());

                                        Intent intent = new Intent(getActivity(), PlayVodFullScreeActivity.class);
                                        intent.putExtra("vid", vodModel);
                                        MobileAppTracker.trackEvent(vodModel.getTitle(), "", "熊猫观察", 0, vodModel.getVid(), "视频观看", getActivity());
                                        MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                                        Log.i("统计", "熊猫观察视频播放=  " + vodModel.getTitle() + "*/****+" + vodModel.getVid());
                                        startActivity(intent);


                                        //	Log.e("eye","item="+ vodModel.toString());

                                    } else {
                                        Intent intent = new Intent(getActivity(), PandaEyeDetailActivity.class);
                                        intent.putExtra(PandaEyeDetailActivity.TYPE, PandaEyeDetailActivity.TYPE_ARTICLE);

                                        intent.putExtra("url", detail.getUrl());
                                        intent.putExtra("title", detail.getTitle());
                                        intent.putExtra("pic", detail.getPicurl());
                                        intent.putExtra("timeval", detail.getFocus_date());
                                        intent.putExtra("id", detail.getId());

                                        //	Log.e("eye","detail.getDatatype()="+ detail.getDatatype());
                                        MobileAppTracker.trackEvent(detail.getTitle(), "", "熊猫观察", 0, detail.getId(), "图文浏览", getActivity());
                                        MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                                        Log.i("统计", "熊猫观察图文浏览=  " + detail.getTitle() + "*/****+" + detail.getId());
                                        startActivity(intent);
                                    }
                                }

                            }
                        });
                    }
                    break;
            }
        }
    });


    /**
     * @return A new instance of fragment PandaEyeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PandaEyeFragment newInstance() {
        return new PandaEyeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_panda_eye, container,
                false);
//        View headView = (View) inflater.inflate(R.layout.pandaeye_listview_head,
//                null);
        initView(view);
        // mImageLoader
        // .setDefaultLoadingListener(imageLoadingListener);
        // 设置ImageLoader加载样式
//        mOptions = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.drawable.def_no_iv)
//                .showImageOnFail(R.drawable.def_no_iv)
//                .showImageOnLoading(R.drawable.def_no_iv).cacheInMemory(true) // 设置是否将被缓存在内存中加载图像，(没有它每次都是从新加载)
//                .considerExifParams(true) // 设置是否会考虑imageloader
//                // JPEG图像EXIF参数（旋转，翻转）
//                .build();
        // initViewpager(inflater);
        return view;
    }

    private void initView(View view) {
        mListView = (XListView) view.findViewById(R.id.pandaeye_home_listview);
        mNetImg = (RelativeLayout) view.findViewById(R.id.pandaeye_no_net);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(outMetrics);
        mDisplayHeight = outMetrics.heightPixels;
        mDisplayWidth = outMetrics.widthPixels;


        // LayoutParams params2 = viewPager.getLayoutParams();
        // params2.width = displayWidth;
        // params2.height = displayWidth / 16 * 9;

//        PointView mPointView = (PointView) headView.findViewById(R.id.pointview);

        //listView.addHeaderView(headView);
        mListView.setXListViewListener(xListViewListener1);
        mListView.setRefreshTime(TimeHelper.getCurrentData());
        mListView.setPullLoadEnable(true);

        mConvertView = LayoutInflater.from(getActivity()).inflate(R.layout.view_banner_viewpager, null);
        mListView.addHeaderView(mConvertView);

        // listView.removeHeaderView(v);


        mNetImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                requestTopData();
            }
        });


        requestTopData();

    }

    IXListViewListener xListViewListener1 = new IXListViewListener() {

        @Override
        public void onRefresh() {
            mPage = 1;
            requestTopData();

        }

        @Override
        public void onLoadMore() {

            mPage++;
            requestListData(mPage);
        }
    };

    /**
     * 从服务器获取tab数据
     */
    private void requestTopData() {

        if (isConnected()) {
            showLoadingDialog();

            SharePreferenceUtil tPreferenceUtil = new SharePreferenceUtil(
                    getActivity());
            String tUrl = tPreferenceUtil.getWebAddress(WebAddressEnum.WEB_PANDA_EYE
                    .toString());

            if (tUrl == null || tUrl.equals("")) {

                Toast.makeText(getActivity(), R.string.load_fail, Toast.LENGTH_SHORT)
                        .show();
            } else {

                mHandler1.getHttpJson(tUrl, PandaEyePageData.class, TAB_DATA);
            }

        } else {
            Toast.makeText(getActivity(), R.string.network_invalid, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void requestListData(int pnum) {

        mHandler1.getHttpJson(mSecUrl.toString() + pnum, PEListData.class,
                LIST_DATA2);
    }





    //此问题需要处理
    private void initBigImg() {

        if (null == mImgUrls || mImgUrls.size() == 0) {
            return;
        }

        List<BigImg>  bigImgs = new ArrayList<BigImg>();

        for (int i = 0; i < mImgUrls.size(); i++) {

            mPtData = mImgUrls.get(i);
            if (null == mPtData) {
                continue;
            } else {
                BigImg bImg = new BigImg();
                bImg.setId(mPtData.getId());
                bImg.setImage(mPtData.getImage());
                bImg.setOrder(Integer.parseInt(mPtData.getOrder()));
                bImg.setPid(mPtData.getPid());
                bImg.setStype(mPtData.getStype());
                bImg.setTitle(mPtData.getTitle());
                bImg.setType(mPtData.getType());
                bImg.setUrl(mPtData.getUrl());
                bImg.setVid(mPtData.getVid());
                bigImgs.add(bImg);
            }
        }

//        List<View> tViews = new ArrayList<View>();

        //HomeAdapterBigTwoImg tBigImg = (HomeAdapterBigTwoImg) mDatas.get(position);
//        int tBigImgSize = imgUrls.size();

        AutoScrollViewPager tBigImgPager = ViewHolder.get(mConvertView,
                R.id.viewPager);

        PointView tPointView = ViewHolder.get(mConvertView, R.id.pointview);
        tBigImgPager.addOnPageChangeListener(new HomeViewPagerListener(
                tPointView));

        int tTitleWidth ;

        if (mImgUrls.size() == 1) {
            tTitleWidth = tPointView.setPointCount(0);
        } else {
            tTitleWidth = tPointView.setPointCount(mImgUrls.size());
        }

        tBigImgPager.addOnPageChangeListener(new HomeViewPagerListener(
                tPointView));

        tBigImgPager.setAdapter(new PEyeAutoScrollAdapter(bigImgs,
                getActivity(), null, tTitleWidth, 2));
        tBigImgPager.startAutoScroll();

        tPointView.setCurrentIndex(0);
        tBigImgPager.setCurrentItem(0);
        tBigImgPager.setOnClickListener(new BigImgClick(mPtData));


    }


    class BigImgClick implements OnClickListener {

        PandaEyeTopData pandaModel;

        public BigImgClick(PandaEyeTopData pandaEyeModel) {
            pandaModel = pandaEyeModel;
        }

        @Override
        public void onClick(View view) {

            PandaEyeTopData topData = pandaModel;
            String tEx2conten ;
            String tContentId = "";
            if (null != topData.getType()) {
                if (topData.getType().trim().equals("6")) {
                    //为图文，h5
                    Intent intent = new Intent(getActivity(), PandaEyeDetailActivity.class);
                    intent.putExtra("url", topData.getUrl());
                    intent.putExtra("title", topData.getTitle());
                    intent.putExtra("pic", topData.getImage());
                    intent.putExtra("id", topData.getId());
                    intent.putExtra("timeval", "");
                    tEx2conten = "图文浏览";
                    tContentId = topData.getId();

                    MobileAppTracker.trackEvent(topData.getTitle(), "大图推荐", "熊猫观察", 0, tContentId, tEx2conten, getActivity());
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.i("熊猫观察：" + topData.getTitle(), "视频ID：" + tEx2conten);
                    //Log.e("eye", topData.getUrl()+"title="+topData.getTitle()+"pic="+topData.getImage());

                    getActivity().startActivity(intent);

                } else if (topData.getType().trim().equals("1")) {
                    if (null != topData.getStype() && topData.getStype().trim().equals("3")) {
                        //直播中国单视角直播
                        PlayLiveEntity liveModel = new PlayLiveEntity(topData.getId().trim(), topData.getTitle(), topData.getImage(), topData.getUrl(), null, "3", CollectPageSourceEnum.XMZB.value(),
                                false);

                        Intent liveIntent = new Intent(getActivity(), PlayLiveActivity.class);
                        tEx2conten = "视频播放";
                        tContentId = topData.getVid();
                        MobileAppTracker.trackEvent(topData.getTitle(), "大图推荐", "熊猫观察", 0, tContentId, tEx2conten, getActivity());
                        MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                        liveIntent.putExtra("live", liveModel);
                        getActivity().startActivity(liveIntent);
                    }
                } else if (topData.getType().trim().equals("2")) {
                    //为单视频点播
                    PlayVodEntity vodModel = new PlayVodEntity("1", topData.getPid(), null, topData.getUrl(), topData.getImage(), topData.getTitle(), null, 2, null);

                    Intent vodIntent = new Intent(getActivity(), PlayVodFullScreeActivity.class);
                    tEx2conten = "视频播放";
                    tContentId = topData.getVid();
                    MobileAppTracker.trackEvent(topData.getTitle(), "大图推荐", "熊猫观察", 0, tContentId, tEx2conten, getActivity());
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    vodIntent.putExtra("vid", vodModel);
                    getActivity().startActivity(vodIntent);

                } else if (topData.getType().trim().equals("3")) {
                    //直接跳到全屏视频集播
                    PlayVodEntity vodModel = new PlayVodEntity("1", topData.getPid(), topData.getVid(), topData.getUrl(), topData.getImage(), topData.getTitle(), null, 2, null);
                    Intent ccIntent = new Intent(getActivity(), PlayVodFullScreeActivity.class);
//					ccIntent.putExtra("id", topData.getVid());
//					ccIntent.putExtra("title", topData.getTitle());
//					ccIntent.putExtra("image", topData.getImage());

                    ccIntent.putExtra("vid", vodModel);
                    tEx2conten = "视频播放";
                    MobileAppTracker.trackEvent(topData.getTitle(), "大图推荐", "熊猫观察", 0, tContentId, tEx2conten, getActivity());
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
//                    tContentId = topData.getVid();
                    getActivity().startActivity(ccIntent);


                } else if (topData.getType().trim().equals("5")) {
                    //为正文
                    Intent intent = new Intent(getActivity(), PandaEyeDetailActivity.class);
                    intent.putExtra(PandaEyeDetailActivity.TYPE, PandaEyeDetailActivity.TYPE_ARTICLE);

                    intent.putExtra("url", topData.getUrl());
                    intent.putExtra("title", topData.getTitle());
                    intent.putExtra("pic", topData.getImage());
                    intent.putExtra("id", topData.getId());
                    intent.putExtra("timeval", "");

                    //Log.e("eye", topData.getUrl()+"title="+topData.getTitle()+"pic="+topData.getImage());
                    tEx2conten = "图文浏览";
                    MobileAppTracker.trackEvent(topData.getTitle(), "大图推荐", "熊猫观察", 0, tContentId, tEx2conten, getActivity());
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
//                    tContentId = topData.getId();
                    getActivity().startActivity(intent);
                }
            }


        }

    }


}
