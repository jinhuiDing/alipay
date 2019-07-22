package com.gbicc.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;




@Data
@Component
public class AliPayConfig {

    /**
     * AppID
     */
    @Value("${alipay.app_id}")
    public  String appId;

    /**
     * 商户的私钥
     */
    @Value("${alipay.merchant_private_key}")
	public  String privateKey;


    /**
     * 支付宝的公钥，无需修改该值
     */
    @Value("${alipay.alipay_public_key}")
    public  String aliPublicKey;

    /**
     * 异步回调地址
     */
    @Value("${alipay.notify_url}")
    public  String notifyUrl;

    /**
     *
     */
    @Value("${alipay.return_url}")
    public  String returnUrl;

    /**
     * 签名方式 不需修改
     */
    @Value("${alipay.sign_type}")
    public  String signType;


    /**
     * 字符编码格式 目前支持 gbk 或 utf-8
     */
    @Value("${alipay.charset}")
    public  String charset;


    /**
     * 支付宝网观关不需修改
     */
    @Value("${alipay.gatewayUrl}")
    public  String gatewayUrl;

    /**
     *卖家支付宝账号
     */
    @Value("${alipay.seller_email}")
    public String sellerEmail;


}
