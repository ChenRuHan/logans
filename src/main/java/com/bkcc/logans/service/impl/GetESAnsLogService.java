package com.bkcc.logans.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bkcc.logans.entity.TaskEntity;
import com.bkcc.logans.service.GetAnsLogService;
import com.bkcc.logans.util.ElasticSearchUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 【描 述】：通过HBase获取分析日志接口
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-10-17 15:03
 */
@Service
public class GetESAnsLogService implements GetAnsLogService {


    @Value("${elastic-search.ip}")
    private String esIp;

    @Value("${elastic-search.port}")
    private int esPort;


    /**
     * 【描 述】：获取需要分析的日志集合
     *
     * @param taskEntity
     * @return
     * @author 陈汝晗
     * @since 2019/10/17 15:02
     */
    @Override
    public List<JSONObject> getAnsLogList(TaskEntity taskEntity) {
        String url = ElasticSearchUtils.getSimpleLogEsUrl(taskEntity.getModuleName(), esIp, esPort);
        Map<String, Object> paramMap = ElasticSearchUtils.createParam(taskEntity);
        List<JSONObject> list = ElasticSearchUtils.querySimpleLogList(url, paramMap);
        return list;
    }
}///:~
