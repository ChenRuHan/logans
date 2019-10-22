package com.bkcc.logans.listener;

import com.alibaba.fastjson.JSONObject;
import com.bkcc.logans.actuator.abs.AbstractTaskActuator;
import com.bkcc.logans.config.LogansSchedulingConfigurer;
import com.bkcc.logans.constant.LogansMQConstant;
import com.bkcc.logans.constant.RedisKeyConstant;
import com.bkcc.logans.constant.TaskConstant;
import com.bkcc.logans.dispatch.abs.AbstractTaskDispatch;
import com.bkcc.logans.entity.TaskEntity;
import com.bkcc.logans.handler.TaskHandler;
import com.bkcc.util.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 【描 述】：任务列表监听类
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-09-27 10:03
 */
@Component
public class TaskListener {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private LogansSchedulingConfigurer logansSchedulingConfigurer;

    @Autowired
    @Qualifier("pollTaskDispatch")
    private AbstractTaskDispatch taskDispatch;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 【描 述】：监听任务状态
     *
     * @param taskMap
     * @return void
     * @author 陈汝晗
     * @since 2019/10/22 10:01
     */
    @JmsListener(destination = LogansMQConstant.LOGANS_TASK, containerFactory = "queueListenerFactory")
    public void task(String str) {
        if (StringUtils.isBlank(str)) {
            return;
        }
        JSONObject json = JSONObject.parseObject(str);
        removeTask(json.getObject(TaskConstant.DELETE_STATUS, TaskEntity.class));
        updateTask(json.getObject(TaskConstant.UPDATE_STATUS, TaskEntity.class));
        insertTask(json.getObject(TaskConstant.INSERT_STATUS, TaskEntity.class));
    }

    /**
     * 【描 述】：修改任务
     *
     * @param task
     * @return void
     * @author 陈汝晗
     * @since 2019/10/22 09:59
     */
    private void updateTask(TaskEntity task) {
        removeTask(task);
        insertTask(task);
    }

    /**
     * 【描 述】：添加任务
     *
     * @param task
     * @return void
     * @author 陈汝晗
     * @since 2019/10/22 09:52
     */
    private void insertTask(TaskEntity task) {
        if (task == null) {
            return;
        }
        Long taskId = task.getId();
        TaskConstant.TASK_MAP.put(taskId, task);
        insertTask2Redis(task);
        if (task.getAnsRateType() == null) {
            return;
        }
        ScheduledTaskRegistrar taskRegistrar = logansSchedulingConfigurer.getTaskRegistrar();
        if (taskRegistrar == null) {
            return;
        }
        AbstractTaskActuator actuator = applicationContext.getBean("logansTaskActuator", AbstractTaskActuator.class);
        actuator.setTaskId(taskId);
        TaskHandler taskHandler = new TaskHandler(taskDispatch, actuator);
        CronTask cronTask = new CronTask(new Thread(taskHandler), task.getAnsCronByAnsRateType());
        ScheduledTask scheduledTask = taskRegistrar.scheduleCronTask(cronTask);
        TaskConstant.SCHEDULED_TASK_MAP.put(taskId, scheduledTask);
    }

    /**
     * 【描 述】：删除任务
     *
     * @param task
     * @return void
     * @author 陈汝晗
     * @since 2019/10/22 09:51
     */
    private void removeTask(TaskEntity task) {
        if (task == null) {
            return;
        }
        Long taskId = task.getId();
        removeTask2Redis(task);
        TaskConstant.TASK_MAP.remove(taskId);
        ScheduledTask scheduledTask = TaskConstant.SCHEDULED_TASK_MAP.remove(taskId);
        if (scheduledTask == null) {
            return;
        }
        scheduledTask.cancel();
    }


    /**
     * 【描 述】：将需要分析的请求加入redis。
     *
     * @param taskEntity
     * @return void
     * @author 陈汝晗
     * @since 2019/10/17 13:47
     */
    private void insertTask2Redis(TaskEntity taskEntity) {
        String key = taskEntity.getModuleName() + taskEntity.getReqMethod() + taskEntity.getReqUri();
        String taskId = taskEntity.getId() + "";
        String value = (String) redisUtil.hmGet(RedisKeyConstant.LOGANS_ANS, key);
        if (StringUtils.isBlank(value)) {
            value = taskId;
        } else {
            value += "," + taskId;
        }
        redisUtil.hmSet(RedisKeyConstant.LOGANS_ANS, key, value);
    }


    /**
     * 【描 述】：将需要分析的请求移出redis。
     *
     * @param taskEntity
     * @return void
     * @author 陈汝晗
     * @since 2019/10/17 13:47
     */
    private void removeTask2Redis(TaskEntity taskEntity) {
        String key = taskEntity.getModuleName() + taskEntity.getReqMethod() + taskEntity.getReqUri();
        String taskId = taskEntity.getId() + "";
        String value = (String) redisUtil.hmGet(RedisKeyConstant.LOGANS_ANS, key);
        if (StringUtils.isBlank(value)) {
            return;
        }
        Set<String> idSet = new HashSet<>(Arrays.asList(value.split(",")));
        if (!idSet.contains(taskId)) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (String s : idSet) {
            if (StringUtils.equals(s, taskId)) {
                continue;
            }
            sb.append(",").append(s);
        }
        if (StringUtils.isBlank(sb)) {
            redisUtil.hmDel(RedisKeyConstant.LOGANS_ANS, key);
        }
        redisUtil.hmSet(RedisKeyConstant.LOGANS_ANS, key, sb.substring(1));
    }


}///:~
