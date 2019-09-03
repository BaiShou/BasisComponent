package com.arnold.common.sdk.http.exception;

public class ApiException extends Exception {

    private int code = 0;
    private String displayMessage = null;

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause, @CodeException.CodeEp int code) {
        super(message, cause);
        setCode(code);
        setDisplayMessage(message);
    }

    @CodeException.CodeEp
    public int getCode() {
        return code;
    }

    public void setCode(@CodeException.CodeEp int code) {
        this.code = code;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }
}
