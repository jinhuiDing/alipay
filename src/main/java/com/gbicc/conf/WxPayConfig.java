package com.gbicc.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class WxPayConfig {
	@Value("${wxpay.app_id}")
	private String appId;

	@Value("${wxpay.mch_id}")
	private String mchId;

	@Value("${wxpay.key}")
	private String key;

	@Value("${wxpay.package_sign}")
	private String packageSign;

	@Value("${wxpay.refund_url}")
	private String refundUrl;

	@Value("${wxpay.web_root_url}")
	private String webRootUrl;

	@Value("${wxpay.transfer_url}")
	private String transferUrl;
}
