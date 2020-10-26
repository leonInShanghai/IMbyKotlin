package com.bobo.imbykotlin.presenter

import android.util.Log
import com.bobo.imbykotlin.contract.ContactContract
import com.bobo.imbykotlin.data.ContactListItem
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import org.jetbrains.anko.doAsync

/**
 * Created by 公众号：IT波 on 2020/10/26 Copyright © Leon. All rights reserved.
 * Functions:
 */
class ContactPresenter(val view : ContactContract.View) : ContactContract.Presenter {

    // 联系人对象集合
    val contactListItems = mutableListOf<ContactListItem>();

    /**
     * 加载联系人列表
     */
    override fun loadContacts() {

        // 在子线程做网络请求
        doAsync {

            // 再次加载数据，先清空集合
            contactListItems.clear()

            try {
                val usernames = EMClient.getInstance().contactManager().allContactsFromServer

                // 首字母按照26个字母顺序排序
                usernames.sortBy { it[0] }

                usernames.forEachIndexed { index, s ->
                    // 判断是否显示首字符
                    val showFirstLetter = index == 0 || s[0] != usernames[index -1][0]
                    val contactListItem = ContactListItem(s, s[0].toUpperCase(), showFirstLetter)
                    contactListItems.add(contactListItem)
                }

                // 没有异常就是请求联系人成功 到主线程通知View层
                uiThread {
                    view.onLoadContactsSuccess()
                    Log.d("ContactPresenter", "加载联系人成功" + usernames.size)
                }

            } catch (e: HyphenateException) {
                // 有异常就是失败 到主线程通知View层
                uiThread {
                    view.onLoadContactsFailed()
                }
                Log.e("ContactPresenter", "加载联系人失败：$e")
            }
        }
    }
}