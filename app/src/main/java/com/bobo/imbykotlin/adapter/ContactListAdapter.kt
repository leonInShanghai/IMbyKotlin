package com.bobo.imbykotlin.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.bobo.imbykotlin.data.ContactListItem
import com.bobo.imbykotlin.ui.activity.ChatActivity
import com.bobo.imbykotlin.widget.ContactListItemView
import org.jetbrains.anko.startActivity
import com.bobo.imbykotlin.R
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast
import kotlin.math.log

/**
 * Created by 公众号：IT波 on 2020/10/26 Copyright © Leon. All rights reserved.
 * Functions: 联系人类别recyclerview的适配器
 */
class ContactListAdapter(val context: Context, val contactListItems1: MutableList<ContactListItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 加这个变量是为了刷新都在主线程进行 之前那样子线程操作数据源主线程刷新会数组越界
    private var mContactListItems = mutableListOf<ContactListItem>()


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        mContactListItems.addAll(contactListItems1)
        return ContactListItemViewHoder(ContactListItemView(context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val contactListItemView = holder!!.itemView as ContactListItemView
        contactListItemView.bindView(mContactListItems[position])

        // 当前position的用户名
        val userName = mContactListItems[position].userName
        // recyclerview条目点击事件的处理
        contactListItemView.setOnClickListener {
            // 跳转到聊天activity
            context.startActivity<ChatActivity>("username" to userName)
        }

        // recyclerview条目长按事件的处理
        contactListItemView.setOnLongClickListener {

            // 拼接字符串 是否要删除xxx?
            val message = String.format(context.getString(R.string.delete_frend_menssage), userName)

            // 长按（弹出对话框）删除联系人
            AlertDialog.Builder(context)
                .setTitle(R.string.delete_frend_title)
                .setMessage(message)
                .setNegativeButton(R.string.cancle, null)
                .setPositiveButton(R.string.confirm, DialogInterface.OnClickListener { dialogInterface, i ->
                    // 当用户点击确定开始删除好友的操作
                    deleteFriend(userName)
                }).show()

            // 返回true表示用户的这次点击事件我管了
            true
        }
    }

    // 删除好友
    private fun deleteFriend(userName : String) {

        // 通知环信服务器要删除好友
        EMClient.getInstance().contactManager().aysncDeleteContact(userName, object :
            EMCallBackAdapter(){

            override fun onSuccess() {
                // 删除成功切换到主线程通知用户
                context.runOnUiThread {
                    toast(R.string.delete_friend_successs)
                }
            }

            override fun onError(p0: Int, p1: String?) {
                // 删除失败切换到主线程通知用户
                context.runOnUiThread {
                    toast(R.string.delete_friend_failed)
                }
            }
        })
    }


    // 老的写法有数组越界的风险
    // override fun getItemCount(): Int = contactListItems.size

    // 新写法 RecyclerView: No adapter attached; skipping layout
    override fun getItemCount(): Int {
        // Inconsistency detected. Invalid view holder adapter positionViewHolder{61caefa
        // position=8 id=-1, oldPos=-1, pLpos:-1 no parent}
        if (mContactListItems == null) {
            return 0
        }

        return mContactListItems.size
    }

    class ContactListItemViewHoder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    }

    /**
     * 2021-1-1 增加刷新方法
     */
    open fun updateList(contactListItems : MutableList<ContactListItem>) {
        if (contactListItems != null) {
            mContactListItems.clear()
            mContactListItems.addAll(contactListItems)
            notifyDataSetChanged()
            Log.d("ContactListAdapter", "notifyDataSetChanged")
        }
    }

}

