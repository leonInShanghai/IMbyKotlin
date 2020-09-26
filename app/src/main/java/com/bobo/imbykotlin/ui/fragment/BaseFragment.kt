package com.bobo.imbykotlin.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by 公众号：IT波 on 2020/9/12 Copyright © Leon. All rights reserved.
 * Functions: super Fragment class
 */
abstract class BaseFragment : Fragment() {

    // 方法中只有一行的时候可以把大括号去掉写 = 号
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
    Bundle?): View? = inflater.inflate(getLayoutResId(), null);

    // 方法中只有一行的时候可以把大括号去掉写 = 号
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = init();

    /**
     * 供子类自由实现的初始化方法
     */
    open fun init() {

    }

    /**
     * 强制子类实现返回布局id
     */
    abstract fun getLayoutResId(): Int
}