package com.xuleyan.frame.common.exception;

public class BQException extends RuntimeException {

    public BQException(Throwable e){
        super(e);
    }

    public BQException(String message){
        super(message);
    }

    public BQException(String errCode, String errMsg){
        this(errCode + "\n" + errMsg);
    }

    public BQException(String message, Throwable cause) {
        super(message, cause);
    }

    public static BQException instance(EnumSupport errEnum) {
        return new BQException(errEnum.getCode(), errEnum.getMessage());
    }

    public static BQException instance(EnumSupport errEnum, String message) {
        return new BQException(errEnum.getCode(), errEnum.getMessage() + "\n" + message);
    }
}
