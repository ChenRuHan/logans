package com.bkcc.logans.controller;

import com.bkcc.core.data.ViewData;
import com.bkcc.logans.controller.base.BaseController;
import com.bkcc.logans.entity.TaskResEntity;
import com.bkcc.logans.entity.hbase.TableHbaseEntity;
import com.bkcc.logans.repository.hbase.TableRepository;
import com.bkcc.logans.service.TaskResService;
import com.bkcc.logans.util.EncryptAndDecryptUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

/**
 * 【描 述】：日志分析任务结果表Controller
 * 【环 境】：J2SE 1.8
 *
 * @author 陈汝晗
 * @version v1.0 新建
 * @since 2019-09-23 17:24:57
 */
@RestController
@Api(value = "日志分析任务结果表Controller")
@RequestMapping("/api/task-res")
public class TaskResController extends BaseController {

    /**
     * 【描 述】：日志分析任务结果表业务接口
     *
     * @since 2019-09-23 17:24:57
     */
    @Autowired
    private TaskResService taskResService;

    /**
     * 【描 述】：HBASE日数据库表数据接口
     *
     * @since 2019/10/19 11:04
     */
    @Autowired
    private TableRepository tableRepository;

    /**
     * 【描 述】：查询日志分析任务结果表信息列表
     *
     * @since 2019-09-23 17:24:57
     */
    @ApiOperation(value = "查询日志分析任务结果表信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "taskRes", value = "{"
                    + "</br> pageNum : 当前页码--不传查询全部,"
                    + "</br> pageSize : 每页大小--不传查询全部"
                    + "</br> orderNO : 任务执行单号,"
                    + "</br> taskId : 日志分析任务ID,"
                    + "</br> beginTime : 任务分析开始时间--begin,"
                    + "</br> endTime : 任务分析开始时间--end,"
                    + "</br> errorCode : 错误码,"
                    + "</br>}", dataType = "TaskResEntity", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "{</br> rows:[{"
                    + "</br> id : 日志分析任务结果表ID,"
                    + "</br> orderNO : 任务执行单号,"
                    + "</br> taskId : 日志分析任务ID,"
                    + "</br> beginTime : 任务分析开始时间,"
                    + "</br> endTime : 任务分析结束时间,"
                    + "</br> errorCode : 错误码,"
                    + "</br> resJSON : 返回结果JSON字符串"
                    + "</br>}...], "
                    + "</br> newPrimaryKeys : {},"
                    + "</br> total:总数 </br>}")
    })
    @PostMapping("/list")
    public ViewData selectTaskResList(@RequestBody TaskResEntity taskRes) {
        if (taskRes == null) {
            return ViewData.error("传入信息有误");
        }
        PageInfo<TaskResEntity> pageInfo = taskResService.selectTaskResList(taskRes);
        ViewData returnV = new ViewData();
        returnV.setrows(pageInfo.getList());
        returnV.settotal(pageInfo.getTotal());
        return returnV;
    }

    /**
     * 【描 述】：通过订单号查询日志分析结果
     *
     * @return
     * @since Jul 15, 2019
     */
    @ApiOperation(value = "通过订单号查询日志分析结果")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "orderNO", value = "订单号", dataType = "Long", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "{</br> rows:[{"
                    + "</br> orderNO : 任务执行单号,"
                    + "</br> taskId : 日志分析任务ID,"
                    + "</br> beginTime : 任务分析开始时间,"
                    + "</br> endTime : 任务分析结束时间,"
                    + "</br> errorCode : 错误码,"
                    + "</br> resJSON : 返回结果JSON字符串"
                    + "</br>}], </br> newPrimaryKeys : {}"
                    + "</br>}")
    })
    @GetMapping("/{orderNO}/detail")
    public ViewData selectTaskResByOrderNO(@PathVariable Long orderNO) {
        TaskResEntity par = new TaskResEntity();
        par.setOrderNO(orderNO);
        return selectTaskResList(par);
    }

    /**
     * 【描 述】：查询指定表中的历史数据
     *
     * @return
     * @since Jul 15, 2019
     */
    @ApiOperation(value = "查询指定表中的历史数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "moduleName", value = "模块名称", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "path", name = "tableName", value = "表名称", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "query", name = "uniqueValue", value = "唯一索引值", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "query", name = "beginTime", value = "查询开始时间(yyyy-MM-dd HH:mm:ss)", dataType = "String", required = false),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "查询结束时间(yyyy-MM-dd HH:mm:ss)", dataType = "String", required = false),
            @ApiImplicitParam(paramType = "query", name = "nextRowKey", value = "查询开始rowKey,为空代表从第一条记录查询", dataType = "String", required = false),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页大小，默认10条", dataType = "Long", required = false)
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "{</br> rows:[{"
                + "</br> uid : 修改人UID,"
                + "</br> time : 修改时间,"
                + "</br> data : 数据内容JSON格式,"
                + "</br>}], </br> newPrimaryKeys : {"
                    + "</br> nextRowKey : 作为查询下一批次数据的nextRowKey,空代表没有数据"
                + "}"
                + "</br>}")
    })
    @GetMapping("/{moduleName}/{tableName}/table")
    public ViewData selectTableData(@PathVariable String moduleName, @PathVariable String tableName
            , @RequestParam String uniqueValue
            , @RequestParam(required = false) String beginTime
            , @RequestParam(required = false) String endTime
            , @RequestParam(required = false) String nextRowKey
            , @RequestParam(required = false) Integer pageSize) {

        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        String beginL = "0000000000000";
        String endL = "9999999999999";
        Long max = 9999999999999L;
        if (StringUtils.isNotBlank(beginTime)) {
            try {
                endL = (max - dateFormat.parse(beginTime).getTime())+"";
            } catch (ParseException e) {
                return ViewData.error("【beginTime】数据非法");
            }
        }
        if (StringUtils.isNotBlank(endTime)) {
            try {
                beginL = (max - dateFormat.parse(endTime).getTime())+"";
            } catch (ParseException e) {
                return ViewData.error("【endTime】数据非法");
            }
        }

        if (pageSize == null || pageSize < 0) {
            pageSize = 10;
        }
        String mtu = moduleName + tableName + "-" + uniqueValue;
        String encrypt = EncryptAndDecryptUtil.encrypt(mtu).substring(0, 1) + mtu;
        String beginRowKey = nextRowKey;
        if (StringUtils.isBlank(beginRowKey)) {
            beginRowKey = encrypt + "-" + beginL;
        }
        String endRowKey = encrypt + "-9999999999999" + endL;
        List<TableHbaseEntity> list = tableRepository.list(beginRowKey, endRowKey, ++pageSize);
        if (list.size() < pageSize) {
            nextRowKey = null;
        } else {
            TableHbaseEntity last = list.remove(list.size());
            nextRowKey = last.getRowKey();
        }
        ViewData viewData = new ViewData();
        viewData.setrows(list);
        viewData.addNewPrimaryKey("nextRowKey", nextRowKey);
        return viewData;
    }


    /**
     * 【描 述】：查询信息列表
     *
     * @since 2019-07-29 15:31:06
     */
    @PostMapping("/list4view")
    public ViewData selectFeedbackList(Integer page, Integer rows, Long taskId) {
        TaskResEntity taskRes = new TaskResEntity();
        taskRes.setPageNum(page);
        taskRes.setPageSize(rows);
        taskRes.setTaskId(taskId);
        PageInfo<TaskResEntity> pageInfo = taskResService.selectTaskResList(taskRes);
        ViewData returnV = new ViewData();
        returnV.setrows(pageInfo.getList());
        returnV.settotal(pageInfo.getTotal());
        return returnV;
    }


}///：～
