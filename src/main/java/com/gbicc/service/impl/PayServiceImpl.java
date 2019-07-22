package com.gbicc.service.impl;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.gbicc.conf.AliPayConfig;
import com.gbicc.conf.WxPayConfig;
import com.gbicc.dto.PayParams;
import com.gbicc.enums.ResultEnums;
import com.gbicc.service.PayService;
import com.gbicc.util.*;
import com.gbicc.vo.AppletResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class PayServiceImpl implements PayService {


    @Autowired
    private WxPayConfig wxPayConfig;

    @Autowired
    private AliPayConfig aliPayConfig;


    /**
     * @param payParams 需要的参数
     * @return
     */
    @Override
    public AppletResult goPay(PayParams payParams) {

        try {
            //获取支付方式
            int payMethod = payParams.getPayMethod();
            String uuid = payParams.getUuid();
            int money = payParams.getMoney();
            String sn = payParams.getSn();

            switch (payMethod) {
                //支付宝APP
                case 0:
                    return aliAppPay(uuid, money, sn);
                //微信APP
                case 1:
                    return wxAppPay(uuid, money, sn);
                default:
                    return ResultUtil.error(ResultEnums.SERVER_ERROR, "支付方式有误。。。");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error(ResultEnums.SERVER_ERROR, "支付方式有误。。。");
    }


    //@SuppressWarnings("all")

    /**
     * 微信支付
     * @param uuid
     * @param money
     * @param sn
     * @return
     */
    private AppletResult wxAppPay(String uuid, int money, String sn){
        log.info("## wxAppPay ## uuid:" + uuid);
        log.info("## wxAppPay ## money:" + money);
        log.info("## wxAppPay ## money:" + sn);

        try {
            if (money<0) {
                log.error("## wxAppPay ## money lt 0");
                //充值金额为空
                return ResultUtil.error(ResultEnums.ERROR_PAY, "充值金额不能小于0");
            }

            // 预下单
            String prepayId = null;
            Map<String, String> m = this.getPrePayId(money, sn, wxPayConfig.getAppId(), wxPayConfig.getMchId(), wxPayConfig.getKey());

            String resultCode = m.get("result_code");
            log.info("## wxAppPay ## resultCode:" + resultCode);

            if (PayConstants.COMMAND_SUCCESS.equals(resultCode)) {
                //预支付单号
                prepayId = m.get("prepay_id");
                log.info("## wxAppPay ## prepayId:" + prepayId);
            } else {
                log.error("微信预下单有误" + m.get("err_code") + "_" + m.get("err_code_des"));
                return ResultUtil.error(ResultEnums.ERROR_PAY, "微信预下单有误" + m.get("err_code") + "_" + m.get("err_code_des"));
            }
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("appid", wxPayConfig.getAppId());
            hashMap.put("partnerid", wxPayConfig.getMchId());
            hashMap.put("package", wxPayConfig.getPackageSign());
            hashMap.put("prepayid", prepayId);
            hashMap.put("noncestr", m.get("nonce_str"));
            hashMap.put("timestamp", SysCuTimeSecOfCNUtils.getSysCuTimeSecOfCN() + "");

            String formatUrlMap = MapToUrlUtils.formatUrlMap(hashMap, false,
                    false) + "&key=" + wxPayConfig.getKey();
            String sign = MD5Utils.md5(formatUrlMap);
            hashMap.put("sign", sign);
            hashMap.put("rechargeId", sn + "");
            hashMap.put("aliPay", "");

            return ResultUtil.success(ResultEnums.RETURN_SUCCESS, hashMap);

        } catch (Exception e) {
            log.error("## wxAppPay ## 微信支付异常:" + e.getMessage());
            return ResultUtil.error(ResultEnums.ERROR_PAY);
        }

    }

    //@SuppressWarnings("all")
    //@Transactional

    /**
     * 支付宝支付
     * @param uuid  用户唯一标识
     * @param money 金额
     * @param sn 订单号
     * @return 支付结果
     */
    private AppletResult aliAppPay(String uuid, int money, String sn) {
        log.info("## alipay ## uuid:" + uuid);
        log.info("## alipay ## money:" + money);
        log.info("## alipay ## sn:" + sn);


        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(aliPayConfig.getGatewayUrl(), aliPayConfig.appId, aliPayConfig.getPrivateKey(), "json", aliPayConfig.charset, aliPayConfig.aliPublicKey, aliPayConfig.signType);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("智慧养老支付项目");  //设置订单详情
        model.setSubject("智慧养老支付服务费");  //设置订单标题
        model.setOutTradeNo(sn); //设置订单号
        model.setTimeoutExpress("30m");  //设置超时时间
        model.setTotalAmount((money/100.0) + "");   //设置金额
        model.setProductCode("QUICK_MSECURITY_PAY"); //设置产品编号
        request.setBizModel(model);
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());   //设置回调路径
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("appid", "");
            hashMap.put("partnerid", "");
            hashMap.put("package", "");
            hashMap.put("prepayid", "");
            hashMap.put("noncestr", "");
            hashMap.put("timestamp", "");
            hashMap.put("sign", "");
            hashMap.put("rechargeId","");
            hashMap.put("aliPay", response.getBody());
            return ResultUtil.success(ResultEnums.RETURN_SUCCESS, hashMap);
        } catch (AlipayApiException e) {
            log.error("## alipay ## 支付宝支付失败:" + e.getMessage());
            e.printStackTrace();
        }
        log.error("## alipay ## 支付宝支付失败:");
        return ResultUtil.error(ResultEnums.ERROR_PAY );
    }


    /**
     * 获取预支付订单号
     * @param money
     * @param rechargeId
     * @param appId
     * @param mchId
     * @param key
     * @return
     */
    private Map<String, String> getPrePayId(int money, String rechargeId, String appId, String mchId, String key) {

        try {
            Map<String, String> m = new HashMap<>();
            m.put("appid", appId);
            m.put("mch_id", mchId);
            m.put("nonce_str", UUidUtil.getUuid());
            m.put("body", "云智充");
            m.put("out_trade_no", rechargeId);
            //以分为单位
            m.put("total_fee", money+"");
            m.put("spbill_create_ip", "172.0.0.1");
            m.put("notify_url", wxPayConfig.getWebRootUrl());
            m.put("trade_type", "APP");
            String stringA = MapToUrlUtils.formatUrlMap(m, false, false);
            // 注：key为商户平台设置的密钥key
            String stringSignTemp = stringA + "&key=" + key;
            String sign = MD5Utils.md5(stringSignTemp);
            m.put("sign", sign);
            return WxUtils.wxUrl("https://api.mch.weixin.qq.com/pay/unifiedorder", m);
        } catch (Exception e) {
            log.error("###########      获取预支付订单号失败  ###########");
            e.printStackTrace();
        }

        return null;

    }
}
