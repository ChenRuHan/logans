package com.bkcc.logans.actuator.abs;

import com.bkcc.logans.entity.TaskEntity;
import com.bkcc.logans.interceptor.inf.TaskExecuteInterceptor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 【描 述】：任务执行器基础类
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-09-21 10:12
 */
@Data
@Slf4j
public abstract class AbstractTaskActuator implements TaskExecuteInterceptor {
    /**
     * 【描 述】：任务单次执行单号
     *
     *  @since 2019/9/23 16:03
     */
    private Long orderNO;
    /**
     * 【描 述】：任务执行开始时间
     *
     *  @since 2019/9/23 11:28
     */
    private String exeBeginTime;

    /**
     * 【描 述】：任务执行结束时间
     *
     *  @since 2019/9/23 11:29
     */
    private String exeEndTime;

    /**
     * 【描 述】：执行方法前是否拦截
     *
     * @return void
     * @author 陈汝晗
     * @since 2019/9/21 12:06
     */
    @Override
    public boolean preExecute() {
        return true;
    }

    /**
     * 【描 述】：方法执行成功之后执行
     *
     * @return void
     * @author 陈汝晗
     * @since 2019/9/21 12:06
     */
    @Override
    public void afterExecuteSuccess(Object res) {}

    /**
     * 【描 述】：方法执行异常之后执行
     *
     * @param e
     * @return void
     * @author 陈汝晗
     * @since 2019/9/21 12:06
     */
    @Override
    public void afterExecuteError(Exception e) {}

    /**
     * 【描 述】：方法执行后执行，无论结果成功或者失败
     *
     * @return void
     * @author 陈汝晗
     * @since 2019/9/21 12:06
     */
    @Override
    public void afterExecute(Object res, Exception e) {}

    /**
     * 【描 述】：执行任务实体类
     *
     *  @since 2019/9/21 12:38
     */
    private TaskEntity taskEntity;

    /**
     * 【描 述】：执行任务方法
     *
     * @param
     * @return void
     * @author 陈汝晗
     * @since 2019/9/21 10:17
     */
    public abstract Object execute();

    /**
     * 【描 述】：获取当前执行任务ID
     *
     * @param
     * @return java.lang.Long
     * @author 陈汝晗
     * @since 2019/9/21 12:37
     */
    protected Long getTaskId(){
        return taskEntity != null ? taskEntity.getId() : null;
    }

}///:~
