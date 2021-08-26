package com.blate.singularity.storage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.blate.singularity.thread.ThreadManager;

import java.io.File;

public class StorageTools {

    /**
     * 将一个File转化为生命周期绑定的File
     * 在生命周期所有者状态变为{@link androidx.lifecycle.Lifecycle.Event#ON_DESTROY}时,文件将被删除
     * 这对于拍照上传之类的应用场景会很方便.
     *
     * @param file           file
     * @param lifecycleOwner lifecycleOwner
     */
    public static void conversionLifecycleFile(@NonNull File file,
                                               @NonNull LifecycleOwner lifecycleOwner) {
        lifecycleOwner.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    ThreadManager.getInstance().getIoExecutor()
                            .execute(new Runnable() {
                                @Override
                                public void run() {
                                    deleteFileOrDirectory(file);
                                }
                            });
                }
            }
        });
    }

    /**
     * 删除一个文件或者目录
     *
     * @param file 文件或目录
     */
    @WorkerThread
    public static void deleteFileOrDirectory(File file) {
        if (file == null) {
            return;
        }
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children == null || children.length == 0) {
                file.delete();
            } else {
                for (File child : children) {
                    deleteFileOrDirectory(child);
                }
            }
        }
    }

    /**
     * 使用Singularity获取一个文件的授权uri
     * 这个Uri没有经过任何授权,如果需要提供给其他包使用,需要在Intent授权:
     * {@link Intent#FLAG_GRANT_READ_URI_PERMISSION}
     * {@link Intent#FLAG_GRANT_WRITE_URI_PERMISSION}
     * 可以直接调用{@link #authIntentUriPermission(Intent)}授权意图启动的组件对其中Uri进行读写
     * 或者直接调用{@link #getAuthUriFromFile(Context, File)}获取一个已经授权的uri,使用完后应该撤销权限(不撤销也没事,只是不安全)
     *
     * @param context context
     * @param file    file
     * @return uri
     */
    public static Uri getUriFromFile(@NonNull Context context, @NonNull File file) {
        return SingularityFileProvider.getUriForFile(
                context,
                String.format("%s.singularity.file.provider", context.getPackageName()), file);
    }

    /**
     * 授权Intent对其中Uri的读写权限
     *
     * @param intent intent
     */
    public static void authIntentUriPermission(@NonNull Intent intent) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    }

    /**
     * 使用Singularity获取一个文件的授权uri
     * 这个uri已经授权,可以给其他包使用.
     * 在其他包使用完后,不在允许访问时应该调用{@link Context#revokeUriPermission}撤销权限
     *
     * @param context context
     * @param file    file
     * @return uri
     */
    public static Uri getAuthUriFromFile(@NonNull Context context, @NonNull File file) {
        Uri uri = getUriFromFile(context, file);
        context.grantUriPermission(context.getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return uri;
    }

}
