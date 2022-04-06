package com.company.training.screen.task;

import com.company.training.entity.Project;
import com.company.training.entity.Subtask;
import com.company.training.entity.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.jmix.core.DataManager;
import io.jmix.core.FileStorage;
import io.jmix.core.FileStorageLocator;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.email.*;
import io.jmix.ui.component.*;
import io.jmix.ui.model.CollectionPropertyContainer;
import io.jmix.ui.model.DataContext;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import com.company.training.entity.Task;
import io.jmix.ui.upload.TemporaryStorage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@UiController("tm_Task.edit")
@UiDescriptor("task-edit.xml")
@EditedEntityContainer("taskDc")
public class TaskEdit extends StandardEditor<Task> {

    @Autowired
    private CurrentAuthentication currentAuthentication;

    @Autowired
    private BrowserFrame attachmentBrowserFrame;

    @Autowired
    private TemporaryStorage temporaryStorage;

    @Autowired
    private FileStorageUploadField uploadSubtasks;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private CollectionPropertyContainer<Subtask> subtasksDc;
    @Autowired
    private DataContext dataContext;
    @Autowired
    private Emailer emailer;
    @Autowired
    private FileStorageLocator fileStorageLocator;

    @Autowired
    private Configuration freemarkerConfiguration;

    private static final Logger log = LoggerFactory.getLogger(TaskEdit.class);

    @Subscribe
    public void onInitEntity(InitEntityEvent<Task> event) {
        User user = (User) currentAuthentication.getUser();
        event.getEntity().setAssignee(user);
    }

    @Subscribe("projectField")
    public void onProjectFieldValueChange(HasValue.ValueChangeEvent<Project> event) {
        if (event.isUserOriginated()) {
            Project project = event.getValue();
            if (project != null) {
                getEditedEntity().setPriority(project.getDefaultTaskPriority());
            }
        }
    }

//    @Subscribe(id = "taskDc", target = Target.DATA_CONTAINER)
//    public void onTaskDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<Task> event) {
//        if ("project".equals(event.getProperty())) {
//            Project newProject = (Project) event.getValue();
//            if (newProject != null) {
//                event.getItem().setPriority(newProject.getDefaultTaskPriority());
//            }
//        }
//    }

    private void refreshAttachmentBrowserFrame() {
        Task task = getEditedEntity();
        if (task.getAttachment() != null) {
            attachmentBrowserFrame.setSource(FileStorageResource.class)
                    .setFileReference(task.getAttachment())
                    .setMimeType(task.getAttachment().getContentType());
        }
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        refreshAttachmentBrowserFrame();
    }

    @Subscribe(id = "taskDc", target = Target.DATA_CONTAINER)
    public void onTaskDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<Task> event) {
        if ("attachment".equals(event.getProperty())) {
            refreshAttachmentBrowserFrame();
        }
    }

    @Subscribe("uploadSubtasks")
    public void onUploadSubtasksFileUploadSucceed(SingleFileUploadField.FileUploadSucceedEvent event) throws IOException {
        UUID fileId = uploadSubtasks.getFileId();
        File file = temporaryStorage.getFile(fileId);
        List<String> subtasksNames = FileUtils.readLines(file, StandardCharsets.UTF_8);
        for (String subtasksName : subtasksNames) {
            Subtask subtask = dataContext.create(Subtask.class);
            subtask.setName(subtasksName);
            subtask.setTask(getEditedEntity());
            subtasksDc.getMutableItems().add(subtask);
        }
    }

    @Subscribe("notifyAssigneeBtn")
    public void onNotifyAssigneeBtnClick(Button.ClickEvent event) throws EmailException, IOException {
        Task task = getEditedEntity();

        FileStorage fileStorage = fileStorageLocator.getDefault();

        byte[] bytes;
        try (InputStream is = fileStorage.openStream(task.getAttachment())) {
            bytes = IOUtils.toByteArray(is);
        }

        EmailAttachment emailAttachment = new EmailAttachment(bytes, task.getAttachment().getFileName());
        EmailAttachment avatarAttachment = new EmailAttachment(task.getAssignee().getAvatar(), "avatar.jpeg", "avatar_id");

        EmailInfo emailInfo = EmailInfoBuilder.create()
                .setAddresses(task.getAssignee().getEmail())
                .setSubject("Task assigned")
                .setBody(generateEmailBody(task))
                .addAttachment(emailAttachment)
                .addAttachment(avatarAttachment)
                .setBodyContentType("text/html;charset=UTF-8")
                .setImportant(true)
                .build();
        emailer.sendEmailAsync(emailInfo);
    }

    private String generateEmailBody(Task task) throws IOException {
        Template template = freemarkerConfiguration.getTemplate("task-email.ftl");

        Map<String, Object> data = new HashMap<>();
        data.put("task", task);

        StringWriter stringWriter = new StringWriter();
        try {
            template.process(data, stringWriter);
        } catch (TemplateException e) {
            log.error("Error processing template", e);
        }

        return stringWriter.toString();
    }
//    private String generateEmailBody(Task task) {
//        String template = """
//                <html>
//                    <body>
//                        <h1>Hello!</h1>
//                        <p>Task <b>%s</b> is assigned;
//                    </body>
//                </html>
//                """;
//        return String.format(template, task.getName());
//    }
}