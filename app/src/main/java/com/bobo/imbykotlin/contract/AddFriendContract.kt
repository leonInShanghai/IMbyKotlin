package com.bobo.imbykotlin.contract

/**
 * Created by 公众号：IT波 on 2020/11/1 Copyright © Leon. All rights reserved.
 * Functions: 添加好友协议
 */
interface AddFriendContract {

    interface Presenter: BasePresenter {

        // 搜索(好友)
        fun search(key: String)
    }

    interface View {

        // 搜索(好友)成功
        fun onSearchSuccess()

        // 搜索(好友)失败
        fun onSearchFailed()
    }

}