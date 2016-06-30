/*
 * Copyright (C) 2013 Baidu Inc. All rights reserved.
 */
package com.hit.wangoceantao.libraryforandroid.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.leo.wifi.WMApplication;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author ligang
 * @since Dec 3, 2013
 */
public class SingletonHolder {

    private static ScheduledThreadPoolExecutor mExecutor;
    private static Object mLock4Executor = new Object();

    private static RequestQueue mRequestQueue;
    private static Object mLock4RequestQueue = new Object();

    private static SerialExecutor mSerialExecutor;
    private static Object mLock4SerialExecutor = new Object();

    private static final int CORE_POOL_SIZE = 5;

    private static HandlerThread mHandlerThread;
    private static Object mLock4Handler = new Object();
    private static Handler mHandler;

    public interface AsynExecutCommomResult {
        public void onSuccessed();
        public void onFailed();
    }

    /**
     * 获取RequestQueue的实例
     * 
     * @return RequestQueue对象
     */
    public static RequestQueue getRequestQueueInstance() {
        if (mRequestQueue == null) {
            synchronized (mLock4RequestQueue) {
                if (mRequestQueue == null) {
                    mRequestQueue = Volley.newRequestQueue(WMApplication.getInstance());
                }
            }
        }

        return mRequestQueue;
    }

    /**
     * 获取ScheduledThreadPoolExecutor的实例
     * 
     * @return ScheduledThreadPoolExecutor对象
     */
    public static ScheduledThreadPoolExecutor getExecutorInstance() {
        if (mExecutor == null) {
            synchronized (mLock4Executor) {
                if (mExecutor == null) {
                    mExecutor = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE, new DefaultThreadFactory());
                }
            }
        }

        return mExecutor;
    }

    /**
     * The default thread factory
     */
    static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            t.setPriority(Thread.MIN_PRIORITY);
            return t;
        }
    }

    /**
     * 在mHandlerThread里执行的任务都是在一个线程里面按顺序执行的，要是前面的任务阻塞住了，后面的任务必须等待
     * 除了在remote进程使用本方法外，其他涉及到线程处理的建议调用getExecutorInstance()方法
     * @return
     */
    @Deprecated
    public static Handler getAsynHandler() {
        LogEx.enter();
        if(mHandler == null) {
            synchronized (mLock4Handler) {
                if(mHandler == null) {
                    mHandlerThread = new HandlerThread("Wifi-Thread");
                    mHandlerThread.start();
                    mHandler = new Handler(mHandlerThread.getLooper(),new Handler.Callback(){
                        @Override
                        public boolean handleMessage(Message msg) {
                            LogEx.enter();
                            return true;
                        }
                    });
                }
            }
        }
        LogEx.leave();
        return mHandler;
    }
    /**
     * 获取SerialExecutor的实例
     * 
     * @return SerialExecutor对象
     */
    public static SerialExecutor getSerialExecutor() {
        if (mSerialExecutor == null) {
            synchronized (mLock4SerialExecutor) {
                if (mSerialExecutor == null) {
                    mSerialExecutor = new SerialExecutor();
                }
            }
        }

        return mSerialExecutor;
    }

    /**
     * SerialExecutor
     */
    public static class SerialExecutor implements Executor {
        /** 任务队列。 */
        private final LinkedList<Runnable> mTasks = new LinkedList<Runnable>();

        /** 当前正在执行的任务。 */
        private Runnable mActive;

        @Override
        public synchronized void execute(final Runnable r) {
            mTasks.add(new Runnable() {
                public void run() {
                    try {
                        r.run();
                    } finally {
                        scheduleNext();
                    }
                }
            });
            if (mActive == null) {
                scheduleNext();
            }
        }

        /**
         * 执行下一个任务。
         */
        protected synchronized void scheduleNext() {
            mActive = mTasks.poll();

            if (mActive != null) {
                SingletonHolder.getExecutorInstance().execute(mActive);
            }
        }
    }

}
