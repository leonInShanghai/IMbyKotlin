package com.bobo.imbykotlin.contract

/**
 * Created by 公众号：IT波 on 2020/9/19 Copyright © Leon. All rights reserved.
 * Functions: 注册页面协议
 */
interface RegiseterContract {

    interface Presenter : BasePresenter {

        /**
         * 注册
         */
        fun register(userName: String, password: String, confirmPassword: String)
    }

    interface View {

        /**
         * 用户名不合法
         */
        fun onUserNameError()

        /**
         * 密码不合法
         */
        fun onPasswordError()

        /**
         * 确认密码不合法
         */
        fun onConfirmPasswordError()

        /**
         * 开始注册
         */
        fun onStartRegister()

        /**
         * 注册成功
         */
        fun onRegisterSuccess()

        /**
         * 注册失败
         */
        fun onRegisterFailed()

        /**
         * 由于用户已经存在 注册失败
         */
        fun onUserExist()
    }

}