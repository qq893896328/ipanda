package cn.cntv.app.ipanda.ui.pandaculture.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.cctv.adapter.CCTVBaseAdapter;
import cn.cntv.app.ipanda.ui.pandaculture.entity.PandaCultureListBean;

/**
 * Created by maqingwei on 2016/5/18.
 */
public class CultureListAdapter extends CCTVBaseAdapter

    {
        private List<PandaCultureListBean> mEntityList;
        private LayoutInflater mInflater;

        public CultureListAdapter(Context context, List<PandaCultureListBean> datas,
            LayoutInflater inflater) {
        mContext = context;
        mEntityList = datas;
        mInflater = inflater;
        setmContext(mContext);
    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
            PandaCultureListBean entity = mEntityList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.cctv_wellknow_item, null,
                    false);
            viewHolder = new ViewHolder();
            initView(position, convertView, viewHolder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setInfo(position, entity, viewHolder, convertView);
        return convertView;
    }

    private void initView(int position, View convertView, ViewHolder viewHolder) {
        viewHolder.imgView = (ImageView) convertView
                .findViewById(R.id.imageView1);
        viewHolder.titleView = (TextView) convertView.findViewById(R.id.title);
        viewHolder.briefView = (TextView) convertView.findViewById(R.id.brief);
        viewHolder.videoLengthView = (TextView) convertView
                .findViewById(R.id.videoLength);
    }

    private void setInfo(int position, PandaCultureListBean entity,
                         ViewHolder viewHolder, View view) {
        viewHolder.imgView.setImageDrawable(null);
        // viewHolder.imgView.setScaleType(StringUtil.isNullOrEmpty(entity
        // .getImage()) ? ScaleType.FIT_XY : ScaleType.CENTER_CROP);
        /*mImageLoader.displayImage(entity.getImage(), viewHolder.imgView,
                mOptions_410x231);*/
        Glide.with(mContext)
                .load(entity.getImage())
                .asBitmap()
                .placeholder(R.drawable._tw)
                .error(R.drawable._tw)
                .into(viewHolder.imgView);

        viewHolder.titleView.setText(entity.getTitle());
        viewHolder.briefView.setText(entity.getBrief());
        viewHolder.videoLengthView.setText(entity.getVideoLength());
    }

    class ViewHolder {
        ImageView imgView;// 直播图片
        TextView titleView;// 直播标题
        TextView briefView;// 直播简介
        TextView videoLengthView;// 直播时长
    }

    @Override
    public int getCount() {
        return mEntityList.size();
    }

    @Override
    public Object getItem(int index) {
        if (index >= getCount()) {
            return null;
        }
        return mEntityList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }
}

