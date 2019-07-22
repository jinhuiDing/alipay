package com.gbicc.conf;

/**
 * @author bianmaren
 * @description xxx
 * @mail 441889070@qq.com
 * @date 2018/4/10
 */
public interface WXConfig {

    /**
     * app微信支付常量
     */
    interface App {
       String AppID = "wx55cdec5c462c13af";
       String MchID = "1503466011";
       String Key = "12AsdAsd88888273hashdjdhASHDSHDD";
    }

    /**
     * 微信小程序支付常量
     */
    interface WxApp {
        String AppID = "wxd02fb28002c34df3";
        String MchID = "1398651802";
        String Key = "3899b0927180a8798197c6862400b2fa";
    }

}
