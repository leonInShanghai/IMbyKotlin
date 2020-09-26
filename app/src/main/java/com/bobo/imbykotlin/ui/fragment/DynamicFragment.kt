package com.bobo.imbykotlin.ui.fragment

import android.content.Context
import android.content.Intent
import android.support.annotation.UiThread
import android.widget.Toast
import com.bobo.imbykotlin.R;
import com.bobo.imbykotlin.adapter.EMCallBackAdapter
import com.bobo.imbykotlin.ui.activity.LoginActivity
import com.bobo.imbykotlin.utils.CacheUtils
import com.hyphenate.chat.EMClient
import kotlinx.android.synthetic.main.fragment_dynamic.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast

/**
 * Created by 公众号：IT波 on 2020/9/26 Copyright © Leon. All rights reserved.
 * Functions: 设置页面  Dynamic动添的意识 由于没有动添icon 设置iocn代替
 */
class DynamicFragment: BaseFragment() {

    override fun getLayoutResId(): Int = R.layout.fragment_dynamic;

    override fun init() {
        super.init()

        // 设置标题
        headerTitle.text = getString(R.string.dynamic)

        val logoutString = String.format(getString(R.string.my_logout),
            EMClient.getInstance().currentUser)
        logout.text = logoutString

        logout.setOnClickListener{
            logout();
        }
    }

    /**
     * 退出的方法
     */
    fun logout() {
        EMClient.getInstance().logout(true, object : EMCallBackAdapter() {

            override fun onSuccess() {
                // 退出成功-跳转到登录页面 一定要切换到主线程
                context!!.runOnUiThread {
                    if (activity != null) {
                        // 本地持久化保存 解决（弥补）环信有时候返回登录状态不正确的bug
                        CacheUtils.putString("userName", "")
                        CacheUtils.putString("password", "")
                        toast(R.string.logout_success)
                        startActivity(Intent(activity, LoginActivity::class.java))
                        activity!!.finish()
                    }
                }
            }

            override fun onError(p0: Int, p1: String?) {
                // 退出失败-切换到主线程通知用户
                context!!.runOnUiThread {
                    toast(R.string.logout_failed)
                }
            }
        })
    }
}


