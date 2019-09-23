package com.bkcc.logans.enums;

public enum CalendarEnum {

    SECOND(0, "秒"),
    MINUTE(1, "分钟"),
    HOUR(2, "小时"),
    DAY(3, "日"),
    MONTH(4, "月"),
    WEEK(5, "周"),
    YEAR(6, "年");


    private Integer value;

    private String remark;

    CalendarEnum(Integer value, String remark) {
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
