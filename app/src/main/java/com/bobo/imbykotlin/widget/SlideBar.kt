package com.bobo.imbykotlin.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
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
}