package com.blate.singularity.repository;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.blate.singularity.storage.StorageTools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MediaTools {

    /**
     * 刷新媒体数据库,只刷新更新的uri
     * 保存图片到相册后,需要刷新媒体数据库才能在系统相册显示
     *
     * @param context context
     * @param uri     更新的uri
     */
    public static void refreshAlbum(@NonNull Context context, @NonNull Uri uri) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    /**
     * 保存一个图片流到系统相册
     *
     * @param context     context
     * @param inputStream 图片流
     * @param path        保存路径,可以为空
     * @param displayName 显示名字,可以为空
     * @return 保存图片的uri, 这个uri没有针对外部包授权.保存失败返回null
     */
    @Nullable
    public static Uri saveImageToAlbum(
            @NonNull Context context,
            @NonNull InputStream inputStream,
            @Nullable String path,
            @Nullable String displayName) {
        Pair<ContentValues, Uri> pair = createAlbumFileUri(context, path, displayName);
        if (pair == null) {
            return null;
        }
        OutputStream outputStream = null;
        try {
            outputStream = context.getContentResolver().openOutputStream(pair.second);
            byte[] buff = new byte[4096];
            for (int length = inputStream.read(buff); length > 0; length = inputStream.read(buff)) {
                outputStream.write(buff, 0, length);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                pair.first.put(MediaStore.MediaColumns.IS_PENDING, false);
            }
            return pair.second;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                inputStream.close();
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存一个图片流到系统相册
     *
     * @param context     context
     * @param bitmap      位图
     * @param path        保存路径,可以为空
     * @param displayName 显示名字,可以为空
     * @return 保存图片的uri, 这个uri没有针对外部包授权.保存失败返回null
     */
    @Nullable
    public static Uri saveImageToAlbum(
            @NonNull Context context,
            @NonNull Bitmap bitmap,
            @Nullable String path,
            @Nullable String displayName) {
        Pair<ContentValues, Uri> pair = createAlbumFileUri(context, path, displayName);
        if (pair == null) {
            return null;
        }
        OutputStream outputStream = null;
        try {
            outputStream = context.getContentResolver().openOutputStream(pair.second);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                pair.first.put(MediaStore.MediaColumns.IS_PENDING, false);
            }
            return pair.second;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 保存一个图片流到@link Context#getExternalFilesDir(String)}参数{@link Environment#DIRECTORY_PICTURES}
     * 文件夹下
     *
     * @param context     context
     * @param inputStream 图片输入流
     * @param fileName    文件名,不是必须的.如果文件已经存在,将以 name(index)的方式命名
     * @return 保存文件的uri, 这个uri没有针对外部包授权.如果保存失败返回null
     */
    @WorkerThread
    public static Uri saveImageToPictureDir(@NonNull Context context,
                                            @NonNull InputStream inputStream,
                                            @Nullable String fileName) {
        OutputStream outputStream = null;
        try {
            Uri uri = createPictureFileUri(context, fileName);
            outputStream = context.getContentResolver().openOutputStream(uri);
            byte[] buff = new byte[4096];
            for (int length = inputStream.read(buff); length > 0; length = inputStream.read(buff)) {
                outputStream.write(buff, 0, length);
            }
            return uri;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                inputStream.close();
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存一个位图到@link Context#getExternalFilesDir(String)}参数{@link Environment#DIRECTORY_PICTURES}
     * 文件夹下
     *
     * @param context  context context
     * @param bitmap   位图
     * @param fileName 文件名,不是必须的.如果文件已经存在,将以 name(index)的方式命名
     * @return 保存文件的uri, 这个uri没有针对外部包授权.如果保存失败返回null
     */
    @WorkerThread
    public static Uri saveImageToPicture(@NonNull Context context,
                                         @NonNull Bitmap bitmap,
                                         @Nullable String fileName) {
        OutputStream outputStream = null;
        try {
            Uri uri = createPictureFileUri(context, fileName);
            outputStream = context.getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return uri;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取一个{@link Context#getExternalFilesDir(String)}参数{@link Environment#DIRECTORY_PICTURES}
     * 中文件的uri
     *
     * @param context  context
     * @param fileName 文件名,不是必须的.如果文件已经存在,将以 fileName(index)的方式命名
     * @return uri, 这个uri没有针对外部包授权
     */
    private static Uri createPictureFileUri(@NonNull Context context,
                                            @Nullable String fileName) throws IOException {
        String effectiveFileName = TextUtils.isEmpty(fileName) ? String.valueOf(System.nanoTime()) : fileName;
        assert effectiveFileName != null;
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), effectiveFileName);
        for (int index = 0; file.exists() && file.isFile(); index += 1) {
            file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), String.format("%s(%s)", effectiveFileName, index));
        }
        file.createNewFile();
        return StorageTools.getUriFromFile(context, file);
    }

    /**
     * 获取一个系统相册文件的uri
     * 如果底层出现异常,返回null
     * 在Android Q以上的系统,如果需要保存的图片可供其他应用访问,应该在保存完后设置:
     * contentValues.put(MediaStore.MediaColumns.IS_PENDING, false);
     *
     * @param context     context
     * @param path        保存路径,可以为空.Android Q以下设置无效
     * @param displayName 显示名字,可以为空
     * @return Pair中的uri没有针对外部包授权
     */
    @Nullable
    public static Pair<ContentValues, Uri> createAlbumFileUri(
            @NonNull Context context,
            @Nullable String path,
            @Nullable String displayName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, TextUtils.isEmpty(displayName) ? String.valueOf(System.nanoTime()) : displayName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!TextUtils.isEmpty(path)) {
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, path);
            }
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, true);
        }
        Uri inserted = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        if (inserted == null) {
            return null;
        } else {
            return new Pair<>(contentValues, inserted);
        }
    }

}
