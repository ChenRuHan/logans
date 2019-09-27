package com.bkcc.logans.actuator;

import com.alibaba.fastjson.JSONObject;
import com.bkcc.hbase.util.HBaseUtil;
import com.bkcc.logans.actuator.abs.AbstractTaskActuator;
import com.bkcc.logans.entity.FieldEntity;
import com.bkcc.logans.entity.FieldEnumEntity;
import com.bkcc.logans.entity.TaskEntity;
import com.bkcc.logans.entity.TaskResEntity;
import com.bkcc.logans.entity.hbase.AnsResHbaseEntity;
import com.bkcc.logans.entity.hbase.QueryResHbaseEntity;
import com.bkcc.logans.enums.CalendarEnum;
import com.bkcc.logans.repository.hbase.AnsResRepository;
import com.bkcc.logans.repository.hbase.QueryResRepository;
import com.bkcc.logans.service.FieldService;
import com.bkcc.logans.service.TaskResService;
import com.bkcc.logans.util.ElasticSearchUtils;
import com.bkcc.logans.util.EncryptAndDecryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 【描 述】：默认任务执行器
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-09-21 10:16
 */
@Slf4j
@Component
@Scope("prototype")
public class LogansTaskActuator extends AbstractTaskActuator {

    /**
     * 【描 述】：消息通知
     *
     * @since Aug 16, 2019 v1.0
     */
    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    /**
     * 【描 述】：日志分析任务结果表业务接口
     *
     * @since 2019/9/23 14:07
     */
    @Autowired
    private TaskResService taskResService;

    /**
     * 【描 述】：日志分析模块字段表业务接口
     *
     * @since 2019/9/23 14:08
     */
    @Autowired
    private FieldService fieldService;
    @Autowired
    private QueryResRepository queryResRepository;
    @Autowired
    private AnsResRepository ansResRepository;

    @Value("${elastic-search.ip}")
    private String ip;

    @Value("${elastic-search.port}")
    private int port;

    /**
     * 【描 述】：任务执行方法
     *
     * @return void
     * @author 陈汝晗
     * @since 2019/9/21 10:17
     */
    @Override
    public Object execute() {
        TaskEntity taskEntity = getTaskEntity();
        List<FieldEntity> fieldList = fieldService.selectFieldListByTaskId(getTaskId());
        if (fieldList == null || fieldList.isEmpty()) {
            log.debug("# 没有需要分析的列,直接返回, taskId:{}", taskEntity.getId());
            return null;
        }
        String reverseOrderNO = StringUtils.reverse(getOrderNO()+"");
        String url = ElasticSearchUtils.getSimpleLogEsUrl(taskEntity.getModuleName(), ip, port);
        Map<String, Object> paramMap = ElasticSearchUtils.createParam(taskEntity);
        List<JSONObject> list = ElasticSearchUtils.querySimpleLogList(url, paramMap);
        List<JSONObject> returnJsonList = new ArrayList<>();
        List<QueryResHbaseEntity> taskResHbaseList = new ArrayList<>();
        Map<String, JSONObject> value2JsonMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonObject = list.get(i);
            StringBuffer fieldSb = new StringBuffer();
            JSONObject json = new JSONObject();
            for (FieldEntity fieldEntity : fieldList) {
                String key = fieldEntity.getFileKey();
                if (!jsonObject.containsKey(key)) {
                    continue;
                }
                Object value = jsonObject.get(key);
                if (value == null) {
                    value = "";
                }
                List<FieldEnumEntity> fieldEnumList = fieldEntity.getFieldEnumList();
                if (fieldEnumList != null && !fieldEnumList.isEmpty()) {
                    for (FieldEnumEntity fieldEnumEntity : fieldEnumList) {
                        if (value.toString().matches(fieldEnumEntity.getAllowRegex())) {
                            value = fieldEnumEntity.getEnumValue();
                            break;
                        }
                    }
                }
                json.put(key, value);
                fieldSb.append(value);
            }
            json.put("_count", 1);
            returnJsonList.add(json);
            QueryResHbaseEntity taskResHbaseEntity = new QueryResHbaseEntity();
            String rowKey = EncryptAndDecryptUtil.encrypt(fieldSb.toString());
            value2JsonMap.put(rowKey, json);
            taskResHbaseEntity.setRowKey(reverseOrderNO + "-" + rowKey + "-" + HBaseUtil.fillKey(i, 6));
            taskResHbaseEntity.setR("1");
            taskResHbaseList.add(taskResHbaseEntity);
        }
        queryResRepository.save(taskResHbaseList);
        if (taskEntity.getAnsType() == 1) {
            return returnJsonList;
        }

        for (String key : value2JsonMap.keySet()) {
            String beginRow = reverseOrderNO + "-" + key + "-000000";
            String endRow = reverseOrderNO + "-" + key + "-999999";
            long c = queryResRepository.count(beginRow, endRow);
            JSONObject json = value2JsonMap.get(key);
            json.put("_count", c);
        }

        return value2JsonMap.values();
    }

    /**
     * 【描 述】：将结果保存到数据库，并通过消息队列输出
     *
     * @param res
     * @param e
     * @return void
     * @author 陈汝晗
     * @since 2019/9/21 12:06
     */
    @Override
    public void afterExecute(Object res, Exception e) {
        if (res == null) {
            res = new Object();
        }
        TaskEntity taskEntity = getTaskEntity();
        /*
            通过mq通知分析结果
         */
        try {
            if (StringUtils.isNotBlank(taskEntity.getOutQueue())) {
                Destination destination = new ActiveMQQueue(taskEntity.getOutQueue());
                jmsTemplate.convertAndSend(destination, JSONObject.toJSONString(res));
            }
        } catch (Exception e1) {
            log.error(e1.getMessage(), e1);
        }
        /*
            持久化分析结果到数据库
         */
        try {
            recordResult(taskEntity, res, e);
        } catch (Exception e1) {
            log.error(e1.getMessage(), e1);
        }
    }

    /**
     * 【描 述】：执行任务前解析任务的开始/结束时间
     *
     * @return void
     * @author 陈汝晗
     * @since 2019/9/21 12:06
     */
    @Override
    public boolean preExecute() {
        TaskEntity taskEntity = getTaskEntity();
        if (taskEntity == null) {
            return false;
        }
        Integer ansRateType = taskEntity.getAnsRateType();
        if (ansRateType == null) {
            return false;
        }
        try {
            Calendar cal = Calendar.getInstance();
            FastDateFormat fastDateFormat;
            if (CalendarEnum.MINUTE.equels(ansRateType)) {
                fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:00");
                cal.add(Calendar.MINUTE, -1);
            } else if (CalendarEnum.HOUR.equels(ansRateType)) {
                fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:00:00");
                cal.add(Calendar.HOUR_OF_DAY, -1);
            } else if (CalendarEnum.DAY.equels(ansRateType)) {
                fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd 00:00:00");
                cal.add(Calendar.DAY_OF_MONTH, -1);
            } else if (CalendarEnum.MONTH.equels(ansRateType)) {
                fastDateFormat = FastDateFormat.getInstance("yyyy-MM-01 00:00:00");
                cal.add(Calendar.MONTH, -1);
            } else {
                return false;
            }
            taskEntity.setBeginTime(fastDateFormat.format(cal.getTime()));
            taskEntity.setEndTime(getEndTimeByBeginTime(cal, taskEntity.getAnsRateType()));
        } catch (Exception e) {
            log.error("# 异常：执行任务前解析任务的开始/结束时间==>{}", e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * 【描 述】：计算分析数据的结束时间
     *
     * @param cal
     * @param ansRateType
     * @return java.lang.String
     * @author 陈汝晗
     * @since 2019/9/23 14:04
     */
    private String getEndTimeByBeginTime(Calendar cal, Integer ansRateType) {
        if (CalendarEnum.MINUTE.equels(ansRateType)) {
            cal.add(Calendar.MINUTE, 1);
        } else if (CalendarEnum.HOUR.equels(ansRateType)) {
            cal.add(Calendar.HOUR_OF_DAY, 1);
        } else if (CalendarEnum.DAY.equels(ansRateType)) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        } else if (CalendarEnum.MONTH.equels(ansRateType)) {
            cal.add(Calendar.MONTH, 1);
        }
        return FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
    }

    /**
     * 【描 述】：记录分析结果
     *
     * @param taskEntity
     * @param res
     * @param e
     * @return void
     * @author 陈汝晗
     * @since 2019/9/23 14:03
     */
    private void recordResult(TaskEntity taskEntity, Object res, Exception e) {
        TaskResEntity taskRes = new TaskResEntity();
        taskRes.setTaskId(taskEntity.getId());
        taskRes.setOrderNO(getOrderNO());
        taskRes.setBeginTime(getExeBeginTime());
        taskRes.setEndTime(getExeEndTime());
        taskRes.setErrorCode(0);
        if (e != null) {
            taskRes.setErrorCode(500);
        }
        taskResService.insertOrUpdate(taskRes);

        AnsResHbaseEntity ansResHbaseEntity = new AnsResHbaseEntity();
        ansResHbaseEntity.setRowKey(StringUtils.reverse(getOrderNO()+""));
        ansResHbaseEntity.setRes(JSONObject.toJSONString(res));
        ansResRepository.save(ansResHbaseEntity);
    }
}///:~

