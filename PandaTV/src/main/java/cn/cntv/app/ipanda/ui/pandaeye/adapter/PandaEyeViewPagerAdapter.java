package cn.cntv.app.ipanda.ui.pandaeye.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.List;

import cn.cntv.app.ipanda.constant.CollectPageSourceEnum;
import cn.cntv.app.ipanda.ui.cctv.activity.CCTVDetailActivity;
import cn.cntv.app.ipanda.ui.pandaeye.activity.PandaEyeDetailActivity;
import cn.cntv.app.ipanda.ui.pandaeye.entity.PandaEyeTopData;
import cn.cntv.app.ipanda.ui.play.PlayLiveActivity;
import cn.cntv.app.ipanda.ui.play.PlayVodFullScreeActivity;
import cn.cntv.app.ipanda.ui.play.entity.PlayLiveEntity;
import cn.cntv.app.ipanda.ui.play.entity.PlayVodEntity;
    


//此类弃用
public class PandaEyeViewPagerAdapter extends PagerAdapter{
	private List<View> mList;    
    private Context mContext;
	private List<PandaEyeTopData> mListBigData;
	
	public PandaEyeViewPagerAdapter(List<View> views,Context context,List<PandaEyeTopData> listBigData) {
		mList = views;
		this.mContext = context;
		this.mListBigData = listBigData;
	}    
	   
	public void setList(List<View> list) {
		mList = list;
		notifyDataSetChanged();
	}
	
	/**
	 * Return the number of views available.
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}
	
	/**
	 * Remove a page for the given position.
	 * 滑动过后就销毁 ，销毁当前页的前一个的前一个的页！
	 * instantiateItem(View container, int position)
	 * This method was deprecated in API level . Use instantiateItem(ViewGroup, int)
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(mList.get(position));
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}
	
	/**
	 * Create the page for the given position.
	 */
	@Override
	public Object instantiateItem(final ViewGroup container, final int position) {
		container.removeView(mList.get(position));
		container.addView(mList.get(position));
		
		mList.get(position).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				
				PandaEyeTopData topData = mListBigData.get(position);
				
				if(null != topData.getType()){
					if(topData.getType().trim().equals("6")){
						//为图文，h5
						Intent intent = new Intent(mContext,PandaEyeDetailActivity.class);
						intent.putExtra("url",topData.getUrl());
						intent.putExtra("title",topData.getTitle());
						intent.putExtra("pic", topData.getImage());
						intent.putExtra("timeval","");
						
						Log.e("eye", topData.getUrl()+"title="+topData.getTitle()+"pic="+topData.getImage());
						
						mContext.startActivity(intent);
						
					}else if(topData.getType().trim().equals("1")){
						if(null != topData.getStype() && topData.getStype().trim().equals("3")){
							//直播中国单视角直播
							PlayLiveEntity liveModel = new PlayLiveEntity(topData.getId().trim(), topData.getTitle(), topData.getImage(), topData.getUrl(), null, "3",CollectPageSourceEnum.XMZB.value(),
									false);
							
							Intent liveIntent = new Intent(mContext,PlayLiveActivity.class);
							liveIntent.putExtra("live", liveModel);
							mContext.startActivity(liveIntent);
						}
					}else if(topData.getType().trim().equals("2")){
						//为单视频点播
						PlayVodEntity vodModel = new PlayVodEntity("1", topData.getPid(), null, topData.getUrl(), topData.getImage(), topData.getTitle(), null, 2,null);
						
						Intent vodIntent = new Intent(mContext,PlayVodFullScreeActivity.class);
						vodIntent.putExtra("vid", vodModel);
						mContext.startActivity(vodIntent);
						
					}else if(topData.getType().trim().equals("3")){
						//为视频集点播，调到央视明栏底层页
						Intent ccIntent = new Intent(mContext,CCTVDetailActivity.class);
						ccIntent.putExtra("id", topData.getVid());
						ccIntent.putExtra("title", topData.getTitle());
						ccIntent.putExtra("image", topData.getImage());
						
						mContext.startActivity(ccIntent);
						
						
					}else if(topData.getType().trim().equals("5")){
						//为正文
						Intent intent = new Intent(mContext,PandaEyeDetailActivity.class);
						intent.putExtra("url",topData.getUrl());
						intent.putExtra("title",topData.getTitle());
						intent.putExtra("pic", topData.getImage());
						intent.putExtra("timeval","");
						
						Log.e("eye", topData.getUrl()+"title="+topData.getTitle()+"pic="+topData.getImage());
						
						mContext.startActivity(intent);
					}
				}
				
				
//				Intent deIntent =  new Intent(context,PandaEyeDetailActivity.class);
//				deIntent.putExtra("model", listBigData.get(position).getUrl());
//				context.startActivity(deIntent);
				
			}
		});
		
		return mList.get(position);

	}
}
