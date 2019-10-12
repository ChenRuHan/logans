package com.bkcc.logans.mapper;

import com.bkcc.logans.entity.FieldEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【描 述】：日志分析模块字段表Mapper接口
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-20 15:27:37
 */
public interface FieldMapper {

    /**
     * 【描 述】：删除日志分析模块字段表信息
     *
     * @param id
     * @since 2019-09-20 15:27:37
     */
    void deleteFieldById(Long id);
    /**
     * 【描 述】：通过任务ID删除
     *
     * @param taskId
     * @return void
     * @author 陈汝晗
     * @since 2019/10/12 17:15
     */
    void deleteFieldByTaskId(@Param("taskId") Long taskId);

    /**
     * 【描 述】：添加日志分析模块字段表数据
     *
     * @param field 实体类数据
     * @since 2019-09-20 15:27:37
     */
    void insertField(FieldEntity field);

    /**
     * 【描 述】：修改日志分析模块字段表数据
     *
     * @param field 实体类数据
     * @since 2019-09-20 15:27:37
     */
    void updateFieldById(FieldEntity field);

    /**
     * 【描 述】：查询日志分析模块字段表信息列表
     *
     * @param field 实体类数据
     * @return
     * @since 2019-09-20 15:27:37
     */
    List<FieldEntity> selectFieldList(FieldEntity field);

    /**
     * 【描 述】：查询单个日志分析模块字段表信息
     *
     * @param id 主键
     * @return
     * @since 2019-09-20 15:27:37
     */
    FieldEntity selectFieldById(Long id);

}///:~