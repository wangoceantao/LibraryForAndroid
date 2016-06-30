package com.hit.wangoceantao.libraryforandroid.widgets.groupview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hit.wangoceantao.libraryforandroid.R;
import com.hit.wangoceantao.libraryforandroid.utils.GenericViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 群头像演示demo
 * <p/>
 * Created by wanghaitao on 16/5/29 11:07.
 * <p/>
 * Email：wanghaitao01@hecom.cn
 */
public class GroupImageSampleActivity extends Activity {
    private ListView mListView;
    private List<ArrayList<String>> mDatas = new ArrayList<ArrayList<String>>();
    private GroupImageAdapter mAdapter;
    private String[] urls = new String[]{
            "http://img540.ph.126.net/5PyPfeSlM3ZWt_npImBosw==/1348265138445286339.jpg",
            "http://imgsrc.baidu.com/forum/pic/item/89f4051f95cad1c87ccacc6b7e3e6709c83d5147.jpg",
            "http://img4.imgtn.bdimg.com/it/u=1321274421,3834507557&fm=21&gp=0.jpg",
            "http://img3.3lian.com/2013/s1/59/d/67.jpg",
            "http://img5.pcpop.com/ArticleImages/picshow/900x675/20150604/2015060415102376862.jpg",
            "http://s11.sinaimg.cn/middle/54da4dbdg7433cd23f9ea&690&690",
            "http://d.hiphotos.baidu.com/image/pic/item/6159252dd42a2834977baad65cb5c9ea14cebf0a.jpg",
            "http://h.hiphotos.baidu.com/image/w%3D310/sign=bc968ede9545d688a302b4a594c37dab/024f78f0f736afc3065a7888b419ebc4b645128c.jpg",
            "http://img14.poco.cn/mypoco/myphoto/20130131/22/17323571520130131221457027_640.jpg",
            "http://img.taopic.com/uploads/allimg/110906/1382-110Z611025585.jpg",
            "http://img01.taopic.com/150920/240455-1509200H31810.jpg",
            "http://imgst-dl.meilishuo.net/pic/_o/84/a4/a30be77c4ca62cd87156da202faf_1440_900.jpg",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_image_sample);
        initUI();
        loadDatas();
    }

    private void initUI() {
        mListView = (ListView) findViewById(R.id.listview);
        mAdapter = new GroupImageAdapter(getApplicationContext());
        mListView.setAdapter(mAdapter);
    }

    private void loadDatas() {
        for (int index = 0; index < 9; index++) {
            ArrayList<String> childUrls = new ArrayList<>();
            int childCount = index + 1;
            for (int childIndex = 0; childIndex < childCount; childIndex++) {
                int imageIndex = Math.min(childIndex, urls.length - 1);
                childUrls.add(urls[imageIndex]);
            }
            mDatas.add(childUrls);
        }
        mAdapter.notifyDataSetChanged();
    }

    class GroupImageAdapter extends BaseAdapter {
        private Context mContext;

        public GroupImageAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            if (mDatas != null && position >= 0 && position < mDatas.size()) {
                mDatas.get(position);
            }
            return "";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_group_image_sample, null);
            }
            GroupImageView groupImageView = GenericViewHolder.get(convertView, R.id.group_image);
            TextView infoView = GenericViewHolder.get(convertView, R.id.info);
            if (mDatas != null && position >= 0 && position < mDatas.size()) {
                ArrayList<String> data = mDatas.get(position);
                groupImageView.setDatas(data, R.mipmap.ic_launcher, 400);
                infoView.setText("image size:" + (position + 1));
            }

            return convertView;
        }
    }
}
