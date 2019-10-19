package com.bkcc.logans.repository.hbase;

import com.alibaba.fastjson.JSONObject;
import com.bkcc.hbase.repository.AbstractHBaseRepository;
import com.bkcc.logans.entity.hbase.AnsLogHbaseEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * 【描 述】：HBase需要分析的日志数据表接口
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 Aug 6, 2019 新建
 *  @since          Aug 6, 2019 
 */
@Repository
public class AnsLogRepository extends AbstractHBaseRepository<AnsLogHbaseEntity>{

    public JSONObject getArgsList(JSONObject arg, String pex) {
        JSONObject returnJson = new JSONObject();
        for (String key : arg.keySet()) {
            String value = arg.getString(key);
            try {
                JSONObject child = JSONObject.parseObject(value);
                if (StringUtils.isNotBlank(pex)) {
                    returnJson.putAll(getArgsList(child, pex + "." + key));
                } else {
                    returnJson.putAll(getArgsList(child, key));
                }
            } catch (Exception e) {
                if (StringUtils.isNotBlank(pex)) {
                    returnJson.put(pex + "." + key, value);
                } else {
                    returnJson.put(key, value);
                }
            }
        }
        return returnJson;
    }


}///:~
