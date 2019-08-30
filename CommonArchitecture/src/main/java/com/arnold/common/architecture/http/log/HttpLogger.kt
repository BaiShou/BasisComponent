package com.arnold.common.architecture.http.log

import android.text.TextUtils
import com.arnold.common.architecture.utils.JsonUtil
import com.arnold.common.architecture.utils.LogUtil
import okhttp3.logging.HttpLoggingInterceptor

class HttpLogger : HttpLoggingInterceptor.Logger {

    private val mMessage = StringBuilder()
    override fun log(message: String) {
        if (TextUtils.isEmpty(message)) {
            LogUtil.i("请求日志null")
            return
        }

        if (message.startsWith("--> POST")) {
            mMessage.setLength(0)
        }

        if (message.startsWith("{") && message.endsWith("}") ||
            message.startsWith("[") && message.endsWith("]")
        ) {
            mMessage.append(JsonUtil.formatJson(JsonUtil.decodeUnicode(message)))

        } else {
            mMessage.append(message)
        }
        mMessage.append("\n")

        // 响应结束，打印整条日志
        if (message.startsWith("<-- END HTTP")) {
            LogUtil.d(mMessage.toString())
        }
    }
}