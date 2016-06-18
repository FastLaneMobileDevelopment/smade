package io.bega.servicebase.model.items;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;

import com.mikepenz.fastadapter_extensions.swipe.SimpleSwipeCallback;

/**
 * Created by user on 4/23/16.
 */
public class ExpandableSimpleSwipeDragCallback extends SimpleSwipeCallback {
    public ExpandableSimpleSwipeDragCallback(ItemSwipeCallback itemSwipeCallback, Drawable leaveBehindDrawableLeft) {
        super(itemSwipeCallback, leaveBehindDrawableLeft);
    }

    public ExpandableSimpleSwipeDragCallback(ItemSwipeCallback itemSwipeCallback, Drawable leaveBehindDrawableLeft, int swipeDirs) {
        super(itemSwipeCallback, leaveBehindDrawableLeft, swipeDirs);
    }

    public ExpandableSimpleSwipeDragCallback(ItemSwipeCallback itemSwipeCallback, Drawable leaveBehindDrawableLeft, int swipeDirs, @ColorInt int bgColor) {
        super(itemSwipeCallback, leaveBehindDrawableLeft, swipeDirs, bgColor);
    }


    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }
}
