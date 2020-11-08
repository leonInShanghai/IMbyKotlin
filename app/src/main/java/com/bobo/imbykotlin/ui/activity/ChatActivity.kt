package com.bobo.imbykotlin.ui.activity

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.bobo.imbykotlin.R
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.header.*

class ChatActivity : BaseActivity() {


    override fun getLayoutResId(): Int = R.layout.activity_chat

    override fun init() {
        super.init()

        // 初始化头布局
        initHeader()

        // 初始化聊天输入框及发送按钮
        initEditText()
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
    }

    fun initHeader() {
        // 让左上角的返回按钮可见
        back.visibility = View.VISIBLE
        // 用户点击了左上角的返回按钮-返回到上一页
        back.setOnClickListener {
            finish()
        }

        // 获取(上一页面传递过来)聊天的用户名
        var username = intent.getStringExtra("username")
        if (username.length > 5){
           username = username.substring(0,5)
        }
        val titleString = String.format(getString(R.string.chat_title), username)
        // 设置标题与XX聊天中
        headerTitle.text = titleString
    }
}