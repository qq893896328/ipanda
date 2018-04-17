package cn.cntv.app.ipanda.ui.home.listener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.gridsum.mobiledissector.MobileAppTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.CollectPageSourceEnum;
import cn.cntv.app.ipanda.constant.CollectTypeEnum;
import cn.cntv.app.ipanda.constant.HomeTypeEnum;
import cn.cntv.app.ipanda.constant.JMPType2Enum;
import cn.cntv.app.ipanda.constant.JMPTypeEnum;
import cn.cntv.app.ipanda.ui.cctv.activity.CCTVDetailActivity;
import cn.cntv.app.ipanda.ui.home.activity.HomeSSubjectActivity;
import cn.cntv.app.ipanda.ui.home.activity.InterDetailActivity;
import cn.cntv.app.ipanda.ui.home.entity.AdapterData;
import cn.cntv.app.ipanda.ui.home.entity.BigImg;
import cn.cntv.app.ipanda.ui.home.entity.GroupData;
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
import cn.cntv.app.ipanda.ui.pandaeye.activity.PandaEyeDetailActivity;
import cn.cntv.app.ipanda.ui.play.PlayLiveActivity;
import cn.cntv.app.ipanda.ui.play.PlayVodFullScreeActivity;
import cn.cntv.app.ipanda.ui.play.entity.PlayLiveEntity;
import cn.cntv.app.ipanda.ui.play.entity.PlayVodEntity;

/**
 * @author Xiao JinLai
 * @ClassName: HomeItemListener
 * @Date Dec 31, 2015 4:33:30 PM
 * @Description：Home adapter item click listener
 */
public class HomeItemListener implements OnClickListener {

    private  String tContent;
    private Context mContext;
    private ArrayList<AdapterData> mDatas;

    public HomeItemListener(Context context, ArrayList<AdapterData> datas) {

        this.mContext = context;
        this.mDatas = datas;
    }

    public void setDatas(ArrayList<AdapterData> datas) {

        this.mDatas = datas;
    }

    @Override
    public void onClick(View v) {

        if (v.getTag() == null) {

            return;
        }

        String[] tTags = v.getTag().toString().split(",");

        if (tTags == null || tTags.length == 0) {

            return;
        }

        int tPosition = Integer.parseInt(tTags[0]);
        int tIndex = Integer.parseInt(tTags[1]);

        if (mDatas == null || mDatas.isEmpty()) {
            return;
        }

        AdapterData tData = mDatas.get(tPosition);

        if (tData.getAdapterType() == HomeTypeEnum.GROUP_TYPE.value()) {

            GroupData tGroupData = (GroupData) tData;

            String tUrl = tGroupData.getUrl();
            if (tUrl != null && !tUrl.equals("")) {

                // 专题页面
                HashMap<String, String> tHashMap = new HashMap<String, String>();
                tHashMap.put("url", tUrl);
                tHashMap.put("title", tGroupData.getTitle());
                //专题页面统计
                MobileAppTracker.trackEvent(tGroupData.getTitle(), "列表", "首页", 0, null, "图文浏览", mContext);
                MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);

                startIntent(HomeSSubjectActivity.class, tHashMap);
            }
        } else if (tData.getAdapterType() == HomeTypeEnum.BIGIMG_TYPE.value()) {
           tContent   = "大图推荐";
            // 轮播大图点击事件
            HomeAdapterBigImg tAdapterBigImg = (HomeAdapterBigImg) tData;

            if (tAdapterBigImg != null
                    && !tAdapterBigImg.getBigImgs().isEmpty()) {

                BigImg tBigImg = tAdapterBigImg.getBigImgs().get(tIndex);

                String tId = tBigImg.getId();
                String tStype = tBigImg.getStype();
                String tType = tBigImg.getType();
                String tTitle = tBigImg.getTitle();
                String tPid = tBigImg.getPid();
                String tVid = tBigImg.getVid();
                String tImg = tBigImg.getImage();
                String tUrl = tBigImg.getUrl();


                if (tStype.equals(JMPTypeEnum.JMP_CCTV.toString())) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "大图推荐", "首页", 0, tVid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"大图推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tVid);
                    // 央视名栏底层页
                    setCCTV(tVid, tTitle, tImg, null);

                }
                if (tPid != null && !tPid.equals("") && tVid != null
                        && !tVid.equals("")) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "大图推荐", "首页", 0, tVid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"大图推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tVid);
                    // 点播视频集
                    setVideoList(tPid,tVid,  tTitle, null, tImg);

                } else if (tPid != null && !tPid.equals("")) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "大图推荐", "首页", 0, tPid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"大图推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tPid);
                    // 点播单视频
                    setVodVideo(tPid, tTitle, null,tImg);

                } else if (tVid != null && !tVid.equals("")) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "大图推荐", "首页", 0, tVid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"大图推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tVid);
                    // 央视名栏底层页
                    setCCTV(tVid, tTitle, tImg, null);

                } else if (tType.equals(JMPTypeEnum.VIDEO_LIVE.toString())) {

                    int tPage = 0;

                    if (tStype.equals("1") || tStype.equals("2")) {

                        tPage = CollectPageSourceEnum.XMZB.value();
                    } else if (tStype.equals("3") || tStype.equals("4")) {

                        tPage = CollectPageSourceEnum.ZBZG.value();
                    } else if (tStype.equals("5")) {

                        tPage = CollectPageSourceEnum.PDZB.value();
                    }
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "大图推荐", "首页", 0, tId, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"大图推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tId);
                    // 直播视频
                    setLiveVideo(tId,tTitle, tImg, tPage);

                } else if (tType.equals(JMPTypeEnum.VIDEO_BODY.toString())) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "大图推荐", "首页", 0, tId, "图文浏览", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"大图推荐"+"事件标签:"+"首页"+"类型:"+"图文浏览"+"*****ID="+tId);

                    // 熊猫观察图文底层页
                    setTeleText(tId, tTitle, tUrl, tImg, null);

                } else if (tBigImg.getType().equals(
                        JMPTypeEnum.VIDEO_BODY6.toString())) {

                    HashMap<String, String> tHashMap = new HashMap<String, String>();
                    tHashMap.put("url", tUrl);
                    tHashMap.put("title", tTitle);
                    tHashMap.put("image", tImg);
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "大图推荐", "首页", 0, tUrl, "图文浏览", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"大图推荐"+"事件标签:"+"首页"+"类型:"+"图文浏览"+"*****ID="+tUrl);
                    startIntent(InterDetailActivity.class, tHashMap);

                } else if (tType.equals(JMPTypeEnum.VIDEO_SSUBJECT.toString())) {
                    tContent   = "列表";
                    // 专题页面
                    HashMap<String, String> tHashMap = new HashMap<String, String>();
                    tHashMap.put("url", tUrl);
                    tHashMap.put("title", tTitle);
                //统计
                    MobileAppTracker.trackEvent(tTitle, "大图推荐", "首页", 0, tUrl, "图文浏览", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"大图推荐"+"事件标签:"+"首页"+"类型:"+"图文浏览"+"*****ID="+tUrl);
                    startIntent(HomeSSubjectActivity.class, tHashMap);

                } else {

                    dataError();
                }
            }
            //专题推荐
        } else if (tData.getAdapterType() == HomeTypeEnum.HOME_VIDEOLIST1_TYPE
                .value()) {
            tContent   = "列表";
            HomeAdapterVideoList1 tVideoList1 = (HomeAdapterVideoList1) tData;

            if (tVideoList1 != null && !tVideoList1.getVideoLists().isEmpty()) {

                HomeVideoList tVideoList = tVideoList1.getVideoLists().get(
                        tIndex);

                String tId = tVideoList.getId();
                String tVid = tVideoList.getVid();
                String tPid = tVideoList.getPid();
                String tTitle = tVideoList.getTitle();
                String tType = tVideoList.getId().substring(0, 4);
                String tUrl = tVideoList.getUrl();
                String tImg = tVideoList.getImage();
                String tVideoLenth = tVideoList.getVideoLength();
                String tTimeval = null;


                if (tPid != null && !tPid.equals("") && tVid != null
                        && !tVid.equals("")) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tVid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"综合推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tVid);
                    // 点播视频集
                    setVideoList(tPid, tVid, tTitle, tVideoLenth, tImg);

                } else if (tPid != null && !tPid.equals("")) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tPid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"综合推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tPid);
                    // 点播单视频
                    setVodVideo(tPid, tTitle, tVideoLenth, tImg);

                } else if (tVid != null && !tVid.equals("")) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tVid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"综合推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tVid);
                    // 央视名栏底层页
                    setCCTV(tVid, tTitle, tImg, tVideoLenth);

                } else if (tType.equals(JMPType2Enum.TELETEXT.toString())) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tId, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"***事件类别:"+"综合推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+tId);
                    // 熊猫观察图文底层页
                    setTeleText(tId, tTitle, tUrl, tImg, tTimeval);

                } else {
                    dataError();
                }
            }

        } else if (tData.getAdapterType() == HomeTypeEnum.HOME_VIDEOLIST2_TYPE
                .value()) {
            tContent   = "列表";
            HomeAdapterVideoList2 tVideoList2 = (HomeAdapterVideoList2) tData;

            if (tVideoList2 != null && tVideoList2.getVideoList() != null) {

                HomeVideoList tVideoList = tVideoList2.getVideoList();

                String tId = tVideoList.getId();
                String tVid = tVideoList.getVid();
                String tPid = tVideoList.getPid();
                String tTitle = tVideoList.getTitle();
                String tType = tVideoList.getId().substring(0, 4);
                String tUrl = tVideoList.getUrl();
                String tImg = tVideoList.getImage();
                String tVideoLenth = tVideoList.getVideoLength();
                String tTimeval = null;

                if (tPid != null && !tPid.equals("") && tVid != null
                        && !tVid.equals("")) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tVid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"综合推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tVid);
                    // 点播视频集
                    setVideoList(tPid, tVid, tTitle, tVideoLenth, tImg);

                } else if (tPid != null && !tPid.equals("")) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tPid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"综合推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tPid);
                    // 点播单视频
                    setVodVideo(tPid, tTitle, tVideoLenth, tImg);

                } else if (tVid != null && !tVid.equals("")) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tVid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"综合推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tVid);
                    // 央视名栏底层页
                    setCCTV(tVid, tTitle, tImg, tVideoLenth);

                } else if (tType.equals(JMPType2Enum.TELETEXT.toString())) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tId, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"综合推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tId);
                    // 熊猫观察图文底层页
                    setTeleText(tId, tTitle, tUrl, tImg, tTimeval);


                } else {
                    dataError();
                }
            }
            //特别策划
        } else if (tData.getAdapterType() == HomeTypeEnum.HOME_SSUBJECT_TYPE
                .value()) {
            tContent   = "列表";
            HomeRecoSSubject tSSubject = (HomeRecoSSubject) tData;

            HashMap<String, String> tHashMap = new HashMap<String, String>();
            tHashMap.put("url", tSSubject.getListurl());
            tHashMap.put("title", tSSubject.getTitle());
            //统计
            MobileAppTracker.trackEvent(tSSubject.getTitle(), "互动专题推荐", "首页", 0, tSSubject.getListurl(), "图文浏览", mContext);
            MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
            Log.e("统计","事件名称:"+tSSubject.getTitle()+"事件类别:"+"互动专题推荐"+"事件标签:"+"首页"+"类型:"+"图文浏览"+"*****ID="+tSSubject.getListurl());
            startIntent(HomeSSubjectActivity.class, tHashMap);

        } else if (tData.getAdapterType() == HomeTypeEnum.HOME_PANDAEYE_TYPE
                .value()) {
            tContent   = "列表";
            HomePandaEye tPandaEye = (HomePandaEye) tData;

            if (tPandaEye != null && !tPandaEye.getItems().isEmpty()) {

                HomePandaEyeItem tPandaEyeItem = tPandaEye.getItems().get(
                        tIndex);

                String tId = tPandaEyeItem.getId();
                String tPid = tPandaEyeItem.getPid();
                String tVid = tPandaEyeItem.getVid();
                String tTitle = tPandaEyeItem.getTitle();
                String tType = tPandaEyeItem.getId().substring(0, 4);
                String tUrl = tPandaEyeItem.getUrl();
                String tVideoLenth = tPandaEyeItem.getVideoLength();
                String image = tPandaEyeItem.getImage();


                if (tPid != null && !tPid.equals("") && tVid != null
                        && !tVid.equals("")) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "熊猫观察推荐", "首页", 0, tVid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"熊猫观察推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tVid);
                    // 点播视频集
                    setVideoList(tPid, tVid, tTitle, "", "");

                } else if (tPid != null && !tPid.equals("")) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "熊猫观察推荐", "首页", 0, tPid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"熊猫观察推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tPid);
                    // 点播单视频
                    setVodVideo(tPid, tTitle, "", "");

                } else if (tVid != null && !tVid.equals("")) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "熊猫观察推荐", "首页", 0, tVid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"熊猫观察推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tVid);
                    // 央视名栏底层页
                    setCCTV(tVid, tTitle, "", "");
                } else if (tType.equals(JMPType2Enum.TELETEXT.toString())) {
                    Log.i("aaaa",tVideoLenth+"**"+image+"**"+tTitle+"**"+tUrl);
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "熊猫观察推荐", "首页", 0, tId, "图文浏览", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"事件类别:"+"熊猫观察推荐"+"事件标签:"+"首页"+"类型:"+"图文浏览"+"*****ID="+tId);
                    // 熊猫图文页面
                    setTeleText(tId, tTitle, tUrl, image, tVideoLenth);
                } else {
                    dataError();
                }
            }

        } else if (tData.getAdapterType() == HomeTypeEnum.HOME_PANDAEYE2_TYPE
                .value()) {
            tContent   = "列表";
            HomePandaEye2 tPandaEye2 = (HomePandaEye2) tData;

            String tId = tPandaEye2.getId();
            String tPid = tPandaEye2.getPid();
            String tVid = tPandaEye2.getVid();
            String tTitle = tPandaEye2.getTitle();
            String tType = tPandaEye2.getId().substring(0, 4);
            String tUrl = tPandaEye2.getUrl();
            String tImg = tPandaEye2.getImage();
            String tVideoLenth = tPandaEye2.getVideoLength();




            if (tPid != null && !tPid.equals("") && tVid != null
                    && !tVid.equals("")) {
                //统计
                MobileAppTracker.trackEvent(tTitle, "熊猫观察推荐", "首页", 0, tId, "视频观看", mContext);
                MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                Log.e("统计","事件名称:"+tTitle+"事件类别:"+"熊猫观察推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tId);
                // 点播视频集
                setVideoList(tPid, tVid, tTitle, tVideoLenth, tImg);
            } else if (tPid != null && !tPid.equals("")) {
                //统计
                MobileAppTracker.trackEvent(tTitle, "熊猫观察推荐", "首页", 0, tPid, "视频观看", mContext);
                MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                Log.e("统计","事件名称:"+tTitle+"事件类别:"+"熊猫观察推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tPid);
                // 点播单视频
                setVodVideo(tPid, tTitle, tVideoLenth, tImg);
            } else if (tVid != null && !tVid.equals("")) {
                //统计
                MobileAppTracker.trackEvent(tTitle, "熊猫观察推荐", "首页", 0, tVid, "视频观看", mContext);
                MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                Log.e("统计","事件名称:"+tTitle+"事件类别:"+"熊猫观察推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tVid);
                // 央视名栏底层页
                setCCTV(tVid, tTitle, null, tVideoLenth);
            } else if (tType.equals(JMPType2Enum.TELETEXT.toString())) {

                //统计
                MobileAppTracker.trackEvent(tTitle, "熊猫观察推荐", "首页", 0, tId, "图文浏览", mContext);
                MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                Log.e("统计","事件名称:"+tTitle+"事件类别:"+"熊猫观察推荐"+"事件标签:"+"首页"+"类型:"+"图文浏览"+"*****ID="+tId);


                // 熊猫图文页面
                setTeleText(tId, tTitle, tUrl, tImg, tVideoLenth);
            } else {
                dataError();
            }

        } else if (tData.getAdapterType() == HomeTypeEnum.HOME_PANDALIVE_TYPE
                .value()) {
            tContent   = "列表";
            HomeAdapterPandaLive tAdapterHotLive = (HomeAdapterPandaLive) tData;

            if (tAdapterHotLive != null
                    && !tAdapterHotLive.getHotlives().isEmpty()) {

                HomeHotLiveList tHotlive = tAdapterHotLive.getHotlives().get(
                        tIndex);
                //统计
                MobileAppTracker.trackEvent(tHotlive.getTitle(), "熊猫直播推荐", "首页", 0, tHotlive.getId(), "视频观看", mContext);
                MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                Log.e("统计","事件名称:"+tHotlive.getTitle()+"事件类别:"+"熊猫直播推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tHotlive.getId());
                // 直播视频
                setLiveVideo(tHotlive.getId(), tHotlive.getTitle(),
                        tHotlive.getImage(), CollectPageSourceEnum.XMZB.value());


            }

        } else if (tData.getAdapterType() == HomeTypeEnum.HOME_GREATWALL_TYPE
                .value()) {
            tContent   = "列表";
            HomeAdapterWallLive tAdapterHotLive = (HomeAdapterWallLive) tData;

            if (tAdapterHotLive != null
                    && !tAdapterHotLive.getHotlives().isEmpty()) {

                HomeHotLiveList tHotlive = tAdapterHotLive.getHotlives().get(
                        tIndex);
                //统计
                MobileAppTracker.trackEvent(tHotlive.getTitle(), "长城直播推荐", "首页", 0, tHotlive.getId(), "视频观看", mContext);
                MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                Log.e("统计","事件名称:"+tHotlive.getTitle()+"事件类别:"+"长城直播推荐"+"事件标签:"+"首页"+"类型:"+"视频观看"+"*****ID="+tHotlive.getId());
                // 直播视频
                setLiveVideo(tHotlive.getId(), tHotlive.getTitle(),
                        tHotlive.getImage(), CollectPageSourceEnum.ZBZG.value());

            }

        } else if (tData.getAdapterType() == HomeTypeEnum.HOME_LIVECHINA_TYPE
                .value()) {
            tContent   = "列表";
            HomeAdapterChinaLive tAdapterHotLive = (HomeAdapterChinaLive) tData;

            if (tAdapterHotLive != null
                    && !tAdapterHotLive.getHotlives().isEmpty()) {

                HomeHotLiveList tHotlive = tAdapterHotLive.getHotlives().get(
                        tIndex);

                //统计
                MobileAppTracker.trackEvent(tHotlive.getTitle(), "直播中国推荐", "首页", 0, tHotlive.getId(), "视频观看", mContext);
                MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                Log.e("统计","事件名称:"+tHotlive.getTitle()+"***事件类别:"+"直播中国推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+tHotlive.getId());
                // 直播视频
                setLiveVideo(tHotlive.getId(), tHotlive.getTitle(),
                        tHotlive.getImage(), CollectPageSourceEnum.ZBZG.value());
            }

        } else if (tData.getAdapterType() == HomeTypeEnum.INTERACTIVEONE_TYPE
                .value()) {
            tContent   = "列表";
            InteractionOne tHomeInteractionOne = (InteractionOne) tData;

            if (tHomeInteractionOne != null
                    && tHomeInteractionOne.getInteraction() != null) {

                Interaction tInteraction = tHomeInteractionOne.getInteraction();
                HashMap<String, String> tHashMap = new HashMap<String, String>();
                tHashMap.put("url", tInteraction.getUrl());
                tHashMap.put("title", tInteraction.getTitle());
                tHashMap.put("image", tInteraction.getImage());
                //统计
                MobileAppTracker.trackEvent(tInteraction.getTitle(), "互动专题推荐", "首页", 0, tInteraction.getUrl(), "图文浏览", mContext);
                MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                Log.e("统计","事件名称:"+tInteraction.getTitle()+"事件类别:"+"互动专题推荐"+"事件标签:"+"首页"+"类型:"+"图文浏览"+"*****ID="+tInteraction.getUrl());
                startIntent(InterDetailActivity.class, tHashMap);
            }

        } else if (tData.getAdapterType() == HomeTypeEnum.INTERACTIVETWO_TYPE
                .value()) {
            tContent   = "列表";
            InteractionTwo tInteractionTwo = (InteractionTwo) tData;

            if (tInteractionTwo != null
                    && !tInteractionTwo.getInteraction().isEmpty()) {

                Interaction tInteraction = tInteractionTwo.getInteraction()
                        .get(tIndex);

                HashMap<String, String> tHashMap = new HashMap<String, String>();
                tHashMap.put("url", tInteraction.getUrl());
                tHashMap.put("title", tInteraction.getTitle());
                tHashMap.put("image", tInteraction.getImage());
                //统计
                MobileAppTracker.trackEvent(tInteraction.getTitle(), "互动专题推荐", "首页", 0, tInteraction.getUrl(), "图文浏览", mContext);
                MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                Log.e("统计","事件名称:"+tInteraction.getTitle()+"事件类别:"+"互动专题推荐"+"事件标签:"+"首页"+"类型:"+"图文浏览"+"*****ID="+tInteraction.getUrl());
                startIntent(InterDetailActivity.class, tHashMap);
            }

        } else if (tData.getAdapterType() == HomeTypeEnum.HOME_LIVE_TYPE
                .value()) {
            tContent   = "列表";
            HomeAdapterLive tAdapterLive = (HomeAdapterLive) tData;

            if (tAdapterLive != null
                    && !tAdapterLive.getHomeLiveLists().isEmpty()) {

                HomeLiveList tLiveList = tAdapterLive.getHomeLiveLists().get(
                        tIndex);
                //统计
                MobileAppTracker.trackEvent(tLiveList.getTitle(), "CCTV栏目推荐", "首页", 0, tLiveList.getId(), "视频观看", mContext);
                MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                Log.e("统计","事件名称:"+tLiveList.getTitle()+"***事件类别:"+"CCTV栏目推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+tLiveList.getId());
                // 直播视频
                setLiveVideo(tLiveList.getId(), tLiveList.getTitle(), null,
                        CollectPageSourceEnum.PDZB.value());
            }

        } else if (tData.getAdapterType() == HomeTypeEnum.HOME_LIVE2_TYPE
                .value()) {
            tContent   = "列表";
            HomeLive2 tLive2 = (HomeLive2) tData;

            if (tLive2 != null && !tLive2.getLive2().isEmpty()) {

                HomeStyleData tStyleData = tLive2.getLive2().get(tIndex);

                String tId = tStyleData.getId();
                String tVid = tStyleData.getVid();
                String tPid = tStyleData.getPid();
                String tTitle = tStyleData.getTitle();
                String tType = tStyleData.getId().substring(0, 4);
                String tUrl = tStyleData.getUrl();
                String tImg = tStyleData.getImage();
                String tVideoLenth = tStyleData.getVideoLength();
                String tTimeval = null;



                if (tPid != null && !tPid.equals("") && tVid != null
                        && !tVid.equals("")) {

                    // 点播视频集
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tVid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"***事件类别:"+"综合推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+tVid);
                    setVideoList(tPid, tVid, tTitle, tVideoLenth, tImg);
                } else if (tPid != null && !tPid.equals("")) {

                    // 点播单视频
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tPid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"***事件类别:"+"综合推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+tPid);
                    setVodVideo(tPid, tTitle, tVideoLenth, tImg);
                } else if (tVid != null && !tVid.equals("")) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "CCTV栏目推荐", "首页", 0, tVid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"***事件类别:"+"CCTV栏目推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+tVid);
                    // 央视名栏底层页
                    setCCTV(tVid, tTitle, tImg, tVideoLenth);
                } else if (tType.equals(JMPType2Enum.TELETEXT.toString())) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tId, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"***事件类别:"+"综合推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+tId);
                    // 熊猫观察图文底层页
                    setTeleText(tId, tTitle, tUrl, tImg, tTimeval);
                } else {
                    dataError();
                }
            }

        } else if (tData.getAdapterType() == HomeTypeEnum.HOME_STYLE1_TYPE
                .value()) {
            tContent   = "列表";
            HomeStyle1 tStyle1 = (HomeStyle1) tData;

            if (tStyle1 != null && !tStyle1.getStyles1().isEmpty()) {

                HomeStyleData tStyleData = tStyle1.getStyles1().get(tIndex);

                String tId = tStyleData.getId();
                String tVid = tStyleData.getVid();
                String tPid = tStyleData.getPid();
                String tTitle = tStyleData.getTitle();
                String tType = tStyleData.getId().substring(0, 4);
                String tUrl = tStyleData.getUrl();
                String tImg = tStyleData.getImage();
                String tVideoLenth = tStyleData.getVideoLength();
                String tTimeval = null;



                if (tPid != null && !tPid.equals("") && tVid != null
                        && !tVid.equals("")) {

                    // 点播视频集
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tVid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"***事件类别:"+"综合推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+tVid);
                    setVideoList(tPid, tVid, tTitle, tVideoLenth, tImg);
                } else if (tPid != null && !tPid.equals("")) {

                    // 点播单视频
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tPid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"***事件类别:"+"综合推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+tPid);
                    setVodVideo(tPid, tTitle, tVideoLenth, tImg);
                } else if (tVid != null && !tVid.equals("")) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tVid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"***事件类别:"+"综合推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+tVid);
                    // 央视名栏底层页
                    setCCTV(tVid, tTitle, tImg, tVideoLenth);
                } else if (tType.equals(JMPType2Enum.TELETEXT.toString())) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tId, "图文浏览", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"***事件类别:"+"综合推荐"+"***事件标签:"+"首页"+"***类型:"+"图文浏览"+"*****ID="+tId);
                    // 熊猫观察图文底层页
                    setTeleText(tId, tTitle, tUrl, tImg, tTimeval);
                } else {
                    dataError();
                }
            }

        } else if (tData.getAdapterType() == HomeTypeEnum.HOME_STYLE2_TYPE
                .value()) {
            tContent   = "列表";
            HomeStyle2 tStyle2 = (HomeStyle2) tData;

            if (tStyle2 != null && tStyle2.getStyleData() != null) {

                HomeStyleData tStyleData = tStyle2.getStyleData();

                String tId = tStyleData.getId();
                String tVid = tStyleData.getVid();
                String tPid = tStyleData.getPid();
                String tTitle = tStyleData.getTitle();
                String tType = tStyleData.getId().substring(0, 4);
                String tUrl = tStyleData.getUrl();
                String tImg = tStyleData.getImage();
                String tVideoLenth = tStyleData.getVideoLength();
                String tTimeval = null;

                if (tPid != null && !tPid.equals("") && tVid != null
                        && !tVid.equals("")) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tVid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"***事件类别:"+"综合推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+tVid);
                    // 点播视频集
                    setVideoList(tPid, tVid, tTitle, tVideoLenth, tImg);
                } else if (tPid != null && !tPid.equals("")) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tPid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"***事件类别:"+"综合推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+tPid);
                    // 点播单视频
                    setVodVideo(tPid, tTitle, tVideoLenth, tImg);
                } else if (tVid != null && !tVid.equals("")) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tVid, "视频观看", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"***事件类别:"+"综合推荐"+"***事件标签:"+"首页"+"***类型:"+"视频观看"+"*****ID="+tVid);
                    // 央视名栏底层页
                    setCCTV(tVid, tTitle, tImg, tVideoLenth);
                } else if (tType.equals(JMPType2Enum.TELETEXT.toString())) {
                    //统计
                    MobileAppTracker.trackEvent(tTitle, "综合推荐", "首页", 0, tId, "图文浏览", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+tTitle+"***事件类别:"+"综合推荐"+"***事件标签:"+"首页"+"***类型:"+"图文浏览"+"*****ID="+tId);
                    // 熊猫观察图文底层页
                    setTeleText(tId, tTitle, tUrl, tImg, tTimeval);
                } else {
                    dataError();
                }
            }
        }

    }

    /**
     * 单视频跳转
     *
     * @param pid
     * @param title
     * @param img
     * @param timeLegth
     */
    private void setVodVideo(String pid, String title, String timeLegth,
                             String img) {

        if (pid == null || pid.equals("")) {

            dataError();
            return;
        }

        setVideo(pid, null, title, JMPTypeEnum.VIDEO_VOD.value(), timeLegth,
                img);
    }

    /**
     * 视频集跳转
     *
     * @param pid
     * @param vid
     * @param title
     * @param videoLength
     */
    private void setVideoList(String pid, String vid, String title,
                              String videoLength, String img) {

        if (vid == null || vid.equals("")) {

            dataError();
            return;
        }

        setVideo(pid, vid, title, JMPTypeEnum.VIDEO_LIST.value(), videoLength,
                img);
    }

    /**
     * 视频跳转
     *
     * @param pid
     * @param vid
     * @param title
     * @param img
     * @param timeLegth
     */
    private void setVideo(String pid, String vid, String title, int videoType,
                          String timeLegth, String img) {

        PlayVodEntity tEntity = new PlayVodEntity();
        tEntity.setVid(pid);
        tEntity.setVideosId(vid);
        tEntity.setTitle(title);
        tEntity.setVideoType(videoType);
        tEntity.setTimeLegth(timeLegth);
        tEntity.setType(CollectTypeEnum.SP.value() + "");
        tEntity.setImg(img);

        Intent tIntent = new Intent(mContext, PlayVodFullScreeActivity.class);
        tIntent.putExtra("vid", tEntity);

        mContext.startActivity(tIntent);
    }

    private void setCCTV(String id, String title, String img, String videoLength) {

        if (id == null || id.equals("")) {

            dataError();
            return;
        }

        // 央视名栏底层页
        Intent tIntent = new Intent(mContext, CCTVDetailActivity.class);
        tIntent.putExtra("id", id);
        tIntent.putExtra("title",title);
        tIntent.putExtra("image", img);
        tIntent.putExtra("videoLength", videoLength);

        mContext.startActivity(tIntent);
    }

    private void setLiveVideo(String id, String title, String img, int page) {

        if (id == null || id.equals("")) {

            dataError();
            return;
        }

        PlayLiveEntity tEntity = new PlayLiveEntity();
        tEntity.setChanneId(id);
        tEntity.setTitle(title);
        tEntity.setType(CollectTypeEnum.PD.value() + "");
        tEntity.setPageSource(page);
        tEntity.setImage(img);

        Intent tIntent = new Intent(mContext, PlayLiveActivity.class);
        tIntent.putExtra("live", tEntity);

        mContext.startActivity(tIntent);
    }

    /**
     * 设置熊猫观察图文跳转
     *
     * @param title
     * @param url
     * @param img
     * @param timeval
     */
    private void setTeleText(String id, String title, String url, String img,
                             String timeval) {


        Intent tIntent = new Intent(mContext, PandaEyeDetailActivity.class);
        tIntent.putExtra(PandaEyeDetailActivity.TYPE, PandaEyeDetailActivity.TYPE_ARTICLE);


        if (id != null && !id.equals("")) {

            tIntent.putExtra("id", id);
        }

        if (title != null && !title.equals("")) {

            tIntent.putExtra("title", title);
        }

        if (url != null && !url.equals("")) {

            tIntent.putExtra("url", url);
        }

        if (img != null && !img.equals("")) {

            tIntent.putExtra("pic", img);
        }

        if (timeval != null && !timeval.equals("")) {

            tIntent.putExtra("timeval", timeval);
        }

        mContext.startActivity(tIntent);
    }

    /**
     * 数据为空时判断
     */
    private void dataError() {

        Toast.makeText(mContext, R.string.video_address_not_exist,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 带参跳转
     *
     * @param startActivity
     * @param hashMap
     */
    private void startIntent(Class<?> startActivity,
                             HashMap<String, String> hashMap) {

        Intent tIntent = new Intent(mContext, startActivity);

        if (hashMap != null) {

            Iterator<String> tIterator = hashMap.keySet().iterator();

            while (tIterator.hasNext()) {

                String tKey = tIterator.next();
                tIntent.putExtra(tKey, hashMap.get(tKey));
            }
        }

        mContext.startActivity(tIntent);
    }

}
