package com.gbicc.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.gbicc.conf.AliPayConfig;
import com.gbicc.conf.WXConfig;
import com.gbicc.conf.WXUtils;
import com.gbicc.dto.PayParams;
import com.gbicc.enums.ResultEnums;
import com.gbicc.exception.AlipayException;
import com.gbicc.pojo.PayOrder;
import com.gbicc.service.PayService;
import com.gbicc.util.PayConstants;
import com.gbicc.vo.AppletResult;
import com.gbicc.vo.WebPayRequestParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.gbicc.util.PayConstants.COMMAND_ALI_PAY;

@Slf4j
@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    PayService payService;

    @Autowired
    private AliPayConfig aliPayConfig;

    private static final String ALIPAYSET = "alipay:set:";

    private static final String WXPAYSET = "wxpay:set:";


    @Autowired
    DefaultAlipayClient alipayClient;

    /**
     * 处理支付请求
     *
     * @param payParams 支付时必传的参数
     * @return 请求的封装结果
     */
    @ApiOperation(value = "app支付接口", httpMethod = "POST", response = AppletResult.class, notes = "提供用户支付功能")
    @PostMapping("/goPay")
    public AppletResult goPay(@RequestBody @Valid PayParams payParams) {
        return payService.goPay(payParams);
    }


    @ApiOperation(value = "电脑端支付接口")
    @RequestMapping(value = "webPay", method = RequestMethod.POST)
    public void webPay(@RequestBody WebPayRequestParams payRequestParams, HttpServletResponse response) throws IOException {
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl(aliPayConfig.getReturnUrl()); //支付成功之后跳转的页面
        alipayRequest.setNotifyUrl(aliPayConfig.getNotifyUrl());  //设置回调地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"" + payRequestParams.getOutTradeNo() + "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":\"" + payRequestParams.getTotalAmount() + "\"," +
                "    \"subject\":\"" + payRequestParams.getSubject() + "\"," +
                "    \"body\":\"" + "这里是订单详情" + "\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }" +
                "  }");//填充业务参数
        String form = null;
        try {
            AlipayTradePagePayResponse aliResponse = alipayClient.pageExecute(alipayRequest);
            boolean success = aliResponse.isSuccess();
            if (success) {
                form = aliResponse.getBody();
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        System.out.println("返回数据了");
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(form);//直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
    }

    @PostMapping("webRefund")
    public void webRefund(@RequestBody WebPayRequestParams payRequestParams,HttpServletResponse httpResponse) throws IOException {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent("{" +
                "    \"out_trade_no\":\""+payRequestParams.getOutTradeNo()+"\"," +  //商户订单号
                //"    \"trade_no\":\"2014112611001004680073956707\"," +  //支付宝交易号， 和商户订单号不能同时为空
                "    \"refund_amount\":"+payRequestParams.getTotalAmount()+"," +
                "    \"refund_reason\":\""+payRequestParams.getRefundReason()+"\"" +  //退款原因
              //  "    \"out_request_no\":\"HZ01RF001\"," +   //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
                //"    \"operator_id\":\"OP001\"," +
                //"    \"store_id\":\"NJ_S_001\"," +
                //"    \"terminal_id\":\"NJ_T_001\"" +
                "  }");
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setCharacterEncoding("utf-8");
//        httpResponse.setContentType("text");
        if(response.isSuccess()){

            System.out.println("调用成功");
            httpResponse.getWriter().print("退款成功，请注意查收");
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
        } else {
            System.out.println("调用失败");
            httpResponse.getWriter().print("退款失败,原因："+response.getSubMsg());
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
        }
    }

    @PostMapping("queryPay")
    public void queryPay(HttpServletResponse httpResponse,@RequestBody WebPayRequestParams params) throws IOException {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{" +
                "\"out_trade_no\":\""+params.getOutTradeNo()+"\"," +  //商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
                "\"trade_no\":\""+params.getTradeNo()+"\"" + //销售产品码，与支付宝签约的产品码名称。注：目前仅支持FAST_INSTANT_TRADE_PAY 和上一个二者需填写一个
                "    }" +
                "  }");
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setCharacterEncoding("utf-8");
        if(response.isSuccess()){

            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        httpResponse.getWriter().write(response.getBody());
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    /**
     * 输入订单信息生成二维码，用户扫描该二维码进行付款操作
     */
    @ApiOperation(value = "输入订单信息生成二维码，用户扫描该二维码进行付款操作")
    @PostMapping("aliPayTiaoMa")
    public void aliPayTiaoma(@RequestBody WebPayRequestParams payRequestParams, HttpServletResponse response, HttpServletRequest httpRequest) throws IOException {
        //创建API对应的request类
        String outTradeNo = httpRequest.getParameter("outTradeNo");
        String subject = httpRequest.getParameter("subject");
        String totalAmount = httpRequest.getParameter("totalAmount");

        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + payRequestParams.getOutTradeNo() + "\"," +   //订单编号
                "\"scene\":\"bar_code\"," +      //付款方式  条码  和声波
                //   "\"auth_code\":\"289784265564278788\"," +   //
                "\"seller_id\":\"\"," +
                "\"total_amount\":\"" + payRequestParams.getTotalAmount() + "\"," +
                //"    \"discountable_amount\":\"8.88\"," +
//                "    \"undiscountable_amount\":\"80\"," +
                //  "    \"buyer_logon_id\":\"sryaji3986@sandbox.com\"," +
                "\"subject\":\"" + payRequestParams.getSubject() + "\"" +
//                "    \"store_id\":\"NJ_001\"" +
                "}");
//通过alipayClient调用API，获得对应的response类
        try {
            AlipayTradePrecreateResponse execute = alipayClient.execute(request);
            if (execute.isSuccess()) {

                log.info("支付宝预下单成功: )");
                response.setContentType("text/html;charset=utf-8");

                //用二维码生成器生成该路径，用户就可以扫描该二维码进行支付
                response.getWriter().print(execute.getQrCode());
                response.getWriter().print("<img src=\"http://qr.liantu.com/api.php?text=" + execute.getQrCode() + "\"/>");
                response.getWriter().flush();
                response.getWriter().close();
                System.out.println("execute.getQrCode() = " + execute.getQrCode());

            } else {
                System.out.println("失败了");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //根据response中的结果继续业务逻辑处理
    }


    /**
     * 支付宝回调
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/aliPayNotice", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @ApiOperation(value = "支付宝支付回调", response = String.class, notes = "支付宝支付回调")
    public String aliPayNotice(HttpServletRequest request) {

        Map<String, String> params = new HashMap<>();
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
            boolean flag = AlipaySignature.rsaCheckV1(params, aliPayConfig.aliPublicKey, aliPayConfig.charset,
                    aliPayConfig.signType);
            if (flag) {
                log.info("支付宝回调签名认证成功");

                this.check(params, aliPayConfig.getSellerEmail(), aliPayConfig.getAppId());
                String tradeStatus = params.get("trade_status");
                String outTradeNo = params.get("out_trade_no");

                // 交易支付成功的执行相关业务逻辑
                if (PayConstants.COMMAND_TRADE_SUCCESS.equals(tradeStatus) || PayConstants.COMMAND_TRADE_FINISHED.equals(tradeStatus)) {
                    log.info("## alipayNotice ## 验签成功");
                    //处理自己的业务逻辑

                    // redisUtil.addToSet(ALIPAYSET,outTradeNo);
                } else {
                    log.error("没有处理支付宝回调业务");
                    return "failure";
                }
            } else {
                log.error("支付宝回调签名认证失败");
                return "failure";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "success";
    }

    /**
     * 在处理自己的业务逻辑之前，需要先进行校验数据的真实性
     *
     * @param params
     * @param email
     * @param id
     */
    private void check(Map<String, String> params, String email, String id) throws AlipayException {
        String outTradeNo = params.get("out_trade_no");
        String totalAmount = params.get("total_amount");
        String sellerEmail = params.get("seller_email");
        String appId = params.get("app_id");
        //1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
        PayOrder payOrder = getPayOrder(outTradeNo);
        if (StringUtils.isEmpty(payOrder)) {
            log.error("--------------error out_trade_no-----------");
            throw new AlipayException(ResultEnums.ERROR_OUT_TRADE_NO);
        }
        // 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额，
        if ((new BigDecimal(totalAmount).multiply(new BigDecimal(COMMAND_ALI_PAY))).compareTo(payOrder.getPrice()) != 0) {
            log.error("------------error total_amount-------------");
            throw new AlipayException(ResultEnums.ERROR_TOTAL_AMOUNT);
        }
        // 3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
        if (!sellerEmail.equals(email)) {
            log.error("---------------error sellerEmail--------------");
            throw new AlipayException(ResultEnums.ERROR_SELLER_EMAIL);
        }
        // 4、验证app_id是否为该商户本身。
        if (!appId.equals(id)) {
            log.error("-------------- error error app_id ----------------");
            throw new AlipayException(ResultEnums.ERROR_APP_ID);
        }

    }

    /**
     * 获取预支付订单
     *
     * @param outTradeNo
     * @return
     */
    private PayOrder getPayOrder(String outTradeNo) {
//        Map payOrderData = (Map) orderFeignClient.findPayOrder(outTradeNo).getData();
        HashMap<String, Object> payOrderData = new HashMap<>();
        return JSON.parseObject(JSON.toJSONString(payOrderData.get("common")), PayOrder.class);
    }


    @RequestMapping(value = "/wxpayNotice", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @ApiOperation(value = "微信支付回调", response = String.class, notes = "微信支付回调")
    public String wxpayNotice(HttpServletRequest request) {

        log.info("## wxpayNotice ##");


        StringBuffer xmlStr = new StringBuffer();
        String line = null;
        try {
            request.setCharacterEncoding("utf-8");
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                xmlStr.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("## wxpayNotice ## xmlStr:" + xmlStr.toString());
        try {
            Map<String, String> data = WXUtils.xmlToMap(xmlStr.toString());
            // 回调增加校验签名
            String returnCode = data.get("return_code");
            String resultCode = data.get("result_code");
            if (PayConstants.COMMAND_SUCCESS.equals(returnCode)
                    && PayConstants.COMMAND_SUCCESS.equals(resultCode)
                    && wxPayVerifySign(data)) {
                //验证返回的订单金额是否与商户侧的订单金额一致
                String outTradeNo = data.get("out_trade_no");
                String totalFee = data.get("total_fee");
                //获取预支付订单
                PayOrder payOrder = getPayOrder(outTradeNo);
                BigDecimal total = new BigDecimal(totalFee);
                BigDecimal payTotal = payOrder.getPrice();

                if (total.compareTo(payTotal) != 0) {
                    //说明支付金额和返回的金额不一致
                    log.error("## wxpayNotice ## totalFee:  返回的订单金额：" + totalFee + "  --商户侧的订单金额:" + (payOrder.getPrice().toString()));
                    return "ERROR";
                }
                //redisUtil.addToSet(WXPAYSET,outTradeNo);
                return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            } else {
                return "ERROR";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    private boolean wxPayVerifySign(final Map<String, String> data) throws Exception {
        String v = WXUtils.generateSignature(data, WXConfig.App.Key);
        return v.equals(data.get("sign"));
    }


}
