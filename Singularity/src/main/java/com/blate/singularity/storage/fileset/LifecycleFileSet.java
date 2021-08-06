package com.blate.singularity.storage.fileset;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * 带有生命周期的文件集合,用来管理一堆文件
 * 可以调用{@link #deleteFiles()}删除文件
 * 在生命周期拥有者状态变为{@link androidx.lifecycle.Lifecycle.Event#ON_DESTROY}这些文件会自动删除
 */
public class LifecycleFileSet
        extends FileSet {

    /**
     * 在生命周期拥有者状态变为{@link androidx.lifecycle.Lifecycle.Event#ON_DESTROY}删除文件
     *
     * @param lifecycleOwner 生命周期拥有者
     */
    public LifecycleFileSet(@NonNull LifecycleOwner lifecycleOwner) {
        lifecycleOwner.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                deleteFiles();
            }
        });
    }

}
