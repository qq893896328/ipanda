package cn.cntv.app.ipanda.ui.pandaculture.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gridsum.mobiledissector.MobileAppTracker;

import java.util.ArrayList;
import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.WebAddressEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.ui.BaseFragment;
import cn.cntv.app.ipanda.ui.cctv.activity.CCTVDetailActivity;
import cn.cntv.app.ipanda.ui.home.auto.view.AutoScrollViewPager;
import cn.cntv.app.ipanda.ui.pandaculture.adapter.CultureBigImgAdapter;
import cn.cntv.app.ipanda.ui.pandaculture.adapter.CultureListAdapter;
import cn.cntv.app.ipanda.ui.pandaculture.adapter.CultureViewPagerListener;
import cn.cntv.app.ipanda.ui.pandaculture.entity.PandaCultureBigImgBean;
import cn.cntv.app.ipanda.ui.pandaculture.entity.PandaCultureData;
import cn.cntv.app.ipanda.ui.pandaculture.entity.PandaCultureListBean;
import cn.cntv.app.ipanda.ui.play.PlayVodFullScreeActivity;
import cn.cntv.app.ipanda.ui.play.entity.PlayVodEntity;
import cn.cntv.app.ipanda.utils.NetUtil;
import cn.cntv.app.ipanda.utils.TimeHelper;
import cn.cntv.app.ipanda.utils.ViewHolder;
import cn.cntv.app.ipanda.view.PointView;
import cn.cntv.app.ipanda.xlistview.XListView;
import cn.cntv.app.ipanda.xlistview.XListView.IXListViewListener;

/**
     * Created by maqingwei on 2016/5/16
     *   熊猫文化页
     */
public class PandaCultureFragment extends BaseFragment {

    private XListView mListView;
    private RelativeLayout mNoNet;
    private View mBigHeader;


    private LayoutInflater mInflater;
    private List<PandaCultureListBean> mListBeans;//熊猫文化视频集列表
    private List<PandaCultureBigImgBean> mBigImgBeans
            = new ArrayList<PandaCultureBigImgBean>();//熊猫文化大轮播图

    private static final int TAB_DATA = 0;//熊猫文化TAB数据

    public XjlHandler mHandler = new XjlHandler(new HandlerListener() {

        @Override
        public void handlerMessage(HandlerMessage msg) {


            switch (msg.what) {
                case Integer.MAX_VALUE:
                    mNoNet.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    break;
                case TAB_DATA:
                    //处理TAB对应的数据

                    onLoad();
                    PandaCultureData cultureData = (PandaCultureData) msg.obj;

                    if (null == cultureData) {
                        mNoNet.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                        return;
                    }

                    mListBeans = new ArrayList<>();
//                    for (PandaCultureListBean bean : cultureData.getList()) {
//                        if (!"1".equals(bean.getType())) {
//                            mListBeans.add(bean);//过滤 不将单视频显示出来
//                        }
//                    }

                  mListBeans = cultureData.getList();

                    dealListBean();
                    List<PandaCultureBigImgBean> bigImg = cultureData.getBigImg();
                    mBigImgBeans.clear();
                    mBigImgBeans.addAll(bigImg);

                    mNoNet.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    initBigImage();


                    break;
            }
        }
    });


    private void dealListBean() {
        //处理视频集列表数据

        if (mListBeans != null || mListBeans.size() != 0) {

            CultureListAdapter cultureListAdapter = new CultureListAdapter(getActivity(),
                    mListBeans, mInflater);
            mListView.setAdapter(cultureListAdapter);
            cultureListAdapter.notifyDataSetChanged();
        }

        dismissLoadDialog();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pandaculture, container,
                false);
        mInflater = inflater;
        init(view);
        return view;
    }

    public static PandaCultureFragment newInstance() {
        return new PandaCultureFragment();
    }

    private void init(View view) {
        mListView = (XListView) view.findViewById(R.id.pandaculture_home_listview);
        mNoNet = (RelativeLayout) view.findViewById(R.id.pandaculture_no_net);


        mListView.setRefreshTime(TimeHelper.getCurrentData());
        mListView.setPullLoadEnable(false);

        mBigHeader = LayoutInflater.from(getActivity()).inflate(R.layout.view_banner_viewpager, null);
        mListView.addHeaderView(mBigHeader);

        mListView.setOnItemClickListener(listItemClick);
        mListView.setXListViewListener(xListViewListener1);

        mNoNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        sendRequest();
    }


    /**
             * 熊猫文化列表项点击
             */
    AdapterView.OnItemClickListener listItemClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View view, int position,
                                long arg3) {

            if (mListBeans != null && mListBeans.size() > 0) {

                try {
                    PandaCultureListBean entity = mListBeans.get(position- mListView.getHeaderViewsCount());

                    if(entity.getType().trim().equals("1")){
                        //单视频
                        PlayVodEntity vodModel = new PlayVodEntity("1", entity.getId(), null, entity.getUrl(), entity.getImage(), entity.getTitle(), null, 2, null);
                        Intent vodIntent = new Intent(getActivity(),
                                PlayVodFullScreeActivity.class);
                        vodIntent.putExtra("vid", vodModel);
                        //统计
                        MobileAppTracker.trackEvent(entity.getTitle(), "", "熊猫文化", 0, entity.getId(), "视频观看", getActivity());
                        MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                        Log.e("统计","事件名称:"+entity.getTitle()+"***事件类别:"+"熊猫文化"+"***事件标签:"+"列表"+"***类型:"+"视频观看"+"*****ID="+entity.getId());

                        startActivity(vodIntent);

                    }else{
                        //视频集
                        Intent intent = new Intent(getActivity(),
                                CCTVDetailActivity.class);
                        intent.putExtra("id", entity.getId());
                        intent.putExtra("title", entity.getTitle());
                        intent.putExtra("image", entity.getImage());
                        intent.putExtra("videoLength", entity.getVideoLength());
                        //统计
                        MobileAppTracker.trackEvent(entity.getTitle(), "", "熊猫文化", 0, entity.getId(), "视频观看", getActivity());
                        MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                        Log.e("统计","事件名称:"+entity.getTitle()+"***事件类别:"+"熊猫文化"+"***事件标签:"+"列表"+"***类型:"+"视频观看"+"*****ID="+entity.getId());

                        startActivity(intent);
                    }

                } catch (Exception e) {
                }
            }
        }
    };

    IXListViewListener xListViewListener1 = new IXListViewListener() {
        @Override
        public void onRefresh() {
            if (NetUtil.isNetConnected(getActivity())) {

                sendRequest();
                mNoNet.setVisibility(View.GONE);
            } else {
                onLoad();

                mNoNet.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), R.string.network_invalid, Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onLoadMore() {

        }
    };

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(TimeHelper.getCurrentData());
    }

        /**
         * 请求熊猫文化TAB数据
         */
    private void sendRequest() {
        if (isConnected()) {
            showLoadingDialog();

            String turl = WebAddressEnum.PANDA_CULTURE.toString();
            if (turl == null || turl.equals("")) {
                Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
            } else {
                mHandler.getHttpJson(turl, PandaCultureData.class, TAB_DATA);
            }

        } else {
            Toast.makeText(getActivity(), R.string.network_invalid, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void initBigImage() {

        if (null == mBigImgBeans || mBigImgBeans.size() == 0) {
            return;
        }

        AutoScrollViewPager tBigImage = ViewHolder.get(mBigHeader, R.id.viewPager);
        PointView tPointView = ViewHolder.get(mBigHeader, R.id.pointview);
        tBigImage.setOnPageChangeListener(new CultureViewPagerListener(tPointView));

        int tTitleWidth;
        int tBigSize = mBigImgBeans.size();
        if (mBigImgBeans.size() == 1) {
            tTitleWidth = tPointView.setPointCount(0);
        } else {
            tTitleWidth = tPointView.setPointCount(tBigSize);
        }
        CultureBigImgAdapter cultureBigImgAdapter = new CultureBigImgAdapter(mBigImgBeans, getActivity(), null, tTitleWidth, 2);
        tBigImage.setAdapter(cultureBigImgAdapter);
        cultureBigImgAdapter.notifyDataSetChanged();

        tPointView.setCurrentIndex(0);
        tBigImage.setCurrentItem(0);
        tBigImage.startAutoScroll();
    }
}
