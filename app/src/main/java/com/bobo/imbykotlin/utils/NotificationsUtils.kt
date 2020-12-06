package com.bobo.imbykotlin.utils

import android.content.Context
import android.support.v4.app.NotificationManagerCompat


/**
 * Created by 公众号：IT波 on 2020/12/6 Copyright © Leon. All rights reserved.
 * Functions:判断用户有没有 设置-通知与状态栏开启权限
 * 没错，只有一行代码。
 * Api24以上，NotificationManagerCompat中提供了areNotificationsEnabled()方法。该方法中已经对API19以下，
 * API19-24，API24以上，这三种情况做了判断。直接使用其返回值即可
 */
object NotificationsUtils {
    fun isNotificationEnabled(context: Context): Boolean {
        var isOpened = false
        isOpened = try {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
        return isOpened
    }
}