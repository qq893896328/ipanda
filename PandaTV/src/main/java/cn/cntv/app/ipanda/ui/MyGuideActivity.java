package cn.cntv.app.ipanda.ui;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import java.io.InputStream;
import java.util.ArrayList;

import cn.cntv.app.ipanda.R;

public class MyGuideActivity extends Activity {
    private ViewPager viewPager;
    private ArrayList<View> pageViews;
    private ImageView imageView;
    private ImageView[] imageViews;
    // 包裹滑动图片LinearLayout
    private ViewGroup main;
    // 包裹小圆点的LinearLayout
    private ViewGroup group;

    private static final String SHAREDPREFERENCES_NAME = "first_pref";
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = getLayoutInflater();


        SharedPreferences pSp = getSharedPreferences("set", Activity.MODE_PRIVATE);
        Editor setEidt = pSp.edit();
        setEidt.putString("push", "open");
        setEidt.putString("play", "open");
        setEidt.commit();


        pageViews = new ArrayList<View>();
        for (int i = 0; i < 3; ++i) {
            iv = new ImageView(this);
            iv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
            iv.setScaleType(ScaleType.FIT_XY);

            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            opt.inPurgeable = true;//允许回收
            opt.inInputShareable = true;
            opt.outWidth = getResources().getDisplayMetrics().widthPixels;
            opt.outHeight = getResources().getDisplayMetrics().heightPixels;

            int tResource=0;

            if (i==0){

                tResource= R.drawable.guide_one;
            }else if (i==1){

                tResource=R.drawable.guide_two;
            }else
                tResource=R.drawable.guide_three;
            InputStream is = getResources().openRawResource(tResource);
            Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
            BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
            iv.setBackgroundDrawable(bd);
            if (i == 2) {
//				iv.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
////						goHome();
//					}
//				});
                iv.setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View arg0, MotionEvent arg1) {
                        // TODO Auto-generated method stub
                        int x = (int) arg1.getX();
                        int y = (int) arg1.getY();
                        Rect r = new Rect(scaleWidth(372), scaleHeight(1990), scaleWidth(869), scaleHeight(2147));
                        if (r.contains(x, y))
                            goHome();
                        return false;
                    }
                });
            }


            pageViews.add(iv);
        }
//		pageViews.add(inflater.inflate(R.layout.guide_one, null));
//		pageViews.add(inflater.inflate(R.layout.guide_two, null));
//		pageViews.add(inflater.inflate(R.layout.guide_three, null));
//		imageViews = new ImageView[pageViews.size()];
        main = (ViewGroup) inflater.inflate(R.layout.activity_my_guide_view,
                null);
        group = (ViewGroup) main.findViewById(R.id.viewGroup);
        viewPager = (ViewPager) main.findViewById(R.id.guidePages);


        setContentView(main);
        viewPager.setAdapter(new GuidePageAdapter());
        //viewPager.setOnPageChangeListener(new GuidePageChangeListener());


    }

    /**
     * 得到等比的宽度
     *
     * @param nowWidth 当前宽度
     * @return 缩放后的宽度
     */
    public int scaleWidth(int nowWidth) {
        try {
            return nowWidth * iv.getWidth() / 1242;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 得到等比高度
     *
     * @param nowHeight 当前高度
     * @return 缩放后的高度
     */
    public int scaleHeight(int nowHeight) {
        try {
            int aa = nowHeight * iv.getHeight() / 2208;
            return aa;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 指引页面数据适配器
    class GuidePageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            // TODO Auto-generated method stub
            ((ViewPager) arg0).removeView(pageViews.get(arg1));
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            // TODO Auto-generated method stub
            ((ViewPager) arg0).addView(pageViews.get(arg1));

            if (arg1 == pageViews.size() - 1) {
//				final ImageView mStartWeiboImageButton = (ImageView) arg0
//						.findViewById(R.id.know);
//				mStartWeiboImageButton
//						.setOnClickListener(new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								// 设置已经引导
//								setGuided();
//								goHome();
//							}
//						});

//				goHome();

            }
            return pageViews.get(arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }
    }

    private void goHome() {

        SharedPreferences guid = getSharedPreferences("guide", Activity.MODE_PRIVATE);
        Editor guidEdit = guid.edit();
        guidEdit.putString("val", "yes");
        String tVersionCode = "";
        try {
            tVersionCode = getPackageManager().getPackageInfo(
                    getPackageName(), 0).versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        guidEdit.putString("code", tVersionCode);
        guidEdit.commit();

        Intent mainIntent = new Intent(MyGuideActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();


        // 跳转
//		Intent intent = new Intent();
//		intent.setClass(MyGuideActivity.this, UserInfoActivity.class);
//		intent.putExtra("type", "guide");
//		MyGuideActivity.this.startActivity(intent);
//		MyGuideActivity.this.finish();
//		overridePendingTransition(android.R.anim.fade_in,
//				android.R.anim.fade_out);
    }

    private void setGuided() {
        // 存入数据
        //SharedPrefHelper.getInstance().setFristLogin(false);
    }


}
