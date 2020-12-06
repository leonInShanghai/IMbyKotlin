package com.bobo.imbykotlin.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.bobo.imbykotlin.R;
import com.bobo.imbykotlin.adapter.ConversationListAdapter
import com.bobo.imbykotlin.adapter.EMMessageListenerAdapter
import com.bobo.imbykotlin.adapter.MessageListAdapter
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by 公众号：IT波 on 2020/9/26 Copyright © Leon. All rights reserved.
 * Functions: 首页（会话页）Conversation交谈的意识
 */
class ConversationFragment: BaseFragment() {

    val conversations = mutableListOf<EMConversation>()

    override fun getLayoutResId(): Int = R.layout.fragment_conversation;

    val messageListener = object : EMMessageListenerAdapter() {

        // Received 收到的意识
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            super.onMessageReceived(p0)
            // 收到新消息重新加载列表
            loadConversations()
        }
    }

    override fun onResume() {
        super.onResume()
        // 重新加载数据
        loadConversations()

        Log.d("ConversationFragment", "onResume loadConversations()")
    }

    override fun init() {
        super.init()
        headerTitle.text = getString(R.string.message)

        // əˈplaɪ 使用
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = ConversationListAdapter(context, conversations)
        }

        EMClient.getInstance().chatManager().addMessageListener(messageListener)

        // onResume已经调用不用重复调用
        // loadConversations()
    }

    private fun loadConversations() {
        doAsync {
            // 加载列表前先清空老的数据
            conversations.clear()
            val allConversations = EMClient.getInstance().chatManager().allConversations
            conversations.addAll(allConversations.values)
            uiThread {
                // 切换到UI线程刷新UI
                recyclerView.adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        // 不用的时候移除消息监听器
        EMClient.getInstance().chatManager().removeMessageListener(messageListener)
        super.onDestroy()
    }
}