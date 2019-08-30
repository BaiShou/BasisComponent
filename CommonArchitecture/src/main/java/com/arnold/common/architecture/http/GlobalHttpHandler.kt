package com.arnold.common.architecture.http

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 处理HTTP请求和响应结果的处理类
 */
interface GlobalHttpHandler {

    /**
     * 这里可以先客户端一步拿到每一次 Http 请求的结果, 可以先解析成 Json, 再做一些操作, 如检测到 token 过期后
     * 重新请求 token, 并重新执行同步请求
     *
     * Request newRequest = chain.request().newBuilder().header("token", newToken).build();
     */
    fun onHttpResultResponse(
        httpResult: String,
        chain: Interceptor.Chain,
        response: Response
    ): Response

    /**
     *
     *
     *
     *
     * 这里可以在请求服务器之前拿到 [Request], 做一些操作比如给 [Request] 统一添加 token 或者 header 以及参数加密等操作
     */
    fun onHttpRequestBefore(chain: Interceptor.Chain, request: Request): Request


    /**
     *
     *
     *  这里可以对响应参数进行拦截处理（比如统一拦截错误，把请求码不是200的都全部抛错，统一进行拦截处理）
     *
     *
     *
    String response = value.string();


    BaseResp httpStatus = gson.fromJson(response, BaseResp.class);

    //验证status返回是否为1
    if (httpStatus.status != 1) {
    value.close();
    //不为-1，表示响应数据不正确，抛出异常
    throw new NetApiException(httpStatus.status, httpStatus.message);
    }


    MediaType contentType = value.contentType();
    Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
    InputStream inputStream = new ByteArrayInputStream(response.getBytes());
    Reader reader = new InputStreamReader(inputStream, charset);
    JsonReader jsonReader = gson.newJsonReader(reader);

    try {
    return adapter.read(jsonReader);
    } finally {
    value.close();
    }
     */
    fun <T : Any> onHttpInterceptResponse(): T


}