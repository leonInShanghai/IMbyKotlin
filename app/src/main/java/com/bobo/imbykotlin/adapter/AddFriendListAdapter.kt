package com.bobo.imbykotlin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bobo.imbykotlin.data.AddFriendItem
import com.bobo.imbykotlin.widget.AddFriendListitemView

/**
 * Created by 公众号：IT波 on 2020/11/1 Copyright © Leon. All rights reserved.
 * Functions: 添加好友recyclerView的适配器
 */
class AddFriendListAdapter(val context: Context, val addFriendItems: MutableList<AddFriendItem>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val addFriendListitemView = holder?.itemView as AddFriendListitemView
        addFriendListitemView.bindView(addFriendItems[position])
    }

    override fun getItemCount(): Int = addFriendItems.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return AddFriendListItemViewHolder(AddFriendListitemView(context))
    }

    class AddFriendListItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    }

}