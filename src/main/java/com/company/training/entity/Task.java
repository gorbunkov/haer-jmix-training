package com.company.training.entity;

import io.jmix.core.FileRef;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "TM_TASK", indexes = {
        @Index(name = "IDX_TASK_ASSIGNEE_ID", columnList = "ASSIGNEE_ID"),
        @Index(name = "IDX_TASK_PROJECT_ID", columnList = "PROJECT_ID")
})
@Entity(name = "tm_Task")
public class Task {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @InstanceName
    @Column(name = "NAME", nullable = false)
    @NotNull
    private String name;

    @Future(message = "{msg://com.company.training.entity/Task.dueDate.validation.Future}")
    @Column(name = "DUE_DATE")
    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @Column(name = "PRIORITY")
    private String priority;

    @JoinColumn(name = "ASSIGNEE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User assignee;

    @JoinColumn(name = "PROJECT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @Column(name = "ATTACHMENT", length = 1024)
    private FileRef attachment;

    @Column(name = "SECOND_ATTACHMENT", length = 1024)
    private FileRef secondAttachment;

    @Composition
    @OneToMany(mappedBy = "task")
    private List<Subtask> subtasks;

    @Column(name = "TOTAL_ESTIMATION")
    private Integer totalEstimation;

    public FileRef getSecondAttachment() {
        return secondAttachment;
    }

    public void setSecondAttachment(FileRef secondAttachment) {
        this.secondAttachment = secondAttachment;
    }

    public FileRef getAttachment() {
        return attachment;
    }

    public void setAttachment(FileRef attachment) {
        this.attachment = attachment;
    }

    public Integer getTotalEstimation() {
        return totalEstimation;
    }

    public void setTotalEstimation(Integer totalEstimation) {
        this.totalEstimation = totalEstimation;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public TaskPriority getPriority() {
        return priority == null ? null : TaskPriority.fromId(priority);
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority == null ? null : priority.getId();
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}