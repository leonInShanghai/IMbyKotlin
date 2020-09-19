package com.bobo.imbykotlin.contract

/**
 * Created by 公众号：IT波 on 2020/9/12 Copyright © Leon. All rights reserved.
 * Functions:
 */
interface SplashContract {

    interface Presenter : BasePresenter {

        // 检查登录状态
        fun checkLoginStatus()
    }

    interface View {

        // 没有登录的业务UI的处理
        fun onNotLoggedIn()

        // 已经登陆的ui处理
        fun onLoginedIn()
    }
}