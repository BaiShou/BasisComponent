package com.arnold.common.architecture.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.arnold.common.architecture.base.App
import com.arnold.common.architecture.di.component.AppComponent
import com.arnold.common.architecture.integration.AppManager
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


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
    return getColorId(
            resources.getIdentifier(colorName, "color", packageName)
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
fun View.removeChild() {
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


fun Context.getProcessName(): String {
    val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager? ?: return ""
    val runningApps = am.runningAppProcesses ?: return ""
    for (proInfo in runningApps) {
        if (proInfo.pid === android.os.Process.myPid()) {
            if (proInfo.processName != null) {
                return proInfo.processName
            }
        }
    }
    return ""
}


fun Context.getHeightInPx(): Float {
    return resources.displayMetrics.heightPixels.toFloat()
}

fun Context.getWidthInPx(): Float {
    return resources.displayMetrics.widthPixels.toFloat()
}

fun Context.getHeightInDp(): Int {
    val height = resources.displayMetrics.heightPixels.toFloat()
    return px2dip(height)
}

fun Context.getWidthInDp(): Int {
    val height = resources.displayMetrics.heightPixels.toFloat()
    return px2dip(height)
}

fun Context.dip2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun Context.px2dip(pxValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

fun Context.px2sp(pxValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

fun Context.sp2px(spValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (spValue * scale + 0.5f).toInt()
}
