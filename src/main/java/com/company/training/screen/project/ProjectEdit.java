package com.company.training.screen.project;

import io.jmix.ui.screen.*;
import com.company.training.entity.Project;

@UiController("tm_Project.edit")
@UiDescriptor("project-edit.xml")
@EditedEntityContainer("projectDc")
public class ProjectEdit extends StandardEditor<Project> {
}