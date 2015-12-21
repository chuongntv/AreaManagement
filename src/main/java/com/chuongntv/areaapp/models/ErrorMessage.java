package com.chuongntv.areaapp.models;

/**
 * Created by chuongntv on 12/17/15.
 */
public class ErrorMessage {
    private int errorCode;
    private String content;

    public ErrorMessage(int errorCode, String content) {
        this.errorCode = errorCode;
        this.content = content;
    }

    public ErrorMessage() {

    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
