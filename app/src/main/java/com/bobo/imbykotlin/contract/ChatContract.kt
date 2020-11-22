package com.bobo.imbykotlin.contract

import com.hyphenate.chat.EMMessage

/**
 * Created by 公众号：IT波 on 2020/11/15 Copyright © Leon. All rights reserved.
 * Functions: 聊天的MVP协议
 */
interface ChatContract {

    interface Presenter : BasePresenter {

        /**
         * 发送一条消息
         */
        fun sendMessage(contact: String, message: String)

        /**
         * 收到消息 一条或多条
         */
        fun addMessage(username: String, p0: MutableList<EMMessage>?)

        /**
         * 加载历史聊天消息
         */
        fun loadMessages(username: String)

        /**
         * 加载更多历史聊天数据
         */
        fun loadMoreMessages(username: String)
    }

    interface View {

        /**
         * 告诉view层开始发送一条消息
         */
        fun onStartSendMessage()

        /**
         * 发送消息成功
         */
        fun onSendMessageSuccess()

        /**
         * 发送消息失败
         */
        fun onSendMessageFailed()

        /**
         * 加载历史聊天消息完成
         */
        fun onMessageLoaded()

        /**
         * 加载更多历史聊天数据成功
         */
        fun onMoreMessageLoaded(size: Int)
    }
}