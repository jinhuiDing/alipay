package com.gbicc.vo;

import lombok.Data;

/**
 * @Description: WebPayRequestParams
 * @Author: ding
 * @Date: 2019/7/15 21:44
 * @Version: 1.0
 */

@Data
public class WebPayRequestParams {

    private String outTradeNo; //订单号
    private String subject; //订单名称
    private String totalAmount;
    private String refundReason;
    private String tradeNo; //支付宝交易号
    private String body;

}
