package com.blate.singularity.storage.fileset;

import androidx.annotation.WorkerThread;

import com.blate.singularity.storage.StorageTools;
import com.blate.singularity.thread.ThreadManager;

import java.io.File;
import java.util.HashSet;
import java.util.List;

/**
 * 一堆文件的容器
 * 最后可以统一删除
 */
public class FileSet {

    public final HashSet<File> mFiles = new HashSet<>();

    public void putFile(File file) {
        if (file != null) {
            mFiles.add(file);
        }
    }

    public void putFiles(File... files) {
        if (files != null) {
            for (File file : files) {
                putFile(file);
            }
        }
    }

    public void putFiles(List<File> files) {
        if (files != null) {
            for (File file : files) {
                putFile(file);
            }
        }
    }

    @WorkerThread
    public void deleteFiles() {
        for (File file : mFiles) {
            StorageTools.deleteFileOrDirectory(file);
        }
    }

}
