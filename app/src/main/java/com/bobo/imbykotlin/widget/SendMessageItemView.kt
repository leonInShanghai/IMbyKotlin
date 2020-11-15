package com.bobo.imbykotlin.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.RelativeLayout
import com.bobo.imbykotlin.R
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMTextMessageBody
import com.hyphenate.util.DateUtils
import kotlinx.android.synthetic.main.view_add_friend_item.view.timestamp
import kotlinx.android.synthetic.main.view_send_message_item.view.*
import java.util.*


/**
 * Created by 公众号：IT波 on 2020/11/8 Copyright © Leon. All rights reserved.
 * Functions: 发送（自己发给别人的）消息的自定义控件
 */
class SendMessageItemView(context: Context?, attrs: AttributeSet? = null) :
    RelativeLayout(context, attrs) {

    init {
        // 注意当前是在自定义view第三个参数要传this
        View.inflate(context, R.layout.view_send_message_item, this)
    }

    fun bindView(emMessage: EMMessage) {
        // 更新时间戳
        updateTimestamp(emMessage)

        // 更新消息
        updateMessage(emMessage)

        // 更新进度条
        updateProgress(emMessage)
    }

    // 更新进度条
    private fun updateProgress(emMessage: EMMessage) {
        emMessage.status().let {
            when (it) {
                // 加载中的处理
                EMMessage.Status.INPROGRESS -> {
                    // 开启动画之前要确保加载圈(imageView) 是显示的
                    sendMessageProgress.visibility = View.VISIBLE
                    sendMessageProgress.setImageResource(R.drawable.send_message_progres)

                    // 加载圈(imageView) 转起来 注释的方法在新版本上不能用了
                    // val animationDrawable = sendMessageProgress.drawable as AnimationDrawable
                    // animationDrawable.start()

                    // 加载圈(imageView) 转起来 新方法
                    val rotate = RotateAnimation(0f, 360f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                    val lin = LinearInterpolator()
                    rotate.interpolator = lin
                    rotate.duration = 2000 //设置动画持续周期
                    rotate.repeatCount = -1 //设置重复次数
                    rotate.fillAfter = true //动画执行完后是否停留在执行完的状态
                    rotate.startOffset = 10 //执行前的等待时间
                    sendMessageProgress.setAnimation(rotate)
                }
                // 发送成功状态的处理
                EMMessage.Status.SUCCESS -> {
                    // 加载圈隐藏
                    sendMessageProgress.visibility = View.GONE
                    // 清除动画 新方法
                    sendMessageProgress.clearAnimation();
                }
                // 发送失败状态的处理
                EMMessage.Status.FAIL -> {
                    // 首先要确保加载圈(imageView) 是显示的
                    sendMessageProgress.visibility = View.VISIBLE
                    // 显示红色感叹号图
                    sendMessageProgress.setImageResource(R.mipmap.msg_error)
                }
            }
        }
    }

    // 更新消息
    private fun updateMessage(emMessage: EMMessage) {
        // 判断消息是否是文本类型
        if (emMessage.type == EMMessage.Type.TXT) {
            sendMessage.text = (emMessage.body as EMTextMessageBody).message
        } else {
            sendMessage.text = context.getString(R.string.no_text_message)
        }
    }

    // 设置显示发送消息的时间
    private fun updateTimestamp(emMessage: EMMessage) {
        timestamp.text = DateUtils.getTimestampString(Date(emMessage.msgTime))
    }

}