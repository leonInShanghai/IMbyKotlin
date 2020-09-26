package com.bobo.imbykotlin.utils

import android.content.Context
import com.bobo.imbykotlin.app.IMApplication



/**
 * Created by IT波 on 2019/7/6. Copyright © Leon. All rights reserved.
 * Functions: (本地持久化保存)缓存软件的一些参数和数据
 */
object CacheUtils {
    /**
     * （根据key）得到本地持久化保存boolean的值
     * @param context 上下文
     * @param key 必须是string类型
     * @return
     */
    fun getBoolean(key: String?): Boolean {
        val sp = IMApplication.getContext().getSharedPreferences("atguigu", Context.MODE_PRIVATE)
        return sp.getBoolean(key, false)
    }

    /**
     * 根据key持久化保存软件的Boolean值参数
     * @param context
     * @param key
     * @param value
     */
    fun putBoolean(key: String?, value: Boolean) {
        val sp = IMApplication.getContext().getSharedPreferences("atguigu", Context.MODE_PRIVATE)
        sp.edit().putBoolean(key, value).commit()
    }


    /**
     * 根据key持久化保存软件的String值参数  磁盘 + SharedPreferences
     * @param context
     * @param key
     * @param value
     */
    fun putString(key: String?, value: String) {
        val sp = IMApplication.getContext().getSharedPreferences("atguigu", Context.MODE_PRIVATE)
        sp.edit().putString(key, value).commit()
    }

    /**
     * （根据key）得到本地持久化保存的String值 磁盘 + SharedPreferences
     * @param context 上下文
     * @param key 必须是string类型
     * @return
     */
    fun getString(key: String?): String {
        val sp = IMApplication.getContext().getSharedPreferences("atguigu", Context.MODE_PRIVATE)
        return sp.getString(key, "")
    }
}