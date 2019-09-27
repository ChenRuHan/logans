package com.bkcc.logans.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 【描 述】：测试监听类
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-09-27 10:03
 */
@Component
public class TestListener {

    @JmsListener(destination = "logans_test_2", containerFactory = "queueListenerFactory")
    public void test1(String json) {
        System.out.println("logans_test_2==========收到消息开始");
        System.out.println("logans_test_2==>"+json);
        System.out.println("logans_test_2==========收到消息结束");
    }

    @JmsListener(destination = "logans_test_1", containerFactory = "queueListenerFactory")
    public void test2(String json) {
        System.out.println("logans_test_1==========收到消息开始");
        System.out.println("logans_test_1==>"+json);
        System.out.println("logans_test_1==========收到消息结束");
    }

}///:~
