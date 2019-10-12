var fieldEnumGridArea = {
    xtype: 'gridArea',
    url: '/api/field-enum/list4view',
    singleSelect: true,
    queryParams: {taskId: 0, fieldId: 0},
    columns: [[
        {field: 'id', checkbox: true},

        {field: 'taskId', title: '日志分析任务ID', width: 40, formatter: function (value) {
                return $("#fieldEnumTaskIdId").textbox('getValue');
            }
        },
        {field: 'taskName', title: '日志分析任务名称', width: 40, formatter: function (value) {
                return $("#fieldEnumTaskNameId").textbox('getValue');
            }
        },
        {field: 'fieldKey', title: '字段英文KEY', width: 20, formatter: function (value) {
                return $("#fieldEnumFieldKeyId").textbox('getValue');
            }
        },
        {field: 'enumValue', title: '字段对应枚举值', width: 20},
        {field: 'allowRegex', title: '允许范围内的值正则表达式', width: 20},
    ]],
    buttons: [{
        text: '返回到任务页面',
        iconCls: 'icon-back',
        onClick: function () {
            taskViewArea.showView();
            taskGridArea.datagrid('reload');
        }
    },{
        text: '返回到配置字段页面',
        iconCls: 'icon-back',
        onClick: function () {
            fieldViewArea.showView();
            fieldGridArea.datagrid('reload');
        }
    },{
        text: '删除分析任务字段',
        iconCls: 'icon-remove',
        onClick: function () {
            var rowData = fieldEnumGridArea.datagrid('getSelected');
            if (!rowData) {
                $.alert.info('请选中一条记录');
                return;
            }
            $.messager.confirm('提示', '确认删除分析任务?', function (r) {
                if (r) {
                    $.ajax({
                        url: __ctx + '/api/field-enum/' + rowData.id + '/delete',
                        contentType: 'application/json;charset=utf-8',
                        method: 'delete',
                        success: function (viewData) {
                            viewData = JSON.parse(viewData);
                            if (!viewData.isSucceed) {
                                $.messager.alert('错误', viewData.message);
                                return;
                            }
                            fieldEnumGridArea.datagrid('reload');
                        }
                    })
                }
            });
        }
    }],
    onDblClickRow: function (rowIndex, rowData) {
    }
}

var fieldEnumFormArea = {
    xtype: "formArea",
    title: "添加分析任务字段",
    height: 400,
    fields: [{
        label: "分析任务ID",
        readonly: true,
        xtype: "textbox",
        name: "fieldEnumTaskId",
        id: "fieldEnumTaskIdId"
    }, {
        label: "分析名称",
        readonly: true,
        xtype: "textbox",
        name: "fieldEnumTaskName",
        id: "fieldEnumTaskNameId"
    }, {
        label: "字段ID",
        readonly: true,
        xtype: "textbox",
        name: "fieldEnumFieldId",
        id: "fieldEnumFieldIdId"
    }, {
        label: "字段名称",
        readonly: true,
        xtype: "textbox",
        name: "fieldEnumFieldKey",
        id: "fieldEnumFieldKeyId"
    }, {
        label: "字段对应枚举值",
        xtype: "textbox",
        name: "enumValue",
        id: "enumValueId"
    }, {
        label: "允许范围内的值正则表达式",
        xtype: "textbox",
        name: "allowRegex",
        id: "allowRegexId"
    }],
    buttons: [{
        text: "保存",
        id: "saveButton",
        iconCls: "icon-save",
        onClick: function () {
            $.messager.confirm('提示', '确认提交么?', function (r) {
                if (r) {
                    $.ajax({
                        url: __ctx + '/api/field-enum',
                        contentType: 'application/json;charset=utf-8',
                        method: 'post',
                        data: JSON.stringify({
                            taskId: $("#fieldEnumTaskIdId").textbox('getValue'),
                            fieldId: $("#fieldEnumFieldIdId").textbox('getValue'),
                            allowRegex: $("#allowRegexId").textbox('getValue'),
                            enumValue: $("#enumValueId").textbox('getValue')
                        }),
                        success: function (viewData) {
                            viewData = JSON.parse(viewData);
                            if (!viewData.isSucceed) {
                                $.messager.alert('错误', viewData.message);
                                return;
                            }
                            fieldEnumGridArea.datagrid('reload');
                        }
                    })
                }
            });

        }
    }]
}
