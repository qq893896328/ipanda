package cn.cntv.app.ipanda.ui.personal.fragment;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gridsum.mobiledissector.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.CollectPageSourceEnum;
import cn.cntv.app.ipanda.constant.CollectTypeEnum;
import cn.cntv.app.ipanda.constant.Constants;
import cn.cntv.app.ipanda.constant.JMPTypeEnum;
import cn.cntv.app.ipanda.constant.WebAddressEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.ResponseData;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.db.DBInterface;
import cn.cntv.app.ipanda.db.entity.FavoriteEntity;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.manager.UserManager;
import cn.cntv.app.ipanda.ui.BaseFragment;
import cn.cntv.app.ipanda.ui.cctv.activity.CCTVDetailActivity;
import cn.cntv.app.ipanda.ui.pandaeye.activity.PandaEyeDetailActivity;
import cn.cntv.app.ipanda.ui.personal.adapter.PersonalCollectionPagerAdapter;
import cn.cntv.app.ipanda.ui.personal.adapter.PersonalShouCangKDListViewAdapter;
import cn.cntv.app.ipanda.ui.personal.adapter.PersonalShouCangZBoListViewAdapter;
import cn.cntv.app.ipanda.ui.play.PlayLiveActivity;
import cn.cntv.app.ipanda.ui.play.PlayVodFullScreeActivity;
import cn.cntv.app.ipanda.ui.play.entity.PlayLiveEntity;
import cn.cntv.app.ipanda.ui.play.entity.PlayVodEntity;
import cn.cntv.app.ipanda.utils.L;
import cn.cntv.app.ipanda.utils.NetUtil;
import cn.cntv.app.ipanda.utils.TimeHelper;
import cn.cntv.app.ipanda.utils.ToastUtil;
import cn.cntv.app.ipanda.utils.xinterface.IAlertDialog;
import cn.cntv.app.ipanda.xlistview.XListView;

/**
 * Created by maqingwei on 16/7/28.
 *
 */
public class CollectionFragment extends BaseFragment implements View.OnClickListener{

    private View view;

    private TextView titleLeft, titleCenter, titleRight, titleRight2;
    private String userId;// 当前登录的用户Id
    private static final int LIST_DATA1 = 1;
    private static final int LIST_DATA2 = 2;
    private static final int DEL_DATA1 = 3;
    private static final int DEL_DATA2 = 4;
    private LayoutInflater mInflater;
    private int mSelectPosition;//记录选项卡选中的位置
    private UserManager mUserManager = UserManager.getInstance();

    // tab的tag
    private String[] tag_id = {"zhibo", "kandian"};
    // 每个tab对应的文字
    private String[] tab_text;
    private LocalActivityManager manager;

    public int displayHeight;
    public int displayWidth;

    private XListView mListView1, mListView2;
    private PersonalShouCangZBoListViewAdapter mAdapter1;
    private PersonalShouCangKDListViewAdapter mAdapter2;
    private PersonalCollectionPagerAdapter mViewpagerAdapter;
    List<FavoriteEntity> mEntityList1;
    private List<FavoriteEntity> mEntityList2;

    private RelativeLayout rootLayout1, rootLayout2;
    private ImageView noNetView;

    private Map<String, String> params = new HashMap<String, String>();// 请求参数

    private boolean isRefresh;// 标识是否为下拉刷新

    private boolean isInit = true;// 标识是否为初始化
    private int flag1;// 直播的操作按钮标识，0-编辑；1-取消；2-完成
    private int flag2;// 精彩看点的操作按钮标识，0-编辑；1-取消；2-完成
    private int zbAllFlag;// 直播全选标识，0-执行全选；1-执行取消全选
    private int kdAllFlag;// 精彩看点全选标识，0-执行全选；1-执行取消全选

    private View zbBottomView, kdBottomView;
    private TextView zbAllView, zbDelView, kdAllView, kdDelView;

    private List<FavoriteEntity> chkEntityList1 = new ArrayList<FavoriteEntity>();
    private List<FavoriteEntity> chkEntityList2 = new ArrayList<FavoriteEntity>();// 选中的对象

    private View noData1View, noData2View;

    private DBInterface dbInterface = DBInterface.getInstance();

    private List<View> mViews = new ArrayList<View>();
    private List<String> mTitles = new ArrayList<String>();

    @BindView(R.id.collect_tablayout)
    TabLayout mTablayout;
    @BindView(R.id.collect_viewpager)
    ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_personal_shoucang,container,false);
        ButterKnife.bind(this,view);
        tab_text = new String[]{getActivity().getString(R.string.live), this.getString(R.string.kandian)};

        this.mInflater = inflater;
        initView();

        initData();
        initListener();

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        if (!isInit) {
            boolean isFirActivedTab = mSelectPosition == 0;
            
            if (isFirActivedTab) {
                requestFirLocalListData();
            } else {
                requestSecLocalListData();
            }

        }
    }

    private XjlHandler<ResponseData<List<FavoriteEntity>>> mHandler1 = new XjlHandler<ResponseData<List<FavoriteEntity>>>(
            new HandlerListener() {

                @Override
                public void handlerMessage(HandlerMessage msg) {

                    switch (msg.what) {
                        case Integer.MAX_VALUE:
                            break;
                        case LIST_DATA1:
                            ResponseData<List<FavoriteEntity>> data1 = (ResponseData<List<FavoriteEntity>>) msg.obj;
                            if (data1 != null) {
                                mEntityList1 = data1.getData();
                            }
                            dealList1Data();
                            break;
                        case LIST_DATA2:
                            ResponseData<List<FavoriteEntity>> data2 = (ResponseData<List<FavoriteEntity>>) msg.obj;
                            if (data2 != null) {
                                mEntityList2 = data2.getData();
                            }
                            dealList2Data();
                            break;
                        case DEL_DATA1:
                            ResponseData<Object> data3 = (ResponseData<Object>) msg.obj;
                            if (data3 != null) {
                                if (Constants.RESPONSE_STATUS_SUCCESS
                                        .equalsIgnoreCase(data3.getStatus())) {
                                    // 删除成功，处理页面数据
                                    dealDelList1Data();
                                } else {
                                    // 删除失败，给出提示
                                    ToastUtil.showLong(getActivity(),
                                            data3.getMsg());
                                }
                            }
                            break;
                        case DEL_DATA2:
                            ResponseData<Object> data4 = (ResponseData<Object>) msg.obj;
                            if (data4 != null) {
                                if (Constants.RESPONSE_STATUS_SUCCESS
                                        .equalsIgnoreCase(data4.getStatus())) {
                                    // 删除成功，处理页面数据
                                    dealDelList2Data();
                                } else {
                                    // 删除失败，给出提示
                                    ToastUtil.showLong(getActivity(),
                                            data4.getMsg());
                                }
                            }
                            break;
                    }
                }
            });

    private void initView() {



        titleLeft = (TextView) view.findViewById(R.id.common_title_left);
        titleCenter = (TextView) view.findViewById(R.id.common_title_center);
        titleRight = (TextView) view.findViewById(R.id.common_title_right);
        titleRight2 = (TextView) view.findViewById(R.id.common_title_right2);
        titleRight.setVisibility(View.GONE);
        titleRight2.setVisibility(View.GONE);
        mListView1 = (XListView) view
                .findViewById(R.id.personal_sc_zhibo_listview);
        mListView2 = (XListView) view
                .findViewById(R.id.personal_sc_kandian_listview);
        mListView1.setPullLoadEnable(false);// 禁用上拉加载更多
        mListView2.setPullLoadEnable(false);
        mListView1.setPullRefreshEnable(false);
        mListView2.setPullRefreshEnable(false);
        rootLayout1 = (RelativeLayout) view
                .findViewById(R.id.personal_sc_zhibo);
        rootLayout2 = (RelativeLayout) view
                .findViewById(R.id.personal_sc_kandian);

        noNetView = (ImageView) view.findViewById(R.id.ivNoNet);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(outMetrics);
        displayHeight = outMetrics.heightPixels;
        displayWidth = outMetrics.widthPixels;

        mViews.add(rootLayout1);
        mViews.add(rootLayout2);
        mTitles.add("直播");
        mTitles.add("精彩看点");

        mViewpagerAdapter = new PersonalCollectionPagerAdapter(mViews,mTitles);
        mViewPager.setAdapter(mViewpagerAdapter);
        mTablayout.setupWithViewPager(mViewPager);
        mTablayout.setTabMode(TabLayout.MODE_FIXED);
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mSelectPosition = tab.getPosition();
                if (tab.getPosition() ==0) {
                    // 切换到第一个tab
                    rootLayout1.setVisibility(View.VISIBLE);
                    rootLayout2.setVisibility(View.GONE);
                    if (mEntityList1 != null && mEntityList1.size() > 0) {
                        titleRight.setVisibility(View.VISIBLE);
                    } else {
                        titleRight.setVisibility(View.GONE);
                    }
                    titleRight2.setVisibility(View.GONE);
                } else {
                    // 切换到第二个tab
                    rootLayout1.setVisibility(View.GONE);
                    rootLayout2.setVisibility(View.VISIBLE);
                    titleRight.setVisibility(View.GONE);
                    if (isInit) {
                        showLoadingDialog();
                        // if (!StringUtil.isNullOrEmpty(userId)) {
                        // // // 第一次切换到第二个tab， 如果当前登录有用户，向服务器请求列表数据，之后切换不再请求，手动刷新
                        // requestSecListData();
                        // } else {
                        // 如果当前登录无用户，从本地获取收藏的列表数据
                        requestSecLocalListData();
                        // }
                        isInit = false;
                    } else {
                        if (mEntityList2 != null && mEntityList2.size() > 0) {
                            titleRight2.setVisibility(View.VISIBLE);
                        } else {
                            titleRight2.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        zbBottomView = view.findViewById(R.id.personal_sc_zhibo_bottom);
        kdBottomView = view.findViewById(R.id.personal_sc_kandian_bottom);

        zbAllView = (TextView)view.findViewById(R.id.personal_sc_all);
        zbDelView = (TextView) view.findViewById(R.id.personal_sc_delete);
        kdAllView = (TextView) view.findViewById(R.id.personal_sc_kandian_all);
        kdDelView = (TextView) view.findViewById(R.id.personal_sc_kandian_delete);

        noData1View = view.findViewById(R.id.layout_noData1);
        noData2View = view.findViewById(R.id.layout_noData2);
    }

    private void initTbale(){

    }

    private void initData() {
        titleCenter.setText(getString(R.string.collection_page));
        userId = mUserManager.getUserId();

        if (NetUtil.isNetConnected(getActivity())) {
            showLoadingDialog();
            // if (!StringUtil.isNullOrEmpty(userId)) {
            // // 如果当前登录有用户，从服务器获取收藏的列表数据
            // requestFirListData();
            // } else {
            // 如果当前登录无用户，从本地获取收藏的列表数据
            requestFirLocalListData();
            // }
            noNetView.setVisibility(View.GONE);
        } else {
            titleRight.setVisibility(View.GONE);
            noNetView.setVisibility(View.VISIBLE);
        }
    }

    private void initListener() {
        titleRight.setOnClickListener(this);
        titleRight2.setOnClickListener(this);
        noNetView.setOnClickListener(this);
        titleLeft.setOnClickListener(new ViewClick());
        mListView1.setOnItemClickListener(itemClick1);
        mListView1.setXListViewListener(xListViewListener1);
        mListView2.setOnItemClickListener(itemClick2);
        mListView2.setXListViewListener(xListViewListener2);
        zbAllView.setOnClickListener(this);
        zbDelView.setOnClickListener(this);
        kdAllView.setOnClickListener(this);
        kdDelView.setOnClickListener(this);
    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener1 = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton ck, boolean isChecked) {
            Object obj = ck.getTag();
            if (obj instanceof FavoriteEntity) {
                FavoriteEntity entity = (FavoriteEntity) obj;
                if (isChecked) {
                    // 没有添加过的添加
                    if (!chkEntityList1.contains(entity)) {
                        chkEntityList1.add(entity);
                    }
                    entity.setIsChecked(true);
                } else {
                    // 已添加的删除
                    if (!isChecked) {
                        if (chkEntityList1.contains(entity)) {
                            chkEntityList1.remove(entity);
                        }
                        entity.setIsChecked(false);
                    }

                }
                // 设置删除按钮的值和是否禁用状态
                if(chkEntityList1.size()<mEntityList1.size()&&chkEntityList1.size()>0){
                    zbAllFlag = 1;//直播
                }else{
                    zbAllFlag = 0;
                }
                setZbDelBtn();
            }
        }
    };

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener2 = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton ck, boolean isChecked) {

            Object obj = ck.getTag();
            if (obj instanceof FavoriteEntity) {
                FavoriteEntity entity = (FavoriteEntity) obj;
                if (isChecked) {
                    // 没有添加过的添加
                    if (!chkEntityList2.contains(entity)) {
                        chkEntityList2.add(entity);
                    }
                } else {
                    // 已添加的删除
                    if (!isChecked) {
                        if (chkEntityList2.contains(entity)) {
                            chkEntityList2.remove(entity);
                        }
                    }
                }
                // 设置删除按钮的值和是否禁用状态


                if(chkEntityList2.size()<mEntityList2.size()&&chkEntityList2.size()>0){
                    kdAllFlag = 1;
                }else{
                    kdAllFlag = 0;
                }
                setKdDelBtn();
            }

        }
    };

    /**
     * 直播列表项点击
     */
    AdapterView.OnItemClickListener itemClick1 = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View view, int position,
                                long arg3) {
            if (mEntityList1 != null && mEntityList1.size() > 0) {
                try {
                    FavoriteEntity entity = mEntityList1.get(position
                            - mListView1.getHeaderViewsCount());
                    // if (entity.getPageSource() == CollectPageSourceEnum.XMZB
                    // .value()) {
                    // setResult(1001, new Intent().putExtra("flag", 1));
                    // finish();
                    // }
                    // else if (entity.getPageSource() ==
                    // CollectPageSourceEnum.ZBZG
                    // .value()) {
                    // setResult(1001, new Intent().putExtra("flag", 2));
                    // finish();
                    // }
                    if (entity.getPageSource() == CollectPageSourceEnum.PDZB
                            .value()
                            || entity.getPageSource() == CollectPageSourceEnum.ZBZG
                            .value()
                            || entity.getPageSource() == CollectPageSourceEnum.XMZB
                            .value()) {
                        isInit = false;
                        // 来源于频道直播的收藏，点击电视直播跳转至“频道直播全屏页3.5.2”
                        Intent i1 = new Intent(getActivity(),
                                PlayLiveActivity.class);
                        PlayLiveEntity playLiveEntity = new PlayLiveEntity(
                                entity.getObject_id(),
                                entity.getObject_title(),
                                entity.getObject_logo(), null, null,
                                CollectTypeEnum.PD.value() + "",
                                CollectPageSourceEnum.PDZB.value(), true);
                        i1.putExtra("listlive", (Serializable) null);
                        i1.putExtra("live", playLiveEntity);
                        startActivity(i1);
                    }
                } catch (Exception e) {
                    L.e(e.toString());
                }
            }
        }
    };

    /**
     * 列表项点击
     */
    AdapterView.OnItemClickListener itemClick2 = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View view, int position,
                                long arg3) {
            if (mEntityList2 != null && mEntityList2.size() > 0) {
                try {
                    FavoriteEntity entity = mEntityList2.get(position
                            - mListView2.getHeaderViewsCount());
                    if (entity.getCollect_type() == CollectTypeEnum.TW.value()) {
                        // 图文(点击进入3.6.2熊猫观察图文底层页)
                        Intent intent = new Intent(
                                getActivity(),
                                PandaEyeDetailActivity.class);
                        intent.putExtra("id", entity.getObject_id());
                        intent.putExtra("url", entity.getObject_url());
                        intent.putExtra("pic", entity.getObject_logo());
                        intent.putExtra("title", entity.getObject_title());
                        startActivity(intent);
                    } else if (entity.getCollect_type() == CollectTypeEnum.LkTW
                            .value()) {
                        // 本地图文
                        Intent intent = new Intent(
                                getActivity(),
                                PandaEyeDetailActivity.class);
                        intent.putExtra(PandaEyeDetailActivity.TYPE, PandaEyeDetailActivity.TYPE_ARTICLE);

                        intent.putExtra("id", entity.getObject_id());
                        intent.putExtra("url", entity.getObject_url());
                        intent.putExtra("pic", entity.getObject_logo());
                        intent.putExtra("title", entity.getObject_title());
                        startActivity(intent);
                    } else if (entity.getPageSource() != null && entity.getPageSource() == CollectPageSourceEnum.YSML
                            .value()) {
                        // 视频集(只支持名栏视频集收藏) (点击进入3.5.8央视名栏底层页)
                        Intent intent = new Intent(
                                getActivity(),
                                CCTVDetailActivity.class);
                        intent.putExtra("id", entity.getObject_id());
                        intent.putExtra("title", entity.getObject_title());
                        intent.putExtra("image", entity.getObject_logo());
                        startActivity(intent);
                    } else {
                        // 单视频(点击进入所对应的视频全屏页)
                        PlayVodEntity tEntity = new PlayVodEntity(
                                entity.getCollect_type() + "",
                                entity.getVideo_pid(), null,
                                entity.getObject_url(),
                                entity.getObject_logo(),
                                entity.getObject_title(), null,
                                JMPTypeEnum.VIDEO_VOD.value(),
                                entity.getVideoLength());
                        Intent tIntent = new Intent(
                                getActivity(),
                                PlayVodFullScreeActivity.class);
                        tIntent.putExtra("vid", tEntity);
                        startActivity(tIntent);
                    }
                    // else if (entity.isSingleVideo()) {
                    // // 单视频(点击进入所对应的视频全屏页)
                    // PlayVodEntity tEntity = new PlayVodEntity(
                    // entity.getCollect_type() + "",
                    // entity.getObject_id(), null,
                    // entity.getObject_url(),
                    // entity.getObject_logo(),
                    // entity.getObject_title(), null,
                    // JMPTypeEnum.VIDEO_VOD.value(),
                    // entity.getVideoLength());
                    // Intent tIntent = new Intent(
                    // PersonalShouCangActivity.this,
                    // PlayVodFullScreeActivity.class);
                    // tIntent.putExtra("vid", tEntity);
                    // startActivity(tIntent);
                    // }
                } catch (Exception e) {
                }
            }
        }
    };

    XListView.IXListViewListener xListViewListener1 = new XListView.IXListViewListener() {

        @Override
        public void onRefresh() {
            isRefresh = true;
            if (!StringUtil.isNullOrEmpty(userId)) {
                // 如果当前登录有用户，从服务器获取收藏的列表数据
                requestFirListData();
            } else {
                // 如果当前登录无用户，从本地获取收藏的列表数据
                requestFirLocalListData();
            }
        }

        @Override
        public void onLoadMore() {
        }
    };

    XListView.IXListViewListener xListViewListener2 = new XListView.IXListViewListener() {

        @Override
        public void onRefresh() {
            isRefresh = true;
            if (!StringUtil.isNullOrEmpty(userId)) {
                // 如果当前登录有用户，从服务器获取收藏的列表数据
                requestSecListData();
            } else {
                // 如果当前登录无用户，从本地获取收藏的列表数据
                requestSecLocalListData();
            }
        }

        @Override
        public void onLoadMore() {
        }
    };

    /**
     * 更新列表1数据
     */
    private void updateFirLocalListData() {
        try {
            mEntityList1 = dbInterface.getFavoriteListByType(CollectTypeEnum.PD.value());
            mAdapter1.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 请求第一个列表本地数据
     */
    private void requestFirLocalListData() {

        mEntityList1 = DBInterface.getInstance().getFavoriteListByType(CollectTypeEnum.PD.value());
        dealList1Data();
    }

    /**
     * 请求第一个列表数据
     */
    private void requestFirListData() {
        params.clear();
        params.put("collect_date", null);
        params.put("user_id", mUserManager.getUserId());
        params.put("collect_type", "1");
        params.put("pagenum", "1");
        params.put("pagesize", "1000");
        mHandler1
                .getHttpJson(
                        mHandler1.appendParameter(
                                WebAddressEnum.PERSONAL_COLLECT_GET.toString(),
                                params), ResponseData.class, LIST_DATA1);
    }

    /**
     * 请求第二个列表本地数据
     */
    private void requestSecLocalListData() {
        try {
            mEntityList2 = dbInterface.getFavoriteListByType(
                    CollectTypeEnum.TW.value(),
                    CollectTypeEnum.SP.value(),
                    CollectTypeEnum.LkTW.value());
            dealList2Data();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求第二个列表数据
     */
    private void requestSecListData() {
        params.clear();
        params.put("collect_date", null);
        params.put("user_id", mUserManager.getUserId());
        params.put("collect_type", "1");
        params.put("pagenum", "1");
        params.put("pagesize", "1000");
        mHandler1
                .getHttpJson(
                        mHandler1.appendParameter(
                                WebAddressEnum.PERSONAL_COLLECT_GET.toString(),
                                params), ResponseData.class, LIST_DATA2);
    }

    /**
     * 向本地发送删除请求
     */
    private void requestLocalDel1Data() {
        try {

            dbInterface.batchDeleteFavorate(chkEntityList1);
            dealDelList1Data();
        } catch (Exception e) {
            e.printStackTrace();
            dismissLoadDialog();
        }
    }

    /**
     * 向本地发送删除请求
     */
    private void requestLocalDel2Data() {
        try {
            dbInterface.batchDeleteFavorate(chkEntityList2);
            dealDelList2Data();
        } catch (Exception e) {
            e.printStackTrace();
            dismissLoadDialog();
        }
    }

    /**
     * 向服务器发送删除请求
     */
    private void requestDel1Data() {
        params.clear();
        JSONObject object = new JSONObject();
        try {
            object.put("collect_id", null);
            object.put("user_id", userId != null ? Integer.parseInt(userId)
                    : null);
            object.put("collect_type", CollectTypeEnum.SP.value());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("data", object.toString());
        mHandler1.getHttpPostJson(
                WebAddressEnum.CCTV_COLLECT_CANCEL.toString(), params,
                ResponseData.class, DEL_DATA1);

    }

    /**
     * 向服务器发送删除请求
     */
    private void requestDel2Data() {
        params.clear();
        JSONObject object = new JSONObject();
        try {
            object.put("collect_id", null);
            object.put("user_id", userId != null ? Integer.parseInt(userId)
                    : null);
            object.put("collect_type", CollectTypeEnum.SP.value());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("data", object.toString());
        mHandler1.getHttpPostJson(
                WebAddressEnum.CCTV_COLLECT_CANCEL.toString(), params,
                ResponseData.class, DEL_DATA2);
    }

    /**
     * 处理列表1数据
     *
     * @param
     */
    private void dealList1Data() {
        try {
            if (isRefresh) {
                if (flag1 == 1 || flag1 == 2) {
                    // 执行取消或完成，按钮文字变成编辑，取消选择框
                    setRefreshUI(mEntityList1, chkEntityList1, true);
                } else {
                    setRefreshUI(mEntityList1, chkEntityList1, false);
                }

            }
            mAdapter1 = new PersonalShouCangZBoListViewAdapter(getActivity(),
                    mEntityList1, onCheckedChangeListener1);
            if (mEntityList1 != null && mEntityList1.size() > 0) {
                mListView1.setAdapter(mAdapter1);
                titleRight.setVisibility(View.VISIBLE);
                if (!isRefresh) {
                    titleRight.setText(getResources().getString(R.string.edit));
                }
                noData1View.setVisibility(View.GONE);
            } else {
                // 无匹配数据时的处理
                titleRight.setVisibility(View.GONE);
                titleRight2.setVisibility(View.GONE);
                noData1View.setVisibility(View.VISIBLE);
            }
            if (isRefresh) {
                // 停止下拉刷新
                onLoad1();
                isRefresh = false;
            }
        } catch (Exception e) {
        }
        dismissLoadDialog();
    }

    /**
     * 处理列表2数据
     */
    private void dealList2Data() {
        try {
            if (isRefresh) {
                if (flag2 == 1 || flag2 == 2) {
                    // 执行取消或完成，按钮文字变成编辑，取消选择框
                    setRefreshUI(mEntityList2, chkEntityList2, true);
                } else {
                    setRefreshUI(mEntityList2, chkEntityList2, false);
                }

            }
            mAdapter2 = new PersonalShouCangKDListViewAdapter(getActivity(),
                    mEntityList2, onCheckedChangeListener2);
            if (mEntityList2 != null && mEntityList2.size() > 0) {
                mListView2.setAdapter(mAdapter2);
                titleRight2.setVisibility(View.VISIBLE);
                if (!isRefresh) {
                    titleRight2
                            .setText(getResources().getString(R.string.edit));
                }
                noData2View.setVisibility(View.GONE);
            } else {
                // 无匹配数据时的处理
                titleRight2.setVisibility(View.GONE);
                noData2View.setVisibility(View.VISIBLE);
            }
            if (isRefresh) {
                // 停止下拉刷新
                onLoad2();
                isRefresh = false;
            }
        } catch (Exception e) {
         //   L.e(TAG, e.toString());
        }
        dismissLoadDialog();
    }

    private void onLoad1() {
        mListView1.stopRefresh();
        mListView1.stopLoadMore();
        mListView1.setRefreshTime(TimeHelper.getCurrentData());
    }

    private void onLoad2() {
        mListView2.stopRefresh();
        mListView2.stopLoadMore();
        mListView2.setRefreshTime(TimeHelper.getCurrentData());
    }








    private void setZbDelBtn() {
        if (chkEntityList1 != null && chkEntityList1.size() > 0) {
            zbDelView.setText(getString(R.string.delete) + chkEntityList1.size());
            zbDelView.setEnabled(true);
            if(chkEntityList1.size() < mEntityList1.size()){
                zbAllView.setText(getString(R.string.select_all));
                zbAllFlag = 0;

            }else{
                zbAllView.setText(getString(R.string.cancel_select_all));
                zbAllFlag = 1;
            }
        } else {
            zbDelView.setText(getString(R.string.delete));
            zbDelView.setEnabled(false);
        }
    }

    private void setKdDelBtn() {
        if (chkEntityList2 != null && chkEntityList2.size() > 0) {
            kdDelView.setText(getString(R.string.delete) + chkEntityList2.size());
            kdDelView.setEnabled(true);
            if(chkEntityList2.size() < mEntityList2.size()){
                kdAllView.setText(getString(R.string.select_all));
                kdAllFlag = 0;
            }else{
                kdAllView.setText(getString(R.string.cancel_select_all));
                kdAllFlag = 1;
            }
        } else {
            kdDelView.setText(getString(R.string.delete));
            kdDelView.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivNoNet:
                if (NetUtil.isNetConnected(getActivity())) {
                    noNetView.setVisibility(View.GONE);
                    if (!StringUtil.isNullOrEmpty(userId)) {
                        // 如果当前登录有用户，从服务器获取收藏的列表数据
                        requestFirListData();
                    } else {
                        // 如果当前登录无用户，从本地获取收藏的列表数据
                        requestFirLocalListData();
                    }
                }
                break;
            case R.id.common_title_right:
                // 直播
                titleRight.setText(getResources().getString(R.string.cancel));
                if (flag1 == 0) {
                    // 执行编辑，按钮文字变成取消，显示选择框，底部的操作按钮可见，
                    setSelOrCancelUI(true, false);
                    flag1 = 1;
                    zbBottomView.setVisibility(View.VISIBLE);
                } else if (flag1 == 1 || flag1 == 2) {
                    // 执行取消或完成，按钮文字变成编辑，取消选择框
                    titleRight.setText(getResources().getString(R.string.edit));
                    setSelOrCancelUI(false, false);
                    chkEntityList1.clear();
                    zbAllView.setText(getString(R.string.select_all));
                    flag1 = 0;
                    zbAllFlag = 0;
                    zbBottomView.setVisibility(View.GONE);
                }
                break;
            case R.id.common_title_right2:
                // 精彩看点
                if (flag2 == 0) {
                    // 执行编辑，按钮文字变成取消，显示选择框
                    titleRight2.setText(getResources().getString(R.string.cancel));
                    setSelOrCancelUI(true, false);
                    flag2 = 1;
                    kdBottomView.setVisibility(View.VISIBLE);
                } else if (flag2 == 1 || flag2 == 2) {
                    // 执行取消或完成，按钮文字变成编辑，取消选择框
                    titleRight2.setText(getResources().getString(R.string.edit));
                    setSelOrCancelUI(false, false);
                    flag2 = 0;
                    kdAllFlag = 0;
                    chkEntityList2.clear();
                    kdAllView.setText(getString(R.string.select_all));
                    kdBottomView.setVisibility(View.GONE);
                }
                break;
            case R.id.personal_sc_all:
                // 点击直播的全选
                if (zbAllFlag == 0) {
                    // 执行全选
                    zbAllView.setText(getString(R.string.cancel_select_all));
                    setSelOrCancelUI(true, true);
                    zbAllFlag = 1;
                } else if (zbAllFlag == 1) {
                    // 执行取消全选
                    zbAllView.setText(getString(R.string.select_all));
                    setSelOrCancelUI(true, false);
                    zbAllFlag = 0;
                }
                setZbDelBtn();
                break;
            case R.id.personal_sc_delete:
                // 点击直播的删除
                ToastUtil.showPpw(v, mInflater, getResources().getString(R.string.del_tip),
                        new IAlertDialog<ToastUtil>() {
                            @Override
                            public void positive() {
                                showLoadingDialog();
                                // if (!StringUtil.isNullOrEmpty(userId)) {
                                // // 如果当前登录有用户，从服务器进行删除，删除成功后刷新页面UI
                                // requestDel1Data();
                                // } else {
                                // 如果当前登录无用户，从本地进行删除，删除成功后刷新页面UI
                                requestLocalDel1Data();
                                // }
                            }

                            @Override
                            public void negative() {

                            }
                        }, "确定", "取消");
                break;
            case R.id.personal_sc_kandian_all:

                // 点击精彩看点的全选
                //Log.i("TAGG",""+kdAllFlag);
                if (kdAllFlag == 0) {
                    // 执行全选
                    kdAllView.setText(getString(R.string.cancel_select_all));
                    setSelOrCancelUI(true, true);
                    kdAllFlag = 1;
                } else if (kdAllFlag == 1) {
                    // 执行取消全选
                    kdAllView.setText(getString(R.string.select_all));
                    setSelOrCancelUI(true, false);
                    kdAllFlag = 0;
                }
                setKdDelBtn();
                break;
            case R.id.personal_sc_kandian_delete:
                // 点击精彩看点的删除
                ToastUtil.showPpw(v, mInflater, getResources().getString(R.string.del_tip),
                        new IAlertDialog<ToastUtil>() {
                            @Override
                            public void positive() {
                                showLoadingDialog();
                                // if (!StringUtil.isNullOrEmpty(userId)) {
                                // // 如果当前登录有用户，从服务器进行删除，删除成功后刷新页面UI
                                // requestDel2Data();
                                // } else {
                                // 如果当前登录无用户，从本地进行删除，删除成功后刷新页面UI
                                requestLocalDel2Data();
                                // }
                            }

                            @Override
                            public void negative() {

                            }
                        }, "确定", "取消");
                break;
        }
    }

    private void dealDelList1Data() {
        if (mEntityList1 != null) {
            mEntityList1.removeAll(chkEntityList1);
            mAdapter1.notifyDataSetChanged();
            chkEntityList1.clear();
            if (mEntityList1.size() == 0) {
                // 全部删除了，显示无数据图片
                noData1View.setVisibility(View.VISIBLE);
                // 不显示编辑按钮
                titleRight.setVisibility(View.GONE);
                titleRight2.setVisibility(View.GONE);
            } else {
                noData1View.setVisibility(View.GONE);
                // 删除了一部分，取消全选按钮设置文字为“全选”，顶部右侧的按钮文字设置为“完成”
                flag1 = 2;
                titleRight.setText(getString(R.string.finish));
                zbAllFlag = 0;
                zbAllView.setText(getString(R.string.select_all));
                // 设置仍然选中的个数
                setZbDelBtn();
            }
        }
        dismissLoadDialog();
    }

    private void dealDelList2Data() {
        if (mEntityList2 != null) {
            mEntityList2.removeAll(chkEntityList2);
            mAdapter2.notifyDataSetChanged();
            chkEntityList2.clear();
            if (mEntityList2.size() == 0) {
                // 全部删除了，显示无数据图片
                noData2View.setVisibility(View.VISIBLE);
                titleRight.setVisibility(View.GONE);
                titleRight2.setVisibility(View.GONE);
            } else {
                noData2View.setVisibility(View.GONE);
                // 删除了一部分，取消全选按钮设置文字为“全选”，顶部右侧的按钮文字设置为“完成”
                flag2 = 2;
                titleRight2.setText(getString(R.string.finish));
                kdAllFlag = 0;
                kdAllView.setText(getString(R.string.select_all));
                // 设置仍然选中的个数
                setKdDelBtn();
            }
        }
        dismissLoadDialog();
    }

    class ViewClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.common_title_left:
                    getActivity().finish();
                    break;
            }
        }
    }

    /**
     * 设置下拉刷新后的复选框保持刷新前的选中状态
     *
     * @param entityList
     * @param chkEntityList
     */
    private void setRefreshUI(List<FavoriteEntity> entityList,
                              List<FavoriteEntity> chkEntityList, boolean visiabled) {
        if (entityList != null && entityList.size() > 0) {
            FavoriteEntity entity;
            FavoriteEntity chkEntity;
            for (int i = 0; i < entityList.size(); i++) {
                entity = entityList.get(i);
                if (entity == null) {
                    continue;
                }
                entity.setIsShowEditUi(visiabled);
                entity.setIsChecked(false);
                if (chkEntityList != null && chkEntityList.size() > 0) {
                    for (int j = 0; j < chkEntityList.size(); j++) {
                        chkEntity = chkEntityList.get(j);
                        if (entity.getCollect_id().equals(
                                chkEntity.getCollect_id())) {
                            entity.setIsChecked(true);
                            break;
                        }
                    }
                }

            }
        }
    }

    /**
     * 针对当前选中的tab，在编辑或取消状态下，刷新列表
     *
     * @param
     * @param
     */
    private void setSelOrCancelUI(boolean visiableFlag, boolean checkFlag) {
        int currentItem = mViewPager.getCurrentItem();
        boolean isFirActivedTab =  mSelectPosition== 0;
        List<FavoriteEntity> entityList = isFirActivedTab ? mEntityList1
                : mEntityList2;
        // 点的是编辑、取消或者取消全选
        if (isFirActivedTab) {
            // 第一个tab
            chkEntityList1.clear();
        } else {
            // 第二个tab
            chkEntityList2.clear();
        }
        if (checkFlag) {
            // 点击的是全选
            if (isFirActivedTab) {
                // 第一个tab
                chkEntityList1.addAll(mEntityList1);
            } else {
                // 第二个tab
                chkEntityList2.addAll(mEntityList2);
            }
        }

        if (entityList != null && entityList.size() > 0) {
            FavoriteEntity entity;
            for (int i = 0; i < entityList.size(); i++) {
                entity = entityList.get(i);
                entity.setIsShowEditUi(visiableFlag);
                entity.setIsChecked(checkFlag);
            }
            if (isFirActivedTab) {
                // 刷新直播的列表适配器
                mAdapter1.notifyDataSetChanged();
            } else {
                // 刷新看点的列表适配器
                mAdapter2.notifyDataSetChanged();
            }
        }
    }



}
