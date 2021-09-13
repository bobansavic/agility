package com.bobansavic.agility.vaadin.common.view;

import com.bobansavic.agility.service.ProjectService;
import com.bobansavic.agility.vaadin.common.Menu;
import com.bobansavic.agility.vaadin.common.SecurityUtils;
import com.bobansavic.agility.vaadin.common.UiPart;
import com.bobansavic.agility.vaadin.ui.component.ConfirmDeleteDialog;
import com.bobansavic.agility.vaadin.ui.component.ControlButtonLayout;
import com.bobansavic.agility.vaadin.ui.utils.UiUtils;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Transactional
@SpringView
@UIScope
@Menu(icon = VaadinIcons.COGS, name = "Settings", order = 2000, availiableFor = {UiPart.ADMIN, UiPart.PM, UiPart.PROJECT})
public class ProjectDetailView extends VerticalLayout implements View {
    private final static Logger log = LoggerFactory.getLogger(ProjectDetailView.class);

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ApplicationContext applicationContext;

    private Panel main;
    private ConfirmDeleteDialog confirmDialog;

    @PostConstruct
    public void postConstruct() {
        setMargin(new MarginInfo(true, false, false, false));
        main = new Panel();
        main.addStyleName("form-window-main");
        main.setSizeFull();
        main.addStyleName(ValoTheme.PANEL_BORDERLESS);

//        Project entity = projectService.findProjectByTitle(SecurityUtils.getCurrentProjectTitle());
        EditProjectView formView = applicationContext.getBean(EditProjectView.class);
        formView.init(SecurityUtils.getCurrentProjectTitle());
        Button saveBtn = formView.getSaveBtn();
        saveBtn.addClickListener(s -> {
            if (formView.getBinder().writeBeanIfValid(formView.getActiveStore())) {
                try {
                    formView.save();
                    UiUtils.showSuccessNotification("Successfully saved project!");
                } catch (Exception ex) {
                    log.error("Error saving project: {}", ex.getMessage());
                    UiUtils.showErrorNotification("A project with that title already exists!");
                }
            }
        });
        ControlButtonLayout controls = new ControlButtonLayout();
        controls.removeActionMenu();
        controls.addLeft(saveBtn);

        confirmDialog = new ConfirmDeleteDialog("Are you sure you'd like to delete this project?");
        Button deleteBtn = new Button("Delete project");
        deleteBtn.setStyleName(ValoTheme.BUTTON_DANGER);
        confirmDialog.addYesAction(e -> {
            projectService.deleteProject(SecurityUtils.getCurrentProjectTitle());

            SecurityUtils.setProjectView(false);
            UI.getCurrent().getPage().setLocation("");
            UiUtils.showSuccessNotification("Project deleted!");
        });
        deleteBtn.addClickListener(e -> {
            if (this.confirmDialog.getParent() == null) {
                UI.getCurrent().addWindow(confirmDialog);
            }
        });
        controls.addRight(deleteBtn);
        addComponent(controls);
        main.setContent(formView);
        addComponentsAndExpand(main);
    }
}
