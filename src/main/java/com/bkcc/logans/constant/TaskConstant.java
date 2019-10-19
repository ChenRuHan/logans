package com.bkcc.logans.constant;

import com.bkcc.logans.entity.TaskEntity;
import org.springframework.scheduling.config.ScheduledTask;

import java.util.HashMap;
import java.util.Map;

/**
 * 【描 述】：任务常量类
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-10-19 09:44
 */
public class TaskConstant {

    public static Map<Long, ScheduledTask> SCHEDULED_TASK_MAP = new HashMap<>();

    public static Map<Long, TaskEntity> TASK_MAP = new HashMap<>();


    private TaskConstant() {}

}///:~
