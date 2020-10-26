package com.bobo.imbykotlin.adapter

import com.hyphenate.EMConferenceListener
import com.hyphenate.EMContactListener
import com.hyphenate.chat.EMConferenceManager
import com.hyphenate.chat.EMConferenceMember
import com.hyphenate.chat.EMConferenceStream
import com.hyphenate.chat.EMStreamStatistics

/**
 * Created by 公众号：IT波 on 2020/10/26 Copyright © Leon. All rights reserved.
 * Functions: 适配器的作用：一些用不到的方法可以不实现
 */
open class EMContactListenerAdapter : EMContactListener {

    override fun onContactInvited(p0: String?, p1: String?) {

    }

    override fun onContactDeleted(p0: String?) {

    }

    override fun onFriendRequestAccepted(p0: String?) {

    }

    override fun onContactAdded(p0: String?) {

    }

    override fun onFriendRequestDeclined(p0: String?) {

    }
}