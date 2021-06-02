package com.bobansavic.agility.web.common.views;

import com.bobansavic.agility.clojure.ClojureDataAccessService;
import com.bobansavic.agility.model.Project;
import com.bobansavic.agility.model.User;
import com.bobansavic.agility.service.ProjectService;
import com.bobansavic.agility.service.UserService;
import com.bobansavic.agility.web.common.Menu;
import com.bobansavic.agility.web.common.SecurityUtils;
import com.bobansavic.agility.web.common.UiPart;
import com.bobansavic.agility.web.ui.component.ControlButtonLayout;
import com.bobansavic.agility.web.ui.component.FormWindow;
import com.bobansavic.agility.web.ui.utils.UiUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringView
@UIScope
@Menu(icon = VaadinIcons.HOME, name = "Projects", order = 100, availiableFor = {UiPart.ADMIN, UiPart.PM, UiPart.USER})
public class ProjectManagementView extends VerticalLayout implements View {

    private final static Logger log = LoggerFactory.getLogger(ProjectManagementView.class);

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private ClojureDataAccessService clojureDataAccessService;
    @Autowired
    private ApplicationContext applicationContext;

    @Value("${use-clojure-data-access-module}")
    private boolean useClojureModule;

    private VerticalLayout holderLayout;

    @PostConstruct
    public void postConstruct() {
        ControlButtonLayout controls = new ControlButtonLayout();
        controls.removeActionMenu();
        holderLayout = new VerticalLayout();

        Button createBtn = new Button("Create new project");
        if (SecurityUtils.isAdmin() || SecurityUtils.isProjectManager()) {
            createBtn.setDescription("Create a new project");
            createBtn.addClickListener(e -> {
                EditProjectView formView = applicationContext.getBean(EditProjectView.class);
                formView.init(null);
                FormWindow window = new FormWindow("Create new project");
                window.setFormView(formView);
                Button saveBtn = formView.getSaveBtn();
                window.addFooterBtn(saveBtn);
                saveBtn.addClickListener(s -> {
                    if (formView.getBinder().writeBeanIfValid(formView.getActiveStore())) {
                        try {
                            formView.save();
                            UiUtils.showSuccessNotification("Successfully saved project!");
                            window.close();
                            loadProjects();
                        } catch (Exception ex) {
                            log.error("Error saving project: {}", ex.getMessage());
                            UiUtils.showErrorNotification("A project with that title already exists!");
                        }
                    }
                });
                UI.getCurrent().addWindow(window);
            });
        } else {
            createBtn.setDescription("You do not have permission to create new projects.");
            createBtn.setEnabled(false);
        }
        setSizeFull();
        setHeightUndefined();

        controls.addLeft(createBtn);
        addComponent(controls);
        loadProjects();
    }

    private List<HorizontalLayout> generateProjectComponents() {
        Set<Project> projectsToLoad = null;

        if (SecurityUtils.isAdmin() || SecurityUtils.isProjectManager()) {
            if (clojureDataAccessService.isClojureServiceAvailable() && useClojureModule) {
                try {
                    projectsToLoad = clojureDataAccessService.getAllProjects();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                projectsToLoad = projectService.getAllProjects();
            }
        } else {
            if (clojureDataAccessService.isClojureServiceAvailable() && useClojureModule) {
                String value;
                if (Strings.isNullOrEmpty(SecurityUtils.getCurrentUser().getUsername())) {
                    value = SecurityUtils.getCurrentUser().getEmail();
                } else {
                    value = SecurityUtils.getCurrentUser().getUsername();
                }
                try {
                    projectsToLoad = clojureDataAccessService.findProjectsByEmailOrUsername(value);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                projectsToLoad = userService.findByUsername(SecurityUtils.getCurrentUser().getUsername()).getProjects();
            }

        }
        List<HorizontalLayout> projectHolders = new ArrayList<>();
        if (projectsToLoad != null && !projectsToLoad.isEmpty()) {
            for (Project p : projectsToLoad) {
                HorizontalLayout projectLabelHolder = new HorizontalLayout();
                projectLabelHolder.setStyleName("project-label-holder");
                projectLabelHolder.setSizeUndefined();

                Label projectLabel = new Label(p.getTitle(), ContentMode.HTML);
                projectLabel.setSizeFull();
                projectLabel.addStyleName(ValoTheme.LABEL_H3);
                projectLabel.addStyleName(ValoTheme.LABEL_LIGHT);
                projectLabel.setStyleName("project-label");
                projectLabel.setSizeUndefined();

                projectLabelHolder.addComponent(projectLabel);
                projectLabelHolder.addLayoutClickListener(e -> {
                    SecurityUtils.setProjectView(true);
                    SecurityUtils.setCurrentProjectTitle(p.getTitle());
                    UI.getCurrent().getPage().setLocation("");
                });
                projectHolders.add(projectLabelHolder);
            }
        }
        return projectHolders;
    }

    private void loadProjects() {
        removeComponent(holderLayout);
        holderLayout = new VerticalLayout();
        for (HorizontalLayout projectLabelHolder : generateProjectComponents()) {
            holderLayout.addComponent(projectLabelHolder);
        }
        holderLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        addComponent(holderLayout);
        setExpandRatio(holderLayout, 1);
    }
}
