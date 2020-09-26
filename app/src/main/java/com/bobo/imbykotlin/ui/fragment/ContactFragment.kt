package com.bobo.imbykotlin.ui.fragment

import android.annotation.SuppressLint
import android.view.View
import com.bobo.imbykotlin.R;
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.header.*

/**
 * Created by 公众号：IT波 on 2020/9/26 Copyright © Leon. All rights reserved.
 * Functions: 联系人页 Contact：联系的意识
 */
class ContactFragment: BaseFragment() {

    override fun getLayoutResId(): Int = R.layout.fragment_contacts;

    @SuppressLint("ResourceAsColor")
    override fun init() {
        super.init()
        // 设置标题
        headerTitle.text = getString(R.string.contact)

        // 添加好友按钮在此时显示
        add.visibility = View.VISIBLE

        // 设置R.color.mainColor 不起作用 直接设置颜色值
        // swipRefreshLayout.setColorSchemeColors(0x43f919)
        // 一进来本页面就自动刷新一次
        // swipRefreshLayout.isRefreshing = true

        // 简化写法
        swipRefreshLayout.apply {
            // 设置R.color.mainColor 不起作用 直接设置颜色值
            setColorSchemeColors(0x43f919)
            // 一进来本页面就自动刷新一次
            isRefreshing = true
        }

    }
}