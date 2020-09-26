package com.bobo.imbykotlin.ui.activity

import com.bobo.imbykotlin.R
import com.bobo.imbykotlin.factory.FragmentFactory
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Created by 公众号：IT波 on 2020/9/12 Copyright © Leon. All rights reserved.
 * Functions: 这是应用程序的首页
 */
class MainActivity : BaseActivity() {

    override fun getLayoutResId(): Int = R.layout.activity_main;

    override fun init() {
        super.init()

        // 底部tabbar的点击事件
        bottomBar.setOnTabSelectListener { tabId ->
            // 根据tabId 获取对应的fragment（可能为null）
            val fragment = FragmentFactory.instance.getFragment(tabId)
            // 要判断不为空才替换
            if (fragment != null) {
                val beginTransaction = supportFragmentManager.beginTransaction()
                beginTransaction.replace(R.id.fragment_frame,fragment)
                beginTransaction.commit()
            }
        }
    }
}