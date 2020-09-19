package com.bobo.imbykotlin.contract

/**
 * Created by 公众号：IT波 on 2020/9/13 Copyright © Leon. All rights reserved.
 * Functions:
 */
interface LoginContract {

    // 登录
    interface Presenter: BasePresenter {
        fun login(userName: String, password: String)
    }

    interface View {

        /**
         * 用户名不合法
         */
        fun onUserNameError()

        /**
         * 用户密码不合法
         */
        fun onPasswordError()

        /**
         * 用户开始登录
         */
        fun onStartLogin()

        /**
         * 用户登录成功
         */
        fun onLoggedInSuccess()

        /**
         * 用户登录失败
         */
        fun onLoggedInFailed()
    }
}