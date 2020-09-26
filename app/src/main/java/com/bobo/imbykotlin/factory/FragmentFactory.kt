package com.bobo.imbykotlin.factory

import android.support.v4.app.Fragment
import  com.bobo.imbykotlin.R
import com.bobo.imbykotlin.ui.fragment.ContactFragment
import com.bobo.imbykotlin.ui.fragment.ConversationFragment
import com.bobo.imbykotlin.ui.fragment.DynamicFragment

/**
 * Created by 公众号：IT波 on 2020/9/26 Copyright © Leon. All rights reserved.
 * Functions: 生产fragment的工厂 单例类
 */
class FragmentFactory private constructor(){

    // 懒加载实例化会话fragment
    val conversation by lazy { ConversationFragment() }

    // 懒加载实例化联系人fragment
    val contact by lazy { ContactFragment() }

    // 懒加载实例化动态（设置）页面
    val dynamic by lazy { DynamicFragment() }

    // 伴生对象保持工厂的实例
    companion object {
        val instance = FragmentFactory()
    }

    /**
     * 根据不同的tabId 返回对应的fragment
     */
    fun getFragment(tabId: Int): Fragment? {
        when (tabId) {

            // 如果是会话界面
            R.id.tab_conversation -> return conversation

            // 如果是联系人页面
            R.id.tab_contacts -> return contact

            // 如果是动态（设置）页面
            R.id.tab_dynamic -> return dynamic
        }

        // 其他情况返回null
        return null
    }
}