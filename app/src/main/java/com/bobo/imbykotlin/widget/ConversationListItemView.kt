package com.bobo.imbykotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bobo.imbykotlin.R
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMTextMessageBody
import com.hyphenate.util.DateUtils
import kotlinx.android.synthetic.main.view_conversation_item.view.*
import java.util.*

/**
 * Created by 公众号：IT波 on 2020/11/22 Copyright © Leon. All rights reserved.
 * Functions:
 */
class ConversationListItemView(context: Context?, attrs: AttributeSet? = null) : RelativeLayout(context,
    attrs) {

    init {
        // 这里是自定义view第二个参数要传this 其他情况传null
        View.inflate(context, R.layout.view_conversation_item, this)
    }

    /**
     * 绑定数据显示到view
     */
    fun bindView(emConversation: EMConversation) {
        // 用户名
        userName.text = emConversation.conversationId()

        // 用户的最后（新）一条消息
        if (emConversation.lastMessage.type == EMMessage.Type.TXT) {
            // 如果是文本类型的消息
            val body = emConversation.lastMessage.body as EMTextMessageBody
            lastMessage.text = body.message
        } else {
            // 如果是非文本类型的消息
            lastMessage.text = context.getString(R.string.no_text_message)
        }

        val timestampString = DateUtils.getTimestampString(Date(emConversation.lastMessage.msgTime))
        // 最后一条消息发来的时间
        timestamp.text = timestampString

        // （小红点）未读消息的个数
        if (emConversation.unreadMsgCount > 0) {
            // 有未读消息
            unreadCount.visibility = View.VISIBLE
            unreadCount.text = emConversation.unreadMsgCount.toString()
        } else {
            // 没有未读的消息 （不显示）
            unreadCount.visibility = View.GONE
        }
    }
}