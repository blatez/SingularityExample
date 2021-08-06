package com.blate.singularity.storage;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.blate.singularity.log.Logg;

import java.io.File;

/**
 * 存储管理类
 */
public class StorageMan {

    private static final String TAG = "StorageMan";

    public static volatile StorageMan sInstance;

    public static StorageMan getInstance(Context context) {
        if (sInstance == null) {
            synchronized (StorageMan.class) {
                if (sInstance == null) {
                    sInstance = new StorageMan(context);
                }
            }
        }
        return sInstance;
    }

    /**
     * 内部存储空间的易失缓存目录
     * 该目录中的缓存文件用后即删,即便在每次退出应用后都删除也不应该影响程序功能
     * 默认指向 context.getCacheDir()/volatile
     * 可以使用{@link #setInternalVolatileCacheDir(File)}指定目录,
     * 在调用过{@link #getInternalVolatileCacheDir()}后该值被锁定,无法设置
     */
    private File mInternalVolatileCacheDir;

    /**
     * 内部存储空间的易失缓存目录锁存标志
     */
    private boolean mLatchInternalVolatileCacheDir = false;


    /**
     * 外部存储空间易失缓存目录
     * 该目录中的缓存文件用后即删,即便在每次退出应用后删除也不应该影响程序功能
     * 默认指向context.getExternalCacheDir()/volatile
     * 可以使用{@link #setExternalVolatileCacheDir(File)} (File)}指定目录,
     * 在调用过{@link #getExternalVolatileCacheDir()} ()}后该值被锁定,无法设置
     */
    private File mExternalVolatileCacheDir;

    /**
     * 外部存储空间的易失缓存目录锁存标志
     */
    private boolean mLatchExternalVolatileCacheDir = false;

    private StorageMan(Context context) {
        initialize(context);
    }

    private void initialize(Context context) {
        mInternalVolatileCacheDir = new File(context.getCacheDir(), "volatile");
        mExternalVolatileCacheDir = new File(context.getExternalCacheDir(), "volatile");
    }

    /**
     * 设置内部易失缓存目录
     * 如果需要指向某些外部目录可能需要存储权限,注意设置时机
     * 应该在{@link #getInternalVolatileCacheDir()}之前调用
     * 且在版本迭代中更改了存储位置,可能需要转移已缓存的文件
     *
     * @param internalVolatileCacheDir 内部易失缓存目录
     */
    public void setInternalVolatileCacheDir(@NonNull File internalVolatileCacheDir) {
        if (mLatchInternalVolatileCacheDir) {
            Logg.w(TAG, String.format("[InternalVolatileCacheDir] has been used; set value[%s]fail; the current using value [%s]",
                    internalVolatileCacheDir.getPath(), mInternalVolatileCacheDir.getPath()));
            return;
        }
        mInternalVolatileCacheDir = internalVolatileCacheDir;
    }

    /**
     * 获取内部易失缓存目录
     *
     * @return 内部易失缓存目录
     */
    public File getInternalVolatileCacheDir() {
        if (!mInternalVolatileCacheDir.exists() || !mInternalVolatileCacheDir.isDirectory()) {
            mInternalVolatileCacheDir.mkdirs();
        }
        mLatchInternalVolatileCacheDir = true;
        return mInternalVolatileCacheDir;
    }

    /**
     * 设置外部易失缓存目录
     * 如果需要指向某些外部目录可能需要存储权限,注意设置时机
     * 应该在{@link #getExternalVolatileCacheDir()}之前调用
     * 且在版本迭代中更改了存储位置,可能需要转移已缓存的文件
     *
     * @param externalVolatileCAcheDir 内部易失缓存目录
     */
    public void setExternalVolatileCacheDir(@NonNull File externalVolatileCAcheDir) {
        if (mLatchExternalVolatileCacheDir) {
            Logg.w(TAG, String.format("[InternalVolatileCacheDir] has been used; set value[%s]fail; the current using value [%s]",
                    externalVolatileCAcheDir.getPath(), mExternalVolatileCacheDir.getPath()));
            return;
        }
        mExternalVolatileCacheDir = externalVolatileCAcheDir;
        mLatchExternalVolatileCacheDir = true;
    }

    /**
     * 获取外部易失缓存目录
     *
     * @return 外部易失缓存目录
     */
    public File getExternalVolatileCacheDir() {
        if (!mExternalVolatileCacheDir.exists() || !mExternalVolatileCacheDir.isDirectory()) {
            mExternalVolatileCacheDir.mkdirs();
        }
        mLatchExternalVolatileCacheDir = true;
        return mExternalVolatileCacheDir;
    }


}
