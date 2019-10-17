package com.bkcc.logans.enums;

public enum LogSourceEnum {

    ES(1, "通过ES获取"),
    HBASE(2, "通过HBase获取");

    private Integer value;

    private String remark;

    public Integer getValue(){
        return this.value;
    }

    LogSourceEnum(Integer value, String remark) {
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
