package com.gbicc.pojo;

import com.alipay.api.domain.ExtendParams;
import com.alipay.api.domain.GoodsDetail;
import com.alipay.api.domain.PromoParam;
import lombok.Data;

/**
 * @Description: PayContent
 * @Author: ding
 * @Date: 2019/7/12 11:40
 * @Version: 1.0
 */


@Data
public class PayContent {

        /*
        商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
         */
    private String out_trade_no;


    /**
     * 支付场景
     * 条码支付，取值：bar_code
     * 声波支付，取值：wave_code
     */
    private String scene;

    /**
     * 支付授权码，25~30开头的长度为16~24位的数字，实际字符串长度以开发者获取的付款码长度为准
     */
    private String auth_code;


    //销售产品码
    private String product_code;

    /*
    订单标题
     */
    private String subject;

    //买家的支付宝用户id，如果为空，会从传入了码值信息中获取买家ID
    private String buyer_id;

    //如果该值为空，则默认为商户签约账号对应的支付宝用户ID
    private String seller_id;

    //订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
    //如果同时传入【可打折金额】和【不可打折金额】，该参数可以不用传入；
    //如果同时传入了【可打折金额】，【不可打折金额】，【订单总金额】三者，则必须满足如下条件：【订单总金额】=【可打折金额】+【不可打折金额】
    private double total_amount;

    //标价币种, total_amount 对应的币种单位。支持英镑：GBP、港币：HKD、美元：USD、新加坡元：SGD、日元：JPY、加拿大元：CAD、澳元：AUD、欧元：EUR、新西兰元：NZD、韩元：KRW、泰铢：THB、瑞士法郎：CHF、瑞典克朗：SEK、丹麦克朗：DKK、挪威克朗：NOK、马来西亚林吉特：MYR、印尼卢比：IDR、菲律宾比索：PHP、毛里求斯卢比：MUR、以色列新谢克尔：ILS、斯里兰卡卢比：LKR、俄罗斯卢布：RUB、阿联酋迪拉姆：AED、捷克克朗：CZK、南非兰特：ZAR、人民币：CNY
    private String trans_currency;


    //商户指定的结算币种，支持英镑：GBP、港币：HKD、美元：USD、新加坡元：SGD、日元：JPY、加拿大元：CAD、澳元：AUD、欧元：EUR、新西兰元：NZD、韩元：KRW、泰铢：THB、瑞士法郎：CHF、瑞典克朗：SEK、丹麦克朗：DKK、挪威克朗：NOK、马来西亚林吉特：MYR、印尼卢比：IDR、菲律宾比索：PHP、毛里求斯卢比：MUR、以色列新谢克尔：ILS、斯里兰卡卢比：LKR、俄罗斯卢布：RUB、阿联酋迪拉姆：AED、捷克克朗：CZK、南非兰特：ZAR、人民币：CNY
    private String settle_currency;
    //参与优惠计算的金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]。
    //如果该值未传入，但传入了【订单总金额】和【不可打折金额】，则该值默认为【订单总金额】-【不可打折金额】
    private double discountable_amount;

    //订单描述
    private String body;

    //订单包含的商品列表信息，json格式，其它说明详见商品明细说明
    private GoodsDetail[] goods_detail;
    //商户操作员编号
    private String operator_id;
    //商户门店编号
    private String store_id;
//商户机具终端编号
    private String terminal_id;

    //业务扩展参数
    private ExtendParams extend_params;

    //该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m
    private  String timeout_express;
    //预授权确认模式，授权转交易请求中传入，适用于预授权转交易业务使用，目前只支持PRE_AUTH(预授权产品码)
    //COMPLETE：转交易支付完成结束预授权，解冻剩余金额; NOT_COMPLETE：转交易支付完成不结束预授权，不解冻剩余金额
    private  String auth_confirm_mode;


//商户传入终端设备相关信息，具体值要和支付宝约定
    private String terminal_params;

    //优惠明细参数，通过此属性补充营销参数
    private PromoParam promo_params;

    //支付模式类型,若值为ENJOY_PAY_V2表示当前交易允许走先享后付2.0垫资
    private String advance_payment_type;
}
