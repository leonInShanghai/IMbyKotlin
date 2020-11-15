package com.bobo.imbykotlin.presenter

import com.bobo.imbykotlin.adapter.EMCallBackAdapter
import com.bobo.imbykotlin.contract.ChatContract
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage

/**
 * Created by 公众号：IT波 on 2020/11/15 Copyright © Leon. All rights reserved.
 * Functions: 聊天界面的P层
 */
class ChatPresenter(val view: ChatContract.View) : ChatContract.Presenter {

    val messages = mutableListOf<EMMessage>()

    /**
     * 发送消息的业务
     */
    override fun sendMessage(contact: String, message: String) {
        // 1.创建一条消息 第一个参数传消息内容，第二个是联系人
        val emMessage = EMMessage.createTxtSendMessage(message, contact)

        // 设置消息监听 知道消息是发送成功还是发送失败
        emMessage.setMessageStatusCallback(object : EMCallBackAdapter() {

            // 消息发送成功
            override fun onSuccess() {
                uiThread {
                    view.onSendMessageSuccess()
                }
            }

            // 消息发送失败
            override fun onError(p0: Int, p1: String?) {
                uiThread {
                    view.onSendMessageFailed()
                }
            }
        })

        // 将消息添加到集合当中
        messages.add(emMessage)

        // 通知view开始发送消息了
        view.onStartSendMessage()

        // 发送消息
        EMClient.getInstance().chatManager().sendMessage(emMessage)
    }

    /**
     * 接收到好友发来消息
     */
    override fun addMessage(username: String, p0: MutableList<EMMessage>?) {
        // 加入到当前的消息列表
        p0?.let { messages.addAll(it) }

        // 更新消息为已读: 先获取跟联系人的会话，然后标记会话里面的消息为全部已读
        val conversation = EMClient.getInstance().chatManager().getConversation(username)
        conversation.markAllMessagesAsRead()
    }

}