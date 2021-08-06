package com.blate.singularity.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Blate
 * Created in 2021/4/28
 */
public class ThreadManager {

    private volatile static ThreadManager sInstance;

    public static ThreadManager getInstance() {
        if (sInstance == null) {
            synchronized (ThreadManager.class) {
                if (sInstance == null) {
                    sInstance = new ThreadManager();
                }
            }
        }
        return sInstance;
    }

    private Executor mIoExecutor;

    private ThreadManager() {
        createExecutor();
    }

    private void createExecutor() {
        mIoExecutor = new ThreadPoolExecutor(
                0,
                Integer.MAX_VALUE,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                (ThreadFactory) Thread::new);
    }

    public Executor getIoExecutor() {
        return mIoExecutor;
    }


}
