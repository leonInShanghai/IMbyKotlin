package com.bobo.imbykotlin.presenter

import com.bobo.imbykotlin.contract.RegiseterContract
import com.bobo.imbykotlin.extentiors.isValidPassword
import com.bobo.imbykotlin.extentiors.isValidUserName
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import org.jetbrains.anko.doAsync

// import org.jetbrains.anko.doAsync
// import org.jetbrains.anko.uiThread

/**
 * Created by 公众号：IT波 on 2020/9/19 Copyright © Leon. All rights reserved.
 * Functions: 注册页面中介
 */
class RegisterPresenter(val view: RegiseterContract.View) : RegiseterContract.Presenter{

    /**
     * 用户发起注册 526247082
     */
    override fun register(userName: String, password: String, confirmPassword: String) {

        // 校验用户名格式是否正确
        if (userName.isValidUserName()) {
            // 如果用户名格式合法检查密码是否格式合法
            if (password.isValidPassword()) {
                // 如果密码格式也正确检查确认密码
                if (password.equals(confirmPassword)) {
                    // 三次都通过检查通知view开始注册
                    view.onStartRegister()

                    // 开始注册逻辑请求发送给 Bmob服务器 再注册环信
                    // registerBmob(userName, password)

                    registerEaseMod(userName, password)
                } else {
                    // 通知view两次密码不一致
                    view.onConfirmPasswordError()
                }
            } else {
                // 通知view层密码格式不正确
                view.onPasswordError()
            }
        } else {
            // 通知view层用户名格式不正确
            view.onUserNameError()
        }
    }

    // 开始注册逻辑请求发送给 Bmob服务器 再注册环信 没有使用下面是原因
    // 指定的Android SDK构建工具版本（26.0.1）被忽略，因为它低于Android Gradle Plugin 3.1.4支持的最低版本
    // （27.0.3）
//    private fun registerBmob(userName: String, password: String) {
//        val bu = BmobUser()
//        bu.username = userName
//        bu.setPassword(password)
//
//        bu.signUp<BmobUser>(object : SaveListener<BmobUser>() {
//            override fun done(s: BmobUser?, e: BmobException?) {
//                if (e == null) {
//                    // Bmob注册成功，注册环信
//                    registerEaseMod(userName, password)
//                } else {
//                    if (e.errorCode == 202){
//                        // 该用户名Bmob已经采取,去注册环信
//                        registerEaseMod(userName, password)
//                    } else {
//                        // Bmob注册失败 通知view注册失败
//                        if (e.errorCode == 202) {
//                            view.onUserExist()
//                        } else {
//                            view.onRegisterFailed()
//                        }
//                    }
//                }
//            }
//        })
//    }

    // 注册好Bmob再注册环信
    private fun registerEaseMod(userName: String, password: String) {

        // 环信的需要到子线程去操作注册业务逻辑 方法二：
//        object : Thread() {
//            override fun run() {
//                try {
//                    // 在子线程注册用户
//                    EMClient.getInstance().createAccount(userName, password)
//
//                    // 没有抛异常就是成功 此时需要切换到主线程通知view
//                    uiThread {
//                        view.onRegisterSuccess()
//                    }
//                } catch (e: HyphenateException) {
//                    // 进了异常就是注册失败，切换到主线程通知view注册失败了
//                    e.printStackTrace()
//                    uiThread {
//                        view.onRegisterFailed()
//                    }
//                }
//            }
//        }.start()

        // 环信的需要到子线程去操作注册业务逻辑  方法一：
        doAsync {
            try {
                // 在子线程注册用户
                EMClient.getInstance().createAccount(userName , password)

                // 没有抛异常就是成功 此时需要切换到主线程通知view
                uiThread {
                    view.onRegisterSuccess()
                }
            } catch (e: HyphenateException) {
                // 进了异常就是注册失败，切换到主线程通知view注册失败了
                e.printStackTrace()
                uiThread {
                    if (e.errorCode == 203) {
                        // 用户已经存在
                        view.onUserExist()
                    } else {
                        // 注册失败
                        view.onRegisterFailed()
                    }
                }
            }
        }
    }
}