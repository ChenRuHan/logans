package com.bkcc.logans.entity;

import com.bkcc.logans.entity.base.BaseEntity;
import com.bkcc.logans.enums.CalendarEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 【描 述】：日志分析任务配置表实体类
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-23 11:00:57
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class TaskEntity extends BaseEntity{

    /**
     * 【描 述】：序列化UID
     *
     *  @since  Jul 15, 2019 v1.0
     */
    private static final long serialVersionUID = 1L;
    /**
     * 【描 述】：分析任务中文名称
     *
     *  @since  2019-09-23 11:00:57
     */
    private String taskName;
    /**
     * 【描 述】：微服务模块名称
     *
     *  @since  2019-09-23 11:00:57
     */
    private String moduleName;
    /**
     * 【描 述】：分析数据日志来源：1--es，2--hbase
     *
     *  @since  2019-09-23 11:00:57
     */
    private Integer logSource;
    /**
     * 【描 述】：需要分析请求的方法 GET POST DELETE PUT
     *
     *  @since  2019-09-23 11:00:57
     */
    private String reqMethod;
    /**
     * 【描 述】：需要分析请求的URI
     *
     *  @since  2019-09-23 11:00:57
     */
    private String reqUri;
    /**
     * 【描 述】：分析类型，1--不分析直接输出指定字段，2--通过指定字段聚合分析，3--通过指定字段对比修改数据
     *
     *  @since  2019-09-23 11:00:57
     */
    private Integer ansType;
    /**
     * 【描 述】：分析频次，每（1--分，2--时，3--日，4--月）分析一次。
     *
     *  @since  2019-09-23 11:00:57
     */
    private Integer ansRateType;
    /**
     * 【描 述】：分析结果输出消息队列名称
     *
     *  @since  2019-09-23 11:00:57
     */
    private String outQueue;


    /**
     * 【描 述】：cron表达式
     *
     * @since 2019-09-20 15:23:35
     */
    private String ansCron;

    /**
     * 【描 述】：任务开始时间
     *
     *  @since 2019/9/23 10:15
     */
    private String beginTime;

    /**
     * 【描 述】：需要对比数据的表名称
     *
     *  @since 2019/9/23 10:15
     */
    private String tableName;

    /**
     * 【描 述】：任务结束时间
     *
     *  @since 2019/9/23 10:15
     */
    private String endTime;

    public String getAnsCronByAnsRateType() {
        if (CalendarEnum.MINUTE.equels(ansRateType)) {
            ansCron = "0 * * * * ?";
        } else if (CalendarEnum.HOUR.equels(ansRateType)) {
            ansCron = "0 0 * * * ?";
        } else if (CalendarEnum.DAY.equels(ansRateType)) {
            ansCron = "0 0 0 * * ?";
        } else if (CalendarEnum.MONTH.equels(ansRateType)) {
            ansCron = "0 0 0 1 * ?";
        }
        return ansCron;
    }

    @Override
    public String toString() {
        return "taskId=" + getId() +
                ", taskName=" + taskName +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime;
    }
}///:~