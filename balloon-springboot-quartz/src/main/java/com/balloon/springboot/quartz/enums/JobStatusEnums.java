package com.balloon.springboot.quartz.enums;


/**
 * 定时任务状态枚举
 *
 * @author liaofuxing
 */
public enum JobStatusEnums {

    RUNNING("RUNNING"),
    COMPLETE("COMPLETE"),
    PAUSED("PAUSED");

    // 响应代码
    private final String status;

    JobStatusEnums(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
