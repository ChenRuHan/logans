package com.bkcc.logans.repository.hbase;

import com.alibaba.fastjson.JSONObject;
import com.bkcc.hbase.repository.AbstractHBaseRepository;
import com.bkcc.hbase.util.HBaseUtil;
import com.bkcc.logans.constant.TaskConstant;
import com.bkcc.logans.entity.FieldEntity;
import com.bkcc.logans.entity.FieldEnumEntity;
import com.bkcc.logans.entity.TaskEntity;
import com.bkcc.logans.entity.hbase.QueryResHbaseEntity;
import com.bkcc.logans.enums.AnsTypeEnum;
import com.bkcc.logans.enums.LogSourceEnum;
import com.bkcc.logans.service.GetAnsLogService;
import com.bkcc.logans.util.EncryptAndDecryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 【描 述】：HBASE日志查询结果接口
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 Aug 6, 2019 新建
 *  @since          Aug 6, 2019 
 */
@Repository
public class QueryResRepository extends AbstractHBaseRepository<QueryResHbaseEntity>{
    /**
     * 【描 述】：获取分析日志接口
     *
     * @since 2019/10/17 15:04
     */
    @Autowired
    @Qualifier("getHbaseAnsLogService")
    private GetAnsLogService getHbaseAnsLogService;

    /**
     * 【描 述】：获取分析日志接口
     *
     * @since 2019/10/17 15:04
     */
    @Autowired
    @Qualifier("getESAnsLogService")
    private GetAnsLogService getESAnsLogService;

    public void save(TaskEntity taskEntity, Long orderNO, List<FieldEntity> fieldList){
        List<JSONObject> ansLogList = null;

        if (LogSourceEnum.ES.equels(taskEntity.getLogSource())) {
            ansLogList = getESAnsLogService.getAnsLogList(taskEntity);
        } else if (LogSourceEnum.HBASE.equels(taskEntity.getLogSource())) {
            ansLogList = getHbaseAnsLogService.getAnsLogList(taskEntity);
        }
        if (ansLogList == null || ansLogList.isEmpty()) {
            return;
        }
        String reverseOrderNO = StringUtils.reverse(orderNO+ "");
        List<JSONObject> returnJsonList = new ArrayList<>();
        List<QueryResHbaseEntity> taskResHbaseList = new ArrayList<>();


        Map<String, JSONObject> value2JsonMap = new HashMap<>();
        for (int i = 0; i < ansLogList.size(); i++) {
            JSONObject jsonObject = ansLogList.get(i);
            StringBuffer fieldSb = new StringBuffer();
            JSONObject json = new JSONObject();
            for (FieldEntity fieldEntity : fieldList) {
                String key = fieldEntity.getFieldKey();
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
            json.put(TaskConstant.COUNT, 1);
            returnJsonList.add(json);
            QueryResHbaseEntity taskResHbaseEntity = new QueryResHbaseEntity();

            String rowKey = EncryptAndDecryptUtil.encrypt(fieldSb.toString());
            value2JsonMap.put(rowKey, json);
            if (AnsTypeEnum.COUNT_ANS_WITHOUT_REPEAT.equels(taskEntity.getAnsType())) {
                taskResHbaseEntity.setRowKey(reverseOrderNO + "-" + rowKey);
            } else if (AnsTypeEnum.COUNT_ANS.equels(taskEntity.getAnsType())) {
                taskResHbaseEntity.setRowKey(reverseOrderNO + "-" + rowKey + "-" + HBaseUtil.fillKey(i, 6));
            }
            taskResHbaseEntity.setR("1");
            taskResHbaseList.add(taskResHbaseEntity);
        }
        super.save(taskResHbaseList);
    }

}///:~
