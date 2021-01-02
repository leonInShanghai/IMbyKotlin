package com.bobo.imbykotlin.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bobo.imbykotlin.R
import com.bobo.imbykotlin.adapter.ContactListAdapter
import com.bobo.imbykotlin.adapter.EMContactListenerAdapter
import com.bobo.imbykotlin.contract.ContactContract
import com.bobo.imbykotlin.data.ContactListItem
import com.bobo.imbykotlin.presenter.ContactPresenter
import com.bobo.imbykotlin.ui.activity.AddFrinedActivity
import com.bobo.imbykotlin.widget.SlideBar
import com.hyphenate.chat.*
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * Created by 公众号：IT波 on 2020/9/26 Copyright © Leon. All rights reserved.
 * Functions: 联系人页 Contact：联系的意识
 */
class ContactFragment: BaseFragment() , ContactContract.View {

    // 实例化presenter
    val presenter = ContactPresenter(this)

    val contactListener = object : EMContactListenerAdapter() {

        /**
         * 当删除用户成功后环信回调
         */
        override fun onContactDeleted(p0: String?) {
            // 从新获取联系人列表集合
            presenter.loadContacts()
        }

        /**
         * 当新增一个好友环信回调
         */
        override fun onContactAdded(p0: String?) {
            // 从新获取联系人列表集合
            presenter.loadContacts()
        }
    }

    override fun getLayoutResId() : Int = R.layout.fragment_contacts

    @SuppressLint("ResourceAsColor")
    override fun init() {
        super.init()
        initHeader()
        initSwipeRefreshLayout()
        initRecyclerView()
        // 联系人变化监听器（添加好友删除好友等）
        EMClient.getInstance().contactManager().setContactListener(contactListener)
        initSlideBar()

        // 加载联系人一进来加载一次
        presenter.loadContacts()
    }

    private fun initSlideBar() {
        slideBar.onSectionChangeListener = object : SlideBar.OnSectionChangeListener {

            override fun onSectionChange(firstLetter: String) {
                // 显示字母
                section.visibility = View.VISIBLE
                section.text = firstLetter

                // recyclerView滚动到对应的位置
                if (firstLetter.equals("#")) {
                    recyclerView.smoothScrollToPosition(0)
                } else {
                    // 因为0代表没有找到对应的item索引所以必须大于0
                    if (getPosition(firstLetter) > 0) {
                        recyclerView.smoothScrollToPosition(getPosition(firstLetter))
                    }
                }
            }

            override fun onSectionFinish() {
                // 隐藏字母
                section.visibility = View.GONE
            }
        }
    }

    private fun initRecyclerView() {
        // 给recyclerView设置属性
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = ContactListAdapter(context, presenter.contactListItems)
        }
    }

    private fun initSwipeRefreshLayout() {
        // 设置下拉时出现的箭头颜色
        // swipRefreshLayout.setColorSchemeResources(R.color.mainColor)
        // 一进来本页面就自动刷新一次
        // swipRefreshLayout.isRefreshing = true

        // 给swipRefreshLayout设置属性简化写法
        swipRefreshLayout.apply {
            // 设置下拉时出现的箭头颜色
            setColorSchemeResources(R.color.mainColor)
            // 一进来本页面就自动刷新一次
            isRefreshing = true

            setOnRefreshListener {
                // 当用户下拉刷新再次请求列表
                presenter.loadContacts()
            }
        }
    }

    private fun initHeader() {
        // 设置标题
        headerTitle.text = getString(R.string.contact)

        // 添加好友按钮在此时显示
        add.visibility = View.VISIBLE

        add.setOnClickListener {
            // 跳转到添加好友界面
            context!!.startActivity<AddFrinedActivity>()
        }
    }

    /**
     * 根据字母获得对应的索引 这个写的不好
     */
    // private fun getPosition(firstLetter: String): Int = presenter.contactListItems.binarySearch {
    //         contactListItem ->  contactListItem.firstLetter.minus(firstLetter[0])
    //     }

    /**
     * 根据字母获得对应的索引
     */
    private fun getPosition(firstLetter: String): Int {
        val size = presenter.contactListItems.size - 1
        for (i in 0..size){
            val item = presenter.contactListItems.get(i)
            if (item.showFirstLetter && item.firstLetter.toString().equals(firstLetter)) {
                return i
            }
        }
        return 0
    }


    /**
     * 联系人列表加载失败
     */
    override fun onLoadContactsFailed() {

        // 避免空指针
        if (swipRefreshLayout != null) {
            // 加载失败后刷新图标要隐藏
            swipRefreshLayout.isRefreshing = false
        }

        // 弹出toast告知用户加载失败了
        context!!.toast(R.string.load_contacts_failed)
    }

    /**
     * 联系人列表加载成功
     */
    override fun onLoadContactsSuccess(contactListItems: MutableList<ContactListItem>) {

        // java.lang.IllegalStateException: swipRefreshLayout must not be null
        if (swipRefreshLayout != null){
            // 加载成功后刷新图标要隐藏
            swipRefreshLayout.isRefreshing = false
        }

        // java.lang.IllegalStateException: recyclerView must not be null
        if (recyclerView != null) {
            // recyclerview更新列表
            // recyclerView.adapter.notifyDataSetChanged()
            (recyclerView.adapter as ContactListAdapter).updateList(contactListItems)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 移除联系人变化监听器（添加好友删除好友）
        EMClient.getInstance().contactManager().removeContactListener(contactListener)
    }
}




//package com.bobo.imbykotlin.ui.fragment
//
//import android.annotation.SuppressLint
//import android.support.v7.widget.LinearLayoutManager
//import android.view.View
//import com.bobo.imbykotlin.R;
//import com.bobo.imbykotlin.adapter.ContactListAdapter
//import com.bobo.imbykotlin.adapter.EMContactListenerAdapter
//import com.bobo.imbykotlin.contract.ContactContract
//import com.bobo.imbykotlin.presenter.ContactPresenter
//import com.bobo.imbykotlin.ui.activity.AddFrinedActivity
//import com.bobo.imbykotlin.widget.SlideBar
//import com.hyphenate.chat.*
//import kotlinx.android.synthetic.main.fragment_contacts.*
//import kotlinx.android.synthetic.main.header.*
//import org.jetbrains.anko.startActivity
//import org.jetbrains.anko.toast
//
///**
// * Created by 公众号：IT波 on 2020/9/26 Copyright © Leon. All rights reserved.
// * Functions: 联系人页 Contact：联系的意识
// */
//class ContactFragment: BaseFragment() , ContactContract.View {
//
//    // 实例化presenter
//    val presenter = ContactPresenter(this)
//
//    // 避免重复加载造成bug的变量 默认是加载完成的可以加载
//    var isLoaded = true
//
//    val contactListener = object : EMContactListenerAdapter() {
//
//        /**
//         * 当删除用户成功后环信回调
//         */
//        override fun onContactDeleted(p0: String?) {
//            // 从新获取联系人列表集合
//            loadContacts()
//        }
//
//        /**
//         * 当新增一个好友环信回调
//         */
//        override fun onContactAdded(p0: String?) {
//            // 从新获取联系人列表集合
//            loadContacts()
//        }
//    }
//
//    override fun getLayoutResId(): Int = R.layout.fragment_contacts;
//
//    @SuppressLint("ResourceAsColor")
//    override fun init() {
//        super.init()
//        initHeader()
//        initSwipeRefreshLayout()
//        initRecyclerView()
//        // 联系人变化监听器（添加好友删除好友等）
//        EMClient.getInstance().contactManager().setContactListener(contactListener)
//        initSlideBar()
//
//        // 加载联系人列表
//        loadContacts()
//    }
//
//    private fun loadContacts() {
//        // 如果上一次还没有加载完成就不要加载
//        if (isLoaded == false) {
//            return
//        }
//
//        // 此时已经在加载了 这次加载没有完成之前不可连续加载
//        isLoaded = false
//
//        presenter.loadContacts()
//    }
//
//    private fun initSlideBar() {
//        slideBar.onSectionChangeListener = object : SlideBar.OnSectionChangeListener {
//
//            override fun onSectionChange(firstLetter: String) {
//                // 显示字母
//                section.visibility = View.VISIBLE
//                section.text = firstLetter
//
//                // recyclerView滚动到对应的位置
//                if (firstLetter.equals("#")) {
//                    recyclerView.smoothScrollToPosition(0)
//                } else {
//                    // 因为0代表没有找到对应的item索引所以必须大于0
//                    if (getPosition(firstLetter) > 0) {
//                        recyclerView.smoothScrollToPosition(getPosition(firstLetter))
//                    }
//                }
//            }
//
//            override fun onSectionFinish() {
//                // 隐藏字母
//                section.visibility = View.GONE
//            }
//        }
//    }
//
//    private fun initRecyclerView() {
//        // 给recyclerView设置属性
//        recyclerView.apply {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(context)
//            ContactListAdapter(context, presenter.contactListItems)
//            adapter = ContactListAdapter(context, presenter.contactListItems)
//        }
//    }
//
//    private fun initSwipeRefreshLayout() {
//        // 设置下拉时出现的箭头颜色
//        // swipRefreshLayout.setColorSchemeResources(R.color.mainColor)
//        // 一进来本页面就自动刷新一次
//        // swipRefreshLayout.isRefreshing = true
//
//        // 给swipRefreshLayout设置属性简化写法
//        swipRefreshLayout.apply {
//            // 设置下拉时出现的箭头颜色
//            setColorSchemeResources(R.color.mainColor)
//            // 一进来本页面就自动刷新一次
//            isRefreshing = true
//
//            setOnRefreshListener {
//                // 当用户下拉刷新再次请求列表
//                loadContacts()
//            }
//        }
//    }
//
//    private fun initHeader() {
//        // 设置标题
//        headerTitle.text = getString(R.string.contact)
//
//        // 添加好友按钮在此时显示
//        add.visibility = View.VISIBLE
//
//        add.setOnClickListener {
//            // 跳转到添加好友界面
//            context!!.startActivity<AddFrinedActivity>()
//        }
//    }
//
//    /**
//     * 根据字母获得对应的索引 这个写的不好
//     */
//    // private fun getPosition(firstLetter: String): Int = presenter.contactListItems.binarySearch {
//    //         contactListItem ->  contactListItem.firstLetter.minus(firstLetter[0])
//    //     }
//
//    /**
//     * 根据字母获得对应的索引
//     */
//    private fun getPosition(firstLetter: String): Int {
//        val size = presenter.contactListItems.size - 1
//        for (i in 0..size){
//            val item = presenter.contactListItems.get(i)
//            if (item.showFirstLetter && item.firstLetter.toString().equals(firstLetter)) {
//                return i
//            }
//        }
//        return 0
//    }
//
//
//    /**
//     * 联系人列表加载失败
//     */
//    override fun onLoadContactsFailed() {
//
//        // 判断一下避免空指针
//        if (swipRefreshLayout != null) {
//            // 加载失败后刷新图标要隐藏
//            swipRefreshLayout.isRefreshing = false
//        }
//
//        // 判断一下避免空指针
//        if (context != null) {
//            // 弹出toast告知用户加载失败了
//            context!!.toast(R.string.load_contacts_failed)
//        }
//
//        // （成功失败都算）此时已经在加载完成了可以再次加载了
//        isLoaded = true
//
//    }
//
//    /**
//     * 联系人列表加载成功
//     */
//    override fun onLoadContactsSuccess() {
//
//        // java.lang.IllegalStateException: swipRefreshLayout must not be null
//        if (swipRefreshLayout != null){
//            // 加载成功后刷新图标要隐藏
//            swipRefreshLayout.isRefreshing = false
//        }
//
//        // java.lang.IllegalStateException: recyclerView must not be null
//        if (recyclerView != null && recyclerView.getScrollState() == 0) {
//            // recyclerview更新列表
//            recyclerView.adapter.notifyDataSetChanged()
//        }
//
//        // （成功失败都算）此时已经在加载完成了可以再次加载了
//        isLoaded = true
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        // 移除联系人变化监听器（添加好友删除好友）
//        EMClient.getInstance().contactManager().removeContactListener(contactListener)
//    }
//}