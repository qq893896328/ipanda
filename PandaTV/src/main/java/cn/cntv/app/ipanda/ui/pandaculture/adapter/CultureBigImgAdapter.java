package cn.cntv.app.ipanda.ui.pandaculture.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.gridsum.mobiledissector.MobileAppTracker;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.cctv.activity.CCTVDetailActivity;
import cn.cntv.app.ipanda.ui.home.activity.HomeSSubjectActivity;
import cn.cntv.app.ipanda.ui.home.activity.InterDetailActivity;
import cn.cntv.app.ipanda.ui.pandaculture.entity.PandaCultureBigImgBean;
import cn.cntv.app.ipanda.ui.pandaeye.activity.PandaEyeDetailActivity;
import cn.cntv.app.ipanda.ui.play.PlayLiveActivity;
import cn.cntv.app.ipanda.ui.play.PlayVodFullScreeActivity;
import cn.cntv.app.ipanda.ui.play.entity.PlayVodEntity;

/**
 * Created by maqingwei on 2016/5/18.
 */
public class CultureBigImgAdapter extends PagerAdapter {

    private List<PandaCultureBigImgBean> mBigImgs;

    private LayoutInflater mInflater;
    private View.OnClickListener mListener;

    private int mTitleWiedth;
    private int mPosition;
    private Activity mActivity;


    public CultureBigImgAdapter(List<PandaCultureBigImgBean> bigImgs, Activity activty,
                                View.OnClickListener listener, int titleWidth, int position) {



        this.mActivity = activty;
        this.mBigImgs = bigImgs;
        this.mInflater = LayoutInflater.from(activty);
        this.mListener = listener;
        this.mTitleWiedth = titleWidth;
        this.mPosition = position;
    }

    @Override
    public int getCount() {
        return mBigImgs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        PandaCultureBigImgBean tBigImg = mBigImgs.get(position);

        View tBannerView = mInflater.inflate(
                R.layout.view_banner_viewpager_item, container, false);

        ImageView tIvBigImg = (ImageView) tBannerView
                .findViewById(R.id.ivBigImg);
        tIvBigImg.setTag(mPosition + "," + position);
        tIvBigImg.setOnClickListener(new BigImgClick(tBigImg));

       if(Util.isOnMainThread()) {
           Glide.with(mActivity)
                   .load(tBigImg.getImage())
                   .asBitmap()
                   .centerCrop()
                   .placeholder(R.drawable.def_no_iv)
                   .error(R.drawable.def_no_iv)
                   .into(tIvBigImg);
       }

        TextView tTvBigImgTitle = (TextView) tBannerView
                .findViewById(R.id.tvBigImgTitle);

        tTvBigImgTitle.setText(tBigImg.getTitle());
        tTvBigImgTitle.setWidth(mTitleWiedth);

        container.addView(tBannerView);

        return tBannerView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    class BigImgClick implements View.OnClickListener {

        PandaCultureBigImgBean pandaModel;

        public BigImgClick(PandaCultureBigImgBean pandaEyeModel) {

            pandaModel = pandaEyeModel;

        }

        @Override
        public void onClick(View view) {

            PandaCultureBigImgBean topData = pandaModel;

            Log.e("peye", "topData..type..=" + topData.getType());


            if (null != topData.getType()) {
                if (topData.getType().trim().equals("6")) {
                    //为图文，h5
                    Intent intent = new Intent(mActivity, InterDetailActivity.class);
                    intent.putExtra("url", topData.getUrl());
                    intent.putExtra("title", topData.getTitle());
                    intent.putExtra("image", topData.getImage());
//                    intent.putExtra("id", topData.getId());
                    //Log.e("eye", topData.getUrl()+"title="+topData.getTitle()+"pic="+topData.getImage());
                //统计
                    MobileAppTracker.trackEvent(topData.getTitle(), "大图推荐", "熊猫文化", 0, topData.getUrl(), "视频观看", mActivity);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+topData.getTitle()+"***事件类别:"+"熊猫文化"+"***事件标签:"+"大图推荐"+"***类型:"+"视频观看"+"*****ID="+topData.getUrl());

                    mActivity.startActivity(intent);

                } else if (topData.getType().trim().equals("1")) {
                    if (null != topData.getStype()) {

                        String liveStype = topData.getStype().trim();
                        //统计
                        MobileAppTracker.trackEvent(topData.getTitle(), "大图推荐", "熊猫文化", 0, topData.getId(), "视频观看", mActivity);
                        MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                        Log.e("统计","事件名称:"+topData.getTitle()+"***事件类别:"+"熊猫文化"+"***事件标签:"+"大图推荐"+"***类型:"+"视频观看"+"*****ID="+topData.getId());

                        Intent liveIntent = new Intent(mActivity, PlayLiveActivity.class);


                        mActivity.startActivity(liveIntent);
                    }


                } else if (topData.getType().trim().equals("2")) {
                    //为单视频点播
                    PlayVodEntity vodModel = new PlayVodEntity("1", topData.getPid(), null, topData.getUrl(), topData.getImage(), topData.getTitle(), null, 2, null);
                    //统计
                    MobileAppTracker.trackEvent(topData.getTitle(), "大图推荐", "熊猫文化", 0, topData.getPid(), "视频观看", mActivity);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+topData.getTitle()+"***事件类别:"+"熊猫文化"+"***事件标签:"+"大图推荐"+"***类型:"+"视频观看"+"*****ID="+topData.getPid());

                    Intent vodIntent = new Intent(mActivity, PlayVodFullScreeActivity.class);
                    vodIntent.putExtra("vid", vodModel);
                    mActivity.startActivity(vodIntent);

                } else if (topData.getType().trim().equals("3")) {
                    //直接跳到全屏视频集播
                    PlayVodEntity vodModel = new PlayVodEntity("1", topData.getPid(), topData.getVid(), topData.getUrl(), topData.getImage(), topData.getTitle(), null, 2, null);
                    Intent ccIntent = new Intent(mActivity,
                            CCTVDetailActivity.class);
                    ccIntent.putExtra("id", topData.getVid());
                    ccIntent.putExtra("title", topData.getTitle());
                    ccIntent.putExtra("image", topData.getImage());

                    ccIntent.putExtra("vid", vodModel);
                    //统计
                    MobileAppTracker.trackEvent(topData.getTitle(), "大图推荐", "熊猫文化", 0, topData.getVid(), "视频观看", mActivity);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+topData.getTitle()+"***事件类别:"+"熊猫文化"+"***事件标签:"+"大图推荐"+"***类型:"+"视频观看"+"*****ID="+topData.getVid());
                    mActivity.startActivity(ccIntent);


                } else if (topData.getType().trim().equals("5")) {
                    //为正文
                    Intent intent = new Intent(mActivity, PandaEyeDetailActivity.class);
                    intent.putExtra(PandaEyeDetailActivity.TYPE, PandaEyeDetailActivity.TYPE_ARTICLE);

                    intent.putExtra("url", topData.getUrl());
                    intent.putExtra("title", topData.getTitle());
                    intent.putExtra("pic", topData.getImage());
                    intent.putExtra("id", topData.getId());
                    intent.putExtra("timeval", "");
                    //统计
                    MobileAppTracker.trackEvent(topData.getTitle(), "大图推荐", "熊猫文化", 0, topData.getId(), "图文浏览", mActivity);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+topData.getTitle()+"***事件类别:"+"熊猫文化"+"***事件标签:"+"大图推荐"+"***类型:"+"图文浏览"+"*****ID="+topData.getId());


                    mActivity.startActivity(intent);
                } else if (topData.getType().trim().equals("4")) {

                    Intent intent = new Intent(mActivity, HomeSSubjectActivity.class);
                    intent.putExtra("url", topData.getUrl());
                    intent.putExtra("title", topData.getTitle());
                    //统计
                    MobileAppTracker.trackEvent(topData.getTitle(), "大图推荐", "熊猫文化", 0, topData.getUrl(), "图文浏览", mActivity);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.e("统计","事件名称:"+topData.getTitle()+"***事件类别:"+"熊猫文化"+"***事件标签:"+"大图推荐"+"***类型:"+"图文浏览"+"*****ID="+topData.getUrl());

                    mActivity.startActivity(intent);
                }
            }
        }

    }


}
