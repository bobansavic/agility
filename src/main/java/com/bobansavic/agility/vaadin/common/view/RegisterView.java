package com.bobansavic.agility.vaadin.common.view;

import com.bobansavic.agility.model.User;
import com.bobansavic.agility.model.UserRole;
import com.bobansavic.agility.model.UserRoleType;
import com.bobansavic.agility.service.UserRoleService;
import com.bobansavic.agility.service.UserService;
import com.bobansavic.agility.vaadin.ui.utils.UiUtils;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Transactional
@ViewScope
@SpringView
public class RegisterView extends VerticalLayout implements View {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService roleService;

    private TextField firstName;
    private TextField lastName;
    private TextField username;
    private TextField email;
    private PasswordField password;
    private Button registerBtn;

    @PostConstruct
    public void postConstruct() {
        setWidthFull();
        setHeightUndefined();
        setMargin(false);
        FormLayout form = UiUtils.getFormLayout();

        firstName = new TextField("First name");
        lastName = new TextField("Last name");
        username = new TextField("Username");
        email = new TextField("E-mail");
        password = new PasswordField("Password");
        registerBtn = new Button("Register");
        registerBtn.addClickListener(e -> {
            User user = new User();
            user.setFirstName(firstName.getValue());
            user.setLastName(lastName.getValue());
            user.setUsername(username.getValue());
            user.setEmail(email.getValue());
            user.setPassword(password.getValue());
            UserRole userRole = roleService.findByName(UserRoleType.USER.getValue());
            user.setRole(userRole);
            userService.save(user);
            UiUtils.showErrorNotification("Registration successful!");
        });

        form.addComponents(firstName, lastName, username, email, password);
        addComponent(form);
    }

    public void init() {
        cleanUpForm();
    }

    private void cleanUpForm() {
        firstName.clear();
        lastName.clear();
        email.clear();
        password.clear();
    }

    public Button getRegisterButton() {
        return registerBtn;
    }
}
