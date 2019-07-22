package com.gbicc.service;


import com.gbicc.dto.PayParams;
import com.gbicc.vo.AppletResult;


public interface PayService {

    /**
     * 去支付
     * @param payParams 支付参数
     * @return 产生的字符串结果
     */
    AppletResult goPay(PayParams payParams);
}
