package com.bobo.imbykotlin.contract

import android.os.Handler
import android.os.Looper


/**
 * Created by 公众号：IT波 on 2020/9/12 Copyright © Leon. All rights reserved.
 * Functions:
 */
interface BasePresenter {

    companion object {
        val handler by lazy {
            Handler(Looper.getMainLooper())
        }
    }

    // 由于环信的回调都在子线程，下面是一个切换到主线程的方法
    fun uiThread(f: () -> Unit) {
        handler.post(object : Runnable {
            override fun run() {
                // 此时我们已经在主线程中了
                f()
            }
        })
    }
}