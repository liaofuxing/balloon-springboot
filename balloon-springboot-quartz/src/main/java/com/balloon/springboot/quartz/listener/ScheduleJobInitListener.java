package com.balloon.springboot.quartz.listener;


import com.balloon.springboot.quartz.entity.QuartzJob;
import com.balloon.springboot.quartz.enums.JobStatusEnums;
import com.balloon.springboot.quartz.service.JobService;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 任务监听器
 * <p>当 springboot 项目启动时 ScheduleJobInitListener 会自动读取数据库的任务列表, 并交由 quartz 调度触发</p>
 *
 * @author liaofuxing
 */
@Component
public class ScheduleJobInitListener implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleJobInitListener.class);

    @Autowired
    private JobService jobService;

    @Autowired
    private Scheduler scheduler;

    @Override
    public void run(String... args) throws Exception {
        loadJobToQuartz();
    }

    private void loadJobToQuartz() throws Exception {
        logger.info("加载 quartz job...");
        List<QuartzJob> jobs = jobService.listJob();
        logger.info("加载到{}个任务...", jobs.size());
        for (QuartzJob job : jobs) {
            try {
                jobService.schedulerJob(job);
            } catch (Exception e) {
                logger.warn("任务{}已在触发列表中, 无须继续添加.", job.getJobName());
            }
            if (JobStatusEnums.PAUSED.getStatus().equals(job.getTriggerState())) {
                scheduler.pauseJob(new JobKey(job.getJobName(), job.getJobGroup()));
            }
        }
    }
}
