package com.gbicc.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.GoodsDetail;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @Description: IndexController
 * @Author: ding
 * @Date: 2019/7/12 9:42
 * @Version: 1.0
 */

@Controller

public class IndexController {
    @Autowired
    private AlipayClient alipayClient;

//    @Autowired
//    DefaultAlipayClient alipayClient;


    @RequestMapping("index")
    public String index(Model model) {
        model.addAttribute("name", "名字");
        return "index";
    }

    @RequestMapping("webPay")
    public String webPay(HttpServletRequest request) {
        return "webPay";
    }

    @RequestMapping("tiaoma")
    public String tiaoma(HttpServletRequest request) {
        return "tiaoma";
    }

    @RequestMapping("queryPay")
    public String queryPay(HttpServletRequest request) {
        return "queryPay";
    }

    @RequestMapping("webRefund")
    public String webRefund(HttpServletRequest request) {
        return "webRefund";
    }



    @PostMapping("payByApi")
    public String payByApi(@RequestParam("money") Double money,@RequestParam("something") String something ,HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        System.out.println("money = " + money);
        //生成订单，调用阿里api
        AlipayClient client = builderClient();
        AlipayTradePagePayRequest request = builderRequest();
        AlipayTradePayModel model = new AlipayTradePayModel();

        model.setBody("云智充-充值");
        model.setSubject("云智充-充值");
        model.setOutTradeNo("dingdanhao123");
        model.setTimeoutExpress("30m");
        model.setTotalAmount((money/100.0) + "");
        model.setProductCode("QUICK_MSECURITY_PAY");
        GoodsDetail goodsDetail = new GoodsDetail();
        goodsDetail.setBody("商品1");
        goodsDetail.setPrice("123");
        ArrayList<GoodsDetail> goodsDetails = new ArrayList<>();
        model.setGoodsDetail(goodsDetails);
        request.setBizModel(model);
       /* request.setBizContent("{\"out_trade_no\":\""+ "123456789"+(Math.random()*1000) +"\","
                + "\"total_amount\":\""+ money +"\","
                + "\"subject\":\""+ something +"\","
                + "\"body\":\""+ "五详细描述" +"\","
               // + "\"buyer_id\":\""+ "sryaji3986@sandbox.com" +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");*/
        try {
            AlipayTradePagePayResponse alipayTradePagePayResponse = client.sdkExecute(request);
//            AlipayTradePagePayResponse alipayTradePagePayResponse = client.sdkExecute(request);
            String body = alipayTradePagePayResponse.getBody();
            System.out.println("body = " + body);
            // String response = client.pageExecute(request).getBody();
            try {
                httpResponse.setContentType("text/html;charset=" + "utf-8");
                httpResponse.getWriter().print(body);
                httpResponse.getWriter().flush();
                httpResponse.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
//        AlipayResponse alipayResponse = client.sdkExecute();
//
//        String body = alipayResponse.getBody();
        return null;
    }
    private AlipayTradePagePayRequest builderRequest() {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        return  request;
    }

    public AlipayClient builderClient() {
//        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");

       // AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", "2016092800613850", "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCpj7V6j0N4xGlHg238TpzLOHQYsgshbUSGVrY7jQdfgl724sykn386zhRWbYjRm4EhRI826hNppeiTcuCSYUm59BTx60bFw+50Jr/7FO7J+r0QbNG7V4zpMZLDK/YhOWhDlnbTeHfS2hpM2p1T6vMvwJ9vLbSeRNA+QIUKZsf0dqIY/NXELBTjkrx5YidDDyS65V5VDgiVq2SdIdrf/7LpGJe1aUMoEhEDkRX2D5IKFTNyNp4AVY+b30iwtDwQyNKYs+aj1cImY9ZD8XVZ1hXaxRruMnSF2SYVM6a5TURoSY/MvGNzWOA47npZZ+gQr4g/hdii2nOPwJIdkstRG+W1AgMBAAECggEAaCB+e/M6phFH7cjCZC0KkKnscbX0q+RkLnNVmaBzpK11D/wbXAUuprgjOSgu2zm5xOpLYJfCsCBYgSQM0Re5pN5FdoLbhVtbNi0NvG0QbyzxqgKJkZikShhNXlnC3ohYhRkDg735J50UhZnC/+PKz8pknmvc/UyY7YHFsauIo5Cp2ZH/lfQ54TKVuIqvSNTlTMLN0in0qwlBR2aSi8BqooKcj52rWRegofR1JWwVX+RQo9TylhKbUrbzFs6bMiTt0ooEwi0xkNkfZdR2JnDFA6Qr06YscJBz/FeMhnL21h/SyYragdmtbbUf5H05yITmGaLKT3Z7enSIp8QraOoigQKBgQD+cZp0wCPtQ7kmilTcNAX2V5q79cITsjwD6RxBlN0NMGi/9yvlNK3uTFnRXc0FQsPa5F1yIcjmLRPLaJ6xzo62vQ7aSZ4CFf8i0lv7Cy+76htoJFktw3q7ZrokDpsfYHfz7r/EZEHPHmMWJjrfk4yGrxWQl8HmJl9LEGUMLaxZ8QKBgQCqmTNTmDdUAGe4VshXpMUUrequ296e2EdQnmVUlMnIjDdmJimNlsH/nncSw68pymyA5NRipIsZ1XhxZqgOe+GXTPVdfZWmtZS/3eXanjFxGpLT+w53LG/rGkgk+9apIFrYbT+z7k4RTGXGlGn3+kN+WTuz5ZZNDxs2NLm0N1xkBQKBgBNOsYa8cb12Tx1ij8/pzq4DtcUff+uYsb8mODOtxRxUGQ7ABb+twKj9eC9P69fDWjNI8uJPN/xnXUIpHSmm3+Nz8yWTQn8sBiExTzN8hNHAkPXVFENJuw1vXR6RUSoPWurSqhCP/wJlbuVmXz9J5fbuYDeJnHgHX2HFy7Uh3hsxAoGARAFwOXmrTJwgvJYzpOR0/8pYjD3vn6idvSB3SmFJkt16VAkQdXXMfeagluVU1TbxS+xsUapAHRtAuQvwIF33zsWJ5ivi1SL7WgTN1PlMwkaSk1g4Kv+W2cSPT9boemEAddgPFd4AVtr6k0+kcorcDujpaMwzvyIvectI3/NVrDUCgYA8oGRthW1WRsJYvC9bTM3oJowsTx4Mwl2t3ybNAbgU65Wymng8CeNz7Z1sgsAjN80j6tezpbk2cr6/8wWJfaOrGqFepJ6J32vn6qeUEilJfOa4zJdGC0HaJbpDaKfLRBhpkpuRP/Pvr52AQmCZqSuXqXHcNysQmZtTQqB9dSoKWw==", "json", "utf-8", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqY+1eo9DeMRpR4Nt/E6cyzh0GLILIW1Ehla2O40HX4Je9uLMpJ9/Os4UVm2I0ZuBIUSPNuoTaaXok3LgkmFJufQU8etGxcPudCa/+xTuyfq9EGzRu1eM6TGSwyv2ITloQ5Z203h30toaTNqdU+rzL8Cfby20nkTQPkCFCmbH9HaiGPzVxCwU45K8eWInQw8kuuVeVQ4IlatknSHa3/+y6RiXtWlDKBIRA5EV9g+SChUzcjaeAFWPm99IsLQ8EMjSmLPmo9XCJmPWQ/F1WdYV2sUa7jJ0hdkmFTOmuU1EaEmPzLxjc1jgOO56WWfoEK+IP4XYotpzj8CSHZLLURvltQIDAQAB", "RSA2");

        return alipayClient;
    }




    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @ApiOperation(value = "支付宝支付回调", response = String.class, notes = "支付宝支付回调")
    public String aliPayNotice(HttpServletRequest request) {

        Map<String, String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        try {
            boolean flag = AlipaySignature.rsaCheckV1(params, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvalKOEm8NC5D8zkk9UltwOrK1tDCmY5moMS8/h7T7rd4A5OqLXtDaarIYupM78Sg2fP7ql8OWP0TWRNmnTp+YbRD85UlFvHMZcuDBDMOJH2Jq61irQnxlyIL75uHW/YpiYWmdYvYWZu48jAwX4o+2YVrNyQIt1f8+4tEpEpcQfZMRaFBLKWJLZaaKMyjpa56WvIq1fz4qG1VTHMLPrs+RJZtj/szSpKsmkkQPAyfHDq3tchERl1UHdn4h1zjLxxtcN5zXDFvPXtixychBG5g7ZZh+h+ALAGSbliboTEXTljP8BonsNSMepNEYr7/zsxqi8YKQUb1ISNO9n8lce+hywIDAQAB", "utf-8",
                    "RSA2");
            if (flag) {
                System.out.println("支付宝回调签名认证成功");

                String tradeStatus = params.get("trade_status");
                String outTradeNo = params.get("out_trade_no");

                // 交易支付成功的执行相关业务逻辑
                /*if (PayConstants.COMMAND_TRADE_SUCCESS.equals(tradeStatus) || PayConstants.COMMAND_TRADE_FINISHED.equals(tradeStatus)) {
                    log.info("## alipayNotice ## 验签成功");
                    //处理自己的业务逻辑

                    redisUtil.addToSet(ALIPAYSET,outTradeNo);
                } else {
                    log.error("没有处理支付宝回调业务");
                    return "failure";
                }*/
            } else {
                System.out.println("支付宝回调签名认证失败");
                return "failure";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "success";
    }
}
