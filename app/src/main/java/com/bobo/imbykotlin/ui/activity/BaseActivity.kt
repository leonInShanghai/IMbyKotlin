package com.bobo.imbykotlin.ui.activity

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager


/**
 * Created by 公众号：IT波 on 2020/9/12 Copyright © Leon. All rights reserved.
 * Functions: super activity class
 */
abstract class BaseActivity : AppCompatActivity() {

    // lazy: 不愿工作的;懒散的;懒惰的;无精打采的;懒洋洋的;没下工夫的;粗枝大叶的;马虎的
    val progressDialog by lazy {
        ProgressDialog(this)
    }

    val inputMethodManager by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutResId());

        init();
    }

    /**
     * 供子类自由发挥复写的 初始化方法
     */
    open fun init() {

    }

    /**
     * 强制各个子类实现的返回布局的方法
     */
    abstract fun getLayoutResId(): Int

    /**
     * 各子类根据业务需要 自主显示 progressDialog
     */
    open fun showProgress(message: String){
        progressDialog.setMessage(message)
        // 设置点击其他地方不要消失progressDialog
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }

    /**
     * 各子类根据业务需要 自主隐藏 progressDialog
     */
    open fun dissmissProgress() {
        if (progressDialog != null && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    /**
     * 供子类根据需要 调用后隐藏键盘
     */
    open fun hideSoftKeyboard() {
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}