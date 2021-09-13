package com.bobansavic.agility.vaadin.common.view;

import com.bobansavic.agility.model.User;
import com.bobansavic.agility.model.UserRoleType;
import com.bobansavic.agility.service.UserRoleService;
import com.bobansavic.agility.service.UserService;
import com.bobansavic.agility.vaadin.ui.utils.UiUtils;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@ViewScope
@SpringView
public class EditUserView extends VerticalLayout implements View {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService roleService;

    private TextField username;
    private TextField firstName;
    private TextField lastName;
    private TextField email;
    private PasswordField password;
    private ComboBox<UserRoleType> userRole;

    private boolean edit;
    private User activeStore;
    private Button saveBtn;

    private BeanValidationBinder<User> binder;

    @PostConstruct
    public void postConstruct() {
        setWidth(100, Unit.PERCENTAGE);
        setHeightUndefined();
        setMargin(false);

        FormLayout form = UiUtils.getFormLayout();

        username = new TextField("Username");
        firstName = new TextField("First name");
        lastName = new TextField("Last name");
        email = new TextField("Email");
        password = new PasswordField("Password");
        userRole = new ComboBox<>("User role");
        userRole.setItems(UserRoleType.values());
        userRole.setTextInputAllowed(false);


        form.addComponents(username, firstName, lastName, email, password, userRole);
        addComponent(form);
    }

    public void init(User user) {
        if (user == null) {
            activeStore = new User();
            userRole.setSelectedItem(UserRoleType.forValue("ROLE_USER"));

        } else {
            username.setValue(user.getUsername());
            firstName.setValue(user.getFirstName());
            lastName.setValue(user.getLastName());
            email.setValue(user.getEmail());
            password.setValue(user.getPassword());
            userRole.setSelectedItem(UserRoleType.forValue(user.getRole().getName()));
            activeStore = user;
        }
        getBinder().readBean(user);
    }

    public void save() {
        activeStore.setUsername(username.getValue());
        activeStore.setFirstName(firstName.getValue());
        activeStore.setLastName(lastName.getValue());
        activeStore.setEmail(email.getValue());
        activeStore.setPassword(password.getValue());
        activeStore.setRole(roleService.findByName(userRole.getValue().getValue()));
        try {
            userService.save(activeStore);
        } catch (Exception e) {
            UiUtils.showErrorNotification("A user with that username or email already exists!");
        }
    }

    public Button getSaveBtn() {
        if (saveBtn == null) {
            saveBtn = UiUtils.createSaveButton();
        }
        return saveBtn;
    }

    public BeanValidationBinder<User> getBinder() {
        if (binder == null) {
            binder = new BeanValidationBinder<>(User.class);
            binder.bindInstanceFields(this);
            binder.addStatusChangeListener(e -> getSaveBtn().setEnabled(e.getBinder().isValid() && e.getBinder().hasChanges()));
        }
        return binder;
    }

    public User getActiveStore() {
        return activeStore;
    }
}
