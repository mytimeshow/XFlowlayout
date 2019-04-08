package com.example.administrator.test.flowlayout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.ViewGroup
import com.example.xflowlayout.R
import com.example.xflowlayout.flowlayout.LayoutManager
/**
 * Created by alan on  2019/3/19 15:09
 */
@SuppressLint("NewApi")
open class FlowLayout : ViewGroup {

    //控件水平方向的间隙
    internal var itemInternal_horizontal = 20
    //控件垂直方向的间隙
    internal var itemInternal_vertical = 20
    //item内间距
    protected var itemContentPadding = 0
    //布局方向
    internal var orientation = HORIZONTAL
    //item默认背景颜色
    protected var defaultBackGroundColor: Int = Color.WHITE
    //item 选中时背景颜色
    protected var selectedbackGroundColor: Int = Color.WHITE
    //控件圆角值
    protected var radius = 3.0f
    //选中文字颜色
    protected var slectedTextColor = Color.BLACK
    //默认文字颜色
    protected var defaultTextColor = Color.parseColor("#FF66FF")
    //布局管理者
    protected var layoutManager: LayoutManager? = null
    //屏幕的宽度
    protected var screenWidth = 0
    // 单选时选中的text
    protected var singleTeSelectedText = ""
    //多选时选中的值
    protected var selectedTexts: ArrayList<String>? = null
    //是否单选
    protected var isSingleSelect = false
    //是否多选
    protected var isMoreSelect = false
    //文字大小
    protected var textSize = 15f
    //默认边框颜色
    protected var defaultStokeColor = Color.parseColor("#CCCCCC")
    //选中时边框颜色
    protected var selectedStokeColor = Color.RED
    //最大选中数
    protected var maxSelect = 3
    //边框大小
    protected var stokeWidth = 1
    protected var drawableId=0

    constructor(context: Context) : super(context, null) {

    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {
        parseAttrs(context, attrs)
        layoutManager = LayoutManager(this)
        screenWidth = context.resources.displayMetrics.widthPixels
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    private fun parseAttrs(context: Context, attrs: AttributeSet?) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout)
        orientation = typeArray.getInt(R.styleable.FlowLayout_orientation, HORIZONTAL)
        itemInternal_horizontal = typeArray.getInt(R.styleable.FlowLayout_itemInternal_horizontal, 20)
        itemInternal_vertical = typeArray.getInt(R.styleable.FlowLayout_itemInternal_vertical, 20)
        radius = typeArray.getFloat(R.styleable.FlowLayout_item_radius, resources.getDimension(R.dimen.dp_4))
        isSingleSelect = typeArray.getBoolean(R.styleable.FlowLayout_singgle_select, false)
        isMoreSelect = typeArray.getBoolean(R.styleable.FlowLayout_moreselect_select, false)
        maxSelect = typeArray.getInt(R.styleable.FlowLayout_max_select, 3)
        textSize = typeArray.getFloat(R.styleable.FlowLayout_text_size,15f)
        itemContentPadding = typeArray.getInt(R.styleable.FlowLayout_item_content_internal, resources.getDimension(R.dimen.dp_6).toInt())
        defaultTextColor = typeArray.getColor(R.styleable.FlowLayout_default_textColor, Color.BLACK)
        slectedTextColor = typeArray.getColor(R.styleable.FlowLayout_selected_textColor, Color.BLACK)
        defaultBackGroundColor = typeArray.getColor(R.styleable.FlowLayout_item_default_backGroundDrawable, Color.WHITE)
        selectedbackGroundColor = typeArray.getColor(R.styleable.FlowLayout_item_select_backGroundDrawable, Color.WHITE)
        stokeWidth = typeArray.getInt(R.styleable.FlowLayout_stoke_width, 1)
        defaultStokeColor = typeArray.getColor(R.styleable.FlowLayout_default_stoke_color, Color.parseColor("#CCCCCC"))
        selectedStokeColor = typeArray.getColor(R.styleable.FlowLayout_selected_stoke_color, Color.RED)
        typeArray.recycle()


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (orientation == HORIZONTAL) measyreByHorizontal(widthMeasureSpec, heightMeasureSpec)
        else measureByVertical(widthMeasureSpec, heightMeasureSpec)
    }

    //默认的Drawable
    fun defaultDrawable(): GradientDrawable? {
        var drawable: GradientDrawable = context.getDrawable(R.drawable.bg_default) as GradientDrawable
        drawable.cornerRadius = radius
        drawable.setColor(defaultBackGroundColor)
        drawable.setStroke(stokeWidth, defaultStokeColor)
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }

    //选中时的Drawable
    fun selectedDrawable(): GradientDrawable? {
        var drawable: GradientDrawable = context.getDrawable(R.drawable.bg_red) as GradientDrawable
        drawable.cornerRadius = radius
        drawable.setStroke(stokeWidth, selectedStokeColor)
        drawable.setColor(selectedbackGroundColor)
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }

    //垂直布局方向的测量
    private fun measureByVertical(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (childCount == 0) return
        var totalHeight = paddingTop + paddingBottom
        var totalWidth = 0
        val childCount = getChildCount()
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            totalWidth = Math.max(totalWidth, child.measuredWidth)
            totalHeight += child.measuredHeight + itemInternal_vertical
        }
        setMeasuredDimension(totalWidth + paddingLeft + paddingRight, totalHeight)
    }

    //水平布局方向的测量
    private fun measyreByHorizontal(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (childCount == 0) return
        var totalHeight = paddingTop
        var totalWidth = MeasureSpec.getSize(widthMeasureSpec)
        var width = paddingLeft
        val childCount = getChildCount()
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            if (totalHeight == paddingTop) totalHeight = child.measuredHeight + paddingTop
            if (width + child.measuredWidth + paddingRight > totalWidth - paddingLeft) {
                totalHeight += child.measuredHeight + itemInternal_vertical
                width = paddingLeft + child.measuredWidth + itemInternal_horizontal
            } else {
                width += child.measuredWidth + itemInternal_horizontal
            }
        }
        setMeasuredDimension(totalWidth, totalHeight + paddingBottom)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        layoutManager?.onLayout(changed, l, t, r, b)
    }

    companion object {
        @JvmField
        val HORIZONTAL = 0   //水平布局方向
        @JvmField
        val VERTICAL = 1    //垂直布局方向
    }


}