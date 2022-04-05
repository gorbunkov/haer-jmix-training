package com.company.training.app;

import com.company.training.entity.Project;
import com.company.training.entity.Task;
import io.jmix.core.DataManager;
import io.jmix.core.EntitySet;
import io.jmix.core.SaveContext;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("tm_TaskImportService")
public class TaskImportService {

    @Autowired
    private DataManager dataManager;

    public int importTasks() {
        List<String> taskNames = obtainTaskNames();

        Project project = dataManager.load(Project.class)
                .query("select p from tm_Project p where p.defaultProject = :defaultProject1")
                .parameter("defaultProject1", true)
                .one();

        List<Task> tasksToCreate = taskNames.stream()
                .map(taskName -> {
                    Task task = dataManager.create(Task.class);
                    task.setName(taskName);
                    task.setProject(project);
                    return task;
                })
                .collect(Collectors.toList());

        EntitySet entitySet = dataManager.save(new SaveContext().saving(tasksToCreate));
        return entitySet.size();
    }

    private List<String> obtainTaskNames() {
        List<String> taskNames = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            taskNames.add("Task " + RandomStringUtils.randomAlphabetic(5));
        }
        return taskNames;
    }

}