package com.bobo.imbykotlin.data.db

import com.bobo.imbykotlin.extentiors.toVarargArry
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

/**
 * Created by 公众号：IT波 on 2020/11/8 Copyright © Leon. All rights reserved.
 * Functions:
 */
class IMDatabase {

    companion object {
        val databaseHelper = DatabaseHelper()
        val instance = IMDatabase()
    }

    /**
     * 保存联系人的方法
     */
    fun saveContact(contact: Contact) {
        databaseHelper.use {
            // SQLiteDatabase的扩展方法 *是展开的意识
            insert(ContactTable.NAME, *contact.map.toVarargArry())
        }
    }

    /**
     * 获取全部联系人的方法
     */
    fun getAllContacts(): List<Contact> = databaseHelper.use {
            select(ContactTable.NAME).parseList(object : MapRowParser<Contact> {
                override fun parseRow(columns: Map<String, Any?>): Contact
                    = Contact(columns.toMutableMap())
            })
    }

    /**
     * 删除所有的联系人
     */
    fun deleteAllContacts() {
        databaseHelper.use {
            delete(ContactTable.NAME, null, null)
        }
    }

    /**
     * 根据用户名搜索用户 新添加
     */
    fun getContactsFromUserKey(key: String): String {
        val sql = "select " + ContactTable.CONTACT + " from " + ContactTable.NAME + " where " +
                ContactTable.CONTACT +" = ?;"
        var name = ""
        databaseHelper.use {
            val cursor = rawQuery(sql, arrayOf(key))

            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(ContactTable.CONTACT))
                cursor.moveToNext()
            }
            cursor.close()
        }
        return name
    }

}