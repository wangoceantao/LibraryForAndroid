package com.hit.wangoceantao.libraryforandroid.widgets.groupview.model;

/**
 * Created by Hankkin on 15/11/19.
 * Bitmap 实体类
 */
public class BitmapBean {
  public   float x;
    public  float y;
    public   float width;
    public  float height;
    public   static int devide = 1;
    public  int index = -1;

    @Override
    public String toString() {
        return "BitmapBean [x=" + x + ", y=" + y + ", width=" + width
                + ", height=" + height + ", devide=" + devide + ", index="
                + index + "]";
    }
}
