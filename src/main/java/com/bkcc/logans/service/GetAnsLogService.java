package com.bkcc.logans.service;

import com.alibaba.fastjson.JSONObject;
import com.bkcc.logans.entity.TaskEntity;

import java.util.List;

/**
 * 【描 述】：获取分析日志接口
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-10-17 15:01
 */
public interface GetAnsLogService {

    /**
     * 【描 述】：获取需要分析的日志集合
     *
     * @param taskEntity
     * @return
     * @author 陈汝晗
     * @since 2019/10/17 15:02
     */
    List<JSONObject> getAnsLogList(TaskEntity taskEntity);

}///:~
