package com.bobo.imbykotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bobo.imbykotlin.R
import com.bobo.imbykotlin.data.AddFriendItem
import kotlinx.android.synthetic.main.view_add_friend_item.view.*


/**
 * Created by 公众号：IT波 on 2020/11/1 Copyright © Leon. All rights reserved.
 * Functions: 添加好友列表item自定义布局 自定义布局一般选择带两个参数的构造方法
 */
class AddFriendListitemView(context: Context?, attrs: AttributeSet? = null) :
    RelativeLayout(context, attrs) {

    init {
        // 自定义布局最后一个参数传this 否则传null
        View.inflate(context, R.layout.view_add_friend_item, this)
    }

    fun bindView(addFriendItem: AddFriendItem) {
        userName.text = addFriendItem.userName
        timestamp.text = addFriendItem.timestamp
        if (addFriendItem.isAdded){
            add.text = context.getString(R.string.added)
        } else {
            add.text = context.getString(R.string.add)
        }
    }
}