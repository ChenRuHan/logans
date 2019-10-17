package com.bkcc.logans.actuator;

import com.bkcc.logans.actuator.abs.AbstractTaskActuator;
import com.bkcc.logans.config.LogansSchedulingConfigurer;
import com.bkcc.logans.constant.RedisKeyConstant;
import com.bkcc.logans.dispatch.abs.AbstractTaskDispatch;
import com.bkcc.logans.entity.TaskEntity;
import com.bkcc.logans.enums.AnsTypeEnum;
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private static Map<Long, ScheduledTask> SCHEDULED_TASK_MAP = new HashMap<>();


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
        /*
            添加新任务
         */
        Set<Long> taskIdSet = new HashSet<>();
        for (TaskEntity taskEntity : getTaskList()) {
            if (taskEntity == null) {
                continue;
            }
            Long taskId = taskEntity.getId();
            insertToRedis(taskEntity);
            if (AnsTypeEnum.COMPARE_ANS.equels(taskEntity.getAnsType())) {
                continue;
            }

            taskIdSet.add(taskId);
            if (SCHEDULED_TASK_MAP.containsKey(taskId)) {
                continue;
            }
            AbstractTaskActuator actuator = applicationContext.getBean("logansTaskActuator", AbstractTaskActuator.class);
            actuator.setTaskEntity(taskEntity);
            TaskHandler taskHandler = new TaskHandler(taskDispatch, actuator);
            CronTask cronTask = new CronTask(new Thread(taskHandler), taskEntity.getAnsCronByAnsRateType());
            ScheduledTask scheduledTask = taskRegistrar.scheduleCronTask(cronTask);
            SCHEDULED_TASK_MAP.put(taskId, scheduledTask);
        }
        /*
            去掉多余任务
         */
        for (Long taskId : SCHEDULED_TASK_MAP.keySet()) {
            if (!taskIdSet.contains(taskId)) {
                log.debug("# 移除已经删除的任务：taskId: {}", taskId);
                redisUtil.hmDel(RedisKeyConstant.TASK_KEY, taskId);
                SCHEDULED_TASK_MAP.get(taskId).cancel();
            }
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
    private List<TaskEntity> getTaskList(){
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
        // 不需要定时分析，直接对比
        String key = taskEntity.getReqMethod() + taskEntity.getReqUri();
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
