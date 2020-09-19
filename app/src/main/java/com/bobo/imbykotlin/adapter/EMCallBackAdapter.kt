package com.bobo.imbykotlin.adapter

import com.hyphenate.EMCallBack

/**
 * Created by 公众号：IT波 on 2020/9/13 Copyright © Leon. All rights reserved.
 * Functions: 适配器对 EMCallBack 进行适配不需要的方法不用每次都复写
 */
open class EMCallBackAdapter: EMCallBack {

    override fun onSuccess() {

    }

    override fun onProgress(p0: Int, p1: String?) {

    }

    override fun onError(p0: Int, p1: String?) {

    }
}