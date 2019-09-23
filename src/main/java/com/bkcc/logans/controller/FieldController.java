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

import com.bkcc.logans.entity.FieldEntity;
import com.bkcc.logans.service.FieldService;

/**
 * 【描 述】：日志分析模块字段表Controller
 * 【环 境】：J2SE 1.8
 *
 *  @author         陈汝晗
 *  @version        v1.0 新建
 *  @since          2019-09-20 15:27:37
 */
@RestController
@Api(value = "日志分析模块字段表Controller")
@RequestMapping("/api/field")
public class FieldController extends BaseController{

    /**
     * 【描 述】：日志分析模块字段表业务接口
     *
     *  @since  2019-09-20 15:27:37
     */
    @Autowired
    private FieldService fieldService;
    
    /**
     * 【描 述】：查询日志分析模块字段表信息列表
     *
     * @since  2019-09-20 15:27:37
     */
    @ApiOperation(value = "查询日志分析模块字段表信息列表")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="body", name="field", value="{"
            + "</br> pageNum : 当前页码--不传查询全部,"
            + "</br> pageSize : 每页大小--不传查询全部"
            + "</br> taskId : 日志分析任务ID,"
            + "</br> fileKey : 字段英文KEY，对用存储到ES中的KEY值,"
            + "</br> fileRemark : 字段中文描述,"
            + "</br>}", dataType="FieldEntity", required=true)
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "{</br> rows:[{"
            + "</br> id : 日志分析模块字段表ID,"
            + "</br> taskId : 日志分析任务ID,"
            + "</br> fileKey : 字段英文KEY，对用存储到ES中的KEY值,"
            + "</br> fileRemark : 字段中文描述,"
            + "</br>}...], "
            + "</br> newPrimaryKeys : {},"
            + "</br> total:总数 </br>}")
    })
    @PostMapping("/list")
    public ViewData selectFieldList(@RequestBody FieldEntity field) {
        if(field == null) {
            return ViewData.error("传入信息有误");
        }
        PageInfo<FieldEntity> pageInfo = fieldService.selectFieldList(field);
        ViewData returnV = new ViewData();
        returnV.setrows(pageInfo.getList());
        returnV.settotal(pageInfo.getTotal());
        return returnV;
    }
            

    /**
     * 【描 述】：添加、修改日志分析模块字段表信息
     *
     * @since  2019-09-20 15:27:37
     */
    @ApiOperation(value = "添加、修改日志分析模块字段表信息")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="body", name="field", value="{"
                + "</br> \"id\" : 主键ID，不传或-1代表新增,"
                + "</br> \"taskId\" : 日志分析任务ID,"
                + "</br> \"fileKey\" : 字段英文KEY，对用存储到ES中的KEY值,"
                + "</br> \"fileRemark\" : 字段中文描述,"
                + "</br>}", dataType="FieldEntity", required=true)
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "{</br>newPrimaryKeys : {"
            + "</br> id : 日志分析模块字段表ID"
            + "</br>}</br>}" )
    })
    @PostMapping
    public ViewData insertOrUpdate(@RequestBody FieldEntity field) {
        if(field == null) {
            return ViewData.error("传入信息有误");
        }
        fieldService.insertOrUpdate(field);
        return ViewData.put("id", field.getId());
    }
    
    /**
     * 【描 述】：删除日志分析模块字段表信息
     *
     * @param fieldId
     * @return
     * @since Jul 15, 2019
     */
    @ApiOperation(value = "删除日志分析模块字段表信息")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="path", name="fieldId", value="日志分析模块字段表ID", dataType="Long", required=true)
    })
    @DeleteMapping("/{fieldId}/delete")
    public ViewData deleteById(@PathVariable Long fieldId) {
        fieldService.deleteFieldById(fieldId);
        return ViewData.ok();
    }
    
    /**
     * 【描 述】：查询单个日志分析模块字段表信息
     *
     * @param fieldId
     * @return
     * @since Jul 15, 2019
     */
    @ApiOperation(value = "查询单个日志分析模块字段表信息")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="path", name="fieldId", value="日志分析模块字段表ID", dataType="Long", required=true)
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "{</br> rows:[{"
                + "</br> taskId : 日志分析任务ID,"
                + "</br> fileKey : 字段英文KEY，对用存储到ES中的KEY值,"
                + "</br> fileRemark : 字段中文描述,"
            + "</br>}], </br> newPrimaryKeys : {}"
            + "</br>}" )
    })
    @GetMapping("/{fieldId}/detail")
    public ViewData selectFieldById(@PathVariable Long fieldId) {
        FieldEntity fieldEntity = fieldService.selectFieldById(fieldId);
        return ViewData.ok(fieldEntity);
    }
    
    
}///：～
