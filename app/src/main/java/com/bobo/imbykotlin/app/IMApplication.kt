package com.bobo.imbykotlin.app

import android.app.Application
import com.hyphenate.chat.BuildConfig
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import com.hyphenate.easeui.EaseUI


/**
 * Created by 公众号：IT波 on 2020/9/12 Copyright © Leon. All rights reserved.
 * Functions:  Application 全局唯一默认单例
 */
class IMApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // 注释原因测试代码
        // EaseUI.getInstance().init(applicationContext, null);

        // 初始化环信sdk
        EMClient.getInstance().init(this, EMOptions())

        // 在做打包混淆时，要关闭debug模式，避免消耗不必要的资源
        // EMClient.getInstance().setDebugMode(true)
        EMClient.getInstance().setDebugMode(BuildConfig.DEBUG)

        // Bmob没有使用的原因是版本太低
        // 指定的Android SDK构建工具版本（26.0.1）被忽略，因为它低于Android Gradle Plugin 3.1.4支持的最低版本
        // （27.0.3）
        // 实例化bmob 第一个参数是上下文 第二个参数是Application ID:
        // Bmob.initialize(applicationContext,"863b889ac932802a9adb395d0fe47024")
    }
}