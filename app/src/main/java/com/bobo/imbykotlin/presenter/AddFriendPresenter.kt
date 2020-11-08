package com.bobo.imbykotlin.presenter

import android.webkit.WebView.FindListener
import com.bobo.imbykotlin.contract.AddFriendContract
import com.bobo.imbykotlin.data.AddFriendItem
import com.bobo.imbykotlin.data.db.IMDatabase
import com.bobo.imbykotlin.extentiors.isValidUserName
import com.hyphenate.chat.EMClient
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by 公众号：IT波 on 2020/11/1 Copyright © Leon. All rights reserved.
 * Functions:
 */
class AddFriendPresenter(val view: AddFriendContract.View): AddFriendContract.Presenter {

    val addFriendItems = mutableListOf<AddFriendItem>()

    override fun search(key: String) {
        // 这里没有做搜索bmob 要独立域名暂时为处理
        // val query = BmobQuery<BmobUser>
        // query.addWhereContains("username",key);
        //      .addWhereNotEqualTo("username",EMClient.getInstance().currentUser)
        // query.findObjects(object : FindListener<BmobUser?> {
        //     fun done(p0: List<BmobUser?>?, e: BmobException?) {
        //         if (e == null) {
        //          doAsync{
        //           p0.forEach{
        //             // 查找成功
        //             val addFrientItem = AddFriendItem(it.username, it.createdAt)
        //             addFriendItems.add(addFrientItem)
        //             }
        //             uiThread{
        //             view.onSearchSuccess()
        //             }
        //          }
        //         } else {
        //             // 查找失败
        //             view.onSearchFailed()
        //         }
        //     }
        // })


        if (key.isValidUserName()) {
            // 这里写的是直接成功



            // 先清空数据
            addFriendItems.clear()

            // 处理数据
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())

            // 本地sql语句查看用户是否已经存在
            val aContacts = IMDatabase.instance.getContactsFromUserKey(key)

            // 比对是否已经添加过好友或者是自己
            var isAdded = aContacts.equals(key) || key.equals(EMClient.getInstance().currentUser);

            val addFrientItem = AddFriendItem(key, currentDate, isAdded)
            addFriendItems.add(addFrientItem)

            view.onSearchSuccess()
        } else {
            // 由于用户名格式都不对 查找失败
            view.onSearchFailed()
        }

    }

    // 环信添加好友参数为要添加的好友的username和添加理由
    // EMClient.getInstance().contactManager().addContact(toAddUsername, reason);
}