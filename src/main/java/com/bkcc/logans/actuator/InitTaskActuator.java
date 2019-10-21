package com.bkcc.logans.actuator;

import com.bkcc.logans.actuator.abs.AbstractTaskActuator;
import com.bkcc.logans.config.LogansSchedulingConfigurer;
import com.bkcc.logans.constant.RedisKeyConstant;
import com.bkcc.logans.constant.TaskConstant;
import com.bkcc.logans.dispatch.abs.AbstractTaskDispatch;
import com.bkcc.logans.entity.TaskEntity;
import com.bkcc.logans.handler.TaskHandler;
import com.bkcc.logans.service.TaskService;
import com.bkcc.util.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 【描 述】：初始化任务执行器
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-09-21 11:08
 */
@Slf4j
@Component
public class InitTaskActuator extends AbstractTaskActuator {


    /**
     * 【描 述】：日志分析任务配置表业务接口
     *
     * @since 2019-09-20 15:23:34
     */
    @Autowired
    private TaskService taskService;
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
     * 【描 述】：执行初始化定时任务任务方法
     *
     * @return void
     * @author 陈汝晗
     * @since 2019/9/21 10:17
     */
    @Override
    public Object execute() {
        ScheduledTaskRegistrar taskRegistrar = logansSchedulingConfigurer.getTaskRegistrar();
        if (taskRegistrar == null) {
            return null;
        }
        redisUtil.remove(RedisKeyConstant.COMPARE_TASK_KEY);
        /*
            添加新任务
         */
        Set<Long> taskIdSet = new HashSet<>();
        for (TaskEntity taskEntity : getTaskList()) {
            if (taskEntity == null) {
                continue;
            }
            insertToRedis(taskEntity);

            Long taskId = taskEntity.getId();
            if (TaskConstant.TASK_MAP.containsKey(taskId) && TaskConstant.SCHEDULED_TASK_MAP.containsKey(taskId)) {
                continue;
            }
            taskIdSet.add(taskId);
            TaskConstant.TASK_MAP.put(taskId, taskEntity);
            if (taskEntity.getAnsRateType() == null) {
                continue;
            }
            AbstractTaskActuator actuator = applicationContext.getBean("logansTaskActuator", AbstractTaskActuator.class);
            actuator.setTaskId(taskId);
            TaskHandler taskHandler = new TaskHandler(taskDispatch, actuator);
            CronTask cronTask = new CronTask(new Thread(taskHandler), taskEntity.getAnsCronByAnsRateType());
            ScheduledTask scheduledTask = taskRegistrar.scheduleCronTask(cronTask);
            TaskConstant.SCHEDULED_TASK_MAP.put(taskId, scheduledTask);
        }
        /*
            去掉多余任务
         */
        Iterator<Map.Entry<Long, ScheduledTask>> it = TaskConstant.SCHEDULED_TASK_MAP.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, ScheduledTask> m = it.next();
            Long taskId = m.getKey();
            ScheduledTask task = m.getValue();
            if (!taskIdSet.contains(taskId)) {
                log.debug("# 移除已经删除的任务：taskId: {}", taskId);
                redisUtil.hmDel(RedisKeyConstant.TASK_KEY, taskId);
                task.cancel();
                it.remove();
                TaskConstant.TASK_MAP.remove(taskId);
            }
        }
        if (redisUtil.exists(RedisKeyConstant.COMPARE_TASK_KEY)) {
            redisUtil.expire(RedisKeyConstant.COMPARE_TASK_KEY, 90, TimeUnit.SECONDS);
        }
        return null;
    }

    /**
     * 【描 述】：获取需要定时分析的任务列表
     *
     * @param
     * @return java.util.List<com.bkcc.logans.entity.TaskEntity>
     * @author 陈汝晗
     * @since 2019/10/16 16:14
     */
    private List<TaskEntity> getTaskList() {
        return taskService.selectTaskList(null).getList();
    }


    /**
     * 【描 述】：将需要分析的请求加入redis。
     *
     * @param taskEntity
     * @return void
     * @author 陈汝晗
     * @since 2019/10/17 13:47
     */
    private void insertToRedis(TaskEntity taskEntity) {
        String key = taskEntity.getModuleName() + taskEntity.getReqMethod() + taskEntity.getReqUri();
        String taskIdRedis = (String) redisUtil.hmGet(RedisKeyConstant.COMPARE_TASK_KEY, key);
        if (StringUtils.isBlank(taskIdRedis)) {
            taskIdRedis = "";
        }
        taskIdRedis += "," + taskEntity.getId();
        if (taskIdRedis.startsWith(",")) {
            taskIdRedis = taskIdRedis.substring(1);
        }
        redisUtil.hmSet(RedisKeyConstant.COMPARE_TASK_KEY, key, taskIdRedis);
    }

}///:~
