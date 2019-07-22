package com.gbicc.exception;


import com.gbicc.enums.ResultEnums;


public class AlipayException extends Exception {
    private Integer code;

    public AlipayException(ResultEnums resultEnums) {
        super(resultEnums.getMsg());
        this.code = resultEnums.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
