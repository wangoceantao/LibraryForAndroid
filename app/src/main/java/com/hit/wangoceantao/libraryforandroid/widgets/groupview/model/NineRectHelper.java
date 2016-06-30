package com.hit.wangoceantao.libraryforandroid.widgets.groupview.model;


import java.util.LinkedList;
import java.util.List;

/**
 * 防微信群聊头像helper
 */
public class NineRectHelper {
    private final static float DEFAULT_MAX_WIDTH = 200F;
    private final static float DEFAULT_MAX_HEIGHT = 200F;
    private float maxWidth = DEFAULT_MAX_WIDTH;
    private float maxHeight = DEFAULT_MAX_HEIGHT;
    private volatile static NineRectHelper sInstance;

    private NineRectHelper() {

    }

    public static NineRectHelper getInstance() {
        if (sInstance == null) {
            synchronized (NineRectHelper.class) {
                if (sInstance == null) {
                    sInstance = new NineRectHelper();
                }
            }
        }
        return sInstance;
    }

    /**
     * 根据图片数获取图片显示的位置信息
     *
     * @param imageCount 图片数目
     * @return
     */
    public List<BitmapBean> getLocationDatasByImageCount(final int imageCount) {
        return getLocationDatasByImageCount(DEFAULT_MAX_WIDTH, DEFAULT_MAX_HEIGHT, imageCount);
    }

    /**
     * 根据图片数获取图片显示的位置信息
     *
     * @param width      图片显示的宽度（pix）
     * @param height     图片显示的高度（pix）
     * @param imageCount 图片数目
     * @return
     */
    public List<BitmapBean> getLocationDatasByImageCount(float width, float height, final int imageCount) {
        if (width > 0) {
            maxWidth = width;
        }
        if (height > 0) {
            maxHeight = height;
        }
        ColumnRowCount mCRC = generateColumnRowCountByCount(imageCount);
        BitmapBean bitmap = null;
        float perBitmapWidth = (maxWidth - BitmapBean.devide * 2 * mCRC.columns) / mCRC.columns;
        float topDownDelta = maxHeight - (mCRC.rows * (perBitmapWidth + BitmapBean.devide * 2));
        List<BitmapBean> mList = new LinkedList<BitmapBean>();
        for (int row = 0; row < mCRC.rows; row++) {
            for (int column = 0; column < mCRC.columns; column++) {
                bitmap = new BitmapBean();
                bitmap.y = 1 + topDownDelta / 2 + row * 2 + row * perBitmapWidth;
                bitmap.x = 1 + column * 2 + column * perBitmapWidth;
                bitmap.width = bitmap.height = perBitmapWidth;
                mList.add(bitmap);
            }
        }
        switch (imageCount) {
            case 3:
                modifyListWhenCountThree(mList);
                break;
            case 5:
                modifyListWhenCountFive(mList);
                break;
            case 7:
                modifyListWhenCountSeven(mList);
                break;
            case 8:
                modifyListWhenCountEight(mList);
                break;
        }
        return mList;

    }

    private static void modifyListWhenCountThree(List<BitmapBean> mList1) {
        BitmapBean mBitmap1 = mList1.get(0);
        BitmapBean mBitmap2 = mList1.get(1);
        BitmapBean mDesBitmap = new BitmapBean();
        mDesBitmap.width = mBitmap1.width;
        mDesBitmap.height = mBitmap1.height;
        mDesBitmap.y = mBitmap1.y;
        mDesBitmap.x = (mBitmap1.x + mBitmap2.x) / 2;
        mList1.set(0, mDesBitmap);
        mList1.remove(1);
    }

    private static void modifyListWhenCountFive(List<BitmapBean> mList1) {
        BitmapBean mBitmap1 = mList1.get(0);
        BitmapBean mBitmap2 = mList1.get(1);
        BitmapBean mBitmap3 = mList1.get(2);
        BitmapBean mDesBitmap = new BitmapBean();
        mDesBitmap.width = mBitmap1.width;
        mDesBitmap.height = mBitmap1.height;
        mDesBitmap.y = mBitmap1.y;
        mDesBitmap.x = (mBitmap1.x + mBitmap2.x) / 2;

        BitmapBean mDesBitmap2 = new BitmapBean();
        mDesBitmap2.width = mBitmap2.width;
        mDesBitmap2.height = mBitmap2.height;
        mDesBitmap2.y = mBitmap2.y;
        mDesBitmap2.x = (mBitmap2.x + mBitmap3.x) / 2;
        mList1.set(0, mDesBitmap);
        mList1.set(1, mDesBitmap2);
        mList1.remove(2);
    }

    private static void modifyListWhenCountSeven(List<BitmapBean> mList1) {
        System.out.println("[modifyListWhenCountSeven]" + mList1.size());
        BitmapBean mBitmap1 = mList1.get(4);
        System.out.println("[modifyListWhenCountSeven]" + mBitmap1);
//		MyBitmap mDesBitmap = new MyBitmap();
//		mDesBitmap.width = mBitmap1.width;
//		mDesBitmap.height = mBitmap1.height;
//		mDesBitmap.y = mList1.get(0).y;
//		mDesBitmap.x = mBitmap1.x;
//		
//		mList1.set(0, mDesBitmap);
        mList1.remove(0);
        mList1.remove(1);
    }

    private static void modifyListWhenCountEight(List<BitmapBean> mList1) {
        BitmapBean mBitmap1 = mList1.get(0);
        BitmapBean mBitmap2 = mList1.get(1);
        BitmapBean mBitmap3 = mList1.get(2);
        BitmapBean mDesBitmap = new BitmapBean();
        mDesBitmap.width = mBitmap1.width;
        mDesBitmap.height = mBitmap1.height;
        mDesBitmap.y = mBitmap1.y;
        mDesBitmap.x = (mBitmap1.x + mBitmap2.x) / 2;

        BitmapBean mDesBitmap2 = new BitmapBean();
        mDesBitmap2.width = mBitmap2.width;
        mDesBitmap2.height = mBitmap2.height;
        mDesBitmap2.y = mBitmap2.y;
        mDesBitmap2.x = (mBitmap2.x + mBitmap3.x) / 2;
        mList1.set(0, mDesBitmap);
        mList1.set(1, mDesBitmap2);
        mList1.remove(2);
    }

    private static class ColumnRowCount {
        int rows;
        int columns;
        int count;

        public ColumnRowCount(int rows, int column, int count) {
            super();
            this.rows = rows;
            this.columns = column;
            this.count = count;
        }

        @Override
        public String toString() {
            return "ColumnRowCount [rows=" + rows + ", columns=" + columns
                    + ", count=" + count + "]";
        }
    }

    private ColumnRowCount generateColumnRowCountByCount(int count) {
        switch (count) {
            case 2:
                return new ColumnRowCount(1, 2, count);
            case 3:
            case 4:
                return new ColumnRowCount(2, 2, count);
            case 5:
            case 6:
                return new ColumnRowCount(2, 3, count);
            case 7:
            case 8:
            case 9:
                return new ColumnRowCount(3, 3, count);
            default:
                return new ColumnRowCount(1, 1, count);
        }
    }

}
