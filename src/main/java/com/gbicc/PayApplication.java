package com.gbicc;

import com.alipay.api.DefaultAlipayClient;
import com.gbicc.conf.AliPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @Description: PayApplication
 * @Author: ding
 * @Date: 2019/7/12 9:31
 * @Version: 1.0
 */

@SpringBootApplication
public class PayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
    }

    @Bean
    public DefaultAlipayClient createAlipayClient(@Autowired AliPayConfig aliPayConfig) {
        return new DefaultAlipayClient(aliPayConfig.getGatewayUrl(), aliPayConfig.getAppId(), aliPayConfig.getPrivateKey(), "json", aliPayConfig.getCharset(), aliPayConfig.getAliPublicKey(), aliPayConfig.getSignType());
    }
}
