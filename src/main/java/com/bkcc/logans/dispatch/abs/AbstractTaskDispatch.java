package com.bkcc.logans.dispatch.abs;

/**
 * 【描 述】：分布式任务调度器基类
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-09-21 10:04
 */
public abstract class AbstractTaskDispatch {

    /**
     * 【描 述】：根据自定义规则判断是否可以执行此任务
     *
     * @param
     * @return boolean
     * @author 陈汝晗
     * @since 2019/9/21 10:20
     */
    public abstract boolean canExecute(Long taskId);

    /**
     * 【描 述】：执行完毕之后执行
     *
     * @param taskId
     * @return void
     * @author 陈汝晗
     * @since 2019/10/22 14:10
     */
    public void afterExecute(Long taskId){}

}///:~
