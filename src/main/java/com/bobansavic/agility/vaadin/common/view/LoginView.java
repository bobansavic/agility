package com.bobansavic.agility.vaadin.common.view;

import com.bobansavic.agility.model.LoginData;
import com.bobansavic.agility.service.UserService;
import com.bobansavic.agility.vaadin.ui.component.FormWindow;
import com.bobansavic.agility.vaadin.ui.utils.UiUtils;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.AuthenticationException;

import javax.annotation.PostConstruct;

@SpringView
@UIScope
public class LoginView extends VerticalLayout implements View {
    private final static Logger LOGGER = LoggerFactory.getLogger(LoginView.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationContext applicationContext;

    private TextField username;
    private PasswordField password;

    private Button loginButton;
    private Button registerButton;

    private BeanValidationBinder<LoginData> binder;

    @PostConstruct
    public void postConstruct() {
        setSizeFull();

        VerticalLayout holderLayout = new VerticalLayout();
        holderLayout.setSizeUndefined();

        Component title = title();
        holderLayout.addComponent(title);
        holderLayout.setComponentAlignment(title, Alignment.BOTTOM_CENTER);

        VerticalLayout form = new VerticalLayout();
        form.addStyleName("login-panel");

        username = new TextField("Username");
        username.setIcon(VaadinIcons.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        username.focus();

        password = new PasswordField("Password");
        password.setIcon(VaadinIcons.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        loginButton = new Button("Log in");
        loginButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        loginButton.setEnabled(false);
        loginButton.addClickListener(e -> {
            LoginData data = new LoginData();
            if (binder.writeBeanIfValid(data)) {
                try {
                    userService.login(data);
                    VaadinService.reinitializeSession(VaadinService.getCurrentRequest());

                    UI.getCurrent().getPage().setLocation("");
                } catch (AuthenticationException ae) {
                    LOGGER.info("Login failed. Cause: {}", ae.getMessage());
                    showErrorNotification("Wrong username and/or password.");
                }
            }
        });

        form.addComponents(username, password, loginButton);
        form.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);

        registerButton = new Button("Register");
        registerButton.setWidth(200, Unit.PIXELS);
        registerButton.addClickListener(e -> register());
        holderLayout.addComponents(form, registerButton);
        holderLayout.setComponentAlignment(registerButton, Alignment.MIDDLE_CENTER);

        addComponent(holderLayout);
        setComponentAlignment(holderLayout, Alignment.MIDDLE_CENTER);

        binder = new BeanValidationBinder<>(LoginData.class);
        binder.bindInstanceFields(this);
        binder.addStatusChangeListener(e -> loginButton.setEnabled(e.getBinder().isValid() && e.getBinder().hasChanges()));
    }

    private void register() {
        RegisterView formView = applicationContext.getBean(RegisterView.class);
        formView.init();
        FormWindow window = new FormWindow("Registration");
        window.setFormView(formView);
        window.setWidth(30, Unit.PERCENTAGE);
        window.setHeight(40, Unit.PERCENTAGE);
        Button formRegisterButton = formView.getRegisterButton();
        window.addFooterBtn(formRegisterButton);
        formRegisterButton.addClickListener(e -> {
//            if (formView.getBinder().writeBeanIfValid(formView.getActiveStore())) {
                try {
//                    formView.register();
                    UiUtils.showSuccessNotification("Registration successful!");
                    window.close();
                } catch (Exception ex) {
                    LOGGER.error("Error saving user: {}", ex.getMessage());
                    UiUtils.showErrorNotification("Failed to save user!");
                }
//            }
        });
        UI.getCurrent().addWindow(window);
    }

    private Component title() {
        Label l = new Label("Welcome to AGILITY", ContentMode.HTML);
        l.setSizeUndefined();
        l.addStyleName(ValoTheme.LABEL_H2);
        l.addStyleName(ValoTheme.LABEL_LIGHT);
        return l;
    }

    private void showErrorNotification(String errorDescription) {
        Notification notification = new Notification("Error", errorDescription, Notification.Type.HUMANIZED_MESSAGE);
        notification.setDelayMsec(-1);
        notification.setIcon(VaadinIcons.EXCLAMATION_CIRCLE);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setHtmlContentAllowed(true);
        notification.show(UI.getCurrent().getPage());
    }
}
