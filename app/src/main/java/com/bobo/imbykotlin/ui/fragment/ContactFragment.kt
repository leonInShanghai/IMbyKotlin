package com.bobo.imbykotlin.ui.fragment

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bobo.imbykotlin.R;
import com.bobo.imbykotlin.adapter.ContactListAdapter
import com.bobo.imbykotlin.adapter.EMContactListenerAdapter
import com.bobo.imbykotlin.contract.ContactContract
import com.bobo.imbykotlin.presenter.ContactPresenter
import com.hyphenate.chat.*
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.toast

/**
 * Created by 公众号：IT波 on 2020/9/26 Copyright © Leon. All rights reserved.
 * Functions: 联系人页 Contact：联系的意识
 */
class ContactFragment: BaseFragment() , ContactContract.View {

    // 实例化presenter
    val presenter = ContactPresenter(this)

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

        // 给swipRefreshLayout设置属性简化写法
        swipRefreshLayout.apply {
            // 设置R.color.mainColor 不起作用 直接设置颜色值
            setColorSchemeColors(0x43f919)
            // 一进来本页面就自动刷新一次
            isRefreshing = true

            setOnRefreshListener {
                // 当用户下拉刷新再次请求列表
                presenter.loadContacts()
            }
        }

        // 给recyclerView设置属性
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = ContactListAdapter(context, presenter.contactListItems)
        }

        // 联系人变化监听器（添加好友删除好友）
        EMClient.getInstance().contactManager().setContactListener(object : EMContactListenerAdapter() {

            /**
             * 当删除用户成功后环信回调
             */
            override fun onContactDeleted(p0: String?) {
                // 从新获取联系人列表集合
                presenter.loadContacts()
            }
        })

        // 加载联系人列表
        presenter.loadContacts()
    }

    /**
     * 联系人列表加载失败
     */
    override fun onLoadContactsFailed() {
        // 加载失败后刷新图标要隐藏
        swipRefreshLayout.isRefreshing = false

        // 弹出toast告知用户加载失败了
        context!!.toast(R.string.load_contacts_failed)
    }

    /**
     * 联系人列表加载成功
     */
    override fun onLoadContactsSuccess() {
        // 加载成功后刷新图标要隐藏
        swipRefreshLayout.isRefreshing = false

        // recyclerview更新列表
        recyclerView.adapter.notifyDataSetChanged()
    }
}