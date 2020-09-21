package com.bobo.imbykotlin

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.view.KeyEvent
import android.widget.TextView
import com.bobo.imbykotlin.contract.LoginContract
import com.bobo.imbykotlin.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * Created by 公众号：IT波 on 2020/9/13 Copyright © Leon. All rights reserved.
 * Functions: 用户登录界面
 */
class LoginActivity : BaseActivity(), LoginContract.View {

    val presenter = LoginPresenter(this)

    override fun init() {
        super.init()

        // 用户点击了新用户按钮 跳转到注册页面
        newUser.setOnClickListener {
            startActivity<RegisterActivity>()
        }

        // 用户点击了登录按钮 presenter开始登录
        login.setOnClickListener {
            login()
        }

        // 用户点击了键盘上的“下一步”按钮也登录
        password.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                login()
                return true
            }
        })
    }

    // 用户点击了登录按钮
    fun login() {
        // 隐藏手机键盘
        hideSoftKeyboard()

        // 判断是否已经有了写磁盘的权限（因为环信sdk中有数据库操作）
        // 不做会有红色日志但没有闪退 android.database.sqlite.SQLiteCantOpenDatabaseException
        if (hasWriteExternalStoragePermission()){
            // 有权限获得用户名和密码登录
            val userNameString = userName.text.trim().toString()
            val passwordString = password.text.trim().toString()
            presenter.login(userNameString, passwordString)
        } else {
            // 没有权限 去申请权限
            applyWriteExteranlStoragePermissino()
        }
    }

    // （没有磁盘的权限时） 申请磁盘写入权限
    private fun applyWriteExteranlStoragePermissino() {
        val persissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, persissions, 0)
    }

    // 判断是否已经有了写磁盘的权限（因为环信sdk中有数据库操作）
    private fun hasWriteExternalStoragePermission(): Boolean {
        val resultStorage = ActivityCompat.checkSelfPermission(this, Manifest.
        permission.WRITE_EXTERNAL_STORAGE)
//        val resultState = ActivityCompat.checkSelfPermission(this, Manifest.
//        permission.READ_PHONE_STATE)
        return resultStorage == PackageManager.PERMISSION_GRANTED 
    }

    // 申请磁盘写入权限 的系统回调
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (index in grantResults.indices){
            if (grantResults[index] != PackageManager.PERMISSION_GRANTED){
                // 用户有不同意权限的行为 提示用户
                toast(R.string.please_allow_permission)

                // 结束后续操作
                return
            }
        }

        // 用户同意了写入权限 开始登录
        login()
    }

    override fun getLayoutResId(): Int = R.layout.activity_login;

    override fun onUserNameError() {
        userName.error = getString(R.string.user_name_error)
    }

    override fun onPasswordError() {
        password.error = getString(R.string.password_error)
    }

    /**
     * 用户开始登录
     */
    override fun onStartLogin() {
        // 开始登录弹出进度条（loading）
        showProgress(getString(R.string.logging))
    }

    /**
     * 用户登录成功
     */
    override fun onLoggedInSuccess() {
        // 隐藏进度条
        dissmissProgress()

        // 进入到主界面
        startActivity<MainActivity>()

        // 当前页面要finish()
        finish()
    }

    /**
     * 用户登录失败
     */
    override fun onLoggedInFailed() {
        // 隐藏进度条
        dissmissProgress()

        // 弹出提示：登录失败
        toast(R.string.login_failed)
    }
}