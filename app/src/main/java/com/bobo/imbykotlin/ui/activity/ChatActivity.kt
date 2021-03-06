package com.bobo.imbykotlin.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.bobo.imbykotlin.R
import com.bobo.imbykotlin.adapter.EMMessageListenerAdapter
import com.bobo.imbykotlin.adapter.MessageListAdapter
import com.bobo.imbykotlin.contract.ChatContract
import com.bobo.imbykotlin.presenter.ChatPresenter
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_chat.recyclerView
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.toast

class ChatActivity : BaseActivity(), ChatContract.View{

    val presenter = ChatPresenter(this)

    lateinit var username: String;

    val messageListener = object : EMMessageListenerAdapter() {

        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            // 收到新的消息 Received :收到的意识
            presenter.addMessage(username, p0)

            // 切换到主线程刷新UI
            runOnUiThread {
                recyclerView.adapter.notifyDataSetChanged()
                scrollToBottom()
            }
        }

    }

    override fun getLayoutResId(): Int = R.layout.activity_chat

    override fun init() {
        super.init()

        // 初始化头布局
        initHeader()

        // 初始化聊天输入框及发送按钮
        initEditText()

        // 初始化下拉加载更多
        initSwipeRefreshLayout()

        // 初始化RecyclerView
        initRecyclerView()

        // 设置接受到消息的监听器
        EMClient.getInstance().chatManager().addMessageListener(messageListener)

        send.setOnClickListener {
            send()
        }
        presenter.loadMessages(username)
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            // 软键盘弹出 RecyclerView会随之上移 这个方法不适用于当前场景
            // (layoutManager as LinearLayoutManager).stackFromEnd = true
            adapter = MessageListAdapter(context, presenter.messages)

            // RecyclerView滚动监听 就是下拉刷新，下面的代码可以用但是体验不好。
            // 2020-1-1 改到initSwipeRefreshLayout()中实现
//            addOnScrollListener(object: RecyclerView.OnScrollListener() {
//
//                // 当滚动状态发送改变时
//                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
//                    // 当循环视图是一个空闲的状态（静止）
//                    // 检查是否划到顶部了,如果是就加载更多数据 IDLE:空闲的
//                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                        val linearLayoutManager = layoutManager as LinearLayoutManager
//                        // 如果第一个可见的条目的位置是0，为划到了顶部
//                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
//                            // 加载更多（聊天历史）数据
//                            presenter.loadMoreMessages(username)
//                        }
//                    }
//                }
//            })
        }

        // 当键盘弹出（此时用户想要输入内容）滚动到最后一条信息
        val counter = Runnable {
            recyclerView.scrollToPosition(presenter.messages.size - 1);
        }

        // 软键盘弹出 RecyclerView会随之上移
        recyclerView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener{
            override fun onLayoutChange(p0: View?, left: Int, top: Int, right: Int, bottom: Int,
                oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                if (bottom < oldBottom) {
                    recyclerView.postDelayed(counter, 100)
                }
            }

        })
    }

    /**
     * 下拉加载更多消息
     */
    private fun initSwipeRefreshLayout() {
        // 设置下拉时出现的箭头颜色
        // swipRefreshLayout.setColorSchemeResources(R.color.mainColor)
        // 一进来本页面就自动刷新一次
        // swipRefreshLayout.isRefreshing = true

        // 给swipRefreshLayout设置属性简化写法
        swipRefreshLayout.apply {
            // 设置下拉时出现的箭头颜色
            setColorSchemeResources(R.color.mainColor)

            // 设置下拉监听
            setOnRefreshListener {
                // 加载更多（聊天历史）数据
                presenter.loadMoreMessages(username)
            }
        }
    }

    // 发送消息
    fun send() {
        // 先隐藏键盘 FIXME:不用隐藏键盘用户说完一句话再说第二句碍事
        // hideSoftKeyboard()

        // 注释原因这样写用户发送不了空格
        // val message = edit.text.trim().toString()
        // 用户可以发送空格
        val message = edit.text.toString()
        // 发送一条消息
        presenter.sendMessage(username, message)

        // 清空编辑(输入框)框
        edit.text.clear()
    }

    private fun initEditText() {
        edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                // 用户输入的文本长度大于0,发送按钮enable
                send.isEnabled = !p0.isNullOrEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

        edit.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                send()
                return true
            }
        })
    }

    fun initHeader() {
        // 让左上角的返回按钮可见
        back.visibility = View.VISIBLE
        // 用户点击了左上角的返回按钮-返回到上一页
        back.setOnClickListener {
            finish()
        }

        // 获取(上一页面传递过来)聊天的用户名
        username = intent.getStringExtra("username")

        var titleString = "";
        if (username.length > 5){
            titleString = username.substring(0,5)
        }
        titleString = String.format(getString(R.string.chat_title), username)
        // 设置标题与XX聊天中
        headerTitle.text = titleString
    }

    // 开始发送一条消息
    override fun onStartSendMessage() {
        // 通知RecyclerView刷新列表
        recyclerView.adapter.notifyDataSetChanged()
    }

    // 发送消息成功
    override fun onSendMessageSuccess() {
        // 通知RecyclerView刷新列表
        recyclerView.adapter.notifyDataSetChanged()
        Log.e("ChatActivity", "onSendMessageSuccess");

        // 发送或接收到新消息的时候RecyclerView自动往上滚动
        scrollToBottom()
    }

    // 收到或发送一条消息后 recyclerView自动往上滚动
    private fun scrollToBottom() {
        recyclerView.scrollToPosition(presenter.messages.size - 1)
    }

    // 发送消息失败
    override fun onSendMessageFailed() {

        // 告诉用户发送消息失败
        toast(R.string.send_message_failed)

        // 通知RecyclerView刷新列表
        recyclerView.adapter.notifyDataSetChanged()
    }

    /**
     * 加载历史聊天消息完成
     */
    override fun onMessageLoaded() {
        // 刷新Recycerview
        recyclerView.adapter.notifyDataSetChanged()
        scrollToBottom()
    }

    override fun onMoreMessageLoaded(size: Int) {

        // 避免空指针 2020-1-1新增加
        if (swipRefreshLayout != null) {
            // 加载失败后刷新图标要隐藏
            swipRefreshLayout.isRefreshing = false
        }

        // 没有更多历史消息消失
        if (size == 0) {
            toast(getString(R.string.no_more_historical_news));
            // 不要再继续往下执行不必要的刷新
            return
        }

        recyclerView.adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(size)
    }

    override fun onDestroy() {
        if (messageListener != null) {
            // 设置接受到消息的监听器不用的时候要移除
            EMClient.getInstance().chatManager().removeMessageListener(messageListener)
        }
        super.onDestroy()
    }
}