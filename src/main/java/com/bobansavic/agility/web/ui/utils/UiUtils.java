package com.bobansavic.agility.web.ui.utils;

import com.bobansavic.agility.web.common.views.KanbanBoardView;
import com.bobansavic.agility.web.common.views.LoginView;
import com.bobansavic.agility.web.common.views.ProjectManagementView;
import com.bobansavic.agility.web.common.views.TicketManagementView;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.Position;
import com.vaadin.spring.internal.Conventions;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.util.ClassUtils;

public abstract class UiUtils {

    public static String getViewName(Class<? extends View> viewClass) {
        Class<?> realBeanClass = ClassUtils.getUserClass(viewClass);
        String viewName = realBeanClass.getSimpleName().replaceFirst("View$", "");
        return Conventions.upperCamelToLowerHyphen(viewName);
    }

    public static String getLoginViewName() {
        return getViewName(LoginView.class);
    }

    public static String getKanbanBoardViewName() {
        return getViewName(KanbanBoardView.class);
    }

    public static String getTicketManagementViewName() {
        return getViewName(TicketManagementView.class);
    }

    public static String getProjectManagementViewName() {
        return getViewName(ProjectManagementView.class);
    }

    public static FormLayout getFormLayout() {
        FormLayout form = new FormLayout();
        form.setSizeFull();
        form.setMargin(false);
        form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        return form;
    }

    public static Button createSaveButton() {
        Button saveBtn = new Button("Save");
        saveBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        saveBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        saveBtn.setClickShortcut(ShortcutAction.KeyCode.S, ShortcutAction.ModifierKey.CTRL);
        saveBtn.setEnabled(false);
        return saveBtn;
    }

    public static Button createRefreshGridButton() {
        Button b = new Button(VaadinIcons.REFRESH);
        b.setDescription("Refresh");
        return b;
    }

    public static void showSuccessNotification(String successDescription) {
        Notification notification = new Notification(successDescription, Notification.Type.HUMANIZED_MESSAGE);
        notification.setStyleName("success");
        notification.setPosition(Position.BOTTOM_RIGHT);
        notification.setHtmlContentAllowed(true);
        notification.show(UI.getCurrent().getPage());
    }

    public static void showErrorNotification(String errorDescription) {
        Notification notification = new Notification(errorDescription, Notification.Type.HUMANIZED_MESSAGE);
        notification.setDelayMsec(-1);
        notification.setIcon(VaadinIcons.EXCLAMATION_CIRCLE);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setHtmlContentAllowed(true);
        notification.show(UI.getCurrent().getPage());
    }
}
