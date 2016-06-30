package com.hit.wangoceantao.libraryforandroid.utils;

import android.util.SparseArray;
import android.view.View;

public class GenericViewHolder {
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
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(int key,View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag(key);
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(key,viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}