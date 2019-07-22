package com.gbicc.enums;

public enum ResultEnums {

    SERVER_ERROR(-1,"系统内部错误"),

    RETURN_SUCCESS(200,"SUCCESS"),
    ERROR_PAY(100,"支付失败"),
    ERROR_TOTAL_AMOUNT(102,"充值金额不匹配"),
    ERROR_SELLER_EMAIL(103,"商家账户不正确"),
    ERROR_APP_ID(104,"商家appId不正确"),
    ERROR_PAY_WAY(105,"支付方式不正确"),

    ERROR_OUT_TRADE_NO(101,"订单号不存在");



    private Integer code;

    private String msg;

    ResultEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}


