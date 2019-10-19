package com.bkcc.logans.listener;

import com.alibaba.fastjson.JSONObject;
import com.bkcc.hbase.util.HBaseUtil;
import com.bkcc.logans.constant.TaskConstant;
import com.bkcc.logans.entity.TaskEntity;
import com.bkcc.logans.entity.hbase.AnsLogHbaseEntity;
import com.bkcc.logans.entity.hbase.TableHbaseEntity;
import com.bkcc.logans.enums.AnsTypeEnum;
import com.bkcc.logans.repository.hbase.AnsLogRepository;
import com.bkcc.logans.repository.hbase.TableRepository;
import com.bkcc.logans.service.FieldService;
import com.bkcc.logans.util.EncryptAndDecryptUtil;
import org.apache.commons.lang3.StringUtils;
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
     * 【描 述】：HBASE日数据库表数据接口
     *
     *  @since 2019/10/19 09:38
     */
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private FieldService fieldService;


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
        String taskIds = json.getString("taskIds");

        String mmu = moduleName + method + uri;
        String encrypt = EncryptAndDecryptUtil.encrypt(mmu).substring(0, 1) + mmu;

        Date date = new Date(json.getInteger("startTimeMillis"));
        String startTime = FastDateFormat.getInstance("yyyyMMddHHmmssSSS").format(date);
        String random = HBaseUtil.getRandomNum(4);
        ansLogHbaseEntity.setRowKey(encrypt + "-" + startTime + "-" + random);
        ansLogHbaseEntity.setData(jsonStr);
        ansLogRepository.save(ansLogHbaseEntity);

        for (String s : taskIds.split(",")) {
            Long taskId = Long.parseLong(s);
            TaskEntity taskEntity = TaskConstant.TASK_MAP.get(taskId);
            if (!AnsTypeEnum.COMPARE_ANS.equels(taskEntity.getAnsType())) {
                continue;
            }
            if (StringUtils.isBlank(taskEntity.getUniqueField())) {
                continue;
            }
            JSONObject arg = ansLogRepository.getArgsList(json.getJSONObject("args"), null);
            StringBuffer unidx = new StringBuffer();
            for (String key : taskEntity.getUniqueField().split(",")) {
                if (arg.containsKey(key)) {
                    unidx.append(arg.get(key));
                }
            }
            TableHbaseEntity tableHbaseEntity = new TableHbaseEntity();
            String mtu = taskEntity.getModuleName() + taskEntity.getTableName() + "-" + unidx.toString();
            encrypt = EncryptAndDecryptUtil.encrypt(mtu).substring(0, 1) + mtu;
            tableHbaseEntity.setRowKey(encrypt + "-" + HBaseUtil.getAscCurrent());
            tableHbaseEntity.setData(arg.toJSONString());
            tableHbaseEntity.setTime(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(date));
            tableHbaseEntity.setUid(json.getLong("uid"));
            tableRepository.save(tableHbaseEntity);
        }
    }


}///:~
