var fieldGridArea = {
    xtype: 'gridArea',
    url: '/api/field/list4view',
    singleSelect: true,
    queryParams: {taskId: 0},
    columns: [[
        {field: 'id', checkbox: true},
        {field: 'taskId', title: '日志分析任务ID', width: 40, formatter: function (value) {
                return $("#fieldTaskIdId").textbox('getValue');
            }},
        {field: 'taskName', title: '日志分析任务名称', width: 40, formatter: function (value) {
                return $("#fieldTaskNameId").textbox('getValue');
            }
        },
        {field: 'fieldKey', title: '字段英文KEY，对应存储到ES中的KEY值', width: 40},
        {field: 'fieldRemark', title: '字段中文描述', width: 20}
    ]],
    buttons: [{
        text: '返回到任务页面',
        iconCls: 'icon-back',
        onClick: function () {
            taskViewArea.showView();
            taskGridArea.datagrid('reload');
        }
    },{
        text: '配置字段枚举值',
        iconCls: 'icon-add',
        onClick: function () {
            var rowData = fieldGridArea.datagrid('getSelected');
            if (!rowData) {
                $.alert.info('请选中一条记录');
                return;
            }

            $("#fieldEnumTaskIdId").textbox('setValue', $("#fieldTaskIdId").textbox('getValue'));
            $("#fieldEnumTaskNameId").textbox('setValue', $("#fieldTaskNameId").textbox('getValue'));

            $("#fieldEnumFieldIdId").textbox('setValue', rowData.id);
            $("#fieldEnumFieldKeyId").textbox('setValue', rowData.fieldKey);

            fieldEnumGridArea.loadData({
                taskId: $("#fieldTaskIdId").textbox('getValue'),
                fieldId: rowData.id
            });
            fieldEnumViewArea.showView();
        }
    },{
        text: '删除分析任务字段',
        iconCls: 'icon-remove',
        onClick: function () {
            var rowData = fieldGridArea.datagrid('getSelected');
            if (!rowData) {
                $.alert.info('请选中一条记录');
                return;
            }
            $.messager.confirm('提示', '确认删除分析任务?', function (r) {
                if (r) {
                    $.ajax({
                        url: __ctx + '/api/field/' + rowData.id + '/delete',
                        contentType: 'application/json;charset=utf-8',
                        method: 'delete',
                        success: function (viewData) {
                            viewData = JSON.parse(viewData);
                            if (!viewData.isSucceed) {
                                $.messager.alert('错误', viewData.message);
                                return;
                            }
                            fieldGridArea.datagrid('reload');
                        }
                    })
                }
            });
        }
    }],
    onDblClickRow: function (rowIndex, rowData) {
    }
}

var fieldFormArea = {
    xtype: "formArea",
    title: "添加分析任务字段",
    height: 400,
    fields: [{
        label: "日志分析任务ID",
        readonly: true,
        xtype: "textbox",
        name: "fieldTaskId",
        id: "fieldTaskIdId"
    }, {
        label: "日志分析名称",
        readonly: true,
        xtype: "textbox",
        name: "fieldTaskName",
        id: "fieldTaskNameId"
    }, {
        label: "字段英文KEY，对应存储到ES中的KEY值",
        xtype: "textbox",
        name: "fieldKey",
        id: "fieldKeyId"
    }, {
        label: "字段中文描述",
        xtype: "textbox",
        name: "fieldRemark",
        id: "fieldRemarkId"
    }],
    buttons: [{
        text: "保存",
        id: "saveButton",
        iconCls: "icon-save",
        onClick: function () {
            $.messager.confirm('提示', '确认提交么?', function (r) {
                if (r) {
                    $.ajax({
                        url: __ctx + '/api/field',
                        contentType: 'application/json;charset=utf-8',
                        method: 'post',
                        data: JSON.stringify({
                            taskId: $("#fieldTaskIdId").textbox('getValue'),
                            fieldKey: $("#fieldKeyId").textbox('getValue'),
                            fieldRemark: $("#fieldRemarkId").textbox('getValue')
                        }),
                        success: function (viewData) {
                            viewData = JSON.parse(viewData);
                            if (!viewData.isSucceed) {
                                $.messager.alert('错误', viewData.message);
                                return;
                            }
                            fieldGridArea.datagrid('reload');
                        }
                    })
                }
            });

        }
    }]
}
