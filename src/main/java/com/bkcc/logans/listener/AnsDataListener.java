package com.bkcc.logans.listener;

import com.alibaba.fastjson.JSONObject;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 【描 述】：数据对比监听器
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-10-16 15:46
 */
@Component
public class AnsDataListener {


    @JmsListener(destination = "logans_log", containerFactory = "queueListenerFactory")
    public void getNewData(String jsonStr) {
        JSONObject json = JSONObject.parseObject(jsonStr);


        /*
                json.put("taskIds", taskIds);
                json.put("method", method);
                json.put("uri", uri);
                json.put("uid", uid);
                json.put("timeMillis", timeMillis);
                json.put("resultStatus", isSuccess);
                json.put("resultObj", result);
                JSONObject param = new JSONObject();
                for (int i = 0; i < parmArr.length; i++) {
                    param.put(parmArr[i], args[i]);
                }
                json.put("args", param);
         */


    }


}///:~
