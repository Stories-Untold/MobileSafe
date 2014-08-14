package com.myapp.mobilesafe.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * 优化ListView用的方法
 * Created by 庹大伟 on 2014/8/7.
 * 使用:
 * public View getView(int i, View view, ViewGroup viewGroup) {
 * if (view == null) {
 * Log.v("myinfo", "new View :" + i);
 * view = mInflater.inflate(R.layout.list_item, null);
 * }
 * TextView title = ViewHolder.get(view, R.id.title);
 * TextView text = ViewHolder.get(view, R.id.text);
 * title.setText(data.get(i).get("ItemTitle").toString());
 * text.setText(data.get(i).get("ItemText").toString());
 * return view;
 * }
 */
public class ViewHolder {
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
