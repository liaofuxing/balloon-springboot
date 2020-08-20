package com.balloon.springboot.quartz.service.impl;

import com.balloon.core.exception.BusinessRuntimeException;
import com.balloon.springboot.quartz.dao.JobDao;
import com.balloon.springboot.quartz.entity.QuartzJob;
import com.balloon.springboot.quartz.enums.JobStatusEnums;
import com.balloon.springboot.quartz.enums.exception.QuartzExceptionEnums;
import com.balloon.springboot.quartz.service.JobService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liaofuxing
 */
@Service
public class JobServiceImpl implements JobService {

    private static final String TRIGGER_IDENTITY = "trigger";

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private JobDao jobDao;

//    @Override
//    public PageInfo listQuartzJob(String jobName, Integer pageNum, Integer pageSize) {
//        PageHelper.startPage(pageNum, pageSize);
//        List<QuartzJob> jobList = jobMapper.listJob(jobName);
//        PageInfo pageInfo = new PageInfo(jobList);
//        return pageInfo;
//    }

    @Override
    public List<QuartzJob> listJob() {
        return jobDao.findAll();
    }

    @Override
    public void saveJob(QuartzJob quartz) {
        try {
            schedulerJob(quartz);

            quartz.setTriggerState(JobStatusEnums.RUNNING.getStatus());
            quartz.setOldJobGroup(quartz.getJobGroup());
            quartz.setOldJobName(quartz.getJobName());
            jobDao.save(quartz);
//            jobDao.saveJob(quartz);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessRuntimeException(QuartzExceptionEnums.SAVE_JOB_ERROR);
        }
    }

    @Override
    public void triggerJob(String jobName, String jobGroup) {
        try {
            JobKey key = new JobKey(jobName, jobGroup);
            scheduler.triggerJob(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessRuntimeException(QuartzExceptionEnums.TRIGGER_JOB_ERROR);
        }
    }

    @Override
    public void pauseJob(String jobName, String jobGroup) {
        try {
            JobKey key = new JobKey(jobName, jobGroup);
            scheduler.pauseJob(key);
            QuartzJob byJobName = jobDao.findByJobName(jobName);
            byJobName.setTriggerState(JobStatusEnums.PAUSED.getStatus());
            jobDao.save(byJobName);
//            jobDao.updateJobStatus(jobName, jobGroup, JobStatus.PAUSED.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessRuntimeException(QuartzExceptionEnums.PAUSE_JOB_ERROR);
        }
    }

    @Override
    public void resumeJob(String jobName, String jobGroup) {
        try {
            JobKey key = new JobKey(jobName, jobGroup);
            scheduler.resumeJob(key);
            QuartzJob byJobName = jobDao.findByJobName(jobName);
            byJobName.setTriggerState(JobStatusEnums.RUNNING.getStatus());
            jobDao.save(byJobName);
//            jobDao.updateJobStatus(jobName,jobGroup, JobStatus.RUNNING.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessRuntimeException(QuartzExceptionEnums.RESUME_JOB_ERROR);
        }
    }

    @Override
    public void removeJob(String jobName, String jobGroup) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(TRIGGER_IDENTITY + jobName, jobGroup);
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
            jobDao.deleteByJobName(jobName);
//            jobDao.removeQuartzJob(jobName, jobGroup);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessRuntimeException(QuartzExceptionEnums.REMOVE_JOB_ERROR);
        }
    }

    @Override
    public QuartzJob getJob(String jobName, String jobGroup) {
        return jobDao.findByJobName(jobName);
    }

    @Override
    public void updateJob(QuartzJob quartz) {
        try {
            // 更新 job, 先调用删除job
            removeJob(quartz.getOldJobName(), quartz.getOldJobGroup());
//            scheduler.deleteJob(new JobKey(quartz.getOldJobName(),quartz.getOldJobGroup()));
            schedulerJob(quartz);

            quartz.setOldJobGroup(quartz.getJobGroup());
            quartz.setOldJobName(quartz.getJobName());

            jobDao.save(quartz);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessRuntimeException(QuartzExceptionEnums.UPDATE_JOB_ERROR);
        }
    }

    @Override
    public void schedulerJob(QuartzJob job) throws Exception {
        //构建job信息
        Class cls = Class.forName(job.getJobClassName());
        //cls.newInstance(); // 检验类是否存在
        JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(job.getJobName(), job.getJobGroup())
                .withDescription(job.getDescription()).build();

        // 触发时间点
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression().trim());
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(TRIGGER_IDENTITY + job.getJobName(), job.getJobGroup())
                .startNow().withSchedule(cronScheduleBuilder).build();

        // 把作业和触发器注册到任务调度中
        scheduler.scheduleJob(jobDetail, trigger);
    }
}
