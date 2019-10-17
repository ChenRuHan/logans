package com.bkcc.logans.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bkcc.hbase.util.HBaseUtil;
import com.bkcc.logans.entity.TaskEntity;
import com.bkcc.logans.entity.hbase.AnsLogHbaseEntity;
import com.bkcc.logans.repository.hbase.AnsLogRepository;
import com.bkcc.logans.service.GetAnsLogService;
import com.bkcc.logans.util.EncryptAndDecryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 【描 述】：通过HBase获取分析日志接口
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-10-17 15:03
 */
@Service
public class GetHbaseAnsLogService implements GetAnsLogService {

    /**
     * 【描 述】：HBase需要分析的日志数据表接口
     *
     * @since 2019/10/17 15:00
     */
    @Autowired
    private AnsLogRepository ansLogRepository;

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
        String encrypt = EncryptAndDecryptUtil.encrypt(taskEntity.getModuleName() + taskEntity.getReqMethod() + taskEntity.getReqUri());
        String beginTime = taskEntity.getBeginTime().replaceAll("\\D", "") + "000";
        String endTime = taskEntity.getEndTime().replaceAll("\\D", "") + "000";
        String random = HBaseUtil.getRandomNum(4);
        String beginRowKey = encrypt + "-" + beginTime + "-0000";
        String endRowKey = encrypt + "-" + endTime + "-9999";
        List<AnsLogHbaseEntity> list = ansLogRepository.list(beginRowKey, endRowKey);
        List<JSONObject> returnList = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return returnList;
        }
        for (AnsLogHbaseEntity vo : list) {
            JSONObject json = JSONObject.parseObject(vo.getData());
            JSONObject argJson = json.getJSONObject("args");
            returnList.addAll(getArgsList(argJson, null));
            json.remove("args");
            returnList.add(json);
        }
        return returnList;
    }

    private List<JSONObject> getArgsList(JSONObject arg, String pex) {
        List<JSONObject> list = new ArrayList<>();
        for (String key : arg.keySet()) {
            String value = arg.getString(key);
            try {
                JSONObject jsonObject = JSONObject.parseObject(value);
                if (StringUtils.isNotBlank(pex)) {
                    list.addAll(getArgsList(jsonObject, pex + "." + key));
                } else {
                    list.addAll(getArgsList(jsonObject, key));
                }
            } catch (Exception e) {
                JSONObject child = new JSONObject();
                if (StringUtils.isNotBlank(pex)) {
                    child.put(pex + "." + key, value);
                } else {
                    child.put(key, value);
                }
                list.add(child);
            }
        }
        return list;
    }


}///:~
