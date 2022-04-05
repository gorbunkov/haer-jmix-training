package com.company.training.security;

import com.company.training.entity.Task;
import com.company.training.entity.TaskPriority;
import io.jmix.security.model.RowLevelPolicyAction;
import io.jmix.security.model.RowLevelPredicate;
import io.jmix.security.role.annotation.JpqlRowLevelPolicy;
import io.jmix.security.role.annotation.PredicateRowLevelPolicy;
import io.jmix.security.role.annotation.RowLevelRole;

@RowLevelRole(name = "View assigned tasks", code = "view-assigned-tasks")
public interface ViewAssignedRole {

    @JpqlRowLevelPolicy(entityClass = Task.class,
        where = "{E}.assignee.id = :current_user_id")
    void tasks();

    @PredicateRowLevelPolicy(entityClass = Task.class,
        actions = {RowLevelPolicyAction.CREATE})
    default RowLevelPredicate<Task> createOnlyNotHighTasks() {
        return task -> task.getPriority() != TaskPriority.HIGH;
    }

}
