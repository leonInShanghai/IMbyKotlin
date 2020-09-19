package com.bobo.imbykotlin.extentiors

/**
 * Created by 公众号：IT波 on 2020/9/13 Copyright © Leon. All rights reserved.
 * Functions: 校验用户名密码的工具类
 */
fun String.isValidUserName(): Boolean = this.matches(Regex("^[1][3,4,5,7,8,9][0-9]{9}$"))

// 3到20位数字 /^[0-9]{3,20}
fun String.isValidPassword(): Boolean = this.matches(Regex("^[0-9a-zA-Z]{3,20}$"))