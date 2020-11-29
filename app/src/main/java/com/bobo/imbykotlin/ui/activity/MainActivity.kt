package com.bobo.imbykotlin.ui.activity

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.util.Log
import com.bobo.imbykotlin.R
import com.bobo.imbykotlin.adapter.EMMessageListenerAdapter
import com.bobo.imbykotlin.app.IMApplication
import com.bobo.imbykotlin.factory.FragmentFactory
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Created by 公众号：IT波 on 2020/9/12 Copyright © Leon. All rights reserved.
 * Functions: 这是应用程序的首页
 */
class MainActivity : BaseActivity() {

    // 定义消息接收器
    val messageListener = object : EMMessageListenerAdapter() {
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            // 先切换到主线程
            runOnUiThread {
                // 更新未读消息个数
                updateBottomBarUnReadCount()
            }
        }
    }

    override fun getLayoutResId(): Int = R.layout.activity_main;

    override fun init() {
        super.init()

        // 底部tabbar的点击事件
        bottomBar.setOnTabSelectListener { tabId ->
            // 根据tabId 获取对应的fragment（可能为null）
            val fragment = FragmentFactory.instance.getFragment(tabId)
            // 要判断不为空才替换
            if (fragment != null) {
                val beginTransaction = supportFragmentManager.beginTransaction()
                beginTransaction.replace(R.id.fragment_frame,fragment)
                beginTransaction.commit()
            }

            // 添加消息监听器
            EMClient.getInstance().chatManager().addMessageListener(messageListener)
        }
    }

    override fun onResume() {
        super.onResume()

        // 更新未读消息个数
        updateBottomBarUnReadCount()
    }

    /**
     * 更新未读消息个数
     */
    private fun updateBottomBarUnReadCount() {
        // 初始化bottomBar上未读消息的个数
        val tab = bottomBar.getTabWithId(R.id.tab_conversation)
        // 设置显示未读信息的个数
        tab.setBadgeCount(EMClient.getInstance().chatManager().unreadMessageCount)
    }

    override fun onDestroy() {
        // 移除消息监听器
        EMClient.getInstance().chatManager().removeMessageListener(messageListener)
        super.onDestroy()
    }
}