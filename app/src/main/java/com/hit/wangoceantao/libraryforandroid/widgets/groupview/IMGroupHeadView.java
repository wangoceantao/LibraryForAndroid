package com.hit.wangoceantao.libraryforandroid.widgets.groupview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.hecom.application.SOSApplication;
import com.hecom.im.dao.IMFriend;
import com.hecom.im.dao.IMGroup;
import com.hecom.management.R;
import com.hecom.user.utils.SplashUtils;
import com.hecom.util.ImTools;
import com.hecom.util.ImageOptionsFactory;
import com.hecom.util.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by licong on 16/5/30.
 *
 */
public class IMGroupHeadView extends GroupImageView {

    private String currentGroupId="";
    private List<Integer> friendIds = new ArrayList<>();
    private int maxRadius, useRadius;

    public IMGroupHeadView(Context context) {
        super(context);
        init();
    }

    public IMGroupHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IMGroupHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        maxRadius = useRadius = mWidth = mHeight = Tools.dip2px(SOSApplication.getInstance(), 48);
    }

    public void setGroupImage(String groupId) {
//        if(currentGroupId.equals(groupId)) return;
        currentGroupId = groupId;
        setGroupImage(SOSApplication.getInstance().getGroupMap().get(groupId));
    }

    public void setGroupImage(String groupId, int value) {
        maxRadius = useRadius = mWidth = mHeight = Tools.dip2px(SOSApplication.getInstance(), value);
        setGroupImage(groupId);
    }

    /**
     * R.drawable.contact_head_group
     * @param group
     */
    private void setGroupImage(IMGroup group) {
        ArrayList<String> urls = new ArrayList<>();
        friendIds.clear();
        if(group == null) {
            urls.add("default_group_image");
            friendIds.add(R.drawable.contact_head_group);
        } else {
            if(TextUtils.isEmpty(group.getGroupImage())) {
                List<IMGroup.Member> memberList = group.getMemberList();
                IMFriend friend;
                String url;
                int drawable;
                for (IMGroup.Member member : memberList) {
                    if (urls.size() >= 4)
                        break;
                    friend = SOSApplication.getInstance().getFriendMap().get(member.getImAccount());
                    if (friend != null) {
                        url = SplashUtils.getImageUrl(friend.getHeadUrl());
                        drawable = ImTools.getDefaultHeadByString(friend.getLoginId());
                        if (TextUtils.isEmpty(url))
                            url = "drawable://" + drawable;
                        urls.add(url);
                        friendIds.add(drawable);
                    }
                }
                if (urls.size() == 0) {
                    urls.add("default_group_image");
                    friendIds.add(R.drawable.contact_head_group);
                }
            } else {
                urls.add(SplashUtils.getImageUrl(group.getGroupImage()));
                friendIds.add(R.drawable.contact_head_group);
            }
        }
        calculateRadius(urls);
        setDatas(urls);
    }

    /**
     * 计算实际显示的图片直径
     */
    protected void calculateRadius(ArrayList<String> urls) {
        if(urls.size() == 1) {
            useRadius = maxRadius;
        } else {
            useRadius = maxRadius / 2;
        }
    }


    @Override
    protected void loadImageDatas() {
        if (mContainerView == null) {
            return;
        }
        int childCount = mContainerView.getChildCount();
        for (int index = 0; index < childCount; index++) {
            ImageView childView = (ImageView) mContainerView.getChildAt(index);
            String url = mUrls.get(index);

            SOSApplication.getGlobalImageLoader().displayImage(
                    url,
                    childView,
                    ImageOptionsFactory.getCircleBitmapOption(
                            useRadius, friendIds.get(index)));
        }
    }

}
