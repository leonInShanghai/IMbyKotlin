package com.bobo.imbykotlin.adapter

import com.hyphenate.EMMessageListener
import com.hyphenate.chat.EMMessage

/**
 * Created by 公众号：IT波 on 2020/11/15 Copyright © Leon. All rights reserved.
 * Functions: 消息监听器的适配器不需要实现的方法可以不实现
 */
open class EMMessageListenerAdapter: EMMessageListener {

    override fun onMessageRecalled(p0: MutableList<EMMessage>?) {

    }

    override fun onMessageChanged(p0: EMMessage?, p1: Any?) {

    }

    override fun onCmdMessageReceived(p0: MutableList<EMMessage>?) {

    }

    override fun onMessageReceived(p0: MutableList<EMMessage>?) {

    }

    override fun onMessageDelivered(p0: MutableList<EMMessage>?) {

    }

    override fun onMessageRead(p0: MutableList<EMMessage>?) {

    }

}