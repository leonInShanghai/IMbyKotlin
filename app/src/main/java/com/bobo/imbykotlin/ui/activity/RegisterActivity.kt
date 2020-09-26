package com.bobo.imbykotlin.ui.activity

import android.view.KeyEvent
import android.widget.TextView
import com.bobo.imbykotlin.R
import com.bobo.imbykotlin.contract.RegiseterContract
import com.bobo.imbykotlin.presenter.RegisterPresenter
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_login.userName
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast

/**
 * Created by 公众号：IT波 on 2020/9/13 Copyright © Leon. All rights reserved.
 * Functions: 用户注册页面
 */
class RegisterActivity : BaseActivity(), RegiseterContract.View {

    val presenter = RegisterPresenter(this)

    override fun getLayoutResId(): Int =
        R.layout.activity_register

    override fun init() {
        super.init()

        // 用户点击了注册按钮
        register.setOnClickListener {
            register()
        }

        // 用户点击了确认密码键盘上的确认键
        cofirmPassword.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                register()
                return true
            }
        })
    }

    /**
     * 注册方法
     */
    fun register() {
        // 隐藏软键盘
        hideSoftKeyboard()

        // 获取用户名 密码 再次输入密码
        val userNameString = userName.text.trim().toString()
        val passwordString = password.text.trim().toString()
        val confiremPasswordString = cofirmPassword.text.trim().toString()

        // 传入用户名 密码 再次确认密码 处理注册逻辑
        presenter.register(userNameString, passwordString, confiremPasswordString)
    }

    /**
     * 用户名不合法
     */
    override fun onUserNameError() {
        userName.error = getString(R.string.user_name_error)
    }

    /**
     * 密码不合法
     */
    override fun onPasswordError() {
        password.error = getString(R.string.password_error)
    }

    /**
     * 第二次密码和第一次不一样
     */
    override fun onConfirmPasswordError() {
        cofirmPassword.error = getString(R.string.cofirm_password_error)
    }

    /**
     * 开始注册
     */
    override fun onStartRegister() {
        showProgress(getString(R.string.registering))
    }

    /**
     * 注册成功隐藏Progress 并返回上一页
     */
    override fun onRegisterSuccess() {
        dissmissProgress()
        toast(R.string.register_success)
        finish()
    }

    /**
     * 注册失败
     */
    override fun onRegisterFailed() {
        dissmissProgress()
        toast(R.string.register_failed)
    }

    /**
     * 由于用户已经存在 注册失败
     */
    override fun onUserExist() {
        dissmissProgress()
        toast(R.string.user_already_exist)
    }
}