package com.bobansavic.agility.web.common.views;

import com.bobansavic.agility.model.User;
import com.bobansavic.agility.service.UserService;
import com.bobansavic.agility.web.common.Menu;
import com.bobansavic.agility.web.common.UiPart;
import com.bobansavic.agility.web.ui.component.ControlButtonLayout;
import com.bobansavic.agility.web.ui.component.FormWindow;
import com.bobansavic.agility.web.ui.utils.UiUtils;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

@SpringView
@Menu(icon = VaadinIcons.USER, name = "Users", order = 100, availiableFor = {UiPart.ADMIN, UiPart.PM})
public class UserManagementView extends VerticalLayout implements View {

    private final static Logger log = LoggerFactory.getLogger(UserManagementView.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationContext applicationContext;

    private Button createBtn;
    private Grid<User> grid;

    @PostConstruct
    public void postConstruct() {
        ControlButtonLayout controls = new ControlButtonLayout();

        createBtn = new Button("New user");
        createBtn.addClickListener(e -> {
            grid.deselectAll();
            edit(null);
        });
        controls.addLeft(createBtn);

        controls.setDeleteAction(item -> {
            Set<User> selectedItems = grid.getSelectedItems();
            if (!selectedItems.isEmpty()) {
                for (User user : selectedItems) {
                    if (!userService.deleteUser(user)) {
                        UiUtils.showErrorNotification("Failed to delete user with id " + user.getId());
                    }
                }
                loadGridItems();
            }
        });

        addComponent(controls);

        grid = new Grid<>(User.class);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setColumns("firstName", "lastName", "email");
        for (Grid.Column<User, ?> column : grid.getColumns()) {
            column.setResizable(false);
        }
        grid.setSizeFull();
        loadGridItems();
        grid.addSelectionListener(e -> {
            int size = e.getAllSelectedItems().size();
            boolean empty = e.getAllSelectedItems().isEmpty();
            controls.setDeleteEnabled(!empty);
        });
        grid.addItemClickListener(e -> {
            grid.deselectAll();
            grid.select(e.getItem());
            edit(e.getItem());
        });

        addComponentsAndExpand(grid);
    }

    private void edit(User item) {
        String title;
        if (item != null) {
            title = "Edit user";
        } else {
            title = "Create new user";
        }

        EditUserView formView = applicationContext.getBean(EditUserView.class);
        formView.init(item);
        FormWindow window = new FormWindow(title);
        window.setFormView(formView);
        window.setWidth(50, Unit.PERCENTAGE);
        window.setHeight(80, Unit.PERCENTAGE);
        Button saveBtn = formView.getSaveBtn();
        window.addFooterBtn(saveBtn);
        saveBtn.addClickListener(e -> {
            if (formView.getBinder().writeBeanIfValid(formView.getActiveStore())) {
                try {
                    formView.save();
                    UiUtils.showSuccessNotification("Successfully saved user!");
                    window.close();
                    loadGridItems();
                } catch (Exception ex) {
                    log.error("Error saving user: {}", ex.getMessage());
                    UiUtils.showErrorNotification("Failed to save user!");
                }
            }


        });
        UI.getCurrent().addWindow(window);
    }

    private void loadGridItems() {
        List<User> allUsers = userService.getAllUsers();
        grid.setItems(allUsers);
    }
}
