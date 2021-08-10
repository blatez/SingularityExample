package com.blate.singularity.ui.decoration;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Px;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blate.singularity.log.Logg;

/**
 * 一个简单的间距装饰器
 * 对于有方向的的整齐布局适用; ex:
 * {@link androidx.recyclerview.widget.LinearLayoutManager}
 * {@link GridLayoutManager}
 *
 * @author Az
 * on 2020/11/6
 */
public class SimpleSpaceDecoration
        extends RecyclerView.ItemDecoration {

    private static final String TAG = "SimpleSpaceDecoration";
    /**
     * 主轴方向条目之间的间距.
     * 只是条目与条目之间,条目与容器边界的间距不适用该值.有两种方法可以实现主轴条目与容器边界的间距设置:
     * 1.给容器设置padding.滑动时条目不能滑到容器边界,间距始终存在
     * 2.设置{@link #mStartSpace}和{@link #mEndSpace}
     * 对于横向的LinearLayoutManager或者gridLayoutManager,主轴方向时横向
     * 对于纵向的LinearLayoutManager或者gridLayoutManager,主轴方向时纵向
     */
    @Px
    private final int mMainAxisSpace;

    /**
     * 交叉轴方向条目之间的间距.
     * 只是条目与条目之间,条目与容器边界的间距不使用该值,实际上该类没有提供交叉轴与容器边界的间距设置,应该在容
     * 器上设置padding来实现交叉轴条目与容器边界的间距设置,滑动时条目可以滑到容器边界
     * 对于横向的LinearLayoutManager或者gridLayoutManager,交叉轴方向时纵向
     * 对于纵向的LinearLayoutManager或者gridLayoutManager,交叉轴方向时横向
     */
    @Px
    private final int mCrossAxisSpace;

    /**
     * 主轴开始条目的间距
     */
    @Px
    private final int mStartSpace;

    /**
     * 主轴结束条目的间距
     */
    @Px
    private final int mEndSpace;

    public SimpleSpaceDecoration(@Px int between) {
        this(0, 0, between, between);
    }

    public SimpleSpaceDecoration(@Px int extremity, @Px int between) {
        this(extremity, extremity, between, between);
    }

    public SimpleSpaceDecoration(@Px int startSpace, @Px int endSpace, @Px int between) {
        this(startSpace, endSpace, between, between);
    }

    public SimpleSpaceDecoration(@Px int startSpace, @Px int endSpace, @Px int mainAxisSpace, @Px int crossAxisSpace) {
        this.mStartSpace = startSpace;
        this.mEndSpace = endSpace;
        this.mMainAxisSpace = mainAxisSpace;
        this.mCrossAxisSpace = crossAxisSpace;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager && parent.getAdapter() != null) {
            getGridLayoutITemOffsets((GridLayoutManager) layoutManager, parent.getAdapter(), outRect, view, parent);
        } else if (layoutManager instanceof LinearLayoutManager && parent.getAdapter() != null) {
            getLinearLayoutItemOffsets((LinearLayoutManager) layoutManager, parent.getAdapter(), outRect, view, parent);
        } else {
            Log.w(TAG, "unsupported LayoutManager");
        }
    }

    private void getGridLayoutITemOffsets(@NonNull GridLayoutManager layoutManager,
                                          @NonNull RecyclerView.Adapter<?> adapter,
                                          @NonNull Rect outRect,
                                          @NonNull View view,
                                          @NonNull RecyclerView parent) {
        int position = parent.getChildLayoutPosition(view);
        boolean isEnd = (adapter.getItemCount() + layoutManager.getSpanCount() - 1) / layoutManager.getSpanCount()
                == (position + layoutManager.getSpanCount() - 1) / layoutManager.getSpanCount();

        //交叉轴的序号
        int crossIndex = position % layoutManager.getSpanCount();
        if (layoutManager.getLayoutDirection() == RecyclerView.HORIZONTAL) {
            if (position < layoutManager.getSpanCount()) {
                //主轴首列
                outRect.left = layoutManager.getReverseLayout() ? mMainAxisSpace / 2 : mStartSpace;
                outRect.right = layoutManager.getReverseLayout() ? mStartSpace : mMainAxisSpace / 2;
            } else if (isEnd) {
                //主轴尾列
                outRect.left = layoutManager.getReverseLayout() ? mEndSpace : mMainAxisSpace / 2;
                outRect.right = layoutManager.getReverseLayout() ? mMainAxisSpace / 2 : mEndSpace;
            } else {
                outRect.left = mMainAxisSpace / 2;
                outRect.right = mMainAxisSpace / 2;
            }
            outRect.top = crossIndex * mCrossAxisSpace - crossIndex * (mCrossAxisSpace * (layoutManager.getSpanCount() - 1) / layoutManager.getSpanCount());
            outRect.bottom = (crossIndex + 1) * (mCrossAxisSpace * (layoutManager.getSpanCount() - 1) / layoutManager.getSpanCount()) - crossIndex * mCrossAxisSpace;
        } else if (layoutManager.getLayoutDirection() == RecyclerView.VERTICAL) {
            if (position < layoutManager.getSpanCount()) {
                //主轴首行
                outRect.top = layoutManager.getReverseLayout() ? mMainAxisSpace / 2 : mStartSpace;
                outRect.bottom = layoutManager.getReverseLayout() ? mStartSpace : mMainAxisSpace / 2;
            } else if (isEnd) {
                //主轴尾行
                outRect.top = layoutManager.getReverseLayout() ? mEndSpace : mMainAxisSpace / 2;
                outRect.bottom = layoutManager.getReverseLayout() ? mMainAxisSpace / 2 : mEndSpace;
            } else {
                outRect.top = mMainAxisSpace / 2;
                outRect.bottom = mMainAxisSpace / 2;
            }
            outRect.left = crossIndex * mCrossAxisSpace - crossIndex * (mCrossAxisSpace * (layoutManager.getSpanCount() - 1) / layoutManager.getSpanCount());
            outRect.right = (crossIndex + 1) * (mCrossAxisSpace * (layoutManager.getSpanCount() - 1) / layoutManager.getSpanCount()) - crossIndex * mCrossAxisSpace;
        } else {
            Log.w(TAG, "unsupported LayoutDirection");
        }
    }

    private void getLinearLayoutItemOffsets(@NonNull LinearLayoutManager layoutManager,
                                            @NonNull RecyclerView.Adapter<?> adapter,
                                            @NonNull Rect outRect,
                                            @NonNull View view,
                                            @NonNull RecyclerView parent) {
        if (layoutManager.getLayoutDirection() == RecyclerView.HORIZONTAL) {
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.left = layoutManager.getReverseLayout() ? mMainAxisSpace / 2 : mStartSpace;
                outRect.right = layoutManager.getReverseLayout() ? mStartSpace : mMainAxisSpace / 2;
            } else if (parent.getChildLayoutPosition(view) == adapter.getItemCount() - 1) {
                outRect.left = layoutManager.getReverseLayout() ? mEndSpace : mMainAxisSpace / 2;
                outRect.right = layoutManager.getReverseLayout() ? mMainAxisSpace / 2 : mEndSpace;
            } else {
                outRect.left = mMainAxisSpace / 2;
                outRect.right = mMainAxisSpace / 2;
            }
        } else if (layoutManager.getLayoutDirection() == RecyclerView.VERTICAL) {
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = layoutManager.getReverseLayout() ? mMainAxisSpace / 2 : mStartSpace;
                outRect.bottom = layoutManager.getReverseLayout() ? mStartSpace : mMainAxisSpace / 2;
            } else if (parent.getChildLayoutPosition(view) == adapter.getItemCount() - 1) {
                outRect.top = layoutManager.getReverseLayout() ? mEndSpace : mMainAxisSpace / 2;
                outRect.bottom = layoutManager.getReverseLayout() ? mMainAxisSpace / 2 : mEndSpace;
            } else {
                outRect.top = mMainAxisSpace / 2;
                outRect.bottom = mMainAxisSpace / 2;
            }
        } else {
            Log.w(TAG, "unsupported LayoutDirection");
        }
    }

}
