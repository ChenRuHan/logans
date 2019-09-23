package com.bkcc.logans.entity.hbase;

import com.bkcc.hbase.annotations.HBaseColumn;
import com.bkcc.hbase.annotations.HBaseRowkey;
import com.bkcc.hbase.annotations.HBaseTable;
import lombok.Data;

import java.io.Serializable;

/**
 * 【描 述】：分析日志结果表
 *
 * @author 陈汝晗
 * @version v1.0
 * @since 2019-09-23 16:13
 */
@Data
@HBaseTable(tableName = "ans_res", nameSpace = "logans")
public class AnsResHbaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 【描 述】：(当前任务单号反转)
     *
     *  @since  Jul 17, 2019 v1.0
     */
    @HBaseRowkey
    private String rowKey;

    /**
     * 【描 述】：分析结果JSON
     *
     *  @since  Aug 6, 2019 v1.0
     */
    @HBaseColumn
    private String res;

}///:~
