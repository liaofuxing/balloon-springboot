package com.balloon.springboot.quartz.dao;

import com.balloon.springboot.quartz.entity.QuartzJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author liaofuxing
 */
@Repository
public interface JobDao extends JpaRepository<QuartzJob, Integer> {
    QuartzJob findByJobName(String jobName);

    void deleteByJobName(String jobName);
}

