package com.company.training.listener;

import com.company.training.entity.Subtask;
import com.company.training.entity.Task;
import io.jmix.core.DataManager;
import io.jmix.core.Id;
import io.jmix.core.event.EntityChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component("tm_SubtaskEventListener")
public class SubtaskEventListener {

    @Autowired
    private DataManager dataManager;

    @EventListener
    public void onSubtaskChangedBeforeCommit(EntityChangedEvent<Subtask> event) {
        Task task;
        if (event.getType() == EntityChangedEvent.Type.DELETED) {
            Id<Task> taskId = event.getChanges().getOldReferenceId("task");
            task = dataManager.load(taskId).one();
        } else {
            Subtask subtask = dataManager.load(event.getEntityId()).one();
            task = subtask.getTask();
        }

        Integer totalEstimation = task.getSubtasks().stream()
                .map(Subtask::getEstimation)
                .reduce(0, Integer::sum);

        task.setTotalEstimation(totalEstimation);
        dataManager.save(task);
    }
}