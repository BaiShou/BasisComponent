package com.arnold.common.architecture.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.arnold.common.architecture.base.App
import com.arnold.common.architecture.di.component.AppComponent
import com.arnold.common.architecture.integration.AppManager
import okhttp3.MediaType
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and


/**
 * 获取AppComponent
 */
fun Context.obtainAppComponentFromContext(): AppComponent {
    Preconditions.checkState(
        applicationContext is App,
        "%s must be implements %s",
        applicationContext.javaClass.name,
        App::class.java.getName()
    )
    return (applicationContext as App).getAppComponent()
}

/**
 * 获得屏幕的宽度
 *
 * @return
 */
fun Context.getScreenWidth(): Int {
    return resources.displayMetrics.widthPixels
}

/**
 * 获得屏幕的高度
 *
 * @return
 */
fun Context.getScreenHeidth(): Int {
    return resources.displayMetrics.heightPixels
}

/**
 * 获得颜色
 */
fun Context.getColorId(rid: Int): Int {
    return resources.getColor(rid)
}

/**
 * 获得颜色
 */
fun Context.getColorName(colorName: String): Int {
    return getColorId(resources.getIdentifier(colorName, "color", packageName)
    )
}


/**
 * MD5
 *
 */
fun String.encodeToMD5(): String {
    try {
        val instance: MessageDigest = MessageDigest.getInstance("MD5")//获取md5加密对象
        val digest: ByteArray = instance.digest(toByteArray())//对字符串加密，返回字节数组
        var sb = StringBuffer()
        for (b in digest) {
            var i: Int = b.toInt() and 0xff//获取低八位有效值
            var hexString = Integer.toHexString(i)//将整数转化为16进制
            if (hexString.length < 2) {
                hexString = "0" + hexString//如果是一位的话，补0
            }
            sb.append(hexString)
        }
        return sb.toString()

    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }

    return ""
}

/**
 * 全屏,并且沉侵式状态栏
 *
 * @param activity
 */
fun Activity.statuInScreen() {
    val attrs = window.attributes
    attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
    window.attributes = attrs
    window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
    window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
}

/**
 * 移除孩子
 *
 * @param view
 */
fun View.removeChild(){
    if (parent is ViewGroup) {
        (parent as ViewGroup).removeView(this)
    }
}


/**
 * 执行 [AppManager.killAll]
 */
fun killAll() {
    AppManager.getAppManager().killAll()
}

/**
 * 执行 [AppManager.appExit]
 */
fun exitApp() {
    AppManager.getAppManager().appExit()
}