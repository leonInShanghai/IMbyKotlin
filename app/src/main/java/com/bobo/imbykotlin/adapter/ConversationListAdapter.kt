package com.bobo.imbykotlin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bobo.imbykotlin.ui.activity.ChatActivity
import com.bobo.imbykotlin.widget.ConversationListItemView
import com.hyphenate.chat.EMConversation
import org.jetbrains.anko.startActivity

/**
 * Created by 公众号：IT波 on 2020/11/22 Copyright © Leon. All rights reserved.
 * Functions:
 */
class ConversationListAdapter(val context: Context, val conversations: MutableList<EMConversation>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val conversationListItemView = holder?.itemView as ConversationListItemView
        conversationListItemView.bindView(conversations[position])
        conversationListItemView.setOnClickListener {
            context.startActivity<ChatActivity>("username" to conversations[position].conversationId())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return  ConversationListItemViewHolder(ConversationListItemView(context))
    }

    override fun getItemCount(): Int = conversations.size

    class ConversationListItemViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {

    }
}