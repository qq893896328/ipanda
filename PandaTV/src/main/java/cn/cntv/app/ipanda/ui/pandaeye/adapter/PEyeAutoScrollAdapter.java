package cn.cntv.app.ipanda.ui.pandaeye.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gridsum.mobiledissector.MobileAppTracker;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.CollectPageSourceEnum;
import cn.cntv.app.ipanda.ui.home.activity.HomeSSubjectActivity;
import cn.cntv.app.ipanda.ui.home.entity.BigImg;
import cn.cntv.app.ipanda.ui.pandaeye.activity.PandaEyeDetailActivity;
import cn.cntv.app.ipanda.ui.play.PlayLiveActivity;
import cn.cntv.app.ipanda.ui.play.PlayVodFullScreeActivity;
import cn.cntv.app.ipanda.ui.play.entity.PlayLiveEntity;
import cn.cntv.app.ipanda.ui.play.entity.PlayVodEntity;

public class PEyeAutoScrollAdapter extends PagerAdapter {

    private List<BigImg> mBigImgs;

    private LayoutInflater mInflater;
    private OnClickListener mListener;

    private int mTitleWiedth;
    private int mPosition;
    private Context mContext;


    public PEyeAutoScrollAdapter(List<BigImg> bigImgs, Context context,
                                 OnClickListener listener, int titleWidth, int position) {

        this.mContext = context;
        this.mBigImgs = bigImgs;
        this.mInflater = LayoutInflater.from(context);
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

        BigImg tBigImg = mBigImgs.get(position);

        View tBannerView = mInflater.inflate(
                R.layout.view_banner_viewpager_item, container, false);

        ImageView tIvBigImg = (ImageView) tBannerView
                .findViewById(R.id.ivBigImg);
        tIvBigImg.setTag(mPosition + "," + position);
        tIvBigImg.setOnClickListener(new BigImgClick(tBigImg));
        Glide.with(mContext)
                .load(tBigImg.getImage())
                .asBitmap()
                .placeholder(R.drawable.def_no_iv)
                .error(R.drawable.def_no_iv)
                .into(tIvBigImg);

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


    class BigImgClick implements OnClickListener {

        BigImg pandaModel;

        public BigImgClick(BigImg pandaEyeModel) {

            pandaModel = pandaEyeModel;

        }


        @Override
        public void onClick(View view) {

            BigImg topData = pandaModel;

            Log.e("peye", "topData..type..=" + topData.getType());


            if (null != topData.getType()) {
                if (topData.getType().trim().equals("6")) {
                    //为图文，h5
                    Intent intent = new Intent(mContext, PandaEyeDetailActivity.class);
                    intent.putExtra("url", topData.getUrl());
                    intent.putExtra("title", topData.getTitle());
                    intent.putExtra("pic", topData.getImage());
                    intent.putExtra("id", topData.getId());
                    intent.putExtra("timeval", "");
                    //Log.e("eye", topData.getUrl()+"title="+topData.getTitle()+"pic="+topData.getImage());

                    mContext.startActivity(intent);

                } else if (topData.getType().trim().equals("1")) {
                    if (null != topData.getStype()) {

                        String liveStype = topData.getStype().trim();

                        Intent liveIntent = new Intent(mContext, PlayLiveActivity.class);
                        if (liveStype.equals("1") || liveStype.equals("2")) {
                            //直播中国单视角直播
                            PlayLiveEntity liveModel = new PlayLiveEntity(topData.getId().trim(), topData.getTitle(), topData.getImage(), topData.getUrl(), null, "3", CollectPageSourceEnum.XMZB.value(),
                                    true);
                            liveIntent.putExtra("live", liveModel);

                        } else if (liveStype.equals("3") || liveStype.equals("4")) {
                            PlayLiveEntity liveModel = new PlayLiveEntity(topData.getId().trim(), topData.getTitle(), topData.getImage(), topData.getUrl(), null, "3", CollectPageSourceEnum.ZBZG.value(),
                                    true);
                            liveIntent.putExtra("live", liveModel);

                        } else if (liveStype.equals("5")) {
                            PlayLiveEntity liveModel = new PlayLiveEntity(topData.getId().trim(), topData.getTitle(), topData.getImage(), topData.getUrl(), null, "3", CollectPageSourceEnum.PDZB.value(),
                                    true);
                            liveIntent.putExtra("live", liveModel);
                        }

                        mContext.startActivity(liveIntent);
                    }


                } else if (topData.getType().trim().equals("2")) {
                    //为单视频点播
                    PlayVodEntity vodModel = new PlayVodEntity("1", topData.getPid(), null, topData.getUrl(), topData.getImage(), topData.getTitle(), null, 2, null);

                    Intent vodIntent = new Intent(mContext, PlayVodFullScreeActivity.class);
                    vodIntent.putExtra("vid", vodModel);
                    mContext.startActivity(vodIntent);

                } else if (topData.getType().trim().equals("3")) {
                    //直接跳到全屏视频集播
                    PlayVodEntity vodModel = new PlayVodEntity("1", topData.getPid(), topData.getVid(), topData.getUrl(), topData.getImage(), topData.getTitle(), null, 2, null);
                    Intent ccIntent = new Intent(mContext, PlayVodFullScreeActivity.class);
//					ccIntent.putExtra("id", topData.getVid());
//					ccIntent.putExtra("title", topData.getTitle());
//					ccIntent.putExtra("image", topData.getImage());

                    ccIntent.putExtra("vid", vodModel);

                    mContext.startActivity(ccIntent);


                } else if (topData.getType().trim().equals("5")) {
                    //为正文
                    Intent intent = new Intent(mContext, PandaEyeDetailActivity.class);
                    intent.putExtra(PandaEyeDetailActivity.TYPE, PandaEyeDetailActivity.TYPE_ARTICLE);
                    intent.putExtra("url", topData.getUrl());
                    intent.putExtra("title", topData.getTitle());
                    intent.putExtra("pic", topData.getImage());
                    intent.putExtra("id", topData.getId());
                    intent.putExtra("timeval", "");
                    MobileAppTracker.trackEvent(topData.getTitle(), "大图推荐", "熊猫观察", 0, topData.getId(), "图文浏览", mContext);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
                    Log.i("统计：", "熊猫观察大图：" + topData.getTitle());
                    Log.i("统计：", "熊猫观察大图视频ID：" + topData.getId());
                    //Log.e("eye", topData.getUrl()+"title="+topData.getTitle()+"pic="+topData.getImage());

                    mContext.startActivity(intent);
                } else if (topData.getType().trim().equals("4")) {

                    Intent intent = new Intent(mContext, HomeSSubjectActivity.class);
                    intent.putExtra("url", topData.getUrl());
                    intent.putExtra("title", topData.getTitle());
                /*	MobileAppTracker.trackEvent(topData.getTitle(), "大图推荐", "熊猫观察", 0, topData.getId(), "图文浏览", context);
                    MobileAppTracker.setPolicy(MobileAppTracker.POLICY_INTIME);
					Log.i("统计：","熊猫观察大图："+topData.getTitle());
					Log.i("统计：","熊猫观察大图视频ID："+topData.getId());*/
                    mContext.startActivity(intent);
                }
            }
        }

    }


}
