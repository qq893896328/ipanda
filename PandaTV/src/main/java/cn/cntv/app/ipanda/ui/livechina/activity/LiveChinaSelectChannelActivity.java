package cn.cntv.app.ipanda.ui.livechina.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.livechina.adapter.DragAdapter;
import cn.cntv.app.ipanda.ui.livechina.adapter.OtherAdapter;
import cn.cntv.app.ipanda.ui.livechina.entity.LiveChinaAllTablist;
import cn.cntv.app.ipanda.ui.livechina.entity.LiveChinaTabItem;
import cn.cntv.app.ipanda.ui.livechina.view.DragGrid;
import cn.cntv.app.ipanda.ui.livechina.view.OtherGridView;

public class LiveChinaSelectChannelActivity extends Activity implements OnClickListener, OnItemClickListener{
	//直播中国选择频道的布局
	private RelativeLayout liveChinaLayot;
	/** 用户已经添加栏目的GRIDVIEW */
	private DragGrid userGridView;
	/** 其它未添加栏目的GRIDVIEW */
	private OtherGridView otherGridView;
	/** 用户栏目对应的适配器，可以拖动 */
	DragAdapter userAdapter;
	/** 其它栏目对应的适配器 */
	OtherAdapter otherAdapter;
	
	/** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */	
	boolean isMove = false;
	private Button btnSelectLiveChinaCancel;
	private Button btnSelectLiveChinaEdit;
	
	//当前界面显示的数据
	private LiveChinaAllTablist allTablist;
	private RelativeLayout fugaiLayout;
	private TextView tuozhuntishi;
	
	public static final String RESUTL_TABLIST_STRING = "RESUTL_TABLIST_STRING";
	
	//点击保存后的数据
	private LiveChinaAllTablist allChinaAllTablistSave;
	//编辑按钮是否被点击
	private boolean boolEdit=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTheme(R.style.Transparent);
		setContentView(R.layout.live_china_channel_seletct);
		
		initView();
		
		LiveChinaAllTablist liveChinaAllTablist = (LiveChinaAllTablist) getIntent().getSerializableExtra("LiveChinaAllTablist");
		if(liveChinaAllTablist!=null){
			setChinaChannelSelecVisible(liveChinaAllTablist);
		}
	}

	private void initView(){
		liveChinaLayot = (RelativeLayout) findViewById(R.id.live_china_select_channel_layout);
		userGridView = (DragGrid) findViewById(R.id.userGridView);
		userGridView.setEnabled(false);
		otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
		otherGridView.setOnItemClickListener(this);
	    userGridView.setOnItemClickListener(this);
	    btnSelectLiveChinaCancel = (Button) findViewById(R.id.live_chinnal_select_channel_cancel);
	    btnSelectLiveChinaCancel.setOnClickListener(this);
	    btnSelectLiveChinaEdit = (Button) findViewById(R.id.live_china_select_channel_bianji);
	    btnSelectLiveChinaEdit.setOnClickListener(this);
	    fugaiLayout = (RelativeLayout) findViewById(R.id.live_china_fugai);
	    fugaiLayout.setVisibility(View.VISIBLE);
	    fugaiLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	    tuozhuntishi = (TextView) findViewById(R.id.live_china_bianji_right);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.live_chinnal_select_channel_cancel:
			notifyChannelDataChange();
			break;
		case R.id.live_china_select_channel_bianji:
			if (boolEdit){
				userGridView.setEnabled(false);
				boolEdit=false;
			}else {
				userGridView.setEnabled(true);
				boolEdit = true;
			}
			if(userAdapter.EDIT_TRUE==true){
				userAdapter.EDIT_TRUE = false;
				btnSelectLiveChinaEdit.setText(getString(R.string.edit));
				fugaiLayout.setVisibility(View.VISIBLE);
				tuozhuntishi.setVisibility(View.GONE);
				
				allChinaAllTablistSave = new LiveChinaAllTablist();
				allChinaAllTablistSave.alllist = new LinkedList<LiveChinaTabItem>();
				allChinaAllTablistSave.tablist = new LinkedList<LiveChinaTabItem>();
				allChinaAllTablistSave.alllist.addAll(allTablist.alllist);
				allChinaAllTablistSave.tablist.addAll(allTablist.tablist);
			}else {
				userAdapter.EDIT_TRUE = true;
				btnSelectLiveChinaEdit.setText(getString(R.string.finish));
				fugaiLayout.setVisibility(View.GONE);
				tuozhuntishi.setVisibility(View.VISIBLE);
			}
			userAdapter.notifyDataSetChanged();
			break;

		}
		
	}
	
	/**
	 * 显示直播中国的选择频道的内容
	 * @param allTablist 
	 */
	public void setChinaChannelSelecVisible(LiveChinaAllTablist allTablist){
		this.allTablist = allTablist;
		
		
		allChinaAllTablistSave = new LiveChinaAllTablist();
		allChinaAllTablistSave.alllist = new LinkedList<LiveChinaTabItem>();
		allChinaAllTablistSave.tablist = new LinkedList<LiveChinaTabItem>();
		allChinaAllTablistSave.alllist.addAll(allTablist.alllist);
		allChinaAllTablistSave.tablist.addAll(allTablist.tablist);
		
		liveChinaLayot.setVisibility(View.VISIBLE);
		fugaiLayout.setVisibility(View.VISIBLE);
		btnSelectLiveChinaEdit.setText(getString(R.string.edit));
		userAdapter = new DragAdapter(this, allTablist.tablist);
		
		for (int i = 0; i < allTablist.tablist.size(); i++) {
			LiveChinaTabItem liveChinaTabItem = allTablist.tablist.get(i);
			for (int j = 0; j < allTablist.alllist.size(); j++) {
				if(liveChinaTabItem.title.equals(allTablist.alllist.get(j).title)){
					allTablist.alllist.remove(j);
				}
			}
		}
		otherAdapter = new OtherAdapter(this, allTablist.alllist);
		userGridView.setAdapter(userAdapter);
		otherGridView.setAdapter(otherAdapter);
	}
	
	
	private void notifyChannelDataChange(){
		Intent intent = new Intent();
        //把返回数据存入Intent
        intent.putExtra(RESUTL_TABLIST_STRING, allChinaAllTablistSave);
        //设置返回数据
        this.setResult(RESULT_OK, intent);
        //关闭Activity
        this.finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			 notifyChannelDataChange();
	         return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	


	@Override
	public void onItemClick(AdapterView<?> parent, final View view, final int position,long id) {
		if (boolEdit){
		//如果点击的时候，之前动画还没结束，那么就让点击事件无效
		if(isMove){
			return;
		}
		switch (parent.getId()) {
		case R.id.userGridView:
			//position为 0，1 的不可以进行任何操作
//					if (position != 0 && position != 1) {
				if(userAdapter.getCount()<=4){
					Toast.makeText(this, R.string.greater_than_four, Toast.LENGTH_SHORT).show();
					return;
				}
				
				final ImageView moveImageView1 = getView(view);
				if (moveImageView1 != null) {
					TextView newTextView = (TextView) view.findViewById(R.id.text_item);
					final int[] startLocation = new int[2];
					newTextView.getLocationInWindow(startLocation);
					final LiveChinaTabItem channel = ((DragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
					otherAdapter.setVisible(false);
					//添加到最后一个
					otherAdapter.addItem(channel);
					new Handler().postDelayed(new Runnable() {
						public void run() {
							try {
								int[] endLocation = new int[2];
								//获取终点的坐标
								otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
								MoveAnim(moveImageView1, startLocation , endLocation, channel,userGridView);
								userAdapter.setRemove(position);
							} catch (Exception localException) {
							}
						}
					}, 50L);
				}
//					}
			break;
		case R.id.otherGridView:
			final ImageView moveImageView = getView(view);
			if (moveImageView != null){
				TextView newTextView = (TextView) view.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final LiveChinaTabItem channel = ((OtherAdapter) parent.getAdapter()).getItem(position);
				userAdapter.setVisible(false);
				//添加到最后一个
				userAdapter.addItem(channel);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						try {
							int[] endLocation = new int[2];
							//获取终点的坐标
							userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
							MoveAnim(moveImageView, startLocation , endLocation, channel,otherGridView);
							otherAdapter.setRemove(position);
						} catch (Exception localException) {
						}
					}
				}, 50L);
			}
			break;
		default:
			break;
		}
		}
	}
	
	/**
	 * 获取点击的Item的对应View，
	 * @param view
	 * @return
	 */
	private ImageView getView(View view) {
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(cache);
		return iv;
	}
	
	/**
	 * 点击ITEM移动动画
	 * @param moveView
	 * @param startLocation
	 * @param endLocation
	 * @param moveChannel
	 * @param clickGridView
	 */
	private void MoveAnim(View moveView, int[] startLocation,int[] endLocation, final LiveChinaTabItem moveChannel,
			final GridView clickGridView) {
		int[] initLocation = new int[2];
		//获取传递过来的VIEW的坐标
		moveView.getLocationInWindow(initLocation);
		//得到要移动的VIEW,并放入对应的容器中
		final ViewGroup moveViewGroup = getMoveViewGroup();
		final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
		//创建移动动画
		TranslateAnimation moveAnimation = new TranslateAnimation(
				startLocation[0], endLocation[0], startLocation[1],
				endLocation[1]);
		moveAnimation.setDuration(300L);//动画时间
		//动画配置
		AnimationSet moveAnimationSet = new AnimationSet(true);
		moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
		moveAnimationSet.addAnimation(moveAnimation);
		mMoveView.startAnimation(moveAnimationSet);
		moveAnimationSet.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				isMove = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				moveViewGroup.removeView(mMoveView);
				// instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
				if (clickGridView instanceof DragGrid) {
					otherAdapter.setVisible(true);
					otherAdapter.notifyDataSetChanged();
					userAdapter.remove();
				}else{
					userAdapter.setVisible(true);
					userAdapter.notifyDataSetChanged();
					otherAdapter.remove();
				}
				isMove = false;
			}
		});
	}
	
	/**
	 * 创建移动的ITEM对应的ViewGroup布局容器
	 */
	private ViewGroup getMoveViewGroup() {
		ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
		LinearLayout moveLinearLayout = new LinearLayout(this);
		moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		moveViewGroup.addView(moveLinearLayout);
		return moveLinearLayout;
	}
	
	/**
	 * 获取移动的VIEW，放入对应ViewGroup布局容器
	 * @param viewGroup
	 * @param view
	 * @param initLocation
	 * @return
	 */
	private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
		int x = initLocation[0];
		int y = initLocation[1];
		viewGroup.addView(view);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = x;
		mLayoutParams.topMargin = y;
		view.setLayoutParams(mLayoutParams);
		return view;
	}
	
}
