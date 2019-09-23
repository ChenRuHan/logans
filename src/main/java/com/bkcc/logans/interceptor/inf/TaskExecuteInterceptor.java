package com.bkcc.logans.interceptor.inf;

/**
 * 【描 述】：任务执行拦截器
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-09-21 10:12
 */
public interface TaskExecuteInterceptor {

    /**
     * 【描 述】：执行方法前拦截
     *
     * @return void
     * @author 陈汝晗
     * @since 2019/9/21 12:06
     */
    boolean preExecute();

    /**
     * 【描 述】：方法执行成功之后执行
     *
     * @param res 结果返回值
     * @return void
     * @author 陈汝晗
     * @since 2019/9/21 12:06
     */
    void afterExecuteSuccess(Object res);

    /**
     * 【描 述】：方法执行异常之后执行
     *
     * @param e 异常信息
     * @return void
     * @author 陈汝晗
     * @since 2019/9/21 12:06
     */
    void afterExecuteError(Exception e);

    /**
     * 【描 述】：方法执行后执行，无论结果成功或者失败
     *
     * @param res 执行返回结果
     * @param e 异常信息
     * @return void
     * @author 陈汝晗
     * @since 2019/9/21 12:06
     */
    void afterExecute(Object res, Exception e);

}///:~
