package com.hit.wangoceantao.libraryforandroid.widgets.groupview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hit.wangoceantao.libraryforandroid.widgets.groupview.model.BitmapBean;
import com.hit.wangoceantao.libraryforandroid.widgets.groupview.model.NineRectHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 群头像View（正方形的形状）
 * <p/>
 * Created by wanghaitao on 16/5/29 10:24.
 * <p/>
 * Email：wanghaitao01@hecom.cn
 */
public class GroupImageView extends FrameLayout {
    protected List<String> mUrls = new ArrayList<String>();
    protected AbsoluteLayout mContainerView;
    /**
     * 最多显示9张图片
     */
    protected final static int MAX_IMAGE_COUNT = 9;
    /**
     * 图片的宽度
     */
    protected final static int DEFAULT_IMAGE_WIDTH = 300;
    /**
     * 图片的高度
     */
    protected final static int DEFAULT_IMAGE_HEIGHT = 300;
    protected int mWidth = DEFAULT_IMAGE_WIDTH;
    protected int mHeight = DEFAULT_IMAGE_HEIGHT;
    protected int mDefaultImageResId;

    public GroupImageView(Context context) {
        super(context);
    }

    public GroupImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public GroupImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置图片数据
     *
     * @param urls 待显示的图片资源地址
     */
    public void setDatas(ArrayList<String> urls) {
        mUrls.clear();
        //最多支持9张图片
        if (urls != null && urls.size() > 0) {
            if (urls.size() > MAX_IMAGE_COUNT) {
                mUrls.addAll(urls.subList(0, MAX_IMAGE_COUNT));
            } else {
                mUrls.addAll(urls);
            }
        }
        notifyRefrewViews();
    }

    /**
     * 设置图片数据
     *
     * @param urls              待显示的图片资源地址
     * @param defaultImageResId 默认显示图片资源Id
     */
    public void setDatas(ArrayList<String> urls, int defaultImageResId) {
        setDefaultImageResId(defaultImageResId);
        mUrls.clear();
        //最多支持9张图片
        if (urls != null && urls.size() > 0) {
            if (urls.size() > MAX_IMAGE_COUNT) {
                mUrls.addAll(urls.subList(0, MAX_IMAGE_COUNT));
            } else {
                mUrls.addAll(urls);
            }
        }
        notifyRefrewViews();
    }

    /**
     * 设置数据
     *
     * @param urls              待显示的图片资源地址：支持网络图片、本地图片
     * @param squareWidth       正方形边长（单位：像素）
     * @param defaultImageResId 默认显示图片资源Id
     */
    public void setDatas(ArrayList<String> urls, int defaultImageResId, int squareWidth) {
        this.mDefaultImageResId = defaultImageResId;
        this.mWidth = squareWidth;
        this.mHeight = squareWidth;
        mUrls.clear();
        //最多支持9张图片
        if (urls != null && urls.size() > 0) {
            if (urls.size() > MAX_IMAGE_COUNT) {
                mUrls.addAll(urls.subList(0, MAX_IMAGE_COUNT));
            } else {
                mUrls.addAll(urls);
            }
        }
        notifyRefrewViews();
    }

    private void setDefaultImageResId(int resId) {
        this.mDefaultImageResId = resId;
    }

    private void notifyRefrewViews() {
        generateContainerView();
        generateChildViews();
        loadImageDatas();
    }

    private void generateContainerView() {
        if (mContainerView == null) {
            removeAllViews();
            mContainerView = new AbsoluteLayout(getContext());
            addView(mContainerView);
        }
        LayoutParams layoutParams = new LayoutParams(mWidth, mHeight);
        mContainerView.setLayoutParams(layoutParams);
    }

    private void generateChildViews() {
        if (mUrls != null) {
            //复用之前的child view
            int childCount = mUrls.size();
            int exitedCount = mContainerView.getChildCount();
            if (childCount == exitedCount) {
                return;
            }
            if (childCount < exitedCount) {
                //移除多余view
                for (; exitedCount - childCount > 0; exitedCount--) {
                    mContainerView.removeViewAt(exitedCount - 1);
                }
            } else {
                //添加childview
                int addCount = childCount - exitedCount;
                for (int index = 0; index < addCount; index++) {
                    ImageView imageView = getChildImageView();
                    mContainerView.addView(imageView);
                }
            }
            //更新每个childView的位置
            List<BitmapBean> bitmapBeens = NineRectHelper.getInstance().getLocationDatasByImageCount(mWidth, mHeight, childCount);
            for (int index = 0; index < childCount; index++) {
                BitmapBean bitmapBean = bitmapBeens.get(index);
                View childView = mContainerView.getChildAt(index);
                AbsoluteLayout.LayoutParams layoutParams = getLayoutParamsByPosition(bitmapBean);
                childView.setLayoutParams(layoutParams);
            }

        }
    }

    protected void loadImageDatas() {
        if (mContainerView == null) {
            return;
        }
        int childCount = mContainerView.getChildCount();
        for (int index = 0; index < childCount; index++) {
            ImageView childView = (ImageView) mContainerView.getChildAt(index);
            String url = mUrls.get(index);

            displayImage(url, childView);
        }
    }

    //TODO  显示图片
    public void displayImage(String uri, ImageView imageView) {
    }

    private AbsoluteLayout.LayoutParams getLayoutParamsByPosition(BitmapBean bitmapBean) {
        AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(
                Float.valueOf(bitmapBean.width).intValue(),
                Float.valueOf(bitmapBean.height).intValue(),
                Float.valueOf(bitmapBean.x).intValue(),
                Float.valueOf(bitmapBean.y).intValue());
        return layoutParams;
    }

    private ImageView getChildImageView() {
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }
}
