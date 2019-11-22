package com.bkcc.logans.handler;

import com.bkcc.core.util.UniqueIdUtil;
import com.bkcc.logans.actuator.abs.AbstractTaskActuator;
import com.bkcc.logans.constant.TaskConstant;
import com.bkcc.logans.dispatch.abs.AbstractTaskDispatch;
import com.bkcc.logans.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 【描 述】：任务处理器基类
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-09-21 10:10
 */
@Slf4j
public class TaskHandler implements Runnable{

    /**
     * 【描 述】：执行任务方法
     *
     * @param
     * @return void
     * @author 陈汝晗
     * @since 2019/9/21 10:46
     */
    @Override
    public void run() {
        Long taskId = actuator.getTaskId();
        if (!taskDispatch.canExecute(taskId)) {
            log.info("# 本次任务不执行. taskId:{}", taskId);
            return;
        }
        Object res = null;
        Exception e1 = null;
        Long tl = System.currentTimeMillis();
        Long taskNO = UniqueIdUtil.genId();
        try {
            actuator.setOrderNO(taskNO);
            actuator.setExeBeginTime(DateTimeUtils.formatDate());
            actuator.preExecute();
            log.debug("# begin执行任务taskNO:{}, {}", taskNO, TaskConstant.TASK_MAP.get(taskId));
            res = actuator.execute();
            log.debug("# end执行任务taskNO:{}, success, taskId:{}, 耗时:{}毫秒", taskNO, taskId, (System.currentTimeMillis() - tl));
            actuator.setExeEndTime(DateTimeUtils.formatDate());
            actuator.afterExecuteSuccess(res);
        } catch (Exception e) {
            log.error("# end执行任务taskNO{},taskId:{},异常:{}", taskNO, taskId, e.getMessage(), e);
            log.debug("# end执行任务taskNO:{}, fail, taskId:{}, 耗时:{}毫秒, errorMsg:{}", taskNO, taskId, (System.currentTimeMillis() - tl), e.getMessage(), e);
            actuator.setExeEndTime(DateTimeUtils.formatDate());
            actuator.afterExecuteError(e);
            e1 = e;
        }
        actuator.afterExecute(res, e1);
        taskDispatch.afterExecute(taskId);
    }


    /**
     * 【描 述】：任务执行器
     *
     *  @since 2019/9/21 10:25
     */
    private AbstractTaskDispatch taskDispatch;

    /**
     * 【描 述】：任务执行器
     *
     *  @since 2019/9/21 10:25
     */
    private AbstractTaskActuator actuator;

    /**
     * 【描 述】：注入任务执行器
     *
     * @param actuator 任务执行器
     * @author 陈汝晗
     * @since 2019/9/21 10:25
     */
    public TaskHandler(AbstractTaskDispatch taskDispatch, AbstractTaskActuator actuator) {
        this.actuator = actuator;
        this.taskDispatch = taskDispatch;
    }

    /**
     * 【描 述】：获取任务执行器
     *
     * @return com.bkcc.logans.actuator.abs.AbstractTaskActuator
     * @author 陈汝晗
     * @since 2019/9/21 10:23
     */
   public AbstractTaskActuator getActuator(){
       return actuator;
   }


}///:~
