package com.bobo.imbykotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bobo.imbykotlin.R;
import com.bobo.imbykotlin.data.ContactListItem
import kotlinx.android.synthetic.main.view_contact_item.view.*

/**
 * Created by 公众号：IT波 on 2020/10/26 Copyright © Leon. All rights reserved.
 * Functions: 联系人列表条目布局自定义控件
 */
class ContactListItemView(context: Context?, attrs: AttributeSet? = null) :
    RelativeLayout(context, attrs) {

    init {
        // 自定义控件最后一个参数传this
        View.inflate(context, R.layout.view_contact_item, this)
    }

    fun bindView(contactListItem: ContactListItem) {

        // 判断是否显示首字符
        if (contactListItem.showFirstLetter) {
            // 需要显示textview
            fitstLetter.visibility = View.VISIBLE
            // 设置首字母
            fitstLetter.text = contactListItem.firstLetter.toString()
        } else {
            // 不需要显示textview
            fitstLetter.visibility = View.GONE
        }

        // 设置用户名
        userName.text = contactListItem.userName
    }
}