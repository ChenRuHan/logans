package com.bkcc.logans.enums;

public enum TaskStatusEnum {

    OPEN(1, "开启"),
    CLOSE(0, "暂停");

    private Integer value;

    private String remark;

    public Integer getValue(){
        return this.value;
    }

    TaskStatusEnum(Integer value, String remark) {
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
