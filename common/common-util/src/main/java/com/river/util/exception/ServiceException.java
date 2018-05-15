package com.river.util.exception;

import com.river.util.res.ResCodeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * create by river  2018/5/15
 * desc:
 */
public class ServiceException extends RuntimeException{

    private static final long serialVersionUID = 3233232323L;

    private Map<String,String> messageMap;
    private String code;
    private String message;
    private Exception originException;

    public ServiceException(ResCodeEnum resCodeEnum) {
        this.code = resCodeEnum.getResCode();
        this.message = resCodeEnum.getResMsg();
    }


    public ServiceException(String message) {
        this.message = message;
    }

    public ServiceException(String code,String message) {
        this.code = code;
        this.message = message;
    }

    public ServiceException(String message,Exception originException) {
        this.message = message;
        this.originException = originException;
    }

    public ServiceException(String code,String message,Exception originException) {
        this.code = code;
        this.message = message;
        this.originException = originException;
    }

    public ServiceException(String code,String message,HashMap<String,String> messageMap){
        this.code = code;
        this.message = message;
        this.messageMap = messageMap;
    }

    public ServiceException(HashMap<String,String> messageMap,String message,String code,Exception originException) {
        this.messageMap = messageMap;
        this.message = message;
        this.code = code;
        this.originException = originException;
    }


    public Map<String, String> getMessageMap() {
        return messageMap;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Exception getOriginException() {
        return originException;
    }
}
