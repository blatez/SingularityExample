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
     * 只是条目与条目之间,边界不在此列
     * 对于横向的LinearLayoutManager或者gridLayoutManager,主轴方向时横向
     * 对于纵向的LinearLayoutManager或者gridLayoutManager,主轴方向时纵向
     */
    @Px
    private final int mMainAxisSpace;

    /**
     * 交叉轴方向条目之间的间距.
     * 只是条目与条目之间,边界不在此列
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
        if (layoutManager instanceof GridLayoutManager) {
            setGridSpace((GridLayoutManager) layoutManager, outRect, view, parent);
        } else if (layoutManager instanceof LinearLayoutManager && parent.getAdapter() != null) {
            getLinearLayoutItemOffsets((LinearLayoutManager) layoutManager, parent.getAdapter(), outRect, view, parent);
        } else {
            Log.w(TAG, "unsupported LayoutManager");
        }
    }

    private void setGridSpace(@NonNull GridLayoutManager layoutManager, @NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent) {
        int position = parent.getChildAdapterPosition(view);
        int columnCount = layoutManager.getSpanCount();

        int column = position % columnCount;
        int columnMargin = mColumnSpace * (columnCount - 1) / columnCount;
        outRect.left = column * mColumnSpace - column * columnMargin;
        outRect.right = (column + 1) * columnMargin - column * mColumnSpace;

        int rowCount = (layoutManager.getItemCount() + (columnCount - 1)) / columnCount;
        int row = position / columnCount;
        int rowMargin = mRowSpace * (rowCount - 1) / rowCount;
        outRect.top = row * mRowSpace - row * rowMargin;
        outRect.bottom = (row + 1) * rowMargin - row * mRowSpace;
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
