package com.bkcc.logans.handler;

import com.bkcc.logans.actuator.abs.AbstractTaskActuator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 【描 述】：任务初始化处理器
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-09-20 17:16
 */
@Component
public class InitTaskHandler {

    @Autowired
    @Qualifier("initTaskActuator")
    private AbstractTaskActuator actuator;

    /**
     * 【描 述】：初始化任务
     *
     * @return void
     * @author 陈汝晗
     * @since 2019/9/20 17:19
     */
    public void initTask() {
        actuator.execute();
    }

}///:~
