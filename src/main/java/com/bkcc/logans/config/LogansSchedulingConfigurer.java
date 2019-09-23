package com.bkcc.logans.config;

import com.bkcc.logans.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.Task;

/**
 * 【描 述】：日志定时分析任务配置
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-09-20 15:56
 */
@Slf4j
@Configuration
public class LogansSchedulingConfigurer implements SchedulingConfigurer {

    /**
     * 【描 述】：日志分析任务配置表业务接口
     *
     *  @since 2019/9/20 16:14
     */
    @Autowired
    private TaskService taskService;

    @Value("task.pool-size")
    private Integer pollSize;

    private ScheduledTaskRegistrar taskRegistrar;

    /**
     * Callback allowing a {@link TaskScheduler
     * TaskScheduler} and specific {@link Task Task}
     * instances to be registered against the given the {@link ScheduledTaskRegistrar}
     *
     * @param taskRegistrar the registrar to be configured.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(pollSize);
        taskScheduler.initialize();
        taskRegistrar.setTaskScheduler(taskScheduler);
        this.taskRegistrar = taskRegistrar;
    }

    public ScheduledTaskRegistrar getTaskRegistrar() {
        return taskRegistrar;
    }
}///:~
