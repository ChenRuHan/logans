var taskGridArea = {
    xtype: 'gridArea',
    url: '/api/task/list4view',
    singleSelect: true,
    columns: [[
        {field: 'id', checkbox: true},
        {field: 'taskName', title: '任务名称', width: 40},
        {field: 'moduleName', title: '微服务模块', width: 40},
        {field: 'reqMethod', title: '请求的方法', width: 20},
        {field: 'reqUri', title: '分析URI', width: 40},
        {
            field: 'ansType', title: '分析类型', width: 50, formatter: function (value) {
                return value == 1 ? '不分析直接输出指定字段' : '通过指定字段聚合分析';
            }
        },
        {
            field: 'ansRateType', title: '分析频次', width: 30, formatter: function (value) {
                switch (value) {
                    case 1:
                        return "每分钟分析一次";
                    case 2:
                        return "每小时分析一次";
                    case 3:
                        return "每天分析一次";
                    case 4:
                        return "每月分析一次";
                }
                return "";
            }
        },
        {field: 'outQueue', title: '输出消息队列', width: 200}
    ]],
    buttons: [{
        text: '新建分析任务',
        iconCls: 'icon-edit',
        onClick: function () {
            taskFormViewArea.showView();
            $("#saveButton").show();
        }
    },{
        text: '配置任务分析字段',
        iconCls: 'icon-add',
        onClick: function () {
            var rowData = taskGridArea.datagrid('getSelected');
            if (!rowData) {
                $.alert.info('请选中一条记录');
                return;
            }

            $("#fieldTaskIdId").textbox('setValue', rowData.id);
            $("#fieldTaskNameId").textbox('setValue', rowData.taskName);

            fieldGridArea.loadData({
                taskId: rowData.id
            });
            fieldViewArea.showView();
        }
    }, '-', {
        text: '删除分析任务',
        iconCls: 'icon-remove',
        onClick: function () {
            var rowData = taskGridArea.datagrid('getSelected');
            if (!rowData) {
                $.alert.info('请选中一条记录');
                return;
            }
            $.messager.confirm('提示', '确认删除分析任务?', function (r) {
                if (r) {
                    $.ajax({
                        url: __ctx + '/api/task/' + rowData.id + '/delete',
                        contentType: 'application/json;charset=utf-8',
                        method: 'delete',
                        success: function (viewData) {
                            viewData = JSON.parse(viewData);
                            if (!viewData.isSucceed) {
                                $.messager.alert('错误', viewData.message);
                                return;
                            }
                            taskGridArea.datagrid('reload');
                        }
                    })
                }
            });
        }
    }],
    onDblClickRow: function (rowIndex, rowData) {
        resGridArea.loadData({
            taskId: rowData.id
        });
    }

}


var ansTypeDataList = [{
    'id': '1',
    'name': '不分析直接输出指定字段'
}, {
    'id': '2',
    'name': '通过指定字段聚合分析',
    'selected': true
}];
var ansRateTypeDataList = [{
    'id': '1',
    'name': '每分钟分析一次',
    'selected': true
}, {
    'id': '2',
    'name': '每小时分析一次'
}, {
    'id': '3',
    'name': '每天分析一次'
}, {
    'id': '4',
    'name': '每月分析一次'
}];
var reqMethodDataList = [{
    'id': 'GET',
    'name': 'GET',
    'selected': true
}, {
    'id': 'POST',
    'name': 'POST'
}, {
    'id': 'DELETE',
    'name': 'DELETE'
}, {
    'id': 'PUT',
    'name': 'PUT'
}];


var taskFormArea = {
    xtype: "formArea",
    title: "添加分析任务",
    fields: [{
        label: "分析任务中文名称",
        xtype: "textbox",
        name: "taskName",
        id: "taskNameId"
    }, {
        label: "微服务模块名称",
        xtype: "textbox",
        name: "moduleName",
        id: "moduleNameId"
    }, {

        label: "需要分析请求的方法",
        xtype: "combobox",
        name: "reqMethod",
        id: "reqMethodId",
        valueField: "id",
        textField: "name",
        data: reqMethodDataList
    }, {
        label: "需要分析请求的URI",
        xtype: "textbox",
        name: "reqUri",
        id: "reqUriId"
    }, {
        label: "分析类型",
        xtype: "combobox",
        name: "ansType",
        id: "ansTypeId",
        valueField: "id",
        textField: "name",
        data: ansTypeDataList
    }, {
        label: "分析频次",
        xtype: "combobox",
        name: "ansRateType",
        id: "ansRateTypeId",
        valueField: "id",
        textField: "name",
        data: ansRateTypeDataList
    }, {
        label: "分析结果输出消息队列名称",
        xtype: "textbox",
        name: "outQueue",
        id: "outQueueId"
    }],
    buttons: [{
        text: "保存",
        id: "saveButton",
        iconCls: "icon-save",
        onClick: function () {
            $.messager.confirm('提示', '确认提交么?', function (r) {
                if (r) {
                    $.ajax({
                        url: __ctx + '/api/task',
                        contentType: 'application/json;charset=utf-8',
                        method: 'post',
                        data: JSON.stringify({
                            reqMethod: $("#reqMethodId").combobox('getValue'),
                            ansType: $("#ansTypeId").combobox('getValue'),
                            ansRateType: $("#ansRateTypeId").combobox('getValue'),

                            taskName: $("#taskNameId").textbox('getValue'),
                            moduleName: $("#moduleNameId").textbox('getValue'),
                            reqUri: $("#reqUriId").textbox('getValue'),
                            outQueue: $("#outQueueId").textbox('getValue')
                        }),
                        success: function (viewData) {
                            viewData = JSON.parse(viewData);
                            if (!viewData.isSucceed) {
                                $.messager.alert('错误', viewData.message);
                                return;
                            }
                            taskViewArea.showView();
                            taskGridArea.datagrid('reload');
                        }
                    })
                }
            });

        }
    }, {
        text: "返回",
        iconCls: "icon-back",
        onClick: function () {
            taskViewArea.showView();
            taskGridArea.datagrid('reload');
        }
    }]
}