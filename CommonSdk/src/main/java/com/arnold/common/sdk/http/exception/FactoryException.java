package com.arnold.common.sdk.http.exception;

import android.text.TextUtils;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;

public class FactoryException {
    private static final String HttpException_MSG = "网络错误";
    private static final String ConnectException_MSG = "连接失败";
    private static final String JSONException_MSG = "fastjeson解析失败";
    private static final String UnknownHostException_MSG = "无法解析该域名";

    public static ApiException analysisExcetpion(Throwable throwable) {
        ApiException apiException = new ApiException(throwable);

        if (throwable instanceof HttpException) {
            /*网络异常*/
            apiException.setCode(CodeException.HTTP_ERROR);
            apiException.setDisplayMessage(HttpException_MSG);
        } else if (throwable instanceof NetApiException) {
            NetApiException e = (NetApiException) throwable;
            apiException.setCode(e.code);
            if (e.msg != null) {
                apiException.setDisplayMessage(e.msg);
            } else {
                apiException.setDisplayMessage("请求失败，请稍后重试");
            }
        } else if (throwable instanceof RuntimeException) {
            /*自定义运行时异常*/
            apiException.setCode(CodeException.RUNTIME_ERROR);
            if (!TextUtils.isEmpty(throwable.getMessage())) {
                apiException.setDisplayMessage(throwable.getMessage());
            } else {
                apiException.setDisplayMessage("请求失败，请稍后重试");
            }
        } else if (throwable instanceof ConnectException
                || throwable instanceof SocketTimeoutException) {
            /*链接异常*/
            apiException.setCode(CodeException.HTTP_ERROR);
            apiException.setDisplayMessage(ConnectException_MSG);
        } else if (throwable instanceof JSONException || throwable instanceof ParseException) {
            /*Gson解析异常*/
            apiException.setCode(CodeException.JSON_ERROR);
            apiException.setDisplayMessage(JSONException_MSG);
        } else if (throwable instanceof UnknownHostException) {
            /*无法解析该域名异常*/
            apiException.setCode(CodeException.UNKOWNHOST_ERROR);
            apiException.setDisplayMessage(UnknownHostException_MSG);
        } else {
            /*未知异常*/
            apiException.setCode(CodeException.UNKNOWN_ERROR);
            if (!TextUtils.isEmpty(throwable.getMessage())) {
                apiException.setDisplayMessage(throwable.getMessage());
            } else {
                apiException.setDisplayMessage("未知异常");
            }

        }
        return apiException;

    }
}
