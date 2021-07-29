package com.arnold.common.network.utils

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

object LogUtil {

    var logDebug: Boolean = false
    fun init(logDebug: Boolean, tag: String = "LOGS") {
        val power = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)  //（可选）是否显示线程信息。 默认值为true
            .methodCount(0)         // （可选）要显示的方法行数。 默认2
            .methodOffset(0)        // （可选）隐藏内部方法调用到偏移量。 默认5
            .tag(tag)           //（可选）每个日志的全局标记。 默认PRETTY_LOGGER
            .build()
        this.logDebug = logDebug
        Logger.addLogAdapter(object : AndroidLogAdapter(power) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return logDebug
            }
        })
    }


    fun d(message: String) {
        Logger.d(message)
    }

    fun i(message: String) {
        Logger.i(message)
    }

    fun w(message: String, e: Throwable) {
        Logger.w("$message：$e")
    }

    fun w(message: String) {
        Logger.w(message)
    }

    fun e(message: String, e: Throwable) {
        Logger.e(e, message)
    }


    fun json(json: String) {
        Logger.json(json)
    }

}