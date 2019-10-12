package com.bkcc.logans.mapper;

import com.bkcc.logans.entity.FieldEnumEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【描 述】：日志分析模块字段枚举表Mapper接口
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-20 15:28:26
 */
public interface FieldEnumMapper {

    /**
     * 【描 述】：删除日志分析模块字段枚举表信息
     *
     * @param id
     * @since 2019-09-20 15:28:26
     */
    void deleteFieldEnumById(Long id);
    /**
     * 【描 述】：通过任务Id删除
     *
     * @param taskId
     * @return void
     * @author 陈汝晗
     * @since 2019/10/12 17:15
     */
    void deleteFieldEnumByTaskId(@Param("taskId") Long taskId);
    /**
     * 【描 述】：通过字段Id删除
     *
     * @param fieldId
     * @return void
     * @author 陈汝晗
     * @since 2019/10/12 17:15
     */
    void deleteFieldEnumByFieldId(@Param("fieldId") Long fieldId);

    /**
     * 【描 述】：添加日志分析模块字段枚举表数据
     *
     * @param fieldEnum 实体类数据
     * @since 2019-09-20 15:28:26
     */
    void insertFieldEnum(FieldEnumEntity fieldEnum);

    /**
     * 【描 述】：修改日志分析模块字段枚举表数据
     *
     * @param fieldEnum 实体类数据
     * @since 2019-09-20 15:28:26
     */
    void updateFieldEnumById(FieldEnumEntity fieldEnum);

    /**
     * 【描 述】：查询日志分析模块字段枚举表信息列表
     *
     * @param fieldEnum 实体类数据
     * @return
     * @since 2019-09-20 15:28:26
     */
    List<FieldEnumEntity> selectFieldEnumList(FieldEnumEntity fieldEnum);

    /**
     * 【描 述】：查询单个日志分析模块字段枚举表信息
     *
     * @param id 主键
     * @return
     * @since 2019-09-20 15:28:26
     */
    FieldEnumEntity selectFieldEnumById(Long id);

}///:~