package com.bobo.imbykotlin.presenter

import com.bobo.imbykotlin.adapter.EMCallBackAdapter
import com.bobo.imbykotlin.contract.LoginContract
import com.bobo.imbykotlin.extentiors.isValidPassword
import com.bobo.imbykotlin.extentiors.isValidUserName
import com.bobo.imbykotlin.utils.CacheUtils
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient

/**
 * Created by 公众号：IT波 on 2020/9/13 Copyright © Leon. All rights reserved.
 * Functions: Login 业务逻辑处理层
 */
class LoginPresenter(val view: LoginContract.View): LoginContract.Presenter {

    /**
     * 处理登录逻辑
     */
    override fun login(userName: String, password: String) {
        if (userName.isValidUserName()) {
            // 用户名格式正确 继续校验密码
            if (password.isValidPassword()) {
                // 密码也是格式正确 view开始登录(loading)
                view.onStartLogin()

                // 开始登录到环信
                loginEaseMob(userName, password)
            } else {
                view.onPasswordError()
            }
        } else {
            view.onUserNameError()
        }
    }

    // 登录到环信
    private fun loginEaseMob(userName: String, password: String) {
        EMClient.getInstance().login(userName, password, object : EMCallBackAdapter() {

            override fun onSuccess() {
                super.onSuccess()

                // 加载所有组和所有聊天对话
                EMClient.getInstance().groupManager().loadAllGroups()
                EMClient.getInstance().chatManager().loadAllConversations()

                // 转到主线程 登录成功
                uiThread {
                    // 本地持久化保存 解决（弥补）环信 有时候返回登录状态不正确的bug
                    CacheUtils.putString("userName",userName)
                    CacheUtils.putString("password",password)
                    view.onLoggedInSuccess()
                }
            }

            override fun onError(p0: Int, p1: String?) {
                super.onError(p0, p1)

                // 转到主线程 登录失败
                uiThread {
                    view.onLoggedInFailed()
                }
            }

        })
    }
}