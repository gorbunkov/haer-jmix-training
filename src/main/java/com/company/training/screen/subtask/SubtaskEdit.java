package com.company.training.screen.subtask;

import io.jmix.ui.screen.*;
import com.company.training.entity.Subtask;

@UiController("tm_Subtask.edit")
@UiDescriptor("subtask-edit.xml")
@EditedEntityContainer("subtaskDc")
public class SubtaskEdit extends StandardEditor<Subtask> {
}