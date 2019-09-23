package com.bkcc.logans.service;

import com.bkcc.logans.entity.FieldEntity;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 【描 述】：日志分析模块字段表业务接口
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-20 15:27:37
 */
public interface FieldService {

    /**
     * 【描 述】：添加或修改数据
     *
     * @param field
     * @since Jul 15, 2019
     */
    void insertOrUpdate(FieldEntity field);

    /**
     * 【描 述】：删除日志分析模块字段表信息
     *
     * @param id 主键
     * @since Jul 15, 2019
     */
    void deleteFieldById(Long id);

    /**
     * 【描 述】：查询日志分析模块字段表信息列表
     *
     * @param phoneBook
     * @return
     * @since Jul 15, 2019
     */
    PageInfo<FieldEntity> selectFieldList(FieldEntity field);

    /**
     * 【描 述】：查询单个日志分析模块字段表信息
     *
     * @param id 主键
     * @return
     * @since Jul 15, 2019
     */
    FieldEntity selectFieldById(Long id);


    /**
     * 【描 述】：通过任务ID查询需要分析的字段集合
     *
     * @param taskId 任务ID
     * @return java.util.List<com.bkcc.logans.entity.FieldEntity>
     * @author 陈汝晗
     * @since 2019/9/23 13:55
     */
    List<FieldEntity> selectFieldListByTaskId(Long taskId);
}///:~
