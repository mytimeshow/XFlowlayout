package com.alan.xflowlayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;



import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

/**
 * Created by alan on  2019/4/9 12:42
 */
@SuppressLint("NewApi")
public class Flowlayout extends ViewGroup {
    public static final int  HORIZONTAL = 10010;
    public static final int VERTICAL = 10011;
    protected int itemInternal_horizontal;
    protected int itemInternal_vertical;
    protected int itemContentPadding;
    protected int orientation;
    protected int defaultBackGroundColor;
    protected int selectedbackGroundColor;
    protected float radius;
    protected int slectedTextColor;
    protected int defaultTextColor;
    protected LayoutManage layoutManager;
    protected String singleTeSelectedText;
    protected ArrayList<String> selectedTexts;
    protected boolean isSingleSelect;
    protected boolean isMoreSelect;
    protected float textSize;
    protected int defaultStokeColor;
    protected int selectedStokeColor;
    protected int maxSelect;
    protected int stokeWidth;
    protected int drawableId;
    protected boolean isAllAttrsDefault;
    protected int flag=-1;
    protected int index;
    protected int end;

    public Flowlayout(Context context) {
       this(context,null);
    }

    public Flowlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
       orientation = typeArray.getInt(R.styleable.FlowLayout_orientation, HORIZONTAL);
       itemInternal_horizontal = typeArray.getInt(R.styleable.FlowLayout_itemInternal_horizontal, 20);
       itemInternal_vertical = typeArray.getInt(R.styleable.FlowLayout_itemInternal_vertical, 20);
       radius = typeArray.getFloat(R.styleable.FlowLayout_item_radius,getResources().getDimension(R.dimen.dp_4));
       isSingleSelect = typeArray.getBoolean(R.styleable.FlowLayout_singgle_select, false);
       isMoreSelect = typeArray.getBoolean(R.styleable.FlowLayout_moreselect_select, false);
       maxSelect = typeArray.getInt(R.styleable.FlowLayout_max_select, 3);
       textSize = typeArray.getFloat(R.styleable.FlowLayout_text_size, 15.0F);
       itemContentPadding = typeArray.getInt(R.styleable.FlowLayout_item_content_internal, (int)this.getResources().getDimension(R.dimen.dp_6));
       defaultTextColor = typeArray.getColor(R.styleable.FlowLayout_default_textColor, Color.parseColor("#CCCCCC"));
       slectedTextColor = typeArray.getColor(R.styleable.FlowLayout_selected_textColor, Color.parseColor("#CCCCCC"));
       defaultBackGroundColor = typeArray.getColor(R.styleable.FlowLayout_item_default_backGroundDrawable, Color.WHITE);
       selectedbackGroundColor = typeArray.getColor(R.styleable.FlowLayout_item_select_backGroundDrawable, Color.WHITE);
       stokeWidth = typeArray.getInt(R.styleable.FlowLayout_stoke_width, 1);
       defaultStokeColor = typeArray.getColor(R.styleable.FlowLayout_default_stoke_color, Color.parseColor("#CCCCCC"));
       selectedStokeColor = typeArray.getColor(R.styleable.FlowLayout_selected_stoke_color, Color.RED);
       typeArray.recycle();
       layoutManager=new LayoutManage(this);
    }


    public  GradientDrawable defaultDrawable() {
        Drawable var10000 =getContext().getDrawable(R.drawable.bg_default);
        if (var10000 == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.graphics.drawable.GradientDrawable");
        } else {
            GradientDrawable drawable = (GradientDrawable)var10000;
            drawable.setCornerRadius(radius);
            drawable.setColor(defaultBackGroundColor);
            drawable.setStroke(stokeWidth,defaultStokeColor);
            drawable.setShape(GradientDrawable.RECTANGLE);
            return drawable;
        }
    }


    public  GradientDrawable selectedDrawable() {
        Drawable var10000 =getContext().getDrawable(R.drawable.bg_red);
        if (var10000 == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.graphics.drawable.GradientDrawable");
        } else {
            GradientDrawable drawable = (GradientDrawable)var10000;
            drawable.setCornerRadius(this.radius);
            drawable.setStroke(this.stokeWidth,selectedStokeColor);
            drawable.setColor(this.selectedbackGroundColor);
            drawable.setShape(GradientDrawable.RECTANGLE);
            return drawable;
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.orientation == HORIZONTAL) {
           measyreByHorizontal(widthMeasureSpec, heightMeasureSpec);
        } else {
           measureByVertical(widthMeasureSpec, heightMeasureSpec);
        }

    }

    protected  void measureByVertical(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.getChildCount() != 0) {
            int totalHeight =getPaddingTop() +getPaddingBottom();
            int totalWidth = 0;
            int childCount =getChildCount();
            int i = 0;

            for(int var7 = childCount; i < var7; ++i) {
                View child =getChildAt(i);
               measureChild(child, widthMeasureSpec, heightMeasureSpec);
                Intrinsics.checkExpressionValueIsNotNull(child, "child");
                totalWidth = Math.max(totalWidth, child.getMeasuredWidth());
                totalHeight += child.getMeasuredHeight() +itemInternal_vertical;
            }

           setMeasuredDimension(totalWidth +getPaddingLeft() +getPaddingRight(), totalHeight);
        }
    }

    protected  void measyreByHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.getChildCount() != 0) {
            int totalHeight =getPaddingTop();
            int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
            int width =getPaddingLeft();
            int childCount =getChildCount();
            int i = 0;

            for(int var8 = childCount; i < var8; ++i) {
                View child =getChildAt(i);
               measureChild(child, widthMeasureSpec, heightMeasureSpec);
                if (totalHeight ==getPaddingTop()) {
                    Intrinsics.checkExpressionValueIsNotNull(child, "child");
                    totalHeight = child.getMeasuredHeight() +getPaddingTop();
                }

                Intrinsics.checkExpressionValueIsNotNull(child, "child");
                if (width + child.getMeasuredWidth() +getPaddingRight() > totalWidth -getPaddingLeft()) {
                    totalHeight += child.getMeasuredHeight() +itemInternal_vertical;
                    width =getPaddingLeft() + child.getMeasuredWidth() +itemInternal_horizontal;
                } else {
                    width += child.getMeasuredWidth() +itemInternal_horizontal;
                }
            }

           setMeasuredDimension(totalWidth, totalHeight +getPaddingBottom());
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (this.layoutManager != null) {
            layoutManager.onLayout(changed, l, t, r, b);
        }

    }









}
