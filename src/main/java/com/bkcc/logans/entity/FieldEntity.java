package com.bkcc.logans.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.bkcc.logans.entity.base.BaseEntity;

import java.util.List;

/**
 * 【描 述】：日志分析模块字段表实体类
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-20 15:27:37
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class FieldEntity extends BaseEntity{

    /**
     * 【描 述】：序列化UID
     *
     *  @since  Jul 15, 2019 v1.0
     */
    private static final long serialVersionUID = 1L;
    /**
     * 【描 述】：日志分析任务ID
     *
     *  @since  2019-09-20 15:27:37
     */
    private Long taskId;
    /**
     * 【描 述】：字段英文KEY，对用存储到ES中的KEY值
     *
     *  @since  2019-09-20 15:27:37
     */
    private String fileKey;
    /**
     * 【描 述】：字段中文描述
     *
     *  @since  2019-09-20 15:27:37
     */
    private String fileRemark;

    /**
     * 【描 述】：字段对应枚举集合
     *
     *  @since 2019/9/23 13:51
     *
     */
    private List<FieldEnumEntity> fieldEnumList;
}///:~