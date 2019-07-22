package cn.demo;

import com.alipay.api.AlipayClient;
import com.gbicc.controller.IndexController;

/**
 * @Description: Test
 * @Author: ding
 * @Date: 2019/7/12 11:23
 * @Version: 1.0
 */


public class Test {

    @org.junit.Test
    public void demo() {
        IndexController indexController = new IndexController();
        AlipayClient alipayClient = indexController.builderClient();
    }
}
