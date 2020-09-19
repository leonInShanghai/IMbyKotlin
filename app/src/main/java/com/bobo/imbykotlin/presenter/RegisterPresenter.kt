package com.bobo.imbykotlin.presenter

import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.bobo.imbykotlin.contract.RegiseterContract
import com.bobo.imbykotlin.extentiors.isValidPassword
import com.bobo.imbykotlin.extentiors.isValidUserName

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
                    registerBmob(userName, password)
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

    // 开始注册逻辑请求发送给 Bmob服务器 再注册环信
    private fun registerBmob(userName: String, password: String) {
        val bu = BmobUser()
        bu.username = userName
        bu.setPassword(password)

        bu.signUp<BmobUser>(object : SaveListener<BmobUser>() {
            override fun done(s: BmobUser?, e: BmobException?) {
                if (s == null) {
                    // Bmob注册成功，注册环信

                } else {
                    // Bmob注册失败 通知view注册失败
                    view.onRegisterFailed()
                }
            }
        })
    }


}