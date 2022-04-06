package com.company.training.app;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class OverdueTasksNotificationJob implements Job {

    @Autowired
    private OverdueTasksInformer overdueTasksInformer;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        overdueTasksInformer.sendOverdueTaskNotifications();
    }
}
