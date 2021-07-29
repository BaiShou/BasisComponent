package com.arnold.common.sdk.core

import android.content.Context
import com.arnold.common.network.http.GlobalHttpHandler
import com.arnold.common.repository.utils.DataHelper
import com.arnold.common.sdk.BuildConfig
import com.arnold.common.sdk.http.BaseResp
import com.arnold.common.sdk.http.exception.NetApiException
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import kotlin.text.Charsets.UTF_8


class GlobalHttpHandlerImpl(val context: Context) : GlobalHttpHandler {

    companion object {
        val BASE_URL_KEY = "BASE_URL"
    }

    override fun onHttpResultResponse(
        httpResult: String?,
        chain: Interceptor.Chain,
        response: Response
    ): Response {

        return response
    }

    override fun onHttpRequestBefore(chain: Interceptor.Chain, request: Request): Request {

        //替换BaseUrl
        val originalRequest = chain.request()

        val newBuilder = originalRequest.newBuilder()

        val urlStr = DataHelper.decodeString(BASE_URL_KEY)
        if (urlStr != "" && BuildConfig.DEBUG) {
            val newBaseUrl = urlStr.toHttpUrlOrNull()
            newBaseUrl?.let {
                val oldHttpUrl = request.url
                //重建新的HttpUrl，修改需要修改的url部分
                val newFullUrl = oldHttpUrl
                    .newBuilder()
                    .scheme(it.scheme)
                    .host(it.host)
                    .port(it.port)
                    .build()
                newBuilder.url(newFullUrl)
            }
        }

        return newBuilder
//            .header("Content-Type", "text/plain; charset=utf-8")
//            .header("Accept", "*/*")
            .build()
    }


    override fun <T : Any> onHttpInterceptResponse(
        adapter: TypeAdapter<T>,
        gson: Gson,
        value: ResponseBody
    ): T {
        val response = value.string()
        val baseResp = gson.fromJson<BaseResp<*>>(response, BaseResp::class.java)

        if (!baseResp.result) {
            value.close();
            //表示响应数据不正确，抛出异常
            throw  NetApiException(baseResp.resultCode, baseResp.errorMsg)
        }

        val contentType = value.contentType()
        val charset = if (contentType != null) contentType.charset(UTF_8) else UTF_8

        val inputStream = ByteArrayInputStream(response.toByteArray())

        val reader = InputStreamReader(inputStream, charset)

        val jsonReader = gson.newJsonReader(reader)

        value.use {
            return adapter.read(jsonReader)
        }
    }

}