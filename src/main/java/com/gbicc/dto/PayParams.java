package com.gbicc.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


/**
 * @author wangrangrang
 * @Date 2019/4/17
 * @mail wangrangrang@yunzhichong.com
 * @desc 封装支付参数
 */
@Data
public class PayParams {

    @NotBlank(message = "用户唯一标识不能为空")
    private String uuid;
    @Min(value=0,message="支付金额不能低于0")
    private int money;

    private int payMethod;
    @NotBlank(message = "订单号不能为空")
    private String sn;


}
