package com.bkcc.logans.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bkcc.logans.entity.FieldEntity;
import com.bkcc.logans.entity.TaskEntity;
import com.bkcc.logans.service.FieldService;
import com.bkcc.logans.service.GetAnsLogService;
import com.bkcc.logans.util.ElasticSearchUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
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
     * 【描 述】：日志分析模块字段表业务接口
     *
     * @since 2019/9/23 14:08
     */
    @Autowired
    private FieldService fieldService;

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
        List<FieldEntity> fieldList = fieldService.selectFieldListByTaskId(taskEntity.getId());
        String[] source = new String[fieldList.size()];
        for (int i = 0; i < fieldList.size(); i++) {
            source[i] = fieldList.get(i).getFieldKey();
        }
        String url = ElasticSearchUtils.getSimpleLogEsUrl(taskEntity.getModuleName(), esIp, esPort);
        int from = 0, size = 10000;
        Map<String, Object> paramMap = ElasticSearchUtils.createParam(taskEntity, source);
        paramMap.put("from", from);
        paramMap.put("size", size);
        PageInfo<JSONObject> page = ElasticSearchUtils.querySimpleLogList(url, paramMap);
        long total = page.getTotal();
        if (total <= size) {
            return page.getList();
        }
        List<JSONObject> list = page.getList();
        long times = total % size == 0 ? total / size : total / size + 1;
        for (int i = 1; i < times; i++) {
            paramMap.put("from", size * i);
            page = ElasticSearchUtils.querySimpleLogList(url, paramMap);
            list.addAll(page.getList());
        }
        return list;
    }


}///:~
