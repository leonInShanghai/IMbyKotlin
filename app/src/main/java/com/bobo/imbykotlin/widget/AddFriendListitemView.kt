package com.bobo.imbykotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bobo.imbykotlin.R
import com.bobo.imbykotlin.adapter.EMCallBackAdapter
import com.bobo.imbykotlin.data.AddFriendItem
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import kotlinx.android.synthetic.main.view_add_friend_item.view.*
import kotlinx.android.synthetic.main.view_add_friend_item.view.add
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast


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
        if (addFriendItem.isAdded){
            add.isEnabled = false
            add.text = context.getString(R.string.added)
        } else {
            add.isEnabled = true
            add.text = context.getString(R.string.add)
        }
        userName.text = addFriendItem.userName
        timestamp.text = addFriendItem.timestamp

        // 当用户点击了“添加”按钮
        add.setOnClickListener{
            // 添加好友
            addFriend(addFriendItem.userName)
        }
    }

    /**
     * 开始添加好友的业务
     */
    private fun addFriend(userName: String) {
        // 添加用户的原因可以为null
        EMClient.getInstance().contactManager().aysncAddContact(userName, null,
            object : EMCallBackAdapter() {
                override fun onSuccess() {
                    context.runOnUiThread {
                        // toast提示用户发送好友请求成功
                        toast(R.string.send_add_friend_success)
                    }
                }

                override fun onError(p0: Int, p1: String?) {
                    context.runOnUiThread {
                        // toast提示用户发送好友请求失败
                        toast(R.string.send_add_friend_failed)
                    }
                }
        })
    }
}