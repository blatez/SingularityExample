package com.blate.singularity.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public class ViewPainter {

    /**
     * 保存View的快照
     * 如果View包含布局参数,使用View的布局参数进行布局保存
     * 如果View不包含布局参数,缺省值是{@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}
     * 这不适用于 AdapterView
     *
     * @param view view
     * @return snapshot
     */
    public static Bitmap saveViewSnapshot(@NonNull View view) {
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            return saveViewSnapshot(view, layoutParams.width, layoutParams.height);
        } else {
            return saveViewSnapshot(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    /**
     * 保存View的快照
     * 按照给定的尺寸布局
     * 这不适用于 AdapterView
     *
     * @param view   view
     * @param width  宽度
     *               如果等于{@link ViewGroup.LayoutParams#MATCH_PARENT},按照屏幕宽度布局.
     *               如果等于{@link ViewGroup.LayoutParams#WRAP_CONTENT},按照屏幕宽度布局,最大可以是屏幕宽度.
     *               如果大于等于0,按照此尺寸布局.
     *               其他情况按照内容大小布局,,最大可以是屏幕宽度.
     * @param height 高度
     *               如果等于{@link ViewGroup.LayoutParams#MATCH_PARENT},按照屏幕高度布局.
     *               如果等于{@link ViewGroup.LayoutParams#WRAP_CONTENT},按照内容需要布局,最大可以是屏幕高度.
     *               如果大于等于0,按照此尺寸布局.
     *               其他情况按照内容大小布局,最大可以是屏幕高度.
     * @return 快照
     */
    public static Bitmap saveViewSnapshot(@NonNull View view, int width, int height) {
        final Point displaySize = new Point();
        view.getContext().getDisplay().getRealSize(displaySize);

        int widthMeasureSpec;
        if (width == ViewGroup.LayoutParams.MATCH_PARENT) {
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(displaySize.x, View.MeasureSpec.EXACTLY);
        } else if (width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(displaySize.x, View.MeasureSpec.AT_MOST);
        } else if (width < 0) {
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(displaySize.x, View.MeasureSpec.AT_MOST);
        } else {
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.UNSPECIFIED);
        }

        int heightMeasureSpec;
        if (height == ViewGroup.LayoutParams.MATCH_PARENT) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(displaySize.y, View.MeasureSpec.EXACTLY);
        } else if (height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(displaySize.y, View.MeasureSpec.AT_MOST);
        } else if (height < 0) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(displaySize.y, View.MeasureSpec.AT_MOST);
        } else {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.UNSPECIFIED);
        }

        return saveViewSnapshotByMeasureSpec(view, widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 保存View快照
     * 使用给定的测量规格保存.
     * 测量规格不是尺寸,包括了测量模式和尺寸.如果想按照尺寸保存,调用{@link #saveViewSnapshot(View, int, int)}
     * 这不适用于 AdapterView
     *
     * @param view              view
     * @param widthMeasureSpec  宽度测量规格
     * @param heightMeasureSpec 高度测量规格
     * @return snapshot
     */
    public static Bitmap saveViewSnapshotByMeasureSpec(@NonNull View view,
                                                       int widthMeasureSpec,
                                                       int heightMeasureSpec) {
        view.measure(widthMeasureSpec, heightMeasureSpec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap snapshot = Bitmap.createBitmap(
                view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(snapshot);
        view.draw(canvas);
        return snapshot;
    }

}
