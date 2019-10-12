package com.bkcc.logans.service.impl;

import com.bkcc.logans.entity.FieldEntity;
import com.bkcc.logans.entity.FieldEnumEntity;
import com.bkcc.logans.mapper.FieldEnumMapper;
import com.bkcc.logans.mapper.FieldMapper;
import com.bkcc.logans.service.FieldService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 【描 述】：日志分析模块字段表业务实现类
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-20 15:27:37
 */
@Service
public class FieldServiceImpl implements FieldService{
    
    /**
     * 【描 述】：日志分析模块字段表Mapper接口
     *
     *  @since  2019-09-20 15:27:37 v1.0
     */
    @Autowired
    private FieldMapper fieldMapper;

    /**
     * 【描 述】：日志分析模块字段枚举表Mapper接口
     *
     *  @since 2019/9/23 13:57
     */
    @Autowired
    private FieldEnumMapper fieldEnumMapper;

    /**
     * 【描 述】：添加或修改数据
     *
     * @since 2019-09-20 15:27:37
     * @see com.bkcc.logans.service.FieldService#insertOrUpdate(com.bkcc.logans.entity.FieldEntity)
     */
    @Override
    public void insertOrUpdate(FieldEntity field) {
        if(field.getId() == null || field.getId() <= 0) {
            fieldMapper.insertField(field);
        } else {
            fieldMapper.updateFieldById(field);
        }
    }

    /**
     * 【描 述】：删除日志分析模块字段表信息
     *
     * @since 2019-09-20 15:27:37
     * @see com.bkcc.logans.service.FieldService#deleteFieldById(java.lang.Long)
     */
    @Override
    public void deleteFieldById(Long id) {
        fieldMapper.deleteFieldById(id);
    }

    /**
     * 【描 述】：查询日志分析模块字段表信息列表
     *
     * @since 2019-09-20 15:27:37
     * @see com.bkcc.logans.service.FieldService#selectFieldList(com.bkcc.logans.entity.FieldEntity)
     */
    @Override
    public PageInfo<FieldEntity> selectFieldList(FieldEntity field) {
        if(field == null) {
            field = new FieldEntity();
        }
        if(field.getPageNum() != null && field.getPageSize() != null) {
            PageHelper.startPage(field.getPageNum(), field.getPageSize());
        }
        List<FieldEntity> returnList = fieldMapper.selectFieldList(field);
        return new PageInfo<>(returnList);
    }

    /**
     * 【描 述】：查询单个日志分析模块字段表信息
     *
     * @since 2019-09-20 15:27:37
     * @see com.bkcc.logans.service.FieldService#selectFieldById(java.lang.Long)
     */
    @Override
    public FieldEntity selectFieldById(Long id) {
        return fieldMapper.selectFieldById(id);
    }

    /**
     * 【描 述】：通过任务ID查询需要分析的字段集合
     *
     * @param taskId 任务ID
     * @return java.util.List<com.bkcc.logans.entity.FieldEntity>
     * @author 陈汝晗
     * @since 2019/9/23 13:55
     */
    @Override
    public List<FieldEntity> selectFieldListByTaskId(Long taskId) {
        Map<Long, List<FieldEnumEntity>> fieldenListMap = new HashMap<>();
        FieldEnumEntity fieldEnumEntity = new FieldEnumEntity();
        fieldEnumEntity.setTaskId(taskId);
        for (FieldEnumEntity enumEntity : fieldEnumMapper.selectFieldEnumList(fieldEnumEntity)) {
            Long key = enumEntity.getFieldId();
            List<FieldEnumEntity> value = fieldenListMap.containsKey(key) ? fieldenListMap.get(key) : new ArrayList<>();
            value.add(enumEntity);
            fieldenListMap.put(key, value);
        }

        FieldEntity field = new FieldEntity();
        field.setTaskId(taskId);
        List<FieldEntity> feildList = fieldMapper.selectFieldList(field);
        for (FieldEntity entity : feildList) {
            if (fieldenListMap.containsKey(entity.getId())) {
                entity.setFieldEnumList(fieldenListMap.get(entity.getId()));
            }
        }
        return feildList;
    }
}///:~
