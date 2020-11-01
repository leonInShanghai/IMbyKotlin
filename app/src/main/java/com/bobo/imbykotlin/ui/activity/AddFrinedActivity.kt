package com.bobo.imbykotlin.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.widget.TextView
import com.bobo.imbykotlin.R
import com.bobo.imbykotlin.contract.AddFriendContract
import com.bobo.imbykotlin.presenter.AddFriendPresenter
import kotlinx.android.synthetic.main.activity_add_frined.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.toast
import com.bobo.imbykotlin.adapter.AddFriendListAdapter as AddFriendListAdapter

/**
 * Created by 公众号：IT波 on 2020/11/1 Copyright © Leon. All rights reserved.
 * Functions: 添加好友页面
 */
class AddFrinedActivity : BaseActivity(), AddFriendContract.View {

    val presenter = AddFriendPresenter(this)

    override fun getLayoutResId(): Int = R.layout.activity_add_frined

    override fun init() {
        super.init()
        headerTitle.text = getString(R.string.add_friend)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = AddFriendListAdapter(context, presenter.addFriendItems)
        }

        // 监听“放大镜”搜索的点击事件
        search.setOnClickListener {
            search()
        }

        // 监听用户点击了键盘上的搜索键
        userName.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                search()
                return true
            }
        })
    }

    /**
     * 用户点击搜索后执行的逻辑
     */
    fun search(){
        // 隐藏键盘
        hideSoftKeyboard()

        // 弹出加载框 搜索中..
        showProgress(getString(R.string.searching))

        // 获取搜索输入框的内容
        val key = userName.text.trim().toString()
        // 执行搜索
        presenter.search(key)
    }

    /**
     * 搜索好友失败
     */
    override fun onSearchFailed() {
        // 隐藏加载框
        dissmissProgress()

        // 提示用户搜索失败
        toast(R.string.search_failed)
    }

    /**
     * 搜索好友成功
     */
    override fun onSearchSuccess() {
        // 隐藏加载框
        dissmissProgress()

        // 提示用户搜索成功
        toast(R.string.search_success)

        // 列表刷新
        recyclerView.adapter.notifyDataSetChanged()
    }
}