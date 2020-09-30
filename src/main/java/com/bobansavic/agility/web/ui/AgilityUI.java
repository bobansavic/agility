package com.bobansavic.agility.web.ui;

import com.bobansavic.agility.model.User;
import com.bobansavic.agility.service.ProjectService;
import com.bobansavic.agility.service.TaskService;
import com.bobansavic.agility.service.UserService;
import com.bobansavic.agility.web.common.SecurityUtils;
import com.bobansavic.agility.web.common.model.MenuItem;
import com.bobansavic.agility.web.common.service.MenuService;
import com.bobansavic.agility.web.common.views.AccessDeniedView;
import com.bobansavic.agility.web.common.views.ErrorView;
import com.bobansavic.agility.web.common.views.TicketManagementView;
import com.bobansavic.agility.web.ui.utils.MenuLayout;
import com.bobansavic.agility.web.ui.utils.UiUtils;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;

import java.util.Locale;

@Viewport("width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no") //check
@SpringUI
@Theme("agility")
public class AgilityUI extends UI {

    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProjectService projectService;

    @Autowired
    private ApplicationContext applicationContext;

    private final SpringViewProvider viewProvider;
    private final ErrorView errorView;
    private final MenuService menuService;
    private final SpringNavigator navigator;

    private MenuLayout root;

    public AgilityUI(SpringViewProvider viewProvider, ErrorView errorView, MenuService menuService, SpringNavigator navigator) {
        this.viewProvider = viewProvider;
        this.errorView = errorView;
        this.menuService = menuService;
        this.navigator = navigator;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Locale locale = LocaleContextHolder.getLocale();
        setLocale(locale);

        getPage().setTitle("Agility");

        Responsive.makeResponsive(this);

        if (SecurityUtils.isLoggedIn()) {
            User currentUser = SecurityUtils.getCurrentUser();

            this.root = new MenuLayout();
            this.root.setMenuId("agility-menu");
            setErrorHandler(this::handleError);
            navigator.init(this, root.getContentContainer());

            VerticalLayout vl = new VerticalLayout();
            vl.setSizeFull();
            vl.setMargin(false);
            vl.setSpacing(false);
            Component header = buildHeader();
            header.setHeight(40, Unit.PIXELS);
            vl.addComponent(header);

            buildMenu();
            vl.addComponentsAndExpand(root);

            setContent(vl);
            addStyleName(ValoTheme.UI_WITH_MENU);
        } else {
            // show login view or pw reset
            navigator.init(this, this);
            navigator.navigateTo(UiUtils.getLoginViewName());
        }
        viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
        navigator.addProvider(viewProvider);
        navigator.setErrorProvider(viewProvider);
        navigator.setErrorView(errorView);

        String fragment = Page.getCurrent().getUriFragment();
        boolean loggedIn = SecurityUtils.isLoggedIn();
        if (StringUtils.isBlank(fragment)) {
            if (!loggedIn) {
                navigator.navigateTo(UiUtils.getLoginViewName());
            } else {
                navigator.navigateTo("welcome");
            }
        }
    }

    private void buildMenu() {
        for (MenuItem mi : this.menuService.getAllowedMenuItems(UI.getCurrent())) {
            Button b = new Button();
            b.setIcon(mi.getIcon());
            if (StringUtils.isNotBlank(mi.getTooltip())) {
                b.setDescription(mi.getTooltip());
            }
            b.setCaption(mi.getName());
            if (mi.getViewName().equals(UiUtils.getTicketManagementViewName())) {
                b.addClickListener(e -> {
                    navigator.navigateTo(UiUtils.getKanbanBoardViewName());
                    TicketManagementView ticketManagementView = new TicketManagementView();
                    ticketManagementView.edit(null, userService, taskService, projectService);
                });
            } else {
                b.addClickListener(e -> navigator.navigateTo(mi.getViewName()));
            }
            root.addMenuButton(b);
        }
    }

    private Component buildHeader() {
        // header
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("agility-header");
        header.setSizeFull();
        Component headerTitle = buildHeaderTitle();
        header.addComponent(headerTitle);
        header.setExpandRatio(headerTitle, 1);
        header.setComponentAlignment(headerTitle, Alignment.MIDDLE_LEFT);
        Component headerControls = buildHeaderControls();
        header.addComponent(headerControls);
        header.setComponentAlignment(headerControls, Alignment.MIDDLE_RIGHT);
        return header;
    }

    private Component buildHeaderTitle() {
        HorizontalLayout h = new HorizontalLayout();
		h.setSizeUndefined();

        Label title = new Label();
        title.addStyleName("agility-title");
        title.setContentMode(ContentMode.HTML);

        String titleContent = "<span><strong>Agility</strong> | ";
        if (SecurityUtils.isProjectView()) {
            titleContent += "</span><span style=\"color:#2071d4;font-weight:bold\">" + SecurityUtils.getCurrentProjectTitle() + "</span>";
        } else {
            titleContent += "Tell your agile story</span>";
        }
        title.setValue(titleContent);
        h.addComponent(title);
        return h;
    }

    private Component buildHeaderControls() {
        HorizontalLayout h = new HorizontalLayout();
        h.setSpacing(false);

        Button userIcon = new Button();
        userIcon.setIcon(VaadinIcons.USER);
        userIcon.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        userIcon.setEnabled(false);
        h.addComponent(userIcon);
        Label username = new Label();
        username.setContentMode(ContentMode.HTML);
        User currentUser = SecurityUtils.getCurrentUser();
        username.setValue(currentUser.getEmail());
        h.addComponent(username);
        h.setComponentAlignment(username, Alignment.MIDDLE_RIGHT);

        Button logoutBtn = new Button(VaadinIcons.POWER_OFF);
        logoutBtn.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        logoutBtn.setDescription("Logout");
        logoutBtn.addClickListener(e -> {
            if (SecurityUtils.isProjectView()) {
                SecurityUtils.setProjectView(false);
                SecurityUtils.setCurrentProjectTitle(null);
                UI.getCurrent().getPage().setLocation("");
            } else {
                logout();
            }
        });
        h.addComponent(logoutBtn);
        return h;

    }

    private void handleError(com.vaadin.server.ErrorEvent event) {
        Throwable t = DefaultErrorHandler.findRelevantThrowable(event.getThrowable());
        if (t instanceof AccessDeniedException) {
            Notification.show("Access denied!", Notification.Type.WARNING_MESSAGE);
        } else {
            DefaultErrorHandler.doDefault(event);
        }
    }

    private void logout() {
        navigator.navigateTo(UiUtils.getLoginViewName());
        getSession().close();
        UI.getCurrent().getPage().setLocation("");
    }

}
