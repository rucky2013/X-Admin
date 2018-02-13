package com.leaf.xadmin.other.quartz.utils;

import com.leaf.xadmin.other.quartz.IBaseJob;
import com.leaf.xadmin.vo.enums.ErrorStatus;
import com.leaf.xadmin.vo.exception.GlobalException;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author leaf
 * <p>date: 2018-02-14 0:09</p>
 * <p>version: 1.0</p>
 */
@Component
public class QuartzCommonUtil {

    @Autowired
    private Scheduler scheduler;

    /**
     * 添加新任务
     *
     * @param jobClassName
     * @param jobGroupName
     * @param cronExpression
     * @throws Exception
     */
    public void add(String jobClassName, String jobGroupName, String cronExpression) {
        try {
            // 启动调度器
            scheduler.start();
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName, jobGroupName).build();
            // 表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName)
                    .withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            throw new GlobalException(ErrorStatus.QUARTZ_JOB_ERROR);
        }
    }

    /**
     * 暂停任务
     *
     * @param jobClassName
     * @param jobGroupName
     * @throws Exception
     */
    public void pause(String jobClassName, String jobGroupName) {
        try {
            scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            throw new GlobalException(ErrorStatus.QUARTZ_JOB_ERROR);
        }
    }

    /**
     * 继续任务
     *
     * @param jobClassName
     * @param jobGroupName
     * @throws Exception
     */
    public void resume(String jobClassName, String jobGroupName) {
        try {
            scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            throw new GlobalException(ErrorStatus.QUARTZ_JOB_ERROR);
        }
    }

    /**
     * 重新安排任务
     *
     * @param jobClassName
     * @param jobGroupName
     * @param cronExpression
     * @throws Exception
     */
    public void reschedule(String jobClassName, String jobGroupName, String cronExpression) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            throw new GlobalException(ErrorStatus.QUARTZ_JOB_ERROR);
        }
    }

    /**
     * 删除任务
     *
     * @param jobClassName
     * @param jobGroupName
     * @throws Exception
     */
    public void delete(String jobClassName, String jobGroupName) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
            scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            throw new GlobalException(ErrorStatus.QUARTZ_JOB_ERROR);
        }
    }

    /**
     * 获取类实例
     *
     * @param classname
     * @return
     * @throws Exception
     */
    private static IBaseJob getClass(String classname) throws Exception {
        Class<?> clazz = Class.forName(classname);
        return (IBaseJob) clazz.newInstance();
    }
}
