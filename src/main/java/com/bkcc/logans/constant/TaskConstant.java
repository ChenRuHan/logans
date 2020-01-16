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
    public static final String COUNT = "_count";


    /**
     * 【描 述】：根据任务ID动态获取过期时间
     *
     * @param taskId
     * @return java.lang.Long
     * @author 陈汝晗
     * @since 2019/10/22 15:45
     */
    public static Long getExpireTime(Long taskId){
        TaskEntity taskEntity = TaskConstant.TASK_MAP.get(taskId);
        if (taskEntity == null || taskEntity.getAnsRateType() == null) {
            return -1L;
        }
        // 每（1--分，2--时，3--日，4--月）分析一次。
        switch (taskEntity.getAnsRateType()) {
            case 1:
                return 60 + 30L;
            case 2:
                return 60 * 60 + 30L;
            case 3:
                return 60 * 60 * 24 + 30L;
            case 4:
                return 60 * 60 * 24 * 31 + 30L;
        }
        return -1L;
    }


    private TaskConstant() {}

}///:~
