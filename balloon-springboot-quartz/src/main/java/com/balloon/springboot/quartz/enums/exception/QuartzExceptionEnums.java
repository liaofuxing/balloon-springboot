package com.balloon.springboot.quartz.enums.exception;


import com.balloon.core.exception.IBaseExceptionEnums;

/**
 * 定时任务异常枚举
 *
 * @author liaofuxing
 */
public enum QuartzExceptionEnums implements IBaseExceptionEnums {

    SAVE_JOB_ERROR(3000, "保存定时任务失败"),
    TRIGGER_JOB_ERROR(3001, "触发定时任务失败"),
    PAUSE_JOB_ERROR(3002, "暂停定时任务失败"),
    RESUME_JOB_ERROR(3003, "恢复定时任务失败"),
    REMOVE_JOB_ERROR(3005, "删除定时任务失败"),
    UPDATE_JOB_ERROR(3006, "修改定时任务失败");

    private final Integer code;

    private final String msg;


    QuartzExceptionEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
