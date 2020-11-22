package com.bobo.imbykotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bobo.imbykotlin.R
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMTextMessageBody
import com.hyphenate.util.DateUtils
import kotlinx.android.synthetic.main.view_receive_message_item.view.*
import java.util.*

/**
 * Created by 公众号：IT波 on 2020/11/8 Copyright © Leon. All rights reserved.
 * Functions: 接收（别人发来的）消息的自定义控件
 */
class ReceiveMessageItemView(context: Context?, attrs: AttributeSet? = null) :
    RelativeLayout(context, attrs) {

    init {
        // 注意当前是在自定义view第三个参数要传this
        View.inflate(context, R.layout.view_receive_message_item, this)
    }

    fun bindView(emMessage: EMMessage, showTimestamp: Boolean) {
        // 更新时间戳
        updateTimestamp(emMessage, showTimestamp)

        // 更新消息
        updateMessage(emMessage)
    }

    // 更新消息
    private fun updateMessage(emMessage: EMMessage) {
        // 判断消息是否是文本类型
        if (emMessage.type == EMMessage.Type.TXT) {
            receiveMessage.text = (emMessage.body as EMTextMessageBody).message
        } else {
            receiveMessage.text = context.getString(R.string.no_text_message)
        }
    }

    // 设置显示发送消息的时间
    private fun updateTimestamp(emMessage: EMMessage, showTimestamp: Boolean) {
        if (showTimestamp) {
            timestamp.visibility = View.VISIBLE
            timestamp.text = DateUtils.getTimestampString(Date(emMessage.msgTime))
        } else {
            timestamp.visibility = View.GONE
        }
    }
}