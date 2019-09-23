package com.bkcc.logans.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.bkcc.logans.entity.FieldEnumEntity;
import com.bkcc.logans.mapper.FieldEnumMapper;
import com.bkcc.logans.service.FieldEnumService;

/**
 * 【描 述】：日志分析模块字段枚举表业务实现类
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-20 15:28:26
 */
@Service
public class FieldEnumServiceImpl implements FieldEnumService{
    
    /**
     * 【描 述】：日志分析模块字段枚举表Mapper接口
     *
     *  @since  2019-09-20 15:28:26 v1.0
     */
    @Autowired
    private FieldEnumMapper fieldEnumMapper;
    
    /**
     * 【描 述】：添加或修改数据
     *
     * @since 2019-09-20 15:28:26
     * @see com.bkcc.logans.service.FieldEnumService#insertOrUpdate(com.bkcc.logans.entity.FieldEnumEntity)
     */
    @Override
    public void insertOrUpdate(FieldEnumEntity fieldEnum) {
        if(fieldEnum.getId() == null || fieldEnum.getId() <= 0) {
            fieldEnumMapper.insertFieldEnum(fieldEnum);
        } else {
            fieldEnumMapper.updateFieldEnumById(fieldEnum);
        }
    }

    /**
     * 【描 述】：删除日志分析模块字段枚举表信息
     *
     * @since 2019-09-20 15:28:26
     * @see com.bkcc.logans.service.FieldEnumService#deleteFieldEnumById(java.lang.Long)
     */
    @Override
    public void deleteFieldEnumById(Long id) {
        fieldEnumMapper.deleteFieldEnumById(id);
    }

    /**
     * 【描 述】：查询日志分析模块字段枚举表信息列表
     *
     * @since 2019-09-20 15:28:26
     * @see com.bkcc.logans.service.FieldEnumService#selectFieldEnumList(com.bkcc.logans.entity.FieldEnumEntity)
     */
    @Override
    public PageInfo<FieldEnumEntity> selectFieldEnumList(FieldEnumEntity fieldEnum) {
        if(fieldEnum == null) {
            fieldEnum = new FieldEnumEntity();
        }
        if(fieldEnum.getPageNum() != null && fieldEnum.getPageSize() != null) {
            PageHelper.startPage(fieldEnum.getPageNum(), fieldEnum.getPageSize());
        }
        List<FieldEnumEntity> returnList = fieldEnumMapper.selectFieldEnumList(fieldEnum);
        return new PageInfo<>(returnList);
    }

    /**
     * 【描 述】：查询单个日志分析模块字段枚举表信息
     *
     * @since 2019-09-20 15:28:26
     * @see com.bkcc.logans.service.FieldEnumService#selectFieldEnumById(java.lang.Long)
     */
    @Override
    public FieldEnumEntity selectFieldEnumById(Long id) {
        return fieldEnumMapper.selectFieldEnumById(id);
    }
    
}///:~
