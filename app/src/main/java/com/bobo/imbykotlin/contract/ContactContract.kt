package com.bobo.imbykotlin.contract

import com.bobo.imbykotlin.data.ContactListItem

/**
 * Created by 公众号：IT波 on 2020/10/26 Copyright © Leon. All rights reserved.
 * Functions: 联系人列表mvp协议
 */
interface ContactContract {

    interface Presenter : BasePresenter{

        /**
         * 加载联系人
         */
        fun loadContacts()
    }

    interface View {

        /**
         * 联系人列表加载成功
         */
        fun onLoadContactsSuccess(contactListItems: MutableList<ContactListItem>)

        /**
         * 联系人列表加载失败
         */
        fun onLoadContactsFailed()

    }
}