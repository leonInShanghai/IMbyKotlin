package com.bobo.imbykotlin.presenter

import com.bobo.imbykotlin.contract.SplashContract
import com.hyphenate.chat.EMClient

/**
 * Created by 公众号：IT波 on 2020/9/12 Copyright © Leon. All rights reserved.
 * Functions:
 */
class SplashPresenter(val view: SplashContract.View) : SplashContract.Presenter {


    override fun checkLoginStatus() {

        // 处理登录和未登录的逻辑
        if (isLoggedIn()) view.onLoginedIn() else view.onNotLoggedIn()
    }

    // 检查登录状态 是判断是否登录到环信的服务器 （已经链接并且已经登录过）
    private fun isLoggedIn(): Boolean = EMClient.getInstance().isConnected &&
            EMClient.getInstance().isLoggedInBefore
}