var resGridArea = {
    xtype: 'gridArea',
    url: '/api/task-res/list4view',
    title: "分析任务结果",
    queryParams: {taskId: 0},
    height: 400,
    singleSelect: true,
    columns: [[
        {field: 'id', checkbox: true},
        {field: 'taskId', title: '日志分析任务ID', width: 20},
        {field: 'orderNO', title: '任务执行单号', width: 20},
        {field: 'beginTime', title: '任务分析开始时间', width: 20},
        {field: 'endTime', title: '任务分析结束时间', width: 20},
        {field: 'errorCode', title: '错误码', width: 15},
        {field: 'resJSON', title: '分析结果JSON字符串', width: 100},
    ]]
}

