package com.web.chen.exception;

import com.web.chen.entities.ExcelContent;

/**
 * Created by CHEN on 2017/1/11.
 */
public class ExcelException extends RuntimeException{

    public ExcelException(String detailMessage) {
        super(detailMessage);
    }

    public ExcelException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
