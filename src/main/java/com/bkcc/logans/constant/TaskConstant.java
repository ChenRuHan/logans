package com.bkcc.logans.constant;

import com.bkcc.logans.entity.TaskEntity;
import org.springframework.scheduling.config.ScheduledTask;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 【描 述】：任务常量类
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-10-19 09:44
 */
public class TaskConstant {

    public static Map<Long, ScheduledTask> SCHEDULED_TASK_MAP = new ConcurrentHashMap<>();

    public static Map<Long, TaskEntity> TASK_MAP = new ConcurrentHashMap<>();

    public static final String INSERT_STATUS = "insert";
    public static final String DELETE_STATUS = "delete";
    public static final String UPDATE_STATUS = "update";


    private TaskConstant() {}

}///:~
