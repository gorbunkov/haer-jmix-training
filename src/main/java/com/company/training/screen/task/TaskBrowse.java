package com.company.training.screen.task;

import com.company.training.app.TaskImportService;
import io.jmix.ui.Notifications;
import io.jmix.ui.component.Button;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import com.company.training.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("tm_Task.browse")
@UiDescriptor("task-browse.xml")
@LookupComponent("tasksTable")
public class TaskBrowse extends StandardLookup<Task> {

    @Autowired
    private CollectionContainer<Task> tasksDc;
    @Autowired
    private TaskImportService taskImportService;

    @Autowired
    private CollectionLoader<Task> tasksDl;

    @Autowired
    private Notifications notifications;

    @Subscribe("importTasksBtn")
    public void onImportTasksBtnClick(Button.ClickEvent event) {
        int n = taskImportService.importTasks();
        tasksDl.load();
        notifications.create()
                .withCaption(n + " tasks imported")
                .show();

        tasksDc.getItems();
    }
}