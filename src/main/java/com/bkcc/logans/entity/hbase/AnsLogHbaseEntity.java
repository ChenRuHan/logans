package com.bkcc.logans.entity.hbase;

import com.bkcc.hbase.annotations.HBaseColumn;
import com.bkcc.hbase.annotations.HBaseRowkey;
import com.bkcc.hbase.annotations.HBaseTable;
import lombok.Data;

import java.io.Serializable;

/**
 * 【描 述】：需要分析的日志数据表
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-09-23 16:13
 */
@Data
@HBaseTable(tableName = "ans_log", nameSpace = "logans")
public class AnsLogHbaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 【描 述】：(模块+请求方法+请求uri 加密之后的值)-(请求日期yyyyMMddHHmmssSSS)-(4位随机数)
     *
     *  @since  Jul 17, 2019 v1.0
     */
    @HBaseRowkey
    private String rowKey;

    /**
     * 【描 述】：日志数据JSON格式
     *   "moduleName": moduleName,
     *   "method": method,
     *   "uri": uri,
     *   "uid": uid,
     *   "startTimeMillis": startTimeMillis,
     *   "stopTimeMillis": stopTimeMillis,
     *   "resultStatus": isSuccess,
     *   "resultObj": result,
     *   "taskIds": taskIds,
     *   "args": {
     *       'paramName1' : {obj1},
     *       'paramName2' : {obj2},
     *   }
     *
     *  @since  Aug 6, 2019 v1.0
     */
    @HBaseColumn
    private String data;

}///:~
