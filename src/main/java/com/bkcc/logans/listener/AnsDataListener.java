package com.bkcc.logans.listener;

import com.alibaba.fastjson.JSONObject;
import com.bkcc.hbase.util.HBaseUtil;
import com.bkcc.logans.entity.hbase.AnsLogHbaseEntity;
import com.bkcc.logans.repository.hbase.AnsLogRepository;
import com.bkcc.logans.util.EncryptAndDecryptUtil;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 【描 述】：监听需要分析的数据
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-10-16 15:46
 */
@Component
public class AnsDataListener {

    /**
     * 【描 述】：HBase需要分析的日志数据表接口
     *
     *  @since 2019/10/17 14:12
     */
    @Autowired
    private AnsLogRepository ansLogRepository;


    /**
     * 【描 述】：监听需要分析的数据
     *
     * @param jsonStr：
            "moduleName": moduleName,
            "method": method,
            "uri": uri,
            "uid": uid,
            "startTimeMillis": startTimeMillis,
            "stopTimeMillis": stopTimeMillis,
            "resultStatus": isSuccess,
            "resultObj": result,
            "taskIds": taskIds,
            "args": {
                'paramName1' : {obj1},
                'paramName2' : {obj2},
            }
     * @return void
     * @author 陈汝晗
     * @since 2019/10/17 13:51
     */
    @JmsListener(destination = "logans_log", containerFactory = "queueListenerFactory")
    public void getNewData(String jsonStr) {
        JSONObject json = JSONObject.parseObject(jsonStr);
        AnsLogHbaseEntity ansLogHbaseEntity = new AnsLogHbaseEntity();
        String moduleName = json.getString("moduleName");
        String method = json.getString("method");
        String uri = json.getString("uri");

        String encrypt = EncryptAndDecryptUtil.encrypt(moduleName + method + uri);
        Date date = new Date(json.getInteger("startTimeMillis"));
        String startTime = FastDateFormat.getInstance("yyyyMMddHHmmssSSS").format(date);
        String random = HBaseUtil.getRandomNum(4);
        ansLogHbaseEntity.setRowKey(encrypt + "-" + startTime + "-" + random);
        ansLogHbaseEntity.setData(jsonStr);
        ansLogRepository.save(ansLogHbaseEntity);
    }


}///:~
