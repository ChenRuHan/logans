package com.bkcc.logans.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bkcc.core.data.ViewData;
import com.bkcc.logans.controller.base.BaseController;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.bkcc.logans.entity.FieldEnumEntity;
import com.bkcc.logans.service.FieldEnumService;

/**
 * 【描 述】：日志分析模块字段枚举表Controller
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-20 15:28:26
 */
@RestController
@Api(value = "日志分析模块字段枚举表Controller")
@RequestMapping("/api/field-enum")
public class FieldEnumController extends BaseController{

    /**
     * 【描 述】：日志分析模块字段枚举表业务接口
     *
     *  @since  2019-09-20 15:28:26
     */
    @Autowired
    private FieldEnumService fieldEnumService;
    
    /**
     * 【描 述】：查询日志分析模块字段枚举表信息列表
     *
     * @since  2019-09-20 15:28:26
     */
    @ApiOperation(value = "查询日志分析模块字段枚举表信息列表")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="body", name="fieldEnum", value="{"
            + "</br> pageNum : 当前页码--不传查询全部,"
            + "</br> pageSize : 每页大小--不传查询全部"
            + "</br> taskId : 日志分析任务ID,"
            + "</br> fieldId : 字段ID,"
            + "</br> enumValue : 字段对应枚举值,"
            + "</br> allowRegex : 允许范围内的值正则表达式,"
            + "</br>}", dataType="FieldEnumEntity", required=true)
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "{</br> rows:[{"
            + "</br> id : 日志分析模块字段枚举表ID,"
            + "</br> taskId : 日志分析任务ID,"
            + "</br> fieldId : 字段ID,"
            + "</br> enumValue : 字段对应枚举值,"
            + "</br> allowRegex : 允许范围内的值正则表达式,"
            + "</br>}...], "
            + "</br> newPrimaryKeys : {},"
            + "</br> total:总数 </br>}")
    })
    @PostMapping("/list")
    public ViewData selectFieldEnumList(@RequestBody FieldEnumEntity fieldEnum) {
        if(fieldEnum == null) {
            return ViewData.error("传入信息有误");
        }
        PageInfo<FieldEnumEntity> pageInfo = fieldEnumService.selectFieldEnumList(fieldEnum);
        ViewData returnV = new ViewData();
        returnV.setrows(pageInfo.getList());
        returnV.settotal(pageInfo.getTotal());
        return returnV;
    }
            

    /**
     * 【描 述】：添加、修改日志分析模块字段枚举表信息
     *
     * @since  2019-09-20 15:28:26
     */
    @ApiOperation(value = "添加、修改日志分析模块字段枚举表信息")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="body", name="fieldEnum", value="{"
                + "</br> \"id\" : 主键ID，不传或-1代表新增,"
                + "</br> \"taskId\" : 日志分析任务ID,"
                + "</br> \"fieldId\" : 字段ID,"
                + "</br> \"enumValue\" : 字段对应枚举值,"
                + "</br> \"allowRegex\" : 允许范围内的值正则表达式,"
                + "</br>}", dataType="FieldEnumEntity", required=true)
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "{</br>newPrimaryKeys : {"
            + "</br> id : 日志分析模块字段枚举表ID"
            + "</br>}</br>}" )
    })
    @PostMapping
    public ViewData insertOrUpdate(@RequestBody FieldEnumEntity fieldEnum) {
        if(fieldEnum == null) {
            return ViewData.error("传入信息有误");
        }
        fieldEnumService.insertOrUpdate(fieldEnum);
        return ViewData.put("id", fieldEnum.getId());
    }
    
    /**
     * 【描 述】：删除日志分析模块字段枚举表信息
     *
     * @param fieldEnumId
     * @return
     * @since Jul 15, 2019
     */
    @ApiOperation(value = "删除日志分析模块字段枚举表信息")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="path", name="fieldEnumId", value="日志分析模块字段枚举表ID", dataType="Long", required=true)
    })
    @DeleteMapping("/{fieldEnumId}/delete")
    public ViewData deleteById(@PathVariable Long fieldEnumId) {
        fieldEnumService.deleteFieldEnumById(fieldEnumId);
        return ViewData.ok();
    }
    
    /**
     * 【描 述】：查询单个日志分析模块字段枚举表信息
     *
     * @param fieldEnumId
     * @return
     * @since Jul 15, 2019
     */
    @ApiOperation(value = "查询单个日志分析模块字段枚举表信息")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="path", name="fieldEnumId", value="日志分析模块字段枚举表ID", dataType="Long", required=true)
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "{</br> rows:[{"
                + "</br> taskId : 日志分析任务ID,"
                + "</br> fieldId : 字段ID,"
                + "</br> enumValue : 字段对应枚举值,"
                + "</br> allowRegex : 允许范围内的值正则表达式,"
            + "</br>}], </br> newPrimaryKeys : {}"
            + "</br>}" )
    })
    @GetMapping("/{fieldEnumId}/detail")
    public ViewData selectFieldEnumById(@PathVariable Long fieldEnumId) {
        FieldEnumEntity fieldEnumEntity = fieldEnumService.selectFieldEnumById(fieldEnumId);
        return ViewData.ok(fieldEnumEntity);
    }
    
    
}///：～
