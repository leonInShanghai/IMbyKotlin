package com.bobo.imbykotlin.app

import android.annotation.SuppressLint
import android.app.*
import android.app.ActivityManager.RunningTaskInfo
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.SoundPool
import android.net.Uri
import android.util.Log
import com.bobo.imbykotlin.R
import com.bobo.imbykotlin.adapter.EMMessageListenerAdapter
import com.bobo.imbykotlin.ui.activity.ChatActivity
import com.bobo.imbykotlin.utils.NotificationsUtils
import com.hyphenate.chat.*


/**
 * Created by 公众号：IT波 on 2020/9/12 Copyright © Leon. All rights reserved.
 * Functions:  Application 全局唯一默认单例
 */
class IMApplication : Application() {

    // soundPool : 声音池, quality : 质量
    val soundPool = SoundPool(2, AudioManager.STREAM_MUSIC, 0)

    // soundPool先加载音频文件
    val duan by lazy {
        Log.d("App", "load duan")
        soundPool.load(instance, R.raw.duan, 0)
    }

    // soundPool先加载音频文件
    val yulu by lazy {
        Log.d("App", "load yulu")
        soundPool.load(instance, R.raw.yulu, 0)
    }

    // 消息监听器用于播放短信铃声，app前台和后台声音不一样
    val messageListener = object : EMMessageListenerAdapter() {
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            // 如果是在前台，则播放短的音效
            if (isForeground()) {
                Log.d("App", "播放短的音效")
                soundPool.play(duan, 1f, 1f, 0, 0, 1f)
            } else {
                // 如果是在后台，则播放长的音效
                val b: Boolean = NotificationsUtils.isNotificationEnabled(applicationContext)
                if (!b) {
                    // 如果没有显示通知栏的权限咱自己铃声响起
                    soundPool.play(yulu, 1f, 1f, 0, 0, 1f)
                }
                Log.d("App", "app 在后台 播放长的音效 并弹出通知$b")

                // 弹出(Notification)通知到通知栏
                showNotification(p0)
            }
        }
    }

    /**
     * 当app在后台收到消息显示通知的方法
     */
    private fun showNotification(p0: MutableList<EMMessage>?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        p0?.forEach {
            var contentText = getString(R.string.no_text_message)

            // 判断收到的消息是否是文本消息
            if (it.type == EMMessage.Type.TXT) {
                contentText = (it.body as EMTextMessageBody).message
            }

            // 注释的代码在Android 8.0(<API 26) 之前可以用不适合2020年了
//            val notification = Notification.Builder(this)
//                .setContentTitle(getString(R.string.receive_new_message))
//                .setContentText(contentText)
//                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ease_default_icon))
//                .setSmallIcon(R.mipmap.ic_contact)
//                .notification
//
//            // 第一个参数id随便写个数字就可以
//            notificationManager.notify(1, notification)

            // 创建一个通知(必须设置channelId)
            val channelId = "ChannelId"; // 通知渠道
            //最主要
            val intent = Intent(this, ChatActivity::class.java)
            // 标识来自状态栏 -> 用户点击的时候跳转到聊天页需要传值
            intent.putExtra("username", it.conversationId())
            // val pendingIntent = PendingIntent.getActivity(this, 1, intent,
            //     PendingIntent.FLAG_UPDATE_CURRENT)
            val taskStackBuilder = TaskStackBuilder.create(this).addParentStack(
                ChatActivity::class.java).addNextIntent(intent)
            val pendingIntent = taskStackBuilder.getPendingIntent(1,
                PendingIntent.FLAG_UPDATE_CURRENT)
            val notification = Notification.Builder(this)
                .setChannelId(channelId)
                .setSmallIcon(R.mipmap.ic_contact)
                .setContentIntent(pendingIntent)
                .setContentTitle(getString(R.string.receive_new_message))
                .setContentText(contentText)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ease_default_icon))
                .setAutoCancel(true) // 设置点击之后notification消失
                .build();

            // 下面这两句代码使用自己的铃声 对原生有用 对国产手机没有用
            // notification.sound= Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.yulu);
            // notification.defaults = Notification.DEFAULT_SOUND;

            val channel = NotificationChannel(channelId, "ChannelId",
                NotificationManager.IMPORTANCE_HIGH)
            // 设置不显示角标（国内的主流app在安卓系统上都不显示角标例如微信）
            channel.setShowBadge(false)
            notificationManager.createNotificationChannel(channel)

            // 发送通知(Notification与NotificationManager的channelId必须对应)
            notificationManager.notify(1, notification)
        }
    }

    /**
     * 获取全局上下文方法
     */
    companion object {
        lateinit var instance: IMApplication
        // var instant:Application? = null
        // fun getContext():Context{
        //     return instant!!
        // }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        // 注释原因测试代码
        // EaseUI.getInstance().init(applicationContext, null);

        // 初始化环信sdk
        EMClient.getInstance().init(applicationContext, EMOptions())

        // 在做打包混淆时，要关闭debug模式，避免消耗不必要的资源
        // EMClient.getInstance().setDebugMode(true)
        EMClient.getInstance().setDebugMode(BuildConfig.DEBUG)

        // Bmob没有使用的原因是版本太低
        // 指定的Android SDK构建工具版本（26.0.1）被忽略，因为它低于Android Gradle Plugin 3.1.4支持的最低版本
        // （27.0.3）
        // 实例化bmob 第一个参数是上下文 第二个参数是Application ID:
        // Bmob.initialize(applicationContext,"863b889ac932802a9adb395d0fe47024")

        // 消息监听器用于播放短信铃声，app前台和后台声音不一样 新SDK在这里写没有用有bug
        EMClient.getInstance().chatManager().addMessageListener(messageListener)

        // 2020年新版本解决第一条消息没有声音的bug 先让它（音量为0）播放一下
        soundPool.play(duan, 0f, 0f, 0, 0, 1f)
        soundPool.play(yulu, 0f, 0f, 0, 0, 1f)
    }

    /**
     * 判断app是在前台还是在后台的方法 这种写法不能用
     */
//    @SuppressLint("ServiceCast")
//    private fun isForeground() : Boolean {
//        val activityManager = getSystemService(Context.ACCOUNT_SERVICE) as ActivityManager
//        for (runningAppProcess in activityManager.runningAppProcesses) {
//            if (runningAppProcess.processName == packageName) {
//                // 说明找到了app的进程 当相等是在前台运行
//                return runningAppProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
//            }
//        }
//        return false
//    }

    /**
     * 2020年新版本判断app是在前台还是在后台的方法
     */
    @SuppressLint("WrongConstant")
    fun isForeground(): Boolean {
        val am = getSystemService("activity") as ActivityManager
        val tasks = am.getRunningTasks(1)
        if (!tasks.isEmpty()) {
            val topActivity = (tasks[0] as RunningTaskInfo).topActivity
            if (topActivity.packageName == packageName) {
                Log.d("App", "App前台运行")
                return true
            }
        }
        Log.d("App", "App后台运行")
        return false
    }
}