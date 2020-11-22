package com.bobo.imbykotlin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bobo.imbykotlin.widget.ReceiveMessageItemView
import com.bobo.imbykotlin.widget.SendMessageItemView
import com.hyphenate.chat.EMMessage
import com.hyphenate.util.DateUtils

/**
 * Created by 公众号：IT波 on 2020/11/15 Copyright © Leon. All rights reserved.
 * Functions: 消息列表的循环视图适配器
 */
class MessageListAdapter(val context: Context, val messages: List<EMMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        // 定义两种条目类型 发送和接受
        val ITEM_TYPE_SEND_MESSAGE = 0
        val ITEM_TYPE_RECEIVE_MESSAGE = 1
    }

    override fun getItemViewType(position: Int): Int {
        if (messages[position].direct() == EMMessage.Direct.SEND) {
            // 发送的消息
            return ITEM_TYPE_SEND_MESSAGE
        } else {
            // 接收的消息
            return ITEM_TYPE_RECEIVE_MESSAGE
        }
    }

    // 根据不同的条目类型绑定不同的信息
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        // 是否显示时间戳（聊天时间）
        val showTimestamp = isShowTimestamp(position)
        if (getItemViewType(position) == ITEM_TYPE_SEND_MESSAGE) {
            val sendMessageItemView = holder?.itemView as SendMessageItemView
            sendMessageItemView.bindView(messages[position], showTimestamp)
        } else {
            val receiveMessageItemView = holder?.itemView as ReceiveMessageItemView
            receiveMessageItemView.bindView(messages[position], showTimestamp)
        }
    }

    // 是否显示聊天时间
    private fun isShowTimestamp(position: Int): Boolean {
        // 如果是第一条消息或者和前一条间隔时间比较长
        var showTimestamp = true
        if (position > 0){
            // 环信提供工具类对比两条消息时间的间隔
            showTimestamp = !DateUtils.isCloseEnough(messages[position].msgTime,
                messages[position - 1].msgTime)
        }
        return showTimestamp
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        // 根据不同的ViewType 创建不同的ViewHolder
        if (viewType == ITEM_TYPE_SEND_MESSAGE) {
            // 发送消息的item
            return SendMessageViewHolder(SendMessageItemView(context))
        } else {
            return ReceiveMessageViewHolder(ReceiveMessageItemView(context))
        }
    }

    override fun getItemCount(): Int = messages.size

    // 发送消息的view holder
    class SendMessageViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    }

    // 接收消息的view holder
    class ReceiveMessageViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    }
}