package com.balloon.springboot.quartz.service;


import com.balloon.springboot.quartz.entity.QuartzJob;

import java.util.List;

/**
 * 任务操作Service
 *
 * @author liaofuxing
 */
public interface JobService {

//    PageInfo listQuartzJob(String jobName, Integer pageNo, Integer pageSize);

    /**
     * 获取全部 Job
     * @return List<QuartzJob> job 列表
     */
    List<QuartzJob> listJob();

    /**
     * 新增job
     * @param quartz Job 对象
     */
    void saveJob(QuartzJob quartz);

    /**
     * 触发job
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     */
    void triggerJob(String jobName, String jobGroup);

    /**
     * 暂停job
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     */
    void pauseJob(String jobName, String jobGroup);

    /**
     * 恢复job
     * @param jobName 任务名称
     * @param jobGroup 任务名称
     */
    void resumeJob(String jobName, String jobGroup);

    /**
     * 移除job
     * @param jobName 任务名称
     * @param jobGroup  任务分组
     */
    void removeJob(String jobName, String jobGroup);

    /**
     * 根据任务名称和任务分组获取 job
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     * @return QuartzJob 返回任务对象
     */
    QuartzJob getJob(String jobName, String jobGroup);

    /**
     * 更新 job
     * @param quartz 任务对象
     */
    void updateJob(QuartzJob quartz);

    /**
     * 调度任务
     * @param job QuartzJob对象
     * @throws Exception 异常
     */
    void schedulerJob(QuartzJob job) throws Exception;
}
