package com.bobo.imbykotlin.ui.activity

import android.os.Handler
import com.bobo.imbykotlin.R
import com.bobo.imbykotlin.contract.SplashContract
import com.bobo.imbykotlin.presenter.SplashPresenter
import org.jetbrains.anko.startActivity

class SplashActivity : BaseActivity(), SplashContract.View{

    val presenter = SplashPresenter(this)

    companion object {
        val DELAY = 1600L;
    }

    /**
     * 解决用户按返回键推出后 app到首页或登录页又出现的 用户体验优化
     */
    var isBackPressed : Boolean = false

    val handler by lazy {
        Handler()
    }

    override fun init() {
        super.init()
        presenter.checkLoginStatus()
    }

    // kotlin语法只有一行的时候可以省略大括号但是要用等号
    override fun getLayoutResId(): Int =
        R.layout.activity_splash

    // 没有登录的业务UI的处理
    override fun onNotLoggedIn() {

        // 延时两秒跳转到登陆界面
        handler.postDelayed(object: Runnable{
            override fun run() {

                // 如果用户按下返回键 不要再继续跳转
                if (isBackPressed) {
                    return
                }

                // 跳转到登录界面
                startActivity<LoginActivity>()

                // 关闭本页面
                finish()
            }

        }, DELAY)
    }

    // 已经登陆的ui处理
    override fun onLoginedIn() {

        // 跳转到登录界面
        startActivity<MainActivity>()

        // 关闭本页面
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // 用户按下了返回键此时返回用户的桌面不再继续其他页面跳转
        isBackPressed = true;
    }
}