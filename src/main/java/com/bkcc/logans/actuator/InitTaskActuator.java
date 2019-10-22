package com.bkcc.logans.actuator;

import com.alibaba.fastjson.JSONObject;
import com.bkcc.logans.actuator.abs.AbstractTaskActuator;
import com.bkcc.logans.constant.LogansMQConstant;
import com.bkcc.logans.constant.TaskConstant;
import com.bkcc.logans.entity.TaskEntity;
import com.bkcc.logans.enums.TaskStatusEnum;
import com.bkcc.logans.listener.TaskListener;
import com.bkcc.logans.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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

    /**
     * 【描 述】：消息通知
     *
     * @since Aug 16, 2019 v1.0
     */
    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    /**
     * 【描 述】：日志分析任务配置表业务接口
     *
     * @since 2019-09-20 15:23:34
     */
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskListener taskListener;

    /**
     * 【描 述】：执行初始化定时任务任务方法
     *
     * @return void
     * @author 陈汝晗
     * @since 2019/9/21 10:17
     */
    @Override
    public Object execute() {
        /*
            添加新任务
         */
        Destination destination = new ActiveMQTopic(LogansMQConstant.LOGANS_TASK);
        Map<String, TaskEntity> taskMap = new HashMap<>();
        Set<Long> taskIdSet = new HashSet<>();

        for (TaskEntity task : getTaskList()) {
            taskListener.insertTask2Redis(task);
            taskIdSet.add(task.getId());
            if (TaskConstant.TASK_MAP.containsKey(task.getId())) {
                if (TaskStatusEnum.CLOSE.equels(task.getStatus())) {
                    taskMap.put(TaskConstant.DELETE_STATUS, task);
                    jmsTemplate.convertAndSend(destination, JSONObject.toJSONString(taskMap));
                }
            } else {
                if (TaskStatusEnum.OPEN.equels(task.getStatus())) {
                    taskMap.put(TaskConstant.INSERT_STATUS, task);
                    jmsTemplate.convertAndSend(destination, JSONObject.toJSONString(taskMap));
                }
            }
        }
        /*
            去掉多余任务
         */
        Iterator<Map.Entry<Long, TaskEntity>> it = TaskConstant.TASK_MAP.entrySet().iterator();
        taskMap = new HashMap<>();
        while (it.hasNext()) {
            Map.Entry<Long, TaskEntity> m = it.next();
            Long taskId = m.getKey();
            if (!taskIdSet.contains(taskId)) {
                taskMap.put(TaskConstant.DELETE_STATUS, m.getValue());
                taskListener.insertTask2Redis(m.getValue());
            }
            jmsTemplate.convertAndSend(destination, JSONObject.toJSONString(taskMap));
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


}///:~
