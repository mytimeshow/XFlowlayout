package com.example.xflowlayout.flowlayout

import com.example.administrator.test.flowlayout.FlowLayout
/**
 * Created by alan on  2019/3/19 15:09
 */
class LayoutManager(flowLayout: FlowLayout) {
    val flowLayout = flowLayout


    fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (flowLayout.orientation == FlowLayout.VERTICAL) layoutVertical()
        if (flowLayout.orientation == FlowLayout.HORIZONTAL) layoutHrizontal()

    }

    //水平布局方向
    private fun layoutHrizontal() {
        var top = flowLayout.paddingTop
        var left = 0
        val childCount = flowLayout.childCount
        for (i in 0 until childCount) {
            val child = flowLayout.getChildAt(i)
            if (left + child.measuredWidth > (flowLayout.measuredWidth - flowLayout.paddingLeft)) {
                left = flowLayout.paddingLeft
                top += child.measuredHeight + flowLayout.itemInternal_vertical
            }
            if (i == 0) left = flowLayout.paddingLeft
            child.layout(left, top, child.measuredWidth + left, child.measuredHeight + top)
            left += child.measuredWidth + flowLayout.itemInternal_horizontal
        }
    }

    //垂直方向布局
    private fun layoutVertical() {
        var top = flowLayout.paddingTop
        val childCount = flowLayout.childCount
        for (i in 0 until childCount) {
            val child = flowLayout.getChildAt(i)
            child.layout(flowLayout.paddingLeft, top, child.measuredWidth + flowLayout.paddingLeft, child.measuredHeight + top)
            top += child.measuredHeight + flowLayout.itemInternal_vertical

        }
    }


}