package com.bobansavic.agility.vaadin.common.view;

import com.bobansavic.agility.model.*;
import com.bobansavic.agility.service.ProjectService;
import com.bobansavic.agility.service.TaskService;
import com.bobansavic.agility.service.UserService;
import com.bobansavic.agility.vaadin.common.SecurityUtils;
import com.bobansavic.agility.vaadin.ui.utils.UiUtils;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ViewScope
@SpringView
public class EditTicketView extends VerticalLayout implements View {

    private static final Logger log = LoggerFactory.getLogger(EditTicketView.class);

    private UserService userService;
    private TaskService taskService;
    private ProjectService projectService;

    private TextField title;
    private ComboBox<TicketStatus> status;
    private ComboBox<TaskPriority> priority;
    private ComboBox<User> assignee;
    private TextArea description;

    private BeanValidationBinder<TaskStore> binder;
    private TaskStore activeStore;

    private Button saveBtn;
    private Button deleteBtn;
    private boolean edit;

    public EditTicketView() {
    }

    public EditTicketView(UserService userService, TaskService taskService, ProjectService projectService) {
        this.userService = userService;
        this.taskService = taskService;
        this.projectService = projectService;
        setWidth(100, Sizeable.Unit.PERCENTAGE);
        setHeightUndefined();
        setMargin(false);

        FormLayout form = UiUtils.getFormLayout();

        title = new TextField("Title");
        status = new ComboBox<>("Status");
        status.setItems(TicketStatus.values());
        status.setTextInputAllowed(false);
        status.setEmptySelectionAllowed(false);
        priority = new ComboBox<>("Priority");
        priority.setItems(TaskPriority.values());
        priority.setTextInputAllowed(false);
        priority.setEmptySelectionAllowed(false);
        assignee = new ComboBox<>("Assignee");
        assignee.setItems(userService.getAllUsers());
        assignee.setEmptySelectionCaption("None");
        description = new TextArea("Description");
        status.setWidth(50, Unit.PERCENTAGE);
        priority.setWidth(50, Unit.PERCENTAGE);
        assignee.setWidth(50, Unit.PERCENTAGE);
        form.addComponents(title, status, priority, assignee, description);
        addComponent(form);
    }

    public void init(TaskStore task) {
        if (task == null) {
            edit = false;
            task = new TaskStore();
            task.setStatus(TicketStatus.BACKLOG);
            task.setPriority(TaskPriority.Low);
            status.setSelectedItem(task.getStatus());
            priority.setSelectedItem(task.getPriority());
            activeStore = new TaskStore();
        } else {
            activeStore = task;
            edit = true;
            title.setValue(task.getTitle());
            status.setSelectedItem(task.getStatus());
            priority.setSelectedItem(task.getPriority());
            assignee.setSelectedItem(task.getAssignee());
            description.setValue(task.getDescription() != null ? task.getDescription() : "");
        }
        getBinder().readBean(task);
    }

    public void save() {
        activeStore.setTitle(title.getValue());
        activeStore.setStatus(status.getValue());
        activeStore.setPriority(priority.getValue());
        activeStore.setAssignee(assignee.getValue());
        activeStore.setDescription(description.getValue());
        try {
            Project currentProject = projectService.findProjectByTitle(SecurityUtils.getCurrentProjectTitle());
            activeStore.setProject(currentProject);
            taskService.saveTask(activeStore, edit);
        } catch (Exception e) {
            log.error("Error saving task", e);
            UiUtils.showErrorNotification("Failed to save task!");
        }
    }

    public void delete() {
        try {
            taskService.deleteTask(activeStore.getId());
        } catch (Exception e) {
            log.error("Error deleting task", e);
            UiUtils.showErrorNotification("Failed to delete task!");
        }
    }

    public Button getSaveBtn() {
        if (saveBtn == null) {
            saveBtn = UiUtils.createSaveButton();
        }
        return saveBtn;
    }

    public Button getDeleteBtn() {
        if (deleteBtn == null) {
            deleteBtn = new Button("Delete");
            deleteBtn.setStyleName(ValoTheme.BUTTON_DANGER);
        }
        return deleteBtn;
    }

    public BeanValidationBinder<TaskStore> getBinder() {
        if (binder == null) {
            binder = new BeanValidationBinder<>(TaskStore.class);
            binder.bindInstanceFields(this);
            binder.addStatusChangeListener(e -> getSaveBtn().setEnabled(e.getBinder().isValid() && e.getBinder().hasChanges()));
        }
        return binder;
    }

    public TaskStore getActiveStore() {
        return activeStore;
    }
}
