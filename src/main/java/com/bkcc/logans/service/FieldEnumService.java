package com.bkcc.logans.service;

import com.bkcc.logans.entity.FieldEnumEntity;
import com.github.pagehelper.PageInfo;

/**
 * 【描 述】：日志分析模块字段枚举表业务接口
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-20 15:28:26
 */
public interface FieldEnumService {

    /**
     * 【描 述】：添加或修改数据
     *
     * @param fieldEnum
     * @since Jul 15, 2019
     */
    void insertOrUpdate(FieldEnumEntity fieldEnum);

    /**
     * 【描 述】：删除日志分析模块字段枚举表信息
     *
     * @param id 主键
     * @since Jul 15, 2019
     */
    void deleteFieldEnumById(Long id);

    /**
     * 【描 述】：查询日志分析模块字段枚举表信息列表
     *
     * @param phoneBook
     * @return
     * @since Jul 15, 2019
     */
    PageInfo<FieldEnumEntity> selectFieldEnumList(FieldEnumEntity fieldEnum);

    /**
     * 【描 述】：查询单个日志分析模块字段枚举表信息
     *
     * @param id 主键
     * @return
     * @since Jul 15, 2019
     */
    FieldEnumEntity selectFieldEnumById(Long id);


}///:~
