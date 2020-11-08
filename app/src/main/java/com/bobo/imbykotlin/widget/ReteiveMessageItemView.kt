package com.bobo.imbykotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bobo.imbykotlin.R

/**
 * Created by 公众号：IT波 on 2020/11/8 Copyright © Leon. All rights reserved.
 * Functions: 接收（别人发来的）消息的自定义控件
 */
class ReteiveMessageItemView(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int) :
    RelativeLayout(context, attrs, defStyleAttr) {

    init {
        // 注意当前是在自定义view第三个参数要传this
        View.inflate(context, R.layout.view_receive_message_item, this)
    }
}