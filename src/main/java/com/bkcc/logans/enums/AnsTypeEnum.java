package com.bkcc.logans.enums;

public enum AnsTypeEnum {
    NOT_ANS(1, "不分析直接输出指定字段"),
    COUNT_ANS(2, "通过指定字段聚合分析"),
    COMPARE_ANS(3, "通过指定字段对比修改数据");


    private Integer value;

    private String remark;

    public Integer getValue(){
        return this.value;
    }

    AnsTypeEnum(Integer value, String remark) {
        this.value = value;
        this.remark = remark;
    }

    public boolean equels(Integer value) {
        if (value == null) {
            return false;
        }
        return value.equals(this.value);
    }

}
