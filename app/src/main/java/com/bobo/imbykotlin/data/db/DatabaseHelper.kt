package com.bobo.imbykotlin.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.bobo.imbykotlin.app.IMApplication
import org.jetbrains.anko.db.*

/**
 * Created by 公众号：IT波 on 2020/11/8 Copyright © Leon. All rights reserved.
 * Functions: 数据库操作类
 */
class DatabaseHelper(ctx: Context = IMApplication.instant) : ManagedSQLiteOpenHelper(
    ctx, NAME, null, VERSION) {

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.createTable(ContactTable.NAME, true,
            ContactTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            ContactTable.CONTACT to TEXT)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.dropTable(ContactTable.NAME, true)
        onCreate(p0)
    }

    companion object {
        // 数据库的名称
        val NAME = "im.db"

        // 数据库的版本号
        val VERSION = 1
    }
}