package com.leaf.xadmin.controller;

import com.leaf.xadmin.other.quartz.utils.QuartzCommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author leaf
 * <p>date: 2018-02-14 0:26</p>
 * <p>version: 1.0</p>
 */
@Api(value = "任务相关请求", description = "/job")
@RestController
@RequestMapping("job")
@CrossOrigin
@Slf4j
public class JobController {

    @Autowired
    private QuartzCommonUtil quartzCommonUtil;

    @ApiOperation(value = "添加任务")
    @PostMapping(value = "add")
    public void add(@RequestParam(value = "jobClassName")String jobClassName,
                    @RequestParam(value = "jobGroupName")String jobGroupName,
                    @RequestParam(value = "cronExpression")String cronExpression) {
        quartzCommonUtil.add(jobClassName, jobGroupName, cronExpression);
    }

    @ApiOperation(value = "删除任务")
    @PostMapping(value = "delete")
    public void delete(@RequestParam(value = "jobClassName")String jobClassName,
                    @RequestParam(value = "jobGroupName")String jobGroupName) {
        quartzCommonUtil.delete(jobClassName, jobGroupName);
    }

    @ApiOperation(value = "暂停任务")
    @PostMapping(value = "pause")
    public void pause(@RequestParam(value = "jobClassName")String jobClassName,
                    @RequestParam(value = "jobGroupName")String jobGroupName) {
        quartzCommonUtil.pause(jobClassName, jobGroupName);
    }

    @ApiOperation(value = "继续任务")
    @PostMapping(value = "resume")
    public void resume(@RequestParam(value = "jobClassName")String jobClassName,
                    @RequestParam(value = "jobGroupName")String jobGroupName) {
        quartzCommonUtil.resume(jobClassName, jobGroupName);
    }

    @ApiOperation(value = "重新安排任务")
    @PostMapping(value = "reschedule")
    public void reschedule(@RequestParam(value = "jobClassName")String jobClassName,
                    @RequestParam(value = "jobGroupName")String jobGroupName,
                    @RequestParam(value = "cronExpression")String cronExpression) {
        quartzCommonUtil.reschedule(jobClassName, jobGroupName, cronExpression);
    }
}
