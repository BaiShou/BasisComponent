package com.arnold.common.sdk.http.exception;

public class NetApiException extends RuntimeException {

    public int code;
    public String msg;

    public NetApiException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "NetApiException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
