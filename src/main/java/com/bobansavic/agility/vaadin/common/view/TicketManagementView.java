package com.bobansavic.agility.vaadin.common.view;

import com.bobansavic.agility.model.TaskStore;
import com.bobansavic.agility.service.ProjectService;
import com.bobansavic.agility.service.TaskService;
import com.bobansavic.agility.service.UserService;
import com.bobansavic.agility.vaadin.common.Menu;
import com.bobansavic.agility.vaadin.common.UiPart;
import com.bobansavic.agility.vaadin.ui.component.ConfirmDeleteDialog;
import com.bobansavic.agility.vaadin.ui.component.FormWindow;
import com.bobansavic.agility.vaadin.ui.utils.UiUtils;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

@SpringView
@Menu(icon = VaadinIcons.PLUS_CIRCLE, name = "Create new ticket", order = 500, availiableFor = {UiPart.ADMIN, UiPart.PM, UiPart.PROJECT})
public class TicketManagementView extends VerticalLayout implements View {
    private final static Logger log = LoggerFactory.getLogger(TicketManagementView.class);

    private ApplicationContext applicationContext;
    private ConfirmDeleteDialog confirmDialog;

    public void edit(TaskStore task, UserService userService, TaskService taskService, ProjectService projectService) {
        EditTicketView formView = new EditTicketView(userService, taskService, projectService);
        String windowTitle = "Create new task";

        Button saveBtn = formView.getSaveBtn();
        Button deleteBtn = formView.getDeleteBtn();

        if (task != null) {
            windowTitle = "Edit task";

            confirmDialog = new ConfirmDeleteDialog("Delete this task?");
            confirmDialog.addYesAction(e -> {
                formView.delete();
                UI.getCurrent().getPage().reload();
                UiUtils.showSuccessNotification("Ticket deleted!");
            });
            deleteBtn.addClickListener(e -> {
                if (this.confirmDialog.getParent() == null) {
                    UI.getCurrent().addWindow(confirmDialog);
                }
            });
        }

        saveBtn.addClickListener(e -> {
            if (formView.getBinder().writeBeanIfValid(formView.getActiveStore())) {
                try {
                    formView.save();
                    UiUtils.showSuccessNotification("Successfully saved ticket!");
//                    window.close();
                    UI.getCurrent().getPage().reload();
                } catch (Exception ex) {
                    log.error("Error saving user: {}", ex.getMessage());
                    UiUtils.showErrorNotification("Failed to save ticket!");
                }
            }


        });

        FormWindow window;

        if (task != null) {
            window = new FormWindow(windowTitle, saveBtn, deleteBtn);
        } else {
            window = new FormWindow(windowTitle);
            window.addFooterBtn(saveBtn);
        }
        formView.init(task);
        window.setFormView(formView);

        UI.getCurrent().getNavigator().navigateTo(UiUtils.getKanbanBoardViewName());
        UI.getCurrent().addWindow(window);
    }
}
