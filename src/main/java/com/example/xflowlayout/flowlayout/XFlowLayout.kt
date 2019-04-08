package com.example.xflowlayout.flowlayout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import com.example.administrator.test.flowlayout.FlowLayout
import com.example.administrator.test.flowlayout.OnItemClickListener
import com.example.administrator.test.flowlayout.OnItemLongPressListener
import com.example.xflowlayout.R
import java.util.*
import kotlin.collections.ArrayList
/**
 * Created by alan on  2019/3/19 15:09
 */
@SuppressLint("NewApi")
class XFlowLayout : FlowLayout {
    //所有的text
    var textLsit = arrayListOf<String>()
        private set
    //textView
    var textViews = arrayListOf<TextView>()
        private set
    //选中的textView
    var selectedTextViews: ArrayList<TextView>? = null
        private set
    //未选中的textView
    var unSelectedTextViews: ArrayList<TextView>? = null
        private set
    //控件单击监听
    private var onItemClickListener: OnItemClickListener? = null
    //控件长按监听
    private var onItemLongPressListener: OnItemLongPressListener? = null
    //最多可选数
    private var enableMaxSelect = false
    //随机文字颜色
    private var isRandomTextColor = false


    constructor(context: Context) : super(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    fun setTexts(texts: Array<String>): XFlowLayout {
        val list = ArrayList<String>()
        texts.forEach { s -> list.add(s) }
        setTexts(list)
        return this
    }

    fun setTexts(texts: ArrayList<String>): XFlowLayout {
        textLsit.clear()
        textViews.clear()
        selectedTexts?.clear()
        removeAllViews()

        for (text in texts) {
            textLsit.add(text)

            var textView: TextView?

            if (isSingleSelect || isMoreSelect) textView = TextView(context)
            else textView = LayoutInflater.from(context).inflate(R.layout.textview_layout, this, false) as TextView
            textViews.add(textView!!)

            textView.setText(text)
            textView.gravity = Gravity.CENTER
            textView.setTextColor(defaultTextColor)
            textView.setPadding(itemContentPadding + 5, itemContentPadding, itemContentPadding + 5, itemContentPadding + 7)

            if(drawableId>0){
                textView.setBackgroundResource(drawableId)
            }else{
                textView.background = defaultDrawable()
            }

            if (isRandomTextColor) textView.setTextColor(Color.parseColor(getRandColor()))

            textView.setTextSize(textSize)
            textView.setOnClickListener {
                if (isSingleSelect) setSingleSelected(text, textView);
                if (isMoreSelect) moreSelect(text, textView)
                onItemClickListener?.onClick(text, textView)

            }
            textView.setOnLongClickListener {
                onItemLongPressListener?.onLongClick(text, textView)
                true
            }


            addView(textView)
        }
        return this
    }

    //单选内部操作
    private fun setSingleSelected(text: String, textView: TextView): XFlowLayout {
        textViews.forEach { t: TextView? ->
            t?.setTextColor(defaultTextColor)
            t?.background = defaultDrawable()
        }
        textView.setTextColor(slectedTextColor)
        textView.background = selectedDrawable()
        singleTeSelectedText = text
        return this
    }

    //item背景Drawable
    fun setItemBackGroudDrawable(drawableId: Int): XFlowLayout {
        if(drawableId>0)this.drawableId=drawableId

        return this
    }

    //itemm默认背景颜色
    fun setItemBackGroundColor(color: Int): XFlowLayout {
        defaultBackGroundColor = color
        return this
    }

    //item被选中时背景颜色
    fun setItemSelectedBackGroundColor(color: Int): XFlowLayout {
        selectedbackGroundColor = color
        return this
    }

    //设置item圆角值
    fun setItemRadius(radius: Float): XFlowLayout {
        this.radius = radius
        return this
    }

    //设置单选
    fun setIsSingleSlect(isSingleSelect: Boolean): XFlowLayout {
        this.isSingleSelect = isSingleSelect
        if (isSingleSelect) isMoreSelect = false
        return this
    }

    //文字大小
    fun setTextSize(spValue: Float): XFlowLayout {
        textSize = spValue
        return this
    }

    //默认文字颜色
    fun setDefaultTextColor(color: Int): XFlowLayout {
        defaultTextColor = color
        return this
    }

    //选中文字颜色
    fun setSelectTextColor(selectedColor: Int): XFlowLayout {
        slectedTextColor = selectedColor

        return this
    }

    //圆角大小
    fun setRadiuSize(radiuSize: Float): XFlowLayout {
        radius = radiuSize

        return this
    }

    //默认边框颜色
    fun setDefaultStokeColor(defaultColor: Int): XFlowLayout {
        defaultStokeColor = defaultColor
        return this
    }

    //选中时边框颜色
    fun setSelectedStokeColor(selectedColor: Int): XFlowLayout {
        selectedStokeColor = selectedColor
        return this
    }

    //边框大小
    fun setStokeSize(stokeSize: Int): XFlowLayout {
        stokeWidth = stokeSize
        return this
    }

    //item水平方向的间隙
    fun setItemInternalHorizontal(pxValue: Int): XFlowLayout {
        itemInternal_horizontal = pxValue
        return this
    }

    //item垂直方向的间隙
    fun setItemInternalVertical(pxValue: Int): XFlowLayout {
        itemInternal_vertical = pxValue
        return this
    }

    //布局方向
    fun setOrirntation(ortentation: Int): XFlowLayout {
        this.orientation = ortentation
        return this
    }

    //单选text
    fun getSingleSelectedText(): String {

        return singleTeSelectedText
    }

    //多选text
    fun getMoreSelectedText(): ArrayList<String>? {

        return selectedTexts
    }

    //设置是否每个text的颜色随机选定
    fun setItemRandowColor(isRandomColor: Boolean): XFlowLayout {
        isSingleSelect = false;
        isMoreSelect = false
        isRandomTextColor = isRandomColor
        return this
    }

    //设置是否每个text的颜色随机选定
    fun setItemContentPadding(pxValue: Int): XFlowLayout {
        itemContentPadding = pxValue
        return this
    }

    //随机颜色
    fun getRandColor(): String {
        var R: String
        var G: String
        var B: String
        val random = Random()
        R = Integer.toHexString(random.nextInt(256)).toUpperCase()
        G = Integer.toHexString(random.nextInt(256)).toUpperCase()
        B = Integer.toHexString(random.nextInt(256)).toUpperCase()
        R = if (R.length == 1) "0$R" else R
        G = if (G.length == 1) "0$G" else G
        B = if (B.length == 1) "0$B" else B
        return "#" + R + G + B
    }

    //设置是否多选
    fun setIsMoreSelected(isMoreSlect: Boolean): XFlowLayout {
        isMoreSelect = isMoreSlect
        if (isMoreSelect) isSingleSelect = false
        return this
    }

    private fun moreSelect(text: String, textView: TextView) {
        if (selectedTextViews == null) selectedTextViews = ArrayList()
        if (unSelectedTextViews == null) unSelectedTextViews = arrayListOf()
        if (selectedTexts == null) selectedTexts = arrayListOf()
        if (!selectedTextViews!!.contains(textView)) {
            if (enableMaxSelect && selectedTextViews!!.size >= maxSelect) return
            selectedTextViews?.add(textView)
            selectedTexts?.add(text)
        } else {
            selectedTextViews?.remove(textView)
            selectedTexts?.remove(text)
            textView?.background = defaultDrawable()
            return
        }
        selectedTextViews?.forEach { t: TextView? ->
            t?.background = selectedDrawable()
        }
    }

    fun setOnItemClickListener(itemOnclickListener: OnItemClickListener): XFlowLayout {
        onItemClickListener = itemOnclickListener
        return this
    }

    fun setOnItemLongPressListener(itemLongPressListener: OnItemLongPressListener): XFlowLayout {
        onItemLongPressListener = itemLongPressListener
        return this
    }

    fun setMaxSelect(maxSize: Int): XFlowLayout {
        enableMaxSelect = true
        maxSelect = maxSize
        return this
    }

    companion object {
        @JvmField
        val HORIZONTAL = 0   //水平布局方向
        @JvmField
        val VERTICAL = 1    //垂直布局方向
    }

}