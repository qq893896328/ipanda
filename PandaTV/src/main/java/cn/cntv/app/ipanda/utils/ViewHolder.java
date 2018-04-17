package cn.cntv.app.ipanda.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * @author: Xiao JinLai
 * @Date: 2015-12-11 11:07
 * @Description: ListView 适配时 ViewItem 再用类
 */
public class ViewHolder {

    // I added a generic return type to reduce the casting noise in client code
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {

        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();

        if (viewHolder == null) {

            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }

        View childView = viewHolder.get(id);

        if (childView == null) {

            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }

        return (T) childView;
    }
}
