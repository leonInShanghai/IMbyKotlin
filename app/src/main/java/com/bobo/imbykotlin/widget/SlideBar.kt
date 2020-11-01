package com.bobo.imbykotlin.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.bobo.imbykotlin.R
import org.jetbrains.anko.sp
import android.view.View as View

/**
 * Created by 公众号：IT波 on 2020/10/26 Copyright © Leon. All rights reserved.
 * Functions: 自定义view 侧划条
 */
class SlideBar(context: Context?, attrs: AttributeSet? = null) : View(context, attrs) {

    var sectionsHeight = 0f;
    var paint = Paint()
    var textBaseline = 0f
    var onSectionChangeListener: OnSectionChangeListener? =null

    // 伴生对象
    companion object {
        private val SECTIONS = arrayOf("#","A","B","C","D","E","F","G","H","I","J","K",
            "L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")
    }

    // 初始化代码块
    init {

        // 设置paint的属性
        paint.apply {
            color = resources.getColor(R.color.qq_section_text_gray)

            // 设置画笔画文字的大小为12sp
            textSize = sp(12).toFloat()

            // 设置画笔的属性为对齐居中
            textAlign = Paint.Align.CENTER
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        // 计算每个字符分配的高度
        sectionsHeight = (h * 1.0 / SECTIONS.size).toFloat()

        val fontMetrics = paint.fontMetrics

        // 计算绘制文本的高度
        val textHeight = fontMetrics.descent - fontMetrics.ascent

        // 计算基准线
        textBaseline = sectionsHeight / 2 + (textHeight / 2 - fontMetrics.descent);
    }

    /**
     * 自定义控件重写绘制方法
     */
    override fun onDraw(canvas: Canvas) {

        // 绘制字符的起始位置
        val x = width / 2.0f;
        var baseLine = textBaseline

        // 绘制所有的字母A-Z
        SECTIONS.forEach {
            canvas.drawText(it, x, baseLine, paint)

            // 画完一个字母后更新y的位置
            baseLine +=  sectionsHeight
        }
    }

    /**
     * 处理触摸事件
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {

            // 当用户按下
            MotionEvent.ACTION_DOWN -> {

                // 设置背景为半透明
                setBackgroundResource(R.drawable.bg_slide_bar)

                // 找到用户点击的字母
                val index = getTouchIndex(event)
                val firstLetter = SECTIONS[index]
                Log.e("SlideBar", "firstLetter: $firstLetter")
                onSectionChangeListener?.onSectionChange(firstLetter)
            }

            // 当用户手指在SlideBar上划动
            MotionEvent.ACTION_MOVE -> {
                // 找到用户点击的字母
                val index = getTouchIndex(event)

                val firstLetter = SECTIONS[index]
                Log.e("SlideBar", "firstLetter: $firstLetter")
                onSectionChangeListener?.onSectionChange(firstLetter)
            }

            // 用户手松开颜色为透明
            MotionEvent.ACTION_UP -> {
                setBackgroundColor(Color.TRANSPARENT)
                onSectionChangeListener?.onSectionFinish()
            }
        }

        // 一定要返回true消费掉事件否则会继续往下传递
        return true
    }

    /**
     * 根据用户点击的位置获取对应的点击字母索引
     */
    private fun getTouchIndex(event: MotionEvent): Int {

        // 当前字母的索引等于当前的y值除以一个字母所占的高度
        var index = (event.y / sectionsHeight).toInt()

        // 数组越界的检查
        if (index < 0) {
            index = 0
        } else if (index >= SECTIONS.size) {
            index = SECTIONS.size - 1
        }

        return index
    }

    /**
     * 点击事件回调接口
     */
    interface OnSectionChangeListener {

        /**
         * 用户按下了某个字母
         */
        fun onSectionChange(firstLetter: String)

        /**
         * 划动结束的回调
         */
        fun onSectionFinish()
    }

}