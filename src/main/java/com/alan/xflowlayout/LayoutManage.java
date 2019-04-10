package com.alan.xflowlayout;

import android.view.View;



import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;

/**
 * Created by alan on  2019/4/9 12:52
 */
public class LayoutManage {

    @NotNull
    private  Flowlayout flowLayout;


    @NotNull
    public  Flowlayout getFlowLayout() {
        return this.flowLayout;
    }

    public  void onLayout(boolean changed, int l, int t, int r, int b) {
        if (this.flowLayout.orientation == Flowlayout.VERTICAL) {
            this.layoutVertical();
        }

        if (this.flowLayout.orientation == Flowlayout.HORIZONTAL) {
            this.layoutHrizontal();
        }

    }

    private  void layoutHrizontal() {
        int top = this.flowLayout.getPaddingTop();
        int left = 0;
        int childCount = this.flowLayout.getChildCount();
        int i = 0;

        for(int var5 = childCount; i < var5; ++i) {
            View child = this.flowLayout.getChildAt(i);
            Intrinsics.checkExpressionValueIsNotNull(child, "child");
            if (left + child.getMeasuredWidth() > this.flowLayout.getMeasuredWidth() - this.flowLayout.getPaddingLeft()) {
                left = this.flowLayout.getPaddingLeft();
                top += child.getMeasuredHeight() + this.flowLayout.itemInternal_vertical;
            }

            if (i == 0) {
                left = this.flowLayout.getPaddingLeft();
            }

            child.layout(left, top, child.getMeasuredWidth() + left, child.getMeasuredHeight() + top);
            left += child.getMeasuredWidth() + this.flowLayout.itemInternal_horizontal;
        }

    }

    private  void layoutVertical() {
        int top = this.flowLayout.getPaddingTop();
        int childCount = this.flowLayout.getChildCount();
        int i = 0;

        for(int var4 = childCount; i < var4; ++i) {
            View child = this.flowLayout.getChildAt(i);
            int var10001 = this.flowLayout.getPaddingLeft();
            Intrinsics.checkExpressionValueIsNotNull(child, "child");
            child.layout(var10001, top, child.getMeasuredWidth() + this.flowLayout.getPaddingLeft(), child.getMeasuredHeight() + top);
            top += child.getMeasuredHeight() + this.flowLayout.itemInternal_vertical;
        }

    }

    public LayoutManage(@NotNull Flowlayout flowLayout) {

        this.flowLayout = flowLayout;
    }
}
