package com.hit.wangoceantao.libraryforandroid.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description: 网络状态监听
 * <p/>
 * Created by wanghaitao on 16/6/18 16:09.
 * <p/>
 * Email：wangoceantao@gmail.com
 */
public class ConnectionManager {
    private final static String TAG = "ConnectionManager";

    private static ConnectionManager mInstance;

    public enum Type {
        NONE, WIFI, MOBILE,
    };

    public interface IConnectionChangeListener {
        /**
         * 连接断开
         */
        void onDisconnect();
        /**
         * 连接上
         */
        void onConnected(Type type);
    }

    private List<IConnectionChangeListener> mListeners;
    private Context mContext;
    private ConnectionChangeReceiver mReceiver;

    public static ConnectionManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ConnectionManager.class) {
                if (mInstance == null) {
                    mInstance = new ConnectionManager(context);
                }
            }
        }
        return mInstance;
    }

    public ConnectionManager(Context context) {
        LogEx.enter();
        mContext = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mReceiver = new ConnectionChangeReceiver();
        mContext.registerReceiver(mReceiver, filter);
    }

    /**
     * 注册网络状态变化监听器
     * @param listener
     */
    public void registerListener(IConnectionChangeListener listener) {
        if (mListeners == null) {
            mListeners = Collections.synchronizedList(new ArrayList<IConnectionChangeListener>());
        }
        synchronized (mListeners) {
            if (mListeners != null && !mListeners.contains(listener)) {
                mListeners.add(listener);
            }
        }
    }

    /**
     * 反注册网络状态变化监听器
     * @param listener
     */
    public void unRegisterListener(IConnectionChangeListener listener) {
        if (mListeners != null) {
            synchronized(mListeners) {
                if(mListeners != null) {
                    mListeners.remove(listener);
                }
            }
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        LogEx.enter();
        if (mListeners != null) {
            synchronized(mListeners) {
                if(mListeners != null) {
                    mListeners.clear();
                }
            }
        }
        mContext.unregisterReceiver(mReceiver);
        mInstance = null;
    }

    /**
     * wifi是否已连接
     *
     * @return
     */
    public boolean isWifiConnected() {
        ConnectivityManager mgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
    }

    /**
     * 获取当前网络的类型
     *
     * @return 网络类型
     */
    public ConnectionManager.Type getOutgoingRoute() {
        final ConnectivityManager connectivity = getConnectivityManager();
        if (null == connectivity) {
            LogEx.e(TAG, "ConnectivityManager is not exsited!");
            return Type.NONE;
        }

        final NetworkInfo network = connectivity.getActiveNetworkInfo();
        if (network != null && network.isAvailable() && !network.isRoaming()) {
            switch (network.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    return Type.WIFI;
                case ConnectivityManager.TYPE_MOBILE:
                    return Type.MOBILE;
            }
        }

        return Type.NONE;
    }

    /**
     * 当前网络连接是否可用
     *
     * @return
     */
    public boolean isNetworkAvailable() {
        final ConnectivityManager connectivity = getConnectivityManager();
        if (null == connectivity) {
            LogEx.e(TAG, "ConnectivityManager is not exsited!");
            return false;
        }
        return getOutgoingRoute() != Type.NONE;
    }

    /**
     * 当前网络类型是否可用
     *
     * @param type
     * @return
     */
    public boolean isNetworkAvailable(Type type) {
        final ConnectivityManager connectivity = getConnectivityManager();
        if (null == connectivity) {
            LogEx.e(TAG, "ConnectivityManager is not exsited!");
            return false;
        }

        switch (type) {
            case WIFI:
                return isNetworkAvailable(connectivity, ConnectivityManager.TYPE_WIFI);

            case MOBILE:
                return isNetworkAvailable(connectivity, ConnectivityManager.TYPE_MOBILE);
        }

        return false;
    }

    private boolean isNetworkAvailable(ConnectivityManager connectivity, int networkType) {
        final NetworkInfo network = connectivity.getNetworkInfo(networkType);
        if (null != network) {
            return network.isAvailable();
        } else {
            return false;
        }
    }

    private ConnectivityManager getConnectivityManager() {
        if (null == mContext) {
            return null;
        }

        return (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public class ConnectionChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (context == null || intent == null) {
                return;
            }
            String action = intent.getAction();
            LogEx.i(TAG, "connection getAction = " + intent.toString());
            //TODO
            //如果当前连接着mobile，连接到wifi，android 5.0 会发送一次mobile断开，一次wifi连接上广播,
            //或者连着mobile时，断开wifi，android 5.0 会发送一次wifi断开，一次mobile连接上广播,

            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {

                ConnectivityManager connectMgr = getConnectivityManager();
                NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mobNetInfo != null && wifiNetInfo != null
                        && !mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                    dispatchDisConnect();
                }else if(mobNetInfo != null && mobNetInfo.isConnected()){
                    dispatchConnected(Type.MOBILE);
                } else if(wifiNetInfo != null && wifiNetInfo.isConnected()) {
                    dispatchConnected(Type.WIFI);
                }
            }
            return;
        }
    }

    private void dispatchConnected(Type type) {
        LogEx.d(TAG,"dispatchConnected type = " + type);
        if (mListeners != null) {
            synchronized(mListeners) {
                if(mListeners != null) {
                    for (IConnectionChangeListener listener : mListeners) {
                        listener.onConnected(type);
                    }
                }
            }
        }
    }

    private void dispatchDisConnect() {
        LogEx.d(TAG,"dispatchDisConnect");
        if (mListeners != null) {
            synchronized(mListeners) {
                if(mListeners != null) {
                    for (IConnectionChangeListener listener : mListeners) {
                        listener.onDisconnect();
                    }
                }
            }
        }
    }
}
