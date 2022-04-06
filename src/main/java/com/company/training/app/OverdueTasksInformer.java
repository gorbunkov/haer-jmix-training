package com.company.training.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("tm_OverdueTasksInformer")
public class OverdueTasksInformer {

    private static final Logger log = LoggerFactory.getLogger(OverdueTasksInformer.class);

    public void sendOverdueTaskNotifications() {
        log.info("Overdue task notifications sent");
    }

}