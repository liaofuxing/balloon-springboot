package com.balloon.springboot.quartz.entity;


import lombok.Data;

import javax.persistence.*;

/**
 * @author liaofuxing
 */
@Data
@Table
@Entity
public class QuartzJob {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 任务名称
	 */
	private String jobName;

	/**
	 * 任务分组
	 */
	private String jobGroup;

	/**
	 * 任务描述
	 */
	private String description;

	/**
	 * 执行类
	 */
	private String jobClassName;

	/**
	 * 执行时间, cron 表达式
	 */
	private String cronExpression;
//	private String triggerName;//执行时间

	/**
	 * 任务状态
	 */
	private String triggerState;

	private String oldJobName;//任务名称 用于修改
	private String oldJobGroup;//任务分组 用于修改

	public QuartzJob() {
		super();
	}
	public QuartzJob(String jobName, String jobGroup, String description, String jobClassName, String cronExpression, String triggerState) {
        super();
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.description = description;
        this.jobClassName = jobClassName;
        this.cronExpression = cronExpression;
        this.triggerState = triggerState;
    }


}
