package com.bobansavic.agility.web.common.views;

import com.bobansavic.agility.clojure.ClojureDataAccessService;
import com.bobansavic.agility.model.Project;
import com.bobansavic.agility.model.User;
import com.bobansavic.agility.service.ProjectService;
import com.bobansavic.agility.service.UserService;
import com.bobansavic.agility.web.ui.utils.UiUtils;
import com.google.common.base.Strings;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Transactional
@ViewScope
@SpringView
public class EditProjectView extends VerticalLayout implements View {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private ClojureDataAccessService clojureDataAccessService;

    @Value("${use-clojure-data-access-module}")
    private boolean useClojureDAM;

    private TextField title;
    private Grid<User> grid;
    private ComboBox<User> userToAdd;
    private Button addUser;
    private Button removeUser;

    private Button saveBtn;

    private BeanValidationBinder<Project> binder;
    private Project activeStore;
    private List<User> usersInProject = new ArrayList<>();
    private List<User> usersToAdd = new ArrayList<>();
    private List<User> usersToRemove = new ArrayList<>();

    private List<User> allUsers = new ArrayList<>();
    private User attachedEntity;

    @PostConstruct
    public void postConstruct() {
        setWidthFull();
        setHeightUndefined();
        setMargin(false);

        FormLayout form = UiUtils.getFormLayout();

        title = new TextField("Project title");
        form.addComponent(title);
        addComponent(form);

        form = UiUtils.getFormLayout();
        Label section = new Label("Users");
        section.addStyleName(ValoTheme.LABEL_H3);
        section.addStyleName("form-section");
        form.addComponent(section);
        addComponent(form);

        addUsersComponent();
    }

    public void init(String project) {
        if (Strings.isNullOrEmpty(project)) {
            activeStore = new Project();
        } else {
            activeStore = projectService.findProjectByTitle(project);
            title.setValue(activeStore.getTitle());
            usersInProject = new ArrayList<>(activeStore.getUsers());
        }
        allUsers = userService.getAllUsers();
        userToAdd.setItems(allUsers);
        loadUsersGrid();
        getBinder().readBean(activeStore);
    }

    public void save() {
        activeStore.setTitle(title.getValue());
        for (User user : usersToAdd) {
            attachedEntity = userService.findByUsername(user.getUsername());
            activeStore.addUser(attachedEntity);
        }
        for (User user : usersToRemove) {
            activeStore.removeUser(user);
        }
        if (clojureDataAccessService.isClojureServiceAvailable() && useClojureDAM) {
            if (!clojureDataAccessService.checkIfProjectExists(activeStore.getTitle())) {
                clojureDataAccessService.createProject(activeStore.getTitle());
            } else {
                projectService.saveProject(activeStore);
            }
        } else {
            projectService.saveProject(activeStore);
        }
        usersToAdd.clear();
        usersToRemove.clear();
        init(activeStore.getTitle());
    }

    public Button getSaveBtn() {
        if (saveBtn == null) {
            saveBtn = UiUtils.createSaveButton();
        }
        return saveBtn;
    }

    public BeanValidationBinder<Project> getBinder() {
        if (binder == null) {
            binder = new BeanValidationBinder<>(Project.class);
            binder.bindInstanceFields(this);
            binder.addStatusChangeListener(e -> getSaveBtn().setEnabled(e.getBinder().isValid() && e.getBinder().hasChanges()));
        }
        return binder;
    }

    public Project getActiveStore() {
        return activeStore;
    }

    private void addUsersComponent() {
        VerticalLayout usersLayout = new VerticalLayout();
        usersLayout.setMargin(new MarginInfo(false, true, false, true));

        HorizontalLayout controls = new HorizontalLayout();
        controls.setSizeFull();

        userToAdd = new ComboBox<>();
        userToAdd.setWidthFull();
        userToAdd.setPlaceholder("Choose a user to add to the project");
        userToAdd.addValueChangeListener(e -> addUser
                .setEnabled(userToAdd.getValue() != null && !usersInProject.contains(userToAdd.getValue())));

        addUser = new Button(VaadinIcons.PLUS);
        addUser.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        addUser.setDescription("Add user to project.");
        addUser.setEnabled(false);
        addUser.addClickListener(e -> {
            if (!isUserAlreadyInProject(userToAdd.getValue())) {
                addUserToProject(userToAdd.getValue());
                triggerFormChange();
            } else {
                UiUtils.showErrorNotification("User already in project.");
            }
        });

        removeUser = new Button(VaadinIcons.TRASH);
        removeUser.setDescription("Remove user from project.");
        removeUser.addStyleName(ValoTheme.BUTTON_DANGER);
        removeUser.setEnabled(false);
        removeUser.addClickListener(e ->{
            Set<User> selectedUsers = grid.getSelectedItems();
            if (!selectedUsers.isEmpty()) {
                for (User user : selectedUsers) {
                    usersInProject.remove(user);
                    usersToAdd.remove(user);
                    usersToRemove.add(user);
                }
                triggerFormChange();
                loadUsersGrid();
            }
        });
        controls.addComponents(userToAdd, addUser, removeUser);
        controls.setExpandRatio(userToAdd, 1);
        usersLayout.addComponent(controls);
        usersLayout.setComponentAlignment(controls, Alignment.MIDDLE_RIGHT);

        grid = new Grid<>();
        grid.setHeight(200, Unit.PIXELS);
        grid.setWidthFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addColumn(User::getFullName).setCaption("Name");
        grid.addColumn(User::getEmail).setCaption("Email");
        grid.addSelectionListener(e -> removeUser.setEnabled(!e.getAllSelectedItems().isEmpty()));
        grid.getEditor().setEnabled(true);
        grid.getEditor().setSaveCaption("Save");
        grid.getEditor().setCancelCaption("Cancel");
        grid.getEditor().addSaveListener(e -> triggerFormChange());

        usersLayout.addComponent(grid);
        addComponent(usersLayout);
    }

    private void addUserToProject(User user) {
        userToAdd.setSelectedItem(null);
        usersInProject.add(user);
        usersToAdd.add(user);
        usersToRemove.remove(user);
        loadUsersGrid();
    }

    private void loadUsersGrid() {
        grid.setItems(usersInProject);
    }

    private boolean isUserAlreadyInProject(User user) {
        return usersInProject.contains(user);
    }

    private void triggerFormChange() {
        getSaveBtn().setEnabled(getBinder().isValid());
    }

}
