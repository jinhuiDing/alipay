package com.gbicc.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @author bianmaren
 * @description 充值订单
 * @mail 441889070@qq.com
 * @date 2018/3/19
 *  @Table(name = "t_pay_order")
 */

@Data
public class PayOrder {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyDate;
    /** 订单编号 */
    private String sn;

    /** 用户 */
    private Long memberId;

    /** 充值金额 */
    private BigDecimal price = BigDecimal.ZERO;

    /** 赠送金额 */
    private BigDecimal giveMoney = BigDecimal.ZERO;

    /** 赠送云钻 */
    private BigDecimal givePoint = BigDecimal.ZERO;

    /** 充值时当前用户充值余额 */
    private BigDecimal currentMoney = BigDecimal.ZERO;

    /** 充值时当前用户充值赠送余额 */
    private BigDecimal currentgiveMoney = BigDecimal.ZERO;

    /** 充值时当前用户云钻 */
    private BigDecimal currentPoint = BigDecimal.ZERO;

    /** 是否付款 */
    private Boolean payFlag = false;

    /** 付款时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payDate;

    /** 付款方式 */
    private String payMethod;
}


