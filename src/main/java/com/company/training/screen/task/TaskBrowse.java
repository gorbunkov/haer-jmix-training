package com.company.training.screen.task;

import com.company.training.app.TaskImportService;
import com.haulmont.yarg.reporting.ReportOutputDocument;
import io.jmix.core.FileRef;
import io.jmix.emailtemplates.EmailTemplates;
import io.jmix.emailtemplates.exception.ReportParameterTypeChangedException;
import io.jmix.emailtemplates.exception.TemplateNotFoundException;
import io.jmix.reports.runner.ReportRunner;
import io.jmix.ui.Notifications;
import io.jmix.ui.UiComponents;
import io.jmix.ui.action.AbstractAction;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.*;
import io.jmix.ui.download.Downloader;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import com.company.training.entity.Task;
import io.jmix.ui.screen.LookupComponent;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;

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

    @Autowired
    private UiComponents uiComponents;

    @Autowired
    private Downloader downloader;
    @Autowired
    private GroupTable<Task> tasksTable;

    @Autowired
    private EmailTemplates emailTemplates;

    @Autowired
    private ReportRunner reportRunner;

    @Subscribe("importTasksBtn")
    public void onImportTasksBtnClick(Button.ClickEvent event) {
        int n = taskImportService.importTasks();
        tasksDl.load();
        notifications.create()
                .withCaption(n + " tasks imported")
                .show();

        tasksDc.getItems();
    }

    @Install(to = "tasksTable.avatar", subject = "columnGenerator")
    private Component tasksTableAvatarColumnGenerator(Task task) {
        if (task.getAttachment() != null) {
            LinkButton linkButton = uiComponents.create(LinkButton.class);
            FileRef attachment = task.getAttachment();
            linkButton.setCaption(attachment.getFileName());
            linkButton.setAction(new AbstractAction() {
                @Override
                public void actionPerform(Component component) {
                    downloader.download(attachment);
                }
            });
            return linkButton;
        }
        return uiComponents.create(Label.class);
    }


    @Subscribe("tasksTable.sendTaskInfo")
    public void onTasksTableSendTaskInfo(Action.ActionPerformedEvent event) throws TemplateNotFoundException, ReportParameterTypeChangedException {
        Task task = tasksTable.getSingleSelected();
        emailTemplates.buildFromTemplate("task-info")
                .setBodyParameter("task", task)
                .setTo("a@a.a")
                .sendEmailAsync();
    }
}