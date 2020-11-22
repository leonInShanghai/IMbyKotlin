package com.bobo.imbykotlin.presenter

import com.bobo.imbykotlin.adapter.EMCallBackAdapter
import com.bobo.imbykotlin.contract.ChatContract
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by 公众号：IT波 on 2020/11/15 Copyright © Leon. All rights reserved.
 * Functions: 聊天界面的P层
 */
class ChatPresenter(val view: ChatContract.View) : ChatContract.Presenter {

    companion object {
        // 常量下拉加载更多聊天数据的个数
        val PAGE_SIZE = 6 // 10
    }

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

    /**
     * 加载历史聊天消息
     */
    override fun loadMessages(username: String) {
        doAsync {
            val conversation = EMClient.getInstance().chatManager().getConversation(username)
            // 将加载的消息标记为已读  mark:标记
            conversation.markAllMessagesAsRead()
            messages.addAll(conversation.allMessages)

            uiThread {
                view.onMessageLoaded()
            }
        }
    }

    /**
     * 加载更多历史聊天数据
     */
    override fun loadMoreMessages(username: String) {
        doAsync {
            val conversation = EMClient.getInstance().chatManager().getConversation(username)
            val startMsgId = messages[0].msgId
            // 从环信的数据库加载更多历史聊天记录
            val loadMoreMsgFromDB = conversation.loadMoreMsgFromDB(startMsgId, PAGE_SIZE)
            // 注意这里要加到集合的前面 第一个参数要传0
            messages.addAll(0, loadMoreMsgFromDB)
            uiThread {
                view.onMoreMessageLoaded(loadMoreMsgFromDB.size)
            }
        }
    }

}