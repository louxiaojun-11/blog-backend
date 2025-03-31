package com.lxj.myblog.exception;

public class AccountAlreadyExistException extends BaseException{
    public AccountAlreadyExistException() {
    }

    public AccountAlreadyExistException(String msg) {
        super(msg);
    }
}
