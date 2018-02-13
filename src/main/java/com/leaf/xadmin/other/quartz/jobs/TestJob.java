package com.leaf.xadmin.other.quartz.jobs;

import com.leaf.xadmin.other.quartz.IBaseJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author leaf
 * <p>date: 2018-02-14 0:04</p>
 * <p>version: 1.0</p>
 */
@Slf4j
public class TestJob implements IBaseJob {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("测试任务......");
    }
}
